# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

## [v4.5.26]

### 🇬🇧 English
- **Instant Seeking & Smooth Resume on MKV, MP4 & 3D Blu-ray Video:**
  - Resolved an endless loading stall when scrubbing the timeline or resuming playback mid-episode on standard MKV and MP4 containers as well as 3D Blu-ray movie rips across both hardware and software video decoders.
  - Kept raw keyframe recovery checks strictly isolated to Blu-ray transport streams while seamlessly extracting clean 2D base frames from 3D multiview video tracks on any seek position.
- **Crystal-Clear Uncompressed Blu-ray LPCM Surround & Automatic Multichannel Track Selection:**
  - Eliminated loud white-noise hiss on uncompressed Blu-ray LPCM 5.1 and 7.1 soundtracks by accurately reading packet headers, preserving multichannel sample boundaries across transport packets, and mapping surround speaker channels cleanly.
  - Enabled automatic selection and immediate playback of high-bitrate multichannel audio tracks on phones, tablets, and stereo headphones without requiring manual track selection after opening a file.
- **Accurate Per-Episode Video Bitrate Telemetry:**
  - Cleared previous stream bitrate and codec readouts immediately when switching between files so high-bitrate Blu-ray values never carry over onto subsequent videos, and added accurate container bitrate estimation when explicit stream tags are absent.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.26)</b></summary>

<br>

- **Мгновенная перемотка и продолжение просмотра на MKV, MP4 и 3D Blu-ray:**
  - Полностью устранено бесконечное ожидание буферизации при перемотке ползунком и запуске воспроизведения не с самого начала на файлах MKV, MP4 и 3D-изданиях Blu-ray как на аппаратном, так и на программном декодере.
  - Проверка восстановления ключевого кадра оставлена строго для транспортных потоков Blu-ray, а из 3D-дорожек при перемотке в любую точку чисто выделяется основной 2D-ракурс без задержек.
- **Чистый несжатый звук Blu-ray LPCM 5.1 / 7.1 и автовыбор многоканальных дорожек:**
  - Полностью устранено громкое шипение на несжатых дорожках Blu-ray LPCM 5.1 и 7.1 за счёт точного разбора заголовков пакетов, строгого сохранения границ многоканальных сэмплов и правильной раскладки объёмных каналов.
  - Многоканальные аудиодорожки с высоким битрейтом теперь автоматически выбираются и звучат с первой секунды открытия файла на любых смартфонах, планшетах и в стереонаушниках без ручного включения в меню звука.
- **Честный расчёт и сброс битрейта видео при смене файлов:**
  - Параметры битрейта и кодеков предыдущего фильма мгновенно очищаются при переходе к следующему ролику без «залипания» старых значений, а для контейнеров без явной метки потока выполняется точная оценка реального битрейта видео.

</details>

## [v4.5.25]

### 🇬🇧 English
- **Searchable Language Picker & Unified Tabbed Floating Windows:**
  - Replaced cramped multi-chip language grids with a clean searchable floating window for choosing the app interface language as well as default audio and subtitle languages across Flat, Frosted Glass, and Liquid Glass themes.
  - Standardized equal-width segmented tabs and clean scroll boundaries across all multi-tab player windows so scrolling lists never overlap header tabs.
- **Crisp Form Labels & Full-Width Selection Cards Without Text Clipping:**
  - Placed input field titles cleanly above uniform single-line input capsules and aligned paired login and password fields across network and server settings.
  - Replaced narrow side-by-side option tiles with spacious full-width selection cards across appearance palettes, video decoders, motion smoothing, upscaling presets, audio decoders, voice cleanup modes, and orientation controls so titles and badges stay readable without truncation.
- **Unified Picture Window & Streamlined Audio, Subtitles & Playback Tools:**
  - Combined screen zoom, video decoder selection, Anime4K upscaling, motion smoothing, ambient video glow, and high refresh rate settings into a single three-tab Picture floating window accessible directly from quick actions.
  - Refined the three-tab Audio window (Tracks, Enhancements with volume boost, voice cleanup, and 3D spatial sound, and Codec & Delay with universal audio sync) and reorganized Playback Tools into dedicated Tools and Info tabs featuring picture-in-picture, background audio, live stream buffer status, file details, and playback diagnostics.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.25)</b></summary>

<br>

- **Плавающее окно выбора языка с поиском и ровные вкладки шторок:**
  - Узкие сетки кнопок выбора языка заменены удобным плавающим окном с мгновенным поиском для выбора языка интерфейса, а также языка озвучки и субтитров по умолчанию во всех темах оформления.
  - Во всех многовкладочных окнах плеера внедрены идеально выровненные равноширокие вкладки и чёткая граница прокрутки без наползания списка на шапку окна.
- **Чёткие подписи полей ввода и полноширинные карточки без обрезки текста:**
  - Заголовки полей ввода вынесены аккуратной строкой над полем единой высоты, а парные поля логина и пароля идеально выровнены между собой.
  - Узкие двухколоночные плитки заменены просторными полноширинными карточками для цветовых палитр, декодеров видео и звука, режимов плавности и улучшения кадра, очистки голоса и поворота экрана — длинные названия и пометки больше не обрезаются.
- **Единое окно «Картинка» и обновлённые окна «Аудио», «Субтитры» и «Инструменты»:**
  - Масштаб кадра, выбор видеодекодера, улучшение чёткости Anime4K, сглаживание движений, фоновая подсветка и высокая герцовка экрана объединены в удобное трёхвкладочное окно «Картинка».
  - Упорядочено окно «Аудио» из трёх вкладок (дорожки, улучшения со звуковым усилением, очисткой речи и объёмным звучанием, а также кодек и универсальная задержка звука) и обновлено окно «Инструменты» с отдельными вкладками быстрых функций просмотра и живой информации о потоке, буфере, файле и тесте производительности.

</details>

## [v4.5.24]

### 🇬🇧 English
- **True Aspect-Preserving Video Zoom & Scaling Modes:**
  - Fixed the Fill (Zoom) display mode so widescreen and standard videos expand to fill ultrawide phone and tablet screens with natural, undistorted character and object proportions instead of stretching horizontally.
  - Synchronized Fit, Fill, and Stretch geometry identically across both direct hardware HDR playback and real-time shader upscaling and motion smoothing.
- **Instant Blu-ray Seeking & Artifact-Free Software Decoding:**
  - Dramatically reduced post-seek recovery delays and eliminated brief audio stalls when scrubbing high-bitrate Blu-ray transport streams or switching decoder modes on the fly.
  - Eliminated green macroblock flashes when starting or seeking Blu-ray videos in software decoding mode by waiting cleanly for a complete reference frame while immediately recognizing open-group recovery points.
- **Crystal-Clear 7.1 Surround Audio, Memory Reuse & Honest Auto Sound Cleanup Status:**
  - Eliminated subtle crackling and timing drift on lossless 7.1 Blu-ray surround soundtracks during loud action peaks across both 3D headphone and stereo speaker spatial modes.
  - Reused stream buffer memory pools across seeks and decoder switches to prevent background memory churn and garbage collection spikes.
  - Updated the Audio menu so smart automatic voice cleanup clearly shows an unhighlighted bypass status on lossless and high-bitrate soundtracks rather than appearing active.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.24)</b></summary>

<br>

- **Честная геометрия масштабирования кадра («Вписать», «Заполнение», «Растянуть»):**
  - Исправлен режим «Заполнение»: теперь видео заполняет широкоформатный экран без чёрных полос со строгим сохранением естественных пропорций лиц и объектов за счёт аккуратной симметричной обрезки краёв, не превращаясь в горизонтальное растягивание.
  - Режимы «Вписать», «Заполнение» и «Растянуть» теперь работают абсолютно одинаково как на прямом аппаратном выводе HDR, так и при включённом улучшении чёткости и плавности движений.
- **Мгновенная перемотка Blu-ray и чистый старт в программном декодере:**
  - Многократно ускорено появление кадра после перемотки на тяжёлых фильмах Blu-ray с высоким битрейтом и устранены заминки звука при перемотке и смене режима декодера на лету.
  - Полностью устранены зелёные вспышки и квадраты при запуске и перемотке Blu-ray в программном режиме декодирования благодаря точному распознаванию точек восстановления кадра и отсечению неполных промежуточных кадров.
- **Чистейший звук 7.1 без хрипов, экономия памяти и точный статус автоочистки речи:**
  - Устранены остаточные похрипывания и микросбои синхронизации на громких боевых сценах с многоканальными дорожками 7.1 как в бинауральном 3D-режиме для наушников, так и на стереодинамиках.
  - Обеспечено бережное повторное использование памяти потокового буфера при перемотках и переключениях декодеров без фоновых скачков расхода памяти.
  - В меню звука умный автоматический режим очистки голоса больше не подсвечивается как активный и наглядно показывает статус обхода на качественных и несжатых аудиодорожках.

</details>

## [v4.5.23]

### 🇬🇧 English
- **Crisp, Artifact-Free Motion Smoothing Across All Modes:**
  - Eliminated jelly warping, smeared intermediate frames, and double-contour ghosting during fast action, camera pans, dialogue animations, and explosions across Cinema, Smooth 60, Max Refresh Rate, and Anime motion modes.
  - Preserved natural smoothness on variable-cadence anime scenes drawn on twos and threes without abrupt motion dropouts or rhythmic judder.
  - Added dual-frame line contour protection across every motion smoothing preset so character outlines and fine details stay razor-sharp.
- **Full-Speed Player Interface & Locked High Refresh Rate:**
  - Restored full panel frame rate when motion smoothing and real-time upscaling are active, eliminating frame-rate halving and micro-stutters when opening floating windows or scrubbing the timeline in Liquid Glass and Frosted Glass themes.
  - Locked the display to its peak refresh rate throughout motion-smoothed playback so showing or hiding the player controls never triggers screen mode flashes or cadence hiccups.
- **Universal Multichannel Stereo Downmixing & Build Reliability:**
  - Ensured every audio channel layout from mono up to 7.1 surround mixes cleanly to stereo with balanced headroom and zero distortion when virtual surround is turned off.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.23)</b></summary>

<br>

- **Чёткое сглаживание движений без «желе» и каши между кадрами:**
  - Полностью устранены искажения фона, двоение контуров и размытые промежуточные кадры на резких движениях, панорамах камеры, мимике персонажей и спецэффектах во всех режимах плавности («Кино», «Плавные 60», «Максимум экрана» и «Аниме»).
  - Сохранена непрерывная плавность на классической аниме-рисовке с переменной частотой кадров без внезапных отключений сглаживания и рывков ритма.
  - Защита чёткости контуров и тонких линий теперь работает во всех режимах сглаживания движений, а не только в профиле для аниме.
- **Плавный стеклянный интерфейс и стабильная максимальная герцовка:**
  - Устранено проседание частоты кадров видео и интерфейса при одновременной работе шейдерного улучшения картинки, сглаживания движений и тем «Жидкое стекло» / «Матовое стекло».
  - Частота обновления экрана надёжно удерживается на пиковом режиме без переключений и подёргиваний при появлении и скрытии панели управления плеера.
- **Надёжное сведение любых многоканальных дорожек:**
  - Обеспечено чистое сведение всех многоканальных форматов звука от моно до 7.1 в стерео с защитой от перегрузок и искажений громкости.

</details>

## [v4.5.22]

### 🇬🇧 English
- **Refreshed Defaults & Smart Automatic Preferences:**
  - Set the signature **Sakura** accent theme as the default palette for fresh installations.
  - Added automatic audio and subtitle language matching that dynamically follows your chosen app interface language (Russian, English, or system language).
  - Enabled smart automatic sound cleanup by default for low-bitrate voiceovers while keeping manual DSP overrides deselected until explicitly chosen, with one-tap toggle-off back to Auto mode.
- **Unified Interface Alignment & Streamlined 7-Category Settings:**
  - Unified horizontal screen margins and full-width card alignment across every library tab, folder browser, top bar, and Settings page in both portrait and landscape orientations.
  - Reorganized global Settings into 7 focused categories (General & Appearance, Controls & Playback, Video & Enhancements, Audio, Subtitles, Network & Storage, and Device & About) with clean diagnostics tools placed inside Device & About and File Information.
- **Tabbed Player Sheets & Pure-JVM Audio Mixing:**
  - Organized the player's Playback Tools, Audio, and Subtitles floating windows into equal-width segmented tabs for faster navigation without long scrolling.
  - Upgraded the surround-to-stereo downmixer for reliable 1–8 channel mixing with smooth overload protection.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.22)</b></summary>

<br>

- **Обновлённые настройки по умолчанию и умная автоматика:**
  - Фирменная цветовая палитра **«Сакура»** установлена в качестве стандартного акцента для новых установок.
  - Добавлен автоматический подбор языка озвучки и субтитров «Как язык интерфейса», который динамически подстраивается под русский, английский или системный язык приложения.
  - Умная автоочистка звука включена по умолчанию только для низкобитрейтных дорожек; ручные профили не выделяются до явного выбора, а повторное нажатие по активному профилю возвращает в режим Авто.
- **Единая ширина интерфейса и компактные Настройки из 7 разделов:**
  - Выровнены отступы и единая ширина всех карточек, фильтров и верхней панели на экранах Папок, Истории, Торрентов и Настроек в портретном и альбомном режимах.
  - Глобальные Настройки перегруппированы в 7 логичных категорий («Общие и внешний вид», «Управление и воспроизведение», «Видео и улучшение кадра», «Звук», «Субтитры», «Сеть, P2P и хранилище», «Устройство и о приложении»), а диагностические инструменты перенесены в «Устройство и о приложении» и «Сведения о файле».
- **Вкладки в окнах плеера и обновлённое сведение звука:**
  - Окна инструментов просмотра, аудио и субтитров разделены на удобные равноширокие вкладки без длинной прокрутки.
  - Обновлён модуль сведения многоканального звука (от 1 до 8 каналов в стерео) с чистой защитой от перегрузки.

</details>

## [v4.5.21]

### 🇬🇧 English
- **True 7.1 Surround Recognition & Decoding for Blu-ray DTS-HD Master Audio:**
  - Fixed Blu-ray `.m2ts` movies with a 2.0 stereo core and 7.1 surround extension so the full 8-channel lossless audio stream is assembled and decoded instead of falling back to 2-channel stereo core audio.
  - Updated the player audio selector, top status bar, and File Info modal to immediately display the true 7.1 surround channel layout and high-definition audio format.
- **Pop-Free Video Transitions & Clean Surround Downmix:**
  - Eliminated audio crackling, clicks, and residual filter noise when switching between videos, changing audio tracks, or toggling sound enhancements.
  - Balanced 5.1 and 7.1 surround-to-stereo downmix levels when headphone virtual surround is turned off so loud action scenes and bass effects play cleanly without digital clipping or distortion.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.21)</b></summary>

<br>

- **Честное распознавание и декодирование 7.1-канального звука DTS-HD Master Audio на дисках Blu-ray:**
  - Исправлено чтение дорожек Blu-ray `.m2ts` со стерео-ядром 2.0 и расширением 7.1 — теперь плеер собирает и декодирует полноценный 8-канальный поток без потерь вместо урезанного 2-канального базового ядра.
  - В меню выбора аудиодорожек, верхней панели статуса и окне «Информация о файле» теперь сразу отображается реальная конфигурация 7.1 Surround и точный формат звука высокой чёткости.
- **Переключение видео без хрипов и чистое сведение объёмного звука:**
  - Полностью устранены щелчки, хрипы и остаточные помехи аудиофильтров при переходе от одного видео к другому, смене звуковой дорожки и переключении улучшений звука.
  - Сбалансированы уровни громкости при сведении многоканального звука 5.1 и 7.1 в стерео без пространственного режима — громкие динамичные сцены и низкие частоты звучат чисто и без перегрузок.

</details>

## [v4.5.20]

### 🇬🇧 English
- **Artifact-Free Blu-ray `.m2ts` Playback & Instant High-Bitrate Seeking:**
  - Eliminated green screen flashes and corrupted macroblocks when starting or seeking Blu-ray `.m2ts` discs and transport streams by enforcing strict keyframe synchronization and discarding incomplete intermediate frames until the first clean keyframe arrives.
  - Dramatically accelerated post-seek frame recovery on high-bitrate `.m2ts` and Ultra HD movies by expanding transport stream timestamp scanning and snapping directly to clean sync keyframes instead of decoding dozens of heavy frames in the background.
- **Accurate Live Decoder Status & 4K 10-Bit HDR Hardware Protection:**
  - Fixed the player quick action decoder badge so it always reflects the actual running hardware or software video decoder in real time, and ensured tapping Retry or toggling the decoder button cleanly restores hardware acceleration.
  - Protected demanding 4K 10-bit HDR and HDR10+ movies from accidentally starting on slow software decoding and eliminated mid-playback decoder memory errors on 4K HDR10+ releases.
- **Zero-Stutter Liquid Glass Scrubbing & Benchmark Precision:**
  - Eliminated memory churn and garbage collection micro-stutters while dragging the timeline seekbar in Liquid Glass mode and during dual-layer Dolby Vision playback.
  - Improved accuracy across the final stages of the built-in player benchmark, preserving true hardware video codec reporting, steady interactive scrubbing measurement, surround downmix channel reporting, and isolated 8-channel spatial audio stress profiling.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.20)</b></summary>

<br>

