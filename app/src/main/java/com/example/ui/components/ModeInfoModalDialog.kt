package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.tr

enum class ModeInfoTopic(
    val tagSuffix: String,
    val icon: ImageVector
) {
    ANIME4K("anime4k", Icons.Default.AutoAwesome),
    FRAME_INTERPOLATION("memc", Icons.Default.MotionPhotosOn),
    AUDIO_CLEANUP("audio_cleanup", Icons.Default.GraphicEq),
    GRAPHICS_API("graphics_api", Icons.Default.DeveloperBoard),
    DECODER("decoder", Icons.Default.Memory)
}

data class ModeInfoItemSpec(
    val title: String,
    val badge: String,
    val badgeColor: Color,
    val summary: String,
    val details: String,
    val hardwareNote: String
)

@Composable
fun ModeInfoIconButton(
    topic: ModeInfoTopic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val isEnglish = LocalIsEnglish.current
    Surface(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .testTag("info_btn_${topic.tagSuffix}"),
        shape = CircleShape,
        color = tint.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.38f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = if (isEnglish) "Detailed modes info" else "Подробная справка о режимах",
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ModeInfoModalDialog(
    topic: ModeInfoTopic,
    onDismiss: () -> Unit
) {
    val isEnglish = LocalIsEnglish.current
    val scrollState = rememberScrollState()

    val dialogTitle = when (topic) {
        ModeInfoTopic.ANIME4K -> tr(
            "Режимы апскейла Anime4K v4.0",
            "Anime4K v4.0 Upscale Modes"
        )
        ModeInfoTopic.FRAME_INTERPOLATION -> tr(
            "Интерполяция кадров (MEMC)",
            "Frame Interpolation (MEMC)"
        )
        ModeInfoTopic.AUDIO_CLEANUP -> tr(
            "Очистка звука (DSP De-Artifact)",
            "Audio Cleanup (DSP De-Artifact)"
        )
        ModeInfoTopic.GRAPHICS_API -> tr(
            "Графические конвейеры (OpenGL ES / Vulkan)",
            "Graphics Pipelines (OpenGL ES / Vulkan)"
        )
        ModeInfoTopic.DECODER -> tr(
            "Видеодекодеры (HW+ / HW / SW)",
            "Video Decoders (HW+ / HW / SW)"
        )
    }

    val dialogIntro = when (topic) {
        ModeInfoTopic.ANIME4K -> tr(
            "Шейдерный конвейер реального времени bloc97/Anime4K (MIT). Восстанавливает чёткость контуров (Line Reconstruction), подавляет звон (De-Ringing) и блочность сжатия без мыла бикубического скейлинга.",
            "Real-time shader pipeline based on bloc97/Anime4K (MIT). Reconstructs line edges, removes compression ringing/aliasing, and upscales 2D animation cleanly."
        )
        ModeInfoTopic.FRAME_INTERPOLATION -> tr(
            "Адаптивный GPU-алгоритм уплавнения движения (Motion Estimation & Motion Compensation) с анализом люмы по Собелю, защитой субтитров и автоматическим пропуском склеек сцен (Scene-Cut Detection).",
            "Adaptive GPU motion estimation & compensation pipeline with Sobel luma gradient analysis, subtitle protection, and automatic scene-cut fallback."
        )
        ModeInfoTopic.AUDIO_CLEANUP -> tr(
            "Многополосный цифровой сигнальный процессор (DSP) реального времени для устранения металлических призвуков сжатия (AAC/Opus/MP3), цифрового клиппинга и фазовой грязи.",
            "Real-time multi-band digital signal processor (DSP) that suppresses lossy codec metallic ringing (AAC/Opus/MP3), clipping, and harsh high-frequency artifacts."
        )
        ModeInfoTopic.GRAPHICS_API -> tr(
            "Выбор низкоуровневого графического API определяет способ доставки декодированных видеокадров с MediaCodec на экран и планирование GPU-вычислений для шейдеров.",
            "Choosing the graphics backend controls how decoded MediaCodec video frames reach the GPU and how shader passes are scheduled across threads."
        )
        ModeInfoTopic.DECODER -> tr(
            "Режим декодирования управляет распределением нагрузки между выделенным аппаратным видео-блоком процессора (VPU/DSP) и ядрами центрального процессора (CPU).",
            "Decoder mode controls how video stream decompression is distributed between dedicated hardware video blocks (VPU/SoC) and CPU cores."
        )
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val items: List<ModeInfoItemSpec> = when (topic) {
        ModeInfoTopic.ANIME4K -> listOf(
            ModeInfoItemSpec(
                title = tr("Выкл (Оригинал)", "Off (Original)"),
                badge = "RAW",
                badgeColor = secondaryColor,
                summary = tr("Вывод исходного кадра 1:1 без шейдерной постобработки", "Direct 1:1 video frame output without shader passes"),
                details = tr(
                    "Кадр передаётся напрямую из декодера в текстуру отображения. Идеально для 4K/1080p фильмов с живыми актёрами или максимальной экономии заряда батареи.",
                    "Frames pass directly from decoder to display surface. Best for high-bitrate live-action movies or maximum battery saving."
                ),
                hardwareNote = tr("Нагрузка GPU: минимальная (~0%)", "GPU Load: minimal (~0%)")
            ),
            ModeInfoItemSpec(
                title = "Anime4K Fast (Mode C)",
                badge = "A4K FAST",
                badgeColor = primaryColor,
                summary = tr("Быстрый денойз и направленный апскейл без перегрева", "Fast denoise + directional upscale without thermal throttling"),
                details = tr(
                    "Реализует оригинальный пресет Mode C (Denoise + Upscale-CNN-M). Убирает москитный шум сжатия вокруг линий и мягко заостряет контуры за 1 проход. Не вызывает троттлинга даже при 60 к/с.",
                    "Implements bloc97 Mode C (Denoise + Upscale-CNN-M). Removes mosquito compression noise around lines and sharpens edges in a single lightweight pass."
                ),
                hardwareNote = tr("Рекомендуется для всех мобильных GPU (Adreno / Mali)", "Recommended for all mobile GPUs (Adreno / Mali)")
            ),
            ModeInfoItemSpec(
                title = "Anime4K HQ (Mode A)",
                badge = "CNN x2",
                badgeColor = primaryColor,
                summary = tr("Реконструкция тонких линий Restore-CNN-M + Upscale-CNN-M", "Restore-CNN-M line reconstruction + Upscale-CNN-M"),
                details = tr(
                    "Классический пресет Mode A для 720p/1080p аниме и веб-рипов. Сначала восстанавливает размытые сведением контуры рисовки, убирает ореолы (De-Blur) и затем выполняет субпиксельный апскейл.",
                    "Classic Mode A preset for 720p/1080p anime and web-rips. First reconstructs blurred line art and removes halos, then performs sub-pixel CNN upscaling."
                ),
                hardwareNote = tr("Нагрузка GPU: средняя • Оптимально для 1080p → 2K/4K", "GPU Load: medium • Ideal for 1080p → 2K/4K")
            ),
            ModeInfoItemSpec(
                title = "Anime4K Ultra (Mode A+A)",
                badge = "CNNX",
                badgeColor = tertiaryColor,
                summary = tr("Двухкаскадная реконструкция (Restore → Upscale → Restore)", "Dual-cascade reconstruction (Restore → Upscale → Restore)"),
                details = tr(
                    "Топовый пресет Mode A+A для старых SD/480p/720p релизов. Выполняет повторный проход реконструкции после масштабирования, делая линии векторно-чёткими даже на больших экранах планшетов.",
                    "Flagship Mode A+A preset for older SD/480p/720p releases. Runs a second restoration cascade after upscaling for razor-sharp line work on high-DPI screens."
                ),
                hardwareNote = tr("Нагрузка GPU: высокая • Лучше всего с Vulkan 1.1+", "GPU Load: high • Best paired with Vulkan 1.1+")
            )
        )

        ModeInfoTopic.FRAME_INTERPOLATION -> listOf(
            ModeInfoItemSpec(
                title = tr("Выкл (Оригинал)", "Off (Original)"),
                badge = "NATIVE",
                badgeColor = secondaryColor,
                summary = tr("Оригинальная частота кадров фильма/аниме (23.976 / 24 / 30 к/с)", "Original source frame rate (23.976 / 24 / 30 fps)"),
                details = tr(
                    "Кадры выводятся строго по таймштампам контейнера (PTS) без генерации промежуточных фаз. Сохраняет классический кинематографический ритм.",
                    "Frames are presented strictly by container PTS timestamps without intermediate frame synthesis. Preserves classic cinematic cadence."
                ),
                hardwareNote = tr("Нагрузка GPU: 0%", "GPU Load: 0%")
            ),
            ModeInfoItemSpec(
                title = tr("Мягкая 2× (Кино)", "Smooth 2× (Cinema)"),
                badge = "MEMC ×2",
                badgeColor = primaryColor,
                summary = tr("Удвоение частоты кадров (24→48 / 30→60 к/с) без эффекта мыльной оперы", "Doubles frame rate (24→48 / 30→60 fps) without soap-opera effect"),
                details = tr(
                    "Сглаживает рывки (judder) на длинных панорамах камеры, сохраняя естественную кинематографичность движений персонажей.",
                    "Removes camera pan judder while preserving natural cinematic motion weight without artificial over-smoothing."
                ),
                hardwareNote = tr("Нагрузка GPU: низкая", "GPU Load: low")
            ),
            ModeInfoItemSpec(
                title = tr("Плавная 60 fps", "Smooth 60 fps"),
                badge = "MEMC 60",
                badgeColor = primaryColor,
                summary = tr("Стабильное уплавнение любого видео до 60 к/с", "Consistent motion interpolation to 60 fps"),
                details = tr(
                    "Синхронизирует фазу движения под стандартные 60 Гц экраны и адаптивные LTPO панели. Включает детектор смены сцен, чтобы на резких склейках не возникало морфинга.",
                    "Synchronizes motion phase for 60 Hz displays and adaptive LTPO panels. Includes scene-cut detection to prevent morphing across cuts."
                ),
                hardwareNote = tr("Нагрузка GPU: средняя", "GPU Load: medium")
            ),
            ModeInfoItemSpec(
                title = tr("Максимум экрана (90–144 Гц)", "Display Max (90–144 Hz)"),
                badge = "MEMC MAX",
                badgeColor = tertiaryColor,
                summary = tr("Адаптация под максимальную герцовку дисплея (90 / 120 / 144 / 165 Гц)", "Matches native high-refresh display (90 / 120 / 144 / 165 Hz)"),
                details = tr(
                    "Генерирует промежуточные фазы на каждом VSync-цикле высокочастотного экрана. Обеспечивает максимальную плавность в динамичных экшен-сценах и спортивных трансляциях.",
                    "Synthesizes intermediate motion phases on every high-refresh VSync tick. Delivers ultra-fluid motion in fast action and sports."
                ),
                hardwareNote = tr("Требует экран 90+ Гц и производительный GPU", "Requires 90+ Hz display and capable GPU")
            ),
            ModeInfoItemSpec(
                title = tr("Аниме 2D (Анти-ореол)", "Anime 2D (Anti-Halo)"),
                badge = "MEMC 2D",
                badgeColor = tertiaryColor,
                summary = tr("Специальный профиль для 2D-анимации с защитой контуров и субтитров", "Specialized 2D animation profile with edge & subtitle protection"),
                details = tr(
                    "В аниме персонажи часто рисуются в 8–12 к/с (на 2-ках и 3-ках), а камера движется в 24 к/с. Этот режим анализирует градиенты контуров и подавляет рваные ореолы вокруг резких линий и титров.",
                    "In 2D anime, characters are often animated on 2s/3s (8–12 fps) while camera pans run at 24 fps. This mode protects dark ink lines and subtitles from halo tearing."
                ),
                hardwareNote = tr("Оптимально в паре с Anime4K Fast / HQ", "Pairs best with Anime4K Fast / HQ")
            )
        )

        ModeInfoTopic.AUDIO_CLEANUP -> listOf(
            ModeInfoItemSpec(
                title = tr("Выкл (Оригинал)", "Off (Original)"),
                badge = "RAW",
                badgeColor = secondaryColor,
                summary = tr("Прямой вывод PCM без фильтрации", "Bit-transparent PCM output without DSP filtering"),
                details = tr(
                    "Звуковой тракт работает без дополнительной фильтрации частот (кроме выбранного эквалайзера и громкости). Лучший выбор для Lossless (FLAC / TrueHD / PCM) дорожек.",
                    "Audio passes through without artifact filtering (aside from user EQ/gain). Ideal for high-bitrate or lossless FLAC/PCM tracks."
                ),
                hardwareNote = tr("Задержка: 0 мс", "Latency: 0 ms")
            ),
            ModeInfoItemSpec(
                title = tr("Мягкий (Тёплый ВЧ)", "Gentle (Warm HF)"),
                badge = "SOFT",
                badgeColor = primaryColor,
                summary = tr("Деликатное сглаживание «песка» и сибилянтов на 10–15 кГц", "Subtle smoothing of harshness and sibilance at 10–15 kHz"),
                details = tr(
                    "Убирает резкий цифровой «песок» на шипящих звуках (с/ц/щ) в любительских озвучках, сохраняя воздух и детализацию музыки.",
                    "Softens harsh digital sibilance in voiceovers while preserving musical air and soundstage detail."
                ),
                hardwareNote = tr("Для озвучек среднего битрейта (128–192 кбит/с)", "For medium-bitrate dubs (128–192 kbps)")
            ),
            ModeInfoItemSpec(
                title = tr("Стандарт (Де-артефакт)", "Standard (De-Artifact)"),
                badge = "DSP STD",
                badgeColor = primaryColor,
                summary = tr("Очистка сжатых AAC/Opus/MP3 дорожек + мягкий де-клиппер", "Cleans lossy AAC/Opus/MP3 ringing + soft de-clipper"),
                details = tr(
                    "Подавляет характерный «металлический звон» квантования в области 7–13 кГц, восстанавливает перегруженные пики голоса (De-Clip) и выравнивает разборчивость речи на 2–3.5 кГц.",
                    "Suppresses lossy quantization metallic ringing in the 7–13 kHz band, restores clipped vocal peaks, and enhances speech clarity at 2–3.5 kHz."
                ),
                hardwareNote = tr("Рекомендуется для онлайн-потоков и ТВ-рипов", "Recommended for web streams and TV rips")
            ),
            ModeInfoItemSpec(
                title = tr("Агрессивный (Старые озвучки)", "Aggressive (Old Dubs)"),
                badge = "DSP MAX",
                badgeColor = tertiaryColor,
                summary = tr("Максимальное подавление звона, гула 60 Гц и жёсткого перегруза", "Maximum suppression of metallic ringing, 60 Hz hum, and heavy clipping"),
                details = tr(
                    "Спроектирован специально для архивных релизов и одноголосых озвучек с низким битрейтом (<96 кбит/с). Обрезает высокочастотный цифровой свист, убирает сетевой гул микрофона и компрессирует резкие всплески.",
                    "Engineered for archival releases and low-bitrate (<96 kbps) fan dubs. Filters ultrasonic digital whine, removes microphone mains hum, and tames harsh vocal spikes."
                ),
                hardwareNote = tr("Для сильно сжатых или архивных дорожек", "For heavily compressed or archival audio tracks")
            )
        )

        ModeInfoTopic.GRAPHICS_API -> listOf(
            ModeInfoItemSpec(
                title = tr("OpenGL ES (По умолчанию)", "OpenGL ES (Default)"),
                badge = "GLES 3.0",
                badgeColor = primaryColor,
                summary = tr("Zero-copy конвейер EGLImage & SurfaceTexture с максимальной совместимостью", "Zero-copy EGLImage & SurfaceTexture pipeline with universal compatibility"),
                details = tr(
                    "Использует аппаратное связывание декодера MediaCodec и текстур `GL_TEXTURE_EXTERNAL_OES` без копирования кадров через оперативную память. Обеспечивает мгновенный старт видео и стабильную работу на 100% Android-устройств.",
                    "Uses direct hardware binding between MediaCodec and `GL_TEXTURE_EXTERNAL_OES` textures without RAM copies. Guarantees instant playback start and rock-solid stability across all Android devices."
                ),
                hardwareNote = tr("Минимум накладных расходов драйвера • Стандарт для всех SoC", "Minimal driver overhead • Default across all SoCs")
            ),
            ModeInfoItemSpec(
                title = tr("Vulkan (Многопоточный GPU)", "Vulkan (Multithreaded GPU)"),
                badge = "Vulkan 1.1+",
                badgeColor = tertiaryColor,
                summary = tr("Асинхронные очереди команд VkCommandPool и аппаратный импорт AHardwareBuffer", "Async VkCommandPool queues & zero-copy AHardwareBuffer import"),
                details = tr(
                    "Распределяет подготовку проходов шейдеров Anime4K и MEMC по нескольким потокам процессора и использует `VK_ANDROID_external_memory_android_hardware_buffer` вместе с YCbCr-сэмплерами. Снижает задержку времени кадра (Frame Time jitter) при одновременной работе тяжёлого апскейла и 120 Гц интерполяции.",
                    "Distributes Anime4K and MEMC shader command recording across CPU threads and utilizes `VK_ANDROID_external_memory_android_hardware_buffer` with Vulkan YCbCr samplers. Reduces frame-time jitter under heavy combined shader loads."
                ),
                hardwareNote = tr("Авто-возврат на OpenGL ES при отсутствии поддержки драйвером", "Auto-fallback to OpenGL ES if device driver lacks support")
            )
        )

        ModeInfoTopic.DECODER -> listOf(
            ModeInfoItemSpec(
                title = tr("HW+ (Гибридный авто)", "HW+ (Hybrid Auto)"),
                badge = "HW+",
                badgeColor = primaryColor,
                summary = tr("Аппаратный декодер GPU/VPU с бесшовным программным резервом", "Hardware GPU/VPU decoder with seamless software fallback"),
                details = tr(
                    "Приоритетно запускает выделенный аппаратный блок декодирования (MediaCodec HW) для минимального нагрева. Если встретится редкий профиль кодека (например, нестандартный Hi10P или специфический AV1/HEVC), плеер автоматически переключит поток на программный декодер без вылета и чёрного экрана.",
                    "Prioritizes dedicated hardware decoding (MediaCodec HW) for cool, efficient playback. If an exotic codec profile is encountered (e.g. legacy Hi10P or non-standard streams), automatically falls back to software decoding without playback errors."
                ),
                hardwareNote = tr("Рекомендуется по умолчанию для всех форматов", "Recommended default for all formats")
            ),
            ModeInfoItemSpec(
                title = tr("HW (Только аппаратный)", "HW (Hardware Only)"),
                badge = "HW",
                badgeColor = primaryColor,
                summary = tr("Строго аппаратное декодирование силами видеоядра SoC", "Strict hardware decoding on dedicated SoC video engine"),
                details = tr(
                    "Принудительно использует только аппаратные кодеки чипсета (Qualcomm, MediaTek, Exynos, Tensor). Обеспечивает минимальный расход батареи при просмотре 4K HDR / 60 fps, но может не открыть экзотические или повреждённые контейнеры.",
                    "Forces hardware-only chipset decoders. Delivers lowest battery consumption on 4K HDR / 60 fps videos, but may reject unsupported codec profiles."
                ),
                hardwareNote = tr("Минимальный расход батареи и нагрев", "Lowest battery drain and heat")
            ),
            ModeInfoItemSpec(
                title = tr("SW (Программный CPU)", "SW (Software CPU)"),
                badge = "SW",
                badgeColor = tertiaryColor,
                summary = tr("Многопоточное декодирование на ядрах центрального процессора", "Multi-threaded decoding on CPU cores"),
                details = tr(
                    "Обходит баги прошивочных аппаратных декодеров. Полезно, если на конкретном файле в аппаратном режиме видны зелёные полосы, рассыпание квадратов или рассинхрон таймингов.",
                    "Bypasses buggy firmware hardware decoders. Useful if a specific video exhibits green macroblocking, corrupt frames, or timestamp desync on hardware mode."
                ),
                hardwareNote = tr("Повышенная нагрузка на CPU и аккумулятор", "Higher CPU and battery usage")
            )
        )
    }

    val isPureOled = MaterialTheme.colorScheme.background.red < 0.01f &&
        MaterialTheme.colorScheme.background.green < 0.01f &&
        MaterialTheme.colorScheme.background.blue < 0.01f

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = 540.dp)
                .heightIn(max = 640.dp)
                .testTag("mode_info_modal_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = if (isPureOled) Color(0xFF050505) else MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            tonalElevation = 12.dp,
            shadowElevation = 24.dp,
            border = BorderStroke(
                1.dp,
                if (isPureOled) MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                else MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                // Header (No close button as required for floating modal cards)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = topic.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = dialogTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = tr("Подробное описание режимов и их различий", "Detailed guide to modes and differences"),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = dialogIntro,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable mode items
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items.forEach { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isPureOled) Color(0xFF0C0C0C) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            border = BorderStroke(
                                1.dp,
                                item.badgeColor.copy(alpha = if (isPureOled) 0.22f else 0.28f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.title,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(7.dp),
                                        color = item.badgeColor.copy(alpha = 0.16f),
                                        border = BorderStroke(1.dp, item.badgeColor.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = item.badge,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = item.badgeColor,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                Text(
                                    text = item.summary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = item.badgeColor
                                )

                                Text(
                                    text = item.details,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 17.sp
                                )

                                Text(
                                    text = item.hardwareNote,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("mode_info_modal_done_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = tr("Готово", "Done"),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
