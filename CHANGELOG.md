# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

---

## [v4.3.2] — `versionCode = 70` / `versionName = "4.3.2"`

### 🇬🇧 English
- **Calibrated AGSL Scroll-Edge Meniscus Lens (`Liquid Glass`) & Matte Diffusion (`Frosted Glass`):**
  - Reduced chromatic dispersion from an extreme `11 dp` (`38 dp` effective RGB split) to a refined **`1.6 dp`** prismatic edge (`36 dp` meniscus zone in `Liquid Glass`, `32 dp` in `Frosted Glass`) and reversed vertical sampling inward (`+dirY`) so top-edge coordinates never clamp at `0.0` ("vertical fence" distortion).
  - Scrolling text in `Liquid Glass` now bends smoothly through a polished glass bevel with 5-tap smoothing (`r = 3.5 px`) and dissolves cleanly (`1.0 - smoothstep(0.18, 1.0, t)`), while `Frosted Glass` applies a compact 9-tap Gaussian diffusion (`r <= 7.5 px`) without double-vision ghosting.
- **Live Glass Background Blur Around Floating Windows Without Black Screen:**
  - Replaced parent-container `RenderEffect` blur (which caused Android's hardware `GraphicsLayer` to blank out the recorded `LayerBackdrop` into a solid black screen when opening modals) with direct live `safeBackdrop` blur (`18 dp`) rendered on the full-screen modal scrim in `Frosted Glass` and `Liquid Glass`.
- **True Hardware HDR Presentation (10-bit BT.2020 PQ / HLG) & Smooth YouTube-Style Warm UI Adaptation:**
  - Routed HDR playback (`HDR10`, `HDR10+`, `HLG`, `Dolby Vision`) directly to the hardware composer (`SurfaceView` 10-bit HWC path, bypassing 8-bit SDR shader FBOs) and restored `SurfaceControl.Transaction` (`DATASPACE_BT2020_PQ` / `DATASPACE_BT2020_HLG` + API 34+ `setExtendedRangeBrightness` & window `COLOR_MODE_HDR` headroom).
  - Added a smooth `520 ms` color-temperature transition (`warmthProgress`) on the SDR overlay layer so player controls and subtitles shift gently to a comfortable D65 paper-white (`#F3EFE6`) during HDR playback just like YouTube.
- **Instant Portrait Orientation on Vertical Videos & Smart MEMC 60 fps Auto-Bypass + Detailed Stream HUD:**
  - Probed local video dimensions and rotation metadata (`90°`/`270°` swap) prior to the first frame and deferred `By Video` (`VIDEO_ASPECT`) orientation locking until valid dimensions are known, eliminating the brief landscape rotation jerk when opening portrait videos (`1080×1920`).
  - Accelerated PTS-delta frame-rate detection for streams without container FPS tags, bypassed the GL shader surface when `Smooth 60 fps` MEMC auto-bypasses on `>= 58.5 fps` videos, and upgraded **Stream HUD** with 4 detailed telemetry lines (Video & Color Space, Cadence & MEMC Auto-Bypass status, Active Renderer / Shader passes, and Audio codec/channels/bitrate/decoder + 3D Spatial + De-Artifact DSP status).

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.2)</b></summary>

<br>

- **Оптическая калибровка краевой линзы скролла без кислотного RGB-разрыва и сжатия текста (`Жидкое стекло` и `Матовое стекло`):**
  - Базовая хроматическая дисперсия уменьшена с экстремальных `11 dp` (дававших разлёт каналов до `38 dp`) до благородных **`1.6 dp`** (высота зоны `36 dp` в `Жидком стекле` и `32 dp` в `Матовом стекле`), а вектор смещения направлен внутрь контента (`+dirY`), что полностью устранило зажатие верхних пикселей в «вертикальный забор».
  - В теме `Жидкое стекло` текст на краях прокрутки мягко преломляется сквозь полированную грань с 5-точечным сглаживанием (`r = 3.5 px`) и плавным растворением (`1.0 - smoothstep(0.18, 1.0, t)`), а в `Матовом стекле` — мягко рассеивается компактным 9-точечным ядром (`r <= 7.5 px`) без двоения букв.
- **Живое размытие фона вокруг плавающих окон без ухода экрана в черноту:**
  - Убрано наложение `RenderEffect` на родительский контейнер записи `LayerBackdrop` (из-за которого аппаратный `GraphicsLayer` сбрасывал буфер фона в чёрный экран при открытии диалогов). Теперь размытие фона (`18 dp`) вокруг всех плавающих окон рисуется напрямую через живой `safeBackdrop` на подложке окна в темах `Матовое стекло` и `Жидкое стекло`.
- **Настоящий аппаратный HDR (10-бит BT.2020 PQ / HLG) и плавная тёплая адаптация интерфейса как в YouTube:**
  - При воспроизведении HDR (`HDR10`, `HDR10+`, `HLG`, `Dolby Vision`) видео направляется напрямую в аппаратный композитор `SurfaceView` (10-бит HWC в обход 8-битных SDR FBO шейдеров) с передачей `DATASPACE_BT2020_PQ` / `DATASPACE_BT2020_HLG`, `setExtendedRangeBrightness` (Android 14+) и `COLOR_MODE_HDR`.
  - Слой интерфейса плеера и субтитров при старте HDR плавно (за `520 мс`) переходит из холодного белого в мягкий тёплый оттенок `D65` (`#F3EFE6`), предотвращая ослепление глаз в темноте точно как в YouTube.
- **Мгновенная портретная ориентация вертикальных видео и умный авто-байпас MEMC 60 к/с + подробный «Монитор потока»:**
  - Разрешение и метаданные поворота (`90°`/`270°`) считываются до первого кадра, а режим ориентации «По формату видео» не переключает экран в ландшафт при `0×0`, благодаря чему вертикальные ролики (`1080×1920`) сразу открываются в портретном режиме без рывка.
  - Ускорен расчёт реального FPS по дельтам PTS для файлов без тега частоты в контейнере, добавлен полный обход GL-рендера при авто-байпасе `MEMC 60 к/с` на видео `>= 58.5 к/с`, а плашка **«Монитор потока» (`Stream HUD`)** теперь выводит 4 подробные строки (Видео и цветовое пространство `BT.2020 HWC` / `SDR BT.709`, Частота и статус `MEMC` / `Авто-Байпас`, Активный рендер и шейдеры, Аудио кодек/каналы/битрейт/декодер + `3D Spatial` + `De-Artifact DSP`).

</details>

---

## [v4.3.1] — `versionCode = 69` / `versionName = "4.3.1"`

### 🇬🇧 English
- **Fixed AGSL Scroll-Edge Meniscus Lens & Matte Diffusion Shader Compilation:**
  - Resolved an Android AGSL/SkSL runtime shader compilation issue caused by `h`-suffixed float literals (`0.72h`, `0.28h`, etc.) that previously forced scroll boundaries to fall back to a plain alpha fade.
  - Scrolling lists and floating windows now execute the full hardware AGSL shader on Android 13+: `Liquid Glass` renders a true cylindrical meniscus lens with 100% hold opacity across 90% of the zone, lateral barrel expansion (`0.36x`), vertical wave compression (`0.48x`), and `11 dp` 5-tap RGB chromatic dispersion, while `Frosted Glass` applies a 9-point matte blur kernel (`r = 13 px`).
- **20 dp Background Blur Around All Floating Windows (`Frosted Glass` & `Liquid Glass`) & Stronger `Flat` Scrim:**
  - Opening any floating modal window in `Frosted Glass` or `Liquid Glass` now blurs the entire underlying screen (`20 dp` hardware `RenderEffect` blur + `52 px` window blur radius) with a balanced dark/light scrim (`44%` / `26%`).
  - In `Flat (Material 3 Expressive)` mode, floating windows keep a crisp unblurred background with stronger contrast dimming (`64%` in Dark mode, `48%` in Light mode).
- **Vivid Dual-Source "Ambient Video Glow" Around the Video Frame:**
  - Upgraded the in-player **Ambient Video Glow** from a subtle center-only gradient hidden behind the video surface to a wide dual-source atmospheric halo (`0.92x` screen span) with soft peripheral screen-blended illumination across letterbox and pillarbox bars while preserving pure `#000000` OLED black when turned off.
- **Unified Top Bar & Player HUD Button Sizes + Landscape Floating Navigation Dock:**
  - Standardized all top bar action buttons (`Star`, `Palette`, `Refresh`) to `38×38 dp` with `20 dp` icons, unified the video player's top HUD **Resolution** (`1080p` / `4K`) and **HDR** pills to the exact `36 dp` capsule height of adjacent HUD buttons, and migrated list resolution badges to theme-aware canonical badges.
  - Replaced the landscape side navigation rail with the unified bottom centered floating capsule dock across both portrait and landscape orientations.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.1)</b></summary>

<br>

- **Исправление компиляции AGSL-шейдера краевой линзы скролла (`Жидкое стекло` и `Матовое стекло`):**
  - Устранена ошибка компиляции Android AGSL/SkSL из-за суффиксов `h` у дробных литералов (`0.72h`, `0.28h` и др.), из-за которой шейдер падал в `catch` и переключался на обычное линейное затухание прозрачности.
  - Теперь на Android 13+ списки и плавающие окна рендерят полноценный аппаратный шейдер: в теме `Жидкое стекло` работает цилиндрическая линза мениска со 100% плотностью текста на 90% высоты зоны, бочкообразным преломлением к краям (`0.36x`), вертикальным сжатием волны (`0.48x`) и 5-точечной RGB-дисперсией (`11 dp`), а в `Матовом стекле` — 9-точечное матовое размытие (`r = 13 px`).
- **Аппаратное размытие фона (20 dp) вокруг плавающих окон в стеклянных темах и усиленное затемнение в `Flat`:**
  - При открытии любого плавающего окна в темах `Матовое стекло` и `Жидкое стекло` весь фон приложения или видеоплеера вокруг окна мягко размывается (`20 dp` `RenderEffect` + радиус `52 px` для системных диалогов) со сбалансированной вуалью (`44%` / `26%`).
  - В теме `Flat (Material 3 Expressive)` фон под диалогами остаётся чётким без размытия, но получил более глубокое контрастное затемнение (`64%` в тёмной теме и `48%` в светлой).
- **Яркая двунаправленная фоновая подсветка «Ambient Video Glow» вокруг кадра в видеоплеере:**
  - Фоновая подсветка в видеоплеере переработана из слабой центральной точки позади `SurfaceView` в широкое двухзонное атмосферное свечение (`0.92x` размера экрана) с мягким периферийным ореолом на чёрных полосах сверху/снизу и по бокам кадра (при выключенной кнопке сохраняется эталонный `#000000` OLED).
- **Выравнивание размеров кнопок верхней панели и HUD плеера + единый нижний парящий док в горизонтальном режиме:**
  - Все кнопки верхней панели (`Звезда`, `Палитра`, `Обновить`) приведены к единому квадрату `38×38 dp` с иконками `20 dp`, кнопки **Качества** (`1080p` / `4K`) и **HDR** в верхней панели видеоплеера выровнены в единые `36 dp` стеклянные капсулы вровень с соседними кнопками HUD, а бейджи разрешения в списках переведены на каноничный тематический компонент.
  - Боковая панель `NavigationRail` в горизонтальной ориентации убрана — теперь и в портретном, и в ландшафтном режиме используется единый центрированный нижний парящий док-капсула.

</details>

---

## [v4.3.0] — `versionCode = 68` / `versionName = "4.3.0"`

### 🇬🇧 English
- **Instant "Ambient Video Glow" Toggle in Video Player Top HUD Bar:**
  - Moved the **Ambient Video Glow** toggle out of the Settings screens (`Appearance` & `Video & Anime4K`) and placed a dedicated one-tap button directly in the video player's top HUD bar right between **Quality** (`1080p` / `4K`) and **Screen Orientation**.
  - Tapping the button immediately switches between pure `#000000` OLED black letterboxing and atmospheric ambient video glow during playback, displaying a confirmation HUD notice pill (`Ambient Video Glow: ON / OFF`). Also available as a customizable quick-action chip (`Glow`) in the bottom toolbar.
- **Cylindrical Scroll-Edge Meniscus Lens (`Liquid Glass`) & 9-Point Matte Diffusion (`Frosted Glass`):**
  - Replaced simple alpha fade at the top and bottom scroll boundaries of lists and floating dialogs (`44 dp` optical zone) with a true **cylindrical meniscus lens** in `Liquid Glass` and **9-point matte blur diffusion** in `Frosted Glass`.
  - In `Liquid Glass`, scrolling text and icons remain at 100% opacity across 90% of the lens zone (`rimDissolve = 1.0 - smoothstep(0.90, 1.0, t)`), undergoing lateral barrel expansion (`barrelX`), vertical meniscus wave compression, 5-tap RGB chromatic dispersion (`9.5 dp`), and a specular meniscus rim gleam before dissolving only at the final 10% edge.
  - In `Frosted Glass`, scrolling content passes through a progressive 9-tap matte diffusion kernel (`r = 2.4x`) with 100% hold opacity across 88% of the zone, while `Flat (Material 3 Expressive)` preserves a clean gradient fade.
- **100% Borderless "About Device" Screen in `Flat (Material 3 Expressive)`:**
  - Migrated all hardware/software decoder cards, `HW Accelerated` / `SW Software` status badges, and legal action buttons on the **About Device** screen to canonical borderless `UnifiedSelectionRow`, `UnifiedBadgeChip`, and `UnifiedActionButton` components, eliminating the last remaining borders in `Flat` mode.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.0)</b></summary>

<br>

- **Быстрая кнопка «Фоновая подсветка» прямо в верхней панели видеоплеера:**
  - Переключатель **«Фоновая подсветка под видео»** убран из разделов настроек (`Оформление` и `Видео и Anime4K`) и вынесен отдельной кнопкой в верхнюю панель управления видеоплеера — прямо между индикатором **Качества** (`1080p` / `4K`) и кнопкой **Ориентации экрана**.
  - Нажатие мгновенно включает или выключает мягкое атмосферное свечение вокруг кадра прямо во время просмотра (по умолчанию сохраняется чисто-чёрный фон `#000000` OLED) с выводом аккуратного уведомления `Фоновая подсветка: Вкл / Выкл`. Кнопку также можно добавить в нижнюю панель быстрых действий.
- **Цилиндрическая линза мениска скролла (`Жидкое стекло`) и 9-точечное матовое размытие (`Матовое стекло`):**
  - Обычное полупрозрачное затухание на верхнем и нижнем краях прокрутки списков и плавающих окон (`44 dp` оптическая зона) заменено на физическое преломление **цилиндрического мениска линзы** в `Жидком стекле` и **9-точечное матовое рассеивание** в `Матовом стекле`.
  - В теме `Жидкое стекло` строки и иконки на 90% высоты краевой зоны сохраняют 100% плотность без бледного выцветания (`rimDissolve = 1.0 - smoothstep(0.90, 1.0, t)`), проходя через бочкообразное растяжение к краям (`barrelX`), вертикальное сжатие волны мениска, 5-точечную хроматическую RGB-дисперсию (`9.5 dp`) и зеркальный блик грани, растворяясь лишь на последних 10% кромки.
  - В теме `Матовое стекло` уходящий под шапку контент плавно размывается 9-точечным матовым ядром (`r = 2.4x`) без ранней потери прозрачности, а в теме `Flat` сохранён классический плавный фейд.
- **Полное удаление рамок в разделе «Об устройстве» в теме `Flat (Material 3 Expressive)`:**
  - Все карточки аппаратных и программных видео/аудио декодеров, бейджи `HW Аппаратный` / `SW Программный` и кнопки лицензий на экране **«Об устройстве»** переведены на каноничные `UnifiedSelectionRow`, `UnifiedBadgeChip` и `UnifiedActionButton` без единой рамки в режиме `Flat`.

</details>

---

## [v4.2.20] — `versionCode = 67` / `versionName = "4.2.20"`

