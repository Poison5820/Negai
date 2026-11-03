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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.PlaybackEndMode
import com.example.ui.components.InstantSlider
import com.example.model.PlayerControlsTheme
import com.example.model.PlayerOrientationMode
import com.example.model.SleepTimerPreset
import com.example.model.VideoScaleMode
import com.example.ui.components.PlayerControlsThemeSelectorSection
import com.example.ui.components.formatTimeMs
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.localizedBadge
import com.example.ui.i18n.localizedShortLabel
import com.example.ui.i18n.localizedSubtitle
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.tr
import com.example.ui.theme.ensureReadableOn
import kotlin.math.roundToInt

enum class VerticalGestureMode {
    NONE,
    BRIGHTNESS,
    VOLUME
}

enum class GestureHudType {
    BRIGHTNESS,
    VOLUME
}

data class GestureHudState(
    val type: GestureHudType,
    val percent: Int
)

@Composable
fun VerticalGestureHudCard(
    mode: VerticalGestureMode,
    fraction: Float,
    displayPercent: Int? = null,
    modifier: Modifier = Modifier
) {
    val type = if (mode == VerticalGestureMode.BRIGHTNESS) GestureHudType.BRIGHTNESS else GestureHudType.VOLUME
    val pct = displayPercent ?: (fraction.coerceIn(0f, 1f) * 100f).roundToInt()
    VerticalGestureHudCard(
        hudState = GestureHudState(type = type, percent = pct),
        modifier = modifier
    )
}

