package com.example.ui.theme

import android.app.Activity
import android.os.Build
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.model.AppThemeMode

// 1. Pure #000000 AMOLED / OLED Theme (Electric Cyan)
private val PureOledCyanScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = Color(0xFF002A32),
    primaryContainer = Color(0xFF002B33),
    onPrimaryContainer = OnElectricCyanContainer,
    secondary = SakuraCoral,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF440B22),
    onSecondaryContainer = OnSakuraCoralContainer,
    tertiary = AmberHighlight,
    onTertiary = Color(0xFF2B1D00),
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = OnAmberContainer,
    background = Color.Black,
    onBackground = Color(0xFFF5F8FF),
    surface = Color.Black,
    onSurface = Color(0xFFF5F8FF),
    surfaceVariant = Color(0xFF0D1118),
    onSurfaceVariant = Color(0xFFB0BCD6),
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF05070B),
    surfaceContainer = Color(0xFF0B0E15),
    surfaceContainerHigh = Color(0xFF121722),
    surfaceContainerHighest = Color(0xFF1A2232),
    outline = Color(0xFF2B364C),
    outlineVariant = Color(0xFF1A2232)
)

private val OledLightCyanScheme = lightColorScheme(
    primary = Color(0xFF00838F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB2EBF2),
    onPrimaryContainer = Color(0xFF00363A),
    secondary = Color(0xFF00ACC1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = Color(0xFF004D40),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF334155),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFE2E8F0),
    surfaceContainerHigh = Color(0xFFCBD5E1),
    surfaceContainerHighest = Color(0xFF94A3B8),
    outline = Color(0xFF64748B),
    outlineVariant = Color(0xFFCBD5E1)
)

// 2. Cyber-Sakura Pure #000000 OLED Theme
private val PureOledSakuraScheme = darkColorScheme(
    primary = SakuraCoral,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4D0D27),
    onPrimaryContainer = Color(0xFFFFD9E3),
    secondary = ElectricCyan,
    onSecondary = Color(0xFF002A32),
    secondaryContainer = Color(0xFF00313A),
    onSecondaryContainer = OnElectricCyanContainer,
    tertiary = Color(0xFFC792EA),
    onTertiary = Color(0xFF290942),
    background = Color.Black,
    onBackground = Color(0xFFFFF5F8),
    surface = Color.Black,
    onSurface = Color(0xFFFFF5F8),
    surfaceVariant = Color(0xFF130B14),
    onSurfaceVariant = Color(0xFFD6B4C6),
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF080409),
    surfaceContainer = Color(0xFF100912),
    surfaceContainerHigh = Color(0xFF19101C),
    surfaceContainerHighest = Color(0xFF241729),
    outline = Color(0xFF3E2645),
    outlineVariant = Color(0xFF26172B)
)

private val SakuraLightScheme = lightColorScheme(
    primary = Color(0xFFE11D48),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD1DC),
    onPrimaryContainer = Color(0xFF4C0519),
    secondary = Color(0xFFBE123C),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE4E6),
    onSecondaryContainer = Color(0xFF881337),
    background = Color(0xFFFFF1F5),
    onBackground = Color(0xFF1F0914),
    surface = Color.White,
    onSurface = Color(0xFF1F0914),
    surfaceVariant = Color(0xFFFFE4E6),
    onSurfaceVariant = Color(0xFF881337),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFFF1F5),
    surfaceContainer = Color(0xFFFECDD3),
    surfaceContainerHigh = Color(0xFFFDA4AF),
    surfaceContainerHighest = Color(0xFFF43F5E),
    outline = Color(0xFFE11D48),
    outlineVariant = Color(0xFFFECDD3)
)

// 3. Obsidian Cinema Dark Theme
private val ObsidianDarkScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = Color(0xFF002A32),
    primaryContainer = ElectricCyanContainer,
    onPrimaryContainer = OnElectricCyanContainer,
    secondary = SakuraCoral,
    onSecondary = Color.White,
    secondaryContainer = SakuraCoralContainer,
    onSecondaryContainer = OnSakuraCoralContainer,
    tertiary = AmberHighlight,
    onTertiary = Color(0xFF2B1D00),
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = OnAmberContainer,
    background = ObsidianBg,
    onBackground = TextPrimaryDark,
    surface = ObsidianSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ObsidianSurfaceHigh,
    onSurfaceVariant = TextSecondaryDark,
    surfaceContainerLowest = Color(0xFF07090E),
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianSurfaceHigh,
    surfaceContainerHigh = ObsidianSurfaceHighest,
    surfaceContainerHighest = Color(0xFF2C374F),
    outline = OutlineDark,
    outlineVariant = Color(0xFF232C40)
)

private val DeepNavyLightScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF0369A1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBAE6FD),
    onSecondaryContainer = Color(0xFF075985),
    background = Color(0xFFF0F4FA),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF334155),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF0F4FA),
    surfaceContainer = Color(0xFFE2E8F0),
    surfaceContainerHigh = Color(0xFFCBD5E1),
    surfaceContainerHighest = Color(0xFF94A3B8),
    outline = Color(0xFF64748B),
    outlineVariant = Color(0xFFCBD5E1)
)