### 🇬🇧 English
- **Crystal-Clean Glass Body Without Vertical White-Black Gradients (`Liquid Glass` & `Frosted Glass`):**
  - Removed the vertical white-to-black gradient overlay in `Liquid Glass` Dark mode that caused card tops to appear milky-gray and card bottoms muddy-black.
  - Replaced it across both `Frosted Glass` and `Liquid Glass` containers with a delicate `135°` diagonal crystal veil reflex (`0.035f` / `0.07f` at the top-left corner fading to `100%` transparent by `38%`), keeping the glass body deep and uniform across its full height.
- **Reference `v4.1.8` Obsidian-Graphite Glass & Translucent Quartz Capsules:**
  - Restored the deep obsidian-graphite base tone (`#10131A` in `Frosted Glass`, `#10141D` in `Liquid Glass`, and `#0E1117` / `#0D1017` in modals) instead of bluish-gray `#151B27`.
  - Replaced opaque slate-gray nested controls (`#28334A` / `#2A364F`) in Dark mode with translucent smoked-quartz glass pills (`#161B26` at `50%` alpha in `Frosted Glass` and `#151A24` at `40%` alpha in `Liquid Glass`) and luminous crystal-accent active capsules.
- **Crisp 5-Stop `135°` Optical Bevel with Inset-Outline Clipping Compensation:**
  - Fixed outer `.clip(shape)` eating half of the `135°` specular bevel stroke by rendering the contour on an inset outline (`inset(rimStroke * 0.5f)`), making 100% of the polished glass edge visible around rounded corners in both Dark and Light themes.
  - Restored the 5-point `135°` optical highlight curve (`0.52f..0.78f` top-left peak, `0.02f..0.06f` mid-edge fade, and `0.24f..0.58f` bottom-right accent reflex) plus a subtle corner-only `0.75 dp` micro-chamfer (`0.0f` transparent along straight mid-edges) for unselected inner pills.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.20)</b></summary>

<br>

- **Чистое тело стекла без мутного вертикального бело-чёрного градиента (`Жидкое стекло` и `Матовое стекло`):**
  - Полностью удалён вертикальный бело-чёрный градиент в тёмной теме `Жидкого стекла`, из-за которого верх карточек выглядел белёсо-мутным, а низ — грязно-чёрным.
  - В обеих стеклянных темах для контейнеров добавлен тончайший диагональный `135°` кристаллический отблеск (`0.035f` / `0.07f` в верхнем левом углу с полным растворением уже к `38%` диагонали), сохраняющий глубокую однородность стекла по всей высоте.
- **Благородный обсидианово-графитовый тон из эталона `v4.1.8` и полупрозрачные кварцевые капсулы:**
  - Возвращён благородный обсидианово-графитовый тон карточек и панелей (`#10131A` в `Матовом стекле`, `#10141D` в `Жидком стекле` и `#0E1117` / `#0D1017` в модальных окнах) вместо уходящего в синеву `#151B27`.
  - Сизо-серые пластиковые подложки вложенных кнопок (`#28334A` / `#2A364F`) в тёмной теме заменены на полупрозрачное дымчато-кварцевое стекло (`#161B26` с `alpha = 0.50f` в `Матовом стекле` и `#151A24` с `alpha = 0.40f` в `Жидком стекле`) и светящиеся акцентные капсулы при выборе.
- **Острая 5-точечная `135°` фаска с компенсацией клиппинга (Inset Outline):**
  - Исправлено срезание внешней половины блика фаски модификатором `.clip(shape)` за счёт отрисовки контура внутри `inset(rimStroke * 0.5f)` как в тёмной, так и в светлой теме — теперь 100% толщины полированной грани чётко видно на скруглениях.
  - Восстановлена 5-точечная кривая диагонального блика из `v4.1.8` (`0.52f..0.78f` пик в левом верхнем углу, `0.02f..0.06f` утончение в середине грани и `0.24f..0.58f` цветной рефлекс в правом нижнем углу), а для невыбранных вложенных кнопок добавлена тончайшая угловая микро-фаска `0.75 dp` с полной прозрачностью (`0.0f`) посередине сторон.

</details>

---

## [v4.2.19] — `versionCode = 66` / `versionName = "4.2.19"`

### 🇬🇧 English
- **Modular 3-Engine Theme Architecture (`Flat`, `Frosted Glass`, `Liquid Glass`):**
  - Separated the three visual engines into dedicated, isolated pipelines so each style obeys its own optical, shadow, and tonal rules without cross-theme leakage.
  - **Flat (Material 3 Expressive):** Pure 4-tier tonal containment (`#F5F7FC` canvas, `#FFFFFF` cards, `#EEF3FA` nested controls, and vibrant accent-tinted active pills) with zero shaders, zero offscreen backdrop passes, and zero borders.
  - **Frosted Glass & Liquid Glass:** Strict separation between top-level optical glass containers (`blur()` + `lens()` + `135°` bevel) and inner **Tonal Satin Pills** (tactile, borderless unselected controls with a subtle micro-shadow in Light mode and a `0.75 dp` accent highlight when selected).
- **Pure Vibrant Palette Chroma in Light Theme:**
  - Restored full chroma for all accent palettes (`Sakura`, `Neon Violet`, `Gold`, `Emerald`, `Crimson`, `Cyan`, and `Dynamic Monet`) in Light mode so selected cards, chips, and buttons display rich pastel-accent tints instead of muted gray-blue.
- **In-Window Live Optical Backdrop & Unified Scroll-Edge Meniscus Across All Modals:**
  - Enabled in-window live `LayerBackdrop` capture for all root library dialogs (`Appearance Quick Switcher`, `Negai Pro`, `Full License`, `What's New`, `Folder / Episode Sort`, `Hidden Folders`) plus Android 12+ hardware window blur (`FLAG_BLUR_BEHIND`) for standalone dialogs (`Open Magnet / .torrent`, `TorrServer Configuration`), eliminating dark opaque modal boxes over Light screens.
  - Unified all floating dialogs and bottom sheets onto a single `36 dp` scroll-edge optical meniscus container with pinned headers and footers that stay 100% crisp when content fits and refract smoothly only while scrolling.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.19)</b></summary>

<br>

- **Модульная архитектура из 3 независимых движков тем (`Flat`, `Матовое стекло`, `Жидкое стекло`):**
  - Три визуальные темы полностью разделены на изолированные конвейеры рендеринга, исключающие взаимное влияние шейдеров, рамок и теней.
  - **Flat (Material 3 Expressive):** Эталонная 4-уровневая тональная иерархия (`#F5F7FC` фон, `#FFFFFF` карточки, `#EEF3FA` вложенные кнопки и сочные акцентные выбранные элементы) без шейдеров, `drawBackdrop` и контурных рамок.
  - **Матовое стекло и Жидкое стекло:** Чёткое разделение между внешними оптическими контейнерами (`blur()` + `lens()` + `135°` фаска) и вложенными сатиновыми пилюлями **Tonal Satin Pills** (тактильные кнопки без рамок в покое, с мягкой микро-тенью в светлой теме и тонким бликом `0.75 dp` при выборе).
- **Чистая насыщенность цветовых палитр в светлой теме:**
  - Устранено приглушение цветности акцентных палитр (`Сакура`, `Неоновый фиолет`, `Золото`, `Изумруд`, ` Алый`, `Циан` и `Monet`) в светлом режиме — выбранные карточки, чипы и кнопки окрашиваются в чистый оттенок выбранной палитры без серо-синего налёта.
- **Живой оптический бэкдроп во всех плавающих окнах и единая 36-dp линза скролла:**
  - Все диалоги и нижние шторки главного экрана (`Быстрый выбор темы`, `Negai Pro`, `Лицензии`, `Что нового`, сортировка папок и серий, скрытые папки) переведены на прямой захват живого оптического фона окна, а отдельные системные диалоги (`Открыть Magnet / .torrent`, `Настройка TorrServer`) дополнены аппаратным размытием окна Android 12+ (`FLAG_BLUR_BEHIND`), что полностью убирает тёмные непрозрачные подложки поверх светлой темы.
  - Все плавающие окна и нижние шторки объединены на базе единого контейнера с закреплённой шапкой и 36-dp зоной преломления скролла, которая включается строго при фактической прокрутке списка.

</details>

---

## [v4.2.18] — `versionCode = 65` / `versionName = "4.2.18"`

### 🇬🇧 English
- **Tonal Satin Nested Controls & Instant Glass Theme Switching (`Frosted Glass` & `Liquid Glass`):**
  - Replaced nested glass-in-a-frame borders with contrasting **Tonal Satin Pills** for all inner buttons, selection rows, chips, and inputs inside cards and floating windows: unselected nested controls now have zero perimeter bevels, while active selections display a delicate accent highlight.
  - Kept the live optical background capture layer attached across theme switches so switching from `Flat` to `Frosted Glass` or `Liquid Glass` activates `blur()` and `lens()` refraction immediately without reopening the screen.
- **Unified Floating Window Scroll Optics & Pinned Modal Headers:**
  - Eliminated false top/bottom scroll-edge fading on non-scrolling dialogs (`Open Magnet / .torrent`, `TorrServer Configuration`, `Mode Info`) so short windows remain 100% crisp from top to bottom.
  - Pinned modal headers and `Done` footers outside the scrollable viewport across all floating dialogs and video player sheets, and tuned the `44 dp` AGSL meniscus zone to act as a clear refracting glass lens with chromatic dispersion across 85% of the zone before dissolving at the very rim.
- **Crisp Daylight 4-Tier `Flat (Material 3 Expressive)` Palette & 100% Borderless Radio Buttons:**
  - Upgraded Light mode in `Flat` to a bright, clean 4-tier Google Material 3 Expressive daylight hierarchy (`#F5F7FC` canvas, `#FFFFFF` cards, `#EEF3FA` tonal controls, and pure vibrant accent tinting) without muddy gray-blue casts.
  - Replaced stroked radio button rings in `Flat` with borderless tonal circles and made `Classic (Flat)` player transport preview buttons 100% opaque and seam-free.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.18)</b></summary>

<br>

- **Контрастные сатиновые пилюли (Tonal Satin Pills) без двойных рамок и мгновенное включение стекла (`Матовое стекло` и `Жидкое стекло`):**
  - Устранён визуальный шум «стекла в рамке внутри стекла в рамке»: все вложенные кнопки, строки выбора, чипы и поля ввода внутри карточек и диалогов переведены на плотные контрастные сатиновые подложки без контурных обводок в невыбранном состоянии и с тонким акцентным бликом при выборе.
  - Захват живого оптического фона теперь остаётся подключённым постоянно, поэтому переключение из темы `Flat` в `Матовое стекло` или `Жидкое стекло` мгновенно включает размытие и преломление без перезахода на экран.
- **Честная оптика прокрутки плавающих окон и закреплённые шапки шторок:**
  - Полностью убрано ложное затухание сверху и снизу в компактных диалогах без прокрутки (`Открыть Magnet / .torrent`, `Настройка TorrServer`, справка по режимам) — все элементы отображаются на 100% чётко.
  - Шапки и нижние кнопки `Готово` во всех диалогах и шторках видеоплеера вынесены из скроллящейся области, а 44-dp зона оптики скролла превращена в прозрачную преломляющую линзу со спектральной дисперсией и растворением только у самого края.
- **Чистая дневная палитра `Flat (Material 3 Expressive)` в светлой теме и радиокнопки без обводок:**
  - Светлая тема в режиме `Flat` переведена на чистую 4-уровневую дневную палитру Google Material 3 Expressive (`#F5F7FC` фон экрана, `#FFFFFF` карточки, `#EEF3FA` вложенные элементы и тонировка чистым ярким акцентом) без грязно-серого оттенка.
  - Радиокнопки в режиме `Flat` переведены с контурных колец на плоские тональные круги без рамок, а кнопки превью темы плеера `Классическая (Плоская)` сделаны на 100% непрозрачными и бесшовными.

</details>

---

## [v4.2.17] — `versionCode = 64` / `versionName = "4.2.17"`

### 🇬🇧 English
- **Live Optical Blur & Refraction Restored (`Frosted Glass` & `Liquid Glass`):**
  - Restored immediate frame-1 live background capture for the top floating bar, bottom navigation dock, and modal dialogs across both glass themes.
  - Added a two-phase optical rim for Light theme (`Frosted Glass` & `Liquid Glass`), pairing a delicate graphite-accent outer micro-rim with an inner `135°` specular highlight for crisp edge definition over bright surfaces.
- **100% Seamless & Borderless `Flat (Material 3 Expressive)` Theme:**
  - Eliminated all remaining border strokes, action bar dividers, and nested double-clip anti-aliasing seams across `System (Auto) / Light / Dark` cards, palette selectors, `Access / File / URL / 👁` pills, and dialog rows in `Flat` mode.
- **Pure `#000000` OLED Video Player Canvas & Optional Ambient Video Glow:**
  - Set the video player letterbox/pillarbox canvas to pure `#000000` black by default and added an optional **Ambient Video Glow** toggle in **Appearance** and **Video & Shaders** settings.
- **Zero-Crackle Audio Resume, Instant Video Frame Start & Gesture HUD Polish:**
  - Eliminated `SurfaceSyncGroup` window timeouts and delayed video frame start when resuming from non-zero timestamps by deferring display refresh rate switching until after first frame render and bypassing redundant initial seek re-triggers on local media.
  - Eliminated initial audio crackling/popping on Dolby and DTS tracks via first-sample envelope seeding and a 65 ms post-flush warmup ramp across all DSP processors.
  - Added an `850 ms` post-release visibility hold and orientation-aware system bar dead zones for the vertical brightness/volume swipe HUD, and rendered player modal sheets in-window to prevent system status bar flashes.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.17)</b></summary>

<br>

- **Восстановление живого размытия `blur()` и преломления `lens()` (`Матовое стекло` и `Жидкое стекло`):**
  - Восстановлен мгновенный захват оптического фона с первого кадра для верхней парящей панели, нижнего дока навигации и модальных окон.
  - Для светлой темы в режимах стекла добавлена двухфазная оптическая грань (тонкий графитово-акцентный внешний микро-ободок + внутренний зеркальный блик `135°`), обеспечивающая чёткую читаемость краёв на светлом фоне.
- **Абсолютно бесшовная тема `Flat (Material 3 Expressive)` без рамок:**
  - Полностью убраны остаточные обводки, разделители и двойные контуры сглаживания на кнопках выбора темы (`System / Light / Dark`), палитрах акцентов, пилюлях `Доступ / Файл / Ссылка / 👁`, кнопке исключений папок и карточках диалогов.
- **Чисто-чёрный (`#000000`) фон видеоплеера и опциональная фоновая подсветка:**
  - Поля вокруг видео по умолчанию сделаны строго чёрными (`#000000` OLED), а в разделы **Внешний вид** и **Видео и шейдеры** добавлен переключатель **«Фоновая подсветка под видео»** (`Ambient Video Glow`).
- **Чистый старт звука Dolby/DTS без треска, мгновенный старт видео и полировка жестов:**
  - Устранены таймауты `SurfaceSyncGroup` и задержка появления видеокадра при продолжении просмотра за счёт переноса переключения герцовки экрана на момент после отрисовки первого кадра и отказа от повторного `seekTo` для локальных файлов.
  - Устранён стартовый треск и щелчки звука на дорожках Dolby / DTS при возобновлении воспроизведения благодаря инициализации огибающей по первому сэмплу и 65 мс плавному прогреву после `flush()`.
  - Индикатор вертикальных свайпов яркости и громкости теперь удерживается на экране `850 мс` после отпускания пальца и защищён от ложных срабатываний у системных краёв экрана, а шторки плеера открываются внутри полноэкранного контейнера без мигания системного статус-бара.

</details>

---

## [v4.2.16] — `versionCode = 63` / `versionName = "4.2.16"`

