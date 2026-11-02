package com.example.player

import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.audio.AudioProcessor.AudioFormat
import androidx.media3.common.audio.BaseAudioProcessor
import androidx.media3.common.util.UnstableApi
import com.example.model.AudioDeArtifactMode
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.min

/**
 * Real-time PCM AudioProcessor for:
 * 1. Adjusting audio synchronization/delay in range [-5000, +5000] ms.
 * 2. Restoring heavily compressed low-bitrate audio tracks (AAC / MP3 / Opus 64–128 kbps)
 *    via a 3-stage DSP pipeline:
 *    - Dynamic High-Frequency De-Ringing (suppresses metallic quantization mosquito noise & pre-echo)
 *    - Warm Mid-Band Harmonic Presence Exciter (restores natural voiceover overtones lost to lowpass cutoffs)
 *    - Cubic Soft-Knee De-Clipper (eliminates harsh inter-sample clipping distortion in fandub tracks)
 */
@OptIn(UnstableApi::class)
class AudioDelayProcessor : BaseAudioProcessor() {

    companion object {
        const val MIN_DELAY_MS = -5000L
        const val MAX_DELAY_MS = 5000L
        const val LOW_BITRATE_THRESHOLD_KBPS = 132
        private const val MAX_DSP_CHANNELS = 8

        fun clampDelayMs(delayMs: Long): Long = delayMs.coerceIn(MIN_DELAY_MS, MAX_DELAY_MS)

        fun formatDelayLabel(delayMs: Long, isEnglish: Boolean = false): String {
            val unit = if (isEnglish) "ms" else "мс"
            return when {
                delayMs > 0L -> "+${delayMs} $unit"
                delayMs < 0L -> "${delayMs} $unit"
                else -> "0 $unit"
            }
        }
    }

    @Volatile
    var delayMs: Long = 0L
        private set

    @Volatile
    var useClockShiftForNegativeDelay: Boolean = true

    @Volatile
    var deArtifactMode: AudioDeArtifactMode = AudioDeArtifactMode.STANDARD

    @Volatile
    var autoDeArtifactLowBitrate: Boolean = true

    @Volatile
    var isLowBitrateStream: Boolean = true

    val isDeArtifactCurrentlyActive: Boolean
        get() = deArtifactMode != AudioDeArtifactMode.OFF &&
            (!autoDeArtifactLowBitrate || isLowBitrateStream)

    val mediaClockOffsetUs: Long
        get() {
            val d = delayMs
            return if (useClockShiftForNegativeDelay && d < 0L) d * 1000L else 0L
        }

    private var appliedDelayMs: Long = 0L
    private var pendingSilenceBytes: Long = 0L
    private var pendingSkipBytes: Long = 0L
    private val zeroBlock = ByteArray(4096)

    // Per-channel DSP filter states for low-bitrate compression artifact removal
    private val lpFastState = FloatArray(MAX_DSP_CHANNELS)
    private val lpSlowState = FloatArray(MAX_DSP_CHANNELS)
    private val envState = FloatArray(MAX_DSP_CHANNELS)

    @Synchronized
    fun setDelayMs(newDelayMs: Long) {
        val clamped = clampDelayMs(newDelayMs)
        val prevDelayMs = appliedDelayMs
        delayMs = clamped
        appliedDelayMs = clamped
        if (inputAudioFormat != AudioFormat.NOT_SET && clamped != prevDelayMs) {
            if (useClockShiftForNegativeDelay) {
                val prevPositiveMs = prevDelayMs.coerceAtLeast(0L)
                val newPositiveMs = clamped.coerceAtLeast(0L)
                val positiveDeltaMs = newPositiveMs - prevPositiveMs
                if (positiveDeltaMs != 0L) {
                    applyDeltaBytes(positiveDeltaMs)
                } else if (clamped <= 0L) {
                    pendingSilenceBytes = 0L
                    pendingSkipBytes = 0L
                }
            } else {
                applyDeltaBytes(clamped - prevDelayMs)
            }
        }
    }

    @Synchronized
    fun forceResyncFlush() {
        recalculateFromScratch()
    }

