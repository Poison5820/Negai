package com.example.player

import android.app.Activity
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import androidx.compose.runtime.Immutable
import com.example.model.FrameInterpolationMode
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@Immutable
data class DisplayRefreshInfo(
    val currentHz: Float = 60f,
    val minSupportedHz: Float = 60f,
    val maxSupportedHz: Float = 60f,
    val supportedModesHz: List<Int> = listOf(60)
) {
    val supportsHighRefreshRate: Boolean
        get() = maxSupportedHz >= 85f

    val isLtpoCapable: Boolean
        get() = minSupportedHz <= 48.5f ||
            supportedModesHz.size >= 3 ||
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && maxSupportedHz >= 90f)
}

@Immutable
data class RefreshRatePlan(
    val sourceFps: Float = 24f,
    val playbackSpeed: Float = 1.0f,
    val effectiveVideoFps: Float = 24f,
    val targetDisplayHz: Float = 60f,
    val targetRenderFps: Float = 60f,
    val surfaceFrameRateHz: Float = 0f,
    val frameRateCompatibility: Int = Surface.FRAME_RATE_COMPATIBILITY_DEFAULT,
    val preferSystemIdleWindowMode: Boolean = false,
    val useContinuousVsync: Boolean = false,
    val memcActive: Boolean = false,
    val memcAutoBypassedBySpeed: Boolean = false,
    val isLtpoSavingActive: Boolean = false,
    val hudBadgeText: String = "60Гц",
    val statusSummary: String = "Стандартный режим 60 Гц"
)

object DisplayRefreshRateController {

    fun queryDisplayRefreshRates(context: Context): DisplayRefreshInfo {
        val display = resolveDisplay(context) ?: return DisplayRefreshInfo()
        val currentMode = display.mode
        val supported = display.supportedModes.orEmpty()
        if (supported.isEmpty()) {
            val hz = currentMode?.refreshRate?.coerceAtLeast(30f) ?: 60f
            return DisplayRefreshInfo(
                currentHz = hz,
                minSupportedHz = hz,
                maxSupportedHz = hz,
                supportedModesHz = listOf(hz.roundToInt())
            )
        }

        val nativeWidth = currentMode?.physicalWidth ?: supported.maxOf { it.physicalWidth }
        val nativeHeight = currentMode?.physicalHeight ?: supported.maxOf { it.physicalHeight }
        val sameResModes = supported.filter {
            it.physicalWidth == nativeWidth && it.physicalHeight == nativeHeight
        }.ifEmpty { supported.toList() }

        val minHz = sameResModes.minOfOrNull { it.refreshRate } ?: currentMode?.refreshRate ?: 60f
        val maxHz = sameResModes.maxOfOrNull { it.refreshRate } ?: currentMode?.refreshRate ?: 60f
        val distinctHz = sameResModes
            .map { it.refreshRate.roundToInt() }
            .distinct()
            .sorted()

        return DisplayRefreshInfo(
            currentHz = currentMode?.refreshRate ?: 60f,
            minSupportedHz = minHz,
            maxSupportedHz = maxHz,
            supportedModesHz = distinctHz.ifEmpty { listOf(60) }
        )
    }

