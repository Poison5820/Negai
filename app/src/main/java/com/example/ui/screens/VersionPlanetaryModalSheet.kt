package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.ensureReadableOn
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppVersionInfo
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.tr
import com.example.ui.theme.NegaiBrandFontFamily
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin

@Immutable
data class PlanetaryFeatureOrbit(
    val index: Int,
    val badge: String,
    val titleRu: String,
    val titleEn: String,
    val subtitleRu: String,
    val subtitleEn: String,
    val specsRu: String,
    val specsEn: String,
    val color: Color,
    val orbitRadiusFraction: Float,
    val speedMultiplier: Float,
    val initialPhaseRad: Float,
    val planetRadiusDp: Float,
    val hasMoon: Boolean,
    val icon: ImageVector
)

private val PLANETARY_FEATURES = listOf(
    PlanetaryFeatureOrbit(
        index = 0,
        badge = "GLSL • Anime4K",
        titleRu = "Апскейлинг Anime4K в реальном времени",
        titleEn = "Real-Time Anime4K GLSL Upscaling",
        subtitleRu = "Шейдерный конвейер по архитектуре bloc97/Anime4K (Clamp_Highlights, Mode C Fast, Mode B CNN, Mode A CNNX) и живое сравнение До/После",
        subtitleEn = "Shader pipeline based on bloc97/Anime4K (Clamp_Highlights, Mode C Fast, Mode B CNN, Mode A CNNX) & live split-screen compare",
        specsRu = "OpenGL ES & Vulkan API • Mode A/B/C • MIT License (bloc97/Anime4K)",
        specsEn = "OpenGL ES & Vulkan API • Mode A/B/C • MIT License (bloc97/Anime4K)",
        color = Color(0xFF00E5FF),
        orbitRadiusFraction = 0.28f,
        speedMultiplier = 1.6f,
        initialPhaseRad = 0.4f,
        planetRadiusDp = 5.5f,
        hasMoon = false,
        icon = Icons.Default.AutoAwesome
    ),
    PlanetaryFeatureOrbit(
        index = 1,
        badge = "MEMC • LTPO",
        titleRu = "Интерполяция кадров MEMC и адаптивная герцовка",
        titleEn = "MEMC Frame Interpolation & LTPO Cadence",
        subtitleRu = "Собственная реализация двунаправленного фазового варпинга до 165 Гц с защитой контуров 2D-анимации и детектором смены сцен",
        subtitleEn = "Custom in-house bidirectional phase-consistent motion warp up to 165Hz with 2D anime contour guard & scene-cut protection",
        specsRu = "Собственная реализация • 24/30 → 60–165 Гц • Highp векторы движения",
        specsEn = "In-House GLSL Pipeline • 24/30 → 60–165Hz • Highp motion vectors",
        color = Color(0xFFFF4D8D),
        orbitRadiusFraction = 0.48f,
        speedMultiplier = 1.1f,
        initialPhaseRad = 2.1f,
        planetRadiusDp = 6.5f,
        hasMoon = true,
        icon = Icons.Default.Speed
    ),
    PlanetaryFeatureOrbit(
        index = 2,
        badge = "DSP • De-Artifact",
        titleRu = "Очистка звука De-Artifact DSP и синхронизация ±5 с",
        titleEn = "Audio De-Artifact DSP, ±5s Sync & Boost",
        subtitleRu = "3-уровневая очистка сжатых дорожек AAC/MP3/Opus от ВЧ-звона и клиппинга, сдвиг ±5 с и пресеты задержки Bluetooth",
        subtitleEn = "3-level low-bitrate audio de-ringing, harmonic exciter & soft-knee de-clipper, ±5s sync & BT latency presets",
        specsRu = "DSP (Мягкий/Стандарт/Агрессивный) • Игровой режим BT (-45мс, -60мс, -150мс)",
        specsEn = "DSP (Soft/Standard/Aggressive) • BT Gaming Presets (-45ms, -60ms, -150ms)",
        color = Color(0xFF69F0AE),
        orbitRadiusFraction = 0.68f,
        speedMultiplier = -0.8f,
        initialPhaseRad = 3.9f,
        planetRadiusDp = 6.0f,
        hasMoon = false,
        icon = Icons.Default.GraphicEq
    ),
    PlanetaryFeatureOrbit(
        index = 3,
        badge = "TorrServer • MKV",
        titleRu = "TorrServer P2P, встроенные главы и субтитры ASS/SRT",
        titleEn = "TorrServer P2P, Embedded Chapters & ASS/SRT",
        subtitleRu = "Прямой стриминг торрентов в 4K, парсер глав Matroska EBML / MP4 Nero, раздельные озвучки и субтитры",
        subtitleEn = "Direct 4K torrent streaming, Matroska EBML / MP4 Nero chapter parser, voiceover parsing & dedicated Episodes/Chapters",
        specsRu = "HTTP Range • DoH • Детектор озвучек (RUS/Дубляж/MVO) • Серии / Главы",
        specsEn = "HTTP Range • DoH • Voiceover Parser (RUS/Dub/MVO) • Episodes / Chapters UI",
        color = Color(0xFFFFAB40),
        orbitRadiusFraction = 0.88f,
        speedMultiplier = 0.55f,
        initialPhaseRad = 5.2f,
        planetRadiusDp = 7.0f,
        hasMoon = true,
        icon = Icons.Default.CloudDownload
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VersionPlanetaryModalSheet(
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    var selectedOrbitIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedFeature = PLANETARY_FEATURES.getOrElse(selectedOrbitIndex) { PLANETARY_FEATURES.first() }

    val uriHandler = LocalUriHandler.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.testTag("version_planetary_modal_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "negai",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = NegaiBrandFontFamily,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.55f))
                    ) {
                        Text(
                            text = AppVersionInfo.BADGE_TEXT,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Surface(
                    onClick = {
                        runCatching { uriHandler.openUri(AppVersionInfo.GITHUB_REPO_URL) }
                    },
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.testTag("planetary_github_repo_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = tr(
                                "GitHub • #${AppVersionInfo.VERSION_CODE}",
                                "GitHub • #${AppVersionInfo.VERSION_CODE}"
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Minimalist Planetary System Orbital Animation Card
            Surface(
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, selectedFeature.color.copy(alpha = 0.45f)),
                color = Color(0xFF060911),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(228.dp)
                    .testTag("planetary_system_card")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    PlanetarySystemMinimalCanvas(
                        orbits = PLANETARY_FEATURES,
                        selectedOrbitIndex = selectedOrbitIndex,
                        onSelectOrbit = { selectedOrbitIndex = it },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Top-left minimal orbital legend badge
                    Surface(
                        color = Color(0xAA0D1322),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.14f)),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = tr("ОРБИТАЛЬНАЯ АРХИТЕКТУРА ЯДРА", "CORE ORBITAL ARCHITECTURE"),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                letterSpacing = 0.9.sp
                            ),
                            color = Color.White.copy(alpha = 0.72f),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Bottom-right active planet indicator pill
                    Surface(
                        color = Color(0xCC0D1322),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, selectedFeature.color.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(selectedFeature.color)
                            )
                            Text(
                                text = selectedFeature.badge,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Orbit selector pills
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PLANETARY_FEATURES.forEach { orbit ->
                    val isSelected = orbit.index == selectedOrbitIndex
                    val baseSurface = MaterialTheme.colorScheme.surfaceContainerLowest
                    val chipBg = if (isSelected) {
                        androidx.compose.ui.graphics.lerp(baseSurface, orbit.color, 0.16f)
                    } else {
                        baseSurface
                    }
                    val readableOrbitColor = orbit.color.ensureReadableOn(chipBg, minContrast = 5.2f)
                    Surface(
                        color = chipBg,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            if (isSelected) 1.dp else 0.8.dp,
                            if (isSelected) readableOrbitColor else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedOrbitIndex = orbit.index }
                            .testTag("planetary_orbit_chip_${orbit.index}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(readableOrbitColor)
                            )
                            Text(
                                text = orbit.badge,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) readableOrbitColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Highlighted Selected Orbit Detail Card
            val detailCardBg = MaterialTheme.colorScheme.surfaceContainerLowest
            val readableSelectedColor = selectedFeature.color.ensureReadableOn(detailCardBg, minContrast = 5.2f)
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.9.dp, readableSelectedColor.copy(alpha = 0.65f)),
                colors = CardDefaults.cardColors(
                    containerColor = detailCardBg
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = selectedFeature.icon,
                            contentDescription = null,
                            tint = readableSelectedColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isEnglish) selectedFeature.titleEn else selectedFeature.titleRu,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = if (isEnglish) selectedFeature.subtitleEn else selectedFeature.subtitleRu,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (isEnglish) selectedFeature.specsEn else selectedFeature.specsRu,
                        style = MaterialTheme.typography.labelSmall,
                        color = readableSelectedColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Full Capabilities List
            Text(
                text = tr("Основные возможности плеера", "Core Player Capabilities"),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PLANETARY_FEATURES.forEach { feature ->
                val isSelected = feature.index == selectedOrbitIndex
                val baseCardBg = MaterialTheme.colorScheme.surfaceContainerLowest
                val cardBg = if (isSelected) {
                    androidx.compose.ui.graphics.lerp(baseCardBg, feature.color, 0.10f)
                } else {
                    baseCardBg
                }
                val iconCircleBg = androidx.compose.ui.graphics.lerp(cardBg, feature.color, 0.18f)
                val readableFeatureColor = feature.color
                    .ensureReadableOn(iconCircleBg, minContrast = 4.8f)
                    .ensureReadableOn(cardBg, minContrast = 5.2f)
                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        0.8.dp,
                        if (isSelected) readableFeatureColor.copy(alpha = 0.70f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedOrbitIndex = feature.index }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(iconCircleBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = feature.icon,
                                contentDescription = null,
                                tint = readableFeatureColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isEnglish) feature.titleEn else feature.titleRu,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isEnglish) feature.specsEn else feature.specsRu,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Open Source Attribution & Technology Credits Card (Anime4K MIT + Custom MEMC)
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.42f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("planetary_licenses_and_credits_card")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = tr(
                            "Исходные репозитории и лицензии технологий",
                            "Open Source Repositories & Technology Licenses"
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // 1. Anime4K Attribution (bloc97/Anime4K - MIT License)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = tr(
                                "• Апскейлинг Anime4K — основано на алгоритмах и пресетах Mode A / B / C проекта bloc97/Anime4K, адаптированных и оптимизированных под мобильные GPU Android (OpenGL ES 2.0 и Vulkan API).",
                                "• Anime4K Upscaling — based on the Mode A / B / C algorithms and presets from bloc97/Anime4K, adapted and optimized for Android mobile GPUs (OpenGL ES 2.0 & Vulkan API)."
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tr(
                                "Лицензия: MIT License (Copyright © 2019–2021 bloc97)",
                                "License: MIT License (Copyright © 2019–2021 bloc97)"
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Surface(
                            onClick = {
                                runCatching { uriHandler.openUri(AppVersionInfo.ANIME4K_ORIGINAL_REPO_URL) }
                            },
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
                            modifier = Modifier.testTag("planetary_anime4k_repo_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "github.com/bloc97/Anime4K (MIT)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // 2. MEMC Frame Interpolation Attribution (In-House Negai Implementation)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = tr(
                                "• Интерполяция кадров MEMC (Motion Estimation / Motion Compensation) — собственная разработка Negai на базе 3-проходного конвейера OpenGL ES 2.0 и многопоточного Vulkan API (иерархический поиск векторов движения по яркости, сглаживание поля скоростей с детектором смены сцен и двунаправленный фазово-согласованный варпинг с защитой контуров 2D-анимации).",
                                "• MEMC Frame Interpolation (Motion Estimation / Motion Compensation) — custom in-house Negai implementation built on a 3-pass OpenGL ES 2.0 and multi-threaded Vulkan API pipeline (hierarchical luminance block motion estimation, vector field smoothing with scene-cut detection, and bidirectional phase-consistent warp with 2D anime contour guard)."
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tr(
                                "Статус: Собственная реализация Negai (без сторонних проприетарных библиотек)",
                                "Status: Original in-house Negai implementation (no third-party proprietary libraries)"
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Release History Card (3.4.3-pre, 3.4.2-pre, 3.4-pre1)
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("planetary_release_history_card")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = tr("История сборок (3.4.x Prerelease)", "Release History (3.4.x Prerelease)"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    AppVersionInfo.RELEASE_HISTORY.forEachIndexed { idx, entry ->
                        val isCurrent = idx == 0
                        Surface(
                            color = if (isCurrent) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.32f)
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerLow
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                0.7.dp,
                                if (isCurrent) MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                                else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${entry.versionName} (build ${entry.versionCode})",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "• ${entry.tag}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = if (isEnglish) entry.summaryEn else entry.summaryRu,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.90f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanetarySystemMinimalCanvas(
    orbits: List<PlanetaryFeatureOrbit>,
    selectedOrbitIndex: Int,
    onSelectOrbit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "planetary_orbit_transition")
    val masterPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "master_orbital_phase"
    )
    val corePulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_star_pulse"
    )

    // Precomputed deterministic starlight coordinates in normalized [0..1] space
    val stars = remember {
        listOf(
            Triple(0.11f, 0.18f, 1.3f),
            Triple(0.24f, 0.78f, 1.0f),
            Triple(0.38f, 0.14f, 1.5f),
            Triple(0.67f, 0.21f, 1.2f),
            Triple(0.84f, 0.16f, 1.7f),
            Triple(0.91f, 0.64f, 1.1f),
            Triple(0.76f, 0.84f, 1.4f),
            Triple(0.16f, 0.52f, 1.2f),
            Triple(0.54f, 0.88f, 1.0f),
            Triple(0.88f, 0.36f, 1.3f)
        )
    }

    Canvas(
        modifier = modifier.pointerInput(orbits) {
            detectTapGestures { tapOffset ->
                val cx = size.width / 2f
                val cy = size.height / 2f
                val maxR = min(size.width, size.height) * 0.44f
                val tapDist = hypot(tapOffset.x - cx, tapOffset.y - cy)
                val closest = orbits.minByOrNull { orbit ->
                    kotlin.math.abs(tapDist - orbit.orbitRadiusFraction * maxR)
                }
                if (closest != null) {
                    onSelectOrbit(closest.index)
                }
            }
        }
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val center = Offset(cx, cy)
        val maxOrbitRadius = min(size.width, size.height) * 0.44f

        // 1. Subtle deep-space radial nebula glow
        val activeColor = orbits.getOrNull(selectedOrbitIndex)?.color ?: Color(0xFF00E5FF)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    activeColor.copy(alpha = 0.16f * corePulse),
                    Color(0xFF0B1426).copy(alpha = 0.35f),
                    Color.Transparent
                ),
                center = center,
                radius = maxOrbitRadius * 1.15f
            ),
            radius = maxOrbitRadius * 1.15f,
            center = center
        )

        // 2. Minimalist background starfield
        stars.forEachIndexed { idx, (nx, ny, radiusDp) ->
            val alpha = if (idx % 2 == 0) 0.25f * corePulse else 0.18f
            drawCircle(
                color = Color.White.copy(alpha = alpha.coerceIn(0.08f, 0.45f)),
                radius = radiusDp.dp.toPx(),
                center = Offset(nx * size.width, ny * size.height)
            )
        }

        // 3. Crosshair minimalist axis ticks
        val axisColor = Color.White.copy(alpha = 0.05f)
        drawLine(
            color = axisColor,
            start = Offset(cx - maxOrbitRadius * 1.05f, cy),
            end = Offset(cx + maxOrbitRadius * 1.05f, cy),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = axisColor,
            start = Offset(cx, cy - maxOrbitRadius * 1.05f),
            end = Offset(cx, cy + maxOrbitRadius * 1.05f),
            strokeWidth = 1.dp.toPx()
        )

        // 4. Orbital rings, trailing arcs, planets, and moons
        orbits.forEach { orbit ->
            val isSelected = orbit.index == selectedOrbitIndex
            val orbitRadius = maxOrbitRadius * orbit.orbitRadiusFraction
            val ringAlpha = if (isSelected) 0.42f else 0.14f
            val ringStroke = if (isSelected) 1.5.dp.toPx() else 1.dp.toPx()

            // Base minimalist orbit circle
            drawCircle(
                color = if (isSelected) orbit.color.copy(alpha = ringAlpha) else Color.White.copy(alpha = ringAlpha),
                radius = orbitRadius,
                center = center,
                style = Stroke(width = ringStroke)
            )

            // Current planet angle in radians
            val angleRad = orbit.initialPhaseRad + masterPhase * orbit.speedMultiplier
            val px = cx + orbitRadius * cos(angleRad)
            val py = cy + orbitRadius * sin(angleRad)
            val planetCenter = Offset(px, py)

            // Subtle orbital wake arc behind the planet
            val degAngle = Math.toDegrees(angleRad.toDouble()).toFloat()
            val sweepDeg = if (orbit.speedMultiplier >= 0f) -42f else 42f
            drawArc(
                color = orbit.color.copy(alpha = if (isSelected) 0.65f else 0.32f),
                startAngle = degAngle,
                sweepAngle = sweepDeg,
                useCenter = false,
                topLeft = Offset(cx - orbitRadius, cy - orbitRadius),
                size = androidx.compose.ui.geometry.Size(orbitRadius * 2f, orbitRadius * 2f),
                style = Stroke(width = (ringStroke * 1.6f), cap = StrokeCap.Round)
            )

            // Planet outer halo glow
            val pRadiusPx = orbit.planetRadiusDp.dp.toPx() * (if (isSelected) 1.22f else 1.0f)
            drawCircle(
                color = orbit.color.copy(alpha = if (isSelected) 0.34f else 0.16f),
                radius = pRadiusPx * 2.3f,
                center = planetCenter
            )

            // Planet body
            drawCircle(
                color = orbit.color,
                radius = pRadiusPx,
                center = planetCenter
            )
            // Specular highlight core on planet
            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                radius = pRadiusPx * 0.35f,
                center = Offset(px - pRadiusPx * 0.25f, py - pRadiusPx * 0.25f)
            )

            // Optional minimalist satellite moon
            if (orbit.hasMoon) {
                val moonOrbitR = pRadiusPx * 2.5f
                val moonAngle = angleRad * 3.2f
                val mx = px + moonOrbitR * cos(moonAngle)
                val my = py + moonOrbitR * sin(moonAngle)
                drawCircle(
                    color = Color.White.copy(alpha = 0.18f),
                    radius = moonOrbitR,
                    center = planetCenter,
                    style = Stroke(width = 0.7.dp.toPx())
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = 1.8.dp.toPx(),
                    center = Offset(mx, my)
                )
            }
        }

        // 5. Central Star ("negai" core)
        val starRadius = 11.dp.toPx()
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White,
                    activeColor.copy(alpha = 0.75f),
                    Color.Transparent
                ),
                center = center,
                radius = starRadius * 2.8f * corePulse
            ),
            radius = starRadius * 2.8f * corePulse,
            center = center
        )
        drawCircle(
            color = Color(0xFF0A101D),
            radius = starRadius,
            center = center
        )
        drawCircle(
            color = activeColor,
            radius = starRadius,
            center = center,
            style = Stroke(width = 1.8.dp.toPx())
        )
        drawCircle(
            color = Color.White,
            radius = 3.5.dp.toPx(),
            center = center
        )
    }
}