### 🇬🇧 English
- **Instant Initial Window Sync (`wmsSync-VRI`) & Zero-BlurMaskFilter Hardware Shadow Pipeline:**
  - Eliminated offscreen software `BlurMaskFilter` allocations (`ShadowNode`, `InnerShadowNode`, `HighlightNode`) across cards and nested controls that caused `SurfaceSyncGroup(wmsSync-VRI[MainActivity]#2)` 1000ms timeouts during initial launch.
  - Added a single-pass hardware `RenderNode` fast path (`physicalShadowMod` + contour-clipped 3D volume gradient + `135°` optical bevel) for all nested controls (`isNestedControl = true`), non-sampling surfaces, and initial window-sync frames.
  - Deferred live `LayerBackdrop` optical capture (`isFirstFrameReady`) and display refresh rate querying by 64–80ms until initial `WindowManager` traversal synchronization completes.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.16)</b></summary>

<br>

- **Мгновенная синхронизация стартового окна (`wmsSync-VRI`) и однопроходный аппаратный конвейер теней:**
  - Устранены программные offscreen-буферы `BlurMaskFilter` (`ShadowNode`, `InnerShadowNode`, `HighlightNode`) на карточках и вложенных кнопках, вызывавшие предупреждение `SurfaceSyncGroup(wmsSync-VRI[MainActivity]#2)` (таймаут 1000 мс) при старте приложения.
  - Для всех вложенных кнопок, бейджей, чипов и первых кадров синхронизации окна внедрён однопроходный аппаратный рендеринг `RenderNode` с контурной тенью, внутренним 3D-градиентом и 135° фаской без создания промежуточных текстур.
  - Захват живого оптического фона `LayerBackdrop` и фоновый опрос режимов герцовки экрана отложены на 64–80 мс до полного завершения стартовой синхронизации `WindowManager`.

</details>

---

## [v4.2.15] — `versionCode = 62` / `versionName = "4.2.15"`

### 🇬🇧 English
- **Native `AndroidLiquidGlass` Pipeline (`Shadow`, `InnerShadow`, `Highlight`):**
  - Migrated all surfaces across `Liquid Glass` and `Frosted Glass` to the native `drawBackdrop` rendering pipeline (`io.github.kyant0:backdrop:1.0.4`), utilizing built-in outer `Shadow`, 3D `InnerShadow` volume, and contour-accurate `Highlight` bevels (`HighlightStyle.Default` / `HighlightStyle.Ambient`).
  - Removed pre-surface `.clip(shape)` wrappers prior to `drawBackdrop` so native outer shadows render cleanly outside rounded contours in both Light and Dark themes while inner fills and content remain strictly clipped inside `onDrawSurface`.
- **Authentic 4-Tier Google Material 3 Expressive (`Flat`) Theme:**
  - Upgraded the `Flat` theme to a strict 4-tier tonal containment hierarchy (`Background` -> `Cards` -> `Nested Controls / Rows` -> `Modals & Floating Bars`) with 100% opaque surfaces and zero borders or glass bevels across both Light and Dark/OLED modes.
- **Full Modernization of Video Player Modals & HUD Across All 3 Themes:**
  - Synchronized all video player control sheets, quick action pills, timeline badges, and HUD popup banners (`Speed Boost`, `Volume / Brightness`, `Seek HUD`, `Resumed Banner`) with the active player surface theme (`Flat`, `Frosted Glass`, `Liquid Glass`).
  - Wired live scroll-state-driven edge optics (`scrollState` / `lazyListState`) across all player modal sheets and lazy backdrop capture of the ambient player HUD strictly when a modal sheet is open.
- **Elimination of `SurfaceSyncGroup` 1000ms Timeouts:**
  - Replaced per-card temporary `RenderNode` allocations with a single shared `LocalFallbackCanvasBackdrop` and bypassed offscreen effect passes on non-sampling nested controls, permanently eliminating `SurfaceSyncGroup` transaction stalls during scrolling and tab transitions.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.15)</b></summary>

<br>

- **Нативный конвейер стекла `AndroidLiquidGlass` (`Shadow`, `InnerShadow`, `Highlight`):**
  - Все карточки, плавающие панели, модальные окна и кнопки в темах «Жидкое стекло» и «Матовое стекло» переведены на нативный рендеринг `drawBackdrop` (`io.github.kyant0:backdrop:1.0.4`) с использованием встроенных внешних теней `Shadow`, внутреннего объёма `InnerShadow` и контурной фаски `Highlight`.
  - Убрано предварительное обрезание `.clip(shape)` перед вызовом `drawBackdrop`, благодаря чему нативная внешняя тень свободно отрисовывается снаружи скруглённой формы как в Светлой, так и в Тёмной теме, а внутренняя заливка строго ограничена контуром.
- **Эталонная 4-уровневая тональная иерархия Material 3 Expressive в теме `Flat`:**
  - Плоская тема `Flat` приведена к 4-уровневой тональной системе Google Material 3 Expressive (`Фон` -> `Карточки` -> `Вложенные строки и кнопки` -> `Модальные окна и парящие панели`) со 100% непрозрачными поверхностями без рамок и стеклянных бликов.
- **Полная модернизация шторок и HUD видеоплеера под все 3 темы оформления:**
  - Все шторки управления видеоплеером, кнопки быстрых действий, капсулы таймлайна и всплывающие HUD-индикаторы (`Ускорение 2x`, `Громкость / Яркость`, `Перемотка`, `Продолжено с...`) синхронизированы с выбранным стилем плеера (`Flat`, `Матовое стекло`, `Жидкое стекло`).
  - Во все модальные окна плеера подключена динамическая оптика границ скролла (`scrollState` / `lazyListState`) и ленивый захват оптического фона HUD строго в момент открытия модального окна.
- **Устранение задержек `SurfaceSyncGroup` (1000ms timeout):**
  - Внедрён единый разделяемый `LocalFallbackCanvasBackdrop` и отключены холостые проходы шейдеров для вложенных кнопок без преломления фона, что полностью устраняет предупреждения `SurfaceSyncGroup` при прокрутке списков и переключении вкладок.

</details>

---

## [v4.2.14] — `versionCode = 61` / `versionName = "4.2.14"`

### 🇬🇧 English
- **Permanent Fix for 90° Rectangular Halos Behind Rounded Corners:**
  - Eliminated the 90° rectangular fallback (`0.dp`) on rounded cards, selection rows, and modals by preserving the original component shape across hardware layer clipping, surface background fills, and optical bevel outlines, while using a `ContinuousRoundedRectangle(22.dp)` fallback for backdrop shader sampling.
  - Replaced legacy Material 3 `Card` wrapper inside `UnifiedSurfaceCard` with a zero-elevation `Surface` and dual pre/post-surface shape clipping so no rectangular bounds ever bleed in Light or Dark themes.
- **Single-Pass 135° Optical Glass Bevel & Satin Matte Nested Controls:**
  - Removed duplicate outer `.border(...)` strokes that stacked on top of the 135° optical bevel in `Frosted Glass` and `Liquid Glass`, ensuring every card, row, modal, and bar shares the exact same clean 135° specular rim as the floating navigation dock and top bar.
  - Introduced **Satin Matte** nested control rendering (`isNestedControl = true` with a soft `0.75dp` bevel and zero backdrop sampling) for all nested icon buttons, action buttons, filter chips, badge chips, and input bars.
- **100% Borderless Material 3 Expressive (`Flat`) Theme & Canonical Dialogs:**
  - Removed all remaining borders in the `Flat` theme from search and text input bars (`UnifiedInputBar`), resolution badges (`360p` / `1080p`), theme/controls selector rows, and torrent action pills.
  - Migrated the **Open Magnet or .torrent**, **TorrServer Configuration**, and **Release Details** dialogs onto canonical `UnifiedModalHeader`, `UnifiedInputBar`, `UnifiedSurfaceCard`, and `UnifiedBadgeChip`.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.14)</b></summary>

<br>

- **Полное устранение 90° прямоугольных ореолов позади скруглённых углов:**
  - Устранён сброс скруглённой формы на прямоугольник `0.dp` (90°), из-за которого позади карточек категорий настроек и строк выбора в светлой и тёмной теме проступал прямоугольный фон.
  - Компонент `UnifiedSurfaceCard` переведён на чистый `Surface` с двойной контурной обрезкой `.clip(resolvedShape)` и сохранением исходной скруглённой фигуры во всех слоях `graphicsLayer`, `background` и `drawNegai3DBevel`.
- **Единая 135° оптическая фаска без двойной обводки и режим Satin Matte для вложенных кнопок:**
  - Из `Frosted Glass` и `Liquid Glass` удалена дублирующая внешняя обводка `.border(...)`, накладывавшаяся поверх 135° оптического блика — теперь все карточки и панели имеют точно такую же чистую 135° грань, как нижний док и верхняя капсула.
  - Все вложенные кнопки, иконки (`←`, `×`, `🔍`), чипы, бейджи и поля ввода переведены в спокойный матовый режим **Satin Matte** (`isNestedControl = true` с мягкой фаской `0.75dp` без рекурсивного преломления фона).
- **100% безрамочная плоская тема `Flat` и каноничные диалоги торрентов:**
  - В теме `Flat` полностью убраны контуры у полей поиска и ввода (`UnifiedInputBar`), бейджа разрешения (`360p` / `1080p`), строк выбора стиля и торрент-кнопок.
  - Диалоги **«Открыть Magnet или .torrent»**, **«Настройка TorrServer»** и инспектор раздачи переведены на эталонные `UnifiedModalHeader`, `UnifiedInputBar`, `UnifiedSurfaceCard` и `UnifiedBadgeChip`.

</details>

---

## [v4.2.13] — `versionCode = 60` / `versionName = "4.2.13"`

### 🇬🇧 English
- **10-Component Atomic Design System Consolidation:**
  - Standardized the entire application interface onto 10 canonical building blocks: `UnifiedSurfaceCard`, `UnifiedSelectionRow`, `UnifiedFloatingBar`, `UnifiedModalHeader` & `UnifiedModalFooter`, `UnifiedActionButton`, `UnifiedIconButton`, `UnifiedFilterChip`, `UnifiedBadgeChip`, `UnifiedInputBar`, and `UnifiedDropdownMenu`.
  - Replaced all legacy local cards, custom chips, ad-hoc badges, standalone text inputs, and unstyled popup menus across Library, Folders, Episode Details, Recent History, Torrents, Settings, and Player HUD overlays.
- **90° Corner Bleed Elimination & Contour-Accurate 135° Glass Bevels:**
  - Eliminated white and gray 90° rectangular halos protruding behind rounded corners of cards, buttons, and chips by enforcing hardware shape clipping prior to backdrop and surface drawing.
  - Replaced rectangular inner highlight fills with contour-accurate outline rendering so 3D optical glass bevels follow rounded and continuous capsule corners with zero corner bleed.
  - Removed harsh dark 1dp contrast outlines on nested glass cards, buttons, and chips so every element shares the exact same smooth 135° optical bevel as the floating navigation dock and top capsule bar.
- **100% Unified Modal Headers & "Done" Footers:**
  - Standardized every dialog, bottom sheet, and player control sheet onto `UnifiedModalHeader` (tinted icon badge, title, subtitle, and optional glass close button) and `UnifiedModalFooter` (right-aligned pill confirmation button with unified `OUTLINED` / `PRIMARY` styling).
- **Strict Borderless Flat Theme Compliance:**
  - Guaranteed zero borders on cards, rows, bars, modals, and filter/badge chips in the Material 3 Expressive `Flat` theme, preserving a clean 1dp `outlineVariant` stroke exclusively on `OUTLINED` action buttons and text input bars.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.13)</b></summary>

<br>

- **Единая атомарная дизайн-система из 10 канонических компонентов:**
  - Весь интерфейс приложения переведён на 10 эталонных компонентов: `UnifiedSurfaceCard`, `UnifiedSelectionRow`, `UnifiedFloatingBar`, `UnifiedModalHeader` и `UnifiedModalFooter`, `UnifiedActionButton`, `UnifiedIconButton`, `UnifiedFilterChip`, `UnifiedBadgeChip`, `UnifiedInputBar` и `UnifiedDropdownMenu`.
  - Устранены все локальные «особенные» карточки, кастомные чипы, разнородные бейджи, поля ввода и выпадающие меню на экранах библиотеки, папок, серий, истории, торрентов, настроек и оверлеев плеера.
- **Устранение прямоугольных 90° ореолов за углами и единая 135° фаска стекла:**
  - Полностью исправлено выступание белых и серых 90° прямоугольников за скруглённые углы карточек, кнопок и чипов благодаря принудительной обрезке по контуру формы до отрисовки слоёв стекла и бликов.
  - Внутренний блик и 135° фаска переведены с прямоугольной заливки на точную отрисовку по контуру скруглённой фигуры.
  - Убрана грубая тёмная обводка вложенных карточек и чипов в темах «Матовое стекло» и «Жидкое стекло» — теперь все элементы имеют такую же гладкую 135° оптическую грань, как нижняя док-панель и верхняя капсула.
- **100% унификация шапок и кнопок «Готово» в модальных окнах:**
  - Все диалоги, шторки сортировки, инспектор торрентов, кастомайзер быстрых действий и окна плеера переведены на единые `UnifiedModalHeader` и `UnifiedModalFooter`.
- **Эталонная чистота темы Flat без лишних рамок:**
  - В плоской теме Material 3 Expressive гарантировано отсутствие рамок у всех карточек, строк, панелей, чипов и модальных окон (тонкий контур `1dp` сохранён строго у контурных кнопок `OUTLINED` и полей ввода).

</details>

---

## [v4.2.12] — `versionCode = 59` / `versionName = "4.2.12"`

### 🇬🇧 English
- **Backdrop Ghosting Elimination (Double Image Fix):**
  - Swapped modifier hierarchy in `LibraryScreen` and `FolderDetailScreen` so the screen's solid background is recorded directly inside `LayerBackdrop` before rendering child content, preventing unrecorded transparent frames.
  - Injected an opaque backing canvas (`opaqueCanvasBase`) inside `drawBackdrop` for both `Frosted Glass` and `Liquid Glass` across all floating navigation capsules, top headers, and modal dialogs, fully occluding unblurred underlying layers from bleeding through.
  - Calibrated floating navigation bar backing density in dark themes to `0.48f` while preserving vivid `vibrancy()` and crystal `lens()` refraction.
- **Pronounced Cylindrical Scroll-Edge Optics & Menu Lens Bending:**
  - Redesigned `NEGAI_SCROLL_EDGE_OPTICS_AGSL` in Liquid Glass to eliminate premature edge fading (`rimDissolve = 1.0 - smoothstep(0.84, 1.0, t)` holding 100% brightness across 84% of the edge span).
  - Sharply enhanced the cylindrical meniscus lens (`scaleX = 1.0 + 0.28 * lensPower * (0.65 + 0.35 * normX * normX) * intensity`), vertical parabolic sag, and expanded chromatic dispersion from `4.6dp` to `6.8dp`.
  - Expanded optical edge span to `42dp` in Liquid Glass and `38dp` in Frosted Glass, and connected scroll states across modal dialogs and nested lists for instantaneous optical responsiveness.
- **Complete Elimination of Borders & 3D Artifacts in Flat Theme:**
  - Removed all 9 remaining glass borders, gradients, and specular bevels when `UiThemeStyle.FLAT` is active: accent palette selection, header version pill, TorrServer status card, search recommendation hero, torrent search result cards, modal header icon badge, floating dialog style selector preview, audio codec card, and player HUD overlay borders (`playerPopupBorderStroke = null`).
- **WCAG 2.1 Contrast Calibration & Light Bleedthrough Protection:**
  - Engineered adaptive `activeThumbColor` in `UnifiedGlassSwitch` dynamically computing luminance against `primaryAccent` and guaranteeing >= 4.5:1 contrast against pastel Monet tracks in dark themes.
  - Reinforced typography contrast against white background cards passing beneath translucent glass with worst-case backdrop compositing floors.
  - Replaced hardcoded text alphas with design system WCAG tokens across torrent cards, folder metadata, and planetary dialog pills.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.12)</b></summary>

<br>