    /**
     * Computes an energy-optimal LTPO / multi-rate refresh & MEMC plan taking into account:
     * 1. Source video frame rate (23.976 / 24 / 25 / 29.97 / 30 / 50 / 59.94 / 60 fps)
     * 2. Active playback speed / 2x long-press acceleration (`effectiveVideoFps = sourceFps * speed`)
     * 3. Selected frame interpolation mode (`OFF`, `CINEMA`, `SMOOTH_60`, `MAX_FPS`, `ANIME`)
     * 4. Display supported modes & LTPO dynamic idle drop on pause.
     */
    fun computeOptimalRefreshPlan(
        displayInfo: DisplayRefreshInfo,
        isPlayerActive: Boolean,
        isPlaying: Boolean,
        sourceVideoFps: Float,
        playbackSpeed: Float,
        highRefreshRateEnabled: Boolean,
        ltpoAdaptiveEnabled: Boolean,
        interpolationMode: FrameInterpolationMode,
        isControlsOrUiActive: Boolean = false
    ): RefreshRatePlan {
        val maxPanelHz = displayInfo.maxSupportedHz.coerceAtLeast(60f)
        val allowedCapHz = if (highRefreshRateEnabled) maxPanelHz else 60f.coerceAtMost(maxPanelHz)
        val candidateModes = displayInfo.supportedModesHz
            .map { it.toFloat() }
            .filter { it <= allowedCapHz + 0.8f }
            .sorted()
            .ifEmpty { listOf(allowedCapHz) }

        val normalizedSrcFps = normalizeSourceFps(sourceVideoFps)
        val clampedSpeed = playbackSpeed.coerceIn(0.25f, 3.0f)
        val effectiveFps = (normalizedSrcFps * clampedSpeed).coerceIn(6f, 240f)

        // 1. Outside player: UI navigation (Folders, Recent, Torrents, Settings)
        // Must ALWAYS run at maximum display refresh rate (90 / 120 / 144 Hz) when highRefreshRateEnabled is true!
        if (!isPlayerActive) {
            val uiHz = if (highRefreshRateEnabled) allowedCapHz else 60f.coerceAtMost(maxPanelHz)
            return RefreshRatePlan(
                sourceFps = normalizedSrcFps,
                playbackSpeed = clampedSpeed,
                effectiveVideoFps = effectiveFps,
                targetDisplayHz = uiHz,
                targetRenderFps = uiHz,
                surfaceFrameRateHz = 0f,
                preferSystemIdleWindowMode = !highRefreshRateEnabled,
                useContinuousVsync = false,
                memcActive = false,
                isLtpoSavingActive = false,
                hudBadgeText = "${uiHz.roundToInt()}Гц",
                statusSummary = "Максимальная частота интерфейса ${uiHz.roundToInt()} Гц"
            )
        }

        // 2. In player WITH controls / UI active (buttons visible, sheets open, scrubbing, gestures)
        // Must immediately boost to maximum refresh rate (120 Hz) for silky-smooth animations and instant response!
        if (isControlsOrUiActive) {
            val boostHz = allowedCapHz
            return RefreshRatePlan(
                sourceFps = normalizedSrcFps,
                playbackSpeed = clampedSpeed,
                effectiveVideoFps = effectiveFps,
                targetDisplayHz = boostHz,
                targetRenderFps = boostHz,
                surfaceFrameRateHz = 0f,
                preferSystemIdleWindowMode = false,
                useContinuousVsync = false,
                memcActive = false,
                isLtpoSavingActive = false,
                hudBadgeText = "${boostHz.roundToInt()}Гц",
                statusSummary = "Управление плеером • Повышенная частота ${boostHz.roundToInt()} Гц"
            )
        }

        // 3. Player paused: drop VSync loop & let LTPO throttle panel down to minimum supported Hz
        if (!isPlaying) {
            val minPanelHz = when {
                displayInfo.minSupportedHz < 58f -> displayInfo.minSupportedHz.coerceAtLeast(1f)
                displayInfo.isLtpoCapable -> 10f
                else -> displayInfo.minSupportedHz.coerceAtMost(60f)
            }
            val idleHz = if (ltpoAdaptiveEnabled) {
                minPanelHz
            } else {
                60f.coerceAtMost(maxPanelHz)
            }
            return RefreshRatePlan(
                sourceFps = normalizedSrcFps,
                playbackSpeed = clampedSpeed,
                effectiveVideoFps = effectiveFps,
                targetDisplayHz = idleHz,
                targetRenderFps = 0f,
                surfaceFrameRateHz = if (ltpoAdaptiveEnabled) 1f else 0f,
                preferSystemIdleWindowMode = ltpoAdaptiveEnabled,
                useContinuousVsync = false,
                memcActive = false,
                isLtpoSavingActive = ltpoAdaptiveEnabled,
                hudBadgeText = if (ltpoAdaptiveEnabled) "LTPO ${idleHz.roundToInt()}Гц" else "${idleHz.roundToInt()}Гц",
                statusSummary = if (ltpoAdaptiveEnabled) {
                    "Пауза • LTPO снижает частоту до минимума (${idleHz.roundToInt()} Гц)"
                } else {
                    "Пауза • Экран удерживает ${idleHz.roundToInt()} Гц"
                }
            )
        }

        // 4. Active video playback WITH Frame Interpolation (MEMC) requested
        if (interpolationMode.isEnabled) {
            // Strict 2x frame doubling cadence: 24 fps -> 48 fps; 25 fps -> 50 fps; 30 fps -> 60 fps.
            // Strictly doubles source video fps without forcing 120 fps on 24 fps content.
            val desiredMemcTarget = when (interpolationMode) {
                FrameInterpolationMode.OFF -> effectiveFps
                else -> (effectiveFps * 2.0f).coerceIn(24f, allowedCapHz)
            }

            // Auto-bypass MEMC when accelerated video FPS already meets or exceeds target Hz!
            // Example: 60fps @ 2.0x = 120fps on 120Hz, or 30fps @ 2.0x = 60fps in SMOOTH_60 mode.
            if (effectiveFps >= desiredMemcTarget * 0.94f) {
                val nativeTargetHz = pickNativeCadenceDisplayHz(
                    effectiveFps = effectiveFps,
                    candidateModes = candidateModes,
                    allowedCapHz = allowedCapHz,
                    ltpoAdaptiveEnabled = ltpoAdaptiveEnabled,
                    isLtpoCapable = displayInfo.isLtpoCapable
                )
                val saving = ltpoAdaptiveEnabled && nativeTargetHz < maxPanelHz - 1.5f
                return RefreshRatePlan(
                    sourceFps = normalizedSrcFps,
                    playbackSpeed = clampedSpeed,
                    effectiveVideoFps = effectiveFps,
                    targetDisplayHz = nativeTargetHz,
                    targetRenderFps = effectiveFps.coerceAtMost(nativeTargetHz),
                    surfaceFrameRateHz = effectiveFps.coerceAtMost(allowedCapHz),
                    frameRateCompatibility = Surface.FRAME_RATE_COMPATIBILITY_FIXED_SOURCE,
                    preferSystemIdleWindowMode = false,
                    useContinuousVsync = false,
                    memcActive = false,
                    memcAutoBypassedBySpeed = true,
                    isLtpoSavingActive = saving,
                    hudBadgeText = buildHzBadge(nativeTargetHz, ltpoAdaptiveEnabled && saving),
                    statusSummary = "${formatFps(normalizedSrcFps)} × ${formatSpeed(clampedSpeed)} = ${formatFps(effectiveFps)} к/с → прямое совпадение ${nativeTargetHz.roundToInt()} Гц (MEMC не требуется)"
                )
            }

            val chosenDisplayHz = if (ltpoAdaptiveEnabled) {
                pickMemcLtpoDisplayHz(
                    effectiveFps = effectiveFps,
                    desiredTargetHz = desiredMemcTarget,
                    interpolationMode = interpolationMode,
                    candidateModes = candidateModes,
                    allowedCapHz = allowedCapHz
                )
            } else {
                candidateModes.firstOrNull { it >= desiredMemcTarget - 0.8f } ?: allowedCapHz
            }

            val renderFps = desiredMemcTarget.coerceAtMost(chosenDisplayHz)
            val saving = ltpoAdaptiveEnabled && chosenDisplayHz < maxPanelHz - 1.5f
            return RefreshRatePlan(
                sourceFps = normalizedSrcFps,
                playbackSpeed = clampedSpeed,
                effectiveVideoFps = effectiveFps,
                targetDisplayHz = chosenDisplayHz,
                targetRenderFps = renderFps,
                surfaceFrameRateHz = renderFps,
                frameRateCompatibility = Surface.FRAME_RATE_COMPATIBILITY_DEFAULT,
                preferSystemIdleWindowMode = false,
                useContinuousVsync = true,
                memcActive = true,
                isLtpoSavingActive = saving,
                hudBadgeText = "${renderFps.roundToInt()} к/с (2x)",
                statusSummary = "${formatFps(normalizedSrcFps)} к/с → 2x Удвоение ${renderFps.roundToInt()} к/с (${chosenDisplayHz.roundToInt()} Гц${if (saving) " LTPO Эко" else ""})"
            )
        }

        // 5. Active video playback WITHOUT Frame Interpolation (LTPO native cadence matching)
        val targetHz = if (ltpoAdaptiveEnabled) {
            pickNativeCadenceDisplayHz(
                effectiveFps = effectiveFps,
                candidateModes = candidateModes,
                allowedCapHz = allowedCapHz,
                ltpoAdaptiveEnabled = true,
                isLtpoCapable = displayInfo.isLtpoCapable
            )
        } else {
            if (highRefreshRateEnabled) allowedCapHz else 60f.coerceAtMost(maxPanelHz)
        }

        val saving = ltpoAdaptiveEnabled && targetHz < maxPanelHz - 1.5f
        val surfaceRate = if (ltpoAdaptiveEnabled) {
            effectiveFps.coerceAtMost(allowedCapHz)
        } else if (highRefreshRateEnabled) {
            targetHz
        } else {
            0f
        }

        return RefreshRatePlan(
            sourceFps = normalizedSrcFps,
            playbackSpeed = clampedSpeed,
            effectiveVideoFps = effectiveFps,
            targetDisplayHz = targetHz,
            targetRenderFps = effectiveFps.coerceAtMost(targetHz),
            surfaceFrameRateHz = surfaceRate,
            frameRateCompatibility = if (ltpoAdaptiveEnabled) {
                Surface.FRAME_RATE_COMPATIBILITY_FIXED_SOURCE
            } else {
                Surface.FRAME_RATE_COMPATIBILITY_DEFAULT
            },
            preferSystemIdleWindowMode = false,
            useContinuousVsync = false,
            memcActive = false,
            isLtpoSavingActive = saving,
            hudBadgeText = buildHzBadge(targetHz, saving),
            statusSummary = if (ltpoAdaptiveEnabled) {
                "${formatFps(normalizedSrcFps)} к/с × ${formatSpeed(clampedSpeed)} = ${formatFps(effectiveFps)} к/с → LTPO ${targetHz.roundToInt()} Гц"
            } else {
                "Прямой вывод ${formatFps(effectiveFps)} к/с на ${targetHz.roundToInt()} Гц"
            }
        )
    }

