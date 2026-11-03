package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.ui.components.ScaleToFitWidthBox
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.model.Anime4KPreset
import com.example.model.AudioDeArtifactMode
import com.example.model.DecoderMode
import com.example.model.EpisodeItem
import com.example.model.PlayerSettings
import com.example.model.SubtitleColorPreset
import com.example.model.VideoScaleMode
import com.example.player.AudioDelayProcessor
import com.example.player.AudioTrackUiOption
import com.example.player.BluetoothAudioRouteState
import com.example.player.SubtitleTrackUiOption
import com.example.player.VideoQualityUiOption
import com.example.ui.components.InstantSlider
import com.example.ui.components.ModeInfoIconButton
import com.example.ui.components.ModeInfoModalDialog
import com.example.ui.components.ModeInfoTopic
import com.example.ui.components.formatDelaySeconds
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.localizedBadge
import com.example.ui.i18n.localizedDescription
import com.example.ui.i18n.localizedLabel
import com.example.ui.i18n.localizedSubtitle
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.tr
import com.example.ui.theme.ensureReadableOn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt

internal val PlayerFrostedGlassBaseColor = Color(0xF2050505)

@Composable
internal fun playerPopupSheetContainerColor(): Color {
    val bg = MaterialTheme.colorScheme.background
    val bgLum = bg.red * 0.2126f + bg.green * 0.7152f + bg.blue * 0.0722f
    val isDark = bgLum < 0.35f
    val isPureOled = bg.red < 0.01f && bg.green < 0.01f && bg.blue < 0.01f
    return when {
        !isDark -> Color(0xF8FFFFFF)
        isPureOled -> Color(0xFA040404)
        else -> Color(0xF20A101C)
    }
}

@Composable
internal fun playerPopupCardColor(): Color {
    val bg = MaterialTheme.colorScheme.background
    val bgLum = bg.red * 0.2126f + bg.green * 0.7152f + bg.blue * 0.0722f
    val isDark = bgLum < 0.35f
    val isPureOled = bg.red < 0.01f && bg.green < 0.01f && bg.blue < 0.01f
    return when {
        !isDark -> MaterialTheme.colorScheme.surfaceContainerLow
        isPureOled -> Color(0xFF0A0A0A)
        else -> Color(0xD9111A2C)
    }
}

@Composable
internal fun playerPopupBorderStroke(): BorderStroke {
    val bg = MaterialTheme.colorScheme.background
    val isPureOled = bg.red < 0.01f && bg.green < 0.01f && bg.blue < 0.01f
    val accent = MaterialTheme.colorScheme.primary
    return if (isPureOled) {
        BorderStroke(0.8.dp, accent.copy(alpha = 0.22f))
    } else {
        BorderStroke(1.dp, accent.copy(alpha = 0.38f))
    }
}

@Composable
internal fun playerFrostedGlassBrush(accentAlpha: Float = 0.04f): Brush {
    val bg = MaterialTheme.colorScheme.background
    val isPureOled = bg.red < 0.01f && bg.green < 0.01f && bg.blue < 0.01f
    val accent = MaterialTheme.colorScheme.primary
    return if (isPureOled) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF050505),
                Color(0xFF030303)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                accent.copy(alpha = accentAlpha.coerceAtMost(0.06f)),
                Color.Transparent
            )
        )
    }
}