- **Полное устранение «гостинга» (двоения резкого контента под стеклом):**
  - Изменён порядок модификаторов в `LibraryScreen` и `FolderDetailScreen`: непрозрачный фон экрана теперь гарантированно записывается внутрь текстуры `LayerBackdrop`.
  - Добавлена непрозрачная подложка `opaqueCanvasBase` в `drawBackdrop` для тем «Жидкое стекло» и «Матовое стекло» под всеми плавающими панелями, шапками и модальными окнами, полностью перекрывающая неразмытый нижний слой.
  - Плотность тонирующей подложки плавающего дока в тёмной теме увеличена до `0.48f` при сохранении яркого эффекта насыщенности и преломления линзы.
- **Глубокая оптическая деформация границ скролла и усиленный мениск линзы:**
  - Переработан шейдер `NEGAI_SCROLL_EDGE_OPTICS_AGSL` в теме «Жидкое стекло»: устранено раннее затухание (100% яркость сохраняется на первых 84% зоны границы скролла).
  - Резко усилена цилиндрическая линза мениска (`scaleX = 1.0 + 0.28 * lensPower`), параболический изгиб строк и спектральная дисперсия (увеличена с `4.6dp` до `6.8dp`).
  - Увеличена зона действия оптической кромки до `42dp` в «Жидком стекле» и `38dp` в «Матовом стекле», а также проброшены состояния скролла в модальные окна для мгновенного отклика.
- **Полное удаление оставшихся рамок и 3D-фасок в теме Flat:**
  - Отключены все 9 оставшихся обводок и стеклянных фасок при выборе плоской темы: в сетке акцентной палитры, бейдже версии в шапке, статусе TorrServer, стартовой карточке поиска, карточках торрентов, иконке шапки модальных окон, превью стилей и HUD-оверлеях плеера.
- **Калибровка автоконтраста WCAG 2.1 и защита от белых карточек:**
  - Разработан динамический подбор цвета бегунка `UnifiedGlassSwitch` с проверкой яркости дорожки `primaryAccent`, обеспечивающий контраст >= 4.5:1 даже на пастельных темах Monet.
  - Добавлена защита контраста текста на стекле при прохождении белых карточек под прозрачным слоем.
  - Заменены прямые цвета и альфа-прозрачности текстов на токены дизайн-системы в результатах поиска торрентов, метаданных папок и бейджах модальных окон.

</details>

---

## [v4.2.11] — `versionCode = 58` / `versionName = "4.2.11"`

### 🇬🇧 English
- **RenderThread Crash Guard & Circular Backdrop Recursion Elimination (SIGSEGV Fix):**
  - Resolved native HWUI stack overflow crash (`SEGV_ACCERR` in `RenderNode::prepareTreeImpl`) caused by nested list elements and action buttons attempting to sample their own actively recorded ancestor backdrop.
  - Implemented an architectural `LocalActiveRecordingBackdrop` composition local barrier in `Modifier.negaiSurface` and `resolveSafeLayerBackdrop` that completely blocks elements inside an active recording container from sampling the parent's backdrop.
  - Isolated tab contents in `LibraryScreen` and episode lists in `FolderDetailScreen` so only top header capsules, floating navigation docks, and floating dialog windows sample the underlying scene.
  - Reset `sampleBackdrop = false` across all action pills, buttons, and sort headers inside list content to prevent circular drawing trees.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.11)</b></summary>

<br>

- **Защита от RenderThread-крашей (SIGSEGV) и устранение циклической рекурсии бэкдропа:**
  - Устранён нативный сбой переполнения стека HWUI (`SEGV_ACCERR` в `RenderNode::prepareTreeImpl`), возникавший из-за попытки вложенных элементов списка и кнопок сэмплировать свой собственный родительский слой преломления.
  - Внедрён системный барьер `LocalActiveRecordingBackdrop` в `Modifier.negaiSurface` и `resolveSafeLayerBackdrop`, блокирующий сэмплирование бэкдропа для любых элементов, находящихся внутри записываемого контейнера.
  - Изолировано содержимое вкладок в `LibraryScreen` и списка серий в `FolderDetailScreen`: живое преломление фона активно исключительно для парящих верхних шапок, нижнего навигационного дока и модальных окон.
  - Сброшен флаг `sampleBackdrop = false` для всех плашек, кнопок и чипов внутри списков, исключая образование циклических графов в дереве отрисовки Android 15.

</details>

---

## [v4.2.10] — `versionCode = 57` / `versionName = "4.2.10"`

### 🇬🇧 English
- **Reference Material 3 Expressive Flat Theme:**
  - Completely eliminated glass stroke borders, bevels, inner groove shadows, and 3D glares from cards, modal sheets, floating navigation dock, top headers, filter chips, and selection rows (`BorderStroke(0.dp, Color.Transparent)` or `border = null`).
  - Enforced pure Material 3 Surface Container tonal elevations (`surfaceContainerLow` / `surfaceContainer` for first-level cards, `surfaceContainerHigh` for floating capsules and modal dialogs, `surfaceContainerHighest` for nested unselected chips and rows, and `secondaryContainer` / `primaryContainer` for selected chips and active buttons).
  - 1.dp hairline border (`outlineVariant`) in Flat mode remains strictly on outlined action buttons and text input fields.
  - Restored clean 2D Material 3 rendering for switches, radio indicators, seekbar controls, and vertical scrollbars, while disabling cylindrical lens bending and chromatic dispersion in Flat scroll-edge optics.
- **Satin Matte Contrast & 100% Text Legibility in Liquid Glass:**
  - Boosted satin backing density for nested elements (inside modals, action chips, and nested list rows) to `0.48f` (selected Dark) / `0.32f` (unselected Dark) and `0.80f` (selected Light) / `0.64f` (unselected Light) with a soft milky-quartz inner fill and 135° beveled rim.
  - Enhanced modal dialog backdrop blur to `16dp` and surface opacity to `0.62f` (Dark/OLED) / `0.76f` (Light) while maintaining crystal lens refraction (`28dp` height, `56dp` amount).
  - Floating navigation capsules now feature calibrated `6.5dp` pre-lens blur and `0.36f` (Dark) / `0.62f` (Light) surface opacity with full lens refraction.
  - Added anti-glare typography micro-shadowing (`0.50f` dark shadow in dark themes, `0.65f` white glow in light themes) in content providers for all glass themes to prevent optical reflections from degrading text legibility.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.10)</b></summary>

<br>

- **Эталонная плоская тема Material 3 Expressive (Flat):**
  - Полностью удалены стеклянные обводки, оптические фаски, внутренние тени и 3D-блики с карточек, модальных окон, плавающего навигационного дока, верхних шапок, чипов фильтрации и строк выбора (`BorderStroke(0.dp, Color.Transparent)` или `border = null`).
  - Реализована чистая тональная иерархия Material 3 Surface Container (`surfaceContainerLow` / `surfaceContainer` для карточек первого уровня, `surfaceContainerHigh` для модальных окон и плавающих панелей, `surfaceContainerHighest` для вложенных невыбранных чипов и строк, `secondaryContainer` / `primaryContainer` для активных элементов).
  - Обводка 1.dp (`outlineVariant`) в теме Flat сохранена исключительно для контурных кнопок и полей ввода текста.
  - Переключатели, радиокнопки, полоса перемотки и скроллбар приведены к строгому 2D-виду Material 3 без 3D-бликов и фасок, а краевая оптика скролла переведена на чистый вертикальный градиент без линзового расширения и хроматических аберраций.
- **Сатиновый контраст вложенных элементов и 100% читаемость текста в Liquid Glass:**
  - Увеличена плотность сатиновой подложки для вложенных элементов (внутри модальных окон, чипов и вложенных строк списка) до `0.48f` (выбрано, тёмная) / `0.32f` (не выбрано, тёмная) и `0.80f` (выбрано, светлая) / `0.64f` (не выбрано, светлая) с мягким молочно-кварцевым градиентом и фаской 135°.
  - Размытие модальных окон в теме «Жидкое стекло» увеличено до `16dp`, а плотность подложки — до `0.62f` (тёмная) / `0.76f` (светлая) при сохранении линзового преломления (`28dp` высота, `56dp` сила).
  - Плавающие навигационные капсулы откалиброваны на размытие `6.5dp` и прозрачность `0.36f` (тёмная) / `0.62f` (светлая) с сохранением живого преломления линзы.
  - Добавлена микротень текста (`0.50f` тёмная в тёмных темах, `0.65f` светлая в светлых темах) во все текстовые стили поверх стекла, гарантирующая идеальную читаемость текста даже поверх ярких оптических бликов и видео.

</details>

---

## [v4.2.9] — `versionCode = 56` / `versionName = "4.2.9"`

### 🇬🇧 English
- **Dynamic Scroll-Edge Lens Optics & Chromatic Dispersion:**
  - Top and bottom scroll-edge optical deformation on lists and floating modal sheets now activates strictly dynamically based on real scroll offset. Stationary cards (such as the storage access card on an empty library) remain 100% sharp with zero false top or bottom fading.
  - Preserved full brightness across the first 70% of the scroll-edge optical zone in **Liquid Glass** so the cylindrical outward lens bend (`10–14%` lateral expansion) and `R / G / B` chromatic dispersion are clearly visible before smoothly dissolving at the very edge.
- **3D Optical Glass Switches & Radio Buttons:**
  - Replaced flat standard toggle switches and radio indicators with custom 3D optical glass controls featuring recessed glass capsules, `135°` specular bevels, and a convex quartz droplet thumb with a specular highlight, inner shadow, and spring morphing on interaction.
- **100% Glass Micro-Component Unification:**
  - Unified all filter chips, quick action pills (`Access`, `File`, `URL`, `Folder`), folder sort controls, search and stream URL input fields, and context dropdown menus (`⋮`) with the active UI theme (`Flat`, `Frosted Glass`, and `Liquid Glass`).

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.9)</b></summary>

<br>

- **Динамическая линзовая оптика границ скролла и хроматическая дисперсия:**
  - Верхняя и нижняя оптическая деформация на границах списков и модальных окон теперь включается строго динамически при фактической прокрутке контента. Карточки в состоянии покоя (например, карточка доступа к памяти) отображаются идеально чётко без ложного размытия или затухания краёв.
  - В теме «Жидкое стекло» сохранена полная яркость пикселей на первых 70% оптической зоны края, благодаря чему отчётливо видны цилиндрический линзовый изгиб карточек (`10–14%` бокового расширения) и спектральная `R / G / B` дисперсия перед мягким растворением у самой кромки.
- **Объёмные 3D-стеклянные переключатели и радиокнопки:**
  - Стандартные плоские тумблеры и радиокнопки заменены на кастомные объёмные стеклянные элементы с углублённой капсулой, оптической фаской `135°` и выпуклой кварцевой каплей-бегунком с верхним бликом, внутренней тенью и пружинной деформацией при переключении.
- **100% унификация микро-элементов интерфейса:**
  - Все чипы фильтрации, кнопки быстрых действий («Доступ», «Файл», «URL», «Папка»), переключатели сортировки, поля поиска и ввода потоковых ссылок, а также контекстные выпадающие меню (`⋮`) полностью переведены на оптический конвейер выбранной темы оформления (`Flat`, «Матовое стекло», «Жидкое стекло»).

</details>

---

## [v4.2.8] — `versionCode = 55` / `versionName = "4.2.8"`

### 🇬🇧 English
- **Unified Floating Glass Capsules & Refraction Flow:**
  - Standardized all top app bars, section headers, and the bottom floating navigation dock into floating glass capsules with real-time scroll refraction physics across all tabs and settings pages.
- **Crystal-Clear Liquid Glass Optics:**
  - Calibrated Liquid Glass optics with reduced pre-lens blur (2–2.5dp on cards/bars, 7dp on modals) and amplified lens refraction (28dp height, 58dp strength) with translucent crystal transparency, creating a sharp water-droplet distortion effect that cleanly contrasts with velvet Frosted Glass (36dp blur).
- **Optical Glass Seekbar & Convex Droplet Thumb (Zero Neon Glow):**
  - Completely eliminated outer neon halo blur from the player seekbar; redesigned the timeline track as a recessed 3D optical glass groove with a 135° specular bevel and a convex glass droplet thumb that refracts the underlying video or scene.
- **AGSL Optical Scroll-Edge Deformation:**
  - Replaced static colored borders and lines at scroll boundaries with a shader engine (Android 13+) that smoothly bends, disperses, and dissolves the actual scrolling content pixels at top and bottom list boundaries while remaining completely transparent over empty background areas.
- **Live Modal Backdrop Sync & Zero Coordinate Shift:**
  - Eliminated vertical coordinate offset on floating modal windows and synchronized live background updates so switching themes, styles, or color palettes immediately updates the refracted scene behind open dialogs.
- **Discrete 5.1 & 7.1 Spatial Audio DSP & Android Spatializer:**
  - Real-time 5.1 and 7.1 channel binaural HRTF headphone convolution (MIT KEMAR), Center Dialogue Anchor, 55–110 Hz LFE Bass Harmonizer, 7.8 kHz Pinna Rear Notch filter, and native integration with the Android 12L+ Spatializer API.
- **WCAG 2.1 AAA Contrast Engine & 85/15 Complementary Palette Balance:**
  - Guaranteed text legibility (≥ 7.0:1 primary, ≥ 4.5:1 secondary contrast) across all 90 theme, style, and accent palette combinations, and enforced the 85% primary / 15% secondary complementary color rule across switches, radio buttons, and seekbars.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.2.8)</b></summary>

<br>

- **Единый стандарт парящих стеклянных капсул:**
  - Все верхние шапки и нижняя навигационная док-панель переведены на единый конвейер с живым преломлением скролла во всех вкладках и разделах настроек.
- **Кристальная оптика «Жидкого стекла»:**
  - Снижено матовое размытие (2–2.5dp на карточках и панелях, 7dp на окнах) и усилено преломление линзы (28dp / 58dp) в теме «Жидкое стекло» для эффекта прозрачной капли воды в отличие от бархатного «Матового стекла» (36dp).
- **Оптическая стеклянная полоса перемотки без неона:**
  - Полностью убрано неоновое свечение полосы перемотки плеера — трек и каплевидный ползунок выполнены из объёмного оптического стекла со скошенной фаской 135° и преломлением фона.
- **Шейдерная оптика границ скролла:**
  - Статичные цветные полосы и линии сверху и снизу списков заменены на шейдер оптической деформации самих карточек контента при входе под края прокрутки.
- **Живой фон и точные координаты всплывающих окон:**
  - Устранено смещение фона во всплывающих модальных окнах и исправлена заморозка подложки при переключении темы, стиля или палитры на лету.
- **Дискретный пространственный звук 5.1 / 7.1 и Android Spatializer:**
  - Бинауральная свертка многоканального звука 5.1 и 7.1 для наушников (MIT KEMAR HRTF), фокус центрального канала диалогов, сабвуферный гармонизатор 55–110 Гц, тыловой пинна-фильтр 7.8 кГц и поддержка системного Android 12L+ Spatializer API.
- **Движок композитного контраста WCAG 2.1 AAA и баланс 85/15:**
  - Гарантированная читаемость текста (≥ 7.0:1) на всех 90 комбинациях тем, стилей и палитр с единым стилем переключателей и радиокнопках.

</details>

---

## [v4.1.1] — `versionCode = 39` / `versionName = "4.1.1"`

### 🇬🇧 English
- **Decoder Lifecycle & Audio Artifact Fixes:**
  - Fixed audio crackling/glitches and video freeze on single frame after opening and closing the player multiple times by ensuring immediate surface detachment (`clearVideoSurface()`) and synchronous audio DSP buffer flushes on player exit.
- **Dynamic Refresh Rate Energy Saving (Non-MEMC):**
  - Optimized non-MEMC playback refresh rate matching: display dynamically matches video FPS (24/25/30/48/50/60 Hz) and falls back to standard 60 Hz instead of maintaining 120/144 Hz, reducing battery drain on standard videos.
- **Title Cleaning & Prefix Preservation:**
  - Preserved title prefixes starting with `hdr10_` and technical metadata tags in video names; fixed file size strings (e.g. `250 mb`) being misidentified as episode numbers.
- **Episode Number Removal:**
  - Completely removed episode badges from video cards, player titles, and lists to eliminate incorrect episode numbers derived from file names.