// 4. Cyber Mint Theme
private val CyberMintDarkScheme = darkColorScheme(
    primary = Color(0xFF00E676),
    onPrimary = Color(0xFF003819),
    primaryContainer = Color(0xFF005227),
    onPrimaryContainer = Color(0xFFB9F6CA),
    secondary = Color(0xFF69F0AE),
    onSecondary = Color(0xFF003819),
    background = Color(0xFF061412),
    onBackground = Color(0xFFE8F5E9),
    surface = Color(0xFF0A1F1C),
    onSurface = Color(0xFFE8F5E9),
    surfaceVariant = Color(0xFF112E2A),
    onSurfaceVariant = Color(0xFFA7F3D0),
    surfaceContainerLow = Color(0xFF0A1F1C),
    surfaceContainer = Color(0xFF112E2A),
    surfaceContainerHigh = Color(0xFF1A403A),
    surfaceContainerHighest = Color(0xFF23524B),
    outline = Color(0xFF00E676),
    outlineVariant = Color(0xFF112E2A)
)

private val CyberMintLightScheme = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF10B981),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA7F3D0),
    onSecondaryContainer = Color(0xFF065F46),
    background = Color(0xFFF0FDF4),
    onBackground = Color(0xFF022C22),
    surface = Color.White,
    onSurface = Color(0xFF022C22),
    surfaceVariant = Color(0xFFDCFCE7),
    onSurfaceVariant = Color(0xFF065F46),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF0FDF4),
    surfaceContainer = Color(0xFFBBF7D0),
    surfaceContainerHigh = Color(0xFF86EFAC),
    surfaceContainerHighest = Color(0xFF4ADE80),
    outline = Color(0xFF059669),
    outlineVariant = Color(0xFFBBF7D0)
)

// 5. Slate Grey Theme
private val SlateGreyDarkScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF312E81),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF818CF8),
    onSecondary = Color(0xFF1E1B4B),
    background = Color(0xFF111827),
    onBackground = Color(0xFFF9FAFB),
    surface = Color(0xFF1F2937),
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = Color(0xFF374151),
    onSurfaceVariant = Color(0xFFD1D5DB),
    surfaceContainerLow = Color(0xFF1F2937),
    surfaceContainer = Color(0xFF374151),
    surfaceContainerHigh = Color(0xFF4B5563),
    surfaceContainerHighest = Color(0xFF6B7280),
    outline = Color(0xFF6366F1),
    outlineVariant = Color(0xFF374151)
)

private val SlateGreyLightScheme = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF1E1B4B),
    secondary = Color(0xFF6366F1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC7D2FE),
    onSecondaryContainer = Color(0xFF312E81),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE0E7FF),
    onSurfaceVariant = Color(0xFF3730A3),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFC7D2FE),
    surfaceContainerHigh = Color(0xFFA5B4FC),
    surfaceContainerHighest = Color(0xFF818CF8),
    outline = Color(0xFF4F46E5),
    outlineVariant = Color(0xFFC7D2FE)
)

val NegaiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

val KinoShapes = NegaiShapes

@Composable
fun NegaiTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM_AUTO,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemInDark = androidx.compose.foundation.isSystemInDarkTheme()

    val effectiveMode = if (themeMode == AppThemeMode.SYSTEM_AUTO) {
        if (systemInDark) AppThemeMode.OLED_PITCH_BLACK_DARK else AppThemeMode.OLED_PITCH_BLACK_LIGHT
    } else {
        themeMode
    }

    val isDark = when (themeMode) {
        AppThemeMode.SYSTEM_AUTO -> systemInDark
        AppThemeMode.MATERIAL_YOU -> systemInDark
        else -> effectiveMode.isDark
    }

    val colorScheme: ColorScheme = when (effectiveMode) {
        AppThemeMode.SYSTEM_AUTO -> if (systemInDark) PureOledCyanScheme else OledLightCyanScheme
        AppThemeMode.MATERIAL_YOU -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (systemInDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (systemInDark) PureOledCyanScheme else OledLightCyanScheme
            }
        }
        AppThemeMode.OLED_PITCH_BLACK_DARK -> PureOledCyanScheme
        AppThemeMode.OLED_PITCH_BLACK_LIGHT -> OledLightCyanScheme
        AppThemeMode.DEEP_NAVY_DARK -> ObsidianDarkScheme
        AppThemeMode.DEEP_NAVY_LIGHT -> DeepNavyLightScheme
        AppThemeMode.SAKURA_PURPLE_DARK -> PureOledSakuraScheme
        AppThemeMode.SAKURA_PURPLE_LIGHT -> SakuraLightScheme
        AppThemeMode.CYBER_MINT_DARK -> CyberMintDarkScheme
        AppThemeMode.CYBER_MINT_LIGHT -> CyberMintLightScheme
        AppThemeMode.SLATE_GREY_DARK -> SlateGreyDarkScheme
        AppThemeMode.SLATE_GREY_LIGHT -> SlateGreyLightScheme
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
    themeMode: AppThemeMode = AppThemeMode.SYSTEM_AUTO,
    content: @Composable () -> Unit
) = NegaiTheme(themeMode = themeMode, content = content)


