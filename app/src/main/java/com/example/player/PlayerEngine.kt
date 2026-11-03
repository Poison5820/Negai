package com.example.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.SystemClock
import android.view.Surface
import androidx.annotation.OptIn
import androidx.compose.runtime.Immutable
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.common.audio.ChannelMixingAudioProcessor
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.audio.AudioCapabilities
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.video.VideoFrameMetadataListener
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.data.LibraryRepository
import com.example.data.MediaAutoMatcher
import com.example.data.PlaybackResumeHelper
import com.example.model.AudioDeArtifactMode
import com.example.model.DecoderMode
import com.example.model.EpisodeItem
import com.example.model.ExternalAudioTrack
import com.example.model.ExternalSubtitleTrack
import com.example.model.PlaybackEndMode
import com.example.model.PreferredTrackLanguage
import com.example.model.SleepTimerPreset
import com.example.model.StreamingQualityPreset
import com.example.model.VideoChapter
import com.example.model.VideoResolutionFormatter
import com.example.subtitles.ParsedSubtitleDocument
import com.example.subtitles.SubtitleAlignment
import com.example.subtitles.SubtitleCue
import com.example.subtitles.SubtitleParser
import com.example.torrent.TorrServerClient
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.io.File
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Immutable
data class VideoQualityUiOption(
    val id: String,
    val label: String,
    val badge: String,
    val width: Int = 0,
    val height: Int = 0,
    val heightPx: Int = height,
    val bitrateBps: Long = 0L,
    val groupIndex: Int = -1,
    val trackIndex: Int = -1,
    val playlistEpisodeIndex: Int = -1,
    val maxHeightCap: Int = 0,
    val isAuto: Boolean = false,
    val isSelected: Boolean = false
)

@Immutable
data class AudioTrackUiOption(
    val id: String,
    val label: String,
    val badge: String,
    val groupIndex: Int,
    val trackIndex: Int,
    val isSelected: Boolean,
    val isExternal: Boolean,
    val bitrateKbps: Int = 0
)

@Immutable
data class SubtitleTrackUiOption(
    val id: String,
    val label: String,
    val badge: String,
    val isExternal: Boolean,
    val externalTrack: ExternalSubtitleTrack? = null,
    val embeddedGroupIndex: Int = -1,
    val embeddedTrackIndex: Int = -1,
    val isSelected: Boolean = false
)

@Immutable
data class PlaybackRuntimeState(
    val playlist: List<EpisodeItem> = emptyList(),
    val currentEpisodeIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedPositionMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val videoWidth: Int = 1280,
    val videoHeight: Int = 720,
    val sourceFrameRate: Float = 0f,
    val chapters: List<VideoChapter> = emptyList(),
    val activeVideoCodecName: String = "Ожидание потока…",
    val videoMimeType: String = "",
    val videoBitrateBps: Long = 0L,
    val isHardwareAccelerated: Boolean = true,
    val activeDecoderMode: DecoderMode = DecoderMode.HW_PLUS,
    val videoQualityOptions: List<VideoQualityUiOption> = emptyList(),
    val selectedVideoQualityId: String = "max",
    val audioTrackOptions: List<AudioTrackUiOption> = emptyList(),
    val subtitleTrackOptions: List<SubtitleTrackUiOption> = emptyList(),
    val selectedSubtitleId: String = "off",
    val subtitleDelayMs: Long = 0L,
    val activeSubtitleCues: List<SubtitleCue> = emptyList(),
    val audioBoostPercent: Int = 100,
    val audioDelayMs: Long = 0L,
    val audioDeArtifactMode: AudioDeArtifactMode = AudioDeArtifactMode.STANDARD,
    val autoDeArtifactLowBitrate: Boolean = true,
    val activeAudioBitrateKbps: Int = 0,
    val audioMimeType: String = "",
    val audioSampleRateHz: Int = 0,
    val audioChannelCount: Int = 0,
    val isAudioDeArtifactActive: Boolean = true,
    val bluetoothAudioRoute: BluetoothAudioRouteState = BluetoothAudioRouteState(),
    val bluetoothAutoSyncEnabled: Boolean = false,
    val bluetoothConfiguredOffsetMs: Long = 0L,
    val bluetoothAppliedOffsetMs: Long = 0L,
    val effectiveAudioDelayMs: Long = 0L,
    val lastAudioResyncTimestampMs: Long = 0L,
    val playbackEndMode: PlaybackEndMode = PlaybackEndMode.AUTO_NEXT,
    val sleepTimerPreset: SleepTimerPreset = SleepTimerPreset.OFF,
    val sleepTimerRemainingMs: Long = 0L,
    val abLoopStartMs: Long? = null,
    val abLoopEndMs: Long? = null,
    val isTempSpeedBoostActive: Boolean = false,
    val resumedFromPositionMs: Long? = null,
    val resumeEventId: Long = 0L,
    val isHdr: Boolean = false,
    val hdrBadge: String = "",
    val hudNotice: String? = null,
    val errorMessage: String? = null
) {
    val currentEpisode: EpisodeItem?
        get() = playlist.getOrNull(currentEpisodeIndex)

    val hasPreviousEpisode: Boolean
        get() = currentEpisodeIndex > 0

    val hasNextEpisode: Boolean
        get() = currentEpisodeIndex in 0 until (playlist.size - 1)

    val isSleepTimerActive: Boolean
        get() = sleepTimerPreset != SleepTimerPreset.OFF &&
            (sleepTimerPreset.stopAtEndOfEpisode || sleepTimerRemainingMs > 0L)

    val isAbLoopActive: Boolean
        get() = abLoopStartMs != null && abLoopEndMs != null && abLoopEndMs > abLoopStartMs
}

