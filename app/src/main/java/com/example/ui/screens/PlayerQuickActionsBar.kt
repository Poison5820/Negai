package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.model.Anime4KPreset
import com.example.model.AudioDeArtifactMode
import com.example.model.DecoderMode
import com.example.model.EpisodeItem
import com.example.model.FrameInterpolationMode
import com.example.model.PlayerOrientationMode
import com.example.model.PlayerQuickAction
import com.example.model.PlayerSettings
import com.example.model.SleepTimerPreset
import com.example.model.VideoScaleMode
import com.example.player.AudioTrackUiOption
import com.example.player.PlaybackRuntimeState
import com.example.player.SubtitleTrackUiOption
import com.example.ui.components.InstantSlider
import com.example.ui.components.ResolutionBadgeChip
import com.example.ui.components.formatTimeMs
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.localizedBadge
import com.example.ui.i18n.localizedShortLabel
import com.example.ui.i18n.localizedShortTitle
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.tr
import com.example.ui.theme.ensureReadableOn
import java.util.Locale
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.roundToInt

/**
 * Item inside a Hold-and-Swipe Mini Pop-up Menu above a bottom bar button.
 * The last item in every menu is the technical disable/reset option ("Откл." / "Off").
 */
internal data class QuickHoldMenuItem(
    val id: String,
    val label: String,
    val badge: String? = null,
    val isSelected: Boolean = false,
    val isOffOption: Boolean = false,
    val onSelect: () -> Unit
)

private val LEFT_ZONE_CANONICAL_ORDER = listOf(
    PlayerQuickAction.AUDIO,
    PlayerQuickAction.SUBTITLES,
    PlayerQuickAction.SPEED
)

private val CENTER_ZONE_CANONICAL_ORDER = listOf(
    PlayerQuickAction.FRAME_INTERPOLATION,
    PlayerQuickAction.ANIME4K
)