- **Landscape Levitating Windows & Smooth Edge Dissolve:**
  - Redesigned levitating dialogs for landscape mode with responsive header scaling and added a smooth optical fading edge (`dialogFadingEdge`) and rounded clipping (`RoundedCornerShape(16.dp)`), eliminating square cutoffs.
- **Google Play Market Rating:**
  - Replaced GitHub repository links in the Google Play Market build flavor with a direct "Rate App on Google Play" action.

### 🇷🇺 Русский
- **Устранение артефактов звука и зависания видео:**
  - Исправлены хрипы звука и зависание видео на одном кадре при повторных открытиях и закрытиях плеера благодаря немедленной отвязке видео-поверхности (`clearVideoSurface()`) и синхронной очистке аудиопроцессоров при выходе.
- **Энергосбережение экрана без MEMC:**
  - Оптимизирована частота обновления экрана при выключенном MEMC: экран подстраивается под FPS видео (24/25/30/48/50/60 Гц) с возвратом к 60 Гц вместо удержания 120/144 Гц, что экономит заряд батареи.
- **Сохранение названий `hdr10_` и чистота заголовков:**
  - Исправлено вырезание префиксов названий (например, `hdr10_sample.mp4`); устранены ошибки, когда размер файла (`250 mb`) воспринимался как номер серии.
- **Полное удаление бейджей эпизодов:**
  - Полностью удалены бейджи номеров серий из карточек файлов, заголовков плеера и списков, исключив неверные номера.
- **Левитирующие окна в альбомном режиме и сглаживание краев:**
  - Адаптирована верстка левитирующих окон для ландшафтной ориентации с компактными заголовками; добавлено скругление `RoundedCornerShape(16.dp)` и градиентная маска затухания краев (`dialogFadingEdge`) вместо прямоугольного среза.
- **Оценка в Google Play в маркет-сборке:**
  - Заменены ссылки на GitHub в сборке для Google Play на действие «Оценить приложение в Google Play».

---

## [v4.1] — `versionCode = 38` / `versionName = "4.1"`

### 🇬🇧 English
- **Automated Maximum Screen Brightness for Real HDR Output:**
  - Automated window brightness boost to 100% (`screenBrightness = 1.0f`) whenever playing HDR10, HLG, or Dolby Vision content to unleash the display hardware's full dynamic range and peak nits.
  - Automatically restores user system brightness when pausing, exiting the player, or playing SDR content.
- **Heavy Video Format Startup & Hardware Decoder Stall Protection:**
  - Integrated a 2.6-second warmup watchdog (`decoderWarmupWatchdog`): if a device's hardware decoder hangs or stalls on heavy profiles (HEVC Main 10, AV1, 4K Blu-ray streams), the player seamlessly falls back to software decoding (`DecoderMode.SOFTWARE_PREFER`).
  - Added continuous buffering spinner feedback during warmup before the first frame is rendered, preventing long black-screen or frozen static frame pauses.
  - Codec errors (`isCodecOrDecoderError`) automatically switch to software decoding without displaying disruptive error banners.
- **Fixed Stale Floating Media File Info:**
  - `MediaFileInfoModalSheet` and metadata extraction now refresh dynamically on episode switching, resolution updates, or codec changes (`isEpisodeMatchingPlayback` guard prevents stale data from the previous file).
- **Timeline Scrubber Clamping on Network Streams:**
  - Resolved timeline jump bug where streaming feeds with unknown/zero duration (`durationMs <= 0`) pegged the scrubber to 100% (the end); it now remains cleanly at 00:00 until stream metrics arrive.
- **Glass HUD Themes Monetization:**
  - Glass controls themes (`GLASS_CAPSULE`, `CYBER_NEON`, `CINEMA_TACTILE`) and surface styles (`FROSTED_GLASS`) are now part of Negai Premium (marked with gold stars `★` and gated behind the Pro modal).
- **P2P Trackers Segregation:**
  - Isolated `p2p_trackers.json` exclusively into the GitHub community build flavor (`src/github/assets`), fully excluding it from Google Play Market builds.
- **Normalized Versioning:**
  - Version updated to `4.1` with patch version `y` cleanly reset to `0`, and workflow parsing updated to support two-part `x.x` version numbers.

### 🇷🇺 Русский
- **Автоматическая максимальная яркость экрана для настоящего HDR:**
  - Автоматическое переключение яркости окна на 100% (`screenBrightness = 1.0f`) при воспроизведении HDR10, HLG и Dolby Vision для раскрытия пиковой аппаратной яркости дисплея.
  - Автоматическое восстановление исходной системной яркости при выходе из плеера или воспроизведении SDR.
- **Устранение зависаний тяжелых форматов и защита от сбоев кодеков:**
  - Внедрен сторожевой таймер разгона декодера (`decoderWarmupWatchdog`, 2.6 с): если аппаратный чип зависает на первом кадре тяжелых видео (HEVC 10-бит, AV1, BDAV), плеер бесшовно переключается на программный декодер (`DecoderMode.SOFTWARE_PREFER`).
  - Во время ожидания первого кадра теперь отображается индикатор загрузки, устраняя эффект замирания на черном экране или статичном кадре.
  - Ошибки кодеков не показывают пользователю тревожный баннер, если видео может быть воспроизведено программно.
- **Исправление плавающей информации о файле:**
  - Окно «Информация о файле» (`MediaFileInfoModalSheet`) теперь гарантированно обновляется при смене видео, смене разрешения потока или кодека, исключая утечку данных от предыдущего файла.
- **Исправление полосы перемотки (Timeline / Seek Bar):**
  - Устранена ошибка, из-за которой на сетевых потоках с не определенной длительностью полоса прокрутки стартовала с самого конца (100%). Теперь прогресс остается на 00:00 до загрузки потока.
- **Платные стеклянные темы оформления:**
  - Стеклянные стили элементов управления («Стеклянная капсула», «Кибер-Неон», «Студийный пульт») и подложек HUD («Матовое стекло») включены в Negai Premium (отмечены звездочками `★` с открытием окна Pro-подписки).
- **Изоляция трекеров только для GitHub-версии:**
  - Файл `p2p_trackers.json` перенесен исключительно в GitHub-сборку (`src/github/assets`) и полностью вырезан из Google Play Market версии.
- **Нормализация версионирования 4.1:**
  - Версия обновлена до `4.1` со сбросом номера патча `y` в `0`.

---

## [v4.0.15] — `versionCode = 37` / `versionName = "4.0.15"`

### 🇬🇧 English
- **Floating Navigation Dock on Main Screen:**
  - Modern floating rounded pill dock suspended above the bottom screen edge.
  - Automatically adapts to **Liquid Glass**, **Frosted Glass**, and **Flat (Material 3)** theme styles.
  - Integrated gesture-safe navigation bar insets (`WindowInsets.navigationBars`) preventing overlay clipping.
- **Optical Liquid Glass & Frosted Glass Overhaul (`liquid-glass.ybouane.com`):**
  - Simulated physical light refraction, specular top gleams, and deep crystal transparency.
  - Subtle chromatic prism border accents on curved surfaces.
  - Glass themes are part of Negai Premium (Flat Material 3 is free).
- **Synchronized Player HUD Interface:**
  - Video controls, top bars, and dialogs now directly mirror the app's selected theme style and accent palette.
- **Clean Complementary Palettes:**
  - Secondary colors apply strictly to secondary chips, badges, and subtle borders, eliminating muddy color combinations on primary controls.
- **Star Indicators (★) Replacing Text Badges:**
  - Removed bulky textual `Premium` and `PRO` badges across all dialogs, sheets, and cards in favor of clean gold star icons.
- **Simplified Theme Settings:**
  - Removed duplicate theme style controls from the complementary colors section.
- **Streamlined Packaging & Documentation:**
  - Single public release artifact `negai.apk` (removed duplicate `app-github-release.apk`).
  - Restructured documentation to be English-first and user-centric, with technical specifications moved to the bottom.

### 🇷🇺 Русский
- **Плавающий док навигации на главном экране:**
  - Современный скругленный парящий док внизу экрана с поддержкой стилей **Жидкое стекло**, **Матовое стекло** и **Flat (Material 3)**.
  - Корректная обработка системных отступов навигационной панели (`WindowInsets.navigationBars`).
- **Обновление физики стекла (Liquid Glass):**
  - Оптическое преломление света, зеркальные блики и кристальная прозрачность.
  - Тонкая хроматическая кайма по контуру элементов.
  - Стеклянные темы входят в Negai Premium (стиль Flat бесплатен).
- **Единый стиль интерфейса плеера:**
  - Элементы управления проигрывателя теперь оформлены в едином стиле с основным приложением.
- **Чистые комплементарные палитры:**
  - Дополнительный цвет применяется строго к бейджам и тонким акцентам, исключая грязные сочетания.
- **Лаконичные звезды (★) вместо бейджей:**
  - Текстовые плашки `Premium` и `PRO` заменены на элегантные золотые звезды.
- **Удаление дублирующихся настроек оформления:**
  - Убран повторный селектор темы из блока комплементарных цветов.
- **Оптимизация релиза и документации:**
  - Единый файл `negai.apk` для открытого репозитория; документация ориентирована на пользователей с английским языком по умолчанию.

---

## [v4.0.14] — `versionCode = 36` / `versionName = "4.0.14"`

### 🇬🇧 English
- **Natural YouTube-Style Hardware HDR Output:**
  - Direct `SurfaceView` (`PixelFormat.RGBA_8888`) routing to hardware decoder bypassing SDR shaders.
  - Natural dynamic HDR brightness scaling (`COLOR_MODE_HDR`, `SurfaceControl.Transaction.setExtendedRangeBrightness`, and `setDesiredHdrHeadroom`) without artificial window brightness overrides.
  - Dynamic HDR10+ metadata forwarding (`KEY_HDR10_PLUS_INFO` from ITU-T T.35 SEI) and Dolby Vision profile initialization (Profile 5, Profile 7, Profile 8 with CSD passthrough).
- **Expanded Premium Gating in Market Build:**
  - Neural Anime4K Mode B CNN and Mode A CNNX alongside MEMC 60fps & Max Screen Hz gated in the Play Store edition (Anime4K Fast, MEMC 2x, and Anime 2D remain free; all features unlocked in the GitHub edition).

### 🇷🇺 Русский
- **Аппаратный HDR-вывод без завышения системной яркости:**
  - Прямой вывод через `SurfaceView` в декодер с обходом SDR-шейдеров, поддержка динамических метаданных HDR10+ и профилей Dolby Vision.
  - Разграничение премиум-функций в сборке для Google Play.

---

## [v4.0.13] — `versionCode = 35` / `versionName = "4.0.13"`

### 🇷🇺 Русский
- **Двухвариантная архитектура дистрибуции (`flavorDimensions = "distribution"`: `github` и `market`):**
  - **Вариант `github` (`applicationId = "app.negai.player"`, `FEATURE_UNLOCKED = true`):** Все комплементарные цветовые палитры и глобальные темы «Матовое стекло» и «Жидкое стекло» разблокированы по умолчанию. В исходном наборе `src/github` реализован провайдер поддержки сообщества (`GithubCommunityPaymentProvider`) с плейсхолдерами донатов (Boosty, Patreon, GitHub Sponsors, крипто-кошелёк TON/USDT) без подключения библиотеки Google Play Billing SDK.
  - **Вариант `market` (`applicationId = "com.negai.player"`, `FEATURE_UNLOCKED = false`):** Коммерческая сборка для Google Play Store с разовой пожизненной покупкой через Google Play Billing (`BillingClient.ProductType.INAPP`, SKU `negai_premium_lifetime`) в изолированном исходном наборе `src/market` (`PlayBillingPaymentProvider`) и полным отсутствием сторонних ссылок донатов на уровне байткода.
- **Обновлённый CI/CD пайплайн релиза (`.github/workflows/release.yml`):**
  - Автоматическая сборка и подпись `assembleGithubRelease` (`app-github-release.apk`) и `bundleMarketRelease assembleMarketRelease` (`app-market-release.aab`, `app-market-release.apk`).
  - Автоматическая выгрузка `app-market-release.aab` в дорожку `internal` (`status: draft`) Google Play Console при наличии секрета `PLAY_STORE_JSON_KEY`.
  - Раздельная публикация релизов: коммерческие артефакты `market` сохраняются во внутреннем релизе приватного репозитория, а в публичную витрину `Poison5820/Negai` отправляются только `app-github-release.apk`, лицензии, `CHANGELOG.md` и документация.

### 🇬🇧 English
- **Dual-Flavor Distribution Architecture (`flavorDimensions = "distribution"`: `github` & `market`):**
  - **`github` Flavor (`applicationId = "app.negai.player"`, `FEATURE_UNLOCKED = true`):** All complementary dual-tone color palettes and 'Frosted Glass' & 'Liquid Glass' UI themes are unlocked out of the box. Isolated `src/github` source set (`GithubCommunityPaymentProvider`) provides community tip-jar placeholders (Boosty, Patreon, GitHub Sponsors, TON/USDT crypto wallet) with zero Google Play Billing SDK dependencies.
  - **`market` Flavor (`applicationId = "com.negai.player"`, `FEATURE_UNLOCKED = false`):** Commercial Google Play Store build gated behind a one-time lifetime purchase via Google Play Billing (`BillingClient.ProductType.INAPP`, SKU `negai_premium_lifetime`) in `src/market` (`PlayBillingPaymentProvider`), completely stripping all non-Google donation mechanisms at compile time.
- **Dual-Repository CI/CD Release Pipeline (`.github/workflows/release.yml`):**
  - Builds and signs both `assembleGithubRelease` (`app-github-release.apk`) and `bundleMarketRelease assembleMarketRelease` (`app-market-release.aab`, `app-market-release.apk`).
  - Deploys `app-market-release.aab` to the Google Play Console `internal` track as a `draft` release using `PLAY_STORE_JSON_KEY`.
  - Publishes `market` artifacts exclusively to the private repository release and dispatches only the signed `github` APK, third-party licenses, `CHANGELOG.md`, and READMEs to the public showcase repository.

---

## [v4.0.11] — `versionCode = 33` / `versionName = "4.0.11"`

### 🇷🇺 Русский
- **Исправление `Unexpected runtime error` на Blu-ray `.m2ts` с Dolby TrueHD + Atmos (`0x83`) и E-AC-3 JOC 7.1 (`0x84`):**
  - **Демультиплексирование субпотоков Blu-ray `stream_id = 0xFD` (`0x71`, `0x72`, `0x76`):** В `BdavM2tsExtractor` и `HdmvExtendedPesReader` реализовано чтение 7-битного поля `stream_id_extension` из PES-заголовка: для гибридных дорожек Dolby TrueHD + Atmos (`0x83`) субпотоки `0x76` (ядро AC-3) и `0x72` (MLP FBA 16-ch) разделяются без повреждения синхрослова `0x0B77`, а для Blu-ray E-AC-3 JOC 7.1 (`0x84`) базовый кадр `0x71` и зависимый субпоток расширения `0x72` атомарно склеиваются в единый фрейм (`HdmvEac3Reader`) для декодера FFmpeg 7.1.
  - **Баланс буфера `SampleQueue` при перемотке и старте с сохранённой позиции:** В `NormalizingTrackOutput` добавлен сброс `resetOnSeek()` и точный учёт `safeOffset` при отбрасывании неключевых слайсов до первого `IDR`, что полностью устраняет исключение `IllegalArgumentException` / `Unexpected runtime error` в `SampleQueue.sampleMetadata`.
- **Устранение тормозов на видео 250+ Мбит/с (Snapdragon 7+ Gen 3):**
  - **Прямой zero-copy вывод через `SurfaceView` без шейдеров:** Когда `Anime4K` и `MEMC` выключены (`!hasVisualProcessing`), видео с 0-го кадра выводится напрямую через аппаратный оверлей `SurfaceView` (`HdrVideoSurface`) в обход `GLSurfaceView` и `SurfaceTexture`.
  - **Разгрузка аппаратного декодера и защита от ложных сбросов к ключевому кадру:** Включена асинхронная очередь `MediaCodec` (`forceEnableMediaCodecAsynchronousQueueing()`), аппаратный пропуск неопорных B-кадров (`vendor.qti-ext-dec-picture-order.enable`), скорректирован порог аварийного сброса к IDR до `-650 мс` (вместо `-180 мс`) и добавлен замер реального времени `eglSwapBuffers()` в `GLThread` с защитой `renderInFlight`.
