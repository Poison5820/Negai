package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.HiddenFolderEntity
import com.example.data.LibraryRootEntity
import com.example.model.Anime4KPreset
import com.example.model.AppAccentColor
import com.example.model.AppLanguage
import com.example.model.AppThemeMode
import com.example.model.AudioDeArtifactMode
import com.example.model.DecoderMode
import com.example.model.FrameInterpolationMode
import com.example.model.GraphicsBackend
import com.example.model.PlaybackEndMode
import com.example.model.PlayerControlsTheme
import com.example.model.PlayerOrientationMode
import com.example.model.PlayerQuickAction
import com.example.model.PlayerSettings
import com.example.model.SettingsCategory
import com.example.model.SubtitleColorPreset
import com.example.model.VideoScaleMode
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.localizedBadge
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.tr
import kotlin.math.roundToInt

data class SettingsActions(
    val onRequestStorageAccess: () -> Unit,
    val onAddSafFolder: () -> Unit,
    val onRemoveSafRoot: (String) -> Unit,
    val onPickFolderToExclude: () -> Unit,
    val onOpenExclusionsSheet: () -> Unit,
    val onUnhideFolder: (String) -> Unit,
    val onClearPlaybackHistory: () -> Unit,
    val onResetAllSettings: () -> Unit,
    val onUpdateAppLanguage: (AppLanguage) -> Unit = {},
    val onUpdateThemeMode: (AppThemeMode) -> Unit,
    val onUpdateAccentColor: (AppAccentColor) -> Unit = {},
    val onUpdatePlayerControlsTheme: (PlayerControlsTheme) -> Unit = {},
    val onUpdateCompactUiMode: (Boolean) -> Unit = {},
    val onUpdateShowEpisodeThumbnails: (Boolean) -> Unit,
    val onUpdateShowEpisodesButtonInHud: (Boolean) -> Unit = {},
    val onUpdateShowHudBadges: (Boolean) -> Unit = {},
    val onUpdateQuickActions: (List<PlayerQuickAction>) -> Unit,
    val onUpdateAppUiScalePercent: (Int) -> Unit = {},
    val onUpdatePlayerUiScalePercent: (Int) -> Unit = {},
    val onUpdatePlayerAutoDpiScale: (Boolean) -> Unit = {},
    val onUpdateControlsAutoHideSeconds: (Int) -> Unit = {},
    val onUpdateResumePlayback: (Boolean) -> Unit,
    val onUpdatePlaybackEndMode: (PlaybackEndMode) -> Unit,
    val onUpdateEnableEmbeddedChapters: (Boolean) -> Unit = {},
    val onUpdateShowChapterMarkersOnTimeline: (Boolean) -> Unit = {},
    val onUpdateShowBufferedProgressOnTimeline: (Boolean) -> Unit = {},
    val onUpdateSeekStepSeconds: (Int) -> Unit,
    val onUpdateDefaultPlaybackSpeed: (Float) -> Unit,
    val onUpdateShowSkipIntro: (Boolean) -> Unit,
    val onUpdateSkipIntroSeconds: (Int) -> Unit,
    val onUpdateAutoEnterPip: (Boolean) -> Unit,
    val onUpdateBackgroundAudioPlay: (Boolean) -> Unit,
    val onUpdateGesturesBrightnessVolume: (Boolean) -> Unit,
    val onUpdateGestureHorizontalSeek: (Boolean) -> Unit,
    val onUpdateGestureDoubleTapSeek: (Boolean) -> Unit,
    val onUpdateGestureLongPress2x: (Boolean) -> Unit,
    val onUpdateLongPressBoostSpeed: (Float) -> Unit,
    val onUpdateDefaultPlayerOrientation: (PlayerOrientationMode) -> Unit,
    val onUpdateDecoderMode: (DecoderMode) -> Unit,
    val onUpdateDefaultScaleMode: (VideoScaleMode) -> Unit,
    val onUpdateHighRefreshRateEnabled: (Boolean) -> Unit = {},
    val onUpdateLtpoAdaptiveRefreshEnabled: (Boolean) -> Unit = {},
    val onUpdateFrameInterpolationEnabled: (Boolean) -> Unit = {},
    val onUpdateFrameInterpolationMode: (FrameInterpolationMode) -> Unit = {},
    val onUpdateAnime4KEnabled: (Boolean) -> Unit,
    val onUpdateAnime4KPreset: (Anime4KPreset) -> Unit,
    val onUpdateAnime4KStrength: (Float) -> Unit,
    val onUpdateAutoBindAudio: (Boolean) -> Unit,
    val onUpdatePreferExternalAudioOnStart: (Boolean) -> Unit,
    val onUpdateDefaultAudioBoostPercent: (Int) -> Unit,
    val onUpdateAudioDeArtifactMode: (AudioDeArtifactMode) -> Unit = {},
    val onUpdateAutoDeArtifactLowBitrate: (Boolean) -> Unit = {},
    val onUpdateBluetoothAudioAutoSync: (Boolean) -> Unit = {},
    val onUpdateBluetoothAudioOffsetMs: (Long) -> Unit = {},
    val onUpdateAutoBindSubs: (Boolean) -> Unit,
    val onUpdateSubtitleFontSize: (Float) -> Unit,
    val onUpdateSubtitleColor: (Long) -> Unit,
    val onUpdateSubtitleBg: (Boolean) -> Unit,
    val onUpdateTorrServerUrl: (String) -> Unit = {},
    val onUpdateTorrServerAuth: (String, String) -> Unit = { _, _ -> },
    val onUpdateTorrServerAutoPlaySingle: (Boolean) -> Unit = {},
    val onUpdateTorrServerSaveToDb: (Boolean) -> Unit = {},
    val onUpdateTorrentIndexerConfig: (String, String) -> Unit = { _, _ -> },
    val onUpdateEnableDirectRutorSearch: (Boolean) -> Unit = {},
    val onUpdateEnableAnimeTrackerSearch: (Boolean) -> Unit = {},
    val onUpdateEnableTrackerDoh: (Boolean) -> Unit = {},
    val onUpdateIgnoreNomediaFolders: (Boolean) -> Unit,
    val onUpdateExcludeCameraAndScreenshots: (Boolean) -> Unit,
    val onUpdateMinVideoSizeMb: (Int) -> Unit,
    val onUpdateSkipDeleteConfirmation: (Boolean) -> Unit = {},
    val onUpdateRememberPerEpisodeSettings: (Boolean) -> Unit = {},
    val onUpdateGraphicsBackend: (GraphicsBackend) -> Unit = {}
)