/**
 * Unified Theme-Adaptive ModalBottomSheet for all player pop-ups:
 * - Horizontally compact (`sheetMaxWidth = 440.dp`)
 * - Adapts to the active theme (Light, Dark, and Pure OLED with pitch-black surface)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerFrostedModalSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 440.dp,
    content: @Composable () -> Unit
) {
    val bg = MaterialTheme.colorScheme.background
    val bgLum = bg.red * 0.2126f + bg.green * 0.7152f + bg.blue * 0.0722f
    val isDark = bgLum < 0.35f
    val isPureOled = bg.red < 0.01f && bg.green < 0.01f && bg.blue < 0.01f

    val sheetBg = when {
        !isDark -> Color(0xFAFFFFFF)
        isPureOled -> Color(0xFC040404)
        else -> Color(0xF50B111E)
    }
    val readableAccent = MaterialTheme.colorScheme.primary.ensureReadableOn(
        sheetBg,
        minContrast = if (isDark) 5.2f else 4.8f
    )
    val readableSecondary = MaterialTheme.colorScheme.secondary.ensureReadableOn(
        sheetBg,
        minContrast = if (isDark) 5.0f else 4.6f
    )
    val readableTertiary = MaterialTheme.colorScheme.tertiary.ensureReadableOn(
        sheetBg,
        minContrast = if (isDark) 4.8f else 4.5f
    )
    val adaptiveScheme = if (!isDark) {
        MaterialTheme.colorScheme.copy(
            surface = sheetBg,
            surfaceContainerLowest = Color.White,
            surfaceContainerLow = Color(0xFFF6F8FC),
            surfaceContainer = Color(0xFFEEF2F8),
            surfaceContainerHigh = Color(0xFFE4EAF3)
        )
    } else {
        MaterialTheme.colorScheme.copy(
            primary = readableAccent,
            onPrimary = Color(0xFF05070C),
            primaryContainer = if (isPureOled) {
                lerp(Color(0xFF0E0E0E), readableAccent, 0.14f)
            } else {
                lerp(Color(0xD9142138), readableAccent, 0.30f)
            },
            onPrimaryContainer = Color.White,
            secondary = readableSecondary,
            onSecondary = Color(0xFF05070C),
            secondaryContainer = if (isPureOled) {
                lerp(Color(0xFF0E0E0E), readableSecondary, 0.12f)
            } else {
                lerp(Color(0xD9142138), readableSecondary, 0.26f)
            },
            onSecondaryContainer = Color.White,
            tertiary = readableTertiary,
            tertiaryContainer = if (isPureOled) {
                lerp(Color(0xFF0E0E0E), readableTertiary, 0.12f)
            } else {
                lerp(Color(0xD9142138), readableTertiary, 0.24f)
            },
            onTertiaryContainer = Color.White,
            surface = sheetBg,
            onSurface = Color(0xFFF8FAFC),
            surfaceVariant = if (isPureOled) Color(0xFF0C0C0C) else Color(0xD9131D30),
            onSurfaceVariant = Color(0xFFCBD5E1),
            surfaceContainerLowest = if (isPureOled) Color(0xFF030303) else Color(0xFF070B14),
            surfaceContainerLow = if (isPureOled) Color(0xFF080808) else Color(0xFF0C1220),
            surfaceContainer = if (isPureOled) Color(0xFF0D0D0D) else Color(0xFF121A2B),
            surfaceContainerHigh = if (isPureOled) Color(0xFF141414) else Color(0xFF192338),
            outline = readableAccent.copy(alpha = if (isPureOled) 0.25f else 0.42f),
            outlineVariant = if (isPureOled) Color(0xFF1A1A1A) else Color.White.copy(alpha = 0.14f)
        )
    }
    val sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetMaxWidth = maxWidth,
        shape = sheetShape,
        containerColor = Color.Transparent,
        scrimColor = PlayerPopupSheetScrimColor,
        dragHandle = null,
        modifier = modifier
    ) {
        KeepPlayerSheetImmersive()
        MaterialTheme(colorScheme = adaptiveScheme) {
            CompositionLocalProvider(LocalContentColor provides adaptiveScheme.onSurface) {
                Surface(
                    color = sheetBg,
                    shape = sheetShape,
                    border = BorderStroke(
                        width = if (isPureOled) 0.8.dp else 1.dp,
                        color = readableAccent.copy(alpha = if (isPureOled) 0.22f else 0.36f)
                    ),
                    shadowElevation = if (isPureOled) 0.dp else 14.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(4.dp)
                                    .clip(CircleShape)
                                    .background(readableAccent.copy(alpha = 0.45f))
                            )
                        }
                        content()
                    }
                }
            }
        }
    }
}

private tailrec fun Context.findHostActivityWindow(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findHostActivityWindow()
    else -> null
}

/**
 * Keeps the Android status bar and navigation bar hidden inside any player ModalBottomSheet
 * so opening a popup menu never drops the system notification shade or causes window-inset jank.
 */