- **Чистое воспроизведение дисков Blu-ray `.m2ts` без зелёных вспышек и мгновенная перемотка:**
  - Полностью устранены зелёные экраны и рассыпание картинки при запуске и перемотке дисков Blu-ray `.m2ts` и транспортных потоков за счёт строгой привязки к полноценным ключевым кадрам и отсечения неполных промежуточных кадров после сброса буфера.
  - Многократно ускорен показ первого кадра после перемотки на тяжёлых высокобитрейтных фильмах `.m2ts` и Ultra HD благодаря расширенному окну поиска меток времени и мгновенному переходу к ближайшему чистому ключевому кадру.
- **Честная индикация режима декодера и защита видео 4K 10-бит HDR:**
  - Исправлен индикатор декодера на нижней панели плеера — теперь он всегда точно показывает реально работающий в данный момент аппаратный или программный декодер, а кнопка повтора и переключение режима гарантированно возвращают полноценное аппаратное ускорение.
  - Добавлена защита тяжёлых фильмов 4K 10-бит HDR и HDR10+ от случайного запуска на медленном программном декодере и устранены сбои памяти видеодекодера во время просмотра роликов 4K HDR10+.
- **Плавное ведение ползунка в «Жидком стекле» и точность встроенного бенчмарка:**
  - Устранены лишние выделения памяти и микрофризы сборки мусора при непрерывном ведении ползунка таймлайна в стиле «Жидкое стекло», а также при просмотре фильмов Dolby Vision.
  - Повышена точность заключительных этапов встроенного бенчмарка плеера: сохраняется реальный профиль аппаратного видеодекодера в итоговом отчёте, обеспечена непрерывная активность панели управления на этапе интерактивной перемотки, уточнён отчёт выходных аудиоканалов при сведении объёмного звука и полностью изолирован синтетический тест 8-канальной аудиообработки.

</details>

## [v4.5.19]

### 🇬🇧 English
- **Flawless Dual-Layer 4K Blu-ray Dolby Vision Profile 7 Playback:**
  - Fixed playback crashes and resolution conflicts on dual-layer 4K Blu-ray Dolby Vision Profile 7 (FEL and MEL) releases in `.m2ts`, `.ts`, `.mp4`, and `.mkv` containers by completely stripping embedded 1080p enhancement layer streams before frames reach the hardware 4K video decoder.
  - Enforced dedicated hardware 4K decoding for Dolby Vision Profile 7 base layers and blocked unintended fallback to slow software decoding on 4K HDR and Dolby Vision movies.
- **High-Bitrate 4K HDR10+ Stability & Instant HDR Display Activation:**
  - Optimized hardware video decoder input buffer memory allocation for 4K Ultra HD streams, resolving decoder initialization memory errors on high-bitrate 4K HDR10+ videos with lossless surround audio.
  - Added pre-start HDR container detection and intelligent 10-bit HDR recognition for videos missing standard container color tags, activating the HDR display pipeline from the very first frame without screen flashes or surface restarts.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.19)</b></summary>

<br>

- **Безошибочное воспроизведение двухслойных фильмов 4K Blu-ray Dolby Vision Profile 7:**
  - Устранены сбои декодера и конфликты разрешений при запуске двухслойных релизов 4K Blu-ray Dolby Vision Profile 7 (FEL и MEL) в контейнерах `.m2ts`, `.ts`, `.mp4` и `.mkv` за счёт полной очистки вложенного дополнительного потока 1080p перед подачей кадров на аппаратный 4K-декодер.
  - Закреплено строго аппаратное декодирование базового 4K-слоя для видео Dolby Vision Profile 7 и заблокирован нежелательный автоматический переход на медленный программный декодер на фильмах 4K HDR и Dolby Vision.
- **Стабильность тяжёлых роликов 4K HDR10+ и мгновенное включение экрана HDR:**
  - Оптимизировано выделение памяти входных буферов аппаратного декодера для потоков 4K Ultra HD, что полностью устраняет ошибки нехватки памяти при запуске высокобитрейтных видео 4K HDR10+ с многоканальным звуком.
  - Добавлено предварительное распознавание HDR до старта декодера и умное определение 10-битного HDR-профиля на файлах без стандартных цветовых метаданных контейнера — экран сразу открывается в режиме HDR с первого кадра без моргания и пересоздания видеоповерхности.

</details>

## [v4.5.18]

### 🇬🇧 English
- **Unified Visual Theme Synchronization Across All Player Floating Windows:**
  - Synchronized every floating sheet and modal window in the video player — including Audio Tracks, Subtitles, Playback Speed, Screen Zoom, Episodes, Chapters, Video Quality, Decoder Mode, Anime4K Upscaling, Motion Smoothing, Volume Boost, Sleep Timer, Skip Intro, Technical File Info, Quick Action Button Customization, and the Pipeline Benchmark — so they all follow the active player visual style identically to the Playback Tools menu.
  - Selecting the **Flat** or **Frameless** player style now renders all player modals with crisp, opaque tonal cards and clean borders without forced glass translucency, while **Frosted Glass** and **Liquid Glass** uniformly apply deep background blur and 3D edge refraction across every window and hold-gesture quick menu.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.18)</b></summary>

<br>

- **Единая синхронизация стиля оформления во всех плавающих окнах плеера:**
  - Приведены к единому источнику темы все всплывающие окна и шторки видеоплеера: выбор озвучки, субтитры, скорость воспроизведения, масштаб экрана, список серий, главы, качество потока, режим декодера, улучшение контуров Anime4K, сглаживание движений, усиление звука, таймер сна, пропуск заставки, техническая информация о файле, настройка быстрых кнопок и встроенный бенчмарк.
  - При выборе **плоского** или **безрамочного** стиля все окна плеера теперь отображаются в честном плоском оформлении с плотными контрастными карточками без принудительной стеклянной прозрачности, а в режимах **«Матовое стекло»** и **«Жидкое стекло»** все окна и быстрые меню удержания единообразно используют глубокое размытие фона и 3D-преломление краёв.

</details>

## [v4.5.17]

### 🇬🇧 English
- **Deep Optical Glass Blur & Refraction in All Floating Windows:**
  - Restored continuous live background capture behind open modals so every floating window, settings sheet, and dialog in both the video player and media library displays rich, translucent Frosted Glass and Liquid Glass blur with 3D edge refraction over live video and artwork.
  - Tuned modal glass translucency and system window blur so underlying video colors and scene lighting remain vividly visible without sacrificing text contrast.
- **Full-HUD Liquid Glass & Frosted Glass Optics & Instant Scrubbing Response:**
  - Extended real-time Liquid Glass lens refraction and Frosted Glass blur to every individual player HUD element, including top bar buttons, resolution and HDR badges, center playback transport buttons, chapter and A-B loop pills, and bottom quick action buttons.
  - Accelerated video backdrop capture during timeline scrubbing and unified ambient edge lighting with glass backdrop sampling into a single shared pass, eliminating refraction lag on the seekbar and transport buttons.
  - Background player HUD buttons now fade out completely whenever a floating modal sheet opens, keeping the screen clean and distraction-free.
- **Expanded 14-Stage Built-In Player Benchmark:**
  - Added two new interactive UI stress stages to the built-in player benchmark: **Stage 10 (HUD Motion & Liquid Scrubbing)** testing continuous timeline scrubbing and HUD fade transitions over live video, and **Stage 11 (Floating Glass Modal + 120Hz Scroll)** measuring real-time frame pacing and GPU load while smoothly scrolling a blurred floating window over playing video.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.17)</b></summary>

<br>

- **Глубокое оптическое размытие и преломление во всех плавающих окнах:**
  - Восстановлен непрерывный захват живой подложки сцены при открытии всплывающих окон: теперь все модальные шторки и диалоги в плеере и медиатеке отображают настоящее глубокое размытие и краевое преломление «Жидкого стекла» и «Матового стекла» поверх идущего видео и обложек.
  - Сбалансирована прозрачность стеклянных окон для выразительной передачи цветов видео на фоне при сохранении безупречной читаемости текста.
- **Оптика Жидкого и Матового стекла на всех кнопках HUD и мгновенный отклик перемотки:**
  - Живое оптическое преломление и матовое размытие подключены ко всем элементам управления плеера: верхним кнопкам, индикаторам разрешения и HDR, всем пяти центральным кнопкам пульта, пилюлям глав и повтора A–B, а также кнопкам нижней панели быстрых действий.
  - Ускорено обновление стеклянной подложки во время ведения ползунка таймлайна, а расчёт фоновой подсветки краёв объединён с захватом кадра для стекла без дублирования операций и без отставания преломления от видео.
  - При открытии любого плавающего окна кнопки основного интерфейса плеера теперь плавно и полностью скрываются, не просвечивая под окном.
- **Расширенный 14-этапный встроенный бенчмарк плеера:**
  - Во встроенный бенчмарк добавлены два новых интерактивных этапа проверки интерфейса поверх видео: **Этап 10 (Динамика HUD и скраббинг стекла)** с автоматическим ведением ползунка перемотки и плавным затуханием панелей, а также **Этап 11 (Плавающее окно: Блюр + Скролл 120 Гц)** с непрерывной прокруткой размытого плавающего окна в реальном времени.

</details>

## [v4.5.16]

### 🇬🇧 English
- **Pitch-Accurate Multichannel Dolby 5.1/7.1 & Seamless Spatial Audio Switching:**
  - Enforced a consistent stereo output stage across all multichannel surround tracks, completely eliminating fast-forwarded audio pitch shifts and rebuffering pauses when toggling 3D headphone binaural audio or speaker crosstalk cancellation on the fly.
  - Routed multichannel surround formats in automatic decoder mode directly through discrete multichannel decoding so all surround channels are preserved before downmixing or 3D spatial processing.
- **Instant Video Motion on Seek & Zero-Overhead Ambient Glass Sampling:**
  - Eliminated frozen video frames after seeking or starting complex anime episodes by preventing forward keyframe jumps past the target playback position.
  - Coordinated background frame capture between ambient edge lighting and liquid/frosted glass refraction with a shared lock and zero-allocation callbacks, eliminating memory pressure spikes.
- **Persistent Benchmark Crash Recovery & Hardware Telemetry Accuracy:**
  - Added a persistent crash recovery journal to the built-in player benchmark that captures the active test stage, memory state, system exit reason, and event trace if a session is interrupted, offering one-tap export on restart.
  - Improved hardware video decoder detection, baseline hardware stage preparation, and isolated audio stress testing in the player benchmark.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.16)</b></summary>

<br>

- **Стабильный многоканальный звук Dolby 5.1/7.1 и бесшовное переключение 3D-аудио:**
  - Обеспечена неизменная двухканальная геометрия выходного звукового тракта на всех многоканальных дорожках, что полностью устраняет эффект ускоренного воспроизведения звука и паузы буферизации при включении или выключении бинаурального звука для наушников и пространственного режима для стереодинамиков на лету.
  - Многоканальные дорожки объёмного звука в автоматическом режиме декодера всегда направляются через дискретное многоканальное декодирование с сохранением всех каналов сцены.
- **Мгновенный старт движения кадра при перемотке и бережный захват фонового свечения:**
  - Устранено трёхсекундное замирание картинки после перемотки и при запуске серий со сложной структурой ключевых кадров за счёт запрета перескока вперёд за выбранную точку времени.
  - Захват кадра для фоновой подсветки краёв экрана и преломления стеклянных панелей объединён через общий синхронизатор без создания лишних объектов в памяти.
- **Журнал восстановления после сбоев и точная телеметрия в бенчмарке плеера:**
  - Во встроенный бенчмарк плеера добавлен энергонезависимый журнал диагностики, который сохраняет текущий этап, состояние памяти, системную причину завершения процесса и цепочку событий при аварийной остановке с возможностью просмотра и экспорта отчёта после перезапуска.
  - Повышена точность распознавания аппаратных видеодекодеров, автоматического возврата к аппаратному режиму перед первым этапом и чистоты замеров процессорного стресс-теста звука.

</details>

## [v4.5.15]

### 🇬🇧 English
- **Thread-Safe Decoder Initialization & Zero Playback Crashes:**
  - Removed all background thread player state access inside the video decoder pipeline, completely eliminating `IllegalStateException` crashes during hardware and software decoder initialization.
  - Ensured all video format, timeline, and color space state transitions execute exclusively on the main user interface thread.
- **Scrollable Error Diagnostics with Deep Root Cause Identification:**
  - Added vertical scrolling and expanded text capacity to the player error card, preventing truncation of lengthy diagnostic information.
  - Implemented recursive root cause inspection that bypasses generic format wrappers to reveal exact system exception details.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.15)</b></summary>

<br>

- **Потокобезопасная инициализация декодера и стабильный запуск:**
  - Полностью исключены обращения к состоянию плеера из фонового потока декодера, что полностью устраняет ошибку `IllegalStateException` и сбои инициализации аппаратного и программного декодеров при открытии любых файлов.
  - Все обновления параметров видео, таймлайна и цветовых пространств перенесены строго в главный поток интерфейса.
- **Прокручиваемая карточка ошибки и глубокая диагностика:**
  - Добавлена плавная вертикальная прокрутка и увеличен лимит строк в окне ошибки плеера, благодаря чему длинные системные сообщения не обрезаются.
  - Реализован рекурсивный поиск первопричины сбоя, отображающий точный класс и текст системного исключения за общими заголовками форматов.

</details>

## [v4.5.14]

### 🇬🇧 English
- **Rock-Solid Video Surface Lifecycle & Playback Stability:**
  - Stabilized video surface binding across playback start, pause, and controls visibility changes, ensuring the rendering surface is never prematurely destroyed or swapped while the video decoder initializes.
  - Eliminated redundant surface reconnection events on video aspect ratio changes and protected graphics pipelines with strict single-release teardown.
  - Guaranteed main-thread surface attachment and detachment, eliminating background thread state conflicts.
- **Robust Software Decoding & Comprehensive Error Diagnostics:**
  - Preserved default input buffer sizing for standard 1080p streams across hardware and software decoders, eliminating decoder configuration failures.
  - Enabled automatic software fallback for high-bitrate and Blu-ray streams without arbitrary resolution caps.
  - Enhanced error diagnostics with detailed system codec codes and underlying exception descriptions.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.14)</b></summary>

<br>

- **Полная стабилизация жизненного цикла видеоповерхности:**
  - Устранено нежелательное пересоздание и переключение видеоповерхности при старте воспроизведения, паузе и скрытии элементов управления, что защищает декодер от потери рабочей поверхности в момент инициализации.
  - Убраны лишние повторные привязки поверхности при изменении соотношения сторон кадра и внедрена строгая однократная очистка графических ресурсов.
  - Гарантировано подключение и отключение видеоповерхности строго в главном потоке интерфейса без фоновых конфликтов.
- **Надёжный программный декодер и подробная диагностика ошибок:**
  - Сохранены стандартные размеры входного буфера для видео 1080p на аппаратных и программных декодерах, предотвращая сбои настройки видеокодека.
  - Разрешён автоматический переход на программный декодер для тяжёлых файлов Blu-ray и видео с высоким битрейтом без искусственных ограничений.
  - В карточку ошибки добавлено подробное отображение системных кодов декодера и точных причин сбоя.

</details>

## [v4.5.13]

### 🇬🇧 English
- **Seamless Decoder Switching & H.264 Playback Stability:**
  - Eliminated video surface ownership conflicts when opening media or switching between Hardware (Auto / HW+) and Software (SW) decoders on the fly, preventing decoder initialization errors on standard 720p, 1080p, and 4K H.264 files.
  - Preserved native memory limits for software H.264 decoding so Software (SW) fallback and manual SW mode initialize reliably across all Android devices.
- **Accurate Player Benchmark Telemetry & Cleaner HUD:**
  - Restored accurate rendered video frame and FPS reporting across all stages of the built-in player benchmark, alongside full glass backdrop sampling telemetry and gentler background frame capture.
  - Kept center playback transport buttons strictly hidden whenever an error recovery card is displayed on screen.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.13)</b></summary>

<br>

- **Бесшовное переключение декодеров и стабильный запуск H.264:**
  - Полностью устранены конфликты доступа к видеоповерхности при открытии файлов и переключении между аппаратным («Авто» / «HW+») и программным («SW») декодерами на лету, что исключает ошибки инициализации на обычных видео 720p, 1080p и 4K.
  - Сохранены штатные лимиты памяти для программного декодирования H.264, благодаря чему режим «SW» и автоматический резервный декодер запускаются безотказно на любых устройствах.
- **Точные замеры в бенчмарке плеера и чистый экран ошибки:**
  - Восстановлен точный подсчёт отрисованных кадров и реального FPS на всех этапах встроенного бенчмарка плеера, добавлен полный учёт преломления стеклянного интерфейса и снижена фоновая нагрузка при открытой панели управления.
  - Центральные кнопки паузы и перемотки теперь гарантированно скрыты при отображении карточки ошибки воспроизведения.

</details>

## [v4.5.12]

### 🇬🇧 English
- **1080p 2D H.264 & Blu-ray Playback Hotfix:**
  - Fixed a regression that caused standard 1080p 2D H.264 videos in MKV, MP4, and Blu-ray .m2ts containers to fail with a video decoder initialization error at launch.
  - Preserved original 2D video stream headers untouched while keeping 3D MVC stream filtering strictly isolated to stereoscopic 3D releases.
  - Tuned 1080p hardware decoder memory limits, enabled full software decoder priority when Software (SW) mode is selected, and restored automatic software fallback on demanding high-bitrate streams.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.12)</b></summary>

<br>