@OptIn(UnstableApi::class)
class PlayerEngine private constructor(
    private val appContext: Context,
    private val repository: LibraryRepository
) {
    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(PlaybackRuntimeState())
    val state: StateFlow<PlaybackRuntimeState> = _state.asStateFlow()

    /**
     * High-frequency playback position decoupled from [PlaybackRuntimeState] so timeline
     * ticks never trigger full-screen recomposition of the player HUD or video surface.
     */
    private val _positionMs = MutableStateFlow(0L)
    val positionMs: StateFlow<Long> = _positionMs.asStateFlow()

    private val playerChangedListeners = mutableListOf<(ExoPlayer) -> Unit>()

    private var _exoPlayer: ExoPlayer? = null
    val exoPlayer: ExoPlayer
        get() = ensureExoPlayer()

    private var configuredDecoderMode: DecoderMode = DecoderMode.HW_PLUS
    private var highRefreshOrInterpActive: Boolean = true
    private var decoderFallbackRetryCount: Int = 0

    fun clearHudNotice() {
        _state.update { it.copy(hudNotice = null) }
    }

    private var boundSurface: Surface? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    // Per-episode extra manual tracks added during session (keyed by videoUri)
    private val extraAudioByVideo = mutableMapOf<String, MutableList<ExternalAudioTrack>>()
    private val extraSubsByVideo = mutableMapOf<String, MutableList<ExternalSubtitleTrack>>()
    private val autoAppliedDefaultAudioForVideo = mutableSetOf<String>()
    private val autoAppliedDefaultSubForVideo = mutableSetOf<String>()
    private val probedContainerAudioBitratesKbps = java.util.concurrent.ConcurrentHashMap<String, List<Int>>()
    private val probedExternalAudioBitratesKbps = java.util.concurrent.ConcurrentHashMap<String, Int>()

    private val audioBoostController = AudioBoostController()
    private val audioDelayProcessor = AudioDelayProcessor()
    private var manualAudioDelayMs: Long = 0L
    private var bluetoothAutoSyncEnabled: Boolean = false
    private var bluetoothConfiguredOffsetMs: Long = 0L
    private val bluetoothSyncController = BluetoothAudioSyncController(appContext) {
        recalculateEffectiveAudioDelay(resyncIfPlaying = false)
    }
    private var activeExternalSubtitleDoc: ParsedSubtitleDocument? = null
    private var latestEmbeddedCues: List<SubtitleCue> = emptyList()
    private var positionTickerJob: Job? = null
    private var chapterExtractionJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var sleepDeadlineElapsedMs: Long = 0L
    private var basePlaybackSpeed: Float = 1.0f
    @Volatile
    private var preferExternalAudioOnStart: Boolean = true
    @Volatile
    private var preferredAudioLanguage: PreferredTrackLanguage = PreferredTrackLanguage.RU
    @Volatile
    private var autoBindExternalSubtitles: Boolean = true
    @Volatile
    private var preferExternalSubtitlesOnStart: Boolean = true
    @Volatile
    private var preferredSubtitleLanguage: PreferredTrackLanguage = PreferredTrackLanguage.RU
    @Volatile
    private var defaultStreamingQuality: StreamingQualityPreset = StreamingQualityPreset.MAX
    @Volatile
    private var enableEmbeddedChapters: Boolean = true
    @Volatile
    private var latestTorrServerBaseUrl: String = "http://127.0.0.1:8090"
    @Volatile
    private var latestTorrServerAuthHeader: String? = null
    private var lastSavedTickCount = 0
    private var lastTrackedEpisodeIndex: Int = -1
    private var lastKnownDurationForTrackedEpisodeMs: Long = 0L
    @Volatile
    private var detectedVideoWidth: Int = 0
    @Volatile
    private var detectedVideoHeight: Int = 0
    @Volatile
    private var pendingNextVideoWidth: Int = 0
    @Volatile
    private var pendingNextVideoHeight: Int = 0
    @Volatile
    private var lastFramePtsUs: Long = C.TIME_UNSET
    @Volatile
    private var smoothedPtsDeltaUs: Float = 0f
    @Volatile
    private var ptsSampleCount: Int = 0

    private val videoFrameMetadataListener = VideoFrameMetadataListener { presentationTimeUs, _, format, _ ->
        val declaredFps = format.frameRate
        if (declaredFps > 1f) {
            lastFramePtsUs = presentationTimeUs
            if ((_state.value.sourceFrameRate - declaredFps).let { it < -0.05f || it > 0.05f }) {
                _state.update { it.copy(sourceFrameRate = declaredFps) }
            }
            return@VideoFrameMetadataListener
        }
        val prevPts = lastFramePtsUs
        lastFramePtsUs = presentationTimeUs
        if (prevPts != C.TIME_UNSET) {
            val deltaUs = presentationTimeUs - prevPts
            if (deltaUs in 4_000L..100_000L) {
                val deltaF = deltaUs.toFloat()
                smoothedPtsDeltaUs = if (ptsSampleCount == 0) {
                    deltaF
                } else {
                    smoothedPtsDeltaUs * 0.85f + deltaF * 0.15f
                }
                ptsSampleCount++
                if (ptsSampleCount >= 8 && smoothedPtsDeltaUs > 1_000f) {
                    val rawFps = 1_000_000f / smoothedPtsDeltaUs
                    val snappedFps = when {
                        rawFps in 23.5f..24.1f -> 23.976f
                        rawFps in 24.8f..25.2f -> 25f
                        rawFps in 29.5f..30.2f -> 29.97f
                        rawFps in 47.5f..48.5f -> 48f
                        rawFps in 49.5f..50.5f -> 50f
                        rawFps in 59.0f..60.5f -> 59.94f
                        else -> rawFps
                    }
                    val currentFps = _state.value.sourceFrameRate
                    if (currentFps <= 1f || kotlin.math.abs(currentFps - snappedFps) >= 0.4f) {
                        _state.update { it.copy(sourceFrameRate = snappedFps) }
                    }
                }
            }
        }
    }

    private var awaitingInitialDurationVerifyIndex: Int = -1
    private var lastManualEpisodeStartIndex: Int = -1
    private var lastManualEpisodeStartRealtimeMs: Long = 0L
    private var lastManualEpisodeInitialPosMs: Long = 0L
    private var suppressNextPlaylistChangedTransition: Boolean = false

    private fun verifyAndResetIfResumedAtEnd(player: ExoPlayer, resolvedDur: Long) {
        if (resolvedDur <= 10_000L) return
        val currentIdx = player.currentMediaItemIndex
        if (awaitingInitialDurationVerifyIndex != currentIdx) return
        awaitingInitialDurationVerifyIndex = -1
        // Only reset if we actually resumed from a non-zero saved position (> 5s) that is at EOF
        val initialResumePos = lastManualEpisodeInitialPosMs
        if (initialResumePos > 5_000L &&
            PlaybackResumeHelper.computeResumePositionMs(
                positionMs = initialResumePos,
                durationMs = resolvedDur,
                resumeEnabled = true
            ) == 0L
        ) {
            lastManualEpisodeInitialPosMs = 0L
            player.seekTo(currentIdx, 0L)
            _positionMs.value = 0L
            _state.update {
                it.copy(
                    currentPositionMs = 0L,
                    durationMs = resolvedDur,
                    resumedFromPositionMs = null
                )
            }
            val ep = _state.value.playlist.getOrNull(currentIdx)
            if (ep != null) {
                engineScope.launch {
                    repository.savePlaybackProgress(
                        episode = ep,
                        positionMs = 0L,
                        durationMs = resolvedDur,
                        videoWidth = detectedVideoWidth,
                        videoHeight = detectedVideoHeight
                    )
                }
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            audioBoostController.onAudioSessionIdChanged(audioSessionId)
        }

        override fun onTimelineChanged(timeline: androidx.media3.common.Timeline, reason: Int) {
            val player = _exoPlayer ?: return
            val liveDur = player.duration.takeIf { it != C.TIME_UNSET && it > 0L } ?: return
            lastKnownDurationForTrackedEpisodeMs = liveDur
            verifyAndResetIfResumedAtEnd(player, liveDur)
            if (_state.value.durationMs != liveDur) {
                _state.update { it.copy(durationMs = liveDur) }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val pos = _exoPlayer?.currentPosition?.coerceAtLeast(0L) ?: _positionMs.value
            _positionMs.value = pos
            _state.update { it.copy(isPlaying = isPlaying, currentPositionMs = pos) }
            if (isPlaying) {
                startPositionTicker()
            } else {
                stopPositionTicker()
                persistCurrentProgress()
            }
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (!playWhenReady &&
                reason == Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM &&
                _state.value.sleepTimerPreset == SleepTimerPreset.END_OF_EPISODE
            ) {
                clearSleepTimerInternal()
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            val player = _exoPlayer ?: return
            if (playbackState == Player.STATE_READY) {
                decoderFallbackRetryCount = 0
            }
            val buffering = playbackState == Player.STATE_BUFFERING
            val liveDur = player.duration.takeIf { it != C.TIME_UNSET && it > 0L }
            val dur = liveDur ?: _state.value.durationMs
            val rawPos = player.currentPosition.coerceAtLeast(0L)
            val previousValidPos = _positionMs.value.coerceAtLeast(_state.value.currentPositionMs)
            var pos = if (playbackState == Player.STATE_IDLE && rawPos == 0L && previousValidPos > 0L) {
                previousValidPos
            } else {
                rawPos
            }
            if (liveDur != null) {
                lastKnownDurationForTrackedEpisodeMs = liveDur
                verifyAndResetIfResumedAtEnd(player, liveDur)
                pos = player.currentPosition.coerceAtLeast(pos).coerceAtLeast(0L)
            }
            _positionMs.value = pos
            _state.update { prev ->
                val updatedChapters = if (dur > 0L && prev.chapters.isNotEmpty() && prev.chapters.last().endTimeMs < dur) {
                    prev.chapters.mapIndexed { idx, ch ->
                        if (idx == prev.chapters.lastIndex && ch.endTimeMs <= ch.startTimeMs) {
                            ch.copy(endTimeMs = dur)
                        } else {
                            ch
                        }
                    }
                } else {
                    prev.chapters
                }
                prev.copy(
                    isBuffering = buffering,
                    durationMs = dur,
                    currentPositionMs = pos,
                    chapters = updatedChapters
                )
            }
            if (playbackState == Player.STATE_ENDED) {
                stopPositionTicker()
                if (_state.value.sleepTimerPreset == SleepTimerPreset.END_OF_EPISODE) {
                    clearSleepTimerInternal()
                }
                val ep = _state.value.currentEpisode
                val completedDur = dur.coerceAtLeast(pos).coerceAtLeast(lastKnownDurationForTrackedEpisodeMs).coerceAtLeast(10_000L)
                if (ep != null) {
                    val w = detectedVideoWidth
                    val h = detectedVideoHeight
                    engineScope.launch {
                        repository.savePlaybackProgress(
                            episode = ep,
                            positionMs = completedDur,
                            durationMs = completedDur,
                            videoWidth = w,
                            videoHeight = h
                        )
                    }
                }
                player.pause()
                // Retain scrub bar position at completion (completedDur) so progress does not snap to 0
                _positionMs.value = completedDur
                _state.update {
                    it.copy(
                        isPlaying = false,
                        currentPositionMs = completedDur,
                        durationMs = completedDur,
                        resumedFromPositionMs = null
                    )
                }
            }
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            if (videoSize.width > 0 && videoSize.height > 0) {
                val par = videoSize.pixelWidthHeightRatio.takeIf { it > 0f } ?: 1f
                val displayW = (videoSize.width * par).roundToInt().coerceAtLeast(1)
                val displayH = videoSize.height.coerceAtLeast(1)
                val swapped = videoSize.unappliedRotationDegrees == 90 || videoSize.unappliedRotationDegrees == 270
                val finalW = if (swapped) displayH else displayW
                val finalH = if (swapped) displayW else displayH

                val player = _exoPlayer
                val dur = player?.duration?.takeIf { it != C.TIME_UNSET && it > 0L } ?: _state.value.durationMs
                val pos = player?.currentPosition?.coerceAtLeast(0L) ?: _positionMs.value
                val currentW = _state.value.videoWidth
                val currentH = _state.value.videoHeight
                val hasEstablishedSize = currentW > 0 && currentH > 0
                val aspectFlipped = hasEstablishedSize && ((finalW >= finalH) != (currentW >= currentH))
                val isVeryEndBeforeNext = _state.value.hasNextEpisode && dur > 5_000L && pos >= (dur - 1_200L)
                if (isVeryEndBeforeNext && aspectFlipped) {
                    pendingNextVideoWidth = finalW
                    pendingNextVideoHeight = finalH
                    return
                }

                pendingNextVideoWidth = 0
                pendingNextVideoHeight = 0
                detectedVideoWidth = finalW
                detectedVideoHeight = finalH
                _state.update {
                    it.copy(
                        videoWidth = finalW,
                        videoHeight = finalH
                    )
                }
                persistCurrentProgress()
            }
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            val playlist = _state.value.playlist
            if (oldPosition.mediaItemIndex != newPosition.mediaItemIndex &&
                oldPosition.mediaItemIndex in playlist.indices
            ) {
                val elapsedSinceManualStart = SystemClock.elapsedRealtime() - lastManualEpisodeStartRealtimeMs
                val isInstantEofOnReopen = reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION &&
                    oldPosition.mediaItemIndex == lastManualEpisodeStartIndex &&
                    lastManualEpisodeInitialPosMs > 5_000L &&
                    elapsedSinceManualStart in 0L..3_500L
                if (isInstantEofOnReopen) {
                    return
                }

                val outgoingEp = playlist[oldPosition.mediaItemIndex]
                val outgoingPos = oldPosition.positionMs.coerceAtLeast(0L)
                val outgoingDur = lastKnownDurationForTrackedEpisodeMs
                    .coerceAtLeast(outgoingEp.durationMs)
                    .coerceAtLeast(_state.value.durationMs)
                val isCompleted = reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION ||
                    (outgoingDur >= 10_000L && (outgoingPos >= (outgoingDur * 95L) / 100L || (outgoingDur - outgoingPos) <= 15_000L))
                val resolvedDur = if (isCompleted) {
                    outgoingDur.coerceAtLeast(outgoingPos).coerceAtLeast(10_000L)
                } else {
                    outgoingDur
                }
                val resolvedPos = if (isCompleted) resolvedDur else outgoingPos
                if (resolvedPos > 0L || resolvedDur > 0L) {
                    val outW = detectedVideoWidth
                    val outH = detectedVideoHeight
                    engineScope.launch {
                        repository.savePlaybackProgress(
                            episode = outgoingEp,
                            positionMs = resolvedPos,
                            durationMs = resolvedDur,
                            videoWidth = outW,
                            videoHeight = outH
                        )
                    }
                }
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val player = _exoPlayer ?: return
            val idx = player.currentMediaItemIndex
            val playlist = _state.value.playlist
            if (idx in playlist.indices) {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED && suppressNextPlaylistChangedTransition) {
                    suppressNextPlaylistChangedTransition = false
                    return
                }
                val elapsedSinceManualStart = SystemClock.elapsedRealtime() - lastManualEpisodeStartRealtimeMs
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO &&
                    lastManualEpisodeStartIndex in playlist.indices &&
                    lastManualEpisodeStartIndex != idx &&
                    lastManualEpisodeInitialPosMs > 5_000L &&
                    elapsedSinceManualStart in 0L..3_500L
                ) {
                    // Guard: the episode opened by the user had a legacy/corrupt saved position right at EOF
                    // and immediately auto-transitioned. Rewind back to the requested episode at 00:00!
                    val targetIdx = lastManualEpisodeStartIndex
                    val targetEp = playlist[targetIdx]
                    val fallbackDur = lastKnownDurationForTrackedEpisodeMs
                        .coerceAtLeast(targetEp.durationMs)
                        .coerceAtLeast(lastManualEpisodeInitialPosMs)
                    lastManualEpisodeInitialPosMs = 0L
                    lastManualEpisodeStartRealtimeMs = SystemClock.elapsedRealtime()
                    lastTrackedEpisodeIndex = targetIdx
                    awaitingInitialDurationVerifyIndex = -1
                    _positionMs.value = 0L
                    player.seekTo(targetIdx, 0L)
                    player.playWhenReady = true
                    _state.update {
                        it.copy(
                            currentEpisodeIndex = targetIdx,
                            currentPositionMs = 0L,
                            durationMs = fallbackDur,
                            resumedFromPositionMs = null,
                            errorMessage = null
                        )
                    }
                    engineScope.launch {
                        repository.savePlaybackProgress(
                            episode = targetEp,
                            positionMs = 0L,
                            durationMs = fallbackDur
                        )
                    }
                    return
                }

                lastTrackedEpisodeIndex = idx
                val currentDur = player.duration.takeIf { d -> d != C.TIME_UNSET && d > 0L } ?: 0L
                val pos = player.currentPosition.coerceAtLeast(0L)
                _positionMs.value = pos
                lastKnownDurationForTrackedEpisodeMs = currentDur

                val newEp = playlist[idx]
                val nextW = pendingNextVideoWidth.takeIf { it > 0 } ?: newEp.videoWidth
                val nextH = pendingNextVideoHeight.takeIf { it > 0 } ?: newEp.videoHeight
                pendingNextVideoWidth = 0
                pendingNextVideoHeight = 0
                detectedVideoWidth = nextW
                detectedVideoHeight = nextH

                _state.update {
                    it.copy(
                        currentEpisodeIndex = idx,
                        currentPositionMs = pos,
                        durationMs = currentDur,
                        videoWidth = nextW,
                        videoHeight = nextH,
                        chapters = emptyList(),
                        abLoopStartMs = null,
                        abLoopEndMs = null,
                        errorMessage = null
                    )
                }
                lastFramePtsUs = C.TIME_UNSET
                ptsSampleCount = 0
                onEpisodeActivated(newEp)

                val isExternalSeekTransition = reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK &&
                    (idx != lastManualEpisodeStartIndex || elapsedSinceManualStart > 1_500L)
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO || isExternalSeekTransition) {
                    engineScope.launch {
                        val settings = repository.settingsFlow.first()
                        val saved = repository.getSavedProgressForEpisode(newEp)
                        val effectiveDur = (saved?.durationMs ?: 0L)
                            .coerceAtLeast(newEp.durationMs)
                            .coerceAtLeast(currentDur)
                        val resumePos = PlaybackResumeHelper.computeResumePositionMs(
                            positionMs = saved?.positionMs ?: newEp.resumePositionMs,
                            durationMs = effectiveDur,
                            resumeEnabled = settings.resumePlayback
                        )
                        awaitingInitialDurationVerifyIndex = if (resumePos > 0L && currentDur <= 0L) idx else -1
                        val effectivePos = if (player.currentMediaItemIndex == idx) {
                            if (resumePos > 0L && player.currentPosition < 2_500L) {
                                player.seekTo(idx, resumePos)
                                _positionMs.value = resumePos
                                _state.update {
                                    it.copy(
                                        currentPositionMs = resumePos,
                                        durationMs = effectiveDur.coerceAtLeast(it.durationMs),
                                        resumedFromPositionMs = resumePos,
                                        resumeEventId = System.currentTimeMillis()
                                    )
                                }
                                resumePos
                            } else if (resumePos == 0L && player.currentPosition > 1_000L) {
                                player.seekTo(idx, 0L)
                                _positionMs.value = 0L
                                _state.update {
                                    it.copy(
                                        currentPositionMs = 0L,
                                        durationMs = effectiveDur.coerceAtLeast(it.durationMs),
                                        resumedFromPositionMs = null
                                    )
                                }
                                0L
                            } else {
                                player.currentPosition.coerceAtLeast(0L)
                            }
                        } else {
                            resumePos
                        }
                        repository.savePlaybackProgress(
                            episode = newEp,
                            positionMs = effectivePos,
                            durationMs = effectiveDur
                        )
                    }
                }
            }
        }

        override fun onTracksChanged(tracks: Tracks) {
            refreshTracksState(tracks)
        }

        override fun onCues(cueGroup: CueGroup) {
            if (_state.value.selectedSubtitleId.startsWith("emb:")) {
                latestEmbeddedCues = cueGroup.cues.mapNotNull { cue ->
                    val txt = cue.text?.toString()?.trim()?.takeIf { it.isNotEmpty() } ?: return@mapNotNull null
                    SubtitleCue(
                        startMs = 0L,
                        endMs = Long.MAX_VALUE,
                        text = txt,
                        alignment = SubtitleAlignment.BOTTOM_CENTER
                    )
                }
                _state.update { it.copy(activeSubtitleCues = latestEmbeddedCues) }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            val isCodecOrDecoderError = when (error.errorCode) {
                PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
                PlaybackException.ERROR_CODE_DECODING_FAILED,
                PlaybackException.ERROR_CODE_DECODING_FORMAT_EXCEEDS_CAPABILITIES,
                PlaybackException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED,
                PlaybackException.ERROR_CODE_AUDIO_TRACK_INIT_FAILED,
                PlaybackException.ERROR_CODE_AUDIO_TRACK_WRITE_FAILED -> true
                else -> {
                    val cause = error.cause
                    cause is androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException ||
                        cause is androidx.media3.exoplayer.mediacodec.MediaCodecRenderer.DecoderInitializationException ||
                        cause is androidx.media3.exoplayer.audio.AudioSink.InitializationException ||
                        cause is androidx.media3.exoplayer.audio.AudioSink.ConfigurationException ||
                        cause is android.media.MediaCodec.CodecException
                }
            }

            if (isCodecOrDecoderError && configuredDecoderMode != DecoderMode.SOFTWARE_PREFER && decoderFallbackRetryCount < 2) {
                decoderFallbackRetryCount++
                _state.update {
                    it.copy(
                        errorMessage = null,
                        isBuffering = true,
                        hudNotice = "Сбой аппаратного декодера: выполнен автопереход на программный (SW)"
                    )
                }
                switchDecoderMode(DecoderMode.SOFTWARE_PREFER)
                return
            }

            stopPositionTicker()
            _state.update {
                it.copy(
                    errorMessage = "Ошибка воспроизведения: ${error.localizedMessage ?: error.errorCodeName}",
                    isBuffering = false
                )
            }
            repository.refreshMediaAvailabilityAsync()
        }
    }

    private val analyticsListener = object : AnalyticsListener {
        override fun onVideoDecoderInitialized(
            eventTime: AnalyticsListener.EventTime,
            decoderName: String,
            initializedTimestampMs: Long,
            initializationDurationMs: Long
        ) {
            val activeIdx = _exoPlayer?.currentMediaItemIndex ?: _state.value.currentEpisodeIndex
            if (eventTime.windowIndex != activeIdx) return
            val lower = decoderName.lowercase()
            val isSw = lower.startsWith("omx.google.") ||
                lower.startsWith("c2.android.") ||
                lower.contains("ffmpeg") ||
                lower.contains("dav1d") ||
                lower.contains("libvpx")
            _state.update {
                it.copy(
                    activeVideoCodecName = decoderName,
                    isHardwareAccelerated = !isSw
                )
            }
        }

        override fun onVideoInputFormatChanged(
            eventTime: AnalyticsListener.EventTime,
            format: Format,
            decoderReuseEvaluation: androidx.media3.exoplayer.DecoderReuseEvaluation?
        ) {
            if (format.width > 0 && format.height > 0) {
                val par = format.pixelWidthHeightRatio.takeIf { it > 0f } ?: 1f
                val parW = (format.width * par).roundToInt().coerceAtLeast(1)
                val parH = format.height.coerceAtLeast(1)
                val rotated = format.rotationDegrees == 90 || format.rotationDegrees == 270
                val dispW = if (rotated) parH else parW
                val dispH = if (rotated) parW else parH
                val fps = format.frameRate.takeIf { it > 1f } ?: 0f

                val activeIdx = _exoPlayer?.currentMediaItemIndex ?: _state.value.currentEpisodeIndex
                if (eventTime.windowIndex != activeIdx) {
                    // Pre-buffered next episode in playlist: cache dimensions for onMediaItemTransition
                    // so the screen never rotates before the current episode finishes!
                    pendingNextVideoWidth = dispW
                    pendingNextVideoHeight = dispH
                    return
                }

                detectedVideoWidth = dispW
                detectedVideoHeight = dispH

                val colorInfo = format.colorInfo
                val colorTransfer = colorInfo?.colorTransfer ?: C.COLOR_TRANSFER_SDR
                val colorSpace = colorInfo?.colorSpace ?: C.COLOR_SPACE_BT709
                val sampleMime = format.sampleMimeType.orEmpty().lowercase()
                val isDolbyVision = sampleMime.contains("dolby-vision") || sampleMime.contains("dvhe") || sampleMime.contains("dvh1")
                val isHdr10 = colorTransfer == C.COLOR_TRANSFER_ST2084
                val isHlg = colorTransfer == C.COLOR_TRANSFER_HLG
                val isHdrDetected = isDolbyVision || isHdr10 || isHlg || (colorSpace == C.COLOR_SPACE_BT2020 && colorTransfer != C.COLOR_TRANSFER_SDR)
                val badge = when {
                    isDolbyVision -> "Dolby Vision"
                    isHdr10 -> "HDR10"
                    isHlg -> "HLG"
                    isHdrDetected -> "HDR"
                    else -> ""
                }

                _state.update { state ->
                    val codecTag = format.sampleMimeType?.substringAfter('/')?.uppercase() ?: ""
                    val updatedName = if (state.activeVideoCodecName.startsWith("Ожидание")) {
                        codecTag.ifBlank { state.activeVideoCodecName }
                    } else {
                        state.activeVideoCodecName
                    }
                    val vBitrate = format.bitrate.takeIf { it > 0 }?.toLong() ?: state.videoBitrateBps
                    state.copy(
                        videoWidth = dispW,
                        videoHeight = dispH,
                        sourceFrameRate = if (fps > 0f) fps else state.sourceFrameRate,
                        activeVideoCodecName = updatedName,
                        videoMimeType = format.sampleMimeType ?: state.videoMimeType,
                        videoBitrateBps = vBitrate,
                        isHdr = isHdrDetected,
                        hdrBadge = badge
                    )
                }
            }
        }

        override fun onAudioInputFormatChanged(
            eventTime: AnalyticsListener.EventTime,
            format: Format,
            decoderReuseEvaluation: androidx.media3.exoplayer.DecoderReuseEvaluation?
        ) {
            val activeIdx = _exoPlayer?.currentMediaItemIndex ?: _state.value.currentEpisodeIndex
            if (eventTime.windowIndex != activeIdx) return
            updateDetectedAudioFormat(format)
        }
    }

    init {
        // Lightweight settings observer only — heavy ExoPlayer & MediaCodec creation is deferred
        // until ensureExoPlayer() is invoked when actual video playback begins!
        val boot = repository.preferencesRepository.readBootSettingsSnapshot()
        configuredDecoderMode = boot.decoderMode
        highRefreshOrInterpActive = boot.highRefreshRateEnabled || boot.effectiveFrameInterpolationMode.isEnabled
        preferExternalAudioOnStart = boot.preferExternalAudioOnStart
        preferredAudioLanguage = boot.preferredAudioLanguage
        autoBindExternalSubtitles = boot.autoBindExternalSubtitles
        preferExternalSubtitlesOnStart = boot.preferExternalSubtitlesOnStart
        preferredSubtitleLanguage = boot.preferredSubtitleLanguage
        defaultStreamingQuality = boot.defaultStreamingQuality
        bluetoothAutoSyncEnabled = boot.bluetoothAudioAutoSync
        bluetoothConfiguredOffsetMs = boot.bluetoothAudioOffsetMs
        audioDelayProcessor.deArtifactMode = boot.audioDeArtifactMode
        audioDelayProcessor.autoDeArtifactLowBitrate = boot.autoDeArtifactLowBitrate
        _state.update {
            it.copy(
                activeDecoderMode = boot.decoderMode,
                playbackEndMode = boot.playbackEndMode,
                bluetoothAutoSyncEnabled = boot.bluetoothAudioAutoSync,
                bluetoothConfiguredOffsetMs = boot.bluetoothAudioOffsetMs,
                audioDeArtifactMode = boot.audioDeArtifactMode,
                autoDeArtifactLowBitrate = boot.autoDeArtifactLowBitrate,
                isAudioDeArtifactActive = audioDelayProcessor.isDeArtifactCurrentlyActive
            )
        }
        recalculateEffectiveAudioDelay(resyncIfPlaying = false)

        engineScope.launch {
            repository.settingsFlow.collect { s ->
                val prevChaptersEnabled = enableEmbeddedChapters
                val prevBtAuto = bluetoothAutoSyncEnabled
                val prevBtOffset = bluetoothConfiguredOffsetMs
                preferExternalAudioOnStart = s.preferExternalAudioOnStart
                preferredAudioLanguage = s.preferredAudioLanguage
                autoBindExternalSubtitles = s.autoBindExternalSubtitles
                preferExternalSubtitlesOnStart = s.preferExternalSubtitlesOnStart
                preferredSubtitleLanguage = s.preferredSubtitleLanguage
                defaultStreamingQuality = s.defaultStreamingQuality
                enableEmbeddedChapters = s.enableEmbeddedChapters
                bluetoothAutoSyncEnabled = s.bluetoothAudioAutoSync
                bluetoothConfiguredOffsetMs = s.bluetoothAudioOffsetMs
                audioDelayProcessor.deArtifactMode = s.audioDeArtifactMode
                audioDelayProcessor.autoDeArtifactLowBitrate = s.autoDeArtifactLowBitrate
                _state.update {
                    it.copy(
                        audioDeArtifactMode = s.audioDeArtifactMode,
                        autoDeArtifactLowBitrate = s.autoDeArtifactLowBitrate,
                        isAudioDeArtifactActive = audioDelayProcessor.isDeArtifactCurrentlyActive
                    )
                }
                latestTorrServerBaseUrl = TorrServerClient.normalizeBaseUrl(s.torrServerUrl)
                latestTorrServerAuthHeader = TorrServerClient.buildBasicAuthHeader(
                    s.torrServerUsername,
                    s.torrServerPassword
                )
                val wantNoRateSwitch = s.effectiveFrameInterpolationMode.isEnabled ||
                    (s.highRefreshRateEnabled && !s.ltpoAdaptiveRefreshEnabled)
                highRefreshOrInterpActive = wantNoRateSwitch
                _exoPlayer?.let { activePlayer ->
                    applyFrameRateStrategy(activePlayer, wantNoRateSwitch)
                }
                if (prevBtAuto != s.bluetoothAudioAutoSync || prevBtOffset != s.bluetoothAudioOffsetMs) {
                    recalculateEffectiveAudioDelay(resyncIfPlaying = false)
                }
                if (prevChaptersEnabled != s.enableEmbeddedChapters) {
                    if (!s.enableEmbeddedChapters) {
                        chapterExtractionJob?.cancel()
                        _state.update { it.copy(chapters = emptyList()) }
                    } else {
                        _state.value.currentEpisode?.let { ep ->
                            if (PlaybackResumeHelper.isLocalFileUri(ep.uri)) {
                                loadEmbeddedChaptersAsync(ep)
                            }
                        }
                    }
                }
            }
        }
    }

    @Synchronized
    private fun ensureExoPlayer(): ExoPlayer {
        _exoPlayer?.let { return it }
        val player = createConfiguredExoPlayer(configuredDecoderMode)
        _exoPlayer = player
        applyFrameRateStrategy(player, highRefreshOrInterpActive)
        applyPlaybackEndModeToExo(_state.value.playbackEndMode)
        audioBoostController.onAudioSessionIdChanged(player.audioSessionId)
        boundSurface?.let { surf ->
            if (surf.isValid) player.setVideoSurface(surf)
        }
        return player
    }

    private fun applyFrameRateStrategy(player: ExoPlayer, keepPanelAtHighRefresh: Boolean) {
        player.videoChangeFrameRateStrategy = if (keepPanelAtHighRefresh) {
            C.VIDEO_CHANGE_FRAME_RATE_STRATEGY_OFF
        } else {
            C.VIDEO_CHANGE_FRAME_RATE_STRATEGY_ONLY_IF_SEAMLESS
        }
    }

    private fun createConfiguredExoPlayer(mode: DecoderMode): ExoPlayer {
        val renderersFactory = object : DefaultRenderersFactory(appContext) {
            override fun buildAudioSink(
                context: Context,
                enableFloatOutput: Boolean,
                enableAudioTrackPlaybackParams: Boolean
            ): AudioSink? {
                val audioCapabilities = AudioCapabilities.getCapabilities(context)
                return DefaultAudioSink.Builder(context)
                    .setAudioCapabilities(audioCapabilities)
                    .setEnableFloatOutput(true)
                    .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
                    .setAudioProcessors(arrayOf(ChannelMixingAudioProcessor(), audioDelayProcessor))
                    .build()
            }

            override fun buildAudioRenderers(
                context: Context,
                extensionRendererMode: Int,
                mediaCodecSelector: MediaCodecSelector,
                enableDecoderFallback: Boolean,
                audioSink: AudioSink,
                eventHandler: android.os.Handler,
                eventListener: androidx.media3.exoplayer.audio.AudioRendererEventListener,
                out: java.util.ArrayList<androidx.media3.exoplayer.Renderer>
            ) {
                super.buildAudioRenderers(
                    context,
                    extensionRendererMode,
                    mediaCodecSelector,
                    enableDecoderFallback,
                    audioSink,
                    eventHandler,
                    eventListener,
                    out
                )
                for (i in out.indices) {
                    if (out[i] is androidx.media3.exoplayer.audio.MediaCodecAudioRenderer) {
                        out[i] = object : androidx.media3.exoplayer.audio.MediaCodecAudioRenderer(
                            context,
                            codecAdapterFactory,
                            mediaCodecSelector,
                            enableDecoderFallback,
                            eventHandler,
                            eventListener,
                            audioSink
                        ) {
                            override fun getPositionUs(): Long {
                                val baseUs = super.getPositionUs()
                                if (baseUs == C.TIME_UNSET || baseUs == Long.MIN_VALUE) return baseUs
                                val offsetUs = audioDelayProcessor.mediaClockOffsetUs
                                return if (offsetUs < 0L) {
                                    (baseUs + offsetUs).coerceAtLeast(0L)
                                } else {
                                    baseUs
                                }
                            }
                        }
                    }
                }
            }
        }.apply {
            when (mode) {
                DecoderMode.HW_PLUS -> {
                    setEnableDecoderFallback(true)
                    setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                    setMediaCodecSelector(MediaCodecSelector.DEFAULT)
                }
                DecoderMode.HW_STRICT -> {
                    setEnableDecoderFallback(false)
                    setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
                    setMediaCodecSelector(MediaCodecSelector.DEFAULT)
                }
                DecoderMode.SOFTWARE_PREFER -> {
                    setEnableDecoderFallback(true)
                    setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
                    setMediaCodecSelector { mimeType, requiresSecureDecoder, requiresTunnelingDecoder ->
                        val defaultList = MediaCodecSelector.DEFAULT.getDecoderInfos(
                            mimeType,
                            requiresSecureDecoder,
                            requiresTunnelingDecoder
                        )
                        defaultList.sortedBy { info ->
                            val n = info.name.lowercase()
                            val isSw = n.startsWith("c2.android.") || n.startsWith("omx.google.") || !info.hardwareAccelerated
                            if (isSw) 0 else 1
                        }
                    }
                }
            }
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                /* minBufferMs = */ 15_000,
                /* maxBufferMs = */ 50_000,
                /* bufferForPlaybackMs = */ 2_000,
                /* bufferForPlaybackAfterRebufferMs = */ 4_000
            )
            .setBackBuffer(
                /* backBufferDurationMs = */ 30_000,
                /* retainBackBufferFromKeyframe = */ true
            )
            .build()

        return ExoPlayer.Builder(appContext, renderersFactory)
            .setLoadControl(loadControl)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .setSeekBackIncrementMs(10_000L)
            .setSeekForwardIncrementMs(10_000L)
            .build()
            .also { player ->
                player.addListener(playerListener)
                player.addAnalyticsListener(analyticsListener)
                player.setVideoFrameMetadataListener(videoFrameMetadataListener)
            }
    }

    fun registerPlayerSwapListener(listener: (ExoPlayer) -> Unit) {
        playerChangedListeners.add(listener)
    }

    fun unregisterPlayerSwapListener(listener: (ExoPlayer) -> Unit) {
        playerChangedListeners.remove(listener)
    }

    fun ensurePlaybackServiceStarted() {
        if (controllerFuture != null) return
        runCatching {
            val token = SessionToken(
                appContext,
                ComponentName(appContext, PlaybackService::class.java)
            )
            val future = MediaController.Builder(appContext, token).buildAsync()
            controllerFuture = future
            future.addListener({}, MoreExecutors.directExecutor())
        }
    }

    fun attachVideoSurface(surface: Surface) {
        boundSurface = surface
        ensureExoPlayer().setVideoSurface(surface)
    }

    fun detachVideoSurface(surface: Surface) {
        if (boundSurface === surface) {
            _exoPlayer?.clearVideoSurface(surface)
            boundSurface = null
        }
    }

    fun startPlaylist(
        episodes: List<EpisodeItem>,
        startIndex: Int,
        autoResume: Boolean = true
    ) {
        if (episodes.isEmpty()) return
        // Persist any currently playing episode before replacing the playlist (e.g., when switching while in PiP)
        persistCurrentProgress()
        val safeIndex = startIndex.coerceIn(episodes.indices)
        val initialEpisode = episodes[safeIndex]
        pendingNextVideoWidth = 0
        pendingNextVideoHeight = 0
        detectedVideoWidth = initialEpisode.videoWidth.coerceAtLeast(0)
        detectedVideoHeight = initialEpisode.videoHeight.coerceAtLeast(0)
        _state.update {
            it.copy(
                playlist = episodes,
                currentEpisodeIndex = safeIndex,
                videoWidth = detectedVideoWidth,
                videoHeight = detectedVideoHeight
            )
        }
        ensurePlaybackServiceStarted()

        engineScope.launch {
            val (settings, progressByUri) = withContext(Dispatchers.IO) {
                val s = repository.settingsFlow.first()
                val map = repository.getSavedProgressMapForEpisodes(episodes)
                s to map
            }

            preferExternalAudioOnStart = settings.preferExternalAudioOnStart
            preferredAudioLanguage = settings.preferredAudioLanguage
            autoBindExternalSubtitles = settings.autoBindExternalSubtitles
            preferExternalSubtitlesOnStart = settings.preferExternalSubtitlesOnStart
            preferredSubtitleLanguage = settings.preferredSubtitleLanguage
            defaultStreamingQuality = settings.defaultStreamingQuality
            bluetoothAutoSyncEnabled = settings.bluetoothAudioAutoSync
            bluetoothConfiguredOffsetMs = settings.bluetoothAudioOffsetMs
            bluetoothSyncController.refreshRouteState()
            recalculateEffectiveAudioDelay(resyncIfPlaying = false)
            if (configuredDecoderMode == null) {
                configuredDecoderMode = settings.decoderMode
            } else if (settings.decoderMode != configuredDecoderMode) {
                switchDecoderMode(settings.decoderMode)
            }
            val activePlayer = ensureExoPlayer()
            applyFrameRateStrategy(
                activePlayer,
                settings.effectiveFrameInterpolationMode.isEnabled ||
                    (settings.highRefreshRateEnabled && !settings.ltpoAdaptiveRefreshEnabled)
            )

            for (ep in episodes) {
                val saved = progressByUri[ep.uri]
                if (saved?.manualAudioUri != null) {
                    val list = extraAudioByVideo.getOrPut(ep.uri) { mutableListOf() }
                    if (list.none { it.uri == saved.manualAudioUri }) {
                        list.add(
                            ExternalAudioTrack(
                                uri = saved.manualAudioUri,
                                displayName = saved.manualAudioName ?: "Внешнее аудио",
                                sourceFolderName = "Вручную",
                                extension = MediaAutoMatcher.extensionOf(saved.manualAudioName ?: "mka"),
                                isAutoMatched = false
                            )
                        )
                    }
                }
                if (saved?.manualSubtitleUri != null) {
                    val list = extraSubsByVideo.getOrPut(ep.uri) { mutableListOf() }
                    if (list.none { it.uri == saved.manualSubtitleUri }) {
                        val subName = saved.manualSubtitleName ?: "Субтитры.srt"
                        list.add(
                            ExternalSubtitleTrack(
                                uri = saved.manualSubtitleUri,
                                displayName = subName,
                                sourceFolderName = "Вручную",
                                format = MediaAutoMatcher.subtitleFormatOf(subName),
                                isAutoMatched = false
                            )
                        )
                    }
                }
            }

            val startEpisode = episodes[safeIndex]
            val savedProgress = progressByUri[startEpisode.uri]
            val effectiveSavedDuration = (savedProgress?.durationMs ?: 0L).coerceAtLeast(startEpisode.durationMs)
            val effectiveSavedPos = savedProgress?.positionMs ?: startEpisode.resumePositionMs
            val startPos = PlaybackResumeHelper.computeResumePositionMs(
                positionMs = effectiveSavedPos,
                durationMs = effectiveSavedDuration,
                resumeEnabled = autoResume && settings.resumePlayback
            )

            latestTorrServerBaseUrl = TorrServerClient.normalizeBaseUrl(settings.torrServerUrl)
            latestTorrServerAuthHeader = TorrServerClient.buildBasicAuthHeader(
                settings.torrServerUsername,
                settings.torrServerPassword
            )
            lastTrackedEpisodeIndex = safeIndex
            lastKnownDurationForTrackedEpisodeMs = effectiveSavedDuration
            awaitingInitialDurationVerifyIndex = if (startPos > 0L) safeIndex else -1
            lastManualEpisodeStartIndex = safeIndex
            lastManualEpisodeStartRealtimeMs = SystemClock.elapsedRealtime()
            lastManualEpisodeInitialPosMs = startPos
            detectedVideoWidth = startEpisode.videoWidth.takeIf { it > 0 } ?: (savedProgress?.videoWidth ?: 0)
            detectedVideoHeight = startEpisode.videoHeight.takeIf { it > 0 } ?: (savedProgress?.videoHeight ?: 0)
            lastFramePtsUs = C.TIME_UNSET
            ptsSampleCount = 0
            autoAppliedDefaultAudioForVideo.clear()
            autoAppliedDefaultSubForVideo.clear()
            val sources = withContext(Dispatchers.IO) {
                episodes.map { buildMediaSourceForEpisode(it) }
            }

            val defaultSpd = settings.defaultPlaybackSpeed.coerceIn(0.25f, 3.0f)
            val epSavedSpd = savedProgress?.playbackSpeed
            val effectiveSpd = if (settings.rememberPerEpisodeSettings && epSavedSpd != null && epSavedSpd > 0f) {
                epSavedSpd.coerceIn(0.25f, 3.0f)
            } else {
                defaultSpd
            }
            basePlaybackSpeed = effectiveSpd
            activePlayer.playbackParameters = PlaybackParameters(effectiveSpd)
            // Volume boost is player-only above 100% system volume, so always start at 100%
            val appliedBoost = audioBoostController.setBoostPercent(100)
            _positionMs.value = startPos

            val initialQualityPreset = settings.defaultStreamingQuality
            val initialQualityId = if (initialQualityPreset.maxHeightPx in 1..4320) {
                "cap:${initialQualityPreset.maxHeightPx}"
            } else {
                "auto"
            }

            // Apply initial video quality constraint BEFORE prepare() so ExoPlayer never restarts buffering
            activePlayer.trackSelectionParameters = activePlayer.trackSelectionParameters
                .buildUpon()
                .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                .apply {
                    if (initialQualityPreset.maxHeightPx in 1..4320) {
                        setMaxVideoSize(Int.MAX_VALUE, initialQualityPreset.maxHeightPx)
                    } else {
                        clearVideoSizeConstraints()
                    }
                }
                .build()

            _state.update {
                it.copy(
                    playlist = episodes,
                    currentEpisodeIndex = safeIndex,
                    currentPositionMs = startPos,
                    durationMs = effectiveSavedDuration,
                    videoWidth = detectedVideoWidth,
                    videoHeight = detectedVideoHeight,
                    chapters = emptyList(),
                    sourceFrameRate = 0f,
                    videoMimeType = "",
                    videoBitrateBps = 0L,
                    audioMimeType = "",
                    audioSampleRateHz = 0,
                    audioChannelCount = 0,
                    selectedVideoQualityId = initialQualityId,
                    playbackSpeed = effectiveSpd,
                    isTempSpeedBoostActive = false,
                    audioBoostPercent = appliedBoost,
                    audioDelayMs = 0L,
                    subtitleDelayMs = 0L,
                    abLoopStartMs = null,
                    abLoopEndMs = null,
                    resumedFromPositionMs = startPos.takeIf { pos -> pos > 0L },
                    resumeEventId = System.currentTimeMillis(),
                    errorMessage = null
                )
            }

            manualAudioDelayMs = 0L
            bluetoothSyncController.refreshRouteState()
            recalculateEffectiveAudioDelay(resyncIfPlaying = false)
            suppressNextPlaylistChangedTransition = true
            activePlayer.setMediaSources(sources, safeIndex, startPos)
            activePlayer.prepare()
            activePlayer.playWhenReady = true

            val startWidth = detectedVideoWidth
            val startHeight = detectedVideoHeight
            withContext(Dispatchers.IO) {
                if (PlaybackResumeHelper.isMediaFileExists(appContext, startEpisode.uri) ||
                    !PlaybackResumeHelper.isLocalFileUri(startEpisode.uri)
                ) {
                    repository.savePlaybackProgress(
                        episode = startEpisode,
                        positionMs = startPos,
                        durationMs = effectiveSavedDuration,
                        videoWidth = startWidth,
                        videoHeight = startHeight
                    )
                }
            }

            onEpisodeActivated(startEpisode)
            startPositionTicker()
        }
    }

    private fun buildMediaSourceForEpisode(episode: EpisodeItem): MediaSource {
        val httpFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("negai/2.0 ExoPlayer")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(30_000)
            .setReadTimeoutMs(45_000)

        val authHeader = latestTorrServerAuthHeader
        if (authHeader != null && episode.uri.startsWith(latestTorrServerBaseUrl, ignoreCase = true)) {
            httpFactory.setDefaultRequestProperties(mapOf("Authorization" to authHeader))
        }

        val dataSourceFactory = DefaultDataSource.Factory(appContext, httpFactory)
        val extractorsFactory = DefaultExtractorsFactory()
            .setConstantBitrateSeekingEnabled(true)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory, extractorsFactory)

        val metadata = MediaMetadata.Builder()
            .setTitle(episode.cleanTitle)
            .setArtist(episode.folderName)
            .setAlbumTitle(episode.folderName)
            .setSubtitle(episode.episodeBadge ?: episode.folderName)
            .setDisplayTitle(episode.cleanTitle)
            .build()

        val videoItem = MediaItem.Builder()
            .setUri(episode.uri)
            .setMediaId(episode.uri)
            .setMediaMetadata(metadata)
            .build()

        val videoSource = mediaSourceFactory.createMediaSource(videoItem)
        val allExtAudio = combinedExternalAudioFor(episode)
        if (allExtAudio.isEmpty()) {
            return videoSource
        }

        val audioSources = allExtAudio.map { ext ->
            val audioItem = MediaItem.Builder()
                .setUri(ext.uri)
                .setMediaId("ext_audio:${ext.uri}")
                .build()
            mediaSourceFactory.createMediaSource(audioItem)
        }

        return MergingMediaSource(
            /* adjustPeriodTimeOffsets = */ true,
            /* clipDurations = */ false,
            videoSource,
            *audioSources.toTypedArray()
        )
    }

    private fun combinedExternalAudioFor(episode: EpisodeItem): List<ExternalAudioTrack> {
        val manual = extraAudioByVideo[episode.uri].orEmpty()
        val existingUris = episode.autoAudioTracks.map { it.uri }.toSet()
        return episode.autoAudioTracks + manual.filter { it.uri !in existingUris }
    }

    private fun combinedExternalSubtitlesFor(episode: EpisodeItem): List<ExternalSubtitleTrack> {
        val manual = extraSubsByVideo[episode.uri].orEmpty()
        val existingUris = episode.autoSubtitleTracks.map { it.uri }.toSet()
        return manual.filter { it.uri !in existingUris } + episode.autoSubtitleTracks
    }

    private fun onEpisodeActivated(episode: EpisodeItem) {
        engineScope.launch {
            val settings = repository.settingsFlow.first()
            enableEmbeddedChapters = settings.enableEmbeddedChapters
            // For HTTP/TorrServer streams, avoid opening a parallel Range request to the end of the MKV
            // stream while ExoPlayer is buffering the start of the stream (which causes TorrServer cache drops).
            // Embedded chapters on streams are extracted from Media3 tracks in refreshTracksState instead.
            if (settings.enableEmbeddedChapters && PlaybackResumeHelper.isLocalFileUri(episode.uri)) {
                loadEmbeddedChaptersAsync(episode)
            } else if (!settings.enableEmbeddedChapters) {
                _state.update { it.copy(chapters = emptyList()) }
            }

            val extSubs = combinedExternalSubtitlesFor(episode)
            val hasManualExtSub = extSubs.any { !it.isAutoMatched }
            val shouldAutoSelectExtSub = extSubs.isNotEmpty() &&
                (hasManualExtSub || (settings.autoBindExternalSubtitles && settings.preferExternalSubtitlesOnStart))
            val chosenExtSub = if (shouldAutoSelectExtSub) {
                MediaAutoMatcher.pickBestExternalSubtitleTrack(
                    tracks = extSubs,
                    preferredLanguage = settings.preferredSubtitleLanguage
                ) ?: extSubs.first()
            } else {
                null
            }

            if (chosenExtSub != null) {
                autoAppliedDefaultSubForVideo.add(episode.uri)
                selectExternalSubtitle(chosenExtSub)
            } else {
                activeExternalSubtitleDoc = null
                _state.update {
                    it.copy(
                        selectedSubtitleId = "off",
                        activeSubtitleCues = emptyList()
                    )
                }
                _exoPlayer?.let { refreshTracksState(it.currentTracks) }
            }
        }
    }

    private fun loadEmbeddedChaptersAsync(episode: EpisodeItem) {
        chapterExtractionJob?.cancel()
        val targetUri = episode.uri
        val knownDur = _state.value.durationMs.coerceAtLeast(episode.durationMs)
        val baseUrl = latestTorrServerBaseUrl
        val auth = latestTorrServerAuthHeader
        chapterExtractionJob = engineScope.launch {
            val extracted = withContext(Dispatchers.IO) {
                VideoChapterExtractor.extractChapters(
                    context = appContext,
                    videoUri = targetUri,
                    totalDurationMs = knownDur,
                    torrServerBaseUrl = baseUrl,
                    torrServerAuthHeader = auth
                )
            }
            if (_state.value.currentEpisode?.uri == targetUri && enableEmbeddedChapters && extracted.isNotEmpty()) {
                val latestDur = _state.value.durationMs.coerceAtLeast(knownDur)
                val updated = extracted.mapIndexed { idx, ch ->
                    if (idx == extracted.lastIndex && ch.endTimeMs <= ch.startTimeMs && latestDur > ch.startTimeMs) {
                        ch.copy(endTimeMs = latestDur)
                    } else {
                        ch
                    }
                }
                _state.update { it.copy(chapters = updated) }
            }
        }
    }

    private fun refreshTracksState(tracks: Tracks) {
        val player = _exoPlayer ?: return
        val episode = _state.value.currentEpisode ?: return
        val extAudioList = combinedExternalAudioFor(episode)
        val extSubList = combinedExternalSubtitlesFor(episode)

        val audioGroups = tracks.groups
            .mapIndexedNotNull { idx, group ->
                if (group.type == C.TRACK_TYPE_AUDIO) idx to group else null
            }

        val embeddedAudioCount = (audioGroups.size - extAudioList.size).coerceAtLeast(0)
        val hasManualExtAudio = extAudioList.any { !it.isAutoMatched }

        if (audioGroups.isNotEmpty() && episode.uri !in autoAppliedDefaultAudioForVideo) {
            var targetGroupPair: Pair<Int, Tracks.Group>? = null
            var targetTrackIdx = 0

            if (extAudioList.isNotEmpty() &&
                (preferExternalAudioOnStart || hasManualExtAudio) &&
                audioGroups.size >= extAudioList.size
            ) {
                val bestExt = MediaAutoMatcher.pickBestExternalAudioTrack(extAudioList, preferredAudioLanguage)
                val targetAudioListIdx = if (bestExt != null) {
                    extAudioList.indexOf(bestExt).takeIf { it >= 0 } ?: 0
                } else {
                    0
                }
                targetGroupPair = audioGroups.getOrNull(embeddedAudioCount + targetAudioListIdx)
                targetTrackIdx = 0
            } else if (preferredAudioLanguage != PreferredTrackLanguage.AUTO) {
                // Search embedded audio tracks for preferred language
                outer@ for (seqIdx in 0 until embeddedAudioCount.coerceAtMost(audioGroups.size)) {
                    val pair = audioGroups[seqIdx]
                    val grp = pair.second
                    for (tIdx in 0 until grp.length) {
                        val fmt = grp.getTrackFormat(tIdx)
                        if (preferredAudioLanguage.matchesLanguageOrLabel(fmt.language, fmt.label.orEmpty())) {
                            targetGroupPair = pair
                            targetTrackIdx = tIdx
                            break@outer
                        }
                    }
                }
            }

            if (targetGroupPair != null) {
                autoAppliedDefaultAudioForVideo.add(episode.uri)
                val group = targetGroupPair.second
                // Only mutate trackSelectionParameters if the target track is not already selected!
                // Mutating trackSelectionParameters unnecessarily causes MergingMediaSource to flush & re-buffer.
                if (!group.isTrackSelected(targetTrackIdx)) {
                    val override = TrackSelectionOverride(group.mediaTrackGroup, targetTrackIdx)
                    player.trackSelectionParameters = player.trackSelectionParameters
                        .buildUpon()
                        .setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, false)
                        .clearOverridesOfType(C.TRACK_TYPE_AUDIO)
                        .addOverride(override)
                        .build()
                    return
                }
            } else if (tracks.groups.isNotEmpty()) {
                autoAppliedDefaultAudioForVideo.add(episode.uri)
            }
        }

        // Auto-select preferred embedded subtitle language if no external subtitle was activated
        if (episode.uri !in autoAppliedDefaultSubForVideo &&
            _state.value.selectedSubtitleId == "off" &&
            activeExternalSubtitleDoc == null &&
            preferredSubtitleLanguage != PreferredTrackLanguage.AUTO
        ) {
            val textGroups = tracks.groups.mapIndexedNotNull { idx, group ->
                if (group.type == C.TRACK_TYPE_TEXT) idx to group else null
            }
            if (textGroups.isNotEmpty()) {
                autoAppliedDefaultSubForVideo.add(episode.uri)
                var matchedSubGroup: Pair<Int, Tracks.Group>? = null
                var matchedSubTrackIdx = 0
                // Prefer full subtitles over signs-only
                for ((rawGroupIdx, grp) in textGroups) {
                    for (tIdx in 0 until grp.length) {
                        val fmt = grp.getTrackFormat(tIdx)
                        val label = fmt.label.orEmpty()
                        if (preferredSubtitleLanguage.matchesLanguageOrLabel(fmt.language, label)) {
                            val isSigns = label.contains("sign", ignoreCase = true) ||
                                label.contains("надпис", ignoreCase = true) ||
                                label.contains("forced", ignoreCase = true)
                            if (matchedSubGroup == null || !isSigns) {
                                matchedSubGroup = rawGroupIdx to grp
                                matchedSubTrackIdx = tIdx
                                if (!isSigns) break
                            }
                        }
                    }
                }
                if (matchedSubGroup != null) {
                    val (rawGroupIdx, grp) = matchedSubGroup
                    val newSubId = "emb:$rawGroupIdx:$matchedSubTrackIdx"
                    _state.update { it.copy(selectedSubtitleId = newSubId) }
                    if (!grp.isTrackSelected(matchedSubTrackIdx)) {
                        val override = TrackSelectionOverride(grp.mediaTrackGroup, matchedSubTrackIdx)
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
                            .clearOverridesOfType(C.TRACK_TYPE_TEXT)
                            .addOverride(override)
                            .build()
                        return
                    }
                }
            }
        }

        val audioOptions = mutableListOf<AudioTrackUiOption>()
        var selectedAudioFormat: Format? = null
        var selectedResolvedBitrateKbps = 0
        audioGroups.forEachIndexed { audioSeqIndex, (rawGroupIndex, group) ->
            val isExt = audioSeqIndex >= embeddedAudioCount && extAudioList.isNotEmpty()
            val extMeta = if (isExt) extAudioList.getOrNull(audioSeqIndex - embeddedAudioCount) else null

            for (tIdx in 0 until group.length) {
                val format = group.getTrackFormat(tIdx)
                val selected = group.isTrackSelected(tIdx)
                val bitrateKbps = resolveAudioTrackBitrateKbps(
                    format = format,
                    episode = episode,
                    extMeta = extMeta,
                    embeddedAudioIndex = audioSeqIndex
                )
                if (selected) {
                    selectedAudioFormat = format
                    selectedResolvedBitrateKbps = bitrateKbps
                }
                val label = if (extMeta != null) {
                    extMeta.formattedLabel
                } else {
                    buildEmbeddedTrackLabel(format, audioSeqIndex + 1, "Аудиодорожка")
                }
                val codecShort = formatShortAudioCodecBadge(format.sampleMimeType)
                val bitrateSuffix = if (bitrateKbps > 0) " • ${bitrateKbps}k" else ""
                val badge = when {
                    extMeta != null && extMeta.isAutoMatched -> "АВТО • ${extMeta.extension.uppercase()}$bitrateSuffix"
                    extMeta != null -> "ВНЕШ • ${extMeta.extension.uppercase()}$bitrateSuffix"
                    else -> "$codecShort$bitrateSuffix"
                }
                audioOptions.add(
                    AudioTrackUiOption(
                        id = "aud:$rawGroupIndex:$tIdx",
                        label = label,
                        badge = badge,
                        groupIndex = rawGroupIndex,
                        trackIndex = tIdx,
                        isSelected = selected,
                        isExternal = isExt,
                        bitrateKbps = bitrateKbps
                    )
                )
            }
        }
        selectedAudioFormat?.let { updateDetectedAudioFormat(it, selectedResolvedBitrateKbps) }

        val currentSubId = _state.value.selectedSubtitleId
        val subOptions = mutableListOf<SubtitleTrackUiOption>()
        subOptions.add(
            SubtitleTrackUiOption(
                id = "off",
                label = "Выключены",
                badge = "OFF",
                isExternal = false,
                isSelected = currentSubId == "off"
            )
        )

        for (extSub in extSubList) {
            val id = "ext:${extSub.uri}"
            subOptions.add(
                SubtitleTrackUiOption(
                    id = id,
                    label = extSub.formattedLabel,
                    badge = if (extSub.isAutoMatched) "АВТО • ${extSub.format.label}" else "ВНЕШ • ${extSub.format.label}",
                    isExternal = true,
                    externalTrack = extSub,
                    isSelected = currentSubId == id
                )
            )
        }

        tracks.groups.forEachIndexed { rawGroupIdx, group ->
            if (group.type == C.TRACK_TYPE_TEXT) {
                for (tIdx in 0 until group.length) {
                    val fmt = group.getTrackFormat(tIdx)
                    val id = "emb:$rawGroupIdx:$tIdx"
                    subOptions.add(
                        SubtitleTrackUiOption(
                            id = id,
                            label = buildEmbeddedTrackLabel(fmt, tIdx + 1, "Встроенные субтитры"),
                            badge = fmt.sampleMimeType?.substringAfter('/')?.uppercase() ?: "MKV",
                            isExternal = false,
                            embeddedGroupIndex = rawGroupIdx,
                            embeddedTrackIndex = tIdx,
                            isSelected = currentSubId == id
                        )
                    )
                }
            }
        }

        val videoQualityOptions = buildVideoQualityOptions(tracks, episode)

        val fallbackId3Chapters = if (enableEmbeddedChapters && _state.value.chapters.isEmpty()) {
            VideoChapterExtractor.extractFromMedia3Tracks(tracks, _state.value.durationMs)
        } else {
            emptyList()
        }

        _state.update {
            it.copy(
                audioTrackOptions = audioOptions,
                subtitleTrackOptions = subOptions,
                videoQualityOptions = videoQualityOptions,
                chapters = if (it.chapters.isEmpty() && fallbackId3Chapters.isNotEmpty()) {
                    fallbackId3Chapters
                } else {
                    it.chapters
                }
            )
        }
    }

    private fun buildVideoQualityOptions(
        tracks: Tracks,
        currentEpisode: EpisodeItem
    ): List<VideoQualityUiOption> {
        val currentQualityId = _state.value.selectedVideoQualityId
        val isLocalFile = PlaybackResumeHelper.isLocalFileUri(currentEpisode.uri)
        val concreteOptions = mutableListOf<VideoQualityUiOption>()

        val videoGroups = tracks.groups.mapIndexedNotNull { idx, group ->
            if (group.type == C.TRACK_TYPE_VIDEO) idx to group else null
        }

        val seenTrackHeights = mutableSetOf<Int>()
        for ((rawGroupIdx, group) in videoGroups) {
            for (tIdx in 0 until group.length) {
                val fmt = group.getTrackFormat(tIdx)
                val w = fmt.width.coerceAtLeast(0)
                val h = fmt.height.coerceAtLeast(0)
                if (h <= 0) continue
                val brMbps = if (fmt.bitrate > 0) String.format(java.util.Locale.US, "%.1f Мбит/с", fmt.bitrate / 1_000_000f) else ""
                val codec = fmt.sampleMimeType?.substringAfter('/')?.uppercase().orEmpty()
                val badge = listOfNotNull(
                    "${h}p",
                    codec.takeIf { it.isNotBlank() },
                    brMbps.takeIf { it.isNotBlank() }
                ).joinToString(" • ")
                val trackId = "trk:$rawGroupIdx:$tIdx"
                seenTrackHeights.add(h)
                concreteOptions.add(
                    VideoQualityUiOption(
                        id = trackId,
                        label = if (w > 0) "${h}p ($w×$h)" else "${h}p",
                        badge = badge,
                        width = w,
                        height = h,
                        heightPx = h,
                        bitrateBps = fmt.bitrate.coerceAtLeast(0).toLong(),
                        groupIndex = rawGroupIdx,
                        trackIndex = tIdx,
                        isSelected = currentQualityId == trackId
                    )
                )
            }
        }

        // Check if the playlist contains sibling resolution files for the same episode number (e.g., in torrents/folders)
        val playlist = _state.value.playlist
        val currentEpNum = currentEpisode.episodeNumber
        val resRegex = Regex("""(2160p|4k|1440p|2k|1080p|fhd|720p|hd|480p|360p)""", RegexOption.IGNORE_CASE)
        if (playlist.size > 1) {
            playlist.forEachIndexed { epIdx, candidate ->
                val sameEpisode = (currentEpNum != null && candidate.episodeNumber == currentEpNum) ||
                    candidate.cleanTitle.equals(currentEpisode.cleanTitle, ignoreCase = true)
                if (sameEpisode && candidate.uri != currentEpisode.uri) {
                    val resMatch = resRegex.find(candidate.fileName)?.value?.uppercase()
                    if (resMatch != null) {
                        val epOptId = "ep:$epIdx"
                        concreteOptions.add(
                            VideoQualityUiOption(
                                id = epOptId,
                                label = "Файл $resMatch — ${candidate.fileName}",
                                badge = "ТОРРЕНТ / ФАЙЛ • $resMatch",
                                playlistEpisodeIndex = epIdx,
                                isSelected = _state.value.currentEpisodeIndex == epIdx || currentQualityId == epOptId
                            )
                        )
                    }
                }
            }
        }

        // For local files with only a single video quality, do not expose artificial streaming caps or multi-option selector
        if (isLocalFile && concreteOptions.size <= 1) {
            return concreteOptions.map { it.copy(isSelected = true) }
        }

        val options = mutableListOf<VideoQualityUiOption>()
        options.add(
            VideoQualityUiOption(
                id = "auto",
                label = "Авто (Макс. доступное)",
                badge = "AUTO",
                isSelected = currentQualityId == "auto"
            )
        )
        options.addAll(concreteOptions)

        // Only provide standard streaming resolution caps for network streams
        if (!isLocalFile) {
            val standardCaps = listOf(
                2160 to "4K Ultra HD (до 2160p)",
                1080 to "Full HD (до 1080p)",
                720 to "HD (до 720p)",
                480 to "SD (до 480p)"
            )
            for ((capH, capLabel) in standardCaps) {
                val capId = "cap:$capH"
                if (options.none { it.heightPx == capH && it.groupIndex >= 0 }) {
                    options.add(
                        VideoQualityUiOption(
                            id = capId,
                            label = capLabel,
                            badge = "${capH}p MAX",
                            heightPx = capH,
                            isSelected = currentQualityId == capId
                        )
                    )
                }
            }
        }

        if (options.none { it.isSelected } && options.isNotEmpty()) {
            options[0] = options[0].copy(isSelected = true)
        }
        return options
    }

    fun selectVideoQualityOption(option: VideoQualityUiOption) {
        val player = ensureExoPlayer()
        when {
            option.playlistEpisodeIndex >= 0 -> {
                val targetIdx = option.playlistEpisodeIndex
                val playlist = _state.value.playlist
                if (targetIdx in playlist.indices && targetIdx != player.currentMediaItemIndex) {
                    val currentPos = player.currentPosition.coerceAtLeast(0L)
                    val wasPlaying = player.playWhenReady
                    lastTrackedEpisodeIndex = targetIdx
                    lastManualEpisodeStartIndex = targetIdx
                    lastManualEpisodeStartRealtimeMs = SystemClock.elapsedRealtime()
                    lastManualEpisodeInitialPosMs = currentPos
                    _state.update {
                        it.copy(
                            currentEpisodeIndex = targetIdx,
                            currentPositionMs = currentPos,
                            selectedVideoQualityId = option.id
                        )
                    }
                    player.seekTo(targetIdx, currentPos)
                    player.playWhenReady = wasPlaying
                }
            }
            option.groupIndex >= 0 && option.trackIndex >= 0 -> {
                val group = player.currentTracks.groups.getOrNull(option.groupIndex) ?: return
                val override = TrackSelectionOverride(group.mediaTrackGroup, option.trackIndex)
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .setTrackTypeDisabled(C.TRACK_TYPE_VIDEO, false)
                    .clearVideoSizeConstraints()
                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                    .addOverride(override)
                    .build()
                _state.update { it.copy(selectedVideoQualityId = option.id) }
                refreshTracksState(player.currentTracks)
            }
            option.id.startsWith("cap:") && option.heightPx > 0 -> {
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .setTrackTypeDisabled(C.TRACK_TYPE_VIDEO, false)
                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                    .setMaxVideoSize(Int.MAX_VALUE, option.heightPx)
                    .build()
                _state.update { it.copy(selectedVideoQualityId = option.id) }
                refreshTracksState(player.currentTracks)
            }
            else -> {
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .setTrackTypeDisabled(C.TRACK_TYPE_VIDEO, false)
                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                    .clearVideoSizeConstraints()
                    .build()
                _state.update { it.copy(selectedVideoQualityId = "auto") }
                refreshTracksState(player.currentTracks)
            }
        }
    }

    private fun buildEmbeddedTrackLabel(format: Format, number: Int, fallbackPrefix: String): String {
        val parts = mutableListOf<String>()
        format.label?.takeIf { it.isNotBlank() }?.let { parts.add(it) }
        format.language?.takeIf { it.isNotBlank() && it != "und" }?.uppercase()?.let { lang ->
            if (parts.none { it.contains(lang, ignoreCase = true) }) parts.add(lang)
        }
        if (format.channelCount > 0) {
            val ch = when (format.channelCount) {
                1 -> "Mono"
                2 -> "2.0 Stereo"
                6 -> "5.1 Surround"
                8 -> "7.1 Surround"
                else -> "${format.channelCount}ch"
            }
            parts.add(ch)
        }
        return if (parts.isEmpty()) "$fallbackPrefix #$number" else parts.joinToString(" • ")
    }

    fun selectAudioTrack(option: AudioTrackUiOption) {
        val player = ensureExoPlayer()
        val group = player.currentTracks.groups.getOrNull(option.groupIndex) ?: return
        val override = TrackSelectionOverride(group.mediaTrackGroup, option.trackIndex)
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, false)
            .clearOverridesOfType(C.TRACK_TYPE_AUDIO)
            .addOverride(override)
            .build()
    }

    fun attachManualExternalAudio(uri: Uri) {
        val episode = _state.value.currentEpisode ?: return
        runCatching {
            appContext.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        val name = repository.resolveFileDisplayName(uri) ?: uri.lastPathSegment ?: "ExternalAudio.mka"
        val ext = MediaAutoMatcher.extensionOf(name).ifBlank { "mka" }
        val track = ExternalAudioTrack(
            uri = uri.toString(),
            displayName = name,
            sourceFolderName = "Внешний файл",
            extension = ext,
            isAutoMatched = false
        )
        val list = extraAudioByVideo.getOrPut(episode.uri) { mutableListOf() }
        list.removeAll { it.uri == track.uri }
        list.add(0, track)

        autoAppliedDefaultAudioForVideo.remove(episode.uri)

        engineScope.launch {
            repository.savePlaybackProgress(
                episode = episode,
                positionMs = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L),
                durationMs = _state.value.durationMs,
                manualAudioUri = track.uri,
                manualAudioName = name
            )
            rebuildCurrentPlaylistPreservingPosition()
        }
    }

    fun selectSubtitleOption(option: SubtitleTrackUiOption) {
        val player = ensureExoPlayer()
        when {
            option.id == "off" -> {
                activeExternalSubtitleDoc = null
                latestEmbeddedCues = emptyList()
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
                    .clearOverridesOfType(C.TRACK_TYPE_TEXT)
                    .build()
                _state.update {
                    it.copy(
                        selectedSubtitleId = "off",
                        activeSubtitleCues = emptyList()
                    )
                }
                refreshTracksState(player.currentTracks)
            }
            option.isExternal && option.externalTrack != null -> {
                selectExternalSubtitle(option.externalTrack)
            }
            option.embeddedGroupIndex >= 0 -> {
                activeExternalSubtitleDoc = null
                val group = player.currentTracks.groups.getOrNull(option.embeddedGroupIndex) ?: return
                val override = TrackSelectionOverride(group.mediaTrackGroup, option.embeddedTrackIndex)
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
                    .clearOverridesOfType(C.TRACK_TYPE_TEXT)
                    .addOverride(override)
                    .build()
                _state.update {
                    it.copy(
                        selectedSubtitleId = option.id,
                        activeSubtitleCues = emptyList()
                    )
                }
                refreshTracksState(player.currentTracks)
            }
        }
    }

    private fun selectExternalSubtitle(track: ExternalSubtitleTrack) {
        val player = ensureExoPlayer()
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
            .clearOverridesOfType(C.TRACK_TYPE_TEXT)
            .build()

        engineScope.launch {
            val parsedDoc = withContext(Dispatchers.IO) {
                loadSubtitleDocument(track)
            }
            activeExternalSubtitleDoc = parsedDoc
            val activeCues = parsedDoc?.activeCuesAt(
                playerPositionMs = player.currentPosition.coerceAtLeast(0L),
                delayMs = _state.value.subtitleDelayMs
            ).orEmpty()

            _state.update {
                it.copy(
                    selectedSubtitleId = "ext:${track.uri}",
                    activeSubtitleCues = activeCues
                )
            }
            refreshTracksState(player.currentTracks)
        }
    }

    fun attachManualExternalSubtitle(uri: Uri) {
        val episode = _state.value.currentEpisode ?: return
        runCatching {
            appContext.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        val name = repository.resolveFileDisplayName(uri) ?: uri.lastPathSegment ?: "Subtitles.srt"
        val format = MediaAutoMatcher.subtitleFormatOf(name)
        val track = ExternalSubtitleTrack(
            uri = uri.toString(),
            displayName = name,
            sourceFolderName = "Внешний файл",
            format = format,
            isAutoMatched = false
        )
        val list = extraSubsByVideo.getOrPut(episode.uri) { mutableListOf() }
        list.removeAll { it.uri == track.uri }
        list.add(0, track)

        engineScope.launch {
            repository.savePlaybackProgress(
                episode = episode,
                positionMs = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L),
                durationMs = _state.value.durationMs,
                manualSubtitleUri = track.uri,
                manualSubtitleName = name
            )
            selectExternalSubtitle(track)
        }
    }

    private fun loadSubtitleDocument(track: ExternalSubtitleTrack): ParsedSubtitleDocument? {
        return runCatching {
            val uri = Uri.parse(track.uri)
            val bytes = when (uri.scheme?.lowercase()) {
                "file" -> uri.path?.let { File(it).readBytes() }
                "http", "https" -> {
                    val reqBuilder = okhttp3.Request.Builder().url(track.uri).get()
                    val authHeader = latestTorrServerAuthHeader
                    if (authHeader != null && track.uri.startsWith(latestTorrServerBaseUrl, ignoreCase = true)) {
                        reqBuilder.header("Authorization", authHeader)
                    }
                    TorrServerClient.httpClient.newCall(reqBuilder.build()).execute().use { resp ->
                        if (resp.isSuccessful) resp.body?.bytes() else null
                    }
                }
                else -> appContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } ?: return null

            val decodedText = SubtitleParser.decodeSubtitleBytes(bytes)
            SubtitleParser.parse(
                rawContent = decodedText,
                formatHint = track.format,
                title = track.displayName
            )
        }.getOrNull()
    }

    fun adjustSubtitleDelay(deltaMs: Long) {
        setSubtitleDelay((_state.value.subtitleDelayMs + deltaMs).coerceIn(-60_000L, 60_000L))
    }

    fun setSubtitleDelay(delayMs: Long) {
        val clamped = delayMs.coerceIn(-60_000L, 60_000L)
        val pos = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L)
        val cues = activeExternalSubtitleDoc?.activeCuesAt(
            playerPositionMs = pos,
            delayMs = clamped
        ) ?: latestEmbeddedCues
        _state.update {
            it.copy(
                subtitleDelayMs = clamped,
                activeSubtitleCues = cues
            )
        }
    }

    fun switchDecoderMode(newMode: DecoderMode) {
        configuredDecoderMode = newMode
        val existingPlayer = _exoPlayer
        if (existingPlayer == null) {
            _state.update { it.copy(activeDecoderMode = newMode) }
            engineScope.launch { repository.preferencesRepository.updateDecoderMode(newMode) }
            return
        }
        if (_state.value.activeDecoderMode == newMode) return

        val wasPlaying = existingPlayer.playWhenReady && existingPlayer.playbackState != Player.STATE_IDLE
        val savedPos = existingPlayer.currentPosition.coerceAtLeast(0L)
        val savedIndex = existingPlayer.currentMediaItemIndex.coerceAtLeast(0)
        val playlist = _state.value.playlist
        val speed = _state.value.playbackSpeed

        existingPlayer.removeListener(playerListener)
        existingPlayer.removeAnalyticsListener(analyticsListener)
        existingPlayer.clearVideoFrameMetadataListener(videoFrameMetadataListener)
        boundSurface?.let { existingPlayer.clearVideoSurface(it) }

        val freshPlayer = createConfiguredExoPlayer(newMode)
        applyFrameRateStrategy(freshPlayer, highRefreshOrInterpActive)
        freshPlayer.playbackParameters = PlaybackParameters(speed)
        boundSurface?.let { freshPlayer.setVideoSurface(it) }
        _exoPlayer = freshPlayer
        applyPlaybackEndModeToExo(_state.value.playbackEndMode)
        audioBoostController.onAudioSessionIdChanged(freshPlayer.audioSessionId)

        _state.update {
            it.copy(
                activeDecoderMode = newMode,
                activeVideoCodecName = "Переключение на ${newMode.shortBadge}…"
            )
        }

        playerChangedListeners.forEach { it(freshPlayer) }
        existingPlayer.release()

        if (playlist.isNotEmpty()) {
            val sources = playlist.map { buildMediaSourceForEpisode(it) }
            freshPlayer.setMediaSources(sources, savedIndex.coerceIn(playlist.indices), savedPos)
            freshPlayer.prepare()
            freshPlayer.playWhenReady = wasPlaying
        }

        engineScope.launch {
            repository.preferencesRepository.updateDecoderMode(newMode)
        }
    }

    private fun rebuildCurrentPlaylistPreservingPosition() {
        val player = _exoPlayer ?: return
        val playlist = _state.value.playlist
        if (playlist.isEmpty()) return
        val idx = player.currentMediaItemIndex.coerceIn(playlist.indices)
        val pos = player.currentPosition.coerceAtLeast(0L)
        val wasPlaying = player.playWhenReady
        val sources = playlist.map { buildMediaSourceForEpisode(it) }
        player.setMediaSources(sources, idx, pos)
        player.prepare()
        player.playWhenReady = wasPlaying
    }

    fun togglePlayPause() {
        val player = ensureExoPlayer()
        if (player.isPlaying) {
            player.pause()
        } else {
            if (player.playbackState == Player.STATE_ENDED) {
                player.seekTo(0L)
                _positionMs.value = 0L
            }
            player.play()
        }
    }

    fun seekBy(deltaMs: Long) {
        val player = ensureExoPlayer()
        val dur = player.duration.takeIf { it != C.TIME_UNSET && it > 0L } ?: Long.MAX_VALUE
        val target = (player.currentPosition + deltaMs).coerceIn(0L, dur)
        player.seekTo(target)
        _positionMs.value = target
        updatePositionAndSubtitlesNow(forceSyncRuntimePosition = true)
        persistCurrentProgress()
    }

    fun seekTo(positionMs: Long) {
        val player = ensureExoPlayer()
        val dur = player.duration.takeIf { it != C.TIME_UNSET && it > 0L } ?: Long.MAX_VALUE
        val target = positionMs.coerceIn(0L, dur)
        player.seekTo(target)
        _positionMs.value = target
        updatePositionAndSubtitlesNow(forceSyncRuntimePosition = true)
        persistCurrentProgress()
    }

    fun seekToChapter(chapter: VideoChapter) {
        seekTo(chapter.startTimeMs)
    }

    fun seekToNextChapter() {
        val chapters = _state.value.chapters
        if (chapters.isEmpty()) return
        val pos = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L)
        val next = chapters.firstOrNull { it.startTimeMs > pos + 400L } ?: return
        seekTo(next.startTimeMs)
    }

    fun seekToPreviousChapter() {
        val chapters = _state.value.chapters
        if (chapters.isEmpty()) return
        val pos = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L)
        // If > 3 seconds into current chapter, rewind to current chapter start; otherwise jump to previous chapter
        val currentIdx = chapters.indexOfLast { pos >= it.startTimeMs }
        if (currentIdx < 0) {
            seekTo(0L)
            return
        }
        val currentChap = chapters[currentIdx]
        val target = if (pos - currentChap.startTimeMs > 3_000L || currentIdx == 0) {
            currentChap.startTimeMs
        } else {
            chapters[currentIdx - 1].startTimeMs
        }
        seekTo(target)
    }

    fun dismissResumeBanner() {
        _state.update { it.copy(resumedFromPositionMs = null) }
    }

    fun restartFromBeginning() {
        dismissResumeBanner()
        seekTo(0L)
    }

    fun playPreviousEpisode() {
        val player = ensureExoPlayer()
        if (player.hasPreviousMediaItem()) {
            playEpisodeAtIndex(player.previousMediaItemIndex)
        } else {
            seekTo(0L)
        }
    }

    fun playNextEpisode() {
        val player = ensureExoPlayer()
        if (player.hasNextMediaItem()) {
            playEpisodeAtIndex(player.nextMediaItemIndex)
        }
    }

    fun playEpisodeAtIndex(index: Int) {
        val player = ensureExoPlayer()
        val playlist = _state.value.playlist
        if (index in playlist.indices && index != player.currentMediaItemIndex) {
            val outgoingEp = _state.value.currentEpisode
            val outgoingPos = player.currentPosition.coerceAtLeast(0L)
            val outgoingDur = player.duration.takeIf { it != C.TIME_UNSET && it > 0L } ?: _state.value.durationMs
            val outgoingW = detectedVideoWidth
            val outgoingH = detectedVideoHeight
            val targetEpisode = playlist[index]
            engineScope.launch {
                if (outgoingEp != null && (outgoingPos > 0L || outgoingDur > 0L)) {
                    val outCompleted = outgoingDur >= 10_000L &&
                        (outgoingPos >= (outgoingDur * 95L) / 100L || (outgoingDur - outgoingPos) <= 15_000L)
                    val outResolvedDur = if (outCompleted) outgoingDur.coerceAtLeast(outgoingPos) else outgoingDur
                    val outResolvedPos = if (outCompleted) outResolvedDur else outgoingPos
                    repository.savePlaybackProgress(
                        episode = outgoingEp,
                        positionMs = outResolvedPos,
                        durationMs = outResolvedDur,
                        videoWidth = outgoingW,
                        videoHeight = outgoingH
                    )
                }
                val settings = repository.settingsFlow.first()
                val saved = repository.getSavedProgressForEpisode(targetEpisode)
                val effectiveDur = (saved?.durationMs ?: 0L).coerceAtLeast(targetEpisode.durationMs)
                val resumePos = PlaybackResumeHelper.computeResumePositionMs(
                    positionMs = saved?.positionMs ?: targetEpisode.resumePositionMs,
                    durationMs = effectiveDur,
                    resumeEnabled = settings.resumePlayback
                )
                lastTrackedEpisodeIndex = index
                lastKnownDurationForTrackedEpisodeMs = effectiveDur
                awaitingInitialDurationVerifyIndex = if (resumePos > 0L) index else -1
                lastManualEpisodeStartIndex = index
                lastManualEpisodeStartRealtimeMs = SystemClock.elapsedRealtime()
                lastManualEpisodeInitialPosMs = resumePos
                val nextW = targetEpisode.videoWidth.takeIf { it > 0 } ?: (saved?.videoWidth ?: 0)
                val nextH = targetEpisode.videoHeight.takeIf { it > 0 } ?: (saved?.videoHeight ?: 0)
                pendingNextVideoWidth = 0
                pendingNextVideoHeight = 0
                detectedVideoWidth = nextW
                detectedVideoHeight = nextH
                _positionMs.value = resumePos
                _state.update {
                    it.copy(
                        currentEpisodeIndex = index,
                        currentPositionMs = resumePos,
                        durationMs = effectiveDur,
                        videoWidth = nextW,
                        videoHeight = nextH,
                        chapters = emptyList(),
                        resumedFromPositionMs = resumePos.takeIf { p -> p > 0L },
                        resumeEventId = System.currentTimeMillis(),
                        abLoopStartMs = null,
                        abLoopEndMs = null,
                        errorMessage = null
                    )
                }
                player.seekTo(index, resumePos)
                player.playWhenReady = true
                if (PlaybackResumeHelper.isMediaFileExists(appContext, targetEpisode.uri) ||
                    !PlaybackResumeHelper.isLocalFileUri(targetEpisode.uri)
                ) {
                    repository.savePlaybackProgress(
                        episode = targetEpisode,
                        positionMs = resumePos,
                        durationMs = effectiveDur
                    )
                }
            }
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        val clamped = speed.coerceIn(0.25f, 3.0f)
        basePlaybackSpeed = clamped
        _exoPlayer?.playbackParameters = PlaybackParameters(clamped)
        _state.update {
            it.copy(
                playbackSpeed = clamped,
                isTempSpeedBoostActive = false
            )
        }
        val currentEp = _state.value.playlist.getOrNull(_state.value.currentEpisodeIndex)
        if (currentEp != null) {
            engineScope.launch {
                repository.savePlaybackProgress(
                    episode = currentEp,
                    positionMs = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L),
                    durationMs = _state.value.durationMs,
                    playbackSpeed = clamped
                )
            }
        }
    }

    fun setTemporarySpeedBoost(active: Boolean, boostSpeed: Float = 2.0f) {
        val player = _exoPlayer ?: return
        if (active) {
            if (!_state.value.isTempSpeedBoostActive) {
                basePlaybackSpeed = _state.value.playbackSpeed
            }
            val target = boostSpeed.coerceIn(1.25f, 3.0f)
            player.playbackParameters = PlaybackParameters(target)
            _state.update {
                it.copy(
                    playbackSpeed = target,
                    isTempSpeedBoostActive = true
                )
            }
        } else if (_state.value.isTempSpeedBoostActive) {
            player.playbackParameters = PlaybackParameters(basePlaybackSpeed)
            _state.update {
                it.copy(
                    playbackSpeed = basePlaybackSpeed,
                    isTempSpeedBoostActive = false
                )
            }
        }
    }

    fun setAudioBoostPercent(percent: Int) {
        val applied = audioBoostController.setBoostPercent(percent)
        _state.update { it.copy(audioBoostPercent = applied) }
    }

    fun setAudioDeArtifactMode(mode: AudioDeArtifactMode) {
        audioDelayProcessor.deArtifactMode = mode
        _state.update {
            it.copy(
                audioDeArtifactMode = mode,
                isAudioDeArtifactActive = audioDelayProcessor.isDeArtifactCurrentlyActive
            )
        }
        engineScope.launch {
            repository.preferencesRepository.updateAudioDeArtifactMode(mode)
        }
    }

    fun setAutoDeArtifactLowBitrate(enabled: Boolean) {
        audioDelayProcessor.autoDeArtifactLowBitrate = enabled
        _state.update {
            it.copy(
                autoDeArtifactLowBitrate = enabled,
                isAudioDeArtifactActive = audioDelayProcessor.isDeArtifactCurrentlyActive
            )
        }
        engineScope.launch {
            repository.preferencesRepository.updateAutoDeArtifactLowBitrate(enabled)
        }
    }

    private fun formatShortAudioCodecBadge(mimeType: String?): String {
        val sub = mimeType?.substringAfter('/')?.lowercase().orEmpty()
        return when {
            sub.contains("mp4a") || sub.contains("aac") -> "AAC"
            sub.contains("opus") -> "OPUS"
            sub.contains("vorbis") -> "VORBIS"
            sub.contains("eac3") -> "E-AC3"
            sub.contains("ac3") -> "AC3"
            sub.contains("flac") -> "FLAC"
            sub.contains("truehd") -> "TRUEHD"
            sub.contains("dts") -> "DTS"
            sub.contains("mpeg") || sub.contains("mp3") -> "MP3"
            sub.contains("raw") || sub.contains("pcm") -> "PCM"
            sub.isNotBlank() -> sub.uppercase()
            else -> "AUDIO"
        }
    }

    private fun resolveAudioTrackBitrateKbps(
        format: Format,
        episode: EpisodeItem,
        extMeta: ExternalAudioTrack?,
        embeddedAudioIndex: Int
    ): Int {
        val mime = format.sampleMimeType.orEmpty().lowercase()
        val ext = extMeta?.extension.orEmpty().lowercase()
        val isAac = mime.contains("mp4a") || mime.contains("aac") || ext == "aac" || ext == "m4a"
        val channels = format.channelCount.takeIf { it > 0 } ?: 2

        val directBps = when {
            format.bitrate > 0 -> format.bitrate
            format.averageBitrate > 0 -> format.averageBitrate
            format.peakBitrate > 0 -> format.peakBitrate
            else -> 0
        }
        val directKbps = if (directBps > 0) (directBps / 1000).coerceAtLeast(1) else 0

        // Android's MP4/MKV extractors frequently return 64000 (64 kbps) as a dummy core rate for stereo AAC
        val isDummyAacRate = isAac && directKbps in 1..64 && channels >= 2

        if (directKbps > 0 && !isDummyAacRate) {
            return directKbps
        }

        if (extMeta != null) {
            val cachedExt = probedExternalAudioBitratesKbps[extMeta.uri]
            if (cachedExt != null && cachedExt > 0 && !(isAac && cachedExt in 1..64 && channels >= 2)) {
                return cachedExt
            }
            val probedExt = probeExternalAudioBitrateKbps(extMeta.uri, episode)
            if (probedExt > 0 && !(isAac && probedExt in 1..64 && channels >= 2)) {
                probedExternalAudioBitratesKbps[extMeta.uri] = probedExt
                return probedExt
            }
        } else if (PlaybackResumeHelper.isLocalFileUri(episode.uri)) {
            val containerRates = probedContainerAudioBitratesKbps.getOrPut(episode.uri) {
                probeContainerAudioBitratesKbps(episode.uri)
            }
            val probed = containerRates.getOrNull(embeddedAudioIndex) ?: 0
            if (probed > 0 && !(isAac && probed in 1..64 && channels >= 2)) {
                return probed
            }
        }

        return estimateAudioBitrateFromFormatKbps(format, extMeta?.extension)
    }

    private fun probeExternalAudioBitrateKbps(extUriStr: String, episode: EpisodeItem): Int {
        val parsedUri = runCatching { Uri.parse(extUriStr) }.getOrNull() ?: return 0
        // 1. Try MediaExtractor KEY_BIT_RATE
        val extractorRate = probeContainerAudioBitratesKbps(extUriStr).firstOrNull { it > 0 } ?: 0
        if (extractorRate > 0) return extractorRate

        // 2. Compute from external audio file size and duration
        val durationMs = (_state.value.durationMs.takeIf { it > 0L } ?: episode.durationMs).coerceAtLeast(0L)
        if (durationMs >= 5_000L) {
            val sizeBytes = queryUriByteSize(parsedUri)
            if (sizeBytes > 16_384L) {
                val kbps = ((sizeBytes * 8L) / durationMs).toInt().coerceIn(24, 6000)
                return kbps
            }
        }
        return 0
    }

    private fun probeTrackBitrateFromSamples(extractor: android.media.MediaExtractor, trackIndex: Int): Int {
        return runCatching {
            extractor.selectTrack(trackIndex)
            extractor.seekTo(0L, android.media.MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            var totalBytes = 0L
            val firstPtsUs = extractor.sampleTime.takeIf { it >= 0L } ?: 0L
            var lastPtsUs = firstPtsUs
            var sampleCount = 0
            while (sampleCount < 40) {
                val size = extractor.sampleSize
                if (size <= 0) break
                totalBytes += size
                lastPtsUs = extractor.sampleTime
                sampleCount++
                if (!extractor.advance()) break
            }
            extractor.unselectTrack(trackIndex)
            val durationUs = lastPtsUs - firstPtsUs
            if (durationUs > 100_000L && totalBytes > 0L) {
                val bps = (totalBytes * 8_000_000L) / durationUs
                (bps / 1000).toInt().coerceIn(32, 4000)
            } else 0
        }.getOrDefault(0)
    }

    private fun probeContainerAudioBitratesKbps(uriStr: String): List<Int> {
        if (!PlaybackResumeHelper.isLocalFileUri(uriStr)) return emptyList()
        val extractor = android.media.MediaExtractor()
        return try {
            val uri = Uri.parse(uriStr)
            if (uri.scheme.equals("file", ignoreCase = true)) {
                val path = uri.path
                if (!path.isNullOrBlank()) {
                    extractor.setDataSource(path)
                } else {
                    extractor.setDataSource(appContext, uri, null)
                }
            } else {
                extractor.setDataSource(appContext, uri, null)
            }
            val result = mutableListOf<Int>()
            val count = extractor.trackCount
            for (i in 0 until count) {
                val mf = runCatching { extractor.getTrackFormat(i) }.getOrNull() ?: continue
                val mime = runCatching { mf.getString(android.media.MediaFormat.KEY_MIME) }.getOrNull().orEmpty()
                if (mime.startsWith("audio/", ignoreCase = true)) {
                    val bps = if (mf.containsKey(android.media.MediaFormat.KEY_BIT_RATE)) {
                        runCatching { mf.getInteger(android.media.MediaFormat.KEY_BIT_RATE) }.getOrDefault(0)
                    } else 0
                    var kbps = if (bps > 0) (bps / 1000).coerceAtLeast(1) else 0
                    val channels = if (mf.containsKey(android.media.MediaFormat.KEY_CHANNEL_COUNT)) {
                        runCatching { mf.getInteger(android.media.MediaFormat.KEY_CHANNEL_COUNT) }.getOrDefault(2)
                    } else 2
                    val sampleRate = if (mf.containsKey(android.media.MediaFormat.KEY_SAMPLE_RATE)) {
                        runCatching { mf.getInteger(android.media.MediaFormat.KEY_SAMPLE_RATE) }.getOrDefault(48000)
                    } else 48000
                    val isAac = mime.contains("mp4a", ignoreCase = true) || mime.contains("aac", ignoreCase = true)

                    if (isAac && (kbps <= 64 || kbps == 0) && channels >= 2) {
                        val sampled = probeTrackBitrateFromSamples(extractor, i)
                        kbps = if (sampled > 0) {
                            sampled
                        } else {
                            if (channels >= 6) 320 else if (sampleRate >= 44100) 192 else 128
                        }
                    }
                    result.add(kbps)
                }
            }
            result
        } catch (_: Throwable) {
            emptyList()
        } finally {
            runCatching { extractor.release() }
        }
    }

    private fun queryUriByteSize(uri: Uri): Long {
        if (uri.scheme.equals("file", ignoreCase = true)) {
            val path = uri.path ?: return 0L
            return runCatching { File(path).length() }.getOrDefault(0L)
        }
        return runCatching {
            appContext.contentResolver.openAssetFileDescriptor(uri, "r")?.use { afd ->
                afd.length.takeIf { it > 0L } ?: 0L
            } ?: 0L
        }.getOrDefault(0L)
    }

    private fun estimateAudioBitrateFromFormatKbps(format: Format, extHint: String?): Int {
        val mime = format.sampleMimeType.orEmpty().lowercase()
        val ext = extHint.orEmpty().lowercase()
        val channels = format.channelCount.takeIf { it > 0 } ?: 2
        val sampleRate = format.sampleRate.takeIf { it > 0 } ?: 48_000
        return when {
            mime.contains("flac") || ext == "flac" ->
                ((sampleRate * channels * 16) / 1000 * 58 / 100).coerceIn(320, 2400)
            mime.contains("truehd") ->
                if (channels >= 6) 2560 else 1400
            mime.contains("raw") || mime.contains("pcm") || ext == "wav" ->
                ((sampleRate * channels * 16) / 1000).coerceIn(256, 6144)
            mime.contains("dts") ->
                if (channels >= 6) 1509 else 768
            mime.contains("eac3") || ext == "eac3" ->
                if (channels >= 6) 640 else 384
            mime.contains("ac3") || ext == "ac3" ->
                if (channels >= 6) 448 else 192
            mime.contains("opus") || ext == "opus" ->
                when {
                    channels >= 6 -> 320
                    channels == 1 -> 96
                    else -> 160
                }
            mime.contains("vorbis") || ext == "ogg" ->
                when {
                    channels >= 6 -> 320
                    channels == 1 -> 96
                    else -> 160
                }
            mime.contains("mpeg") || mime.contains("mp3") || ext == "mp3" ->
                if (channels == 1) 128 else 192
            mime.contains("mp4a") || mime.contains("aac") || ext == "m4a" || ext == "aac" ->
                when {
                    channels >= 8 -> 448
                    channels >= 6 -> 320
                    channels == 1 -> 96
                    sampleRate >= 44_100 -> 192
                    else -> 128
                }
            else ->
                when {
                    channels >= 8 -> 512
                    channels >= 6 -> 384
                    channels == 1 -> 96
                    sampleRate >= 48_000 -> 192
                    else -> 160
                }
        }
    }

    private fun updateDetectedAudioFormat(format: Format, resolvedBitrateKbps: Int = 0) {
        val rawBitrate = format.bitrate
        val mime = format.sampleMimeType.orEmpty().lowercase()
        val isAac = mime.contains("mp4a") || mime.contains("aac")
        val channels = format.channelCount.takeIf { it > 0 } ?: 2
        val directKbps = if (rawBitrate > 0) (rawBitrate / 1000).coerceAtLeast(1) else 0
        val isDummyAac = isAac && directKbps in 1..64 && channels >= 2

        val kbps = when {
            resolvedBitrateKbps > 0 && !(isAac && resolvedBitrateKbps in 1..64 && channels >= 2) -> resolvedBitrateKbps
            directKbps > 0 && !isDummyAac -> directKbps
            else -> estimateAudioBitrateFromFormatKbps(format, null)
        }
        val isLosslessCodec = mime.contains("flac") || mime.contains("raw") || mime.contains("pcm") ||
            mime.contains("truehd") || mime.contains("alac")
        // Consider low-bitrate when reported bitrate <= 132 kbps, or when a lossy stereo/mono stream
        // (AAC/MP3/Opus/Vorbis in MKV/MKA) omits container bitrate metadata.
        val isLowBitrate = when {
            isLosslessCodec -> false
            rawBitrate > 0 -> kbps <= AudioDelayProcessor.LOW_BITRATE_THRESHOLD_KBPS
            else -> format.channelCount in 1..2
        }
        audioDelayProcessor.isLowBitrateStream = isLowBitrate
        _state.update {
            it.copy(
                activeAudioBitrateKbps = kbps,
                audioMimeType = format.sampleMimeType.orEmpty(),
                audioSampleRateHz = format.sampleRate.coerceAtLeast(0),
                audioChannelCount = format.channelCount.coerceAtLeast(0),
                isAudioDeArtifactActive = audioDelayProcessor.isDeArtifactCurrentlyActive
            )
        }
    }

    fun adjustAudioDelayMs(deltaMs: Long) {
        setAudioDelayMs(manualAudioDelayMs + deltaMs)
    }

    fun setAudioDelayMs(delayMs: Long) {
        val clamped = AudioDelayProcessor.clampDelayMs(delayMs)
        val route = bluetoothSyncController.currentRouteState
        if (route.isBluetoothActive && bluetoothAutoSyncEnabled && clamped in listOf(-250L, -200L, -150L, -100L, -60L, -45L, 0L)) {
            manualAudioDelayMs = 0L
            bluetoothConfiguredOffsetMs = clamped
            engineScope.launch {
                repository.preferencesRepository.updateBluetoothAudioOffsetMs(clamped)
            }
        } else {
            manualAudioDelayMs = clamped
        }
        recalculateEffectiveAudioDelay(resyncIfPlaying = false)
    }

    fun refreshBluetoothAudioRoute() {
        bluetoothSyncController.refreshRouteState()
        recalculateEffectiveAudioDelay(resyncIfPlaying = false)
    }

    private fun recalculateEffectiveAudioDelay(resyncIfPlaying: Boolean) {
        val route = bluetoothSyncController.currentRouteState
        val btApplied = BluetoothAudioSyncController.computeAutoBluetoothOffsetMs(
            route = route,
            autoSyncEnabled = bluetoothAutoSyncEnabled,
            userConfiguredA2dpOffsetMs = bluetoothConfiguredOffsetMs
        )
        val totalEffective = BluetoothAudioSyncController.computeTotalEffectiveDelayMs(
            manualDelayMs = manualAudioDelayMs,
            route = route,
            autoSyncEnabled = bluetoothAutoSyncEnabled,
            userConfiguredBtOffsetMs = bluetoothConfiguredOffsetMs
        )
        audioDelayProcessor.setDelayMs(totalEffective)
        _state.update {
            it.copy(
                audioDelayMs = manualAudioDelayMs,
                bluetoothAudioRoute = route,
                bluetoothAutoSyncEnabled = bluetoothAutoSyncEnabled,
                bluetoothConfiguredOffsetMs = bluetoothConfiguredOffsetMs,
                bluetoothAppliedOffsetMs = btApplied,
                effectiveAudioDelayMs = totalEffective
            )
        }
        if (resyncIfPlaying && _exoPlayer?.isPlaying == true) {
            engineScope.launch {
                delay(220L)
                if (_exoPlayer?.isPlaying == true) {
                    resyncAudioStream()
                }
            }
        }
    }

    fun resyncAudioStream() {
        val player = _exoPlayer ?: return
        bluetoothSyncController.refreshRouteState()
        recalculateEffectiveAudioDelay(resyncIfPlaying = false)
        audioDelayProcessor.forceResyncFlush()
        val wasPlaying = player.playWhenReady
        val pos = player.currentPosition.coerceAtLeast(0L)
        val idx = player.currentMediaItemIndex.coerceAtLeast(0)
        val savedParams = player.trackSelectionParameters

        // Force MediaCodecAudioRenderer & DefaultAudioSink to release their drifted hardware
        // AudioTimestamp anchor and re-anchor strictly to the video frame PTS
        player.playWhenReady = false
        player.trackSelectionParameters = savedParams
            .buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, true)
            .build()
        player.trackSelectionParameters = savedParams
            .buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, false)
            .build()
        player.setSeekParameters(SeekParameters.EXACT)
        player.seekTo(idx, pos)
        player.setSeekParameters(SeekParameters.DEFAULT)
        player.playWhenReady = wasPlaying

        _positionMs.value = pos
        _state.update {
            it.copy(
                currentPositionMs = pos,
                lastAudioResyncTimestampMs = System.currentTimeMillis()
            )
        }
        updatePositionAndSubtitlesNow(forceSyncRuntimePosition = true)
    }

    fun setPlaybackEndMode(mode: PlaybackEndMode) {
        _state.update { it.copy(playbackEndMode = mode) }
        applyPlaybackEndModeToExo(mode)
        engineScope.launch {
            repository.preferencesRepository.updatePlaybackEndMode(mode)
        }
    }

    private fun applyPlaybackEndModeToExo(mode: PlaybackEndMode) {
        val player = _exoPlayer ?: return
        val sleepEndEpisode = _state.value.sleepTimerPreset == SleepTimerPreset.END_OF_EPISODE
        when (mode) {
            PlaybackEndMode.AUTO_NEXT -> {
                player.repeatMode = Player.REPEAT_MODE_OFF
                player.pauseAtEndOfMediaItems = sleepEndEpisode
            }
            PlaybackEndMode.PAUSE_AT_END -> {
                player.repeatMode = Player.REPEAT_MODE_OFF
                player.pauseAtEndOfMediaItems = true
            }
            PlaybackEndMode.REPEAT_ONE -> {
                player.repeatMode = if (sleepEndEpisode) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ONE
                player.pauseAtEndOfMediaItems = sleepEndEpisode
            }
        }
    }

    fun setSleepTimer(preset: SleepTimerPreset) {
        sleepTimerJob?.cancel()
        sleepTimerJob = null

        when {
            preset == SleepTimerPreset.OFF -> {
                clearSleepTimerInternal()
            }

            preset.stopAtEndOfEpisode -> {
                _state.update {
                    it.copy(
                        sleepTimerPreset = preset,
                        sleepTimerRemainingMs = 0L
                    )
                }
                applyPlaybackEndModeToExo(_state.value.playbackEndMode)
            }

            preset.durationMinutes > 0 -> {
                val durationMs = preset.durationMinutes * 60_000L
                sleepDeadlineElapsedMs = System.currentTimeMillis() + durationMs
                _state.update {
                    it.copy(
                        sleepTimerPreset = preset,
                        sleepTimerRemainingMs = durationMs
                    )
                }
                applyPlaybackEndModeToExo(_state.value.playbackEndMode)
                sleepTimerJob = engineScope.launch {
                    while (isActive) {
                        val rem = (sleepDeadlineElapsedMs - System.currentTimeMillis()).coerceAtLeast(0L)
                        _state.update { it.copy(sleepTimerRemainingMs = rem) }
                        if (rem <= 0L) {
                            _exoPlayer?.pause()
                            persistCurrentProgress()
                            clearSleepTimerInternal()
                            break
                        }
                        delay(1_000L)
                    }
                }
            }
        }
    }

    private fun clearSleepTimerInternal() {
        sleepTimerJob?.cancel()
        sleepTimerJob = null
        _state.update {
            it.copy(
                sleepTimerPreset = SleepTimerPreset.OFF,
                sleepTimerRemainingMs = 0L
            )
        }
        applyPlaybackEndModeToExo(_state.value.playbackEndMode)
    }

    fun markAbLoopPointA() {
        val pos = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L)
        val currentEnd = _state.value.abLoopEndMs
        val validEnd = currentEnd?.takeIf { it > pos + 500L }
        _state.update {
            it.copy(
                abLoopStartMs = pos,
                abLoopEndMs = validEnd
            )
        }
    }

    fun markAbLoopPointB() {
        val pos = (_exoPlayer?.currentPosition ?: _positionMs.value).coerceAtLeast(0L)
        val start = _state.value.abLoopStartMs ?: 0L
        if (pos > start + 500L) {
            _state.update {
                it.copy(
                    abLoopStartMs = start,
                    abLoopEndMs = pos
                )
            }
        }
    }

    fun clearAbLoop() {
        _state.update {
            it.copy(
                abLoopStartMs = null,
                abLoopEndMs = null
            )
        }
    }

    fun pauseAndPersist() {
        val player = _exoPlayer ?: return
        setTemporarySpeedBoost(false)
        stopPositionTicker()
        player.pause()
        persistCurrentProgress()
    }

    private fun startPositionTicker() {
        if (positionTickerJob?.isActive == true) return
        positionTickerJob = engineScope.launch {
            while (isActive) {
                val player = _exoPlayer
                if (player == null || !player.isPlaying) {
                    break
                }
                updatePositionAndSubtitlesNow(forceSyncRuntimePosition = false)
                lastSavedTickCount++
                if (lastSavedTickCount >= 12) {
                    lastSavedTickCount = 0
                    persistCurrentProgress()
                }
                delay(80L)
            }
        }
    }

    private fun stopPositionTicker() {
        positionTickerJob?.cancel()
        positionTickerJob = null
    }

    private fun updatePositionAndSubtitlesNow(forceSyncRuntimePosition: Boolean = false) {
        val player = _exoPlayer ?: return
        var pos = player.currentPosition.coerceAtLeast(0L)
        val currentState = _state.value
        val liveDur = player.duration.takeIf { it != C.TIME_UNSET && it > 0L }
        if (liveDur != null) {
            lastKnownDurationForTrackedEpisodeMs = liveDur
            verifyAndResetIfResumedAtEnd(player, liveDur)
            pos = player.currentPosition.coerceAtLeast(0L)
        }
        val dur = liveDur ?: currentState.durationMs

        // Enforce A-B segment loop when both A and B markers are set
        val loopA = currentState.abLoopStartMs
        val loopB = currentState.abLoopEndMs
        if (loopA != null && loopB != null && loopB > loopA && pos >= loopB) {
            player.seekTo(loopA)
            pos = loopA
        }

        _positionMs.value = pos

        val extDoc = activeExternalSubtitleDoc
        val cues = if (extDoc != null) {
            extDoc.activeCuesAt(pos, currentState.subtitleDelayMs)
        } else if (currentState.selectedSubtitleId.startsWith("emb:")) {
            latestEmbeddedCues
        } else {
            emptyList()
        }

        val buffered = player.bufferedPosition.coerceAtLeast(pos)
        if (forceSyncRuntimePosition ||
            dur != currentState.durationMs ||
            buffered != currentState.bufferedPositionMs ||
            cues != currentState.activeSubtitleCues
        ) {
            _state.update {
                it.copy(
                    currentPositionMs = pos,
                    durationMs = dur,
                    bufferedPositionMs = buffered,
                    activeSubtitleCues = cues
                )
            }
        }
    }

    fun persistCurrentProgress() {
        val player = _exoPlayer ?: return
        val currentState = _state.value
        if (currentState.errorMessage != null) return
        val ep = currentState.currentEpisode ?: return
        val rawPos = player.currentPosition.coerceAtLeast(0L)
        val lastKnownPos = _positionMs.value.coerceAtLeast(currentState.currentPositionMs)
        val validPos = if (rawPos == 0L && (player.playbackState == Player.STATE_IDLE || player.playbackState == Player.STATE_ENDED) && lastKnownPos > 0L) {
            lastKnownPos
        } else {
            rawPos
        }
        val dur = (player.duration.takeIf { it != C.TIME_UNSET && it > 0L } ?: currentState.durationMs)
            .coerceAtLeast(lastKnownDurationForTrackedEpisodeMs)
        val isEnded = player.playbackState == Player.STATE_ENDED ||
            (dur >= 10_000L && (validPos >= (dur * 95L) / 100L || (dur - validPos) <= 15_000L))
        val effectiveDur = if (isEnded) dur.coerceAtLeast(validPos).coerceAtLeast(10_000L) else dur
        val posToSave = if (isEnded) effectiveDur else validPos
        _positionMs.value = if (player.playbackState == Player.STATE_ENDED) effectiveDur else validPos
        if (effectiveDur <= 0L && posToSave <= 0L) return
        engineScope.launch {
            if (PlaybackResumeHelper.isLocalFileUri(ep.uri) &&
                !PlaybackResumeHelper.isMediaFileExists(appContext, ep.uri)
            ) {
                repository.refreshMediaAvailabilityAsync()
                return@launch
            }
            repository.savePlaybackProgress(
                episode = ep,
                positionMs = posToSave,
                durationMs = effectiveDur,
                videoWidth = detectedVideoWidth,
                videoHeight = detectedVideoHeight
            )
        }
    }

    companion object {
        @Volatile
        private var instance: PlayerEngine? = null

        fun getInstance(context: Context): PlayerEngine {
            return instance ?: synchronized(this) {
                instance ?: run {
                    val app = context.applicationContext
                    PlayerEngine(app, LibraryRepository.getInstance(app)).also { instance = it }
                }
            }
        }
    }
}
