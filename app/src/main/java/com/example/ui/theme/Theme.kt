package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.model.AppAccentColor
import com.example.model.AppThemeMode
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

val NegaiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

val KinoShapes = NegaiShapes

/**
 * Computes WCAG 2.1 relative luminance in [0.0, 1.0].
 */
fun Color.wcagRelativeLuminance(): Float {
    fun channelLuminance(c: Float): Float {
        val v = c.coerceIn(0f, 1f)
        return if (v <= 0.03928f) v / 12.92f else ((v + 0.055f) / 1.055f).pow(2.4f)
    }
    val r = channelLuminance(red)
    val g = channelLuminance(green)
    val b = channelLuminance(blue)
    return 0.2126f * r + 0.7152f * g + 0.0722f * b
}

/**
 * Computes WCAG 2.1 contrast ratio in [1.0, 21.0] between two colors.
 */
fun wcagContrastRatio(fg: Color, bg: Color): Float {
    val l1 = fg.wcagRelativeLuminance()
    val l2 = bg.wcagRelativeLuminance()
    val lighter = max(l1, l2)
    val darker = min(l1, l2)
    return (lighter + 0.05f) / (darker + 0.05f)
}

/**
 * Algorithmically adjusts [this] foreground color's lightness & saturation against [background]
 * until it meets or exceeds [minContrast] (default 4.5:1 WCAG AA).
 */
fun Color.ensureReadableOn(
    background: Color,
    minContrast: Float = 4.5f
): Color {
    if (wcagContrastRatio(this, background) >= minContrast) return this
    val bgLum = background.wcagRelativeLuminance()
    // Determine whether we should pull toward a tinted near-white or near-dark color
    val lightTarget = lerp(this, Color(0xFFF8FAFC), 0.88f)
    val darkTarget = lerp(this, Color(0xFF060911), 0.90f)
    val preferLight = bgLum < 0.36f
    val primaryTarget = if (preferLight) lightTarget else darkTarget
    val fallbackPole = if (preferLight) Color.White else Color(0xFF05070B)

    // Binary search along the interpolation curve to preserve hue & saturation as much as possible
    var low = 0f
    var high = 1f
    var best = fallbackPole
    for (i in 0 until 10) {
        val mid = (low + high) * 0.5f
        val candidate = lerp(this, primaryTarget, mid)
        if (wcagContrastRatio(candidate, background) >= minContrast) {
            best = candidate
            high = mid
        } else {
            low = mid
        }
    }
    if (wcagContrastRatio(best, background) >= minContrast) return best

    // Final guarantee toward pure white/black pole if needed
    var low2 = 0f
    var high2 = 1f
    for (i in 0 until 8) {
        val mid = (low2 + high2) * 0.5f
        val candidate = lerp(primaryTarget, fallbackPole, mid)
        if (wcagContrastRatio(candidate, background) >= minContrast) {
            best = candidate
            high2 = mid
        } else {
            low2 = mid
        }
    }
    return best
}

@JvmName("computeAutoContrastTextColor")
fun autoContrastTextColor(
    background: Color,
    minContrast: Float = 4.8f
): Color = background.autoContrastTextColor(minContrast)

/**
 * Computes an optimal, hue-harmonized text/icon color on top of [this] background surface/accent.
 */
fun Color.autoContrastTextColor(
    minContrast: Float = 4.8f
): Color {
    val bgLum = wcagRelativeLuminance()
    // Slightly tint the text color with the underlying accent hue for visual harmony
    val darkInk = lerp(Color(0xFF05080F), this, 0.14f)
    val lightInk = lerp(Color(0xFFF8FAFC), this, 0.10f)

    val darkContrast = wcagContrastRatio(darkInk, this)
    val lightContrast = wcagContrastRatio(lightInk, this)

    val chosen = when {
        bgLum > 0.34f && darkContrast >= minContrast -> darkInk
        bgLum <= 0.34f && lightContrast >= minContrast -> lightInk
        darkContrast >= lightContrast -> darkInk
        else -> lightInk
    }
    return chosen.ensureReadableOn(this, minContrast)
}

/**
 * Builds a complete Material 3 [ColorScheme] from the separated [AppThemeMode] (5 modes)
 * and [AppAccentColor] (7 accents including Monet), applying algorithmic WCAG 2.1 auto-contrast
 * to all foreground/container pairs.
 */