- **Исправление запуска стандартных 2D-видео H.264 1080p и дисков Blu-ray:**
  - Устранён сбой инициализации видеодекодера при открытии обычных роликов и фильмов H.264 1080p в форматах MKV, MP4 и Blu-ray .m2ts.
  - Обычные 2D-потоки теперь передаются декодеру в первозданном виде без пересборки заголовков и пакетов, а фильтрация стереоскопических данных включается исключительно на реальных фильмах 3D MVC.
  - Оптимизирован объём памяти буферов для 1080p-видео, восстановлен приоритет программного декодера в режиме «SW» и возвращён бесшовный резервный переход на программное декодирование на тяжёлых потоках.

</details>

## [v4.5.11]

### 🇬🇧 English
- **Consolidated Playback & Interface Stability Release:**
  - Delivered full release synchronization for Blu-ray 3D MVC and multi-resolution H.264 video playback across both Hardware (Auto / HW+) and Software (SW) decoder modes without startup errors.
  - Finalized the centered, overlap-free player error recovery card with non-destructive decoder switching alongside spacious floating windows expanding up to 90% of screen height with a pinned Done confirmation button.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.11)</b></summary>

<br>

- **Консолидированный стабильный выпуск плеера и интерфейса:**
  - Финализирована поддержка фильмов Blu-ray 3D MVC и многоразрешающих видеопотоков H.264 с чистым запуском как в аппаратном («Авто» / «HW+»), так и в программном («SW») режиме декодера без ошибок инициализации.
  - Объединены центрированная карточка ошибки воспроизведения без пересечения с кнопками плеера и бережным переключением декодеров, а также просторные всплывающие окна высотой до 90% экрана с закреплённой кнопкой подтверждения «Готово».

</details>

## [v4.5.10]

### 🇬🇧 English
- **Adaptive Player Error Card Layout & Release Stabilization:**
  - Refined button flow arrangement on the centered playback error card so retry and decoder recovery actions wrap cleanly across compact screens and multi-window layouts without clipping.
  - Verified full compatibility with Blu-ray 3D MVC and multi-resolution H.264 streams across both Hardware (Auto / HW+) and Software (SW) decoder modes.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.10)</b></summary>

<br>

- **Адаптивная вёрстка карточки ошибки в плеере и стабилизация сборки:**
  - Улучшено расположение кнопок повтора и смены декодера на центрированной карточке ошибки плеера — кнопки аккуратно переносятся и выравниваются на компактных экранах и в режиме разделённого окна без обрезки текста.
  - Проверена полная совместимость с фильмами Blu-ray 3D MVC и многоразрешающими видео H.264 как в аппаратном («Авто» / «HW+»), так и в программном («SW») режиме декодирования.

</details>

## [v4.5.9]

### 🇬🇧 English
- **Blu-ray 3D MVC & Multi-Resolution H.264 Playback Compatibility:**
  - Fixed video decoder startup failures on Blu-ray 3D MVC movies and multi-resolution H.264 streams so the full 1080p base picture plays smoothly in both Hardware (Auto / HW+) and Software (SW) decoder modes without crashing at launch.
  - Tuned software video decoder buffer sizing and automatic track selection so secondary 3D extension streams never block playback.
- **Clean Player Error Dialog & Non-Destructive Decoder Recovery:**
  - Centered the in-player playback error card and automatically hid the floating center transport buttons whenever an error is displayed so retry controls are never covered or blocked.
  - Upgraded the decoder recovery button on the error card to switch cleanly between Software (SW) and Hardware (HW+) decoding for the current video without overwriting your saved global Auto preference.
- **Spacious Floating Windows & Unified Done Confirmation Footers:**
  - Expanded all floating windows, track selectors, file detail dialogs, and player settings sheets up to 90% of available screen height in both portrait and landscape orientations with smooth vertical scrolling.
  - Standardized a full-width Done confirmation button at the bottom of every floating window and removed developer benchmark clutter from the Appearance settings page.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.9)</b></summary>

<br>

- **Совместимость с фильмами Blu-ray 3D MVC и многоразрешающими видео H.264:**
  - Устранены ошибки запуска видеодекодера на релизах Blu-ray 3D MVC и тестовых потоках H.264 с динамической сменой разрешения — полная базовая картинка 1080p стабильно воспроизводится как в аппаратном («Авто» / «HW+»), так и в программном («SW») режиме декодера.
  - Оптимизирован размер входного буфера для программного декодера видео, а дополнительные служебные потоки второго ракурса 3D больше не мешают старту воспроизведения.
- **Аккуратное окно ошибки плеера и умное восстановление декодера:**
  - Карточка ошибки воспроизведения теперь выравнивается строго по центру рабочей области, а центральные кнопки перемотки и паузы автоматически скрываются на время показа ошибки и больше никогда не перекрывают кнопки действий.
  - Кнопка смены декодера на карточке ошибки теперь предлагает переключение на программный («SW») или возврат к аппаратному («HW+») декодеру в зависимости от текущего режима, не перезаписывая вашу основную настройку «Авто».
- **Просторные всплывающие окна до 90% высоты экрана и единая кнопка «Готово»:**
  - Все плавающие окна, списки дорожек, свойства файлов и шторки настроек плеера теперь раскрываются до 90% доступной высоты экрана в портретном и альбомном режимах с плавной вертикальной прокруткой.
  - В нижней части каждого всплывающего окна закреплена полноразмерная кнопка подтверждения «Готово», а страница настроек «Оформление» очищена от отладочного блока авто-бенчмарка.

</details>

## [v4.5.8]

### 🇬🇧 English
- **Effortless 120 Hz Scrolling & Lighter Glass Rendering:**
  - Restricted heavy optical lens refraction, background blur passes, and elevated drop shadows strictly to top-level floating bars and outer modal windows, so scrolling through large episode lists and nested dialog cards stays locked at peak refresh rates with minimal battery drain.
  - Replaced full-screen edge shaders in the Flat and Frosted Glass themes with lightweight edge fading, and disabled live video backdrop capture whenever Frameless or Flat player controls are selected or while the performance benchmark is running.
- **Bit-Transparent Audio Fidelity & Expanded Multichannel 3D Spatial Audio:**
  - Fixed automatic low-bitrate voice restoration so tracks with high bitrates or containers without explicit bitrate tags play through completely untouched without artificial filtering.
  - Added full center and surround channel mixing for 3.0, 4.0, and 5.0 audio formats on stereo speakers, and automatically routes multichannel tracks (AAC, FLAC, Opus, AC-3, and E-AC-3 with more than 2 channels) to the high-precision software decoder in Auto mode whenever 3D Spatial Audio is enabled so discrete surround channels are never flattened to stereo by the system codec.
- **Reliable HDR Playlist Transitions, Unified Licenses Search & Benchmark Precision:**
  - Prevented background preloading of the next playlist episode from prematurely resetting HDR mode on the currently playing video, and improved stability when updating HDR10+ dynamic scene metadata and switching episodes after a temporary decoder fallback.
  - Pauses live video decoding during the isolated 7.1 spatial audio benchmark stage for clean memory measurements, and upgraded the Open Source Licenses search field to match the unified theme design system.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.8)</b></summary>

<br>

- **Плавная прокрутка 120 Гц и облегчённый рендеринг стеклянных тем:**
  - Тяжёлое оптическое преломление, размытие фона и глубокие тени теперь применяются только к главным парящим панелям и внешним контурам всплывающих окон — прокрутка длинных списков серий и карточек внутри диалогов работает на максимальной герцовке экрана без лишнего нагрева и расхода заряда.
  - В темах «Плоская» и «Матовое стекло» полноэкранный шейдер краёв списка заменён на лёгкое плавное затухание, а в видеоплеере захват фонового кадра полностью отключается при выборе безрамочного или плоского стиля панели управления, а также во время работы окна бенчмарка.
- **Чистый неокрашенный звук по умолчанию и расширенный многоканальный 3D-звук:**
  - Исправлена автоматическая очистка низкобитрейтного звука: дорожки с высоким качеством или файлы без прописанного в контейнере битрейта теперь передаются без малейшего вмешательства фильтров и окрашивания частот.
  - Добавлено полноценное сведение центрального и тыловых каналов для форматов 3.0, 4.0 и 5.0 при выводе пространственного звука на стереодинамики, а в режиме декодера «Авто» при включённом 3D-звуке любые многоканальные дорожки (AAC, FLAC, Opus, AC-3 и E-AC-3) автоматически декодируются с сохранением всех каналов без принудительного сжатия в обычное стерео.
- **Стабильный HDR в плейлистах, единый поиск в лицензиях и точность бенчмарка:**
  - Фоновая подготовка следующей серии в плейлисте больше не сбрасывает активный режим HDR на текущем видео, повышена надёжность обновления динамических метаданных HDR10+ и переключения между сериями после временного отката декодера.
  - Во время изолированного теста многоканального звука 7.1 в бенчмарке фоновое воспроизведение видео ставится на паузу для абсолютно точного замера памяти, а поле поиска в окне «Открытые лицензии» приведено к единому стилю приложения.

</details>

## [v4.5.7]

### 🇬🇧 English
- **Three-Mode Video & Audio Decoders (Auto / HW+ / SW & Auto / HW / SW) with Automatic Hardware Recovery:**
  - Unified both video and audio decoder selectors into three clear options—Auto (default), Hardware (HW+ / HW), and Software (SW)—across the player menus and global Settings.
  - Strictly separated your saved decoder preference from temporary per-file software fallback: when Auto is selected and a specific file requires software decoding, your Auto selection stays highlighted and untouched, and the player automatically retries hardware acceleration as soon as you open the next video or episode.
  - Preserved full hardware video decoding on high-bitrate Blu-ray .m2ts movies and added a protective memory ceiling so ultra-high-bitrate 4K recordings (250+ Mbps) play smoothly without running out of memory.
- **Restored Video Thumbnails & Automatic In-Player Cover Capture:**
  - Fixed a thumbnail loader conflict where cached preview images were misidentified as raw video streams, restoring folder covers and episode thumbnails across the library and history screens.
  - Added automatic cover capture in the player: if a complex local video or disc file could not generate a preview in the background scanner, the player automatically captures a clean preview frame shortly after playback begins.
- **Categorized Playback History (All, Local, Streaming & P2P):**
  - Added a top category filter bar to the History screen with four views—All, Local, Streaming, and P2P—across all app editions, with source badges in the All view and per-category history clearing.
- **Clean Modal Background Blur, Benchmark State Reset & Landscape Dock Alignment:**
  - Eliminated rectangular gray halos around the floating top header and bottom navigation dock when opening modal sheets and dialogs.
  - Fixed the Player Benchmark so motion smoothing, upscaling shaders, and decoder modes always return to your exact prior settings upon completion or cancellation, and the Play/Pause button immediately updates to the Play icon when paused at the end of a test.
  - Expanded the landscape side navigation dock to full workspace height and optically centered text, icons, and badges across all header capsules and cards.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.7)</b></summary>

<br>

- **Тройка декодеров видео и звука («Авто / HW+ / SW» и «Авто / HW / SW») и защита памяти на тяжёлых 4K-потоках:**
  - Выбор декодера видео и звука приведён к единой тройке режимов — «Авто» (по умолчанию), аппаратный («HW+» для видео и «HW» для звука) и программный («SW») — во всех окнах плеера и в глобальных настройках.
  - Пользовательская настройка полностью отделена от временного переключения на программный декодер: выбранной всегда остаётся кнопка «Авто», а при открытии любого следующего видео плеер снова включает аппаратное ускорение.
  - Сохранено аппаратное декодирование на высокобитрейтных дисках Blu-ray .m2ts и добавлено жёсткое ограничение объёма буфера в памяти, полностью устраняющее вылеты на экстремально тяжёлых 4K-видео с битрейтом 250+ Мбит/с.
- **Восстановление всех превью видео и автозахват обложек в плеере:**
  - Устранён внутренний конфликт декодера картинок, из-за которого готовые кэшированные превью ошибочно обрабатывались как видеофайлы — обложки папок, серий и записей истории снова отображаются мгновенно.
  - Добавлен автоматический захват превью во время просмотра: если для сложного локального файла не удалось извлечь кадр в фоновом сканере, плеер сам сохранит аккуратную обложку сразу после начала воспроизведения.
- **Разделение экрана «История» на категории (Все, Локальные, Стриминг и P2P):**
  - В верхней части вкладки «История» появилась капсула фильтрации из четырёх категорий — «Все», «Локальные», «Стриминг» и «P2P» — с компактным бейджем источника и возможностью очистить как всю историю, так и только выбранную категорию.
- **Чистое размытие фона под окнами, сброс состояния после бенчмарка и геометрия в альбоме:**
  - Убраны серые прямоугольные ореолы вокруг верхней шапки и нижней панели навигации при открытии всплывающих окон и диалогов.
  - После завершения или остановки бенчмарка плеера сглаживание движений, шейдеры чёткости и декодеры гарантированно возвращаются к исходным пользовательским настройкам, а центральная кнопка управления сразу переключается на значок «Play».
  - Левая боковая панель в альбомном режиме вытянута по всей высоте рабочей области, а текст, иконки и бейджи выровнены по единой центральной линии во всех капсулах и карточках.

</details>

## [v4.5.6]

### 🇬🇧 English
- **Restored Video Thumbnails & Accurate Episode Duration Sync:**
  - Fixed missing folder covers and episode preview thumbnails across the media library by isolating the thumbnail storage cache from internal image loader journals and automatically cleaning up corrupted legacy cache files on startup.
  - Improved thumbnail extraction for system document folders, screen recordings, and short clips with sparse keyframes, and automatically updates inaccurate library duration labels with the real video duration as soon as the file is opened in the player.
- **Authentic Frosted & Liquid Glass on Player Floating Windows (Live & Paused):**
  - Floating player windows (Playback Tools, Audio & Subtitles, File Information, and Shader settings) now render genuine Frosted Glass blur and Liquid Glass lens refraction directly over the active video frame—including when playback is paused.
  - Added hardware window blur support on Android 12+ for system dialogs inside the player while keeping zero extra graphics overhead while watching with menus closed.
- **Instant Post-Seek Playback & Honest Per-Episode Settings Toggle:**
  - Eliminated infinite buffering spinners after scrubbing the timeline slider on local videos, screen recordings, and Blu-ray .m2ts files so playback resumes immediately from your chosen timestamp.
  - Fixed the "Remember settings for each episode" toggle in Settings -> Playback so individual audio tracks, subtitle choices, playback speed, video zoom, and sync offsets are saved and restored per file or series folder when enabled, and cleanly use your global defaults when turned off.
- **Centered Player Bottom Bar, Extended Top Visor, Crisp Light Theme & Benchmark Fixes:**
  - Locked the center divider in the player's bottom quick actions bar strictly to the middle of the screen with balanced left and right control groups.
  - Extended the top status-bar scroll visor across all main tabs (including Settings) so scrolled cards never clash with the system clock or battery icons, and upgraded selected chips and rows in the Light theme (when color tint is off or in Monochrome) to high-contrast dark graphite with crisp white text and icons.
  - Eliminated unnecessary background layer recording in the Flat theme during the interface benchmark, and fixed seek responsiveness testing and hardware decoder name reporting in the player benchmark.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.6)</b></summary>

<br>

- **Полное восстановление обложек видео и уточнение реальной длительности:**
  - Восстановлено отображение пропавших обложек папок и превью серий во всей медиатеке: хранилище превью полностью отделено от внутренних журналов загрузчика изображений с автоматической очисткой повреждённого старого кэша при запуске.
  - Улучшено создание превью для системных папок, записей экрана и коротких роликов с редкими ключевыми кадрами, а при открытии видео плеер автоматически заменяет неточную системную длительность карточки в списке на реальную длительность файла.
- **Настоящее «Матовое» и «Жидкое стекло» у всплывающих окон плеера (в движении и на паузе):**
  - Парящие окна плеера («Инструменты воспроизведения», дорожки звука и субтитров, свойства файла и настройки шейдеров) теперь отображают настоящее матовое размытие и оптическую линзу преломления прямо поверх видеокадра — даже когда фильм стоит на паузе.
  - Для системных диалоговых окон плеера на Android 12+ дополнительно включено аппаратное размытие фона при полном отсутствии лишней нагрузки на видеокарту во время обычного просмотра с закрытыми меню.
- **Мгновенное продолжение после перемотки и честное запоминание настроек серий:**
  - Устранена бесконечная буферизация после перетаскивания ползунка времени на локальных видео, записях экрана и дисках Blu-ray .m2ts — воспроизведение продолжается мгновенно с выбранной секунды.
  - Исправлена работа переключателя «Запоминать настройки для каждой серии» в разделе «Настройки -> Воспроизведение»: индивидуальная озвучка, субтитры, скорость, масштаб кадра и задержки синхронизации сохраняются и применяются для файла и папки сериала при включённой опции и строго следуют общим настройкам по умолчанию при её отключении.
- **Центровка нижней панели плеера, верхний козырёк, контрастная светлая тема и точность бенчмарков:**
  - Разделитель в нижней панели быстрых действий плеера жёстко зафиксирован ровно посередине экрана со сбалансированными левой и правой группами кнопок.
  - Защитный козырёк под статус-баром расширен и добавлен на все вкладки главного экрана (включая «Настройки»), чтобы прокручиваемые карточки не сливались с часами и индикаторами, а выбранные кнопки и строки в светлой теме без цветного оттенка и в палитре «Монохром» получили контрастную тёмно-графитовую заливку с идеально читаемым белым текстом и иконками.
  - В бенчмарке интерфейса убрана лишняя нагрузка в плоской теме для честного сравнения со стеклянными стилями, а в бенчмарке плеера исправлены замер скорости перемотки на локальных файлах и сохранение названия активного декодера видео в итоговом отчёте.