    private fun pickNativeCadenceDisplayHz(
        effectiveFps: Float,
        candidateModes: List<Float>,
        allowedCapHz: Float,
        ltpoAdaptiveEnabled: Boolean,
        isLtpoCapable: Boolean = true
    ): Float {
        if (!ltpoAdaptiveEnabled) return allowedCapHz

        // For 24 / 25 / 30 fps video without interpolation, NEVER jump to 90 / 120 / 144 Hz!
        // Adapt for LTPO by lowering display refresh rate to 24 / 25 / 30 / 48 / 60 Hz.
        if (effectiveFps <= 31f) {
            val lowHzModes = candidateModes.filter { it <= 62f }
            // 1. Check if an exact 1x or 2x low-Hz mode is exposed (e.g. 24Hz, 25Hz, 30Hz, 48Hz, 50Hz, 60Hz)
            val lowHarmonic = lowHzModes.firstOrNull { modeHz ->
                modeHz >= effectiveFps - 0.6f && isNearHarmonicMultiple(modeHz, effectiveFps)
            }
            if (lowHarmonic != null) return lowHarmonic

            // 2. On LTPO screens where Display.supportedModes only exposes [60, 120] and VRR is driven by
            // Surface.setFrameRate + Window.preferredRefreshRate, target 24Hz/30Hz directly!
            if (isLtpoCapable) {
                return when {
                    effectiveFps <= 24.5f -> 24f.coerceAtLeast(
                        candidateModes.firstOrNull { it in 23f..31f } ?: 30f
                    )
                    effectiveFps <= 25.5f -> 25f.coerceAtLeast(
                        candidateModes.firstOrNull { it in 24f..31f } ?: 30f
                    )
                    else -> 30f
                }
            }

            // 3. Non-LTPO 60Hz fallback (never 120Hz for <=30fps without MEMC)
            return lowHzModes.firstOrNull { it >= effectiveFps - 0.5f }
                ?: candidateModes.firstOrNull()
                ?: 60f.coerceAtMost(allowedCapHz)
        }

        if (candidateModes.isEmpty()) return effectiveFps.coerceAtMost(allowedCapHz)

        val exactHarmonic = candidateModes.firstOrNull { modeHz ->
            modeHz >= effectiveFps - 0.6f && isNearHarmonicMultiple(modeHz, effectiveFps)
        }
        if (exactHarmonic != null) return exactHarmonic

        return candidateModes.firstOrNull { it >= effectiveFps - 0.5f }
            ?: allowedCapHz
    }

