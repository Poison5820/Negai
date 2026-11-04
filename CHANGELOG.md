# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

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