</details>

## [v4.5.5]

### 🇬🇧 English
- **Flicker-Free MEMC Motion Smoothing on Heavy & Grainy Cinema:**
  - Eliminated flickering gray block grids and motion-boundary clamping artifacts when watching high-bitrate 4K releases and grainy live-action movies with MEMC motion smoothing enabled.
  - Locked internal motion-smoothing resolution steadily during playback so heavy scenes never trigger mid-scene buffer resets or visual flashes, and unified 24 Hz reference frame rendering when Anime4K is off to prevent subtle sharpness or color strobing.
- **Physical Liquid Glass Lens, Live Modal Blur & Top Scroll Visor:**
  - Upgraded Liquid Glass headers, floating docks, and modal windows to uniform physical lens refraction with natural depth, subtle chromatic dispersion, and beveled specular rims across every surface size while maintaining effortless 120 Hz list scrolling in both glass themes.
  - Restored live background blur behind all floating dialogs on the main screens without darkening the screen, and added a soft top status-bar visor that gently separates scrolled library cards from the system clock and battery icons.
- **Accurate Seeking on Screen Recordings, Long-Keyframe MP4/MKV & Blu-ray .m2ts:**
  - Fixed timeline scrubbing on screen recordings and MP4/MKV videos with sparse keyframes so seeking lands accurately on your chosen timestamp and holds your selected slider position smoothly instead of snapping back to 00:00.
  - Accelerated timeline scrubbing and automated seek-responsiveness testing on high-bitrate Blu-ray .m2ts movies by jumping straight to the nearest preceding keyframe without redundant full-file scanning.
- **Full-Color Video Thumbnails, Cleaner Player Overlays & Open-Source Credits:**
  - Restored vibrant full-color episode and folder cover thumbnails (including Blu-ray .m2ts videos) and isolated interface benchmark test cards so temporary benchmark runs never overwrite real library thumbnails.
  - Automatically hides the live stream statistics overlay while File Information or any other player dialog card is open, eliminated background memory allocations across the timeline progress bar, ambient edge lighting, and fading controls, harmonized margins across the app, and added open-source license attribution for AndroidLiquidGlass in the About and Licenses sections.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.5)</b></summary>

<br>

- **Плавное сглаживание движений MEMC на тяжёлых фильмах без мерцающей сетки:**
  - Полностью устранены мерцающая серая сетка и блочные артефакты на границах движения при просмотре тяжёлых 4K-релизов и художественного кино с плёночным зерном со включённым сглаживанием MEMC.
  - Зафиксировано внутреннее разрешение обработки на время воспроизведения ролика без резких перестроений буферов на сложных сценах, а также выровнен вывод опорных кадров на 24 Гц при выключенном Anime4K без стробирования резкости и оттенков.
- **Физическая линза «Жидкого стекла», живое размытие под окнами и верхний козырёк скролла:**
  - Панели шапки, парящий док и всплывающие окна в теме «Жидкое стекло» переведены на эталонную физическую линзу преломления с естественной глубиной, мягкой хроматической дисперсией и зеркальной гранью независимо от размеров панели при сохранении лёгкой прокрутки списков на 120 Гц.
  - Возвращено живое размытие фона позади всех всплывающих диалогов главного экрана без ухода подложки в темноту, а при прокрутке списков под системными часами и индикаторами батареи появляется мягкий защитный козырёк для чистого чтения значков.
- **Точная перемотка записей экрана, видео с длинными интервалами кадров и Blu-ray .m2ts:**
  - Исправлен сброс перемотки в 00:00 на записях экрана и видеофайлах MP4/MKV с редкими ключевыми кадрами: плеер точно переходит к выбранному моменту и плавно удерживает выбранную позицию ползунка и таймера без скачков назад.
  - Кратно ускорена перемотка по таймлайну и прохождение стресс-теста отзывчивости перемотки на тяжёлых файлах Blu-ray .m2ts за счёт прямого перехода к ближайшему ключевому кадру без лишнего сканирования файла.
- **Полноцветные превью видео, чистые окна плеера и атрибуция лицензий:**
  - Восстановлены чёткие полноцветные обложки папок и превью серий (включая файлы Blu-ray .m2ts), а тестовые карточки бенчмарка интерфейса полностью изолированы от кэша реальной медиатеки.
  - Фоновая карточка статистики потока теперь автоматически скрывается на время открытия окна «Информация о файле» или других диалогов плеера, снижена нагрузка на память и батарею в плеере, выровнены боковые отступы на всех экранах, а в разделы «О приложении» и «Лицензии открытого ПО» добавлена атрибуция библиотеки AndroidLiquidGlass.

</details>

## [v4.5.4]

### 🇬🇧 English
- **Blu-ray .m2ts Timeline, Hardware Decoding & 7.1 Surround Fixes:**
  - Restored full timeline duration display and instant seek scrubbing on all local Blu-ray .m2ts movies, ensuring the automated seek responsiveness benchmark also runs reliably.
  - Fixed video header detection and Open-GOP Blu-ray frame recovery so 1080p H.264 and 4K HEVC movies stay on fast hardware decoding at their native cinematic frame rate without dropping intermediate frames or jumping erratically.
  - Fixed 7.1 Dolby Digital Plus surround sound playback and audio-video synchronization on Blu-ray .m2ts releases so multichannel audio plays smoothly without muting.
- **Reliable Per-File Audio & Subtitle Track Memory:**
  - Selecting a secondary voiceover or subtitle track (even when a movie has multiple tracks in the same language, or on .m2ts files) is now saved immediately and restored automatically on reopen without being overwritten by global language preferences.
- **Negai Premium Gold Styling & Benchmark Convenience:**
  - Restored warm gold styling for the purchase button, star medallion, and Premium badge in the Negai Premium dialog across the Flat, Frosted Glass, and Liquid Glass themes.
  - Completed video player and main interface benchmarks now save numbered diagnostic reports automatically to your device Downloads folder without overwriting previous runs, and automatically pause video playback when the test finishes.
- **Pure Edge Liquid Glass Optics, 120 Hz Scrolling & Spatial Audio Optimization:**
  - Refined the Liquid Glass lens so refraction, gentle chromatic dispersion, and specular highlights occur cleanly along the curved outer rim while keeping the center completely undistorted without shrinking text.
  - Accelerated library and folder scrolling to maintain smooth 120 Hz performance in glass themes with lighter cover thumbnail memory usage, and eliminated background memory allocations during 7.1 surround 3D audio playback.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.4)</b></summary>

<br>

- **Исправление таймлайна, аппаратного декодирования и звука 7.1 в Blu-ray .m2ts:**
  - Полностью восстановлено отображение длительности, шкалы времени и мгновенной перемотки на всех локальных фильмах Blu-ray .m2ts, включая корректную работу автоматического теста скорости перемотки.
  - Исправлена инициализация видеопотока и обработка открытых групп кадров на дисках Blu-ray: фильмы 1080p H.264 и 4K HEVC стабильно воспроизводятся на аппаратном декодере с родной кинематографической частотой кадров без пропусков промежуточных сцен и рывков картинки.
  - Восстановлено воспроизведение восьмиканального звука Dolby Digital Plus 7.1 и точная синхронизация аудио и видео на ремуксах и дисках .m2ts.
- **Надёжное запоминание выбранной озвучки и субтитров:**
  - Выбор второй или любой другой аудиодорожки и субтитров теперь сохраняется мгновенно и точно восстанавливается при повторном открытии файла во всех контейнерах — даже если в файле несколько дорожек одного языка.
- **Золотое оформление окна Negai Premium и улучшения бенчмарков:**
  - Кнопка покупки, верхний звёздный медальон и значок в окне Negai Premium получили выразительное тёплое золотое оформление во всех трёх темах интерфейса («Плоская», «Матовое стекло» и «Жидкое стекло»).
  - Отчёты бенчмарков видеоплеера и основного интерфейса теперь автоматически сохраняются в системную папку «Загрузки» с порядковой нумерацией файлов без перезаписи прошлых замеров, а по завершении теста фильм автоматически ставится на паузу.
- **Чистая краевая линза «Жидкого стекла», плавные 120 Гц и оптимизация 3D-звука:**
  - Обновлена оптика темы «Жидкое стекло»: объёмное преломление, мягкая дисперсия света и блик работают строго по скруглённой кромке элементов, оставляя центральную область идеально чёткой без эффекта уменьшения текста.
  - Ускорена прокрутка списков медиатеки и обложек папок для стабильных 120 Гц в стеклянных темах, а конвейер пространственного 3D-звука 7.1 полностью освобождён от фоновых выделений памяти.

</details>

## [v4.5.3]

### 🇬🇧 English
- **Automated Main Interface & Theme Optics Benchmark (GitHub Edition):**
  - Added a hands-free six-stage interface benchmark accessible from Appearance settings, Diagnostics, and the quick developer header button to measure real high-refresh smoothness across the Flat (Material 3 Expressive), Frosted Glass, and Liquid Glass themes.
  - Automatically tests reversible high-speed list scrolling, tactile navigation dock and control shape transitions, floating modal windows over live moving backgrounds, and intensive cover thumbnail scrolling.
  - Safely previews each theme style in temporary memory without overwriting your personal theme preferences, always restoring your active look and tab even if stopped mid-run.
  - Compares the exact graphics and frame-time cost of Frosted Glass and Liquid Glass against the Flat baseline, recommends the ideal theme style for your screen, and lets you copy or share the full diagnostic report in one tap.
- **Upgraded Video Player Benchmark with Seek & 7.1 Surround Stress Tests:**
  - Added an automated timeline seek responsiveness test that jumps across four points in the video to measure real first-frame recovery and audio resynchronization speed, highlighting slow storage or container indexing bottlenecks while skipping live non-seekable streams automatically.
  - Added an isolated 7.1 surround sound stress test that benchmarks headphone 3D binaural spatialization, speaker 3D virtualization, and voice clarity restoration throughput even when watching a regular stereo video, returning you to your exact playback spot afterward.
  - Expanded playback diagnostics with active graphics pipeline resolution, rendering mode details, and frame-pacing smoothness metrics, alongside internal modularity and stability optimizations.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.3)</b></summary>

<br>

- **Автоматический бенчмарк основного интерфейса и оптики тем (сборка GitHub):**
  - Добавлен шестиэтапный автоматический стресс-тест главного интерфейса (запускается из разделов «Оформление», «Диагностика» или быстрой кнопки в шапке в режиме разработчика) для проверки реальной плавности при высокой герцовке в темах «Плоская» (Material 3 Expressive), «Матовое стекло» и «Жидкое стекло».
  - Без участия рук проверяет скоростную прокрутку насыщенных списков карточек, упругую трансформацию вкладок дока и переключателей, плавное открытие всплывающих окон над живым движущимся фоном и интенсивный скролл сетки видеообложек.
  - Переключает темы строго во временной песочнице в памяти без перезаписи ваших личных настроек оформления и гарантированно возвращает привычный вид и вкладку после завершения или отмены.
  - Наглядно сравнивает нагрузку стеклянных тем относительно базовой плоской темы, рекомендует оптимальный визуальный стиль для вашего устройства и позволяет в одно нажатие скопировать или отправить подробный отчёт.
- **Обновлённый бенчмарк видеоплеера с проверкой перемотки и 3D-звука 7.1:**
  - Добавлен автоматический тест отзывчивости перемотки: плеер выполняет серию переходов по таймлайну, замеряет реальное время до появления первого кадра и восстановления звука, выявляет задержки чтения контейнера и автоматически пропускает потоковые прямые эфиры.
  - Добавлен изолированный стресс-тест многоканального звука 7.1, который проверяет запас мощности процессора для бинаурального 3D-звука в наушниках, объёмной виртуализации динамиков и фильтра чёткости речи даже при открытом обычном стереофайле с возвратом на вашу исходную секунду просмотра.
  - Расширена диагностика вывода изображения с учётом активного разрешения обработки и равномерности смены кадров, а также проведена внутренняя оптимизация стабильности и модульности приложения.

</details>

## [v4.5.2]

### 🇬🇧 English
- **Crash-Free High-Bitrate 4K & Blu-ray .m2ts Playback:**
  - Eliminated out-of-memory crashes on high-bitrate 4K and Blu-ray remux movies by dynamically adapting playback buffer memory to your device capacity and immediately reclaiming unused memory when switching tracks, decoders, or closing a video.
  - Redesigned Blu-ray .m2ts stream reading to reuse memory buffers instead of creating temporary copies on every packet, eliminating background memory spikes and micro-stutters during intense action scenes.
  - Added full initialization support for 1080p Blu-ray H.264 and 4K HEVC video headers so hardware decoding starts cleanly without visual macroblocking or unnecessary software decoder fallbacks.
- **Instant Keyframe Seeking & Embedded Blu-ray PGS Subtitles:**
  - Resuming or seeking inside .m2ts movies now jumps accurately to the nearest preceding keyframe, eliminating multi-second startup delays and gray or garbled transition frames.
  - Added built-in support for Blu-ray PGS graphic subtitles inside .m2ts files, including language selection and subtitle sync adjustments.
- **Smart Per-Episode & Series Folder Playback Memory:**
  - The player now automatically remembers your chosen audio track, subtitle track (including turning subtitles off), audio and subtitle sync offsets, playback speed, and video zoom mode for each individual file.
  - Starting the next episode in the same series folder automatically carries over your preferred voiceover language, subtitle choice, sync timing, speed, and screen zoom from the previous episode.
- **Lighter 7.1 Spatial Audio, Faster Library Loading & Benchmark Restoration:**
  - Optimized the surround audio pipeline so 5.1 and 7.1 tracks are spatialized into stereo before delay and voice cleanup filtering, cutting audio processor load on multichannel movies.
  - Reduced storage overhead when reading video metadata and chapters from local folders, and automatically stops background thumbnail generation as soon as you leave a folder screen.
  - Running the automated performance benchmark now preserves and restores your active audio track, internal or external subtitles, and sync offsets upon completion or cancellation.
  - Improved internal modularity and build stability, with automated cloud builds focused exclusively on the GitHub community edition.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.2)</b></summary>

<br>

- **Стабильное воспроизведение тяжёлых 4K-фильмов и Blu-ray .m2ts без вылетов:**
  - Полностью устранены вылеты из-за нехватки оперативной памяти на фильмах и ремуксах с высоким битрейтом: объём буфера подстраивается под возможности устройства и мгновенно очищается при остановке видео, смене дорожки или переключении декодера.
  - Потоковое чтение контейнеров Blu-ray .m2ts переведено на постоянные буферы без лишних временных копий в памяти, благодаря чему исчезли фоновые подвисания во время динамичных сцен.
  - Исправлена передача параметров видеопотока для 1080p Blu-ray H.264 и 4K HEVC, что обеспечивает стабильный запуск аппаратного декодирования без рассыпания картинки и падения кадровой частоты.
- **Мгновенная перемотка с ключевого кадра и графические субтитры Blu-ray PGS:**
  - Продолжение просмотра не с начала и любая перемотка в файлах .m2ts теперь выполняют точный переход к ближайшему ключевому кадру без долгих задержек старта и серых артефактов на экране.
  - Добавлена поддержка встроенных графических субтитров Blu-ray PGS в файлах .m2ts с выбором языка и настройкой задержки.
- **Умное запоминание дорожек, синхронизации и масштаба для серий и папок:**
  - Плеер автоматически сохраняет выбранную аудиодорожку, субтитры (включая их полное отключение), задержку звука и субтитров, скорость воспроизведения и режим масштабирования кадра для каждого просмотренного файла.
  - При переходе к следующей серии в той же папке сериала плеер автоматически подхватывает выбранную вами озвучку, настройку субтитров, синхронизацию, скорость и масштаб экрана от предыдущего эпизода.
- **Лёгкий 3D-звук 7.1, быстрая медиатека и сохранение настроек после бенчмарка:**
  - Оптимизирована цепочка обработки многоканального звука 5.1 и 7.1: пространственное сведение в стерео теперь выполняется до линии задержки и очистки голоса, что заметно снижает нагрузку на процессор и расход заряда.
  - Ускорено чтение свойств видеофайлов и встроенных глав из памяти устройства, а фоновая генерация превью серий теперь мгновенно останавливается при выходе из папки.
  - После завершения или отмены встроенного авто-бенчмарка плеер полностью возвращает выбранную звуковую дорожку, внутренние или внешние субтитры и настройки синхронизации.
  - Повышена общая стабильность и модульность приложения, а облачная сборка релизов сфокусирована на открытом выпуске для GitHub.

</details>

## [v4.5.1]

### 🇬🇧 English
- **Accurate Interface & Subtitle Scaling and Seamless Player Sheets:**
  - Fixed interface scaling across both the main app and the video player so text and controls scale linearly without oversized double-magnified labels, while keeping subtitle font sizes completely independent of control scaling.
  - All player settings sheets and reference guides now open smoothly inside the fullscreen player viewport without system bar flashes, keeping background transport controls gently dimmed, blocking accidental background video seeks, and preserving your exact scroll position when viewing info cards.
  - Improved landscape sheet width adaptation so floating windows stay crisp and never clip along screen edges at high zoom levels.