- **Настоящее переключение цветового пространства экрана в HDR (`BT.2020 PQ / HLG / Display P3`):**
  - **Снятие ограничения SDR-запаса яркости и явная передача метаданных `BT.2020`:** Убран вызов `setDesiredHdrHeadroom(1.0f)` на Android 14+ (блокировавший HDR-диапазон на уровне `1.0x` SDR), в `MediaCodecVideoRenderer.getMediaFormat()` добавлена явная передача `COLOR_STANDARD_BT2020`, `COLOR_TRANSFER_ST2084` / `COLOR_TRANSFER_HLG` и `KEY_HDR_STATIC_INFO` в `SurfaceView`, а для шейдерного режима `Anime4KGlSurfaceView` добавлен 10-битный `RGBA1010102` `EGLConfig` и `EGL_GL_COLORSPACE_BT2020_PQ_EXT` / `HLG_EXT` / `DISPLAY_P3`.

### 🇬🇧 English
- **Fixed `Unexpected runtime error` on Blu-ray `.m2ts` with Dolby TrueHD + Atmos (`0x83`) & E-AC-3 JOC 7.1 (`0x84`):**
  - **Blu-ray `stream_id = 0xFD` Substream Demuxing (`0x71`, `0x72`, `0x76`):** Added 7-bit PES `stream_id_extension` parsing in `BdavM2tsExtractor` and `HdmvExtendedPesReader`, separating interleaved AC-3 core (`0x76`) and TrueHD MLP (`0x72`) substreams on `0x83` tracks (`HdmvTrueHdReader`) and atomically coalescing `[AC-3 Core + E-AC-3 Dependent Substream]` frames on `0x84` tracks (`HdmvEac3Reader`) for FFmpeg 7.1 decoding.
  - **`SampleQueue` Offset Balance on Seek & Resume:** Added `resetOnSeek()` and strict `safeOffset` accounting in `NormalizingTrackOutput` when dropping pre-IDR slices, eliminating `SampleQueue.sampleMetadata` `Unexpected runtime error` crashes on seek/resume.
- **Eliminated 250+ Mbps Video Lag on Snapdragon 7+ Gen 3:**
  - **Direct Zero-Copy `SurfaceView` Output When Shaders Are Off:** Routed all video directly to `SurfaceView` (`HdrVideoSurface`) from frame 0 whenever `Anime4K` and `MEMC` are `OFF`, bypassing `GLSurfaceView` / `SurfaceTexture` GPU copies.
  - **Hardware Decoder Offload & Anti-Stall Tuning:** Enabled `forceEnableMediaCodecAsynchronousQueueing()`, Qualcomm low-latency picture-order / B-frame hints, relaxed `shouldDropBuffersToKeyframe` to `-650 ms` (preventing premature `codec.flush()` freezes), and measured true post-`eglSwapBuffers()` GPU frame duration with a `renderInFlight` guard.
- **True Hardware Display Color Space Switching for HDR (`BT.2020 PQ / HLG / Display P3`):**
  - **Unlocked HDR Headroom & Explicit `BT.2020` Format Keys:** Replaced `setDesiredHdrHeadroom(1.0f)` with `0f` on Android 14+, explicitly populated `COLOR_STANDARD_BT2020`, `COLOR_TRANSFER_ST2084` / `COLOR_TRANSFER_HLG`, and `KEY_HDR_STATIC_INFO` on `MediaCodec`, and added 10-bit `RGBA1010102` + `EGL_GL_COLORSPACE_BT2020_PQ_EXT` / `HLG_EXT` / `DISPLAY_P3` support to `Anime4KGlSurfaceView`.

---

## [v4.0.10] — `versionCode = 32` / `versionName = "4.0.10"`

### 🇷🇺 Русский
- **Адаптивный пропуск кадров декодера и оптимизация буфера на видео 250+ Мбит/с (`AdaptiveFrameDropPolicy`):**
  - **Ранний сброс опоздавших кадров с защитой от замирания картинки:** В `MediaCodecVideoRenderer` внедрён порог сброса `-12 мс` для сверхтяжёлых потоков (`>= 80 Мбит/с`) и серийных задержек (вместо стандартных `-30 мс`), при этом гарантируется принудительная отрисовка опорного кадра не реже каждых `220 мс`, чтобы экран никогда не замирал.
  - **Быстрый перескок к ключевому кадру (`IDR`) при перегрузке декодера:** При отставании декодирования более чем на `180 мс` на 250 Мбит/с потоках очередь декодера автоматически сбрасывается до следующего ключевого кадра (`shouldDropBuffersToKeyframe`), мгновенно восстанавливая синхронизацию с аудио.
  - **Приоритет реального времени и лимит памяти предбуфера `96 МБ`:** Для `MediaCodec` активирован `KEY_PRIORITY = 0` (реальное время), `KEY_OPERATING_RATE` и пропуск дублирующихся буферов релиза; в `DefaultLoadControl` установлен потолок `targetBufferBytes = 96 МБ` (`prioritizeTimeOverSizeThresholds = false`), предотвращающий выделение 1.5–2.5 ГБ ОЗУ и фризы сборщика мусора на 250 Мбит/с файлах.
- **Интерполяция кадров (`MEMC`) на всех сложных форматах (HDR, 10-бит, Blu-ray `.m2ts`, 4K 250 Мбит/с) с авто-пропуском субкадров:**
  - **Активация MEMC и Anime4K на HDR / 10-бит потоках:** В `PlayerScreen` конвейер `Anime4KVideoSurface` теперь автоматически используется для всех форматов (включая HDR10 / HLG / 10-бит BT.2020) при включённой интерполяции кадров (`MEMC`) или `Anime4K`, а также ускорен захват исходного FPS по дельтам `PTS` (4 кадра вместо 8) и оценка битрейта по размеру файла.
  - **Автоматический пропуск субкадров MEMC и масштабирование буферов движения при нагрузке:** В `Anime4KRenderer` добавлен мониторинг времени рендера GPU и VSync (`evaluateMemcTick`): при нехватке времени кадра пропускается синтез промежуточного субкадра (`DROP_SUBFRAME_TO_REFERENCE`), при скачке `PTS` после пропуска кадра декодером пропускается проход оптического потока (`SKIP_OPTICAL_FLOW_CATCHUP`), а внутренние FBO оценки движения ограничиваются до `1920px` (`1440px` под высокой нагрузкой).

### 🇬🇧 English
- **Adaptive Decoder Frame Dropping & Buffer Optimization for 250+ Mbps Video (`AdaptiveFrameDropPolicy`):**
  - **Early Late-Frame Dropping with Anti-Freeze Guarantee:** Custom `MediaCodecVideoRenderer` policy drops late output buffers at `-12 ms` on ultra-high-bitrate streams (`>= 80 Mbps`) instead of waiting `-30 ms`, while enforcing a maximum `220 ms` interval between rendered frames so video never freezes completely.
  - **Fast Keyframe (`IDR`) Catch-Up Under Decoder Overload:** Flushes queued packets to the next keyframe when lag exceeds `180 ms` on ultra-high-bitrate streams (`260 ms` standard), immediately restoring A/V sync.
  - **Realtime Codec Priority & `96 MB` Buffer Memory Cap:** Configured `MediaCodec` with `KEY_PRIORITY = 0` (realtime), `KEY_OPERATING_RATE`, and identical release time skipping; capped `DefaultLoadControl` `targetBufferBytes` to `96 MB` (`prioritizeTimeOverSizeThresholds = false`) to prevent 1.5–2.5 GB RAM allocation and GC stalls on 250 Mbps files.
- **Frame Interpolation (`MEMC`) Across All Complex Formats (HDR, 10-bit, Blu-ray `.m2ts`, 4K 250 Mbps) with Auto Sub-Frame Skipping:**
  - **Enabled MEMC & Anime4K on HDR / 10-bit Streams:** Updated `PlayerScreen` so `Anime4KVideoSurface` runs on HDR10 / HLG / 10-bit BT.2020 streams whenever MEMC or Anime4K is active, alongside faster 4-sample `PTS` FPS detection and file-size bitrate estimation.
  - **Automatic MEMC Sub-Frame Skipping & Internal FBO Scaling Under Load:** Integrated `evaluateMemcTick` into `Anime4KRenderer` to automatically skip intermediate sub-frame synthesis (`DROP_SUBFRAME_TO_REFERENCE`) when GPU/VSync budget is exceeded, skip optical-flow estimation across decoder frame-drop jumps (`SKIP_OPTICAL_FLOW_CATCHUP`), and bound internal MEMC FBOs to `1920px` (`1440px` under load).

---

## [v4.0.9] — `versionCode = 31` / `versionName = "4.0.9"`

### 🇷🇺 Русский
- **Переработка физики стекла и света в теме «Жидкое стекло» (`Liquid Glass` / Fluid Glassmorphism):**
  - **Скошенная стеклянная грань (`Bevel / Chamfer`) вместо плоских неоновых рамок:** Полностью убраны равномерные неоновые обводки `1px solid var(--accent)`. Вместо них реализована скошенная грань с падением света сверху вниз: верхняя кромка имеет мягкий светлый блик (`rgba(255, 255, 255, 0.35)` с деликатным 14% оттенком акцента), плавно затухающий к нижней кромке (`rgba(255, 255, 255, 0.08)`).
  - **Объём и толщина выпуклой линзы (`Specular Highlights & Inner Shadow`):** Добавлены внутренние блики и тени толстой полированной линзы (`inset 0 1px 1px rgba(255, 255, 255, 0.40)` сверху и `inset 0 -2px 4px rgba(0, 0, 0, 0.30)` снизу), а также мягкая внешняя тень модальных окон (`0 20px 40px rgba(0, 0, 0, 0.45)` через `Modifier.negaiModalExteriorDropShadow` с отсечением `ClipOp.Difference` строго снаружи стекла).
  - **Глубокое оптическое размытие фона (`Backdrop Blur`):** Для модальных окон («Быстрое оформление», «Negai Premium» и др.) и выпадающих листов увеличена глубина оптического размытия (`blurBehindRadius = 64` + насыщенная световая диффузия `saturate(180%)`), чтобы фоновый текст превращался в мягкую абстрактную световую текстуру и не мешал чтению.
  - **Тело стекла `135°` и деликатная тонировка (`5–12%`):** Фон стеклянных панелей выполнен диагональным градиентом под углом `135°` от `rgba(255, 255, 255, 0.12)` к `rgba(255, 255, 255, 0.03)`. При включённой акцентной тонировке оттенок акцента подмешивается в тело стекла с минимальной непрозрачностью (`5–12%`), не заливая рамки.
  - **Объёмные гелевые переключатели и радио-кнопки (`UnifiedGlassSwitch` и `UnifiedGlassRadioButton`):** В стеклянных темах переключатели и радио-кнопки выполнены в виде выпуклых капель смолы/геля с внутренним градиентным свечением и плавным смещением светового блика при наведении и нажатии (в теме «Плоский» сохранены стандартные элементы Material 3).

### 🇬🇧 English
- **Rebuilt Glass & Light Physics for the "Liquid Glass" (Fluid Glassmorphism) Theme:**
  - **Top-Down Beveled Glass Edge (`Bevel / Chamfer`) Replacing Flat Neon Borders:** Eliminated uniform bright `1px solid var(--accent)` outlines and replaced them with a top-down chamfered glass edge: a soft top specular highlight (`rgba(255, 255, 255, 0.35)` with a subtle 14% accent hint) tapering down to a nearly invisible bottom edge (`rgba(255, 255, 255, 0.08)`).
  - **Convex Lens Volume & Thickness (`Specular Highlights & Inner Shadow`):** Added thick lens inner highlights and shadows (`inset 0 1px 1px rgba(255, 255, 255, 0.40)` at the top, `inset 0 -2px 4px rgba(0, 0, 0, 0.30)` at the bottom) and an exterior-only modal drop shadow (`0 20px 40px rgba(0, 0, 0, 0.45)` via `Modifier.negaiModalExteriorDropShadow` with `ClipOp.Difference`).
  - **Deep Optical Backdrop Blur (`Backdrop Blur`):** Increased modal and menu backdrop blur (`blurBehindRadius = 64` + `saturate(180%)` radial light diffusion) so background text dissolves into a soft abstract light texture rather than bleeding through.
  - **`135°` Glass Body Gradient & Subtle `5–12%` Tinting:** Replaced flat fills with a `135°` diagonal glass gradient from `rgba(255, 255, 255, 0.12)` to `rgba(255, 255, 255, 0.03)`, mixing the accent color into the glass body at `5–12%` opacity when element tinting is enabled.
  - **3D Resin / Gel Interactive Controls (`UnifiedGlassSwitch` & `UnifiedGlassRadioButton`):** Styled active items, switches, and radio buttons in glass themes as 3D resin/gel droplets with inner gradient luminescence and interactive specular shift on hover/press (keeping standard Material 3 controls in the Flat theme).

---

## [v4.0.8] — `versionCode = 30` / `versionName = "4.0.8"`

### 🇷🇺 Русский
- **Исправление воспроизведения и перемотки Blu-ray `.m2ts` (`BDAV 192 байта`, `DTS-HD Master Audio 7.1`, 4-слайсовый `H.264 High@L4.1`):**
  - **Предчтение длительности и пакетно-выровненный `SeekMap`:** В `BdavM2tsExtractor` добавлено предварительное сканирование меток `PTS` в начале и конце `.m2ts` файла до запуска `TsExtractor`, что исключает внутренний прыжок `TsDurationReader` в конец файла без сброса буферов элементарных потоков, а также пакетно-выровненный `BdavPacketAlignedSeekMap` с запасом на ключевой кадр.
  - **Нормализация шкалы времени к `00:00`:** Добавлен `BdavTimestampCoordinator`, автоматически вычитающий стартовое смещение `PTS` Blu-ray диска (например, `08:11` / `491 с`) и сглаживающий разрывы `PTS` для всех дорожек, благодаря чему таймлайн всегда начинается с `00:00` и точно соответствует длительности ролика (`01:28`).
  - **Устранение зелёных вспышек и залипания программного декодера (`SW`):** Отключён флаг `FLAG_ALLOW_NON_IDR_KEYFRAMES` и добавлен строгий фильтр `IDR` ключевых кадров в `NormalizingTrackOutput` при старте и после перемотки (предотвращающий декодирование зависимых слайсов 4-slice Blu-ray H.264 по пустым буферам `Y=0, U=0, V=0`); автоматический аварийный фоллбэк больше не перезаписывает пользовательскую настройку декодера в `DataStore` (`HW+` остаётся по умолчанию).
- **Переработка тем «Матовое стекло» (`Frosted Glass`) и «Жидкое стекло» (`Liquid Glass`):**
  - **Устранён прямоугольный артефакт позади полупрозрачных элементов и окон:** Отключена аппаратная тень Skia (`shadowElevation = 0.dp`, `tonalElevation = 0.dp`) на всех полупрозрачных стеклянных поверхностях, которая ранее рисовала прямоугольник внутренней умбры (`inner umbra`) сквозь прозрачный фон.
  - **Жидкое стекло (`Liquid Glass`):** Повышена оптическая прозрачность (`alpha = 0.20..0.24` для элементов, `0.42` для модальных окон) и добавлен 4-слойный шейдер краевого линзового преломления (`Modifier.negaiGlassSurfaceOverlay` — внешний мениск преломления, тёмная линзовая фаска глубины, противоположная внутренняя каустика и острый влажный блик по контуру).
  - **Матовое стекло (`Frosted Glass`):** Добавлен процедурный шейдер мелкозернистого матового шума (`FrostedNoiseTileBrush` 96×96 с наложением `BlendMode.Overlay`), сатиновая диффузия контура и системное размытие фона позади левитирующих окон (`FLAG_BLUR_BEHIND` на Android 12+).