@Composable
fun SettingsTabContent(
    settings: PlayerSettings,
    hasStorageAccess: Boolean,
    hasAllFilesAccess: Boolean,
    libraryRoots: List<LibraryRootEntity>,
    hiddenFolders: List<HiddenFolderEntity>,
    recentCount: Int,
    actions: SettingsActions
) {
    var selectedCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
    var showQuickActionsCustomizer by rememberSaveable { mutableStateOf(false) }

    val activeCategory = SettingsCategory.entries.firstOrNull { it.id == selectedCategoryId }

    BackHandler(enabled = activeCategory != null) {
        selectedCategoryId = null
    }

    if (activeCategory == null) {
        SettingsCategoryHub(
            settings = settings,
            hasStorageAccess = hasStorageAccess,
            hasAllFilesAccess = hasAllFilesAccess,
            excludedCount = hiddenFolders.size,
            onSelectCategory = { cat -> selectedCategoryId = cat.id }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 1200.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val isEnglish = LocalIsEnglish.current
                    IconButton(
                        onClick = { selectedCategoryId = null },
                        modifier = Modifier.testTag("settings_category_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = tr("Назад к категориям настроек", "Back to settings categories")
                        )
                    }
                    Icon(
                        imageVector = activeCategory.icon(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = activeCategory.localizedTitle(isEnglish),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("settings_category_detail_list"),
                contentPadding = PaddingValues(
                    horizontal = if (settings.compactUiMode) 8.dp else 10.dp,
                    vertical = if (settings.compactUiMode) 8.dp else 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(if (settings.compactUiMode) 12.dp else 14.dp)
            ) {
                item(key = activeCategory.id) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(if (settings.compactUiMode) 12.dp else 14.dp)
                    ) {
                        when (activeCategory) {
                            SettingsCategory.APPEARANCE -> AppearanceSettingsPage(
                                settings = settings,
                                actions = actions,
                                onOpenQuickActionsCustomizer = { showQuickActionsCustomizer = true }
                            )

                            SettingsCategory.PLAYBACK -> PlaybackSettingsPage(
                                settings = settings,
                                actions = actions
                            )

                            SettingsCategory.CONTROLS -> GesturesSettingsPage(
                                settings = settings,
                                actions = actions
                            )

                            SettingsCategory.ENHANCEMENTS -> VideoAndAnime4KSettingsPage(
                                settings = settings,
                                actions = actions
                            )

                            SettingsCategory.AUDIO_AND_SUBTITLES -> AudioAndSubtitlesSettingsPage(
                                settings = settings,
                                actions = actions
                            )

                            SettingsCategory.TORRENTS -> TorrServerSettingsPage(
                                settings = settings,
                                actions = actions
                            )

                            SettingsCategory.STORAGE -> StorageAndExclusionsSettingsPage(
                                settings = settings,
                                hasStorageAccess = hasStorageAccess,
                                hasAllFilesAccess = hasAllFilesAccess,
                                libraryRoots = libraryRoots,
                                hiddenFolders = hiddenFolders,
                                recentCount = recentCount,
                                actions = actions
                            )

                            SettingsCategory.DEVICE_INFO -> DeviceInfoSettingsPage()

                            SettingsCategory.MISCELLANEOUS -> MiscellaneousSettingsPage(
                                settings = settings,
                                actions = actions
                            )
                        }
                    }
                }

                item(key = "category_bottom_spacer") {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    if (showQuickActionsCustomizer) {
        QuickActionsCustomizerModalSheet(
            activeActions = settings.quickActions,
            onUpdateQuickActions = actions.onUpdateQuickActions,
            onDismiss = { showQuickActionsCustomizer = false }
        )
    }
}

@Composable
private fun SettingsCategoryHub(
    settings: PlayerSettings,
    hasStorageAccess: Boolean,
    hasAllFilesAccess: Boolean,
    excludedCount: Int,
    onSelectCategory: (SettingsCategory) -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f
    val isPureOled = MaterialTheme.colorScheme.background.red < 0.01f &&
        MaterialTheme.colorScheme.background.green < 0.01f &&
        MaterialTheme.colorScheme.background.blue < 0.01f
    val hubCardBg = when {
        !isDark -> androidx.compose.ui.graphics.Color(0xFFFFFFFF).copy(alpha = 0.94f)
        isPureOled -> androidx.compose.ui.graphics.Color(0xFF050505).copy(alpha = 0.96f)
        else -> androidx.compose.ui.graphics.Color(0xFF0A101B).copy(alpha = 0.92f)
    }
    val hubBorderColor = when {
        !isDark -> MaterialTheme.colorScheme.primary.copy(alpha = 0.48f)
        isPureOled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.58f)
    }
    val hubBorder = remember(hubBorderColor) { BorderStroke(0.75.dp, hubBorderColor) }
    val hubCardShape = remember(settings.compactUiMode) {
        RoundedCornerShape(if (settings.compactUiMode) 15.dp else 18.dp)
    }
    val hubIconShape = remember { RoundedCornerShape(13.dp) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 1200.dp)
            .testTag("settings_lazy_list"),
        contentPadding = PaddingValues(
            horizontal = if (settings.compactUiMode) 8.dp else 10.dp,
            vertical = if (settings.compactUiMode) 8.dp else 12.dp
        ),
        verticalArrangement = Arrangement.spacedBy(if (settings.compactUiMode) 10.dp else 12.dp)
    ) {
        items(
            items = SettingsCategory.entries,
            key = { it.id }
        ) { category ->
            val summary = categorySummary(
                category = category,
                settings = settings,
                hasStorageAccess = hasStorageAccess,
                hasAllFilesAccess = hasAllFilesAccess,
                excludedCount = excludedCount,
                isEnglish = isEnglish
            )
            Card(
                shape = hubCardShape,
                border = hubBorder,
                colors = CardDefaults.cardColors(
                    containerColor = hubCardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = if (settings.compactUiMode) 64.dp else 72.dp)
                    .clip(hubCardShape)
                    .clickable { onSelectCategory(category) }
                    .testTag("settings_category_${category.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = if (settings.compactUiMode) 10.dp else 12.dp,
                            vertical = if (settings.compactUiMode) 10.dp else 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(hubIconShape)
                            .background(
                                if (isPureOled) {
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon(),
                            contentDescription = null,
                            tint = if (isPureOled) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = category.localizedTitle(isEnglish),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item(key = "hub_bottom_spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun SettingsCategory.icon(): ImageVector = when (this) {
    SettingsCategory.APPEARANCE -> Icons.Default.Palette
    SettingsCategory.PLAYBACK -> Icons.Default.PlayCircleOutline
    SettingsCategory.CONTROLS -> Icons.Default.Swipe
    SettingsCategory.ENHANCEMENTS -> Icons.Default.AutoAwesome
    SettingsCategory.AUDIO_AND_SUBTITLES -> Icons.Default.GraphicEq
    SettingsCategory.TORRENTS -> Icons.Default.Dns
    SettingsCategory.STORAGE -> Icons.Default.Storage
    SettingsCategory.DEVICE_INFO -> Icons.Default.Memory
    SettingsCategory.MISCELLANEOUS -> Icons.Default.Tune
}

private fun categorySummary(
    category: SettingsCategory,
    settings: PlayerSettings,
    hasStorageAccess: Boolean,
    hasAllFilesAccess: Boolean,
    excludedCount: Int,
    isEnglish: Boolean = false
): String = when (category) {
    SettingsCategory.APPEARANCE -> {
        val langTxt = settings.appLanguage.shortBadge
        val scaleTxt = "${settings.playerUiScalePercent}%"
        "${settings.themeMode.localizedBadge(isEnglish)} • ${settings.accentColor.localizedTitle(isEnglish)} • $langTxt • $scaleTxt"
    }
    SettingsCategory.PLAYBACK -> {
        if (isEnglish) {
            val resumeTxt = if (settings.resumePlayback) "Resume ON" else "From start"
            "$resumeTxt • Step ${settings.seekStepSeconds}s • +${settings.skipIntroSeconds}s Intro"
        } else {
            val resumeTxt = if (settings.resumePlayback) "Продолжение" else "С начала"
            "$resumeTxt • Шаг ${settings.seekStepSeconds} сек • +${settings.skipIntroSeconds}с Опенинг"
        }
    }
    SettingsCategory.CONTROLS -> {
        if (isEnglish) {
            val boostTxt = if (settings.gestureLongPress2x) "Hold ${settings.longPressBoostSpeed}x" else "Hold OFF"
            "$boostTxt • Orientation: ${settings.defaultPlayerOrientation.shortBadge}"
        } else {
            val boostTxt = if (settings.gestureLongPress2x) "Удержание ${settings.longPressBoostSpeed}x" else "Удержание выкл"
            "$boostTxt • Ориентация: ${settings.defaultPlayerOrientation.shortBadge}"
        }
    }
    SettingsCategory.ENHANCEMENTS -> {
        val memc = settings.effectiveFrameInterpolationMode.badgeLabel
        val a4k = if (settings.anime4kEnabled) settings.anime4kPreset.badgeLabel else if (isEnglish) "OFF" else "Выкл"
        "${settings.decoderMode.shortBadge} • $memc • Anime4K: $a4k"
    }
    SettingsCategory.AUDIO_AND_SUBTITLES -> {
        val colorLabel = SubtitleColorPreset.fromArgb(settings.subtitleColorArgb).label
        val dspBadge = settings.audioDeArtifactMode.localizedBadge(isEnglish)
        if (isEnglish) {
            "De-Artifact: $dspBadge • Boost ${settings.defaultAudioBoostPercent}% • ${settings.subtitleFontSizeSp.roundToInt()} sp • $colorLabel"
        } else {
            "Очистка: $dspBadge • Усиление ${settings.defaultAudioBoostPercent}% • ${settings.subtitleFontSizeSp.roundToInt()} sp • $colorLabel"
        }
    }
    SettingsCategory.TORRENTS -> {
        val indexerHost = settings.torrentIndexerUrl
            .substringBefore(',')
            .trim()
            .removePrefix("https://")
            .removePrefix("http://")
            .substringBefore('/')
        val dohLabel = if (settings.enableTrackerDoh) {
            if (isEnglish) "DoH ON" else "DoH вкл"
        } else {
            if (isEnglish) "DoH OFF" else "DoH выкл"
        }
        "${settings.torrServerUrl.removePrefix("http://")} • $indexerHost • $dohLabel"
    }
    SettingsCategory.STORAGE -> {
        if (isEnglish) {
            val accessLabel = when {
                hasAllFilesAccess -> "All files access"
                hasStorageAccess -> "Media access"
                else -> "Access required"
            }
            "$accessLabel • Exclusions: $excludedCount"
        } else {
            val accessLabel = when {
                hasAllFilesAccess -> "Полный доступ"
                hasStorageAccess -> "Доступ к медиа"
                else -> "Требуется доступ"
            }
            "$accessLabel • Исключений: $excludedCount"
        }
    }
    SettingsCategory.DEVICE_INFO -> {
        if (isEnglish) {
            "Screen resolution, refresh rate (Hz) & HW/SW MediaCodec decoders"
        } else {
            "Разрешение экрана, герцовка (Гц) и аппаратные/программные декодеры"
        }
    }
    SettingsCategory.MISCELLANEOUS -> {
        val memStatus = if (isEnglish) {
            if (settings.rememberPerEpisodeSettings) "Per-episode memory ON" else "Per-episode memory OFF"
        } else {
            if (settings.rememberPerEpisodeSettings) "Память серий вкл" else "Память серий выкл"
        }
        "${settings.graphicsBackend.badge} • $memStatus"
    }
}