    override fun onConfigure(inputAudioFormat: AudioFormat): AudioFormat {
        resetDspStates()
        return when (inputAudioFormat.encoding) {
            C.ENCODING_PCM_16BIT,
            C.ENCODING_PCM_8BIT,
            C.ENCODING_PCM_24BIT,
            C.ENCODING_PCM_32BIT,
            C.ENCODING_PCM_FLOAT -> inputAudioFormat
            else -> AudioFormat.NOT_SET
        }
    }

    @Synchronized
    override fun queueInput(inputBuffer: ByteBuffer) {
        var remaining = inputBuffer.remaining()
        if (remaining == 0) return

        val frameSize = inputAudioFormat.bytesPerFrame.coerceAtLeast(1)

        // 1. If we have pending skip bytes (negative delay adjustment), drop frames cleanly aligned to frameSize
        if (pendingSkipBytes > 0L) {
            val bytesToSkip = min(remaining.toLong(), pendingSkipBytes).toInt()
            val alignedSkip = (bytesToSkip / frameSize) * frameSize
            if (alignedSkip > 0) {
                inputBuffer.position(inputBuffer.position() + alignedSkip)
                pendingSkipBytes = (pendingSkipBytes - alignedSkip).coerceAtLeast(0L)
                remaining = inputBuffer.remaining()
                if (remaining == 0) return
            }
        }

        // 2. If we have pending silence bytes (positive delay adjustment), prepend zeroed PCM frames
        val silenceChunk = if (pendingSilenceBytes > 0L) {
            val maxSilenceThisTurn = min(pendingSilenceBytes, 32768L).toInt()
            ((maxSilenceThisTurn / frameSize) * frameSize).coerceAtLeast(0)
        } else {
            0
        }

        val outBuffer = replaceOutputBuffer(silenceChunk + remaining)
        outBuffer.order(ByteOrder.nativeOrder())
        if (silenceChunk > 0) {
            var left = silenceChunk
            while (left > 0) {
                val step = min(left, zeroBlock.size)
                outBuffer.put(zeroBlock, 0, step)
                left -= step
            }
            pendingSilenceBytes = (pendingSilenceBytes - silenceChunk).coerceAtLeast(0L)
        }

        val mode = deArtifactMode
        val runDeArtifact = isDeArtifactCurrentlyActive &&
            inputAudioFormat.encoding == C.ENCODING_PCM_16BIT &&
            remaining >= 2

        if (runDeArtifact) {
            processPcm16DeArtifact(
                inputBuffer = inputBuffer,
                outBuffer = outBuffer,
                byteCount = remaining,
                channelCount = inputAudioFormat.channelCount.coerceIn(1, MAX_DSP_CHANNELS),
                mode = mode
            )
        } else {
            outBuffer.put(inputBuffer)
        }
        outBuffer.flip()
    }