/**
 * 3-Zone bottom quick-actions bar in the video player HUD:
 * - Left zone: AUDIO, SUBTITLES, SPEED
 * - Center zone: [MEMC text <- MEMC icon] | [Anime4K icon -> Anime4K text]
 *   with the vertical divider strictly pinned at the 50% center of the timeline bar.
 * - Right zone: DECODER (visible by default) + all remaining enabled actions + customize button.
 *
 * Holding any button opens a frosted-glass vertical mini pop-up menu directly above that button.
 * Without lifting the finger, sliding onto an option highlights it and releasing immediately applies it.
 * At the bottom of every mini-menu is "Откл." ("Off").
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlayerQuickActionsBar(
    quickActions: List<PlayerQuickAction>,
    playbackState: PlaybackRuntimeState,
    settings: PlayerSettings,
    scaleMode: VideoScaleMode,
    orientationMode: PlayerOrientationMode,
    isOrientationLocked: Boolean = false,
    pipSupported: Boolean,
    onTriggerAction: (PlayerQuickAction) -> Unit,
    onLongPressAction: (PlayerQuickAction) -> Unit = {},
    onSelectAudio: (AudioTrackUiOption) -> Unit = {},
    onSelectAudioDeArtifactMode: (AudioDeArtifactMode) -> Unit = {},
    onSelectSubtitle: (SubtitleTrackUiOption) -> Unit = {},
    onSelectSpeed: (Float) -> Unit = {},
    onSelectFrameInterpolationMode: (FrameInterpolationMode) -> Unit = {},
    onToggleFrameInterpolation: (Boolean) -> Unit = {},
    onSelectAnime4KPreset: (Anime4KPreset) -> Unit = {},
    onToggleAnime4KEnabled: (Boolean) -> Unit = {},
    onSelectDecoderMode: (DecoderMode) -> Unit = {},
    onSelectScaleMode: (VideoScaleMode) -> Unit = {},
    onSetSleepTimer: (SleepTimerPreset) -> Unit = {},
    onMarkAbPointA: () -> Unit = {},
    onMarkAbPointB: () -> Unit = {},
    onClearAbLoop: () -> Unit = {},
    onUpdateSkipIntroSeconds: (Int) -> Unit = {},
    onSeekByMs: (Long) -> Unit = {},
    onSelectOrientationMode: (PlayerOrientationMode) -> Unit = {},
    onToggleOrientationLock: () -> Unit = {},
    onOpenCustomizer: () -> Unit,
    onUserInteraction: () -> Unit = {},
    onScrollStateChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isEnglish = LocalIsEnglish.current
    val visibleActions = remember(quickActions, pipSupported) {
        val filtered = quickActions.filter { action ->
            action != PlayerQuickAction.CHAPTERS &&
                action != PlayerQuickAction.AUDIO_BOOST &&
                (action != PlayerQuickAction.PIP || pipSupported)
        }
        // Guarantee DECODER is shown by default in the right zone unless explicitly customized out
        if (filtered.isEmpty()) {
            PlayerQuickAction.defaultActions
        } else {
            filtered
        }
    }

    val leftActions = remember(visibleActions) {
        LEFT_ZONE_CANONICAL_ORDER.filter { it in visibleActions }
    }
    val hasFrameInterpolation = remember(visibleActions) {
        PlayerQuickAction.FRAME_INTERPOLATION in visibleActions
    }
    val hasAnime4K = remember(visibleActions) {
        PlayerQuickAction.ANIME4K in visibleActions
    }
    val hasCenterActions = hasFrameInterpolation || hasAnime4K
    val rightActions = remember(visibleActions) {
        val rawRight = visibleActions.filter {
            it !in LEFT_ZONE_CANONICAL_ORDER && it !in CENTER_ZONE_CANONICAL_ORDER
        }
        // Put DECODER first in the right zone so it is always immediately visible
        val decoderFirst = rawRight.filter { it == PlayerQuickAction.DECODER } +
            rawRight.filterNot { it == PlayerQuickAction.DECODER }
        decoderFirst
    }

    var activeHoldAction by remember { mutableStateOf<PlayerQuickAction?>(null) }
    var hoveredMenuIndex by remember { mutableIntStateOf(-1) }

    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    LaunchedEffect(leftScrollState.isScrollInProgress, rightScrollState.isScrollInProgress) {
        val scrolling = leftScrollState.isScrollInProgress || rightScrollState.isScrollInProgress
        onScrollStateChanged(scrolling)
        if (scrolling) {
            onUserInteraction()
        }
    }

    val chipGap = if (settings.compactUiMode) 5.dp else 6.dp
    val buttonShape = RoundedCornerShape(12.dp)
    val accentFree = settings.playerControlsTheme.isAccentFree
    val frameless = settings.playerControlsTheme.isFrameless

    val buildMenuForAction: (PlayerQuickAction) -> List<QuickHoldMenuItem> = { action ->
        buildHoldMenuItemsForAction(
            action = action,
            isEnglish = isEnglish,
            playbackState = playbackState,
            settings = settings,
            scaleMode = scaleMode,
            orientationMode = orientationMode,
            onTriggerAction = onTriggerAction,
            onSelectAudio = onSelectAudio,
            onSelectAudioDeArtifactMode = onSelectAudioDeArtifactMode,
            onSelectSubtitle = onSelectSubtitle,
            onSelectSpeed = onSelectSpeed,
            onSelectFrameInterpolationMode = onSelectFrameInterpolationMode,
            onToggleFrameInterpolation = onToggleFrameInterpolation,
            onSelectAnime4KPreset = onSelectAnime4KPreset,
            onToggleAnime4KEnabled = onToggleAnime4KEnabled,
            onSelectDecoderMode = onSelectDecoderMode,
            onSelectScaleMode = onSelectScaleMode,
            onSetSleepTimer = onSetSleepTimer,
            onMarkAbPointA = onMarkAbPointA,
            onMarkAbPointB = onMarkAbPointB,
            onClearAbLoop = onClearAbLoop,
            onUpdateSkipIntroSeconds = onUpdateSkipIntroSeconds,
            onSeekByMs = onSeekByMs,
            onSelectOrientationMode = onSelectOrientationMode,
            isOrientationLocked = isOrientationLocked,
            onToggleOrientationLock = onToggleOrientationLock
        )
    }

    // Symmetric two-wing layout around the exact 50% center divider so the center buttons
    // (MEMC | Anime4K) never shift horizontally when left/right zones or badge texts change.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.changes.any { it.pressed || it.previousPressed }) {
                            onUserInteraction()
                        }
                    }
                }
            }
            .padding(horizontal = 2.dp)
            .testTag("player_quick_actions_row"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT WING (50% width): Left Zone (Audio, Subtitles, Speed) on the far left,
        // and MEMC (Frame Interpolation) pinned at the inner right edge next to the center divider.
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .horizontalScroll(leftScrollState, enabled = activeHoldAction == null)
                    .testTag("player_quick_actions_left_zone"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(chipGap, Alignment.Start)
            ) {
                leftActions.forEach { action ->
                    QuickActionHoldButton(
                        action = action,
                        playbackState = playbackState,
                        settings = settings,
                        scaleMode = scaleMode,
                        orientationMode = orientationMode,
                        isOrientationLocked = isOrientationLocked,
                        buttonShape = buttonShape,
                        textOnLeft = false,
                        isHoldMenuOpen = activeHoldAction == action,
                        hoveredMenuIndex = if (activeHoldAction == action) hoveredMenuIndex else -1,
                        buildMenuItems = { buildMenuForAction(action) },
                        onOpenHoldMenu = { initialIdx ->
                            onUserInteraction()
                            activeHoldAction = action
                            hoveredMenuIndex = initialIdx
                            onLongPressAction(action)
                        },
                        onUpdateHoveredIndex = { idx ->
                            onUserInteraction()
                            hoveredMenuIndex = idx
                        },
                        onDismissHoldMenu = {
                            activeHoldAction = null
                            hoveredMenuIndex = -1
                        },
                        onClick = {
                            onUserInteraction()
                            activeHoldAction = null
                            onTriggerAction(action)
                        }
                    )
                }
            }

            if (hasFrameInterpolation) {
                Spacer(modifier = Modifier.width(chipGap))
                // MEMC button pinned immediately left of the center divider;
                // its text badge slides out to the LEFT ([Text] [Icon] |)
                QuickActionHoldButton(
                    action = PlayerQuickAction.FRAME_INTERPOLATION,
                    playbackState = playbackState,
                    settings = settings,
                    scaleMode = scaleMode,
                    orientationMode = orientationMode,
                    isOrientationLocked = isOrientationLocked,
                    buttonShape = buttonShape,
                    textOnLeft = true,
                    isHoldMenuOpen = activeHoldAction == PlayerQuickAction.FRAME_INTERPOLATION,
                    hoveredMenuIndex = if (activeHoldAction == PlayerQuickAction.FRAME_INTERPOLATION) hoveredMenuIndex else -1,
                    buildMenuItems = { buildMenuForAction(PlayerQuickAction.FRAME_INTERPOLATION) },
                    onOpenHoldMenu = { initialIdx ->
                        onUserInteraction()
                        activeHoldAction = PlayerQuickAction.FRAME_INTERPOLATION
                        hoveredMenuIndex = initialIdx
                        onLongPressAction(PlayerQuickAction.FRAME_INTERPOLATION)
                    },
                    onUpdateHoveredIndex = { newIdx ->
                        onUserInteraction()
                        hoveredMenuIndex = newIdx
                    },
                    onDismissHoldMenu = {
                        activeHoldAction = null
                        hoveredMenuIndex = -1
                    },
                    onClick = {
                        onUserInteraction()
                        activeHoldAction = null
                        onTriggerAction(PlayerQuickAction.FRAME_INTERPOLATION)
                    }
                )
            }
        }

        // EXACT 50% CENTER DIVIDER (pinned to the middle of the timeline bar)
        if (hasCenterActions) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .testTag("player_quick_actions_center_zone"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (hasFrameInterpolation && hasAnime4K) {
                    Box(
                        modifier = Modifier
                            .width(1.2.dp)
                            .height(22.dp)
                            .background(Color.White.copy(alpha = 0.44f), RoundedCornerShape(1.dp))
                            .testTag("player_center_quick_actions_divider")
                    )
                }
            }
        }

        // RIGHT WING (50% width): Anime4K pinned at the inner left edge next to the center divider,
        // and Right Zone (Decoder + remaining actions + Customize) on the far right.
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (hasAnime4K) {
                // Anime4K button pinned immediately right of the center divider;
                // its text badge slides out to the RIGHT (| [Icon] [Text])
                QuickActionHoldButton(
                    action = PlayerQuickAction.ANIME4K,
                    playbackState = playbackState,
                    settings = settings,
                    scaleMode = scaleMode,
                    orientationMode = orientationMode,
                    isOrientationLocked = isOrientationLocked,
                    buttonShape = buttonShape,
                    textOnLeft = false,
                    isHoldMenuOpen = activeHoldAction == PlayerQuickAction.ANIME4K,
                    hoveredMenuIndex = if (activeHoldAction == PlayerQuickAction.ANIME4K) hoveredMenuIndex else -1,
                    buildMenuItems = { buildMenuForAction(PlayerQuickAction.ANIME4K) },
                    onOpenHoldMenu = { initialIdx ->
                        onUserInteraction()
                        activeHoldAction = PlayerQuickAction.ANIME4K
                        hoveredMenuIndex = initialIdx
                        onLongPressAction(PlayerQuickAction.ANIME4K)
                    },
                    onUpdateHoveredIndex = { newIdx ->
                        onUserInteraction()
                        hoveredMenuIndex = newIdx
                    },
                    onDismissHoldMenu = {
                        activeHoldAction = null
                        hoveredMenuIndex = -1
                    },
                    onClick = {
                        onUserInteraction()
                        activeHoldAction = null
                        onTriggerAction(PlayerQuickAction.ANIME4K)
                    }
                )
                Spacer(modifier = Modifier.width(chipGap))
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            Row(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .horizontalScroll(rightScrollState, enabled = activeHoldAction == null)
                    .testTag("player_quick_actions_right_zone"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(chipGap, Alignment.End)
            ) {
                rightActions.forEach { action ->
                    QuickActionHoldButton(
                        action = action,
                        playbackState = playbackState,
                        settings = settings,
                        scaleMode = scaleMode,
                        orientationMode = orientationMode,
                        isOrientationLocked = isOrientationLocked,
                        buttonShape = buttonShape,
                        textOnLeft = false,
                        isHoldMenuOpen = activeHoldAction == action,
                        hoveredMenuIndex = if (activeHoldAction == action) hoveredMenuIndex else -1,
                        buildMenuItems = { buildMenuForAction(action) },
                        onOpenHoldMenu = { initialIdx ->
                            onUserInteraction()
                            activeHoldAction = action
                            hoveredMenuIndex = initialIdx
                            onLongPressAction(action)
                        },
                        onUpdateHoveredIndex = { newIdx ->
                            onUserInteraction()
                            hoveredMenuIndex = newIdx
                        },
                        onDismissHoldMenu = {
                            activeHoldAction = null
                            hoveredMenuIndex = -1
                        },
                        onClick = {
                            onUserInteraction()
                            activeHoldAction = null
                            onTriggerAction(action)
                        }
                    )
                }

                val customizeDesc = tr("Настроить быстрые клавиши плеера", "Customize player quick actions")
                val readableAccent = MaterialTheme.colorScheme.primary.ensureReadableOn(
                    Color(0xFF0C1322),
                    minContrast = 5.2f
                )
                val customizeBorder = when {
                    frameless -> null
                    accentFree -> BorderStroke(0.85.dp, Color.White.copy(alpha = 0.28f))
                    else -> BorderStroke(0.85.dp, readableAccent.copy(alpha = 0.52f))
                }
                Surface(
                    color = if (frameless) Color.Transparent else Color(0x88101828),
                    shape = buttonShape,
                    border = customizeBorder,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 38.dp, minHeight = 36.dp)
                        .clip(buttonShape)
                        .clickable {
                            onUserInteraction()
                            onOpenCustomizer()
                        }
                        .semantics { contentDescription = customizeDesc }
                        .testTag("player_customize_quick_actions_button")
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = customizeDesc,
                            tint = if (accentFree) Color.White.copy(alpha = 0.90f) else readableAccent,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QuickActionHoldButton(
    action: PlayerQuickAction,
    playbackState: PlaybackRuntimeState,
    settings: PlayerSettings,
    scaleMode: VideoScaleMode,
    orientationMode: PlayerOrientationMode,
    isOrientationLocked: Boolean,
    buttonShape: RoundedCornerShape,
    textOnLeft: Boolean = false,
    isHoldMenuOpen: Boolean,
    hoveredMenuIndex: Int,
    buildMenuItems: () -> List<QuickHoldMenuItem>,
    onOpenHoldMenu: (Int) -> Unit,
    onUpdateHoveredIndex: (Int) -> Unit,
    onDismissHoldMenu: () -> Unit,
    onClick: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val stepPx = with(density) { 28.dp.toPx().coerceAtLeast(20f) }
    val dragThresholdPx = with(density) { 6.dp.toPx() }

    val chipState = resolveQuickChipVisuals(
        action = action,
        playbackState = playbackState,
        settings = settings,
        scaleMode = scaleMode,
        orientationMode = orientationMode,
        isOrientationLocked = isOrientationLocked
    )

    val currentBuildMenuItems by rememberUpdatedState(buildMenuItems)
    val currentOnOpenHoldMenu by rememberUpdatedState(onOpenHoldMenu)
    val currentOnUpdateHoveredIndex by rememberUpdatedState(onUpdateHoveredIndex)
    val currentOnDismissHoldMenu by rememberUpdatedState(onDismissHoldMenu)
    val currentOnClick by rememberUpdatedState(onClick)

    var buttonWindowPos by remember { mutableStateOf(Offset.Zero) }
    val itemBoundsInWindow = remember { mutableStateMapOf<Int, ClosedFloatingPointRange<Float>>() }

    Box(contentAlignment = Alignment.TopCenter) {
        if (isHoldMenuOpen) {
            val menuItems = currentBuildMenuItems()
            if (menuItems.isNotEmpty()) {
                val popupOffsetY = with(density) {
                    -((menuItems.size * 36 + 38).coerceAtMost(305)).dp.roundToPx()
                }
                Popup(
                    alignment = Alignment.TopCenter,
                    offset = IntOffset(0, popupOffsetY),
                    onDismissRequest = currentOnDismissHoldMenu,
                    properties = PopupProperties(
                        focusable = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                        clippingEnabled = true
                    )
                ) {
                    QuickActionHoldMiniPopupCard(
                        action = action,
                        title = action.localizedShortTitle(isEnglish),
                        items = menuItems,
                        hoveredIndex = hoveredMenuIndex,
                        onItemWindowBoundsChanged = { idx, range ->
                            itemBoundsInWindow[idx] = range
                        },
                        onSelectItem = { item ->
                            item.onSelect()
                            currentOnDismissHoldMenu()
                        }
                    )
                }
            }
        }

        Surface(
            color = chipState.containerColor,
            shape = buttonShape,
            border = chipState.border,
            modifier = Modifier
                .defaultMinSize(minWidth = 38.dp, minHeight = 36.dp)
                .clip(buttonShape)
                .onGloballyPositioned { coords ->
                    buttonWindowPos = coords.positionInWindow()
                }
                .pointerInput(action) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val downPos = down.position
                        var holdTriggered = false
                        var releasedAsTap = false
                        var maxMovePx = 0f

                        try {
                            withTimeout(240L) {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                    if (!change.pressed) {
                                        change.consume()
                                        releasedAsTap = true
                                        break
                                    }
                                    val dist = hypot(
                                        change.position.x - downPos.x,
                                        change.position.y - downPos.y
                                    )
                                    if (dist > maxMovePx) {
                                        maxMovePx = dist
                                    }
                                }
                            }
                        } catch (_: PointerEventTimeoutCancellationException) {
                            holdTriggered = true
                        }

                        if (releasedAsTap) {
                            if (maxMovePx < dragThresholdPx * 2f) {
                                currentOnClick()
                            }
                            return@awaitEachGesture
                        }

                        if (!holdTriggered) return@awaitEachGesture

                        // Long press activated: open the mini popup menu immediately and orient selection
                        // directly to the finger's vertical position over the interface (no virtual scroll offset).
                        val initialItems = currentBuildMenuItems()
                        if (initialItems.isEmpty()) return@awaitEachGesture

                        itemBoundsInWindow.keys.retainAll(initialItems.indices.toSet())
                        val activeSelectedIdx = initialItems.indexOfFirst { it.isSelected }

                        fun mapFingerPositionToMenuIndex(
                            localY: Float,
                            itemsList: List<QuickHoldMenuItem>
                        ): Int {
                            if (itemsList.isEmpty()) return 0
                            val fingerWindowY = buttonWindowPos.y + localY
                            val validBounds = itemBoundsInWindow.entries
                                .filter { it.key in itemsList.indices }
                                .sortedBy { it.key }

                            if (validBounds.isNotEmpty()) {
                                // Direct hit inside an item's window Y range
                                validBounds.firstOrNull { (_, range) ->
                                    fingerWindowY in range
                                }?.let { return it.key }

                                val topBound = validBounds.first()
                                val bottomBound = validBounds.last()
                                if (fingerWindowY <= topBound.value.start) {
                                    return topBound.key
                                }
                                // When finger is on the button or at/below the bottom of the popup,
                                // it points directly at the bottom "Откл." ("Off") item.
                                if (fingerWindowY >= bottomBound.value.start) {
                                    return itemsList.lastIndex
                                }
                                return validBounds.minByOrNull { (_, range) ->
                                    val center = (range.start + range.endInclusive) * 0.5f
                                    abs(fingerWindowY - center)
                                }?.key ?: itemsList.lastIndex
                            }

                            // Geometric fallback before first layout pass:
                            // Bottom of the menu (localY >= -6dp, on or right above the button) = lastIndex ("Откл."),
                            // moving finger upward above the button selects progressively higher items.
                            val heightAboveButtonPx = (-localY - with(density) { 6.dp.toPx() }).coerceAtLeast(0f)
                            val stepsUpFromBottom = (heightAboveButtonPx / stepPx).toInt()
                            return (itemsList.lastIndex - stepsUpFromBottom).coerceIn(0, itemsList.lastIndex)
                        }

                        var localHoveredIdx = mapFingerPositionToMenuIndex(downPos.y, initialItems)

                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        currentOnOpenHoldMenu(localHoveredIdx)

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            if (!change.pressed) {
                                change.consume()
                                val finalItems = currentBuildMenuItems()
                                if (localHoveredIdx in finalItems.indices &&
                                    (maxMovePx >= dragThresholdPx || localHoveredIdx != activeSelectedIdx)
                                ) {
                                    finalItems[localHoveredIdx].onSelect()
                                    currentOnDismissHoldMenu()
                                }
                                break
                            }

                            change.consume()
                            val moveDist = hypot(
                                change.position.x - downPos.x,
                                change.position.y - downPos.y
                            )
                            if (moveDist > maxMovePx) {
                                maxMovePx = moveDist
                            }

                            val itemsNow = currentBuildMenuItems()
                            if (itemsNow.isNotEmpty()) {
                                val computedIdx = mapFingerPositionToMenuIndex(change.position.y, itemsNow)
                                if (computedIdx != localHoveredIdx) {
                                    localHoveredIdx = computedIdx
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    currentOnUpdateHoveredIndex(localHoveredIdx)
                                }
                            }
                        }
                    }
                }
                .semantics { contentDescription = chipState.accessibilityLabel }
                .testTag(chipState.testTag)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.10f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(
                        horizontal = if (chipState.counterBadge != null) 9.dp else 10.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (textOnLeft) {
                    AnimatedVisibility(
                        visible = chipState.counterBadge != null,
                        enter = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut()
                    ) {
                        Text(
                            text = chipState.counterBadge.orEmpty(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }

                Icon(
                    imageVector = when (action) {
                        PlayerQuickAction.SCALE -> when (scaleMode) {
                            VideoScaleMode.FIT -> Icons.Default.AspectRatio
                            VideoScaleMode.ZOOM -> Icons.Default.Fullscreen
                            VideoScaleMode.STRETCH -> Icons.Default.OpenInFull
                        }
                        else -> action.iconVector()
                    },
                    contentDescription = chipState.accessibilityLabel,
                    tint = chipState.iconTint,
                    modifier = Modifier.size(18.dp)
                )
                if (action == PlayerQuickAction.ORIENTATION && isOrientationLocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = chipState.iconTint,
                        modifier = Modifier.size(12.dp)
                    )
                }

                if (!textOnLeft) {
                    AnimatedVisibility(
                        visible = chipState.counterBadge != null,
                        enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut()
                    ) {
                        Text(
                            text = chipState.counterBadge.orEmpty(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionHoldMiniPopupCard(
    action: PlayerQuickAction,
    title: String,
    items: List<QuickHoldMenuItem>,
    hoveredIndex: Int,
    onItemWindowBoundsChanged: (Int, ClosedFloatingPointRange<Float>) -> Unit = { _, _ -> },
    onSelectItem: (QuickHoldMenuItem) -> Unit
) {
    val glassBg = Color(0xC80B1220)
    val readableAccent = MaterialTheme.colorScheme.primary.ensureReadableOn(glassBg, minContrast = 5.4f)
    val glassShape = RoundedCornerShape(16.dp)

    Surface(
        color = glassBg,
        shape = glassShape,
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.44f),
                    readableAccent.copy(alpha = 0.62f),
                    Color.White.copy(alpha = 0.20f)
                )
            )
        ),
        shadowElevation = 16.dp,
        modifier = Modifier
            .widthIn(min = 176.dp, max = 248.dp)
            .testTag("quick_hold_mini_menu_${action.id}")
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.16f),
                        readableAccent.copy(alpha = 0.08f),
                        Color(0x33040812)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 296.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 7.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        color = readableAccent,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.14f),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
                items.forEachIndexed { index, item ->
                    val isHovered = index == hoveredIndex
                    if (item.isOffOption && index > 0) {
                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.16f),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    val bgColor = when {
                        isHovered && item.isOffOption -> Color(0xD97F1D1D)
                        isHovered -> lerp(Color(0xD9152238), readableAccent, 0.34f)
                        item.isSelected -> Color.White.copy(alpha = 0.16f)
                        else -> Color.White.copy(alpha = 0.04f)
                    }
                    val itemBorder = when {
                        isHovered && item.isOffOption -> BorderStroke(0.9.dp, Color(0xFFFFB4AB).copy(alpha = 0.85f))
                        isHovered -> BorderStroke(0.9.dp, readableAccent.copy(alpha = 0.90f))
                        item.isSelected -> BorderStroke(0.65.dp, readableAccent.copy(alpha = 0.48f))
                        else -> null
                    }
                    val textColor = when {
                        isHovered -> Color.White
                        item.isOffOption -> Color(0xFFFFB4AB)
                        item.isSelected -> readableAccent
                        else -> Color(0xFFF1F5F9)
                    }
                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(10.dp),
                        border = itemBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .onGloballyPositioned { coords ->
                                val topY = coords.positionInWindow().y
                                val bottomY = topY + coords.size.height
                                onItemWindowBoundsChanged(index, topY..bottomY)
                            }
                            .clickable { onSelectItem(item) }
                            .testTag("quick_hold_item_${action.id}_${item.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (item.isOffOption) {
                                    Icon(
                                        imageVector = Icons.Default.PowerSettingsNew,
                                        contentDescription = null,
                                        tint = textColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else if (item.isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = textColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(14.dp))
                                }
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                    fontWeight = if (isHovered || item.isSelected || item.isOffOption) {
                                        FontWeight.SemiBold
                                    } else {
                                        FontWeight.Medium
                                    },
                                    color = textColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (!item.badge.isNullOrBlank()) {
                                Surface(
                                    color = if (isHovered) {
                                        Color.White.copy(alpha = 0.22f)
                                    } else {
                                        Color.White.copy(alpha = 0.14f)
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = item.badge,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        fontWeight = FontWeight.SemiBold,
                                        color = textColor,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun buildHoldMenuItemsForAction(
    action: PlayerQuickAction,
    isEnglish: Boolean,
    playbackState: PlaybackRuntimeState,
    settings: PlayerSettings,
    scaleMode: VideoScaleMode,
    orientationMode: PlayerOrientationMode,
    onTriggerAction: (PlayerQuickAction) -> Unit,
    onSelectAudio: (AudioTrackUiOption) -> Unit,
    onSelectAudioDeArtifactMode: (AudioDeArtifactMode) -> Unit,
    onSelectSubtitle: (SubtitleTrackUiOption) -> Unit,
    onSelectSpeed: (Float) -> Unit,
    onSelectFrameInterpolationMode: (FrameInterpolationMode) -> Unit,
    onToggleFrameInterpolation: (Boolean) -> Unit,
    onSelectAnime4KPreset: (Anime4KPreset) -> Unit,
    onToggleAnime4KEnabled: (Boolean) -> Unit,
    onSelectDecoderMode: (DecoderMode) -> Unit,
    onSelectScaleMode: (VideoScaleMode) -> Unit,
    onSetSleepTimer: (SleepTimerPreset) -> Unit,
    onMarkAbPointA: () -> Unit,
    onMarkAbPointB: () -> Unit,
    onClearAbLoop: () -> Unit,
    onUpdateSkipIntroSeconds: (Int) -> Unit,
    onSeekByMs: (Long) -> Unit,
    onSelectOrientationMode: (PlayerOrientationMode) -> Unit,
    isOrientationLocked: Boolean = false,
    onToggleOrientationLock: () -> Unit = {}
): List<QuickHoldMenuItem> {
    val offText = if (isEnglish) "Off" else "Откл."
    return when (action) {
        PlayerQuickAction.FRAME_INTERPOLATION -> {
            val activeMode = settings.effectiveFrameInterpolationMode
            val modes = listOf(
                FrameInterpolationMode.ANIME,
                FrameInterpolationMode.MAX_FPS,
                FrameInterpolationMode.SMOOTH_60,
                FrameInterpolationMode.CINEMA
            )
            modes.map { mode ->
                QuickHoldMenuItem(
                    id = mode.id,
                    label = mode.localizedTitle(isEnglish),
                    badge = mode.localizedBadge(isEnglish),
                    isSelected = activeMode == mode,
                    onSelect = { onSelectFrameInterpolationMode(mode) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = "RAW",
                isSelected = activeMode == FrameInterpolationMode.OFF,
                isOffOption = true,
                onSelect = { onToggleFrameInterpolation(false) }
            )
        }

        PlayerQuickAction.ANIME4K -> {
            val activePreset = settings.effectiveAnime4KPreset
            val presets = listOf(
                Anime4KPreset.ANIME4K_UPSCALE_CNNX,
                Anime4KPreset.ANIME4K_UPSCALE_CNN,
                Anime4KPreset.ANIME4K_FAST
            )
            presets.map { preset ->
                QuickHoldMenuItem(
                    id = preset.id,
                    label = preset.localizedShortLabel(isEnglish),
                    badge = preset.badgeLabel,
                    isSelected = activePreset == preset,
                    onSelect = { onSelectAnime4KPreset(preset) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = "RAW",
                isSelected = activePreset == Anime4KPreset.OFF,
                isOffOption = true,
                onSelect = { onToggleAnime4KEnabled(false) }
            )
        }

        PlayerQuickAction.DECODER -> {
            val current = playbackState.activeDecoderMode
            listOf(
                DecoderMode.SOFTWARE_PREFER,
                DecoderMode.HW_STRICT,
                DecoderMode.HW_PLUS
            ).map { mode ->
                QuickHoldMenuItem(
                    id = mode.id,
                    label = mode.localizedTitle(isEnglish),
                    badge = mode.shortBadge,
                    isSelected = current == mode,
                    onSelect = { onSelectDecoderMode(mode) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = "HW+",
                isSelected = false,
                isOffOption = true,
                onSelect = { onSelectDecoderMode(DecoderMode.HW_PLUS) }
            )
        }

        PlayerQuickAction.SPEED -> {
            val current = playbackState.playbackSpeed
            val speeds = listOf(2.0f, 1.5f, 1.25f, 0.75f)
            speeds.map { spd ->
                val lbl = formatPlaybackSpeedLabel(spd)
                QuickHoldMenuItem(
                    id = lbl,
                    label = lbl,
                    isSelected = abs(current - spd) < 0.03f,
                    onSelect = { onSelectSpeed(spd) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = "1.0x",
                isSelected = abs(current - 1.0f) < 0.03f,
                isOffOption = true,
                onSelect = { onSelectSpeed(1.0f) }
            )
        }

        PlayerQuickAction.SUBTITLES -> {
            val tracks = playbackState.subtitleTrackOptions.filter { it.id != "off" }
            val offOpt = playbackState.subtitleTrackOptions.firstOrNull { it.id == "off" }
            tracks.take(6).map { opt ->
                QuickHoldMenuItem(
                    id = opt.id,
                    label = opt.label,
                    badge = opt.badge,
                    isSelected = opt.isSelected,
                    onSelect = { onSelectSubtitle(opt) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                isSelected = playbackState.selectedSubtitleId == "off",
                isOffOption = true,
                onSelect = {
                    if (offOpt != null) {
                        onSelectSubtitle(offOpt)
                    } else {
                        onTriggerAction(PlayerQuickAction.SUBTITLES)
                    }
                }
            )
        }

        PlayerQuickAction.AUDIO -> {
            val trackItems = playbackState.audioTrackOptions.take(4).map { opt ->
                QuickHoldMenuItem(
                    id = opt.id,
                    label = opt.label,
                    badge = opt.badge,
                    isSelected = opt.isSelected,
                    onSelect = { onSelectAudio(opt) }
                )
            }
            val dspModes = listOf(
                AudioDeArtifactMode.AGGRESSIVE,
                AudioDeArtifactMode.STANDARD,
                AudioDeArtifactMode.SOFT
            ).map { mode ->
                val dspPrefix = if (isEnglish) "DSP: " else "Очистка: "
                QuickHoldMenuItem(
                    id = "dsp_${mode.id}",
                    label = "$dspPrefix${mode.localizedTitle(isEnglish)}",
                    badge = mode.localizedBadge(isEnglish),
                    isSelected = playbackState.audioDeArtifactMode == mode,
                    onSelect = { onSelectAudioDeArtifactMode(mode) }
                )
            }
            trackItems + dspModes + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = if (isEnglish) "DSP Off" else "Без очистки",
                isSelected = playbackState.audioDeArtifactMode == AudioDeArtifactMode.OFF,
                isOffOption = true,
                onSelect = { onSelectAudioDeArtifactMode(AudioDeArtifactMode.OFF) }
            )
        }

        PlayerQuickAction.SCALE -> {
            listOf(VideoScaleMode.STRETCH, VideoScaleMode.ZOOM).map { mode ->
                QuickHoldMenuItem(
                    id = mode.id,
                    label = mode.localizedTitle(isEnglish),
                    badge = mode.localizedShortLabel(isEnglish),
                    isSelected = scaleMode == mode,
                    onSelect = { onSelectScaleMode(mode) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = VideoScaleMode.FIT.localizedShortLabel(isEnglish),
                isSelected = scaleMode == VideoScaleMode.FIT,
                isOffOption = true,
                onSelect = { onSelectScaleMode(VideoScaleMode.FIT) }
            )
        }

        PlayerQuickAction.SLEEP_TIMER -> {
            listOf(
                SleepTimerPreset.END_OF_EPISODE,
                SleepTimerPreset.MIN_60,
                SleepTimerPreset.MIN_45,
                SleepTimerPreset.MIN_30,
                SleepTimerPreset.MIN_15
            ).map { preset ->
                QuickHoldMenuItem(
                    id = preset.id,
                    label = preset.localizedTitle(isEnglish),
                    isSelected = playbackState.sleepTimerPreset == preset,
                    onSelect = { onSetSleepTimer(preset) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                isSelected = playbackState.sleepTimerPreset == SleepTimerPreset.OFF,
                isOffOption = true,
                onSelect = { onSetSleepTimer(SleepTimerPreset.OFF) }
            )
        }

        PlayerQuickAction.AB_LOOP -> {
            listOf(
                QuickHoldMenuItem(
                    id = "POINT_B",
                    label = if (isEnglish) "Set Point B" else "Точка B",
                    badge = playbackState.abLoopEndMs?.let { formatTimeMs(it) },
                    isSelected = playbackState.abLoopEndMs != null,
                    onSelect = onMarkAbPointB
                ),
                QuickHoldMenuItem(
                    id = "POINT_A",
                    label = if (isEnglish) "Set Point A" else "Точка A",
                    badge = playbackState.abLoopStartMs?.let { formatTimeMs(it) },
                    isSelected = playbackState.abLoopStartMs != null,
                    onSelect = onMarkAbPointA
                ),
                QuickHoldMenuItem(
                    id = "OFF",
                    label = offText,
                    isSelected = playbackState.abLoopStartMs == null && playbackState.abLoopEndMs == null,
                    isOffOption = true,
                    onSelect = onClearAbLoop
                )
            )
        }

        PlayerQuickAction.SKIP_INTRO -> {
            val secUnit = if (isEnglish) "s" else "с"
            listOf(120, 90, 85, 75, 60).map { sec ->
                QuickHoldMenuItem(
                    id = "skip_$sec",
                    label = "+$sec $secUnit",
                    isSelected = settings.skipIntroSeconds == sec,
                    onSelect = {
                        onUpdateSkipIntroSeconds(sec)
                        onSeekByMs(sec * 1000L)
                    }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                isSelected = false,
                isOffOption = true,
                onSelect = {}
            )
        }

        PlayerQuickAction.ORIENTATION -> {
            val lockItem = QuickHoldMenuItem(
                id = "LOCK_ORIENTATION",
                label = if (isEnglish) {
                    if (isOrientationLocked) "Unlock Orientation" else "Lock Orientation"
                } else {
                    if (isOrientationLocked) "Снять блокировку" else "Закрепить поворот"
                },
                badge = if (isOrientationLocked) "🔒 ON" else "🔒",
                isSelected = isOrientationLocked,
                onSelect = onToggleOrientationLock
            )
            listOf(lockItem) + listOf(
                PlayerOrientationMode.PORTRAIT,
                PlayerOrientationMode.LANDSCAPE,
                PlayerOrientationMode.VIDEO_ASPECT
            ).map { mode ->
                QuickHoldMenuItem(
                    id = mode.id,
                    label = mode.localizedTitle(isEnglish),
                    badge = mode.localizedBadge(isEnglish),
                    isSelected = !isOrientationLocked && orientationMode == mode,
                    onSelect = { onSelectOrientationMode(mode) }
                )
            } + QuickHoldMenuItem(
                id = "OFF",
                label = offText,
                badge = settings.defaultPlayerOrientation.localizedBadge(isEnglish),
                isSelected = !isOrientationLocked && orientationMode == settings.defaultPlayerOrientation,
                isOffOption = true,
                onSelect = {
                    if (isOrientationLocked) {
                        onToggleOrientationLock()
                    }
                    onSelectOrientationMode(settings.defaultPlayerOrientation)
                }
            )
        }

        else -> {
            listOf(
                QuickHoldMenuItem(
                    id = "OPEN",
                    label = action.localizedTitle(isEnglish),
                    isSelected = true,
                    onSelect = { onTriggerAction(action) }
                ),
                QuickHoldMenuItem(
                    id = "OFF",
                    label = offText,
                    isSelected = false,
                    isOffOption = true,
                    onSelect = {}
                )
            )
        }
    }
}

private data class QuickChipVisualState(
    val accessibilityLabel: String,
    val counterBadge: String? = null,
    val iconTint: Color,
    val containerColor: Color,
    val border: BorderStroke? = null,
    val testTag: String
)

@Composable
private fun resolveQuickChipVisuals(
    action: PlayerQuickAction,
    playbackState: PlaybackRuntimeState,
    settings: PlayerSettings,
    scaleMode: VideoScaleMode,
    orientationMode: PlayerOrientationMode,
    isOrientationLocked: Boolean = false
): QuickChipVisualState {
    val isEnglish = LocalIsEnglish.current
    val accentFree = settings.playerControlsTheme.isAccentFree
    val frameless = settings.playerControlsTheme.isFrameless
    val darkHudGlass = Color(0xFF0C1322)
    val primary = if (accentFree) {
        Color.White.copy(alpha = 0.92f)
    } else {
        MaterialTheme.colorScheme.primary.ensureReadableOn(darkHudGlass, minContrast = 5.2f)
    }
    val secondary = if (accentFree) {
        Color.White.copy(alpha = 0.92f)
    } else {
        MaterialTheme.colorScheme.secondary.ensureReadableOn(darkHudGlass, minContrast = 5.2f)
    }
    val activePrimaryBg = when {
        frameless -> Color.Transparent
        accentFree -> Color(0x991E293B)
        else -> lerp(Color(0xCC0E1729), primary, 0.28f)
    }
    val activeSecondaryBg = when {
        frameless -> Color.Transparent
        accentFree -> Color(0x991E293B)
        else -> lerp(Color(0xCC0E1729), secondary, 0.28f)
    }
    val neutralBg = if (frameless) Color.Transparent else Color(0x88101828)
    val neutralBorder = when {
        frameless -> null
        accentFree -> BorderStroke(0.75.dp, Color.White.copy(alpha = 0.24f))
        else -> BorderStroke(0.75.dp, Color.White.copy(alpha = 0.20f))
    }
    val activeBorder = when {
        frameless -> null
        accentFree -> BorderStroke(0.9.dp, Color.White.copy(alpha = 0.45f))
        else -> BorderStroke(0.9.dp, primary.copy(alpha = 0.72f))
    }

    return when (action) {
        PlayerQuickAction.SPEED -> {
            val isModified = abs(playbackState.playbackSpeed - 1.0f) >= 0.02f
            val speedStr = formatPlaybackSpeedLabel(playbackState.playbackSpeed)
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Speed $speedStr" else "Скорость $speedStr",
                counterBadge = speedStr,
                iconTint = if (isModified) primary else Color.White,
                containerColor = if (isModified) activePrimaryBg else neutralBg,
                border = if (isModified) activeBorder else neutralBorder,
                testTag = "player_quick_chip_SPEED"
            )
        }

        PlayerQuickAction.AUDIO -> {
            val deArtifactActive = playbackState.isAudioDeArtifactActive &&
                playbackState.audioDeArtifactMode != AudioDeArtifactMode.OFF
            val modified = playbackState.audioDelayMs != 0L ||
                playbackState.audioBoostPercent > 100 ||
                deArtifactActive
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Audio & Restoration" else "Аудио и очистка",
                iconTint = primary,
                containerColor = if (modified) activePrimaryBg else neutralBg,
                border = if (modified) activeBorder else neutralBorder,
                testTag = "player_open_audio_sheet_chip"
            )
        }

        PlayerQuickAction.SUBTITLES -> {
            val subActive = playbackState.selectedSubtitleId != "off"
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Subtitles" else "Субтитры",
                iconTint = if (subActive) primary else Color.White,
                containerColor = if (subActive) activePrimaryBg else neutralBg,
                border = if (subActive) activeBorder else neutralBorder,
                testTag = "player_open_subtitles_sheet_chip"
            )
        }

        PlayerQuickAction.DECODER -> {
            val badge = playbackState.activeDecoderMode.shortBadge
            val isCustom = playbackState.activeDecoderMode != DecoderMode.HW_PLUS
            QuickChipVisualState(
                accessibilityLabel = (if (isEnglish) "Decoder " else "Декодер ") + badge,
                counterBadge = badge,
                iconTint = primary,
                containerColor = if (isCustom) activePrimaryBg else neutralBg,
                border = if (isCustom) activeBorder else neutralBorder,
                testTag = "player_open_decoder_sheet_chip"
            )
        }

        PlayerQuickAction.FRAME_INTERPOLATION -> {
            val activeMemc = settings.effectiveFrameInterpolationMode
            val isOn = activeMemc != FrameInterpolationMode.OFF
            val badge = if (isOn) activeMemc.localizedBadge(isEnglish) else null
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Frame Interpolation ${badge ?: "Off"}" else "Интерполяция кадров ${badge ?: "Выкл"}",
                counterBadge = badge,
                iconTint = if (isOn) primary else Color.White,
                containerColor = if (isOn) activePrimaryBg else neutralBg,
                border = if (isOn) activeBorder else neutralBorder,
                testTag = "player_quick_chip_FRAME_INTERPOLATION"
            )
        }

        PlayerQuickAction.ANIME4K -> {
            val activePreset = settings.effectiveAnime4KPreset
            val isOn = activePreset != Anime4KPreset.OFF
            val badge = if (isOn) activePreset.localizedShortLabel(isEnglish) else null
            QuickChipVisualState(
                accessibilityLabel = "Anime4K ${badge ?: if (isEnglish) "Off" else "Выкл"}",
                counterBadge = badge,
                iconTint = if (isOn) secondary else Color.White,
                containerColor = if (isOn) activeSecondaryBg else neutralBg,
                border = if (isOn) activeBorder else neutralBorder,
                testTag = "player_open_anime4k_sheet_chip"
            )
        }

        PlayerQuickAction.EPISODES -> {
            val total = playbackState.playlist.size.coerceAtLeast(1)
            val currentNum = (playbackState.currentEpisodeIndex + 1).coerceIn(1, total)
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Episodes $currentNum/$total" else "Серии $currentNum/$total",
                counterBadge = "$currentNum/$total",
                iconTint = primary,
                containerColor = neutralBg,
                border = neutralBorder,
                testTag = "player_quick_chip_EPISODES"
            )
        }

        PlayerQuickAction.CHAPTERS -> {
            val chapterCount = playbackState.chapters.size
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Chapters ($chapterCount)" else "Главы ($chapterCount)",
                iconTint = if (chapterCount > 0) primary else Color.White,
                containerColor = if (chapterCount > 0) activePrimaryBg else neutralBg,
                border = if (chapterCount > 0) activeBorder else neutralBorder,
                testTag = "player_quick_chip_CHAPTERS"
            )
        }

        PlayerQuickAction.SCALE -> {
            val isZoomed = scaleMode != VideoScaleMode.FIT
            QuickChipVisualState(
                accessibilityLabel = scaleMode.localizedShortLabel(isEnglish),
                iconTint = if (isZoomed) primary else Color.White,
                containerColor = if (isZoomed) activePrimaryBg else neutralBg,
                border = if (isZoomed) activeBorder else neutralBorder,
                testTag = "player_quick_chip_SCALE"
            )
        }

        PlayerQuickAction.AUDIO_BOOST -> {
            val boosted = playbackState.audioBoostPercent > 100
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Audio Boost" else "Усиление звука",
                iconTint = if (boosted) primary else Color.White,
                containerColor = if (boosted) activePrimaryBg else neutralBg,
                border = if (boosted) activeBorder else neutralBorder,
                testTag = "player_quick_chip_AUDIO_BOOST"
            )
        }

        PlayerQuickAction.SLEEP_TIMER -> {
            val active = playbackState.isSleepTimerActive
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Sleep Timer" else "Таймер сна",
                iconTint = if (active) primary else Color.White,
                containerColor = if (active) activePrimaryBg else neutralBg,
                border = if (active) activeBorder else neutralBorder,
                testTag = "player_quick_chip_SLEEP_TIMER"
            )
        }

        PlayerQuickAction.AB_LOOP -> {
            val hasA = playbackState.abLoopStartMs != null
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "A–B Loop" else "Повтор A–B",
                iconTint = if (hasA) primary else Color.White,
                containerColor = if (hasA) activePrimaryBg else neutralBg,
                border = if (hasA) activeBorder else neutralBorder,
                testTag = "player_quick_chip_AB_LOOP"
            )
        }

        PlayerQuickAction.SKIP_INTRO -> {
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "+${settings.skipIntroSeconds}s Intro" else "+${settings.skipIntroSeconds}с Опенинг",
                iconTint = primary,
                containerColor = neutralBg,
                border = neutralBorder,
                testTag = "player_quick_chip_SKIP_INTRO"
            )
        }

        PlayerQuickAction.ORIENTATION -> {
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Screen Orientation" else "Ориентация экрана",
                iconTint = if (isOrientationLocked) primary else Color.White,
                containerColor = if (isOrientationLocked) activePrimaryBg else neutralBg,
                border = if (isOrientationLocked) activeBorder else neutralBorder,
                testTag = "player_quick_chip_ORIENTATION"
            )
        }

        PlayerQuickAction.LOCK -> {
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "Lock Screen" else "Блокировка экрана",
                iconTint = Color.White,
                containerColor = neutralBg,
                border = neutralBorder,
                testTag = "player_quick_chip_LOCK"
            )
        }

        PlayerQuickAction.PIP -> {
            QuickChipVisualState(
                accessibilityLabel = "PiP",
                iconTint = primary,
                containerColor = neutralBg,
                border = neutralBorder,
                testTag = "player_quick_chip_PIP"
            )
        }

        PlayerQuickAction.TOOLS -> {
            val hasActiveTool = playbackState.isSleepTimerActive ||
                playbackState.isAbLoopActive ||
                playbackState.audioBoostPercent > 100
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "More Tools" else "Все инструменты",
                iconTint = if (hasActiveTool) primary else Color.White,
                containerColor = if (hasActiveTool) activePrimaryBg else neutralBg,
                border = if (hasActiveTool) activeBorder else neutralBorder,
                testTag = "player_open_tools_sheet_chip"
            )
        }

        PlayerQuickAction.FILE_INFO -> {
            QuickChipVisualState(
                accessibilityLabel = if (isEnglish) "File Info" else "Сведения о файле",
                iconTint = primary,
                containerColor = neutralBg,
                border = neutralBorder,
                testTag = "player_quick_chip_FILE_INFO"
            )
        }
    }
}

internal fun formatPlaybackSpeedLabel(speed: Float): String {
    val roundedHundredths = (speed * 100).roundToInt()
    return if (roundedHundredths % 10 == 0) {
        String.format(Locale.US, "%.1fx", roundedHundredths / 100f)
    } else {
        String.format(Locale.US, "%.2fx", roundedHundredths / 100f)
    }
}

/**
 * Dedicated Skip Intro Interval configuration bottom sheet opened by long-pressing
 * the Skip Intro button directly in the player. Supports presets + arbitrary custom intervals.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun SkipIntroConfigModalSheet(
    skipIntroSeconds: Int,
    onUpdateSkipIntroSeconds: (Int) -> Unit,
    onSkipNow: () -> Unit,
    onDismiss: () -> Unit
) {
    var customInputText by remember(skipIntroSeconds) { mutableStateOf(skipIntroSeconds.toString()) }
    val presets = listOf(60, 75, 85, 90, 110, 120)

    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("skip_intro_config_modal_sheet")
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = tr("Интервал пропуска опенинга", "Skip Intro Interval"),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = tr(
                                "Выберите готовый пресет или укажите любое количество секунд",
                                "Pick a preset or enter any custom duration in seconds"
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = tr("+${skipIntroSeconds} с", "+${skipIntroSeconds} s"),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Presets
            Text(
                text = tr("Готовые интервалы", "Presets"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                presets.forEach { sec ->
                    FilterChip(
                        selected = skipIntroSeconds == sec,
                        onClick = {
                            onUpdateSkipIntroSeconds(sec)
                            customInputText = sec.toString()
                        },
                        label = { Text(tr("+${sec} с", "+${sec}s"), style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("skip_intro_preset_chip_$sec")
                    )
                }
            }

            // Smooth Slider for arbitrary custom value (5s .. 240s) + direct input (5s .. 600s)
            Text(
                text = tr("Произвольный интервал (5–600 сек)", "Custom Interval (5–600 sec)"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            InstantSlider(
                value = skipIntroSeconds.coerceIn(5, 240).toFloat(),
                onValueChange = { raw ->
                    val v = raw.roundToInt().coerceIn(5, 240)
                    onUpdateSkipIntroSeconds(v)
                    customInputText = v.toString()
                },
                valueRange = 5f..240f,
                modifier = Modifier.testTag("skip_intro_custom_slider")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        val next = (skipIntroSeconds - 5).coerceIn(5, 600)
                        onUpdateSkipIntroSeconds(next)
                        customInputText = next.toString()
                    },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("skip_intro_minus_5_btn")
                ) {
                    Text(tr("-5с", "-5s"), style = MaterialTheme.typography.labelMedium, maxLines = 1)
                }
                OutlinedButton(
                    onClick = {
                        val next = (skipIntroSeconds - 1).coerceIn(5, 600)
                        onUpdateSkipIntroSeconds(next)
                        customInputText = next.toString()
                    },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("skip_intro_minus_1_btn")
                ) {
                    Text(tr("-1с", "-1s"), style = MaterialTheme.typography.labelMedium, maxLines = 1)
                }
                OutlinedButton(
                    onClick = {
                        val next = (skipIntroSeconds + 1).coerceIn(5, 600)
                        onUpdateSkipIntroSeconds(next)
                        customInputText = next.toString()
                    },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("skip_intro_plus_1_btn")
                ) {
                    Text(tr("+1с", "+1s"), style = MaterialTheme.typography.labelMedium, maxLines = 1)
                }
                OutlinedButton(
                    onClick = {
                        val next = (skipIntroSeconds + 5).coerceIn(5, 600)
                        onUpdateSkipIntroSeconds(next)
                        customInputText = next.toString()
                    },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("skip_intro_plus_5_btn")
                ) {
                    Text(tr("+5с", "+5s"), style = MaterialTheme.typography.labelMedium, maxLines = 1)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customInputText,
                    onValueChange = { text ->
                        val digits = text.filter { it.isDigit() }.take(3)
                        customInputText = digits
                        digits.toIntOrNull()?.let { parsed ->
                            if (parsed in 5..600) {
                                onUpdateSkipIntroSeconds(parsed)
                            }
                        }
                    },
                    label = { Text(tr("Точное значение (сек)", "Exact value (sec)"), style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("skip_intro_custom_input")
                )

                FilledTonalButton(
                    onClick = {
                        customInputText.toIntOrNull()?.let { parsed ->
                            onUpdateSkipIntroSeconds(parsed.coerceIn(5, 600))
                        }
                        onSkipNow()
                        onDismiss()
                    },
                    modifier = Modifier.testTag("skip_intro_apply_and_skip_btn")
                ) {
                    Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(tr("Пропустить (+${skipIntroSeconds}с)", "Skip (+${skipIntroSeconds}s)"), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

/**
 * Dedicated Playback Speed bottom sheet opened by the SPEED quick button.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun SpeedQuickModalSheet(
    playbackSpeed: Float,
    onSelectSpeed: (Float) -> Unit,
    onSetDefaultSpeed: (Float) -> Unit = {},
    onDismiss: () -> Unit
) {
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            tr("Скорость воспроизведения", "Playback Speed"),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = formatPlaybackSpeedLabel(playbackSpeed),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Slider(
                    value = playbackSpeed,
                    onValueChange = { raw ->
                        val stepped = ((raw * 20f).roundToInt() / 20f).coerceIn(0.25f, 3.0f)
                        onSelectSpeed(stepped)
                    },
                    valueRange = 0.25f..3.0f,
                    modifier = Modifier.testTag("speed_sheet_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val next = (((playbackSpeed - 0.05f) * 20f).roundToInt() / 20f).coerceAtLeast(0.25f)
                            onSelectSpeed(next)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("0.05x", style = MaterialTheme.typography.labelSmall)
                    }

                    FilledTonalButton(
                        onClick = { onSelectSpeed(1.0f) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(tr("1.0x Норма", "1.0x Normal"), style = MaterialTheme.typography.labelSmall)
                    }

                    OutlinedButton(
                        onClick = {
                            val next = (((playbackSpeed + 0.05f) * 20f).roundToInt() / 20f).coerceAtMost(3.0f)
                            onSelectSpeed(next)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("0.05x", style = MaterialTheme.typography.labelSmall)
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f, 2.5f, 3.0f).forEach { preset ->
                        FilterChip(
                            selected = abs(playbackSpeed - preset) < 0.02f,
                            onClick = { onSelectSpeed(preset) },
                            label = { Text(formatPlaybackSpeedLabel(preset), style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.testTag("speed_preset_chip_$preset")
                        )
                    }
                }

                Button(
                    onClick = {
                        onSetDefaultSpeed(playbackSpeed)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("set_default_speed_button")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        tr("Сделать по умолчанию", "Set as default speed"),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }

/**
 * Dedicated Aspect Ratio / Scale Mode bottom sheet opened by long-pressing SCALE quick action.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScaleQuickModalSheet(
    currentScaleMode: VideoScaleMode,
    onSelectScaleMode: (VideoScaleMode) -> Unit,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = when (currentScaleMode) {
                        VideoScaleMode.FIT -> Icons.Default.AspectRatio
                        VideoScaleMode.ZOOM -> Icons.Default.Fullscreen
                        VideoScaleMode.STRETCH -> Icons.Default.OpenInFull
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    tr("Масштаб кадра", "Aspect Ratio"),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            VideoScaleMode.entries.forEach { mode ->
                val selected = currentScaleMode == mode
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        } else {
                            playerPopupCardColor()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onSelectScaleMode(mode)
                            onDismiss()
                        }
                        .testTag("scale_sheet_option_${mode.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = when (mode) {
                                VideoScaleMode.FIT -> Icons.Default.AspectRatio
                                VideoScaleMode.ZOOM -> Icons.Default.Fullscreen
                                VideoScaleMode.STRETCH -> Icons.Default.OpenInFull
                            },
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = mode.localizedTitle(isEnglish),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = selected,
                            onClick = {
                                onSelectScaleMode(mode)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Playlist / Episodes bottom sheet opened by the EPISODES quick button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EpisodesQuickModalSheet(
    playlist: List<EpisodeItem>,
    currentEpisodeIndex: Int,
    onSelectEpisodeIndex: (Int) -> Unit,
    onDismiss: () -> Unit
) {
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
                    imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = tr("Серии в плейлисте (${playlist.size})", "Episodes in Playlist (${playlist.size})"),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            playlist.forEachIndexed { idx, ep ->
                val isCurrent = idx == currentEpisodeIndex
                Surface(
                    color = if (isCurrent) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                    } else {
                        PlayerPopupCardColor
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = if (isCurrent) {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onSelectEpisodeIndex(idx)
                            onDismiss()
                        }
                        .testTag("playlist_sheet_episode_$idx")
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = if (isCurrent) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.White.copy(alpha = 0.12f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = ep.episodeBadge ?: "#${idx + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isCurrent) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    },
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }

                            Text(
                                text = ep.cleanTitle,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            ep.resolutionBadge?.let { resBadge ->
                                ResolutionBadgeChip(
                                    badge = resBadge,
                                    compact = true
                                )
                            }
                        }

                        if (ep.progressFraction > 0f) {
                            LinearProgressIndicator(
                                progress = { ep.progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color.White.copy(alpha = 0.12f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Audio Boost bottom sheet opened by the AUDIO_BOOST quick button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AudioBoostQuickModalSheet(
    audioBoostPercent: Int,
    onSetAudioBoostPercent: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AudioBoostControlSection(
                audioBoostPercent = audioBoostPercent,
                onSetAudioBoostPercent = onSetAudioBoostPercent
            )
            Text(
                text = tr(
                    "Использует системный DSP LoudnessEnhancer для усиления тихих озвучек без изменения скорости речи.",
                    "Uses system DSP LoudnessEnhancer to amplify quiet voiceovers without altering speech pitch or speed."
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Dedicated Sleep Timer bottom sheet opened by the SLEEP_TIMER quick button.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun SleepTimerQuickModalSheet(
    sleepTimerPreset: SleepTimerPreset,
    sleepTimerRemainingMs: Long,
    onSetSleepTimer: (SleepTimerPreset) -> Unit,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    PlayerFrostedModalSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(tr("Таймер сна", "Sleep Timer"), style = MaterialTheme.typography.titleMedium)
                }

                if (sleepTimerPreset != SleepTimerPreset.OFF) {
                    val statusText = if (sleepTimerPreset.stopAtEndOfEpisode) {
                        tr("До конца серии", "Until end of episode")
                    } else {
                        formatTimeMs(sleepTimerRemainingMs)
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SleepTimerPreset.entries.forEach { preset ->
                    FilterChip(
                        selected = sleepTimerPreset == preset,
                        onClick = { onSetSleepTimer(preset) },
                        label = { Text(preset.localizedTitle(isEnglish), style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag("sleep_sheet_chip_${preset.id}")
                    )
                }
            }
        }
    }
}