@Composable
fun VerticalGestureHudCard(
    hudState: GestureHudState,
    modifier: Modifier = Modifier
) {
    val glassBg = PlayerFrostedGlassBaseColor
    val readableAccent = MaterialTheme.colorScheme.primary.ensureReadableOn(glassBg, minContrast = 5.4f)
    val isBoostedVolume = hudState.type == GestureHudType.VOLUME && hudState.percent > 100
    Surface(
        color = glassBg,
        shape = RoundedCornerShape(16.dp),
        border = playerPopupBorderStroke(),
        shadowElevation = 14.dp,
        modifier = modifier.testTag("vertical_gesture_hud")
    ) {
        Column(
            modifier = Modifier
                .width(152.dp)
                .background(playerFrostedGlassBrush(accentAlpha = if (isBoostedVolume) 0.20f else 0.12f))
                .padding(horizontal = 12.dp, vertical = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = when {
                    hudState.type == GestureHudType.BRIGHTNESS -> Icons.Default.BrightnessHigh
                    isBoostedVolume -> Icons.Default.GraphicEq
                    else -> Icons.Default.VolumeUp
                },
                contentDescription = null,
                tint = readableAccent,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = when {
                    hudState.type == GestureHudType.BRIGHTNESS ->
                        tr("Яркость ${hudState.percent}%", "Brightness ${hudState.percent}%")
                    isBoostedVolume ->
                        tr("Усиление ${hudState.percent}%", "Volume Boost ${hudState.percent}%")
                    else ->
                        tr("Громкость ${hudState.percent}%", "Volume ${hudState.percent}%")
                },
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isBoostedVolume) readableAccent else Color.White,
                maxLines = 1,
                softWrap = false
            )
            LinearProgressIndicator(
                progress = {
                    if (isBoostedVolume) {
                        ((hudState.percent - 100f) / 100f).coerceIn(0f, 1f)
                    } else {
                        (hudState.percent / 100f).coerceIn(0f, 1f)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = readableAccent,
                trackColor = Color.White.copy(alpha = 0.22f)
            )
        }
    }
}

@Composable
fun LongPressSpeedPill(
    speed: Float,
    modifier: Modifier = Modifier
) {
    val glassBg = PlayerFrostedGlassBaseColor
    val readableAccent = MaterialTheme.colorScheme.primary.ensureReadableOn(glassBg, minContrast = 5.4f)
    Surface(
        color = glassBg,
        shape = RoundedCornerShape(50),
        border = playerPopupBorderStroke(),
        shadowElevation = 12.dp,
        modifier = modifier.testTag("long_press_speed_pill")
    ) {
        Row(
            modifier = Modifier
                .background(playerFrostedGlassBrush(accentAlpha = 0.14f))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FastForward,
                contentDescription = null,
                tint = readableAccent,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = tr("Ускорение ${speed}x", "Speed Boost ${speed}x"),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

@Composable
fun ScreenLockFloatingPill(
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassBg = PlayerFrostedGlassBaseColor
    val readableAccent = MaterialTheme.colorScheme.primary.ensureReadableOn(glassBg, minContrast = 5.4f)
    Surface(
        color = glassBg,
        shape = RoundedCornerShape(50),
        border = playerPopupBorderStroke(),
        shadowElevation = 14.dp,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onUnlock)
            .testTag("unlock_screen_pill")
    ) {
        Row(
            modifier = Modifier
                .background(playerFrostedGlassBrush(accentAlpha = 0.14f))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(readableAccent.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = tr("Разблокировать экран", "Unlock screen"),
                    tint = readableAccent,
                    modifier = Modifier.size(15.dp)
                )
            }
            Column {
                Text(
                    text = tr("Экран заблокирован", "Screen Locked"),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
                Text(
                    text = tr("Нажмите для разблокировки", "Tap to unlock controls"),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.78f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlayerToolsModalSheet(
    playbackSpeed: Float,
    scaleMode: VideoScaleMode,
    audioBoostPercent: Int,
    maxAudioBoostPercent: Int = 200,
    sleepTimerPreset: SleepTimerPreset,
    sleepTimerRemainingMs: Long,
    abLoopPointAMs: Long?,
    abLoopPointBMs: Long?,
    currentPositionMs: Long,
    playbackEndMode: PlaybackEndMode,
    controlsTheme: PlayerControlsTheme = PlayerControlsTheme.CLASSIC_OLED,
    activeOrientationMode: PlayerOrientationMode = PlayerOrientationMode.VIDEO_ASPECT,
    playerUiScalePercent: Int = 100,
    playerAutoDpiScale: Boolean = true,
    controlsAutoHideSeconds: Int = 4,
    skipIntroSeconds: Int = 85,
    onSelectSpeed: (Float) -> Unit,
    onSelectScaleMode: (VideoScaleMode) -> Unit,
    onSetAudioBoostPercent: (Int) -> Unit,
    onUpdateMaxAudioBoostPercent: (Int) -> Unit = {},
    onSetSleepTimer: (SleepTimerPreset) -> Unit,
    onMarkAbPointA: () -> Unit,
    onMarkAbPointB: () -> Unit,
    onClearAbLoop: () -> Unit,
    onSelectPlaybackEndMode: (PlaybackEndMode) -> Unit,
    onSelectControlsTheme: (PlayerControlsTheme) -> Unit = {},
    onSelectOrientationMode: (PlayerOrientationMode) -> Unit = {},
    onUpdatePlayerUiScalePercent: (Int) -> Unit = {},
    onUpdatePlayerAutoDpiScale: (Boolean) -> Unit = {},
    onUpdateControlsAutoHideSeconds: (Int) -> Unit = {},
    onUpdateSkipIntroSeconds: (Int) -> Unit = {},
    onLockControls: () -> Unit,
    onOpenQuickActionsCustomizer: () -> Unit = {},
    onOpenFileTechnicalInfo: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f, 2.5f, 3.0f)

    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = tr("Инструменты просмотра", "Playback Tools"),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false)
                )
                ScaleToFitWidthBox(
                    modifier = Modifier.weight(1f, fill = false),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (onOpenFileTechnicalInfo != null) {
                            FilledTonalButton(
                                onClick = {
                                    onDismiss()
                                    onOpenFileTechnicalInfo()
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("sheet_open_file_info_button")
                            ) {
                                Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(tr("Инфо", "Info"), maxLines = 1, softWrap = false)
                            }
                        }
                        FilledTonalButton(
                            onClick = {
                                onDismiss()
                                onOpenQuickActionsCustomizer()
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("sheet_open_quick_actions_customizer")
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(tr("Кнопки", "Buttons"), maxLines = 1, softWrap = false)
                        }
                        FilledTonalButton(
                            onClick = {
                                onLockControls()
                                onDismiss()
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("sheet_lock_controls_button")
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(tr("Блок", "Lock"), maxLines = 1, softWrap = false)
                        }
                    }
                }
            }

            // 0. Player Controls & Progress Bar Theme
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = tr(
                            "Тема кнопок и полосы (${controlsTheme.localizedTitle(isEnglish)})",
                            "Controls & Timeline Theme (${controlsTheme.localizedTitle(isEnglish)})"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                PlayerControlsThemeSelectorSection(
                    selectedTheme = controlsTheme,
                    onSelectTheme = onSelectControlsTheme,
                    compactChipsOnly = true
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 0a. Global App & Player UI Scale (75% - 140% + Auto DPI) & HUD Auto-Hide Timeout
            var pendingPlayerScale by remember {
                mutableIntStateOf(playerUiScalePercent.coerceIn(75, 140))
            }
            var isDraggingPlayerScale by remember { mutableStateOf(false) }
            LaunchedEffect(playerUiScalePercent) {
                if (!isDraggingPlayerScale) {
                    pendingPlayerScale = playerUiScalePercent.coerceIn(75, 140)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = tr(
                            "Масштаб интерфейса ($pendingPlayerScale%)",
                            "UI & Text Scale ($pendingPlayerScale%)"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                    FilledTonalButton(
                        onClick = {
                            isDraggingPlayerScale = false
                            pendingPlayerScale = 100
                            onUpdatePlayerUiScalePercent(100)
                        },
                        enabled = playerUiScalePercent != 100 || pendingPlayerScale != 100,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("tools_reset_ui_scale_btn")
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("100%", style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false)
                    }
                }
                Slider(
                    value = pendingPlayerScale.toFloat(),
                    onValueChange = {
                        isDraggingPlayerScale = true
                        pendingPlayerScale = it.roundToInt().coerceIn(75, 140)
                    },
                    onValueChangeFinished = {
                        isDraggingPlayerScale = false
                        val target = pendingPlayerScale.coerceIn(75, 140)
                        if (target != playerUiScalePercent) {
                            onUpdatePlayerUiScalePercent(target)
                        }
                    },
                    valueRange = 75f..140f,
                    modifier = Modifier.testTag("tools_ui_scale_slider")
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(80, 90, 100, 110, 125, 140).forEach { pct ->
                        FilterChip(
                            selected = pendingPlayerScale == pct,
                            onClick = {
                                isDraggingPlayerScale = false
                                pendingPlayerScale = pct
                                onUpdatePlayerUiScalePercent(pct)
                            },
                            label = { Text("$pct%") },
                            modifier = Modifier.testTag("tools_ui_scale_chip_$pct")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tr(
                        "Время скрытия кнопок плеера: $controlsAutoHideSeconds сек",
                        "HUD Controls Auto-Hide: ${controlsAutoHideSeconds}s"
                    ),
                    style = MaterialTheme.typography.titleSmall
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(2, 3, 4, 5, 7, 10, 15).forEach { sec ->
                        FilterChip(
                            selected = controlsAutoHideSeconds == sec,
                            onClick = { onUpdateControlsAutoHideSeconds(sec) },
                            label = { Text(tr("$sec с", "${sec}s")) },
                            modifier = Modifier.testTag("tools_autohide_chip_$sec")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 0b. Screen Orientation Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.ScreenRotation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = tr(
                            "Поворот экрана (${activeOrientationMode.localizedTitle(isEnglish)})",
                            "Screen Rotation (${activeOrientationMode.localizedTitle(isEnglish)})"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PlayerOrientationMode.entries.forEach { mode ->
                        FilterChip(
                            selected = activeOrientationMode == mode,
                            onClick = { onSelectOrientationMode(mode) },
                            label = { Text(mode.localizedBadge(isEnglish)) },
                            modifier = Modifier.testTag("tools_orientation_chip_${mode.id}")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 1. Playback Speed
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = tr(
                            "Скорость воспроизведения (${playbackSpeed}x)",
                            "Playback Speed (${playbackSpeed}x)"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    speeds.forEach { spd ->
                        FilterChip(
                            selected = kotlin.math.abs(playbackSpeed - spd) < 0.02f,
                            onClick = { onSelectSpeed(spd) },
                            label = { Text("${spd}x") },
                            modifier = Modifier.testTag("speed_chip_${spd}x")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 2. Video Scale / Aspect Ratio
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AspectRatio, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = tr("Масштаб и соотношение сторон", "Aspect Ratio & Scale"),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VideoScaleMode.entries.forEach { mode ->
                        FilterChip(
                            selected = scaleMode == mode,
                            onClick = { onSelectScaleMode(mode) },
                            label = { Text(mode.localizedTitle(isEnglish)) },
                            modifier = Modifier.testTag("scale_chip_${mode.name}")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 3. Software Audio Boost (100% - maxAudioBoostPercent, active only at 100% system volume)
            AudioBoostControlSection(
                audioBoostPercent = audioBoostPercent,
                maxAudioBoostPercent = maxAudioBoostPercent,
                onSetAudioBoostPercent = onSetAudioBoostPercent,
                onUpdateMaxAudioBoostPercent = onUpdateMaxAudioBoostPercent
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 4. A-B Loop Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Repeat, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = tr(
                            "Повтор отрезка A–B (сейчас ${formatTimeMs(currentPositionMs)})",
                            "A–B Segment Loop (now ${formatTimeMs(currentPositionMs)})"
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onMarkAbPointA,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ab_mark_a_button")
                    ) {
                        ScaleToFitWidthBox {
                            Text(
                                text = if (abLoopPointAMs != null) {
                                    "A: ${formatTimeMs(abLoopPointAMs)}"
                                } else {
                                    tr("Точка A", "Point A")
                                },
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                    FilledTonalButton(
                        onClick = onMarkAbPointB,
                        enabled = abLoopPointAMs != null,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ab_mark_b_button")
                    ) {
                        ScaleToFitWidthBox {
                            Text(
                                text = if (abLoopPointBMs != null) {
                                    "B: ${formatTimeMs(abLoopPointBMs)}"
                                } else {
                                    tr("Точка B", "Point B")
                                },
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                    if (abLoopPointAMs != null || abLoopPointBMs != null) {
                        OutlinedButton(
                            onClick = onClearAbLoop,
                            modifier = Modifier.testTag("ab_clear_button")
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = tr("Сбросить A-B", "Clear A-B loop"),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 5. Sleep Timer
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Bedtime, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    val timerTitle = if (sleepTimerPreset == SleepTimerPreset.OFF) {
                        tr("Таймер сна", "Sleep Timer")
                    } else if (sleepTimerPreset == SleepTimerPreset.END_OF_EPISODE) {
                        tr("Таймер сна: До конца серии", "Sleep Timer: Until end of episode")
                    } else {
                        tr(
                            "Таймер сна: осталось ${formatTimeMs(sleepTimerRemainingMs)}",
                            "Sleep Timer: ${formatTimeMs(sleepTimerRemainingMs)} left"
                        )
                    }
                    Text(timerTitle, style = MaterialTheme.typography.titleSmall)
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SleepTimerPreset.entries.forEach { preset ->
                        FilterChip(
                            selected = sleepTimerPreset == preset,
                            onClick = { onSetSleepTimer(preset) },
                            label = { Text(preset.localizedBadge(isEnglish)) },
                            modifier = Modifier.testTag("sleep_timer_chip_${preset.id}")
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 6. Playback End Mode
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = tr("Режим перехода между сериями", "Episode Transition Mode"),
                    style = MaterialTheme.typography.titleSmall
                )
                PlaybackEndMode.entries.forEach { mode ->
                    val selected = playbackEndMode == mode
                    Surface(
                        color = if (selected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectPlaybackEndMode(mode) }
                            .testTag("sheet_end_mode_${mode.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(mode.localizedTitle(isEnglish), style = MaterialTheme.typography.titleSmall)
                                Text(
                                    mode.localizedSubtitle(isEnglish),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            RadioButton(
                                selected = selected,
                                onClick = { onSelectPlaybackEndMode(mode) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AudioBoostControlSection(
    audioBoostPercent: Int,
    maxAudioBoostPercent: Int = 200,
    onSetAudioBoostPercent: (Int) -> Unit,
    onUpdateMaxAudioBoostPercent: (Int) -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val audioManager = remember(context) {
        context.getSystemService(android.content.Context.AUDIO_SERVICE) as? android.media.AudioManager
    }
    val maxSysVol = remember(audioManager) {
        (audioManager?.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC) ?: 15).coerceAtLeast(1)
    }
    var curSysVol by remember(audioManager, audioBoostPercent) {
        mutableIntStateOf(
            (audioManager?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: maxSysVol)
                .coerceIn(0, maxSysVol)
        )
    }
    val stepPercent = remember(maxSysVol) {
        (100f / maxSysVol.toFloat()).roundToInt().coerceIn(5, 15)
    }
    val ceiling = maxAudioBoostPercent.coerceIn(150, 300)
    val effectiveBoost = if (curSysVol < maxSysVol) 100 else audioBoostPercent.coerceIn(100, ceiling)
    val combinedVolumePercent = if (curSysVol < maxSysVol) {
        ((curSysVol.toFloat() / maxSysVol.toFloat()) * 100f).roundToInt()
    } else {
        effectiveBoost
    }

    val applyBoostWithSystemMax: (Int) -> Unit = { targetPct ->
        val clamped = targetPct.coerceIn(100, ceiling)
        if (clamped > 100 && curSysVol < maxSysVol) {
            runCatching {
                audioManager?.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, maxSysVol, 0)
            }
            curSysVol = maxSysVol
        }
        onSetAudioBoostPercent(clamped)
    }

    val stepVolumeOrBoostDown: () -> Unit = {
        val liveVol = (audioManager?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: curSysVol)
            .coerceIn(0, maxSysVol)
        curSysVol = liveVol
        if (effectiveBoost > 100) {
            val nextBoost = (effectiveBoost - stepPercent).coerceAtLeast(100)
            onSetAudioBoostPercent(nextBoost)
        } else if (liveVol > 0) {
            val nextVol = (liveVol - 1).coerceAtLeast(0)
            runCatching {
                audioManager?.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, nextVol, 0)
            }
            curSysVol = nextVol
            onSetAudioBoostPercent(100)
        }
    }

    val stepVolumeOrBoostUp: () -> Unit = {
        val liveVol = (audioManager?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: curSysVol)
            .coerceIn(0, maxSysVol)
        curSysVol = liveVol
        if (liveVol < maxSysVol) {
            val nextVol = (liveVol + 1).coerceAtMost(maxSysVol)
            runCatching {
                audioManager?.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, nextVol, 0)
            }
            curSysVol = nextVol
            onSetAudioBoostPercent(100)
        } else {
            val nextBoost = (effectiveBoost + stepPercent).coerceAtMost(ceiling)
            onSetAudioBoostPercent(nextBoost)
        }
    }

    val boostPresets = remember(ceiling) {
        buildList {
            add(100)
            add(125)
            add(150)
            if (ceiling >= 175) add(175)
            if (ceiling >= 200) add(200)
            if (ceiling >= 250) add(250)
            if (ceiling >= 300) add(300)
        }.distinct()
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Column {
                    Text(
                        text = tr("Усиление громкости (свыше 100%)", "Volume Booster (above 100%)"),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = if (curSysVol < maxSysVol) {
                            tr(
                                "Системная громкость: $combinedVolumePercent% ($curSysVol/$maxSysVol) • Бустер включится на 100%",
                                "System volume: $combinedVolumePercent% ($curSysVol/$maxSysVol) • Boost activates at 100%"
                            )
                        } else {
                            tr(
                                "Система 100% + Бустер: $effectiveBoost% (шаг +$stepPercent%)",
                                "System 100% + Booster: $effectiveBoost% (step +$stepPercent%)"
                            )
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = "$combinedVolumePercent%",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (effectiveBoost > 100) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = stepVolumeOrBoostDown,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("player_boost_step_down")
            ) {
                ScaleToFitWidthBox {
                    Text(
                        text = tr("− Шаг ($stepPercent%)", "− Step ($stepPercent%)"),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
            FilledTonalButton(
                onClick = { applyBoostWithSystemMax(100) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("player_boost_reset_100")
            ) {
                ScaleToFitWidthBox {
                    Text(
                        text = tr("100% (Норма)", "100% (Normal)"),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
            OutlinedButton(
                onClick = stepVolumeOrBoostUp,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("player_boost_step_up")
            ) {
                ScaleToFitWidthBox {
                    Text(
                        text = tr("+ Шаг ($stepPercent%)", "+ Step ($stepPercent%)"),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        InstantSlider(
            value = effectiveBoost.toFloat(),
            onValueChange = { applyBoostWithSystemMax(it.roundToInt()) },
            valueRange = 100f..ceiling.toFloat(),
            modifier = Modifier.testTag("player_audio_boost_slider")
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            boostPresets.forEach { pct ->
                FilterChip(
                    selected = effectiveBoost == pct,
                    onClick = { applyBoostWithSystemMax(pct) },
                    label = { Text(if (pct == 100) tr("100% (Норма)", "100% (Normal)") else "$pct%") },
                    modifier = Modifier.testTag("player_boost_chip_$pct")
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = tr("Лимит усиления:", "Max boost limit:"),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(150, 200, 250, 300).forEach { maxPct ->
                    FilterChip(
                        selected = ceiling == maxPct,
                        onClick = {
                            onUpdateMaxAudioBoostPercent(maxPct)
                            if (audioBoostPercent > maxPct) {
                                onSetAudioBoostPercent(maxPct)
                            }
                        },
                        label = { Text("$maxPct%", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("player_max_boost_limit_$maxPct")
                    )
                }
            }
        }
    }
}