- **Refined Frosted Glass, Liquid Glass & Material 3 Expressive Themes:**
  - Restored full optical blur and genuine translucency with subtle micro-grain texture on floating bars, navigation docks, and modal windows in the Frosted Glass theme.
  - Eliminated edge pixel stretching on narrow controls in the Liquid Glass theme and kept live background refraction active behind main library dialogs without dark screen flashes.
  - Enhanced five-step tonal contrast, active navigation indicators, and tactile spring morphing across Light, Dark, and OLED modes in the Flat (Material 3 Expressive) theme, and added smooth scroll-edge optics to the Recent and Torrents tabs.
- **Native 5.1 / 7.1 Surround Passthrough & Single-Stage Speaker 3D Audio:**
  - Turning off 3D Spatial Audio on 5.1 and 7.1 surround tracks now passes multi-channel audio directly to external receivers, soundbars, and TVs without forced stereo downmixing.
  - Upgraded speaker 3D virtualization to a cleaner single-stage spatial processor with high-resolution 24-bit, 32-bit, and floating-point audio support, eliminating double-filtering artifacts and instantly adapting when headphones are connected or disconnected.
- **Adaptive Display Efficiency, Ambient Glow Smoothness, HDR & 3D Video Fixes:**
  - Pausing a video or closing the player now immediately releases display refresh rate locks so LTPO screens can save battery, while touching player controls or opening menus instantly restores peak 120 / 144 Hz responsiveness.
  - Eliminated background frame-rate overhead during Ambient Video Glow playback and restored accurate colors when Anime4K or motion smoothing is active.
  - Preserved native hardware HDR output on HDR10, HLG, and Dolby Vision videos, improved Dolby Vision Profile 7 playback stability, and fixed horizontal or vertical squishing on Half Side-by-Side and Half Top-and-Bottom 3D movies.
- **Google Play Purchase Reliability & Upgraded Performance Benchmark:**
  - Hardened Google Play license verification and offline reconnection handling so purchase status stays accurately synchronized across devices.
  - Upgraded the GitHub Lab performance benchmark to measure both Anime4K and motion smoothing shader load, automatically skip stages that do not apply to stereo audio or hardware HDR streams, and report live display refresh rate, stream buffering headroom, and multi-channel audio routing.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.5.1)</b></summary>

<br>

- **Точное масштабирование интерфейса и субтитров и бесшовные окна плеера:**
  - Исправлено масштабирование в главном меню и видеоплеере: текст и кнопки теперь увеличиваются строго пропорционально без двойного раздувания шрифтов, а выбранный размер субтитров остаётся неизменным при любом масштабе панели управления.
  - Все окна настроек и справочные карточки в плеере теперь плавно открываются внутри полноэкранной сцены без моргания системных полос, мягко приглушают панель управления на фоне, защищают от случайных нажатий сквозь окно и сохраняют позицию прокрутки при открытии подсказок.
  - Исправлена адаптация ширины всплывающих окон в горизонтальной ориентации при увеличенном масштабе интерфейса без обрезки краёв.
- **Обновлённая оптика тем «Матовое стекло», «Жидкое стекло» и «Плоская» (Material 3 Expressive):**
  - В теме «Матовое стекло» возвращены полноценная полупрозрачность, глубокое размытие фона и деликатная матовая микротекстура на плавающих доках, шапках и модальных окнах.
  - В теме «Жидкое стекло» устранены краевые искажения на узких элементах и сохранено живое преломление фона позади диалоговых окон библиотеки и папок без ухода экрана в черноту.
  - В теме «Плоская» (Material 3 Expressive) усилено пятиступенчатое разделение оттенков карточек, вложенных кнопок и активной вкладки навигации в светлом, тёмном и OLED режимах, улучшен отклик нажатия кнопок и добавлено плавное краевое сглаживание списков на вкладках «Недавние» и «Торренты».
- **Прямой вывод звука 5.1 / 7.1 и чистый пространственный 3D-звук на динамиках:**
  - При отключении пространственного 3D-звука многоканальные дорожки 5.1 и 7.1 теперь передаются напрямую без принудительного сведения в стерео, сохраняя полноценное объёмное звучание на ресиверах, саундбарах и телевизорах.
  - Режим объёмного звучания для стереодинамиков переведён на чистую одноступенчатую обработку с поддержкой аудио высокой чёткости (24 бит, 32 бит и с плавающей точкой) без двойной фильтрации и с мгновенным переключением при подключении или отключении наушников.
- **Энергоэффективный LTPO-экран, лёгкая фоновая подсветка, HDR и стереопары 3D:**
  - На паузе и при выходе из плеера экран теперь сразу переходит в адаптивный энергосберегающий режим, а при появлении панели управления или открытии меню мгновенно возвращается на плавные 120 / 144 Гц.
  - Устранена лишняя фоновая нагрузка от подсветки краёв кадра и восстановлено точное считывание оттенков при включённом улучшении линий Anime4K или сглаживании движений.
  - Сохранён прямой аппаратный вывод цвета для HDR10, HLG и Dolby Vision, повышена стабильность воспроизведения фильмов в Dolby Vision Profile 7 и исправлены пропорции кадра на анаморфных 3D-фильмах с горизонтальной и вертикальной стереопарой.
- **Надёжная синхронизация покупок Google Play и обновлённый авто-бенчмарк:**
  - Улучшена проверка статуса покупки и переподключение к Google Play при нестабильной сети.
  - Авто-бенчмарк в GitHub Lab теперь замеряет нагрузку графических шейдеров как для Anime4K, так и для сглаживания движений, автоматически пропускает неприменимые этапы на стереодорожках и прямом выводе HDR, а также фиксирует реальную частоту экрана, запас буфера сети и конфигурацию аудиоканалов.

</details>

## [v4.4.9]

### 🇬🇧 English
- **Convex Collecting Liquid Glass Optics Without Inward Shrinking:**
  - Redesigned the optical refraction shader across floating bars, modal sheets, and cards into a unified dome-convex collecting lens with balanced edge magnification and lateral light flare.
  - Eliminated inward image shrinking and black border artifacts around the floating bottom dock, ensuring underlying album art and titles bend smoothly outward around the rounded glass rims without haze.
  - Increased floating bar transparency depth while keeping text crystal-clear and removing opaque canvas backings beneath floating windows and panels.
- **Pure Material 3 Expressive Geometry in Flat Theme & Button Styling:**
  - Preserved continuous superellipse (G2) rounded corner curvature in the Flat theme when switching styles, preventing rounded cards from collapsing into rigid circular corners.
  - Updated theme descriptions and button rendering to respect pure borderless Material 3 Expressive tonal container hierarchies.
- **Reliable Brightness Gestures & Picture-in-Picture System Brightness Restoration:**
  - Resolved an issue where localized language wrappers could prevent vertical brightness swipe gestures from interacting with the display window.
  - Entering Picture-in-Picture (PiP) mode now automatically restores default system brightness, ensuring mini-window video playback never overrides your device screen brightness.
  - Keeps display awake continuously during automated performance benchmark execution even while switching video decoders.
- **Refined Benchmark Refresh Rate Accuracy & Movie Motion Interpolation:**
  - Synchronizes display frame metrics sampling right after modal window dismissal to prevent open dialogs from artificially depressing benchmark frame rates.
  - Prevents accidental background audio playback upon exiting the player during a benchmark run.
  - Corrected motion interpolation resolution scaling so high-bitrate 1080p Blu-ray movies are never unnecessarily downscaled during frame smoothing.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.9)</b></summary>

<br>

- **Настоящая собирающая купольно-выпуклая линза в теме «Жидкое стекло» (Liquid Glass):**
  - Оптический шейдер преломления на плавающих панелях, шторках и карточках полностью переработан в единую собирающую выпуклую линзу с мягким купольным увеличением и краевым преломлением.
  - Устранено затягивание изображения внутрь панели и появление тёмных полей у краёв плавающего дока: обложки и заголовки под стеклом теперь плавно увеличиваются и красиво обтекают скругления граней без размытия в туман.
  - Сбалансирована прозрачность плавающих панелей и убраны непрозрачные подложки под плавающими элементами и модальными окнами.
- **Каноническая геометрия Material 3 Expressive в теме «Плоская» и исправление кнопок:**
  - Сохранена плавная суперэллиптическая (G2) геометрия скругления углов в теме «Плоская», предотвращая переход форм в грубые циркулярные скругления при смене оформления.
  - Обновлены описания тем и рендеринг кнопок в соответствии с чистым стандартом безрамочной тональной иерархии контейнеров Material 3 Expressive.
- **Стабильные жесты яркости и сброс подсветки в режиме «Картинка в картинке»:**
  - Исправлена ошибка, из-за которой контекст смены языка мог препятствовать работе вертикального свайпа яркости.
  - При переходе в режим «Картинка в картинке» (PiP) локальная яркость плеера теперь автоматически сбрасывается к системной яркости экрана смартфона, предотвращая залипание максимальной яркости в мини-окне.
  - Экран больше не гаснет во время автоматического бенчмарка при смене декодеров между этапами.
- **Повышенная точность авто-бенчмарка и плавная интерполяция кадров в кино:**
  - Замер кадровой частоты в авто-бенчмарке теперь стартует строго после закрытия окна настроек и стабилизации экрана, исключая ложное занижение герцовки из-за открытого диалога.
  - Исключён случайный фоновый запуск звука при закрытии плеера во время бенчмарка.
  - Исправлено определение разрешения для сглаживания движений: высокобитрейтные 1080p Blu-ray фильмы больше не урезаются по разрешению при интерполяции кадров.

</details>

## [v4.4.8]

### 🇬🇧 English
- **Floating Windows Scrolling, Benchmark Layout & 120 Hz Dialog Responsiveness:**
  - Restored smooth vertical scrolling across all floating windows and settings sheets throughout the player and main app.
  - Updated the Performance Benchmark window so the primary start button stays pinned in the bottom bar and remains immediately accessible on the first screen at any interface scale or orientation, while duration chips wrap cleanly without squishing.
  - Floating windows opened over the video player now preserve peak 120 / 144 Hz display responsiveness on Realme UI, ColorOS, and other custom Android skins instead of dropping to 30 Hz.
- **Proportional Player Text & Control Scaling and Two-Row Portrait Top Header:**
  - Fixed interface scaling in the video player so text labels, badges, and buttons scale together proportionally when adjusting Player UI Scale or Auto DPI Adaptation, including inside floating windows opened over the video.
  - Added a clean two-row top header layout in portrait mode and on narrow screens: the first row keeps the back button, scrolling title, lock, and picture-in-picture controls comfortable to read, while the second row displays resolution, HDR, playlist, benchmark, ambient light, and orientation badges without cramping.
- **Accurate Colors on Standard Videos After Watching HDR:**
  - Fixed an issue where opening a standard video after watching an HDR10, HLG, or Dolby Vision video could leave HDR tone mapping active and make standard colors look washed out or overly compressed.
- **Smooth High-Bitrate Blu-ray (.m2ts) Playback, Sharper Ambient Lighting & LTPO Efficiency:**
  - Eliminated gradual frame drops during extended playback of heavy Blu-ray files with lossless 8-channel DTS-HD Master Audio tracks by expanding media buffer capacity and preventing false low-bitrate audio filtering on lossless surround streams.
  - Upgraded the 4-zone Ambient Video Glow sampler to detect and ignore black letterbox and pillarbox bars, capturing livelier, more accurate colors directly from the active edges of the video frame.
  - Improved adaptive refresh rate coordination with the Android system compositor during standard playback to save battery on LTPO displays.
- **Refined Landscape Library Header & Search Bar Typography:**
  - In landscape mode, the library search bar, last played file card, and quick action pills now scroll smoothly along with the folder list, freeing up vertical space so multiple rows of folders are visible at once.
  - Fixed vertical text clipping on letters with descenders in search fields and harmonized the corner rounding of nested buttons inside compact bars.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.8)</b></summary>

<br>

- **Плавная прокрутка всплывающих окон, удобный бенчмарк и честные 120 Гц в диалогах:**
  - Восстановлена плавная вертикальная прокрутка контента во всех всплывающих окнах и шторках настроек в плеере и главном меню.
  - В окне «Бенчмарк производительности» главная кнопка запуска вынесена в нижнюю закреплённую панель и теперь всегда видна на первом экране при любом масштабе интерфейса и повороте экрана, а кнопки выбора длительности прогона аккуратно переносятся без сжатия текста.
  - Открытие всплывающих окон поверх видео на прошивках Realme UI, ColorOS и других оболочках больше не сбрасывает частоту экрана до 30 Гц, сохраняя плавные 120 / 144 Гц.
- **Честное масштабирование текста и кнопок в плеере и двухрядная портретная шапка:**
  - Исправлено масштабирование в видеоплеере: при изменении ползунка масштаба интерфейса плеера и авто-адаптации под экран теперь пропорционально меняются не только отступы и плашки, но и весь текст как на панели управления, так и внутри открываемых поверх видео окон.
  - В портретной ориентации и на узких экранах верхняя панель плеера теперь автоматически выстраивается в два удобных ряда: в верхнем ряду расположены кнопка возврата, бегущая строка названия, блокировка и «Картинка в картинке», а во втором — значки разрешения, HDR, серий, бенчмарка, фоновой подсветки и поворота экрана.
- **Естественные цвета на обычных видео после просмотра HDR:**
  - Исправлена ошибка, из-за которой при включении обычного видео сразу после просмотра ролика в HDR10, HLG или Dolby Vision в декодере мог сохраняться режим HDR, делавший картинку блеклой или серой.
- **Плавное воспроизведение тяжёлых Blu-ray (.m2ts), точная фоновая подсветка и энергоэффективный LTPO:**
  - Устранено постепенное накопление пропусков кадров при длительном просмотре тяжёлых Blu-ray файлов с многоканальными дорожками DTS-HD Master Audio 7.1 за счёт увеличенного буфера чтения и отключения ненужной фильтрации сжатого звука на аудиодорожках без потерь.
  - Повышена точность 4-зонной фоновой подсветки вокруг видео: анализатор кадра теперь автоматически отсекает чёрные полосы по краям широкоформатных роликов и считывает живые оттенки строго с границ самого изображения.
  - Улучшена передача точной частоты кадров системному экрану при обычном воспроизведении для экономии заряда батареи на LTPO-дисплеях.
- **Удобная прокрутка шапки медиатеки в альбомном режиме и чистый текст в поиске:**
  - В горизонтальной ориентации строка поиска, карточка последнего просмотренного файла и панель быстрых действий теперь плавно прокручиваются вверх вместе со списком папок, освобождая экран для одновременного обзора нескольких рядов карточек.
  - Исправлено подрезание нижних хвостиков букв («р», «у», «й») в полях поиска и выровнены скругления вложенных кнопок внутри компактных панелей.

</details>

## [v4.4.7]

### 🇬🇧 English
- **Lateral Convex Edge Refraction in the Liquid Glass Theme:**
  - Redesigned the optical lens behavior on the floating bottom dock, top header, floating windows, and cards so background content no longer shrinks inward or recedes into the distance.
  - Added a convex side-edge meniscus effect that gently magnifies and bends cards and titles outward around the left and right edges and rounded corners as they scroll beneath the glass, while keeping text on the panels crisp and free of color fringing.
- **Crisp Borderless Tonal Hierarchy in the Flat (Material 3 Expressive) Theme:**
  - Refined the tonal step separation in the borderless Flat theme across Light, Dark, and OLED modes—especially in the Monochrome palette and when surface color tinting is turned off.
  - **Light Mode Clarity:** Placed pure-white cards over a calm soft-gray screen background, gave nested buttons and chips a distinct tonal step inside cards, and styled the floating navigation dock and top bar with a deeper container tone so they never blend into white cards scrolling underneath.
  - **High-Contrast Selection & Radio Indicators:** Selected rows, filter chips, and active navigation tabs now stand out immediately with a rich dark-graphite highlight and crisp white text and icons in Light Monochrome mode (or a balanced indigo-slate highlight when color tinting is disabled), paired with clearer unselected radio button indicators.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.7)</b></summary>

<br>

- **Объёмное боковое преломление краёв в теме «Жидкое стекло» (Liquid Glass):**
  - Переработана оптика линзы на плавающем нижнем доке, верхней шапке, всплывающих окнах и карточках: фон под стеклом больше не сжимается к центру и не «проваливается вдаль».
  - Добавлено выпуклое боковое преломление у левой и правой граней и на закруглениях углов — проезжающие под доком и шапкой карточки и заголовки теперь плавно увеличиваются и «обтекают» края стекла в стороны, а текст на самих панелях остаётся кристально чётким без радужных ореолов.
- **Чёткая безрамочная тональная иерархия в теме «Плоская» (Flat Material 3 Expressive):**
  - Выстроено контрастное разделение слоёв по яркости строго без рамок и контуров в светлом, тёмном и OLED режимах (особенно в палитре «Монохром» и при отключённой цветовой тонировке поверхностей).
  - **Чистота в светлом режиме:** Чисто-белые карточки теперь контрастно лежат на спокойном светло-сером холсте экрана, вложенные кнопки и переключатели внутри карточек имеют собственный заметный тон, а плавающий нижний док и верхняя шапка выделены более плотным тоном и никогда не сливаются с проезжающими под ними белыми карточками.
  - **Контрастный выбор и радио-индикаторы:** Выбранные строки, чипы и активная вкладка нижнего дока в светлом монохромном режиме теперь выделяются глубокой графитовой заливкой с чисто-белым текстом и иконками (или насыщенным пепельно-индиговым тоном при выключенной тонировке), а невыбранные кружки радио-кнопок стали заметнее на фоне строк.