fun buildNegaiColorScheme(
    themeMode: AppThemeMode,
    accentColor: AppAccentColor,
    isSystemInDarkTheme: Boolean,
    dynamicDark: ColorScheme? = null,
    dynamicLight: ColorScheme? = null
): ColorScheme {
    val resolvedMode = themeMode.resolveModeFor(isSystemInDarkTheme)
    val isDark = resolvedMode.isDark
    val isPureOled = resolvedMode.isPureOled

    val monetScheme = if (accentColor == AppAccentColor.MONET) {
        if (isDark) dynamicDark else dynamicLight
    } else {
        null
    }

    val rawPrimary = monetScheme?.primary
        ?: Color(if (isDark) accentColor.darkPrimaryArgb else accentColor.lightPrimaryArgb)
    val rawSecondary = monetScheme?.secondary
        ?: Color(if (isDark) accentColor.darkSecondaryArgb else accentColor.lightSecondaryArgb)
    val rawTertiary = monetScheme?.tertiary
        ?: if (isDark) Color(0xFFFFCA28) else Color(0xFFD97706)

    // Base background & surface hierarchy:
    // - LIGHT: Crisp daylight surfaces (#F8FAFC / #FFFFFF)
    // - DARK: Soft slate-graphite (#0C111B / #111827)
    // - OLED: Pure pitch-black (#000000)
    val background: Color
    val surface: Color
    val surfaceVariant: Color
    val surfaceContainerLowest: Color
    val surfaceContainerLow: Color
    val surfaceContainer: Color
    val surfaceContainerHigh: Color
    val surfaceContainerHighest: Color
    val rawOnBg: Color
    val rawOnSurfaceVariant: Color
    val outline: Color
    val outlineVariant: Color

    when {
        !isDark -> {
            background = Color(0xFFF8FAFC)
            surface = Color(0xFFFFFFFF)
            surfaceVariant = lerp(Color(0xFFEFF4F9), rawPrimary, 0.06f)
            surfaceContainerLowest = Color(0xFFFFFFFF)
            surfaceContainerLow = lerp(Color(0xFFF1F5F9), rawPrimary, 0.03f)
            surfaceContainer = lerp(Color(0xFFE2E8F0), rawPrimary, 0.05f)
            surfaceContainerHigh = lerp(Color(0xFFCBD5E1), rawPrimary, 0.06f)
            surfaceContainerHighest = lerp(Color(0xFFB8C4D4), rawPrimary, 0.08f)
            rawOnBg = Color(0xFF0F172A)
            rawOnSurfaceVariant = Color(0xFF334155)
            outline = Color(0xFF64748B).ensureReadableOn(background, 3.2f)
            outlineVariant = Color(0xFFCBD5E1)
        }
        isPureOled -> {
            background = Color.Black
            surface = Color.Black
            surfaceVariant = lerp(Color(0xFF0D121C), rawPrimary, 0.08f)
            surfaceContainerLowest = Color.Black
            surfaceContainerLow = lerp(Color(0xFF06090F), rawPrimary, 0.04f)
            surfaceContainer = lerp(Color(0xFF0B101A), rawPrimary, 0.06f)
            surfaceContainerHigh = lerp(Color(0xFF121926), rawPrimary, 0.08f)
            surfaceContainerHighest = lerp(Color(0xFF1B2436), rawPrimary, 0.10f)
            rawOnBg = Color(0xFFF8FAFC)
            rawOnSurfaceVariant = Color(0xFFCBD5E1)
            outline = lerp(Color(0xFF334155), rawPrimary, 0.22f)
            outlineVariant = Color(0xFF1E293B)
        }
        else -> {
            // DARK (Non-OLED): Soft slate-obsidian background (#0C111B) clearly distinct from #000000 OLED
            background = Color(0xFF0C111B)
            surface = Color(0xFF111827)
            surfaceVariant = lerp(Color(0xFF1E293B), rawPrimary, 0.08f)
            surfaceContainerLowest = Color(0xFF080C14)
            surfaceContainerLow = lerp(Color(0xFF131C2E), rawPrimary, 0.04f)
            surfaceContainer = lerp(Color(0xFF182338), rawPrimary, 0.06f)
            surfaceContainerHigh = lerp(Color(0xFF1F2D46), rawPrimary, 0.08f)
            surfaceContainerHighest = lerp(Color(0xFF293A56), rawPrimary, 0.10f)
            rawOnBg = Color(0xFFF3F6FC)
            rawOnSurfaceVariant = Color(0xFFCBD5E1)
            outline = lerp(Color(0xFF3B4A66), rawPrimary, 0.22f)
            outlineVariant = Color(0xFF253147)
        }
    }

    // Ensure primary & secondary accents themselves are readable when used as text/icons on surfaces
    val primary = rawPrimary.ensureReadableOn(surface, if (isDark) 4.5f else 4.2f)
    val secondary = rawSecondary.ensureReadableOn(surface, if (isDark) 4.3f else 4.0f)
    val tertiary = rawTertiary.ensureReadableOn(surface, 4.0f)

    // Compute balanced containers from the surface + accent
    val primaryContainer = monetScheme?.primaryContainer
        ?: if (isDark) lerp(surfaceContainerHigh, primary, 0.28f) else lerp(Color.White, primary, 0.18f)
    val secondaryContainer = monetScheme?.secondaryContainer
        ?: if (isDark) lerp(surfaceContainerHigh, secondary, 0.25f) else lerp(Color.White, secondary, 0.16f)
    val tertiaryContainer = monetScheme?.tertiaryContainer
        ?: if (isDark) lerp(surfaceContainerHigh, tertiary, 0.24f) else lerp(Color.White, tertiary, 0.16f)

    // Algorithmic auto-contrast for every "on*" role
    val onPrimary = primary.autoContrastTextColor(minContrast = 4.8f)
    val onPrimaryContainer = (monetScheme?.onPrimaryContainer ?: primary)
        .ensureReadableOn(primaryContainer, minContrast = 4.8f)

    val onSecondary = secondary.autoContrastTextColor(minContrast = 4.8f)
    val onSecondaryContainer = (monetScheme?.onSecondaryContainer ?: secondary)
        .ensureReadableOn(secondaryContainer, minContrast = 4.8f)

    val onTertiary = tertiary.autoContrastTextColor(minContrast = 4.8f)
    val onTertiaryContainer = (monetScheme?.onTertiaryContainer ?: tertiary)
        .ensureReadableOn(tertiaryContainer, minContrast = 4.8f)

    val onBackground = rawOnBg.ensureReadableOn(background, minContrast = 7.0f)
    val onSurface = rawOnBg.ensureReadableOn(surface, minContrast = 7.0f)
    val onSurfaceVariant = rawOnSurfaceVariant
        .ensureReadableOn(surfaceVariant, minContrast = 4.8f)
        .ensureReadableOn(surfaceContainer, minContrast = 4.8f)

    return if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceContainerLowest = surfaceContainerLowest,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainer = surfaceContainer,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
            outline = outline,
            outlineVariant = outlineVariant
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceContainerLowest = surfaceContainerLowest,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainer = surfaceContainer,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
            outline = outline,
            outlineVariant = outlineVariant
        )
    }
}