### 🇬🇧 English
- **Fixed Blu-ray `.m2ts` Playback & Seeking (`BDAV 192-byte`, `DTS-HD Master Audio 7.1`, 4-slice `H.264 High@L4.1`):**
  - **Pre-Read Duration Scanner & Packet-Aligned `SeekMap`:** Added pre-read head/tail PES `PTS` scanning in `BdavM2tsExtractor` before `TsExtractor` runs (preventing mid-stream `TsDurationReader` EOF seeks without elementary reader resets) and `BdavPacketAlignedSeekMap` with keyframe lead-in.
  - **Zero-Based Timeline Normalization (`00:00`):** Added `BdavTimestampCoordinator` to normalize non-zero Blu-ray disc `PTS` base offsets (fixing the `08:11 / 01:28` timeline bug) and smooth mid-stream `PTS` discontinuities across all tracks.
  - **Eliminated Green Screen Flashes & Sticky `SW` Decoder Fallback:** Removed `FLAG_ALLOW_NON_IDR_KEYFRAMES` and enforced strict `IDR` keyframe gating in `NormalizingTrackOutput` on startup and post-seek (preventing 4-slice Blu-ray H.264 streams from decoding dependent slices against uninitialized `Y=0, U=0, V=0` buffers); transient error recovery no longer overwrites the user's saved `HW+` preference in `DataStore`.
- **Reworked "Frosted Glass" & "Liquid Glass" Interface Themes:**
  - **Removed Rectangular Artifact Behind Translucent Elements:** Zeroed Skia hardware shadow elevation (`shadowElevation = 0.dp`, `tonalElevation = 0.dp`) on all translucent surfaces so the solid inner umbra polygon is never visible through the glass.
  - **Liquid Glass:** Increased optical transparency (`alpha = 0.20..0.24` for elements, `0.42` for modals) and implemented a 4-band curved lens edge refraction overlay (`Modifier.negaiGlassSurfaceOverlay` — outer refractive meniscus, concave depth trough, opposing inner caustic highlight, and crisp wet specular rim).
  - **Frosted Glass:** Added a procedural 96×96 fine matte noise grain shader (`FrostedNoiseTileBrush` with `BlendMode.Overlay`), satin inner frost rim, and Android 12+ window background blur (`FLAG_BLUR_BEHIND`) on levitating dialogs.

---

## [v4.0.7] — `versionCode = 29` / `versionName = "4.0.7"`

### 🇷🇺 Русский
- **Единая дизайн-система (`UnifiedDesignSystem`) и 3 глобальные темы интерфейса:**
  - Все однотипные UI-элементы приложения (карточки папок и настроек, строки выбора и переключатели, левитирующие окна и листы, бейджи, иконки-кнопки и ползунки) переведены на единые шаблоны (`UnifiedSurfaceCard`, `UnifiedModalSurface`, `UnifiedSelectionRow`, `UnifiedBadgeChip`, `UnifiedIconButton`, `unifiedSliderColors`) и токены `LocalNegaiUiStyleTokens`.
  - Добавлены **3 глобальные темы интерфейса** (`UiThemeStyle`), применяемые одновременно ко всему приложению, левитирующим окнам и HUD видеоплеера:
    - **Плоский (Material 3 Expressive)** — бесплатный базовый стиль без теней с чёткими контурами и тональными контейнерами M3.
    - **Матовое стекло (Frosted Glass ★, Negai Premium)** — полупрозрачные матовые подложки под элементы и фоны окон с мягким рассеянным свечением контура.
    - **Жидкое стекло (Liquid Glass ★, Negai Premium)** — многослойное оптическое преломление, двойные блики граней и глубокий объём.
- **Переключатель акцентной тонировки элементов (`accentTintEnabled`):**
  - В настройки оформления и окно «Быстрое оформление» добавлен независимый переключатель тонировки подложек, рамок, кнопок и окон под активный акцентный цвет либо в нейтральном ахроматическом стиле без цветовой окраски.

### 🇬🇧 English
- **Unified Design System (`UnifiedDesignSystem`) & 3 Global Interface Themes:**
  - Standardized all identical UI element types across the app (folder & settings cards, selection/toggle rows, levitating dialogs & bottom sheets, badges, icon buttons, and sliders) onto shared composable templates (`UnifiedSurfaceCard`, `UnifiedModalSurface`, `UnifiedSelectionRow`, `UnifiedBadgeChip`, `UnifiedIconButton`, `unifiedSliderColors`) driven by `LocalNegaiUiStyleTokens`.
  - Introduced **3 global interface themes** (`UiThemeStyle`) applied uniformly across the app, levitating windows, and video player HUD:
    - **Flat (Material 3 Expressive)** — free base style with minimal shadows, crisp outlines, and M3 tonal containers.
    - **Frosted Glass (★ Negai Premium)** — translucent matte surface pills and window backings with a diffused frost rim.
    - **Liquid Glass (★ Negai Premium)** — multi-layer optical refraction sheen, dual specular edge highlights, and deep lens elevation.
- **Independent Element Accent Tint Toggle (`accentTintEnabled`):**
  - Added a dedicated toggle in Appearance Settings and the Quick Appearance dialog to switch element surfaces, borders, buttons, and windows between active accent tinting and neutral achromatic styling.

---

## [v4.0.6] — `versionCode = 28` / `versionName = "4.0.6"`

### 🇷🇺 Русский
- **Единые максимальные внешние отступы для всех левитирующих окон:**
  - В `FloatingElevatedDialog`, `VersionPlanetaryModalSheet`, `ModeInfoModalDialog` и `QuickActionsCustomizerSheet` заданы общие константы внешних отступов (`20.dp` по горизонтали, `24.dp` по вертикали с `WindowInsets.safeDrawing`), единая максимальная ширина (`280.dp..520.dp`) и адаптивная максимальная высота (`rememberFloatingDialogMaxHeight()`, до `640.dp` с гарантированным запасом от краёв экрана).
  - Убрано переполнение высоты контейнера диалога (`fillMaxSize` внутри `Dialog`) и отсутствие верхнего предела `heightIn` у `FloatingElevatedDialog`, из-за которых высокие окна (такие как `Negai Premium` и `Быстрое оформление`) смещались вниз и прижимались вплотную к краю экрана.
- **Гарантированная автонумерация версий в CI (`release.yml`):**
  - В GitHub Actions workflow добавлена автоматическая проверка существующих релизов и тегов: если текущая версия `x.x.y` уже опубликована в репозитории, CI автоматически увеличивает патч-версию `y` и `versionCode` перед сборкой APK, чтобы никогда не перезаписывать предыдущий релиз.

### 🇬🇧 English
- **Unified Maximum Outer Margins Across All Levitating Windows:**
  - Standardized outer padding (`20.dp` horizontal, `24.dp` vertical with `WindowInsets.safeDrawing`), width bounds (`280.dp..520.dp`), and adaptive maximum height (`rememberFloatingDialogMaxHeight()`, up to `640.dp` with guaranteed screen-edge clearance) across `FloatingElevatedDialog`, `VersionPlanetaryModalSheet`, `ModeInfoModalDialog`, and `QuickActionsCustomizerSheet`.
  - Fixed dialog container height overflow (`fillMaxSize` inside `Dialog`) and missing `heightIn` cap on `FloatingElevatedDialog` that previously pushed tall dialogs (`Negai Premium`, `Quick Appearance`) right up against the bottom edge of the screen.
- **Guaranteed Automatic Version Increment in CI (`release.yml`):**
  - Added automatic release tag collision detection in GitHub Actions so that if `vX.Y.Z` already exists, the workflow automatically increments the patch version `Z` and `versionCode` prior to building the APK instead of overwriting the previous release.

---

## [v4.0.5] — `versionCode = 27` / `versionName = "4.0.5"`

### 🇷🇺 Русский
- **Исправление падения яркости экрана при запуске приложения (с включённой системной автояркостью):**
  - Из `AndroidManifest.xml` у `MainActivity` удалён статический атрибут `android:colorMode="hdr"`, из-за которого Android при запуске приложения переводил всё окно в режим HDR-композиции и принудительно занижал яркость SDR-интерфейса («SDR paper-white dimming») для резервирования запаса пиковой яркости HDR.
  - Теперь окно приложения запускается и работает в стандартном цветовом режиме `COLOR_MODE_DEFAULT` с системной автояркостью (`BRIGHTNESS_OVERRIDE_NONE`), а аппаратный `COLOR_MODE_HDR` и повышенная яркость включаются динамически в `PlayerScreen` исключительно во время воспроизведения реального HDR-видео (HDR10 / HLG / Dolby Vision) и сбрасываются обратно при выходе из него.

### 🇬🇧 English
- **Fixed Screen Brightness Dropping to Minimum on App Launch (with System Auto-Brightness Enabled):**
  - Removed the static `android:colorMode="hdr"` attribute from `MainActivity` in `AndroidManifest.xml`, which caused the Android display compositor to enter HDR mode immediately on app startup and dim the entire SDR UI ("SDR paper-white dimming") to reserve HDR peak headroom.
  - The app window now launches and operates in standard `COLOR_MODE_DEFAULT` with system auto-brightness (`BRIGHTNESS_OVERRIDE_NONE`), dynamically switching to `COLOR_MODE_HDR` in `PlayerScreen` strictly when playing actual HDR video (HDR10 / HLG / Dolby Vision).

---

## [v4.0.4] — `versionCode = 26` / `versionName = "4.0.4"`

### 🇷🇺 Русский
- **Сбалансированные отступы и системная безопасность (Safe Drawing) для всех левитирующих меню:**
  - Унифицированы внутренние и внешние отступы для всех левитирующих диалогов (`FloatingElevatedDialog`, `QuickThemeBottomSheet`, `ProSubscriptionLevitatingDialog`, `ModeInfoModalDialog`, `VersionPlanetaryModalSheet`, `QuickActionsCustomizerSheet`).
  - Добавлена обработка `WindowInsets.safeDrawing` и внешние отступы `24.dp` по вертикали, предотвращающие прилипание и наложение модальных окон на системную навигационную полосу и жесты Android.
  - Увеличены нижние отступы скроллируемого контента (до `16.dp` в меню быстрого оформления и `14.dp` в окне Premium) и закреплена комфортная высота кнопок действий (`48.dp`), устраняющая ощущение отсутствия отступа внизу.

### 🇬🇧 English
- **Harmonized Paddings and Safe Drawing Insets for all Levitating Menus:**
  - Unified internal and external margins across all levitating dialogs (`FloatingElevatedDialog`, `QuickThemeBottomSheet`, `ProSubscriptionLevitatingDialog`, `ModeInfoModalDialog`, `VersionPlanetaryModalSheet`, `QuickActionsCustomizerSheet`).
  - Added `WindowInsets.safeDrawing` and vertical `24.dp` outer margins, preventing floating cards from overlapping or sticking to the system navigation bar and Android gesture pill.
  - Increased bottom scroll content padding (up to `16.dp` in Quick Appearance and `14.dp` in Premium dialog) and ensured accessible action button height (`48.dp`), eliminating the missing bottom spacing issue.

---

## [v4.0.3] — `versionCode = 25` / `versionName = "4.0.3"`

### 🇷🇺 Русский
- **Исправление ошибки `FfmpegAudioRenderer error` и отображения `Mono` на DTS-HD Master Audio 7.1 (`.m2ts`):**
  - В парсере дескриптора `AudioAssetDescriptor` (`ETSI TS 102 114 Table 7-9` в `HdmvDtsHdReader`) поле `nuTotalNumChs` теперь считывается как 8-битное значение (`readBits(8) + 1`) вместо 4-битного, благодаря чему дорожка `DTS-HD Master Audio 7.1` (`8 каналов`, 48 кГц, 24 бит) корректно определяется как **7.1 Surround** (`8ch`), а не `Mono` (`1ch`).
  - Реализована атомарная буферизация пары `[DTS Core (0x7FFE8001) + Extension Substream (0x64582025)]` и фильтрация неполных/осиротевших фреймов расширения (`0x64582025`) после перемотки или возобновления воспроизведения, предотвращающая сбой `SwrContext` / `AVERROR_INVALIDDATA` в `FfmpegAudioRenderer` (`libdcadec`).
- **Устранение чёрного экрана со звуком и сбоев синхронизации Blu-ray BDAV (`.m2ts`):**
  - Исправлено выравнивание 4-байтного `TP_extra_header` на границах 188-байтных пакетов в `BdavStrippingExtractorInput.skipFully` и `resetPeekPosition`, а также увеличен диапазон поиска PCR-таймстампов до `3000` пакетов для точного бинарного поиска H.264 IDR-ключевых кадров.
  - Исключена отправка аудиосэмплов с фиктивным `PTS = 0` до получения первого PES PTS после перемотки и синхронизирован порядок освобождения `Surface` при переключении декодеров (`existingPlayer.release()` до привязки `Surface` к новому экземпляру плеера).

### 🇬🇧 English
- **Fixed `FfmpegAudioRenderer error` & `Mono` Channel Detection on DTS-HD Master Audio 7.1 (`.m2ts`):**
  - Fixed `nuTotalNumChs` in `HdmvDtsHdReader` (`ETSI TS 102 114 Table 7-9`) to read the full 8-bit field (`readBits(8) + 1`) instead of 4 bits, accurately reporting 8 channels (**7.1 Surround**) instead of `Mono` (`1ch`).
  - Enforced atomic `[DTS Core (0x7FFE8001) + Extension Substream (0x64582025)]` frame coalescing and skipped orphan post-seek extension substreams so `FfmpegAudioDecoder` (`libdcadec`) never receives mismatched or coreless XLL packets.
- **Fixed Intermittent Black Screen with Audio on Blu-ray BDAV (`.m2ts`):**
  - Fixed 4-byte `TP_extra_header` boundary alignment in `BdavStrippingExtractorInput.skipFully` and `resetPeekPosition`, and expanded the PCR search window to `3000` TS packets.
  - Prevented emitting audio samples with a synthetic `0L` PTS prior to the first PES PTS header and ensured synchronous `existingPlayer.release()` before re-binding the video `Surface` during decoder fallback.

---

## [v4.0.2] — `versionCode = 24` / `versionName = "4.0.2"`

### 🇷🇺 Русский
- **Кнопка звезды Negai Premium в верхней панели рядом с палитрой:**
  - Кнопка открытия окна покупки перенесена из списка категорий настроек в верхнюю панель (`TopAppBar`) в виде компактной золотой звезды рядом с иконкой палитры (`top_bar_premium_button`).
- **Ребрендинг PRO → Premium и аккуратные звёзды справа у платных элементов:**
  - Во всех диалогах, заголовках и бейджах обозначение `PRO` заменено на `Premium` (`Negai Premium`).
  - Иконки звёзд у платных палитр, стилей левитирующих окон и премиум-блоков уменьшены и перенесены на правую сторону; после покупки (`isProUnlocked = true`) звёзды автоматически исчезают.

### 🇬🇧 English
- **Top-Bar Negai Premium Star Button Next to Palette:**
  - Relocated the purchase trigger from the settings category list into a compact golden star button in the top bar right beside the palette icon (`top_bar_premium_button`).
- **PRO → Premium Rebranding & Compact Right-Aligned Stars:**
  - Replaced `PRO` with `Premium` (`Negai Premium`) across all user-facing dialogs, headers, and badges.
  - Positioned smaller gold star icons on the right side of paid palettes, dialog style rows, and locked blocks; the stars automatically disappear once Premium is unlocked.

---

## [v4.0.1] — `versionCode = 23` / `versionName = "4.0.1"`