</details>

## [v4.4.6]

### 🇬🇧 English
- **Smooth Satin Glass Balance in the Liquid Glass Theme:**
  - Softened the background blur inside floating bars, cards, and modal windows so titles and icons remain effortlessly readable over busy backgrounds while preserving edge refraction.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.6)</b></summary>

<br>

- **Мягкое сатиновое сглаживание в теме «Жидкое стекло» (Liquid Glass):**
  - Сбалансировано внутреннее сглаживание фона на плавающих панелях, карточках и всплывающих окнах для комфортного чтения текста поверх пёстрых обложек и списков с сохранением краевого преломления.

</details>

## [v4.4.5]

### 🇬🇧 English
- **True Convex Water-Drop Lens Optics in the Liquid Glass Theme:**
  - Calibrated the optical refraction profile across floating bars, windows, and cards so text and icons passing beneath the glass bend smoothly along the curved meniscus rim without blurring into a haze.
  - Increased the clarity of the central glass area in both Dark and Light modes and added a subtle inner specular rim reflection that highlights the physical thickness of each glass edge.
- **Peak 120 / 144 Hz Scrolling on Custom OEM Skins & Theme Mode Icons:**
  - Explicitly activates your display's highest refresh rate at native screen resolution across menus, lists, and settings so devices running Realme UI, ColorOS, HyperOS, and OneUI no longer throttle the interface to 60 Hz.
  - Replaced plain colored dots on the base theme mode selector (Light, Dark, OLED, Auto, and Auto OLED) in the quick theme sheet and Settings with clear, recognizable icons.
- **Reliable Online HLS Streaming & Automatic Decoder Recovery:**
  - Fixed playback errors on online HLS streams whose links contain embedded file extensions before the manifest suffix.
  - Improved hardware decoder stability across diverse Android chipsets and added automatic fallback to software decoding if a hardware video or audio codec encounters an unexpected runtime error.
- **Cleaner Player Modal Windows & Unified Control Rounding:**
  - Opening any settings or track window inside the video player now hides the underlying player buttons and applies live hardware blur directly to the video frame behind the window, eliminating distracting double-blurred button silhouettes.
  - Expanded the visible list height inside the player tools window and unified the corner rounding of all player transport buttons, quick action chips, and floating status pills.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.5)</b></summary>

<br>

- **Эталонный эффект выпуклой водяной линзы в теме «Жидкое стекло» (Liquid Glass):**
  - Откалибровано оптическое преломление на плавающих панелях, всплывающих окнах и карточках: контуры текста и иконок под стеклом теперь оставляют чёткий геометрический изгиб у самой кромки линзы без размытия в туман.
  - Повышена прозрачность центральной части стекла в тёмной и светлой темах и добавлен тонкий внутренний зеркальный блик вдоль контура, подчёркивающий физическую толщину стеклянного бортика.
- **Честные 120 / 144 Гц в интерфейсе на прошивках Realme UI, ColorOS, HyperOS и OneUI и иконки режима темы:**
  - Приложение теперь явно запрашивает максимальную частоту обновления экрана в родном разрешении при навигации по спискам и настройкам, устраняя принудительное ограничение в 60 Гц для видеоплееров на сторонних оболочках Android.
  - В окне быстрого выбора темы и в настройках оформления кружки предпросмотра на кнопках режима фона (Светлая, Тёмная, OLED, Авто и Авто OLED) заменены на понятные тематические иконки.
- **Надёжный запуск онлайн-потоков HLS и авто-переключение декодера при сбоях:**
  - Исправлена ошибка запуска потокового онлайн-видео по сложным ссылкам балансировщиков, содержащим расширение видеофайла внутри адреса манифеста.
  - Повышена стабильность аппаратных декодеров на разных процессорах и добавлено автоматическое переключение на программный декодер видео или звука при непредвиденных сбоях системного кодека.
- **Чистые всплывающие окна в видеоплеере и единая геометрия кнопок:**
  - При открытии любого окна настроек в плеере кнопки управления теперь полностью скрываются, а живое аппаратное размытие накладывается напрямую на кадр видео позади окна без размытых силуэтов интерфейса.
  - Увеличена полезная высота списков в окне инструментов плеера и приведены к единому скруглению все кнопки управления воспроизведением, чипы быстрых действий и плавающие индикаторы жестов.

</details>

## [v4.4.4]

### 🇬🇧 English
- **Crisper, Deeper Edge Refraction in the Liquid Glass Theme:**
  - Refined the optical lens profile across floating bars, windows, and cards so light bends sharply along a focused outer rim while keeping the center of each surface crystal-clear.
  - Reduced background pre-blur and lightened the glass surface tint on floating bars and cards so background colors and wallpaper details refract vividly through the edges.
  - Enabled live edge refraction on main content cards over the background canvas and strengthened the top-edge specular highlight for a tactile thick-glass feel.
- **Smoother 120 Hz Scrolling & Glass Rendering Optimizations:**
  - Eliminated redundant layer clipping on shadowed glass panels and floating windows.
  - Pre-cached glass bevel outlines and surface gradients outside the per-frame drawing loop to prevent memory allocations and garbage-collection pauses during fast 120 Hz scrolling.
  - Streamlined the scroll-edge optical shader in the Frosted Glass theme to cut GPU texture sampling overhead nearly in half while maintaining a soft edge fade.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.4)</b></summary>

<br>

- **Глубокое и отчётливое краевое преломление в теме «Жидкое стекло» (Liquid Glass):**
  - Обновлён оптический профиль линзы на плавающих панелях, всплывающих окнах и карточках: преломление теперь сфокусировано вдоль аккуратной внешней кромки с более сильным изгибом лучей и чистым прозрачным центром.
  - Снижено предварительное размытие фона и уменьшена плотность тонирующей подложки на плавающих панелях и карточках, чтобы детали и цвета фона отчётливо преломлялись на стеклянных гранях.
  - Включено живое краевое преломление на основных карточках поверх фонового полотна и усилен тонкий блик верхней грани для ощущения физического объёмного стекла.
- **Плавная прокрутка 120 Гц и оптимизация рендеринга стекла:**
  - Устранено двойное обрезание контуров на стеклянных панелях и модальных окнах с тенями.
  - Контуры фасок и градиентные блики стеклянных поверхностей теперь кэшируются заранее и не пересоздаются на каждом кадре, что полностью исключает лишнюю нагрузку на память и микрофризы при быстрой прокрутке 120 Гц.
  - Оптимизирован шейдер мягкого затухания краёв списков в теме «Матовое стекло» (Frosted Glass): нагрузка на видеоядро при скролле снижена почти вдвое без потери плавности края.

</details>

## [v4.4.3]

### 🇬🇧 English
- **GitHub Lab Experimental Suite & In-Player Performance Benchmark:**
  - Added an **Experimental Features** section in **Settings → Miscellaneous** for GitHub releases with a toggle to unlock the built-in performance laboratory.
  - **Automated Real-Media Benchmark:** Launch `[⚡ Benchmark]` directly from the video player top bar, the live stream monitor, or the player tools menu while watching any video to automatically test your device across a complete matrix of playback modes:
    - Hardware vs. software video decoding.
    - Real-time Anime4K neural upscaling (Fast, Balanced, and Ultra presets).
    - Motion smoothing (MEMC) up to your screen's maximum refresh rate, plus combined Anime4K + MEMC stress testing.
    - Edge-outward Ambient Video Glow sampling.
    - Audio decoding, voice cleanup filters, and 3D Spatial Audio (binaural headphones and stereo speaker modes).
  - **Smart Diagnostics & Instant Restoration:** Each test stage seeks back to the exact same video scene for a fair comparison, automatically restores your original player settings when finished or cancelled, and highlights whether your device handles every mode smoothly or where hardware limits occur.
  - **One-Tap Report Export:** Copy the complete diagnostic report to your clipboard or share it as a file directly from the player or from Settings.
- **Cleaner, Reader-Friendly Release Notes:**
  - Redesigned the release history and update notes in the app and on GitHub to focus on clear, practical user improvements without internal developer jargon.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.3)</b></summary>

<br>

- **Лаборатория GitHub Lab и встроенный авто-бенчмарк производительности:**
  - В раздел **Настройки → Прочее** для сборок GitHub добавлена карточка **«Экспериментальные функции»** с переключателем встроенного профилировщика и авто-бенчмарка.
  - **Автоматическое тестирование на реальном видео:** Кнопка `[⚡ Бенчмарк]` в верхней панели видеоплеера, в Мониторе потока и в меню «Инструменты» запускает пошаговую проверку устройства прямо на текущем открытом видеофайле по всем ключевым режимам:
    - Аппаратное и программное декодирование видео.
    - Нейросетевое улучшение чёткости Anime4K (быстрый, сбалансированный и ультра-режимы).
    - Сглаживание движений (MEMC) до максимальной герцовки экрана, а также совместный стресс-тест Anime4K + MEMC.
    - Краевая фоновая подсветка кадра (Ambient Video Glow).
    - Декодирование звука, очистка голосовых дорожек и пространственный 3D-звук для наушников и стереодинамиков.
  - **Точное сравнение и возврат настроек:** Каждый этап тестирования прогоняется на одном и том же фрагменте видео для честного сравнения, по завершении или отмене теста все исходные настройки плеера автоматически возвращаются на место, а в итоговой сводке наглядно показывается запас производительности устройства и возможные узкие места.
  - **Быстрый экспорт отчёта:** Итоговый отчёт автоматически сохраняется в памяти приложения — его можно скопировать в буфер обмена или отправить файлом прямо из окна результатов или из настроек.
- **Понятный пользовательский формат списков изменений:**
  - Обновления в истории версий внутри приложения и на GitHub теперь описываются простым и понятным языком без внутренних технических имён кода.

</details>

## [v4.4.2]

### 🇬🇧 English
- **Harmonized Concentric Corner Rounding Across the Entire Interface:**
  - Unified the shape of all floating windows, top bars, navigation docks, cards, buttons, and status badges so nested elements align naturally with their outer containers.
  - Full pill shapes are now reserved exclusively for sliding controls with circular thumbs (switches, radio buttons, and the playback timeline).
- **Refined 'Monochrome' Palette Optics:**
  - Selected cards and list rows in the dark Monochrome theme now use a comfortable dark-graphite highlight with crisp white text and a subtle silver rim instead of harsh solid-white fills.
  - High-contrast black-and-white inversion is kept strictly on compact switches, radio dots, and timeline sliders for instant visual clarity.
  - Preserved the gold star accent on Pro badges and matched theme mode preview outlines to your active accent color.
- **Adaptive Landscape Library & Left Navigation Rail:**
  - **Compact Two-Row Landscape Header:** Placed the search bar and a single-line "Continue Watching" card side by side on the top row, and merged folder counts and sorting controls into the quick action bar on the second row—freeing up significantly more vertical space for your media folders.
  - **Left Navigation Rail in Landscape:** Moved the main navigation dock to a sleek vertical rail along the left edge of the screen when holding your device horizontally.
- **Cleaner Top Bar, Neutral Action Buttons & Full English Localization:**
  - Simplified the main library header next to the app logo to show only the interactive version badge.
  - Styled all quick action bar buttons uniformly without permanent accent highlights on the storage access or file picker buttons.
  - Completed English translations for all empty-folder prompts and storage status cards on the home screen.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.2)</b></summary>

<br>

- **Гармоничная система скруглений во всём интерфейсе:**
  - Все всплывающие окна, верхние панели, карточки, кнопки, переключатели и бейджи приведены к единой концентрической геометрии, где форма внутренних элементов плавно повторяет контур внешних панелей.
  - Полностью скруглённая форма капсулы оставлена только для переключателей, радиокнопок и полосы перемотки плеера.
- **Обновлённая оптика палитры «Монохром»:**
  - В тёмной теме палитры «Монохром» выбранные карточки и строки списков больше не заливаются слепящим белым цветом — теперь они сохраняют благородный тёмно-графитовый оттенок с чётким белым текстом и серебристой гранью.
  - Контрастная чёрно-белая заливка применяется только к компактным индикаторам: тумблерам, точкам выбора и ползунку таймлайна.
  - Сохранено золотое свечение значка Pro на выбранных строках и привязана обводка карточек выбора режима темы к цвету активной палитры.
- **Адаптивная альбомная библиотека и боковая панель навигации:**
  - **Компактная двухрядная шапка в ландшафте:** Строка поиска и карточка «Продолжить просмотр» теперь располагаются в один ряд бок о бок, а счётчик папок и кнопка сортировки объединены со строкой быстрых действий, открывая больше места под карточки видео.
  - **Левая боковая панель навигации:** В горизонтальном положении экрана нижнее меню перестраивается в компактную вертикальную панель слева.
- **Чистая верхняя панель, нейтральные кнопки и перевод:**
  - Из главной шапки рядом с логотипом убраны лишние технические индикаторы — оставлен только значок текущей версии.
  - Кнопки панели быстрых действий приведены к единому спокойному стилю без постоянной яркой подсветки на кнопках доступа и выбора файла.
  - Завершён перевод на английский язык для всех подсказок выбора папок и карточек состояния памяти на главном экране.

</details>

## [v4.4.1]

### 🇬🇧 English
- **Active App Color Palette in the Video Player:**
  - Fixed an issue where the video player controls and settings sheets defaulted to a standard purple accent.
  - The video player now automatically uses your chosen app color theme (including Material You wallpaper colors and all built-in presets) for active buttons, sliders, switches, and badges while keeping player backgrounds comfortably dark.
- **New Black-and-White 'Monochrome' Palette:**
  - Added a pure black, white, and silver-graphite **Monochrome** color scheme with zero color tinting and crisp contrast between active and inactive controls in both dark and light modes.
- **True Neutral Surface & Glass Tinting Toggle:**
  - Turning off surface tinting in Settings now completely removes color casts from unselected cards, floating windows, buttons, and folder icons across all three interface styles (Flat, Frosted Glass, and Liquid Glass), applying accent color strictly to active selections.
- **Natural Screen Brightness on HDR Videos:**
  - Starting an HDR video no longer forces your screen brightness to 100%. The player now respects your current system brightness while still delivering rich hardware HDR highlights, changing brightness only when you swipe vertically on the screen.
- **Smooth Adaptive 120 Hz Scrolling & Internal Optimization:**
  - Restored full high-refresh-rate smoothness across library lists and menus immediately upon pausing or exiting a video, matching screen refresh rate to video frame rate strictly during active playback.
  - Streamlined internal app architecture and audio-video processing modules for faster startup and smoother UI responsiveness.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.1)</b></summary>

<br>

- **Единая цветовая палитра приложения в видеоплеере:**
  - Исправлена ошибка, из-за которой элементы управления и окна настроек внутри видеоплеера отображались со стандартным фиолетовым оттенком.
  - Теперь видеоплеер наследует выбранную вами цветовую палитру приложения (включая цвета обоев Material You и все встроенные темы): фон панелей остаётся тёмным, а активные кнопки, ползунки, переключатели и бейджи окрашиваются в ваш акцентный цвет.
- **Новая чёрно-белая палитра «Монохром»:**
  - В выбор цветовых схем добавлена строгая ахроматическая палитра **«Монохром»** в чистых графитовых, серебристых и белых тонах без цветных примесей.
- **Честная работа переключателя тонирования поверхностей:**
  - При отключении тонирования в настройках цветной оттенок теперь полностью убирается с невыбранных карточек, всплывающих окон, кнопок и иконок папок во всех трёх стилях оформления («Flat», «Матовое стекло» и «Жидкое стекло»), оставляя цвет только на активных выбранных элементах.
- **Естественная яркость экрана при просмотре HDR:**
  - При запуске HDR-видео плеер больше не выкручивает яркость экрана на максимум автоматически: сохраняется ваша комфортная системная яркость с поддержкой аппаратного расширенного динамического диапазона, а изменить яркость всегда можно привычным вертикальным свайпом.
- **Плавные 120 Гц в интерфейсе и внутренняя оптимизация:**
  - Устранено ограничение герцовки экрана в меню: при выходе из плеера или постановке видео на паузу интерфейс мгновенно возвращается к плавной прокрутке 90/120 Гц.
  - Проведена глубокая внутренняя оптимизация модулей приложения для более быстрого отклика интерфейса и экономии ресурсов устройства.

</details>

## [v4.4.0]

### 🇬🇧 English
- **Reliable First-Try Startup for Online Streams & Live Broadcasts:**
  - Improved connection resilience when opening online HLS streams and live broadcasts, automatically retrying and recovering when streaming servers take extra time to prepare playlists instead of stopping with an error.
- **Smooth Video Start Without Initial Frame Flicker:**
  - Fixed a brief visual flash that could appear for a split second before the first video frame was drawn on screen.
- **True Translucent Glass for Floating Windows:**
  - Removed the dark backing layer behind floating dialogs and settings sheets in the **Frosted Glass** and **Liquid Glass** themes so live blurred video and background artwork shine through cleanly.
- **Lightweight Edge-Outward Ambient Video Glow:**
  - Redesigned the ambient video backlight so smooth color gradients radiate directly outward from the edges of the video frame into the top and side black bars.
  - Ambient lighting now automatically pauses with zero processor or battery usage whenever a video fills the entire screen, and adapts its update speed on heavy 4K and 60 fps videos to keep playback silky smooth.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.4.0)</b></summary>