    private fun pickMemcLtpoDisplayHz(
        effectiveFps: Float,
        desiredTargetHz: Float,
        interpolationMode: FrameInterpolationMode,
        candidateModes: List<Float>,
        allowedCapHz: Float
    ): Float {
        if (candidateModes.isEmpty()) return desiredTargetHz.coerceAtMost(allowedCapHz)

        // 1. Strict exact match for 2x cadence (e.g. 24 fps -> 48 Hz mode on LTPO panels)
        val exactMatch = candidateModes.firstOrNull { abs(it - desiredTargetHz) <= 1.25f }
        if (exactMatch != null) return exactMatch

        // 2. Harmonic multiple of 2x (e.g. 48 fps -> 96 Hz)
        val harmonicMatch = candidateModes.firstOrNull { modeHz ->
            modeHz >= desiredTargetHz - 0.8f && isNearHarmonicMultiple(modeHz, desiredTargetHz)
        }
        if (harmonicMatch != null) return harmonicMatch

        // 3. Fallback: lowest candidate mode >= desiredTargetHz, or allowedCapHz
        return candidateModes.firstOrNull { it >= desiredTargetHz - 1.2f } ?: allowedCapHz
    }

    private fun isNearHarmonicMultiple(panelHz: Float, videoFps: Float): Boolean {
        if (videoFps <= 1f || panelHz < videoFps - 0.8f) return false
        val ratio = panelHz / videoFps
        val nearestInt = ratio.roundToInt().coerceAtLeast(1)
        val expectedHz = nearestInt * videoFps
        // Allow NTSC 1000/1001 tolerance (e.g. 23.976 * 5 = 119.88 vs 120.0 Hz -> diff 0.12 Hz)
        return abs(panelHz - expectedHz) <= 1.25f
    }