### 🇷🇺 Русский
- **Демультиплексор Blu-ray BDAV (`.m2ts`) и исправление `Source Error` на DTS-HD Master Audio 7.1 (`0x86`):**
  - Реализован специализированный экстрактор **Blu-ray BDAV (`.m2ts`)** (`BdavM2tsExtractor`) с поддержкой 192-байтных пакетов (`4-byte TP_extra_header + 188-byte TS`) и таблицей дескрипторов **HDMV PMT** (`0x86` / `134` DTS-HD Master Audio XLL, `0x85` DTS-HD HRA, `0x82` HDMV DTS, `0x83` TrueHD, `0x84` E-AC-3, `0x80` Blu-ray LPCM).
  - Реализован парсер **ETSI TS 102 114 (`HdmvDtsHdReader`)**, объединяющий ядро DTS (`0x7FFE8001`) и расширение DTS-HD XLL (`0x64582025`) в единый 8-канальный фрейм для декодера **FFmpeg (`libdcadec`)**, устраняющий ошибку `Source Error` на файлах `dts_hd_ma_7-1_dredd_sound_check.m2ts`.
- **Карточка Negai PRO в настройках и левитирующее окно Google Play Billing:**
  - На главной странице настроек вверху добавлена кнопка **Negai PRO** с левитирующим окном (`ProSubscriptionLevitatingDialog`), содержащим описание премиум-функций и кнопку оплаты через **Google Play Billing** (тестовая заглушка активации с блокировкой всех PRO-функций до покупки).
  - Убран ручной переключатель PRO над темами; блок **«Комплиментарные палитры и стили диалогов (PRO)»** без активного PRO затемнён, отмечен золотой звездой вверху и открывает левитирующее окно покупки PRO при нажатии.
- **Левитирующее быстрое меню оформления:**
  - Всплывающее меню оформления по кнопке в верхней панели переведено с нижней шторки на левитирующий диалог (`FloatingElevatedDialog`).

### 🇬🇧 English
- **Blu-ray BDAV (`.m2ts`) Demuxer & DTS-HD Master Audio 7.1 (`0x86`) `Source Error` Fix:**
  - Added `BdavM2tsExtractor` supporting 192-byte Blu-ray BDAV packets (`4-byte TP_extra_header + 188-byte TS`) and HDMV PMT descriptors (`0x86` / `134` DTS-HD Master Audio XLL, `0x85` DTS-HD HRA, `0x82` HDMV DTS, `0x83` TrueHD, `0x84` E-AC-3, `0x80` Blu-ray LPCM).
  - Implemented `HdmvDtsHdReader` (ETSI TS 102 114) to coalesce DTS Core (`0x7FFE8001`) and DTS-HD Extension Substreams (`0x64582025`) into unified 8-channel frames for FFmpeg (`libdcadec`), fixing `Source Error` on `dts_hd_ma_7-1_dredd_sound_check.m2ts`.
- **Top Settings Negai PRO Banner & Google Play Billing Levitating Dialog:**
  - Added a top **Negai PRO** card in Settings opening `ProSubscriptionLevitatingDialog` with feature descriptions and a Google Play Billing purchase flow stub that gates PRO features until activated.
  - Removed the manual PRO preview switch above themes; styled the locked **PRO Complementary Palettes & Dialog Styles** block with a darkened card and top Star icon that opens `ProSubscriptionLevitatingDialog` on tap.
- **Levitating Quick Appearance Menu:**
  - Converted the top bar Quick Appearance popup from a bottom sheet to a levitating dialog (`FloatingElevatedDialog`).

---

## [v4.0] — `versionCode = 22` / `versionName = "4.0"`

### 🇷🇺 Русский
- **Бинауральное пространственное аудио HRTF (MIT KEMAR):**
  - Реализован процессор многоканального звука **5.1 / 7.1 / 6.1** (`HrtfBinauralAudioProcessor`) для наушников на базе открытого акустического датасета **MIT Media Lab KEMAR HRTF** (*Bill Gardner & Keith Martin, MIT License*).
  - Применяется межушная задержка по времени (**ITD**) и импульсная фильтрация ушной раковины (**Pinna FIR**) по азимутам всех окружающих каналов (`FL`, `FR`, `FC`, `LFE`, `BL`, `BR`, `SL`, `SR`) с переключателем в шторке аудиодорожек плеера и в разделе настроек «Аудио».
- **Поддержка потоков DTS-HD Master Audio 7.1 (24-бит) и защита от зависания при сбое:**
  - Включена поддержка дескрипторов **HDMV DTS-HD MA 7.1** (`FLAG_ENABLE_HDMV_DTS_AUDIO_STREAMS`), обработка **16 / 24 / 32-бит и Float PCM** в аудиоконвейере и мгновенное восстановление проигрывателя из `STATE_IDLE` после `Source Error` без необходимости выходить из плеера.
- **Точная индикация режима декодирования (HW / SW):**
  - При автоматическом переключении с неподдерживаемого аппаратного кодека на программный декодер (**FFmpeg / CPU**) кнопки **SW** в меню аудио/видео и индикатор в верхнем HUD мгновенно подсвечиваются активным состоянием (`effectiveVideoDecoderMode` / `effectiveAudioDecoderMode`).
- **Строгая автопривязка внешних дорожек и субтитров:**
  - Исправлено ложное связывание сторонних музыкальных альбомов (`.flac`, `.mp3`, `.wav` и др.) из одной папки с видеофайлом: теперь требуется точное совпадение базового имени файла (или совпадение с языковым/трековым суффиксом `.ru`, `.en`, `.rus`, `.audio`, `.dts`, `.ac3`).
- **3D IMAX и полноширинный интерфейс в зоне выреза камеры:**
  - Исправлено соотношение сторон для Full-SBS (`width >= height * 3.0`) и Full-TAB (`height >= width * 1.2`) в режиме **3D IMAX**.
  - Кнопка **«+85с Опенинг»** жёстко закреплена у правого края над таймлайном во всех режимах масштабирования.
  - Верхняя и нижняя панели HUD в ландшафтном режиме используют всю ширину экрана включая зону выреза камеры (`DisplayCutout`).
- **Левитирующие диалоги, живая иконка Monet и комплиментарные Pro-темы:**
  - Все всплывающие диалоги вне плеера переведены на левитирующие карточки (`FloatingElevatedDialog`).
  - Иконка **Monet** отображает реальные динамические цвета системных обоев Android 12+, значки остальных тем разделены на **основной цвет** и **дополнительный акцент**, добавлен блок **«Комплиментарные палитры (PRO)»**.
- **Подготовка к Google Play и лицензионная чистота:**
  - Оформлен закрытый проприетарный статус **Negai Player** (*Copyright © Negai, All Rights Reserved*) и полная атрибуция используемых открытых компонентов (**Anime4K** — MIT, **MIT KEMAR HRTF** — MIT, **FFmpeg** — LGPL v2.1+, **AndroidX Media3** — Apache 2.0).

### 🇬🇧 English
- **Binaural HRTF Spatial Audio (MIT KEMAR):**
  - Implemented real-time **5.1 / 7.1 / 6.1** headphone spatialization (`HrtfBinauralAudioProcessor`) powered by the **MIT Media Lab KEMAR HRTF** dataset (*Bill Gardner & Keith Martin, MIT License*) with Interaural Time Difference (**ITD**) delay lines and pinna FIR filtering, toggleable from the player audio sheet and Audio settings.
- **DTS-HD Master Audio 7.1 (24-bit) & Instant Error Recovery:**
  - Enabled **HDMV DTS-HD MA 7.1** stream descriptors (`FLAG_ENABLE_HDMV_DTS_AUDIO_STREAMS`), **16 / 24 / 32-bit & Float PCM** processing, and automatic `STATE_IDLE` recovery after `Source Error` without exiting the player.
- **Accurate HW / SW Decoder Fallback Indicator:**
  - When an unsupported hardware codec automatically falls back to software decoding (**FFmpeg / CPU**), the **SW** selector buttons and top HUD badge immediately reflect the active software state (`effectiveVideoDecoderMode` / `effectiveAudioDecoderMode`).
- **Strict External Audio & Subtitle Auto-Matching:**
  - Eliminated false-positive binding of unrelated music files (`.flac`, `.mp3`, etc.) in the same folder by enforcing strict base-filename stem matching (`ExternalTrackMatcher`).
- **3D IMAX & Full-Width Camera Cutout HUD:**
  - Fixed Full-SBS / Full-TAB aspect ratio calculation in **3D IMAX** mode, pinned the **+85s Skip Intro** button to the right edge of the timeline, and extended the player HUD across the camera cutout area.
- **Levitating Dialogs, Live Wallpaper Monet Badge & Pro Complementary Palettes:**
  - Replaced standard dialogs outside the player with levitating cards (`FloatingElevatedDialog`), updated the **Monet** icon to display live Android 12+ wallpaper colors, redesigned color badges into Primary outer ring + Secondary inner core, and added a **Complementary Palettes (PRO)** section.
- **Google Play Readiness & Legal Attribution:**
  - Clarified **Negai Player** proprietary closed-source license (*All Rights Reserved*) alongside complete open-source notices for **Anime4K** (MIT), **MIT KEMAR HRTF** (MIT), **FFmpeg** (LGPL v2.1+), and **AndroidX Media3** (Apache 2.0).

---

## [v3.6.6] — `versionCode = 21` / `versionName = "3.6.6"`

### 🇷🇺 Русский
- Встроен программный декодер аудио **FFmpeg (LGPL v2.1+)** (`org.jellyfin.media3:media3-ffmpeg-decoder`) для форматов **AC3, E-AC3, DTS, DTS-HD, Dolby TrueHD, MLP, PCM/WAV, FLAC, Vorbis, Opus и MP3**.
- Добавлен выбор режима декодирования аудио (**Авто / Аппаратный + FFmpeg / Программный FFmpeg**) с автоматическим переключением на программный декодер при отсутствии аппаратного кодека на устройстве.
- Реализовано автоматическое включение системного **HDR-режима окна** (`ActivityInfo.COLOR_MODE_HDR`) и максимальной пиковой яркости экрана при просмотре **HDR10 / HDR10+ / HLG / Dolby Vision** с возвратом яркости при паузе или выходе.
- Добавлены полные тексты лицензий **Anime4K (MIT)** и **FFmpeg (GNU LGPL v2.1+)** и синхронизация документации через GitHub REST API.

### 🇬🇧 English
- Integrated **FFmpeg (LGPL v2.1+)** software audio decoder (`org.jellyfin.media3:media3-ffmpeg-decoder`) for **AC3, E-AC3, DTS, DTS-HD, Dolby TrueHD, MLP, PCM/WAV, FLAC, Vorbis, Opus, and MP3**.
- Added selectable audio decoding modes (**Auto / Hardware + FFmpeg / Software FFmpeg**) with automatic software fallback when hardware codecs are missing.
- Implemented automatic system **window HDR mode** (`ActivityInfo.COLOR_MODE_HDR`) and peak screen brightness during **HDR10 / HDR10+ / HLG / Dolby Vision** playback.
- Added full **Anime4K (MIT)** and **FFmpeg (GNU LGPL v2.1+)** license files and non-blocking GitHub REST API synchronization.

---

## [v3.6] — `versionCode = 17` / `versionName = "3.6"`

### 🇷🇺 Русский
- Поддержка воспроизведения **HDR (HDR10, HLG, Dolby Vision)** с прямым выводом через 10-бит `SurfaceView`.
- Поддержка многоканального аудио **AC3, E-AC3, DTS, Dolby** с программным стерео-даунмиксом (`MultichannelDownmixAudioProcessor` ITU-R BS.775) и HDMI/SPDIF Passthrough.
- Автоматическая смена декодера на программный при сбое аппаратного декодирования и исправление расчёта битрейта AAC.
- Новые 3D-режимы **SBS (Горизонтальная стереопара)** и **TAB (Вертикальная стереопара)** для просмотра IMAX 3D видео на 2D-экране.

### 🇬🇧 English
- **HDR playback (HDR10, HLG, Dolby Vision)** via 10-bit `SurfaceView` direct hardware presentation.
- Multichannel **AC3, E-AC3, DTS, Dolby** audio support with ITU-R BS.775 software stereo downmix (`MultichannelDownmixAudioProcessor`) and HDMI/SPDIF passthrough.
- Automatic software decoder fallback on hardware error and accurate AAC bitrate detection fix.
- Added **3D SBS (Side-by-Side)** and **3D TAB (Top-and-Bottom)** projection modes for IMAX 3D videos on standard 2D screens.

---

## [v3.4.6] — `versionCode = 16` / `versionName = "3.4.6"`

### 🇷🇺 Русский
- Единый полупрозрачный стиль матового стекла для всех верхних и нижних элементов управления плеера.
- Переключение режимов подложек (**Стекло / Без фона / Акцент**) в быстром меню оформления и в меню «Ещё».
- Исправление автоповорота экрана по соотношению сторон видео, копирование названия файла долгим нажатием, бегущая строка заголовка, узкая боковая шкала громкости/яркости и пресеты масштаба интерфейса **90% / 100% / 110%**.

### 🇬🇧 English
- Unified translucent frosted glass styling across all top and bottom player HUD controls.
- Surface mode switching (**Glass / Frameless / Accent**) in both the top appearance popup and player More menu.
- Video aspect auto-orientation fix, long-press title copy, marquee title, slim side volume/brightness bar, and **90% / 100% / 110%** UI scale presets.

---

## [v3.4.4] — `versionCode = 14` / `versionName = "3.4.4"`

### 🇷🇺 Русский
- Изоляция жеста удержания кнопок плеера от ускорения 2x и точный вертикальный трекинг мини-меню от уровня кнопки.
- Левитирующие диалоги без крестика с кнопкой «Готово» и адаптация под чистый чёрный цвет **OLED**.
- Переименование сетевого раздела в **P2P** и вынесение публичных зеркал в отдельный загружаемый JSON-конфиг.
- Управление усилением звука (**Audio Boost до 200%**) аппаратными клавишами громкости и мультимедийными кнопками Bluetooth-гарнитур.

### 🇬🇧 English
- Quick actions hold gesture isolation from 2x speed boost and vertical mini-menu tracking from button origin.
- Floating dialogs with "Done" button and true **OLED** pitch-black adaptation.
- P2P rebranding with external on-demand JSON configuration.
- Hardware volume keys integration with **Audio Boost (up to 200%)** and Bluetooth headset media controls.

---

## [v3.4.3-pre] — `versionCode = 13` / `versionName = "3.4.3-pre"`

### 🇷🇺 Русский
- Стабилизация потокового воспроизведения и интерактивный выбор качества по бейджу в плеере.
- Адаптация частоты экрана **LTPO** до 24/30 Гц под FPS видео и снижение до минимума на паузе.
- Поддержка встроенных стилей/шрифтов **ASS/SSA** и автоопределение кодировок субтитров (UTF-8, Windows-1251, Shift-JIS).
- Раздельный выбор языков аудио и субтитров, усиление громкости строго свыше 100% и точная синхронизация А/В.

### 🇬🇧 English
- Streaming stability improvements and interactive quality badge selector in the player HUD.
- **LTPO** display refresh rate matching down to 24/30Hz and minimum Hz on pause.
- Embedded **ASS/SSA** fonts/styles and multi-charset subtitle detection (UTF-8, Windows-1251, Shift-JIS).
- Independent preferred audio/subtitle languages, volume boost strictly above 100%, and exact A/V resync.

---

## [v3.4.2-pre] — `versionCode = 12` / `versionName = "3.4.2-pre"`

### 🇷🇺 Русский
- Графический конвейер **Vulkan 1.1+ и OpenGL ES 3.0**, всплывающие карточки-справки для режимов апскейла, обновлённые темы **OLED** и **Тёмная**, полноэкранный режим с поддержкой выреза камеры.

### 🇬🇧 English
- **Vulkan 1.1+ and OpenGL ES 3.0** shader pipeline, floating info cards for upscale modes, updated **OLED** & **Dark** themes, and display cutout landscape support.

---

## [v3.4-pre1] — `versionCode = 11` / `versionName = "3.4-pre1"`

### 🇷🇺 Русский
- Оригинальные пресеты **Anime4K (Mode A / B / C)**, компактный HUD-оверлей технической информации «Инфо» и интерактивная планетарная модель с лицензиями открытого кода.

### 🇬🇧 English
- Original **Anime4K (Mode A / B / C)** shader presets, compact technical Info HUD overlay, and interactive planetary model with open-source licenses.