<br>

- **Надёжный запуск онлайн-трансляций и сетевых потоков с первого раза:**
  - Повышена стабильность подключения к сетевым потокам и прямым трансляциям: плеер теперь автоматически дожидается готовности потока при задержках сервера и переподключается без сброса воспроизведения.
- **Плавное открытие видео без промаргивания первого кадра:**
  - Устранено краткое промаргивание экрана в момент открытия видеофайла и переключения видеоповерхности.
- **Настоящая прозрачность всплывающих окон в стеклянных темах:**
  - В темах **«Матовое стекло»** и **«Жидкое стекло»** убрана плотная непрозрачная подложка позади всплывающих окон и шторок настроек — теперь сквозь них мягко просвечивает живое размытое видео и фон приложения.
- **Лёгкая краевая фоновая подсветка видео без нагрузки на 4K:**
  - Фоновая подсветка (Ambient Video Glow) теперь строится прямо от внешних границ кадра, мягко окрашивая чёрные полосы по краям экрана.
  - Когда видео открыто на весь экран без чёрных полос, подсветка автоматически переходит в спящий режим с нулевой нагрузкой на процессор и батарею, а на тяжёлых 4K и 60 fps видео работает в бережном режиме без просадок частоты кадров.

</details>

## [v4.3.9] — `versionCode = 77` / `versionName = "4.3.9"`

### 🇬🇧 English
- **Streamlined Bottom Quick Action Hold Mini-Menus:**
  - Disabled hold popups on the `Decoder` and `Scale` buttons—now operating strictly via regular tap for immediate mode cycling without unexpected hold menus.
  - Simplified the `Audio` hold popup to display exclusively available audio tracks for rapid soundtrack selection, removing DSP cleanup modes and the redundant disable action.
  - Cleaned up the `Tools` hold popup by removing the top "All Tools & Settings" entry (already opened directly on click), keeping quick surface theme presets (`Flat`, `Frosted Glass`, `Liquid Glass`, and `Frameless`).
- **Preserved `AUTO (HW + SW)` Selection Across Decoders:**
  - Preserved the active `Auto (HW + SW)` selection when software decoding or automatic fallback initializes, displaying the real-time active engine in the header badge (`AUTO (SW)` or `AUTO (HW)`) rather than overwriting the user's highlighted choice.
  - Video decoder similarly maintains highlighted `HW+` user preference while indicating actual hardware/software acceleration status in the codec metrics card.
- **100% Free & Default 3D Spatial Audio (HRTF + XTC) and Free 'Soft' Audio Cleanup:**
  - `3D Spatial Audio` (HRTF binaural processing for headphones and XTC crosstalk cancellation for stereo speakers) is now enabled by default and 100% free for all users, with all Premium locks and gold stars removed.
  - Added the 'Soft' profile in `Audio Cleanup (De-Artifact DSP)` as a free tier enabled by default alongside 'Off', offering gentle de-essing and warm high-frequency harmonic restoration without requiring a subscription.
- **Compact 3-Button 3D Spatial Audio Row & Interactive [ⓘ] Technical Guide:**
  - Replaced tall vertical radio rows in the Audio sheet with a compact horizontal 3-button row (`Auto`, `HRTF 3D`, `XTC 3D`) matching the Audio Decoder style.
  - Added an interactive `[ⓘ]` reference dialog explaining binaural MIT KEMAR HRTF, speaker transaural XTC, and 2.0 Stereo Auto-Bypass architecture.
- **Removed Duplicate Privacy Policy Buttons:**
  - Consolidated Privacy Policy and open-source attributions into a single canonical location inside the version planetary modal sheet, removing duplicate entries from Settings and Device Info.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.9)</b></summary>

<br>

- **Ревизия и очистка мини-меню удержания в нижней панели быстрого доступа:**
  - Отключены мини-меню удержания для кнопок `Декодер` и `Масштаб` — теперь они работают только по обычному нажатию без лишних всплывающих окон.
  - В меню удержания кнопки `Звук` оставлен исключительно список доступных аудиодорожек для мгновенного переключения дорожки без кнопки отключения и пунктов DSP.
  - В меню удержания кнопки `Инструменты` убран верхний пункт открытия настроек, оставлены только 4 стиля поверхности (`Flat`, `Матовое стекло`, `Жидкое стекло`, `Без рамок`).
- **Сохранение выбора «Авто (HW + SW)» и «HW+» в декодерах:**
  - При программном декодировании или автоматическом фолбэке выбор пользователя `АВТО (HW + SW)` больше не сбрасывается на `SW`, а фактический движок наглядно отображается в заголовке бейджа (`АВТО (SW)` или `АВТО (HW)`).
  - В видеодекодере кнопка `HW+` остаётся активной, а статус ускорения отображается в блоке параметров кодека.
- **100% бесплатный и включённый по умолчанию Пространственный звук 3D (HRTF + XTC) и бесплатный режим «Мягкий» в Очистке звука:**
  - `Пространственный звук 3D` (HRTF для наушников и XTC для стереодинамиков) теперь включён по умолчанию и полностью бесплатен для всех пользователей без Pro-ограничений и звёздочек.
  - В модуль `Очистка звука (De-Artifact DSP)` добавлен бесплатный профиль «Мягкий» (установлен по умолчанию наряду с «Откл.») для деликатного де-эссинга и устранения артефактов без подписки.
- **Компактный ряд из 3 кнопок для 3D-звука и подробное справочное окно [ⓘ]:**
  - Вместо трёх громоздких вертикальных строк с радиокнопками в шторке звука сделан компактный горизонтальный ряд из 3 кнопок в стиле декодеров (`Авто`, `HRTF 3D`, `XTC 3D`).
  - Добавлена кнопка справки `[ⓘ]`, открывающая окно с подробным описанием алгоритмов MIT KEMAR HRTF, трансаурального XTC и авто-обхода для дорожек 2.0 стерео.
- **Удаление дублирующихся кнопок Политики конфиденциальности:**
  - Ссылка на Политику конфиденциальности и лицензии открытого ПО консолидирована в едином каноническом месте — в планетарном окне версии приложения.

</details>

---

## [v4.3.8] — `versionCode = 76` / `versionName = "4.3.8"`

### 🇬🇧 English
- **Unified Theme Badges & Status Indicators Across All Player & App Modal Windows:**
  - Replaced all legacy local badge boxes and custom color/corner overrides inside `Audio Tracks & Sync`, `Playback Tools`, `Playback Speed`, `Subtitles`, `Hardware Decoder & Video`, `Anime4K`, `Frame Interpolation`, `Video Quality`, `Playlist`, `Video Chapters`, `Skip Intro`, `Sleep Timer`, and `Media File Info` sheets with the canonical theme badge component (`Liquid Glass`, `Frosted Glass`, and `Flat`).
  - Slider value badges (`0ms`, `1.0x`, `+85s`, `100%`, `20 sp`), audio/subtitle codec badges (`AAC • 192k`, `DTS`, `SRT`, `ASS`), decoder status pills (`Auto`, `HW`, `SW`), and volume boost indicators now automatically adapt to the active theme engine with guaranteed WCAG >= 4.5:1 text contrast—eliminating pale, unreadable rectangular badges.
  - Refined the `[i]` mode reference icon button across `Audio Cleanup`, `Anime4K`, `Frame Interpolation`, and `Hardware Decoder` headers into a crisp, compact `28 dp` circular glass/tonal control.
- **Unified Interactive Step & Preset Filter Chips:**
  - Standardized audio sync step pills (`-250ms`, `-50ms`, `0ms`, `+50ms`, `+250ms`), subtitle delay step pills (`-0.5s`, `-0.1s`, `0.0s`, `+0.1s`, `+0.5s`), skip-intro step pills (`-5s`, `-1s`, `+1s`, `+5s`), the UI scale reset pill (`↻ 100%`), and the full playback speed grid (`0.5x` through `3.0x`) under the canonical filter chip design without container clipping.
- **Accurate Direct Stereo (`2.0`) Status Copy in Audio & Spatial Settings:**
  - When playing 2-channel stereo (`2.0`) or mono (`1.0`) tracks, the `3D Spatial Audio` card now displays `2.0 Stereo (Native)` when spatial audio is off (reserving `ITU Downmix` strictly for multichannel `5.1/7.1` tracks) and `Bypass 2.0 (5.1/7.1 only)` when enabled, while the output route line accurately reports `Direct Stereo Output (2.0)`.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.8)</b></summary>

<br>

- **Полная унификация всех мелких бейджей и индикаторов в модальных окнах плеера и приложения:**
  - Все локальные прямоугольные плашки и кастомные переопределения цветов/скруглений в окнах `Аудиодорожки и синхронизация`, `Инструменты плеера`, `Скорость воспроизведения`, `Субтитры`, `Аппаратный декодер и видео`, `Anime4K`, `Интерполяция кадров`, `Качество видео`, `Плейлист`, `Главы видео`, `Пропуск опенинга`, `Таймер сна` и `Сведения о файле` переведены на единый канонический компонент бейджа с полной поддержкой тем `Жидкое стекло`, `Матовое стекло` и `Flat`.
  - Индикаторы значений слайдеров (`0 мс`, `1.0x`, `+85 с`, `100%`, `20 sp`), бейджи кодеков дорожек (`AAC • 192k`, `DTS`, `SRT`, `ASS`), статусы декодера (`Авто`, `HW`, `SW`) и лимита усиления громкости теперь автоматически наследуют оптику выбранной темы и гарантируют высокий WCAG-контраст текста без выцветших нечитаемых заливок.
  - Кнопка справки `[i]` в заголовках `Очистка звука`, `Anime4K`, `Интерполяция кадров` и `Декодер` приведена к аккуратным компактным габаритам `28 dp`.
- **Единый стиль интерактивных чипов шага синхронизации, сброса масштаба и сетки скоростей:**
  - Кнопки шага сдвига звука (`-250 мс`, `-50 мс`, `0 мс`, `+50 мс`, `+250 мс`), задержки субтитров (`-0.5 с`, `-0.1 с`, `0.0 с`, `+0.1 с`, `+0.5 с`), шага пропуска опенинга (`-5 с`, `-1 с`, `+1 с`, `+5 с`), кнопка сброса масштаба интерфейса (`↻ 100%`) и вся сетка пресетов скорости (`0.5x .. 3.0x`) переведены на единый канонический стиль чипов без обрезания по краям контейнера.
- **Точная индикация прямого стереовыхода (`2.0`) в настройках звука и 3D-аудио:**
  - На обычных двухканальных стереодорожках (`2.0`) карточка `Пространственный звук 3D` теперь отображает бейдж `2.0 Стерео (Нативно)` при выключенном тумблере (оставляя `ITU Downmix` только для многоканальных `5.1/7.1` дорожек) и `Байпас 2.0 (Только 5.1/7.1)` при включённом, а строка маршрута вывода корректно показывает `Прямой стереовыход (2.0)`.

</details>

---

## [v4.3.7] — `versionCode = 75` / `versionName = "4.3.7"`

### 🇬🇧 English
- **True Zero-Copy Audio Passthrough & Zero-Allocation DSP Pipeline:**
  - Stereo (`2.0`) and Mono (`1.0`) audio tracks now return `AudioFormat.NOT_SET` during audio sink configuration in both the 3D HRTF binaural and XTC speaker processors, completely excluding them from the active audio processing graph (`0% CPU` overhead, `0` buffer copies, and full preservation of native 24-bit/32-bit/Float bit depth).
  - Eliminated per-buffer `FloatArray` heap allocations on the real-time audio thread and replaced `Double`-precision `tanh` calls with a fast Pade `[3/2]` `Float` soft-clipping approximation (`fastTanh`), cutting mathematical DSP overhead by up to 5x on multichannel `5.1/7.1` tracks.
  - Added direct zero-overhead `ByteBuffer` block transfer when audio sync delay is `0 ms` and low-bitrate de-artifact filtering is inactive, bypassing per-sample `Float` conversion loops.
- **Zero-Allocation HDR10+ / Dolby Vision Parsing & Smart Hardware Audio Decoding:**
  - Added an immediate SDR early exit before NAL unit scanning, replaced per-frame byte array allocations on Dolby Vision Profile 7 streams with a reusable scratch buffer, and deduplicated `KEY_HDR10_PLUS_INFO` `MediaCodec.setParameters` IPC calls when dynamic metadata payloads are unchanged.
  - Enabled dedicated hardware `MediaCodec` decoders for Dolby Digital (`AC3`) and Dolby Digital Plus (`E-AC3`) in `Auto` audio mode when 3D spatial audio is turned off or the stream is stereo, and cached system `MediaCodecList` queries in lazy lookup tables.
- **Eliminated Parasitic UI Recompositions & Optimized Ambient Video Glow Sampling:**
  - Throttled buffered position state updates (`bufferedPositionMs`) to `>= 1000 ms` deltas with adaptive `100 ms / 250 ms` playback ticker intervals, preventing unnecessary whole-screen Compose recompositions while keeping the seekbar at 60/120 fps via its isolated `positionMsFlow`.
  - Optimized Ambient Video Glow hardware `PixelCopy` sampling to run strictly after the first video frame is rendered using a reusable `16x16` bitmap and pixel array at `350 ms` intervals.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.7)</b></summary>

<br>

- **Истинный Zero-Copy Passthrough и отказ от аллокаций памяти в аудиопроцессорах:**
  - Для всех стерео (`2.0`) и моно (`1.0`) дорожек процессоры бинаурального звука 3D HRTF и акустической сцены динамиков XTC теперь возвращают `AudioFormat.NOT_SET` на этапе конфигурации, полностью исключая себя из активного графа аудиосинка (`0%` нагрузки на CPU, `0` копирований буферов и полное сохранение нативной разрядности `24-bit / 32-bit / Float`).
  - Устранены аллокации массивов `FloatArray` в куче на каждый буфер в реальном времени, а тяжелые вызовы `tanh(Double)` заменены на быструю `Float`-аппроксимацию Паде `[3/2]` (`fastTanh`), что ускоряет нелинейное лимитирование и басовый гармонайзер на многоканальных `5.1/7.1` дорожках до 5 раз.
  - В процессоре синхронизации звука при нулевой задержке (`0 мс`) и выключенной очистке низкого битрейта реализована прямая блочная пересылка памяти `ByteBuffer` без по-сэмпльной конвертации в `Float`.
- **Оптимизация парсера HDR10+ / Dolby Vision без нагрузки на GC и умный выбор аппаратных декодеров AC3/E-AC3:**
  - Добавлен мгновенный ранний выход для обычных SDR-видео без сканирования NAL-единиц, устранены покадровые аллокации `ByteArray` при очистке базового слоя Dolby Vision Profile 7 за счёт переиспользуемого буфера, а синхронные IPC-вызовы `MediaCodec.setParameters` для `KEY_HDR10_PLUS_INFO` выполняются только при фактическом изменении метаданных сцены.
  - В режиме аудиодекодера «Авто» разрешено использование энергоэффективных аппаратных чипов `MediaCodec` для дорожек `AC3` и `E-AC3`, когда пространственный 3D-звук выключен (или дорожка стереофоническая), а системный список кодеков `MediaCodecList` кэшируется один раз за сессию.
- **Устранение паразитных перерисовок UI во время воспроизведения и оптимизация Ambient Video Glow:**
  - Обновления буферизованной позиции (`bufferedPositionMs`) в общем состоянии плеера теперь срабатывают только при сдвиге буфера на `>= 1000 мс` с адаптивным шагом тикера `100 мс / 250 мс`, что полностью избавляет `PlayerScreen` от холостых перерисовок при сохранении идеальной плавности ползунка через изолированный поток `positionMsFlow`.
  - Сэмплер фоновой подсветки `Ambient Video Glow` переведён на переиспользуемый буфер `16x16` пикселей с запуском строго после отрисовки первого видеокадра и интервалом `350 мс`.

</details>

---

## [v4.3.6] — `versionCode = 74` / `versionName = "4.3.6"`

### 🇬🇧 English
- **Legacy CI/CD Release Tail Removed (`.github/workflows/release.yml`):**
  - Completely purged hardcoded legacy blocks (`Technical Specifications & Architecture` and `Licenses & Attribution`) from GitHub Actions `release.yml` workflow after `cat "$CHANGELOG_SECTION"`. Releases now contain clean, dynamic, version-specific release notes without outdated boilerplate.
- **Reliable Hardware Screen Blur Around Floating Windows in Glass Modes (`LibraryScreen.kt`, `PlayerScreen.kt`, `UnifiedModalsAndSelectors.kt`):**
  - Re-enabled 16-dp hardware screen blur (`RenderEffect.createBlurEffect(16.dp, 16.dp, Shader.TileMode.CLAMP)`) behind floating modal windows in `FROSTED_GLASS` and `LIQUID_GLASS` themes on Android 12+ (API 31+).
  - Fully eliminated Android Skia RenderNode recursion conflicts and backdrop blackouts by skipping `.recordGlassBackdrop(mainScreenBackdrop)` on the content `Row` and feeding `topBarBackdrop = null` into headers and navigation docks when `hasOpenModal == true`.
  - In `UnifiedModalSurface`, switched to `fallbackMeshBackdrop` directly when glass themes are active, preventing duplicate backdrop reads from blurred root nodes.
  - In `PlayerScreen.kt`, applied 16-dp hardware blur to the player HUD layer behind open modal sheets when glass themes are active without competing backdrop recording.
  - In `BaseFloatingWindow`, calibrated translucent scrim tinting: gentle `0.24f` dark / `0.14f` light veil in glass themes and clean `0.45f` dark / `0.30f` light dimming in `FLAT` theme.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.6)</b></summary>