    fun normalizeSourceFps(rawFps: Float): Float {
        if (rawFps <= 1f || rawFps.isNaN() || rawFps.isInfinite()) return 24f
        return when {
            abs(rawFps - 23.976f) <= 0.35f || abs(rawFps - 24f) <= 0.35f -> 24f
            abs(rawFps - 25f) <= 0.35f -> 25f
            abs(rawFps - 29.97f) <= 0.4f || abs(rawFps - 30f) <= 0.4f -> 30f
            abs(rawFps - 48f) <= 0.5f -> 48f
            abs(rawFps - 50f) <= 0.5f -> 50f
            abs(rawFps - 59.94f) <= 0.6f || abs(rawFps - 60f) <= 0.6f -> 60f
            else -> rawFps.coerceIn(10f, 165f)
        }
    }

    private fun buildHzBadge(hz: Float, ltpoSaving: Boolean): String =
        if (ltpoSaving) "LTPO ${hz.roundToInt()}Гц" else "${hz.roundToInt()}Гц"

    private fun formatFps(fps: Float): String {
        val rounded = fps.roundToInt()
        return if (abs(fps - rounded) < 0.12f) {
            rounded.toString()
        } else {
            String.format(Locale.US, "%.1f", fps)
        }
    }

    private fun formatSpeed(speed: Float): String =
        String.format(Locale.US, "%.2fx", speed).replace("0x", "x").replace(".x", ".0x")

