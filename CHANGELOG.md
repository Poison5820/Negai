# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

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