    private fun processPcm16DeArtifact(
        inputBuffer: ByteBuffer,
        outBuffer: ByteBuffer,
        byteCount: Int,
        channelCount: Int,
        mode: AudioDeArtifactMode
    ) {
        val deRingStrength: Float
        val harmonicGain: Float
        val alphaFast: Float
        when (mode) {
            AudioDeArtifactMode.OFF -> {
                outBuffer.put(inputBuffer)
                return
            }
            AudioDeArtifactMode.SOFT -> {
                deRingStrength = 0.28f
                harmonicGain = 0.05f
                alphaFast = 0.48f
            }
            AudioDeArtifactMode.STANDARD -> {
                deRingStrength = 0.46f
                harmonicGain = 0.09f
                alphaFast = 0.40f
            }
            AudioDeArtifactMode.AGGRESSIVE -> {
                deRingStrength = 0.65f
                harmonicGain = 0.14f
                alphaFast = 0.33f
            }
        }
        val alphaSlow = 0.18f
        val sampleCount = byteCount / 2
        var ch = 0

        val origOrder = inputBuffer.order()
        inputBuffer.order(ByteOrder.nativeOrder())
        for (i in 0 until sampleCount) {
            val rawShort = inputBuffer.short
            val x = rawShort * (1.0f / 32768.0f)

            var fast = lpFastState[ch]
            var slow = lpSlowState[ch]
            var env = envState[ch]

            fast += alphaFast * (x - fast)
            slow += alphaSlow * (fast - slow)
            val absX = abs(x)
            env = if (absX > env) {
                0.85f * env + 0.15f * absX
            } else {
                0.985f * env + 0.015f * absX
            }

            lpFastState[ch] = fast
            lpSlowState[ch] = slow
            envState[ch] = env

            // High-frequency residual (> ~4 kHz) where AAC/MP3 quantization ringing lives
            val hf = x - fast
            val absHf = abs(hf)
            // Adaptive gate: stronger suppression when HF hash exceeds natural ratio to envelope
            val noiseRatio = if (env > 0.002f) (absHf / (env + 0.004f)).coerceIn(0f, 1.5f) else 1.0f
            val dynamicSuppression = (deRingStrength * (0.55f + 0.45f * noiseRatio)).coerceIn(0f, 0.82f)
            val cleanedHf = hf * (1.0f - dynamicSuppression)

            // Mid-band vocal presence (1.5 kHz – 4.2 kHz) soft harmonic restoration
            val midBand = fast - slow
            val warmHarmonic = midBand * (1.0f - abs(midBand)) * harmonicGain

            var y = fast + cleanedHf + warmHarmonic

            // Cubic soft-knee de-clipper for harsh fandub peaks above 0.82 FS
            val absY = abs(y)
            if (absY > 0.82f) {
                val sign = if (y >= 0f) 1f else -1f
                val excess = (absY - 0.82f) / 0.18f
                val compressed = 0.82f + 0.175f * (excess / (1.0f + 0.85f * excess))
                y = sign * compressed
            }

            val outInt = (y * 32767.0f).toInt().coerceIn(-32768, 32767)
            outBuffer.putShort(outInt.toShort())

            ch++
            if (ch >= channelCount) ch = 0
        }
        if (byteCount % 2 != 0 && inputBuffer.hasRemaining()) {
            outBuffer.put(inputBuffer.get())
        }
        inputBuffer.order(origOrder)
    }

    private fun resetDspStates() {
        lpFastState.fill(0f)
        lpSlowState.fill(0f)
        envState.fill(0f)
    }

    @Synchronized
    override fun onFlush() {
        resetDspStates()
        recalculateFromScratch()
    }

    @Synchronized
    override fun onReset() {
        resetDspStates()
        pendingSilenceBytes = 0L
        pendingSkipBytes = 0L
        appliedDelayMs = delayMs
    }

    private fun recalculateFromScratch() {
        appliedDelayMs = delayMs
        if (inputAudioFormat == AudioFormat.NOT_SET || inputAudioFormat.sampleRate <= 0) {
            pendingSilenceBytes = 0L
            pendingSkipBytes = 0L
            return
        }
        val bytesForDelay = msToAlignedBytes(abs(delayMs))
        if (delayMs > 0L) {
            pendingSilenceBytes = bytesForDelay
            pendingSkipBytes = 0L
        } else if (delayMs < 0L && !useClockShiftForNegativeDelay) {
            pendingSilenceBytes = 0L
            pendingSkipBytes = bytesForDelay
        } else {
            pendingSilenceBytes = 0L
            pendingSkipBytes = 0L
        }
    }

    private fun applyDeltaBytes(deltaMs: Long) {
        val deltaBytes = msToAlignedBytes(abs(deltaMs))
        if (deltaMs > 0L) {
            if (pendingSkipBytes >= deltaBytes) {
                pendingSkipBytes -= deltaBytes
            } else {
                val leftover = deltaBytes - pendingSkipBytes
                pendingSkipBytes = 0L
                pendingSilenceBytes += leftover
            }
        } else if (deltaMs < 0L) {
            if (pendingSilenceBytes >= deltaBytes) {
                pendingSilenceBytes -= deltaBytes
            } else {
                val leftover = deltaBytes - pendingSilenceBytes
                pendingSilenceBytes = 0L
                pendingSkipBytes += leftover
            }
        }
    }

    private fun msToAlignedBytes(ms: Long): Long {
        if (inputAudioFormat == AudioFormat.NOT_SET || inputAudioFormat.sampleRate <= 0) return 0L
        val frameSize = inputAudioFormat.bytesPerFrame.coerceAtLeast(1).toLong()
        val frames = (ms * inputAudioFormat.sampleRate.toLong()) / 1000L
        return frames * frameSize
    }
}