    /**
     * Applies [RefreshRatePlan] to the Activity window so LTPO / multi-rate panels can lower
     * refresh rate to 24/30/60 Hz during native video playback and to minimum Hz on pause,
     * while restoring 120 Hz when UI controls or app navigation are active.
     */
    fun applyWindowRefreshPlan(activity: Activity, plan: RefreshRatePlan): Float {
        val window = activity.window ?: return 60f
        val display = resolveActivityDisplay(activity) ?: return 60f
        val currentMode = display.mode
        val supported = display.supportedModes.orEmpty()

        val targetWidth = currentMode?.physicalWidth ?: supported.maxOfOrNull { it.physicalWidth } ?: 0
        val targetHeight = currentMode?.physicalHeight ?: supported.maxOfOrNull { it.physicalHeight } ?: 0
        val sameResModes = supported.filter {
            it.physicalWidth == targetWidth && it.physicalHeight == targetHeight
        }.ifEmpty { supported.toList() }

        // On pause / idle with LTPO: select lowest supported mode & set preferredRefreshRate to min Hz
        if (plan.preferSystemIdleWindowMode) {
            val minMode = sameResModes.minByOrNull { it.refreshRate }
            val desiredIdleRate = plan.targetDisplayHz.coerceAtLeast(1f)
            val attrs = window.attributes
            val targetModeId = minMode?.modeId ?: 0
            if (attrs.preferredDisplayModeId != targetModeId || abs(attrs.preferredRefreshRate - desiredIdleRate) > 0.5f) {
                attrs.preferredDisplayModeId = targetModeId
                attrs.preferredRefreshRate = desiredIdleRate
                window.attributes = attrs
            }
            return desiredIdleRate
        }

        if (sameResModes.isEmpty()) {
            return currentMode?.refreshRate ?: plan.targetDisplayHz
        }

        val bestMode = sameResModes.minByOrNull { mode ->
            val diff = abs(mode.refreshRate - plan.targetDisplayHz)
            // When MEMC is NOT active and targetDisplayHz <= 60Hz, strongly penalize 90/120Hz modes!
            val highHzPenalty = if (!plan.memcActive && plan.targetDisplayHz <= 62f && mode.refreshRate > 65f) {
                500f + mode.refreshRate
            } else {
                0f
            }
            val undershootPenalty = if (plan.memcActive && mode.refreshRate + 0.8f < plan.targetRenderFps) 200f else 0f
            diff + highHzPenalty + undershootPenalty
        }

        if (bestMode != null) {
            val attrs = window.attributes
            // Hint the exact LTPO target rate (e.g. 24f or 30f) via preferredRefreshRate
            val desiredRefreshRate = if (!plan.memcActive && plan.targetDisplayHz <= 62f) {
                plan.targetDisplayHz
            } else {
                bestMode.refreshRate
            }
            val isSame = attrs.preferredDisplayModeId == bestMode.modeId &&
                abs(attrs.preferredRefreshRate - desiredRefreshRate) <= 0.5f

            if (!isSame) {
                attrs.preferredDisplayModeId = bestMode.modeId
                attrs.preferredRefreshRate = desiredRefreshRate
                window.attributes = attrs
            }
            return desiredRefreshRate
        }
        return currentMode?.refreshRate ?: plan.targetDisplayHz
    }

    fun applyWindowRefreshRate(activity: Activity, enableHighRefreshRate: Boolean): Float {
        val info = queryDisplayRefreshRates(activity)
        val plan = computeOptimalRefreshPlan(
            displayInfo = info,
            isPlayerActive = false,
            isPlaying = false,
            sourceVideoFps = 24f,
            playbackSpeed = 1f,
            highRefreshRateEnabled = enableHighRefreshRate,
            ltpoAdaptiveEnabled = true,
            interpolationMode = FrameInterpolationMode.OFF
        )
        return applyWindowRefreshPlan(activity, plan)
    }

    /**
     * Hints SurfaceFlinger / LTPO VRR controller (Android 11 / API 30+) about the exact desired
     * frame rate and seamless switching policy.
     */
    fun configureSurfaceFrameRate(
        surface: Surface?,
        targetFps: Float,
        highRefreshOrInterpActive: Boolean,
        compatibility: Int = Surface.FRAME_RATE_COMPATIBILITY_DEFAULT
    ) {
        if (surface == null || !surface.isValid) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            runCatching {
                if (targetFps in 0.5f..5f) {
                    // Paused / LTPO minimum refresh hint
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        surface.setFrameRate(
                            targetFps,
                            Surface.FRAME_RATE_COMPATIBILITY_DEFAULT,
                            Surface.CHANGE_FRAME_RATE_ONLY_IF_SEAMLESS
                        )
                    } else {
                        surface.setFrameRate(targetFps, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT)
                    }
                } else if (highRefreshOrInterpActive && targetFps >= 12f) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        surface.setFrameRate(
                            targetFps,
                            compatibility,
                            Surface.CHANGE_FRAME_RATE_ALWAYS
                        )
                    } else {
                        surface.setFrameRate(targetFps, compatibility)
                    }
                } else {
                    surface.setFrameRate(0f, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT)
                }
            }
        }
    }

    private fun resolveActivityDisplay(activity: Activity): Display? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            runCatching { activity.display }.getOrNull() ?: resolveDisplay(activity)
        } else {
            @Suppress("DEPRECATION")
            activity.windowManager?.defaultDisplay
        }
    }

    private fun resolveDisplay(context: Context): Display? {
        val dm = context.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
        val primary = dm?.getDisplay(Display.DEFAULT_DISPLAY)
        if (primary != null) return primary
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        @Suppress("DEPRECATION")
        return wm?.defaultDisplay
    }
}