<br>

- **Удаление устаревшего хвоста из CI/CD релиза GitHub Actions (`.github/workflows/release.yml`):**
  - Полностью удалён захардкоженный устаревший блок со спецификациями и лицензиями (`Technical Specifications & Architecture` и `Licenses & Attribution`) из шага публикации релиза в `.github/workflows/release.yml`. Теперь описания релизов GitHub формируются динамически строго из актуального чейнджлога без лишнего текста.
- **Возврат 16-dp аппаратного размытия экрана вокруг плавающих окон в темах стекла без ухода в черноту (`LibraryScreen.kt`, `PlayerScreen.kt`, `UnifiedModalsAndSelectors.kt`):**
  - Восстановлено аппаратное размытие экрана на 16 dp (`RenderEffect.createBlurEffect`) позади всех плавающих окон в темах «Матовое стекло» и «Жидкое стекло» на Android 12+ (API 31+).
  - Полностью устранён конфликт RenderNode в Skia: когда открыто плавающее окно (`hasOpenModal == true`), на главном экране отключается запись `recordGlassBackdrop(mainScreenBackdrop)`, а в шапку и нижний док передаётся `topBarBackdrop = null`, благодаря чему весь фоновый интерфейс размывается с кристальной чистотой без единого чёрного пикселя.
  - В `UnifiedModalSurface` для стеклянных тем используется `fallbackMeshBackdrop`, исключающий чтение размытого узла дерева отрисовки.
  - В `PlayerScreen.kt` при открытых шторках и выбранной стеклянной теме интерфейс HUD позади окна размывается на 16 dp без паразитной записи бэкдропа.
  - В `BaseFloatingWindow` настроена аккуратная полупрозрачная вуаль затемнения: лёгкая вуаль `24%` (тёмная) / `14%` (светлая) в темах стекла поверх размытого фона и аккуратное затемнение `45%` (тёмная) / `30%` (светлая) в теме Flat.

</details>

---

## [v4.3.5] — `versionCode = 73` / `versionName = "4.3.5"`

### 🇬🇧 English
- **Truly Dynamic 4-Zone Ambient Video Glow Strictly Outside Video Bounds:**
  - Implemented real-time 4-zone frame color sampling (`VideoAmbientPaletteState`) capturing average top, bottom, left, and right edge colors every 260ms via hardware-accelerated `PixelCopy` on Android 7.0+ (API 24+), enhancing optical glass saturation (`boostOpticalGlassSaturation(..., 1.25f)`) and smoothly interpolating transitions over 420ms with linear easing.
  - Completely removed any overlay canvas layer drawn with `BlendMode.Screen` over the video rectangle, keeping the video frame 100% pristine and free of hazy purple tints.
  - In `Anime4KVideoSurface`, applied `Modifier.aspectRatio(effectiveAspect)` inside a centered `Box` container when `scaleMode == VideoScaleMode.FIT`, exposing letterbox and pillarbox margins for 4 soft radial-elliptical glow halos that bloom from the video frame perimeter outwards.
  - Synchronized `LocalVideoAmbientTintColor` with the animated dominant scene color so floating player HUD controls and glass sheets breathe naturally with the film palette.
- **ITU-R BS.775 Multichannel Mixing & Precise 3D Spatial Audio Localization:**
  - Corrected `ChannelMixingAudioProcessor` matrices in 5.1, 6.1, and 7.1 audio: routed `ch3` (LFE) symmetrically to both Left and Right channels (`0.22f, 0.22f`), mapped `ch4` (BL/SL) strictly to the left channel (`0.34f, 0.0f`), and mapped `ch5` (BR/SR) strictly to the right channel (`0.0f, 0.34f`).
  - Fixed 7.1 channel layout ordering in `HrtfBinauralAudioProcessor` and `XtcSpeakerAudioProcessor`: mapped `ch4` and `ch5` as Back Left/Right (`BL/BR`, `±145°`) and `ch6` and `ch7` as Side Left/Right (`SL/SR`, `±95°`).
  - Added acoustic Head-Shadow low-pass filtering (2600 Hz cutoff) on contralateral front channels (`FL/FR`) for crisp, forward-projected screen-plane staging without cross-bleeding.
  - Equipped rear channels (`BL/BR`) with a 1st-order Pinna Rear Shadow filter (3100 Hz cutoff, combining 62% warm low-pass + 38% notched signal), rear propagation delay, and coherent rear reflection routing without anti-phase phase cancellation.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.5)</b></summary>

<br>

- **Настоящая динамическая 4-зонная подсветка Ambient Video Glow строго за пределами кадра:**
  - Реализован аппаратный 4-зонный сэмплер реального видеокадра (`VideoAmbientPaletteState`), извлекающий средние цвета верхней, нижней, левой и правой четвертей изображения каждые 260 мс через `PixelCopy` (на Android 7.0+ API 24+) с усилением насыщенности (`boostOpticalGlassSaturation(..., 1.25f)`), сохранением физической яркости сцены и плавной 420-мс интерполяцией.
  - Полностью удалён любой слой подсветки с `BlendMode.Screen`, рисовавшийся поверх видеокадра: само видео теперь отображается со 100% кристальной чистотой без мутной цветной пелены.
  - В `Anime4KVideoSurface` при режиме `scaleMode == VideoScaleMode.FIT` добавлен модификатор `aspectRatio` с центровкой в `Box`, благодаря чему чёрные полосы (`letterbox` / `pillarbox`) остаются открытыми для 4 мягких радиально-эллиптических ореолов, расходящихся от границ кадра.
  - Значение `LocalVideoAmbientTintColor` синхронизировано с доминирующим цветом сцены, благодаря чему стеклянные панели HUD деликатно дышат в тон видеоряду.
- **Матрицы микширования ITU-R BS.775 и точное разделение 3D-звука в 5.1 и 7.1:**
  - Исправлены матрицы `ChannelMixingProcessor` в 5.1, 6.1 и 7.1: сабвуфер `ch3` (LFE) теперь подаётся строго по центру в оба уха (`0.22f, 0.22f`), задний левый `ch4` (BL/SL) — строго в левый канал (`0.34f, 0.0f`), а задний правый `ch5` (BR/SR) — строго в правый канал (`0.0f, 0.34f`).
  - В 7.1 потоках процессоров `HRTF` и `XTC` исправлен порядок каналов: `ch4, ch5` назначены тыловыми (`BL/BR`, `±145°`), а `ch6, ch7` — боковыми (`SL/SR`, `±95°`).
  - Для передних каналов (`FL/FR`) добавлен ФНЧ тени головы на 2600 Гц для противоположного уха с чистым верхом для ипсилатерального канала, что даёт чёткую широкую фронтальную сцену перед экраном.
  - Для тыловых каналов (`BL/BR`) добавлена физическая фильтрация Pinna Rear Shadow (ФНЧ 3100 Гц + режекторный фильтр 6200 Гц), тыловая задержка прихода волны и когерентное тыловое отражение без противофазного размытия.

</details>

---

## [v4.3.4] — `versionCode = 72` / `versionName = "4.3.4"`

### 🇬🇧 English
- **Delicate Scroll-Edge Meniscus Calibration in Portrait & Landscape Modes (`UnifiedDesignSystem.kt`):**
  - Reduced scroll-edge optical zone height from `36..44 dp` down to **`22 dp`** (`Liquid Glass`) / **`20 dp`** (`Frosted Glass`) in portrait mode, and to a slim **`14 dp`** / **`13 dp`** in landscape orientation.
  - Capped maximum edge zone container fraction to `16%` in portrait and `11%` in landscape (down from `30%`), and reduced base chromatic dispersion to a delicate **`0.85 dp`**.
  - Tuned the AGSL shader to weaken geometric wave warping by 3.5x (`scaleX = 1.0 + 0.022 * t2 * ...`, `dy = 0.065 * t2 * ...` in Liquid Glass; `dy = 0.04 * t2 * ...` in Frosted Glass) with smooth edge dissolving (`smoothstep(0.08, 1.0, t)`) and refined micro-dispersion so text never ripples violently across short landscape lists.
- **Auto-Bypass 3D Spatial Audio for Stereo (2.0) and Mono (1.0) Tracks:**
  - Configured `HrtfBinauralAudioProcessor`, `XtcSpeakerAudioProcessor`, and `SpatialAudioCoordinator` to activate 3D spatial audio strictly for multichannel streams (`5.1 / 7.1`, `channels > 2`), automatically switching to bit-transparent, zero-filtering bypass when playing standard 2.0 Stereo or 1.0 Mono tracks.
  - Added live `Bypass 2.0 (5.1/7.1 only)` status badges and informative hints in the Audio Tracks Sheet, plus `3D: Bypass (2.0)` status reporting in Stream HUD.
- **Zero Screen Black-Out Behind Floating Modal Windows in Glass Themes:**
  - Removed duplicate `Modifier.drawBackdrop(...)` sampling from the fullscreen `Box` scrim in `BaseFloatingWindow` (which previously blanked out the screen to black in glass themes due to recursive `LayerBackdrop` sampling), replacing it with clean, translucent scrim tinting (`38%` dark / `22%` light in glass themes, `62%` / `45%` in Flat).
  - Wired `mainScreenBackdrop.notifyContentScrolled()` to `mainScreenBackdrop.hasOpenModal` in `LibraryScreen.kt` so background content and glass refraction remain crystal-clear from the very first frame of opening any dialog.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.4)</b></summary>

<br>

- **Деликатная калибровка краевой преломляющей линзы скролла в портретном и альбомном режимах:**
  - Высота оптической зоны уменьшена с громоздких `36..44 dp` до деликатных **`22 dp`** («Жидкое стекло») / **`20 dp`** («Матовое стекло») в портретном режиме и до тонких **`14 dp`** / **`13 dp`** в альбомной ориентации.
  - Максимальная доля зоны от высоты окна ограничена до `16%` в портрете и `11%` в ландшафте (вместо старых `30%`), а базовая хроматическая дисперсия снижена до тонких **`0.85 dp`**.
  - В AGSL-шейдере изгиб текста ослаблен в 3.5 раза (`scaleX = 1.0 + 0.022 * t2 * ...`, `dy = 0.065 * t2 * ...` в «Жидком стекле»; `dy = 0.04 * t2 * ...` в «Матовом стекле») с мягким растворением `smoothstep(0.08, 1.0, t)` и аккуратным сглаживанием — строки текста больше не «плавают» волнами при прокрутке.
- **Автоматический обход (Auto-Bypass) пространственного звука 3D для стерео (2.0) и моно (1.0) дорожек:**
  - Процессоры `HRTF` (для наушников) и `XTC` (для динамиков) теперь активируют виртуализацию объёма строго на многоканальных дорожках (`5.1 / 7.1`, каналов > 2), а для стандартных дорожек 2.0 Стерео и 1.0 Моно мгновенно переходят в чистый побитовый обход без изменения АЧХ.
  - В шторку аудиодорожек добавлена индикация `Байпас 2.0 (Только для 5.1/7.1)` с подробным пояснением, а в «Монитор потока» (Stream HUD) выводится статус `3D: Байпас (2.0)`.
- **Устранение потемнения экрана позади плавающих окон в темах стекла:**
  - Убрано дублирующее размытие `Modifier.drawBackdrop` с полноэкранного скрима вокруг `BaseFloatingWindow` (вызывавшее уход фона в чёрный цвет из-за двойного захвата `LayerBackdrop`), скрим переведён на чистую полупрозрачную тонировку (`38%` / `22%` в темах стекла).
  - Добавлен мгновенный вызов `notifyContentScrolled()` при открытии модального окна в `LibraryScreen.kt`, благодаря чему главный экран позади окна виден с первой миллисекунды без затемнения.

</details>

---

## [v4.3.3] — `versionCode = 71` / `versionName = "4.3.3"`

### 🇬🇧 English
- **Overhauled HLS (`/720.mp4:hls:manifest.m3u8`), DASH, RTSP, P2P TorrServer & Network Playlist Streaming:**
  - Added explicit stream MIME resolution (`APPLICATION_M3U8` for `:hls:`, `/hls/`, `manifest.m3u8`, `master.m3u8`, `index.m3u8`, `format=m3u8`; `APPLICATION_MPD` for `:dash:`, `/dash/`, `manifest.mpd`; `APPLICATION_SS` for `.ism/manifest`; `APPLICATION_RTSP` for `rtsp://`) and case-insensitive scheme normalization (`Https://` -> `https://`, `Http://` -> `http://`, `Rtsp://` -> `rtsp://`) so CDN URLs containing `.mp4:hls:manifest.m3u8` open directly in the HLS engine instead of failing as progressive MP4 files.
  - Upgraded the HTTP streaming transport with a modern mobile browser `User-Agent` (preventing CDN balancing blocks on `solodcdn` and similar hosts), cross-protocol `http <-> https` redirect following, `|User-Agent=...&Referer=...&Origin=...&Cookie=...` pipe-header parsing, remote HTTP/HTTPS `.m3u` playlist loading (`#EXTINF` + `#EXTVLCOPT`), and 5-attempt segment retry policy.
  - Prevented network stream source/container errors from falsely downgrading the hardware video decoder (`HW+` -> `SW`) and added automatic HLS fallback retry when an extensionless HTTP stream returns an HLS manifest.
- **Unified 100% UI & Font Scale Across Video Player HUD and Floating Sheets:**
  - Removed the hidden `0.90x` density and `0.88x` font-scale downscaling on player modal windows as well as DPI-based auto-shrinking at `100%` scale, ensuring all player buttons, `1080p`/`4K` & `HDR` badges (`SemiBold`), and floating sheets match the main interface proportions at `100%`.
- **100% Transparent "Frameless" HUD Mode & Distinct "Frosted Glass" vs "Liquid Glass" in Video Player:**
  - Removed residual glass overlays and dark outer shadow rings in **Frameless** (`Без фона`) mode so top HUD buttons, timeline pills (`Skip Intro`, `A-B Loop`, `Chapter`), center transport buttons, and bottom quick actions become 100% transparent floating icons (`0 dp` border, `0%` background fill).
  - Established a bold visual distinction in the video player between **Frosted Glass** (`76%` velvet-matte graphite fill with soft satin sheen and `0.9 dp` matte rim) and **Liquid Glass** (`24%` crystal-clear optical lens fill with bright dome specular highlight and a sharp `1.45 dp` `135°` diamond gradient rim).

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.3.3)</b></summary>

<br>

- **Ревизия потокового воспроизведения HLS (`/720.mp4:hls:manifest.m3u8`), DASH, RTSP, P2P TorrServer и сетевых плейлистов:**
  - Добавлено явное определение MIME-типа потока (`APPLICATION_M3U8` для `:hls:`, `/hls/`, `manifest.m3u8`, `master.m3u8`, `index.m3u8`, `format=m3u8`; `APPLICATION_MPD` для `:dash:`, `/dash/`, `manifest.mpd`; `APPLICATION_SS` для `.ism/manifest`; `APPLICATION_RTSP` для `rtsp://`) и нормализация регистра протокола (`Https://` -> `https://`, `Http://` -> `http://`, `Rtsp://` -> `rtsp://`), благодаря чему CDN-ссылки вида `.mp4:hls:manifest.m3u8` сразу открываются как HLS-поток без ошибки чтения контейнера.
  - Обновлён сетевой HTTP-транспорт плеера: современный браузерный `User-Agent` (без блокировок со стороны CDN вроде `solodcdn`), поддержка кросс-протокольных редиректов `http <-> https`, разбор кастомных заголовков через `|User-Agent=...&Referer=...&Origin=...&Cookie=...`, загрузка удалённых `.m3u` плейлистов по `http/https` (`#EXTINF` и `#EXTVLCOPT`) и 5 попыток переподключения при сбое сегмента.
  - Исключено ложное переключение видеодекодера с `HW+` на `SW` при сетевых ошибках источника и добавлен автоматический перезапуск в режиме HLS, если сервер отдаёт манифест по ссылке без расширения `.m3u8`.
- **Единый 100% масштаб интерфейса и шрифта в видеоплеере и плавающих окнах:**
  - Убрано принудительное уменьшение плотности (`0.90x`) и шрифта (`0.88x`) у модальных шторок плеера, а также авто-сжатие на широких экранах при масштабе `100%` — теперь кнопки HUD, пилюли разрешения `1080p`/`4K` и `HDR` (`SemiBold`) и все шторки плеера имеют единый эталонный размер наравне с главным меню.
- **100% прозрачный режим «Без фона» и яркое визуальное различие «Матового стекла» и «Жидкого стекла» в плеере:**
  - В режиме **«Без фона» (`Frameless`)** полностью отключены остаточные стеклянные подложки и тёмные контурные кольца у всех верхних кнопок, капсул над таймлайном («Пропустить опенинг», `A-B`, «Глава»), центральных кнопок управления и нижнего бара — остаются только чистые парящие иконки и текст с тенью (`0%` фона, `0 dp` рамок).
  - Кардинально разведены стили **«Матовое стекло»** (плотная бархатно-матовая графитовая тонировка `76%` с мягкой сатиновой вуалью и деликатной гранью `0.9 dp`) и **«Жидкое стекло»** (кристально прозрачная оптическая линза `24%` с ярким купольным бликом и острой алмазной фаской `1.45 dp` под углом `135°`).

</details>

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
