package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import kotlin.math.roundToInt
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.PlayerPreferencesRepository
import com.example.data.StorageAccessHelper
import com.example.player.DisplayRefreshRateController
import com.example.player.PipController
import com.example.player.RefreshRatePlan
import com.example.torrent.ExternalOpenOutcome
import com.example.torrent.TorrentIntentHelper
import com.example.ui.NegaiViewModel
import com.example.ui.i18n.ProvideAppLocalization
import com.example.ui.screens.FolderDetailScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.SettingsActions
import com.example.ui.screens.TorrentsActions
import com.example.ui.theme.NegaiTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: NegaiViewModel by viewModels()
    private var pipReceiver: BroadcastReceiver? = null
    private var noisyReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val bootSettings = PlayerPreferencesRepository.readBootSettingsSnapshot(this)
        window.setBackgroundDrawable(ColorDrawable(bootSettings.themeMode.previewBgArgb.toInt()))
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        enableEdgeToEdge()
        window.setBackgroundDrawable(ColorDrawable(bootSettings.themeMode.previewBgArgb.toInt()))
        DisplayRefreshRateController.applyWindowRefreshRate(this, bootSettings.highRefreshRateEnabled)


        pipReceiver = PipController.registerReceiver(
            context = this,
            onRewind10 = {
                val stepMs = viewModel.playerSettings.value.seekStepSeconds.coerceAtLeast(3) * 1000L
                viewModel.playerEngine.seekBy(-stepMs)
            },
            onPlayPause = { viewModel.playerEngine.togglePlayPause() },
            onForward10 = {
                val stepMs = viewModel.playerSettings.value.seekStepSeconds.coerceAtLeast(3) * 1000L
                viewModel.playerEngine.seekBy(stepMs)
            }
        )

        val becomingNoisyReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                    if (viewModel.isPlayerOpen.value && viewModel.playbackState.value.isPlaying) {
                        viewModel.playerEngine.pauseAndPersist()
                    }
                }
            }
        }
        runCatching {
            registerReceiver(
                becomingNoisyReceiver,
                IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
            )
            noisyReceiver = becomingNoisyReceiver
        }

        handleIncomingVideoIntent(intent)

        setContent {
            val settings by viewModel.playerSettings.collectAsStateWithLifecycle()
            val isPlayerOpen by viewModel.isPlayerOpen.collectAsStateWithLifecycle()
            val isPlayerUiActive by viewModel.isPlayerUiActive.collectAsStateWithLifecycle()
            val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

            val displayRefreshInfo = remember {
                DisplayRefreshRateController.queryDisplayRefreshRates(this@MainActivity)
            }
            val refreshPlan = remember(
                displayRefreshInfo,
                isPlayerOpen,
                playbackState.isPlaying,
                playbackState.sourceFrameRate,
                playbackState.playbackSpeed,
                settings.highRefreshRateEnabled,
                settings.ltpoAdaptiveRefreshEnabled,
                settings.effectiveFrameInterpolationMode
            ) {
                DisplayRefreshRateController.computeOptimalRefreshPlan(
                    displayInfo = displayRefreshInfo,
                    isPlayerActive = isPlayerOpen,
                    isPlaying = playbackState.isPlaying,
                    sourceVideoFps = playbackState.sourceFrameRate,
                    playbackSpeed = playbackState.playbackSpeed,
                    highRefreshRateEnabled = settings.highRefreshRateEnabled,
                    ltpoAdaptiveEnabled = settings.ltpoAdaptiveRefreshEnabled,
                    interpolationMode = settings.effectiveFrameInterpolationMode,
                    // Never toggle display refresh mode or surface frame rate on simple HUD taps
                    isControlsOrUiActive = false
                )
            }

            var targetDisplayHz by remember {
                mutableFloatStateOf(refreshPlan.targetDisplayHz)
            }
            val systemInDark = androidx.compose.foundation.isSystemInDarkTheme()
            LaunchedEffect(settings.themeMode, systemInDark) {
                val resolved = settings.themeMode.resolveModeFor(systemInDark)
                window.setBackgroundDrawable(ColorDrawable(resolved.previewBgArgb.toInt()))
            }
            LaunchedEffect(refreshPlan, isPlayerOpen) {
                targetDisplayHz = DisplayRefreshRateController.applyWindowRefreshPlan(
                    this@MainActivity,
                    refreshPlan
                )
            }
            val baseDensity = androidx.compose.ui.platform.LocalDensity.current
            val appUiScale = (settings.appUiScalePercent / 100f).coerceIn(0.75f, 1.4f)
            val scaledAppDensity = remember(baseDensity, appUiScale) {
                androidx.compose.ui.unit.Density(
                    density = baseDensity.density * appUiScale,
                    fontScale = baseDensity.fontScale * appUiScale
                )
            }
            NegaiTheme(
                themeMode = settings.themeMode,
                accentColor = settings.accentColor
            ) {
                ProvideAppLocalization(appLanguage = settings.appLanguage) {
                    androidx.compose.runtime.CompositionLocalProvider(
                        androidx.compose.ui.platform.LocalDensity provides scaledAppDensity
                    ) {
                        NegaiAppRoot(
                            viewModel = viewModel,
                            targetDisplayHz = targetDisplayHz,
                            refreshPlan = refreshPlan,
                            onUpdatePipParams = { isPlayerActive, isPlaying, width, height, autoEnter, stepSec ->
                                PipController.updatePipParams(
                                    activity = this,
                                    isPlayerActive = isPlayerActive,
                                    isPlaying = isPlaying,
                                    videoWidth = width,
                                    videoHeight = height,
                                    autoEnterEnabled = autoEnter,
                                    seekStepSeconds = stepSec
                                )
                            },
                            onRequestEnterPip = { isPlaying, width, height, autoEnter, stepSec ->
                                PipController.enterPipNow(
                                    activity = this,
                                    isPlaying = isPlaying,
                                    videoWidth = width,
                                    videoHeight = height,
                                    autoEnterEnabled = autoEnter,
                                    seekStepSeconds = stepSec
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingVideoIntent(intent)
    }

    private fun handleIncomingVideoIntent(intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_VIEW,
            Intent.ACTION_SEND,
            Intent.ACTION_SEND_MULTIPLE -> {
                viewModel.handleIncomingIntent(intent)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.O until Build.VERSION_CODES.S) {
            val isPlayerOpen = viewModel.isPlayerOpen.value
            val pb = viewModel.playbackState.value
            val settings = viewModel.playerSettings.value
            if (isPlayerOpen && pb.isPlaying && settings.autoEnterPip) {
                PipController.enterPipNow(
                    activity = this,
                    isPlaying = true,
                    videoWidth = pb.videoWidth,
                    videoHeight = pb.videoHeight,
                    autoEnterEnabled = settings.autoEnterPip,
                    seekStepSeconds = settings.seekStepSeconds
                )
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.playerEngine.persistCurrentProgress()
        viewModel.onPipModeChanged(isInPictureInPictureMode)
        if (!isInPictureInPictureMode &&
            !lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.STARTED)
        ) {
            if (!viewModel.playerSettings.value.backgroundAudioPlay) {
                viewModel.playerEngine.pauseAndPersist()
            } else {
                viewModel.playerEngine.persistCurrentProgress()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAppResumed()
        if (!viewModel.isPlayerOpen.value) {
            val s = viewModel.playerSettings.value
            DisplayRefreshRateController.applyWindowRefreshRate(this, s.highRefreshRateEnabled)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.playerEngine.persistCurrentProgress()
    }

    override fun onStop() {
        super.onStop()
        viewModel.playerEngine.persistCurrentProgress()
        if (!isInPictureInPictureMode &&
            !isChangingConfigurations &&
            viewModel.isPlayerOpen.value &&
            !viewModel.playerSettings.value.backgroundAudioPlay
        ) {
            viewModel.playerEngine.pauseAndPersist()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (viewModel.isPlayerOpen.value) {
            when (keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    val audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                    if (audioManager != null) {
                        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
                        val curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).coerceIn(0, maxVol)
                        val stepPercent = (100f / maxVol.toFloat()).roundToInt().coerceIn(5, 15)
                        val maxBoostPct = viewModel.playerSettings.value.maxAudioBoostPercent.coerceIn(100, 300)
                        val curBoost = viewModel.playbackState.value.audioBoostPercent.coerceIn(100, maxBoostPct)
                        if (curVol < maxVol) {
                            val nextVol = (curVol + 1).coerceAtMost(maxVol)
                            runCatching {
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, nextVol, 0)
                            }
                            if (curBoost > 100) {
                                viewModel.playerEngine.setAudioBoostPercent(100)
                            }
                            val frac = (nextVol.toFloat() / maxVol.toFloat()).coerceIn(0f, 1f)
                            val pct = (frac * 100f).roundToInt().coerceIn(0, 100)
                            viewModel.emitHardwareVolumeHud(frac, pct, maxBoostPct)
                        } else {
                            if (maxBoostPct > 100) {
                                val nextBoost = (curBoost + stepPercent).coerceIn(100, maxBoostPct)
                                viewModel.playerEngine.setAudioBoostPercent(nextBoost)
                                viewModel.emitHardwareVolumeHud(1.0f, nextBoost, maxBoostPct)
                            } else {
                                viewModel.playerEngine.setAudioBoostPercent(100)
                                viewModel.emitHardwareVolumeHud(1.0f, 100, 100)
                            }
                        }
                        return true
                    }
                }

                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    val audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                    if (audioManager != null) {
                        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
                        val curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).coerceIn(0, maxVol)
                        val stepPercent = (100f / maxVol.toFloat()).roundToInt().coerceIn(5, 15)
                        val maxBoostPct = viewModel.playerSettings.value.maxAudioBoostPercent.coerceIn(100, 300)
                        val curBoost = viewModel.playbackState.value.audioBoostPercent.coerceIn(100, maxBoostPct)
                        // First step down software audio boost before lowering system volume
                        if (curBoost > 100) {
                            val nextBoost = (curBoost - stepPercent).coerceAtLeast(100)
                            viewModel.playerEngine.setAudioBoostPercent(nextBoost)
                            viewModel.emitHardwareVolumeHud(1.0f, nextBoost, maxBoostPct)
                        } else {
                            val nextVol = (curVol - 1).coerceAtLeast(0)
                            runCatching {
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, nextVol, 0)
                            }
                            val frac = (nextVol.toFloat() / maxVol.toFloat()).coerceIn(0f, 1f)
                            val pct = (frac * 100f).roundToInt().coerceIn(0, 100)
                            viewModel.emitHardwareVolumeHud(frac, pct, maxBoostPct)
                        }
                        return true
                    }
                }

                KeyEvent.KEYCODE_MEDIA_PLAY -> {
                    if (!viewModel.playbackState.value.isPlaying) {
                        viewModel.playerEngine.togglePlayPause()
                    }
                    return true
                }

                KeyEvent.KEYCODE_MEDIA_PAUSE,
                KeyEvent.KEYCODE_MEDIA_STOP -> {
                    if (viewModel.playbackState.value.isPlaying) {
                        viewModel.playerEngine.pauseAndPersist()
                    }
                    return true
                }

                KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
                KeyEvent.KEYCODE_HEADSETHOOK -> {
                    viewModel.playerEngine.togglePlayPause()
                    return true
                }

                KeyEvent.KEYCODE_MEDIA_NEXT -> {
                    viewModel.playerEngine.playNextEpisode()
                    return true
                }

                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                    viewModel.playerEngine.playPreviousEpisode()
                    return true
                }

                KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
                    val stepMs = viewModel.playerSettings.value.seekStepSeconds.coerceAtLeast(3) * 1000L
                    viewModel.playerEngine.seekBy(stepMs)
                    return true
                }

                KeyEvent.KEYCODE_MEDIA_REWIND -> {
                    val stepMs = viewModel.playerSettings.value.seekStepSeconds.coerceAtLeast(3) * 1000L
                    viewModel.playerEngine.seekBy(-stepMs)
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        pipReceiver?.let {
            runCatching { unregisterReceiver(it) }
            pipReceiver = null
        }
        noisyReceiver?.let {
            runCatching { unregisterReceiver(it) }
            noisyReceiver = null
        }
        super.onDestroy()
    }
}

@Composable
fun NegaiAppRoot(
    viewModel: NegaiViewModel,
    targetDisplayHz: Float,
    refreshPlan: RefreshRatePlan = RefreshRatePlan(targetDisplayHz = targetDisplayHz, targetRenderFps = targetDisplayHz),
    onUpdatePipParams: (Boolean, Boolean, Int, Int, Boolean, Int) -> Unit,
    onRequestEnterPip: (Boolean, Int, Int, Boolean, Int) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val visibleFolders by viewModel.filteredVisibleFolders.collectAsStateWithLifecycle()
    val hiddenFolders by viewModel.hiddenFolders.collectAsStateWithLifecycle()
    val recentProgress by viewModel.recentProgress.collectAsStateWithLifecycle()
    val lastPlayedFile by viewModel.lastPlayedFile.collectAsStateWithLifecycle()
    val libraryRoots by viewModel.libraryRoots.collectAsStateWithLifecycle()
    val hasStorageAccess by viewModel.hasStorageAccess.collectAsStateWithLifecycle()
    val hasAllFilesAccess by viewModel.hasAllFilesAccess.collectAsStateWithLifecycle()
    val selectedFolder by viewModel.selectedFolder.collectAsStateWithLifecycle()
    val isPlayerOpen by viewModel.isPlayerOpen.collectAsStateWithLifecycle()
    val isInPipMode by viewModel.isInPipMode.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val settings by viewModel.playerSettings.collectAsStateWithLifecycle()
    val torrentsState by viewModel.torrentsState.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val scaleMode by viewModel.videoScaleMode.collectAsStateWithLifecycle()
    val splitCompare by viewModel.anime4kSplitCompare.collectAsStateWithLifecycle()
    val splitPos by viewModel.anime4kSplitPos.collectAsStateWithLifecycle()
    val hardwareVolumeHudEvent by viewModel.hardwareVolumeHudEvent.collectAsStateWithLifecycle()

    LaunchedEffect(
        isPlayerOpen,
        playbackState.isPlaying,
        playbackState.videoWidth,
        playbackState.videoHeight,
        settings.autoEnterPip,
        settings.seekStepSeconds
    ) {
        onUpdatePipParams(
            isPlayerOpen,
            playbackState.isPlaying,
            playbackState.videoWidth,
            playbackState.videoHeight,
            settings.autoEnterPip,
            settings.seekStepSeconds
        )
    }

    val manageStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.onStoragePermissionResult()
    }

    val runtimePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.onStoragePermissionResult()
    }

    val requestStorageAccess: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val primaryIntent = StorageAccessHelper.createManageAllFilesIntent(context)
            val launchedPrimary = primaryIntent != null && runCatching {
                manageStorageLauncher.launch(primaryIntent)
                true
            }.getOrDefault(false)

            if (!launchedPrimary) {
                val fallbackIntent = StorageAccessHelper.createManageAllFilesFallbackIntent()
                val launchedFallback = fallbackIntent != null && runCatching {
                    manageStorageLauncher.launch(fallbackIntent)
                    true
                }.getOrDefault(false)

                if (!launchedFallback) {
                    runtimePermissionsLauncher.launch(StorageAccessHelper.runtimeMediaPermissions())
                }
            }
        } else {
            runtimePermissionsLauncher.launch(StorageAccessHelper.runtimeMediaPermissions())
        }
    }

    val folderTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { treeUri ->
        if (treeUri != null) {
            viewModel.addFolderTree(treeUri)
        }
    }

    val excludeFolderTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { treeUri ->
        if (treeUri != null) {
            viewModel.excludeFolderByUri(treeUri)
        }
    }

    val openVideoFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { videoUri ->
        if (videoUri != null) {
            viewModel.playStandaloneUri(videoUri)
        }
    }

    val openExternalAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { audioUri ->
        if (audioUri != null) {
            viewModel.attachExternalAudioFile(audioUri)
        }
    }

    val openExternalSubtitleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { subtitleUri ->
        if (subtitleUri != null) {
            viewModel.attachExternalSubtitleFile(subtitleUri)
        }
    }

    var pickTorrentForExternal by remember { mutableStateOf(false) }
    val openTorrentDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { torrentUri ->
        if (torrentUri != null) {
            if (pickTorrentForExternal) {
                scope.launch {
                    val (extractedMagnet, extractedTitle) =
                        viewModel.torrentsController.extractMagnetFromLocalTorrent(torrentUri)
                    val outcome = TorrentIntentHelper.openTorrentInExternalApp(
                        context = context,
                        magnetUri = extractedMagnet,
                        localTorrentUri = torrentUri,
                        title = extractedTitle
                    )
                    if (outcome == ExternalOpenOutcome.COPIED_MAGNET_NO_EXTERNAL_APP) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_magnet_copied_no_app),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                viewModel.torrentsController.openLocalTorrentUriForPlayback(torrentUri)
            }
        }
    }

    when {
        isPlayerOpen -> {
            PlayerScreen(
                playbackState = playbackState,
                positionMsFlow = viewModel.playerPositionMs,
                settings = settings,
                targetDisplayHz = targetDisplayHz,
                refreshPlan = refreshPlan,
                isInPipMode = isInPipMode,
                scaleMode = scaleMode,
                splitCompareEnabled = splitCompare,
                splitPosition = splitPos,
                hardwareVolumeHudEvent = hardwareVolumeHudEvent,
                onSurfaceReady = { surface -> viewModel.playerEngine.attachVideoSurface(surface) },
                onSurfaceDestroyed = { surface -> viewModel.playerEngine.detachVideoSurface(surface) },
                onBack = { viewModel.exitPlayer() },
                onEnterPip = {
                    onRequestEnterPip(
                        playbackState.isPlaying,
                        playbackState.videoWidth,
                        playbackState.videoHeight,
                        settings.autoEnterPip,
                        settings.seekStepSeconds
                    )
                },
                onTogglePlayPause = { viewModel.playerEngine.togglePlayPause() },
                onSeekByMs = { delta -> viewModel.playerEngine.seekBy(delta) },
                onSeekToMs = { pos -> viewModel.playerEngine.seekTo(pos) },
                onPreviousEpisode = { viewModel.playerEngine.playPreviousEpisode() },
                onNextEpisode = { viewModel.playerEngine.playNextEpisode() },
                onSelectEpisodeIndex = { idx -> viewModel.playerEngine.playEpisodeAtIndex(idx) },
                onSelectChapter = { ch -> viewModel.seekToChapter(ch) },
                onPreviousChapter = { viewModel.seekToPreviousChapter() },
                onNextChapter = { viewModel.seekToNextChapter() },
                onSelectAudio = { opt -> viewModel.selectAudioTrack(opt) },
                onPickExternalAudio = {
                    openExternalAudioLauncher.launch(
                        arrayOf("audio/*", "video/x-matroska", "application/octet-stream", "*/*")
                    )
                },
                onSetAudioBoostPercent = { pct -> viewModel.playerEngine.setAudioBoostPercent(pct) },
                onSelectAudioDeArtifactMode = { mode -> viewModel.setAudioDeArtifactMode(mode) },
                onToggleAutoDeArtifactLowBitrate = { enabled -> viewModel.setAutoDeArtifactLowBitrate(enabled) },
                onAdjustAudioDelay = { delta -> viewModel.adjustAudioDelay(delta) },
                onSetAudioDelay = { ms -> viewModel.setAudioDelay(ms) },
                onResyncAudio = { viewModel.resyncAudioStream() },
                onToggleBluetoothAutoSync = { enabled -> viewModel.setBluetoothAudioAutoSync(enabled) },
                onSelectSubtitle = { opt -> viewModel.selectSubtitleTrack(opt) },
                onPickExternalSubtitle = {
                    openExternalSubtitleLauncher.launch(
                        arrayOf("application/x-subrip", "text/*", "application/octet-stream", "*/*")
                    )
                },
                onAdjustSubtitleDelay = { delta -> viewModel.adjustSubtitleDelay(delta) },
                onSetSubtitleDelay = { ms -> viewModel.setSubtitleDelay(ms) },
                onSetSubtitleFontSize = { sp -> viewModel.setSubtitleFontSize(sp) },
                onSetSubtitleColor = { argb -> viewModel.setSubtitleColor(argb) },
                onSetSubtitleBg = { bg -> viewModel.setSubtitleBackground(bg) },
                onToggleHighRefreshRate = { enabled -> viewModel.setHighRefreshRateEnabled(enabled) },
                onToggleLtpoAdaptive = { enabled -> viewModel.setLtpoAdaptiveRefreshEnabled(enabled) },
                onToggleFrameInterpolation = { enabled -> viewModel.setFrameInterpolationEnabled(enabled) },
                onSelectFrameInterpolationMode = { mode -> viewModel.setFrameInterpolationMode(mode) },
                onToggleAnime4KEnabled = { enabled -> viewModel.setAnime4KEnabled(enabled) },
                onSelectAnime4KPreset = { preset -> viewModel.setAnime4KPreset(preset) },
                onChangeAnime4KStrength = { str -> viewModel.setAnime4KStrength(str) },
                onToggleSplitCompare = { enabled -> viewModel.setAnime4KSplitCompare(enabled) },
                onChangeSplitPos = { pos -> viewModel.setAnime4KSplitPos(pos) },
                onSelectDecoderMode = { mode -> viewModel.setDecoderMode(mode) },
                onSelectSpeed = { spd -> viewModel.playerEngine.setPlaybackSpeed(spd) },
                onSetTempSpeedBoost = { active ->
                    viewModel.playerEngine.setTemporarySpeedBoost(active, settings.longPressBoostSpeed)
                },
                onSelectScaleMode = { mode -> viewModel.setVideoScaleMode(mode) },
                onSetSleepTimer = { preset -> viewModel.playerEngine.setSleepTimer(preset) },
                onMarkAbPointA = { viewModel.playerEngine.markAbLoopPointA() },
                onMarkAbPointB = { viewModel.playerEngine.markAbLoopPointB() },
                onClearAbLoop = { viewModel.playerEngine.clearAbLoop() },
                onSelectPlaybackEndMode = { mode -> viewModel.setPlaybackEndMode(mode) },
                onSelectControlsTheme = { theme -> viewModel.setPlayerControlsTheme(theme) },
                onSelectSurfaceStyle = { style -> viewModel.setPlayerSurfaceStyle(style) },
                onUpdateQuickActions = { actions -> viewModel.setQuickActions(actions) },
                onUpdateSkipIntroSeconds = { sec -> viewModel.setSkipIntroSeconds(sec) },
                onUpdatePlayerUiScalePercent = { pct -> viewModel.setPlayerUiScalePercent(pct) },
                onUpdatePlayerAutoDpiScale = { enabled -> viewModel.setPlayerAutoDpiScale(enabled) },
                onUpdateControlsAutoHideSeconds = { sec -> viewModel.setControlsAutoHideSeconds(sec) },
                onDismissResumeBanner = { viewModel.dismissResumeBanner() },
                onDismissHudNotice = { viewModel.dismissHudNotice() },
                onRestartFromBeginning = { viewModel.restartEpisodeFromBeginning() },
                onSelectVideoQuality = { opt -> viewModel.selectVideoQualityOption(opt) },
                onUpdateMaxAudioBoostPercent = { pct -> viewModel.setMaxAudioBoostPercent(pct) },
                onUiActiveChanged = { active -> viewModel.setPlayerUiActive(active) }
            )
        }

        selectedFolder != null -> {
            val activeFolder = selectedFolder!!
            FolderDetailScreen(
                folder = activeFolder,
                showThumbnails = settings.showEpisodeThumbnails,
                compactMode = settings.compactUiMode,
                resumePlaybackEnabled = settings.resumePlayback,
                sortOption = settings.episodeSortOption,
                sortAscending = settings.episodeSortAscending,
                skipDeleteConfirmation = settings.skipDeleteConfirmation,
                onSelectSortOption = { viewModel.selectEpisodeSortOption(it) },
                onSetSortDirection = { viewModel.setEpisodeSortDirection(it) },
                onToggleSortDirection = { viewModel.toggleEpisodeSortDirection() },
                onBack = { viewModel.closeFolderDetail() },
                onPlayEpisode = { index -> viewModel.playFolderPlaylist(activeFolder, index) },
                onHideFolder = { viewModel.hideFolder(activeFolder) },
                onDeleteFolder = { viewModel.deleteFolders(listOf(activeFolder)) },
                onDeleteEpisodes = { episodes -> viewModel.deleteEpisodes(activeFolder.id, episodes) },
                onRenameEpisode = { episode, newName -> viewModel.renameEpisode(activeFolder.id, episode, newName) },
                onRenameFolder = { newName -> viewModel.renameFolder(activeFolder, newName) },
                onSetSkipDeleteConfirmation = { viewModel.setSkipDeleteConfirmation(it) }
            )
        }

        else -> {
            val settingsActions = SettingsActions(
                onRequestStorageAccess = requestStorageAccess,
                onAddSafFolder = { folderTreeLauncher.launch(null) },
                onRemoveSafRoot = { viewModel.removeFolderTree(it) },
                onPickFolderToExclude = { excludeFolderTreeLauncher.launch(null) },
                onOpenExclusionsSheet = {},
                onUnhideFolder = { viewModel.unhideFolder(it) },
                onClearPlaybackHistory = { viewModel.clearPlaybackHistory() },
                onResetAllSettings = { viewModel.resetAllSettings() },
                onUpdateAppLanguage = { viewModel.setAppLanguage(it) },
                onUpdateThemeMode = { viewModel.setThemeMode(it) },
                onUpdateAccentColor = { viewModel.setAccentColor(it) },
                onUpdatePlayerControlsTheme = { viewModel.setPlayerControlsTheme(it) },
                onUpdatePlayerSurfaceStyle = { viewModel.setPlayerSurfaceStyle(it) },
                onUpdateCompactUiMode = { viewModel.setCompactUiMode(it) },
                onUpdateShowEpisodeThumbnails = { viewModel.setShowEpisodeThumbnails(it) },
                onUpdateShowEpisodesButtonInHud = { viewModel.setShowEpisodesButtonInHud(it) },
                onUpdateShowHudBadges = { viewModel.setShowHudBadges(it) },
                onUpdateAppUiScalePercent = { viewModel.setAppUiScalePercent(it) },
                onUpdatePlayerUiScalePercent = { viewModel.setPlayerUiScalePercent(it) },
                onUpdatePlayerAutoDpiScale = { viewModel.setPlayerAutoDpiScale(it) },
                onUpdateControlsAutoHideSeconds = { viewModel.setControlsAutoHideSeconds(it) },
                onUpdateQuickActions = { viewModel.setQuickActions(it) },
                onUpdateResumePlayback = { viewModel.setResumePlayback(it) },
                onUpdatePlaybackEndMode = { viewModel.setPlaybackEndMode(it) },
                onUpdateEnableEmbeddedChapters = { viewModel.setEnableEmbeddedChapters(it) },
                onUpdateShowChapterMarkersOnTimeline = { viewModel.setShowChapterMarkersOnTimeline(it) },
                onUpdateShowBufferedProgressOnTimeline = { viewModel.setShowBufferedProgressOnTimeline(it) },
                onUpdateSeekStepSeconds = { viewModel.setSeekStepSeconds(it) },
                onUpdateDefaultPlaybackSpeed = { viewModel.setDefaultPlaybackSpeed(it) },
                onUpdateShowSkipIntro = { viewModel.setShowSkipIntroButton(it) },
                onUpdateSkipIntroSeconds = { viewModel.setSkipIntroSeconds(it) },
                onUpdateAutoEnterPip = { viewModel.setAutoEnterPip(it) },
                onUpdateBackgroundAudioPlay = { viewModel.setBackgroundAudioPlay(it) },
                onUpdateGesturesBrightnessVolume = { viewModel.setGesturesBrightnessVolume(it) },
                onUpdateGestureHorizontalSeek = { viewModel.setGestureHorizontalSeek(it) },
                onUpdateGestureDoubleTapSeek = { viewModel.setGestureDoubleTapSeek(it) },
                onUpdateGestureLongPress2x = { viewModel.setGestureLongPress2x(it) },
                onUpdateLongPressBoostSpeed = { viewModel.setLongPressBoostSpeed(it) },
                onUpdateDefaultPlayerOrientation = { viewModel.setDefaultPlayerOrientation(it) },
                onUpdateDecoderMode = { viewModel.setDecoderMode(mode = it) },
                onUpdateDefaultScaleMode = { viewModel.setDefaultScaleMode(it) },
                onUpdateHighRefreshRateEnabled = { viewModel.setHighRefreshRateEnabled(it) },
                onUpdateLtpoAdaptiveRefreshEnabled = { viewModel.setLtpoAdaptiveRefreshEnabled(it) },
                onUpdateFrameInterpolationEnabled = { viewModel.setFrameInterpolationEnabled(it) },
                onUpdateFrameInterpolationMode = { viewModel.setFrameInterpolationMode(it) },
                onUpdateAnime4KEnabled = { viewModel.setAnime4KEnabled(it) },
                onUpdateAnime4KPreset = { viewModel.setAnime4KPreset(it) },
                onUpdateAnime4KStrength = { viewModel.setAnime4KStrength(it) },
                onUpdateAutoBindAudio = { viewModel.setAutoBindAudio(it) },
                onUpdatePreferExternalAudioOnStart = { viewModel.setPreferExternalAudioOnStart(it) },
                onUpdatePreferredAudioLanguage = { viewModel.setPreferredAudioLanguage(it) },
                onUpdateDefaultAudioBoostPercent = { viewModel.setDefaultAudioBoostPercent(it) },
                onUpdateMaxAudioBoostPercent = { viewModel.setMaxAudioBoostPercent(it) },
                onUpdateAudioDeArtifactMode = { viewModel.setAudioDeArtifactMode(it) },
                onUpdateAutoDeArtifactLowBitrate = { viewModel.setAutoDeArtifactLowBitrate(it) },
                onUpdateBluetoothAudioAutoSync = { viewModel.setBluetoothAudioAutoSync(it) },
                onUpdateBluetoothAudioOffsetMs = { viewModel.setBluetoothAudioOffsetMs(it) },
                onUpdateAutoBindSubs = { viewModel.setAutoBindSubtitles(it) },
                onUpdatePreferExternalSubtitlesOnStart = { viewModel.setPreferExternalSubtitlesOnStart(it) },
                onUpdatePreferredSubtitleLanguage = { viewModel.setPreferredSubtitleLanguage(it) },
                onUpdateSubtitleFontSize = { viewModel.setSubtitleFontSize(it) },
                onUpdateSubtitleColor = { viewModel.setSubtitleColor(it) },
                onUpdateSubtitleBg = { viewModel.setSubtitleBackground(it) },
                onUpdateDefaultStreamingQuality = { viewModel.setDefaultStreamingQuality(it) },
                onUpdateTorrServerUrl = { viewModel.setTorrServerUrl(it) },
                onUpdateTorrServerAuth = { u, p -> viewModel.setTorrServerAuth(u, p) },
                onUpdateTorrServerAutoPlaySingle = { viewModel.setTorrServerAutoPlaySingle(it) },
                onUpdateTorrServerSaveToDb = { viewModel.setTorrServerSaveToDb(it) },
                onUpdateTorrentIndexerConfig = { url, key -> viewModel.setTorrentIndexerConfig(url, key) },
                onUpdateEnableDirectRutorSearch = { viewModel.setEnableDirectRutorSearch(it) },
                onUpdateEnableAnimeTrackerSearch = { viewModel.setEnableAnimeTrackerSearch(it) },
                onUpdateEnableTrackerDoh = { viewModel.setEnableTrackerDoh(it) },
                onUpdateIgnoreNomediaFolders = { viewModel.setIgnoreNomediaFolders(it) },
                onUpdateExcludeCameraAndScreenshots = { viewModel.setExcludeCameraAndScreenshots(it) },
                onUpdateMinVideoSizeMb = { viewModel.setMinVideoSizeMb(it) },
                onUpdateSkipDeleteConfirmation = { viewModel.setSkipDeleteConfirmation(it) },
                onUpdateRememberPerEpisodeSettings = { viewModel.setRememberPerEpisodeSettings(it) },
                onUpdateGraphicsBackend = { viewModel.setGraphicsBackend(it) }
            )

            val torrentsActions = TorrentsActions(
                onQueryChange = { viewModel.torrentsController.updateQuery(it) },
                onSubmitSearch = { overrideQ -> viewModel.torrentsController.submitSearch(overrideQ) },
                onSelectTrackerFilter = { viewModel.torrentsController.setTrackerFilter(it) },
                onSelectQualityFilter = { viewModel.torrentsController.setQualityFilter(it) },
                onSelectSortMode = { viewModel.torrentsController.setSortMode(it) },
                onCheckConnection = { viewModel.torrentsController.checkTorrServerConnection() },
                onWatchResult = { viewModel.torrentsController.openSearchResultForPlayback(it) },
                onWatchRawLink = { link, title ->
                    viewModel.torrentsController.openRawLinkForPlayback(link, title)
                },
                onPickTorrentFileForWatch = {
                    pickTorrentForExternal = false
                    openTorrentDocumentLauncher.launch(
                        arrayOf("application/x-bittorrent", "application/octet-stream", "*/*")
                    )
                },
                onPickTorrentFileForExternal = {
                    pickTorrentForExternal = true
                    openTorrentDocumentLauncher.launch(
                        arrayOf("application/x-bittorrent", "application/octet-stream", "*/*")
                    )
                },
                onSaveServerConfig = { url, user, pass ->
                    viewModel.setTorrServerConfig(url, user, pass)
                },
                onSelectInspectorVoiceover = { voice ->
                    viewModel.torrentsController.selectInspectorVoiceover(voice)
                },
                onPlayInspectorEpisode = { idx ->
                    viewModel.torrentsController.playInspectorEpisode(idx)
                },
                onForceDirectStream = { link, title ->
                    viewModel.torrentsController.forceStartDirectStream(link, title)
                },
                onDismissInspector = { viewModel.torrentsController.dismissInspector() },
                onLoadSavedTorrents = { viewModel.torrentsController.loadSavedServerTorrents() },
                onOpenSavedTorrent = { viewModel.torrentsController.openSavedServerTorrent(it) },
                onDropSavedCache = { hash ->
                    viewModel.torrentsController.dropSavedServerTorrentCache(hash)
                },
                onRemoveSavedTorrent = { hash ->
                    viewModel.torrentsController.removeSavedServerTorrent(hash)
                },
                onOpenReleaseDetails = { result ->
                    viewModel.torrentsController.openReleaseDetails(result)
                },
                onDismissReleaseDetails = {
                    viewModel.torrentsController.dismissReleaseDetails()
                }
            )

            LibraryScreen(
                currentTab = currentTab,
                onSelectTab = { viewModel.selectTab(it) },
                visibleFolders = visibleFolders,
                hiddenFolders = hiddenFolders,
                recentItems = recentProgress,
                lastPlayedFile = lastPlayedFile,
                libraryRoots = libraryRoots,
                settings = settings,
                torrentsState = torrentsState,
                torrentsActions = torrentsActions,
                hasStorageAccess = hasStorageAccess,
                hasAllFilesAccess = hasAllFilesAccess,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                isScanning = isScanning,
                onRequestStorageAccess = requestStorageAccess,
                onAddSafFolder = { folderTreeLauncher.launch(null) },
                onPickFolderToExclude = { excludeFolderTreeLauncher.launch(null) },
                onOpenSingleFile = { openVideoFileLauncher.launch(arrayOf("video/*")) },
                onPlayStreamUrl = { url, title -> viewModel.playStreamUrl(url, title) },
                onRescan = { viewModel.rescanLibrary() },
                onOpenFolder = { viewModel.openFolder(it.id) },
                onHideFolder = { viewModel.hideFolder(it) },
                onUnhideFolder = { viewModel.unhideFolder(it) },
                onDeleteFolders = { folders -> viewModel.deleteFolders(folders) },
                onRenameFolder = { folder, newName -> viewModel.renameFolder(folder, newName) },
                onSetSkipDeleteConfirmation = { viewModel.setSkipDeleteConfirmation(it) },
                onResumeRecent = { viewModel.resumeRecentItem(it) },
                onOpenLastPlayed = { onMissing -> viewModel.openLastPlayedFile(onMissing) },
                onSelectFolderSortOption = { viewModel.selectFolderSortOption(it) },
                onSetFolderSortDirection = { viewModel.setFolderSortDirection(it) },
                onToggleFolderSortDirection = { viewModel.toggleFolderSortDirection() },
                settingsActions = settingsActions
            )
        }
    }
}