@Composable
fun NegaiTheme(
    themeMode: AppThemeMode = AppThemeMode.AUTO_OLED,
    accentColor: AppAccentColor = AppAccentColor.CYAN,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemInDark = isSystemInDarkTheme()
    val isDark = themeMode.resolveIsDark(systemInDark)

    val dynamicDark = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && accentColor == AppAccentColor.MONET) {
        dynamicDarkColorScheme(context)
    } else {
        null
    }
    val dynamicLight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && accentColor == AppAccentColor.MONET) {
        dynamicLightColorScheme(context)
    } else {
        null
    }

    val colorScheme = remember(themeMode, accentColor, systemInDark, dynamicDark, dynamicLight) {
        buildNegaiColorScheme(
            themeMode = themeMode,
            accentColor = accentColor,
            isSystemInDarkTheme = systemInDark,
            dynamicDark = dynamicDark,
            dynamicLight = dynamicLight
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = NegaiShapes,
        content = content
    )
}

@Composable
fun Kino4KTheme(
    themeMode: AppThemeMode = AppThemeMode.AUTO_OLED,
    accentColor: AppAccentColor = AppAccentColor.CYAN,
    content: @Composable () -> Unit
) = NegaiTheme(
    themeMode = themeMode,
    accentColor = accentColor,
    content = content
)