@Composable
internal fun KeepPlayerSheetImmersive() {
    val view = LocalView.current
    val context = LocalContext.current
    SideEffect {
        val dialogWindow = (view.parent as? DialogWindowProvider)?.window
        val activityWindow = context.findHostActivityWindow()?.window
        listOfNotNull(dialogWindow, activityWindow).forEach { win ->
            WindowCompat.setDecorFitsSystemWindows(win, false)
            win.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            val controller = WindowCompat.getInsetsController(win, win.decorView)
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}

internal val PlayerPopupSheetContainerColor: Color
    @Composable get() = playerPopupSheetContainerColor()

internal val PlayerPopupSheetScrimColor = Color.Black.copy(alpha = 0.22f)

internal val PlayerPopupCardColor: Color
    @Composable get() = playerPopupCardColor()

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AudioTracksModalSheet(
    audioOptions: List<AudioTrackUiOption>,
    audioBoostPercent: Int,
    maxAudioBoostPercent: Int = 200,
    audioDeArtifactMode: AudioDeArtifactMode = AudioDeArtifactMode.STANDARD,
    autoDeArtifactLowBitrate: Boolean = true,
    activeAudioBitrateKbps: Int = 0,
    isAudioDeArtifactActive: Boolean = false,
    audioDelayMs: Long = 0L,
    effectiveAudioDelayMs: Long = audioDelayMs,
    bluetoothRoute: BluetoothAudioRouteState = BluetoothAudioRouteState(),
    bluetoothAutoSyncEnabled: Boolean = true,
    bluetoothAppliedOffsetMs: Long = 0L,
    bluetoothConfiguredOffsetMs: Long = -150L,
    lastAudioResyncTimestampMs: Long = 0L,
    onToggleBluetoothAutoSync: (Boolean) -> Unit = {},
    onSelectAudio: (AudioTrackUiOption) -> Unit,
    onSetAudioBoostPercent: (Int) -> Unit,
    onUpdateMaxAudioBoostPercent: (Int) -> Unit = {},
    onSelectAudioDeArtifactMode: (AudioDeArtifactMode) -> Unit = {},
    onToggleAutoDeArtifactLowBitrate: (Boolean) -> Unit = {},
    onAdjustAudioDelayMs: (Long) -> Unit = {},
    onSetAudioDelayMs: (Long) -> Unit = {},
    onResyncAudio: () -> Unit = {},
    onPickExternalAudioFile: () -> Unit,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    val cardBg = playerPopupCardColor()
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    tr("Аудиодорожки и синхронизация", "Audio Tracks & Sync"),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = onPickExternalAudioFile,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("player_attach_audio_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    tr("Подключить внешнюю аудиодорожку (.mka, .mp3, .aac…)", "Attach External Audio (.mka, .mp3, .aac…)"),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            if (audioOptions.isEmpty()) {
                Text(
                    text = tr(
                        "Аудиодорожки в контейнере не обнаружены. Подключите внешний аудиофайл кнопкой выше.",
                        "No audio tracks found in container. Attach an external audio file above."
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                audioOptions.forEachIndexed { idx, opt ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (opt.isSelected) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                            } else {
                                PlayerPopupCardColor
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectAudio(opt) }
                            .testTag("audio_track_option_$idx")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = opt.isSelected,
                                onClick = { onSelectAudio(opt) }
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = opt.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Surface(
                                color = if (opt.isExternal) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                                } else {
                                    Color.White.copy(alpha = 0.12f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = opt.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (opt.isExternal) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            // Audio De-Artifact / Compression Restoration DSP Section
            var showAudioDspInfoModal by rememberSaveable { mutableStateOf(false) }
            if (showAudioDspInfoModal) {
                ModeInfoModalDialog(
                    topic = ModeInfoTopic.AUDIO_CLEANUP,
                    onDismiss = { showAudioDspInfoModal = false }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        tr(
                            "Очистка звука (De-Artifact DSP)",
                            "Audio Cleanup (De-Artifact DSP)"
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    val bitrateHint = if (activeAudioBitrateKbps > 0) {
                        tr(
                            "Поток: $activeAudioBitrateKbps кбит/с • ${if (isAudioDeArtifactActive) "Фильтр активен" else "Байпас (без изменений)"}",
                            "Stream: $activeAudioBitrateKbps kbps • ${if (isAudioDeArtifactActive) "DSP Active" else "Bypass"}"
                        )
                    } else {
                        tr(
                            "Подавление ВЧ-звона и клиппинга AAC/MP3/Opus",
                            "De-ringing & de-clipper for low-bitrate tracks"
                        )
                    }
                    Text(
                        text = bitrateHint,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isAudioDeArtifactActive) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
                ModeInfoIconButton(
                    topic = ModeInfoTopic.AUDIO_CLEANUP,
                    onClick = { showAudioDspInfoModal = true }
                )
                Surface(
                    color = if (isAudioDeArtifactActive) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f)
                    } else {
                        Color.White.copy(alpha = 0.10f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = audioDeArtifactMode.localizedBadge(isEnglish),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isAudioDeArtifactActive) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("audio_de_artifact_status_badge")
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    AudioDeArtifactMode.SOFT,
                    AudioDeArtifactMode.STANDARD,
                    AudioDeArtifactMode.AGGRESSIVE,
                    AudioDeArtifactMode.OFF
                ).chunked(2).forEach { rowModes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowModes.forEach { mode ->
                            val selected = audioDeArtifactMode == mode
                            Surface(
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                                } else {
                                    cardBg
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.14f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onSelectAudioDeArtifactMode(mode) }
                                    .testTag("sheet_audio_de_artifact_${mode.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = { onSelectAudioDeArtifactMode(mode) },
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = mode.localizedTitle(isEnglish),
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleAutoDeArtifactLowBitrate(!autoDeArtifactLowBitrate) }
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tr(
                            "Авто для низкого битрейта (≤ 128 кбит/с)",
                            "Auto-enable only for low bitrate (≤ 128 kbps)"
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = tr(
                            "Не затрагивать FLAC/lossless и дорожки высокого битрейта (> 132 кбит/с)",
                            "Bypass DSP on FLAC/lossless and high-bitrate tracks (> 132 kbps)"
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = autoDeArtifactLowBitrate,
                    onCheckedChange = onToggleAutoDeArtifactLowBitrate,
                    modifier = Modifier.testTag("sheet_auto_de_artifact_low_bitrate_switch")
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            // Audio Synchronization & Manual Fine-Tune Offset Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        tr("Синхронизация аудио и видео", "Audio & Video Sync Offset"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = tr(
                            "Итоговый сдвиг потока: ${AudioDelayProcessor.formatDelayLabel(effectiveAudioDelayMs)}",
                            "Effective stream offset: ${AudioDelayProcessor.formatDelayLabel(effectiveAudioDelayMs)}"
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("effective_audio_delay_text")
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = AudioDelayProcessor.formatDelayLabel(effectiveAudioDelayMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("audio_delay_value_text")
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = { onAdjustAudioDelayMs(-250L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_delay_minus_250")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("-250мс", "-250ms"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                OutlinedButton(
                    onClick = { onAdjustAudioDelayMs(-50L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_delay_minus_50")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("-50мс", "-50ms"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                FilledTonalButton(
                    onClick = { onSetAudioDelayMs(0L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_delay_reset")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("0 мс", "0ms"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                OutlinedButton(
                    onClick = { onAdjustAudioDelayMs(50L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_delay_plus_50")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("+50мс", "+50ms"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                OutlinedButton(
                    onClick = { onAdjustAudioDelayMs(250L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_delay_plus_250")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("+250мс", "+250ms"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }
            }

            InstantSlider(
                value = audioDelayMs.toFloat().coerceIn(-5000f, 5000f),
                onValueChange = { rawMs ->
                    val snappedMs = (rawMs / 50f).roundToInt() * 50L
                    onSetAudioDelayMs(snappedMs)
                },
                valueRange = -5000f..5000f,
                modifier = Modifier.testTag("audio_delay_slider")
            )

            FilledTonalButton(
                onClick = onResyncAudio,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audio_resync_button")
            ) {
                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    tr("Синхронизировать аудиопоток с видео", "Resynchronize Audio & Video Streams"),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            if (lastAudioResyncTimestampMs > 0L) {
                Text(
                    text = tr(
                        "✓ Аудио- и видеопоток пересинхронизированы по текущему таймкоду (сдвиг: ${AudioDelayProcessor.formatDelayLabel(effectiveAudioDelayMs)})",
                        "✓ Audio & video streams resynchronized to current position (offset: ${AudioDelayProcessor.formatDelayLabel(effectiveAudioDelayMs)})"
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("audio_resync_status_text")
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            AudioBoostControlSection(
                audioBoostPercent = audioBoostPercent,
                maxAudioBoostPercent = maxAudioBoostPercent,
                onSetAudioBoostPercent = onSetAudioBoostPercent,
                onUpdateMaxAudioBoostPercent = onUpdateMaxAudioBoostPercent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SubtitleControlModalSheet(
    subtitleOptions: List<SubtitleTrackUiOption>,
    subtitleDelayMs: Long,
    settings: PlayerSettings,
    onSelectSubtitle: (SubtitleTrackUiOption) -> Unit,
    onLoadExternalSubtitleFile: () -> Unit,
    onAdjustDelayMs: (Long) -> Unit,
    onSetDelayMs: (Long) -> Unit,
    onSetFontSizeSp: (Float) -> Unit,
    onSetColorArgb: (Long) -> Unit,
    onSetBackgroundEnabled: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Subtitles,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(tr("Субтитры (SRT / ASS)", "Subtitles (SRT / ASS)"), style = MaterialTheme.typography.titleMedium)
            }

            Button(
                onClick = onLoadExternalSubtitleFile,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("player_load_subtitle_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    tr("Загрузить внешние субтитры (.SRT / .ASS)", "Load External Subtitles (.SRT / .ASS)"),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // Track Selection
            Text(tr("Активная дорожка субтитров", "Active Subtitle Track"), style = MaterialTheme.typography.bodyMedium)
            subtitleOptions.forEachIndexed { idx, opt ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (opt.isSelected) {
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
                        } else {
                            PlayerPopupCardColor
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectSubtitle(opt) }
                        .testTag("subtitle_track_option_$idx")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = opt.isSelected,
                            onClick = { onSelectSubtitle(opt) }
                        )
                        Text(
                            text = if (isEnglish && opt.label == "Выключены") "Off" else opt.label,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Surface(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = opt.badge,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Subtitle Delay Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tr("Задержка субтитров", "Subtitle Delay"), style = MaterialTheme.typography.bodyMedium)
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = formatDelaySeconds(subtitleDelayMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .testTag("subtitle_delay_value_text")
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = { onAdjustDelayMs(-500L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("subtitle_delay_minus_500")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("-0.5с", "-0.5s"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                OutlinedButton(
                    onClick = { onAdjustDelayMs(-100L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("subtitle_delay_minus_100")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("-0.1с", "-0.1s"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                FilledTonalButton(
                    onClick = { onSetDelayMs(0L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("subtitle_delay_reset")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("0.0с", "0.0s"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                OutlinedButton(
                    onClick = { onAdjustDelayMs(100L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("subtitle_delay_plus_100")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("+0.1с", "+0.1s"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }

                OutlinedButton(
                    onClick = { onAdjustDelayMs(500L) },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("subtitle_delay_plus_500")
                ) {
                    ScaleToFitWidthBox {
                        Text(tr("+0.5с", "+0.5s"), style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }
            }

            InstantSlider(
                value = (subtitleDelayMs / 1000f).coerceIn(-10f, 10f),
                onValueChange = { sec ->
                    val snappedMs = (sec * 10f).roundToInt() * 100L
                    onSetDelayMs(snappedMs)
                },
                valueRange = -10f..10f,
                modifier = Modifier.testTag("subtitle_delay_slider")
            )

            HorizontalDivider()

            // Subtitle Font Size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tr("Размер шрифта", "Font Size"), style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${settings.subtitleFontSizeSp.roundToInt()} sp",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            InstantSlider(
                value = settings.subtitleFontSizeSp,
                onValueChange = onSetFontSizeSp,
                valueRange = 14f..40f,
                modifier = Modifier.testTag("player_subtitle_font_size_slider")
            )

            // Subtitle Color Swatches
            Text(tr("Цвет субтитров", "Subtitle Color"), style = MaterialTheme.typography.bodyMedium)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubtitleColorPreset.entries.forEach { preset ->
                    val selected = settings.subtitleColorArgb == preset.argb
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(preset.argb))
                            .border(
                                width = if (selected) 3.dp else 1.dp,
                                color = if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.35f),
                                shape = CircleShape
                            )
                            .clickable { onSetColorArgb(preset.argb) }
                            .testTag("player_subtitle_color_${preset.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = preset.localizedLabel(isEnglish),
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(tr("Тёмная подложка под текстом", "Dark Background Box"), style = MaterialTheme.typography.bodySmall)
                Switch(
                    checked = settings.subtitleBackgroundEnabled,
                    onCheckedChange = onSetBackgroundEnabled
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Anime4KControlModalSheet(
    settings: PlayerSettings,
    splitCompareEnabled: Boolean,
    splitPosition: Float,
    onToggleEnabled: (Boolean) -> Unit,
    onSelectPreset: (Anime4KPreset) -> Unit,
    onChangeStrength: (Float) -> Unit,
    onToggleSplitCompare: (Boolean) -> Unit,
    onChangeSplitPosition: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    var showAnime4kInfoModal by rememberSaveable { mutableStateOf(false) }
    if (showAnime4kInfoModal) {
        ModeInfoModalDialog(
            topic = ModeInfoTopic.ANIME4K,
            onDismiss = { showAnime4kInfoModal = false }
        )
    }
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    tr("Anime4K — Реалтайм апскейлинг", "Anime4K — Real-Time Upscaling"),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                ModeInfoIconButton(
                    topic = ModeInfoTopic.ANIME4K,
                    onClick = { showAnime4kInfoModal = true }
                )
            }

            if (settings.isAnime4kAutoDowngradedForMemc) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.72f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("anime4k_memc_auto_fast_banner")
                ) {
                    Text(
                        text = tr(
                            "⚡ Авто-оптимизация GPU: при одновременной работе с интерполяцией кадров (MEMC) Anime4K автоматически переключён в быстрый однопроходный режим (Anime4K_Fast) для стабильного FPS без рывков.",
                            "⚡ Auto GPU Optimization: while Frame Interpolation (MEMC) is active, Anime4K automatically runs in fast single-pass mode (Anime4K_Fast) to prevent frame drops."
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PlayerPopupCardColor
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tr("Активировать Anime4K", "Enable Anime4K"), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            tr(
                                "${settings.graphicsBackend.badge} шейдер реконструкции линий",
                                "${settings.graphicsBackend.badge} line reconstruction shader"
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = settings.anime4kEnabled,
                        onCheckedChange = onToggleEnabled,
                        modifier = Modifier.testTag("player_anime4k_enable_switch")
                    )
                }
            }

            Text(tr("Пресеты качества", "Quality Presets"), style = MaterialTheme.typography.bodyMedium)
            Anime4KPreset.entries.forEach { preset ->
                val isSelected = settings.effectiveAnime4KPreset == preset ||
                    (!settings.anime4kEnabled && preset == Anime4KPreset.OFF)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        } else {
                            PlayerPopupCardColor
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (preset == Anime4KPreset.OFF) {
                                onToggleEnabled(false)
                            } else {
                                onSelectPreset(preset)
                            }
                        }
                        .testTag("anime4k_preset_${preset.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                if (preset == Anime4KPreset.OFF) {
                                    onToggleEnabled(false)
                                } else {
                                    onSelectPreset(preset)
                                }
                            }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(preset.localizedTitle(isEnglish), style = MaterialTheme.typography.bodyMedium)
                            Text(
                                preset.localizedDescription(isEnglish),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = preset.badgeLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(tr("Интенсивность реконструкции", "Reconstruction Strength"), style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${(settings.anime4kStrength * 100).roundToInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            InstantSlider(
                value = settings.anime4kStrength,
                onValueChange = onChangeStrength,
                enabled = settings.anime4kEnabled,
                valueRange = 0.25f..1.5f,
                modifier = Modifier.testTag("anime4k_strength_slider")
            )

            // Split-Screen Live Comparison
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Compare, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                    Column {
                        Text(tr("Сравнение До / После", "Before / After Comparison"), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            tr(
                                "Слева оригинал, справа улучшение Anime4K",
                                "Original on the left, Anime4K on the right"
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(
                    checked = splitCompareEnabled,
                    onCheckedChange = onToggleSplitCompare,
                    enabled = settings.anime4kEnabled,
                    modifier = Modifier.testTag("anime4k_split_compare_switch")
                )
            }

            if (splitCompareEnabled && settings.anime4kEnabled) {
                InstantSlider(
                    value = splitPosition,
                    onValueChange = onChangeSplitPosition,
                    valueRange = 0.1f..0.9f,
                    modifier = Modifier.testTag("anime4k_split_slider")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DecoderAndVideoModalSheet(
    activeDecoderMode: DecoderMode,
    activeCodecName: String,
    isHardwareAccelerated: Boolean,
    videoWidth: Int,
    videoHeight: Int,
    onSelectDecoderMode: (DecoderMode) -> Unit,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    var showDecoderInfoModal by rememberSaveable { mutableStateOf(false) }
    if (showDecoderInfoModal) {
        ModeInfoModalDialog(
            topic = ModeInfoTopic.DECODER,
            onDismiss = { showDecoderInfoModal = false }
        )
    }
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Memory,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    tr("Аппаратный декодер и видео", "Hardware Decoder & Video"),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                ModeInfoIconButton(
                    topic = ModeInfoTopic.DECODER,
                    onClick = { showDecoderInfoModal = true }
                )
            }

            // Live Codec Telemetry Box
            Surface(
                color = PlayerPopupCardColor,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = tr("Кодек: $activeCodecName", "Codec: $activeCodecName"),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isEnglish) {
                            "Stream: ${videoWidth}×${videoHeight} • Acceleration: ${if (isHardwareAccelerated) "Hardware (MediaCodec GPU)" else "Software (CPU)"}"
                        } else {
                            "Поток: ${videoWidth}×${videoHeight} • Ускорение: ${if (isHardwareAccelerated) "Аппаратное (MediaCodec GPU)" else "Программное (CPU)"}"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(tr("Режим декодера", "Decoder Mode"), style = MaterialTheme.typography.bodyMedium)
            DecoderMode.entries.forEach { mode ->
                val selected = activeDecoderMode == mode
                Surface(
                    color = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                    } else {
                        PlayerPopupCardColor
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectDecoderMode(mode) }
                        .testTag("decoder_mode_chip_${mode.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = selected,
                            onClick = { onSelectDecoderMode(mode) }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(mode.localizedTitle(isEnglish), style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = mode.localizedSubtitle(isEnglish),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = mode.shortBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoQualityModalSheet(
    qualityOptions: List<VideoQualityUiOption>,
    videoWidth: Int,
    videoHeight: Int,
    videoBitrateBps: Long,
    activeCodecName: String,
    onSelectQuality: (VideoQualityUiOption) -> Unit,
    onDismiss: () -> Unit
) {
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("video_quality_modal_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        tr("Качество видео и потока", "Stream & Video Quality"),
                        style = MaterialTheme.typography.titleMedium
                    )
                    val bitrateStr = if (videoBitrateBps > 0L) {
                        String.format(java.util.Locale.US, " • %.1f Mbps", videoBitrateBps / 1_000_000f)
                    } else {
                        ""
                    }
                    Text(
                        text = tr(
                            "Текущий поток: ${videoWidth}×${videoHeight} • $activeCodecName$bitrateStr",
                            "Active stream: ${videoWidth}×${videoHeight} • $activeCodecName$bitrateStr"
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (qualityOptions.isEmpty()) {
                Surface(
                    color = PlayerPopupCardColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = tr(
                            "Воспроизводится единственная доступная видеодорожка (${videoWidth}×${videoHeight}).",
                            "Playing the single available video stream (${videoWidth}×${videoHeight})."
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                qualityOptions.forEachIndexed { idx, opt ->
                    Surface(
                        color = if (opt.isSelected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        } else {
                            PlayerPopupCardColor
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (opt.isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectQuality(opt)
                                onDismiss()
                            }
                            .testTag("video_quality_option_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = opt.isSelected,
                                onClick = {
                                    onSelectQuality(opt)
                                    onDismiss()
                                }
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = opt.label,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (opt.width > 0 && opt.height > 0) {
                                    val kbps = (opt.bitrateBps / 1000L).toInt()
                                    val kbpsTxt = if (kbps > 0) " • $kbps kbps" else ""
                                    Text(
                                        text = "${opt.width}×${opt.height}$kbpsTxt",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = opt.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
