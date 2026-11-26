# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

> Older versions (v4.9.14 and earlier) are archived in `CHANGELOG_ARCHIVE.md`.

## [v4.11.6]

### 🇬🇧 English
- Switched video playback to a direct hardware display layer for lower latency and native refresh-rate voting.
- Added an on-demand lightweight backdrop for frosted and liquid glass that uses zero GPU work when hidden or in flat styles.
- Smoothed pinch-zoom, panning, and predictive back gestures directly on the video surface without black trails.
- Preserved clean screenshots, ambient edge lighting, and smoothness diagnostics on the direct video layer.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.6)</b></summary>

<br>

- Вывод видео переведён на прямой аппаратный слой дисплея для снижения задержки и точного управления частотой экрана.
- Добавлена лёгкая подложка по требованию для матового и жидкого стекла, полностью отключающаяся при скрытом HUD и в плоских стилях.
- Повышена плавность масштабирования, панорамирования и жеста «Назад» прямо на видеослое без чёрных шлейфов.
- Сохранена корректная работа чистых скриншотов, внешней подсветки и диагностики плавности на прямом видеослое.

</details>

## [v4.11.5]

### 🇬🇧 English
- Smoothed media-clock timing to eliminate micro-jitter and single-tick phase freezes during camera pans.
- Fixed two-step ×2 smoothing rhythm to prevent premature frame snapping and improved frame-capture reliability.
- Eliminated frame-rate drops in neural Max Hz smoothing so playback stays smooth on 120 Hz screens.
- Added live refresh-rate tracking with even 60 fps pacing on 120 Hz panels.
- Updated benchmark jitter telemetry to measure actual displayed output cadence.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.5)</b></summary>

<br>

- Добавлено плавное сглаживание медиа-часов для устранения микродрожания фазы и залипаний на панорамах.
- Исправлен строгий двухфазный ритм режима плавности ×2 без досрочного перескока и повышена стабильность захвата кадров.
- Устранено падение частоты кадров в максимальном режиме нейросетевой плавности на экранах 120 Гц.
- Подключён живой замер реальной частоты экрана и строго равномерный вывод 60 к/с на 120 Гц дисплеях.
- Обновлён замер джиттера в бенчмарке для честного контроля ритма выводимых на экран кадров.

</details>

## [v4.11.4]

### 🇬🇧 English
- Cleaned up neural motion smoothing to follow reference blending for smoother character turns and even pans.
- Fixed hardware GPU timing measurement and frame submission recovery for more reliable neural smoothness.
- Eliminated translucent double-image ghosting and ragged island breakup across optical flow motion smoothing.
- Improved cohesive motion across flat color regions and bright lights during camera pans.
- Added full-frame fallback to a crisp original frame during flashes, chaotic motion, or high motion error.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.4)</b></summary>

<br>

- Очищено нейросетевое сглаживание движений до эталонного смешивания для плавных поворотов персонажей и ровных панорам.
- Исправлен аппаратный замер времени GPU и безопасное восстановление состояния после ошибок отправки кадров.
- Полностью убрано полупрозрачное двоение и рассыпание объектов на «островки» в оптическом потоке.
- Улучшено цельное движение однотонных заливок и ярких объектов на панорамах камеры.
- Добавлен полнокадровый откат к резкому исходному кадру при вспышках, резком хаосе и высоком проценте брака движения.

</details>

## [v4.11.3]

### 🇬🇧 English
- Added a real-time Audio Spectrum Inspector overlay comparing Pre-DSP and Post-DSP frequency response across 36 logarithmic bands with peak hold and smooth decay.
- Added live stereo soundstage width (Side/Mid), phase correlation, L/R level meters, final limiter (CLIP/GR) indicator, and active audio pipeline badges.
- Added a quick A/B (Pre / Post) button directly in the inspector header to bypass or restore all audio DSP on the fly, plus drag, pin, and compact/expanded modes.
- Added Audio Spectrum Inspector toggles in Audio → Enhancements and Tools → HUD while keeping zero CPU usage when closed.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.3)</b></summary>

<br>

- Добавлен «Инспектор спектра аудио» в реальном времени с визуализацией спектра До и После DSP по 36 логарифмическим полосам с удержанием пиков и плавным спадом.
- Добавлены индикаторы ширины стереосцены (Side/Mid), фазовой корреляции, уровней L/R, срабатывания финального лимитера (CLIP/GR) и бейджи активного аудиотракта.
- Добавлена быстрая кнопка A/B («До / После») прямо в шапке инспектора для мгновенного обхода DSP на лету, а также перетаскивание, закрепление и компактный/расширенный режимы.
- Добавлены переключатели «Инспектора спектра аудио» в «Звук → Улучшения» и «Инструменты → HUD» при нулевой нагрузке на CPU в закрытом состоянии.

</details>

## [v4.11.2]

### 🇬🇧 English
- Fixed instant on-the-fly switching for 2.0 stereo soundstage widening (Off / Soft / Wide) on headphones and speakers without restarting audio playback.
- Widened the stereo soundstage in Soft and Wide modes while keeping centered dialogue clear and deep bass focused.
- Cleaned up the Stereo 2.0 Volume card by removing duplicate titles and redundant switches and updating the description.
- Unified the player audio button gesture so a short tap toggles sound enhancements on or off and a long press opens the Audio Enhancements menu.
- Eliminated background glass stuttering behind the Audio modal sheet during live video playback.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.2)</b></summary>

<br>

- Исправлено мгновенное переключение расширения стереосцены 2.0 («Выкл / Мягкая / Широкая») на лету в наушниках и динамиках без перезапуска звука.
- Усилена ощутимая ширина стереосцены в режимах «Мягкая» и «Широкая» при сохранении чётких голосов по центру и плотного баса.
- Упрощена карточка «Объём для стерео 2.0»: убраны дублирующийся заголовок и лишний тумблер, обновлено описание.
- Кнопка звука в панели плеера приведена к единому жесту: короткое нажатие включает или выключает улучшения, а удержание открывает меню «Звук → Улучшения».
- Убраны рывки и отставание стеклянного фона под модальным окном «Звук» во время воспроизведения видео.

</details>

## [v4.11.1]

### 🇬🇧 English
- Moved all player quick-hold mini-menus (Smoothness, Upscale, speed, and audio) into an in-window overlay so frosted and liquid glass refresh synchronously with the video at full 120 Hz.
- Added full-screen tap-to-dismiss and Back button support for active player mini-menus with safe-area positioning above the quick action bar.
- Made the bottom floating navigation dock slide down smoothly whenever Quick Appearance or any other modal opens and return smoothly on close.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.1)</b></summary>

<br>

- Все быстрые мини-меню плеера («Плавность», «Апскейл», скорость и звук) переведены на отрисовку внутри основного окна без отставания матового и жидкого стекла при 120 Гц.
- Сохранено закрытие мини-меню плеера по нажатию мимо и кнопкой «Назад» с точным позиционированием над панелью быстрых кнопок.
- Нижний плавающий док навигации теперь плавно уезжает вниз при открытии окна «Быстрое оформление» и других модальных окон и плавно возвращается при их закрытии.

</details>

## [v4.11.0]

### 🇬🇧 English
- Upgraded the internal sound engine to high-precision floating-point processing with lower output latency.
- Added a ten-band graphic equalizer with genre presets, automatic headroom protection, and smooth transitions.
- Introduced dedicated stereo headphone widening, cleaner speaker crosstalk bass, and smarter automatic spatial routing.
- Accelerated neural anime upscaling, improved cold startup speed, and added adaptive low-end device profiles.
- Stabilized lookahead motion smoothing frame order, neural precomputation hit rate, and optical flow guards.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.11.0)</b></summary>

<br>

- Звуковой тракт переведён на точную обработку с плавающей точкой и сниженную задержку вывода.
- Добавлен десятиполосный эквалайзер с пресетами, защитой запаса громкости и плавным сглаживанием без щелчков.
- Добавлено расширение стереосцены в наушниках, сохранён бас динамиков и обновлена автоматическая маршрутизация объёма.
- Ускорен нейросетевой апскейл аниме, сокращено время холодного запуска и добавлены адаптивные профили устройств.
- Стабилизирован порядок кадров опережающего сглаживания, восстановлен предрасчёт нейросети и усилена защита оптического потока.

</details>

## [v4.10.1]

### 🇬🇧 English
- Restored crisp, delicate frosted glass texture on high-DPI screens with gentle attenuation on HD+ displays.
- Kept frosted glass texture visible on popups, modals, and settings cards while suppressing double grain on small nested controls.
- Streamlined the Upscale quick-hold mini-menu into a single compact row of all five engines without wrapping.
- Tightened preset row spacing and collapsed unused quality presets when ArtCNN is selected so the menu stays clear of the seekbar.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.10.1)</b></summary>

<br>

- Возвращена чёткая деликатная фактура матового стекла на экранах высокого разрешения с мягким снижением на HD+-дисплеях.
- Сохранена текстура матового стекла на всплывающих меню, модалках и карточках настроек при подавлении лишнего шума на мелких вложенных контролах.
- Разгружен верх мини-меню «Апскейл»: все пять движков выстроены в одну компактную строку без переноса.
- Уменьшены вертикальные отступы строк пресетов и свёрнуты неиспользуемые уровни качества при выборе ArtCNN, чтобы меню не перекрывало сикбар.

</details>

## [v4.10.0]

### 🇬🇧 English
- Rebuilt MEMC motion smoothing around a 6-frame Lookahead ring with ahead-of-time decoder frame release and exact 1s / 2s / 3s anime cadence detection.
- Added double-buffered RIFE neural flow calculation with 2-chunk scheduling at 85 Hz and above to eliminate high-refresh stutters and remove redundant pre-restore passes.
- Upgraded Vulkan Compute optical flow to TV-style 3DRS predictive block search, multi-vector OBMC synthesis, and blurred soft artifact masks with a unified post-MEMC upscale path.
- Added ArtCNN C4F16 neural anime upscaling, single-pass Qualcomm SGSR v1 cinema upscaling, and optional 16-bit Debanding before upscaling.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.10.0)</b></summary>

<br>

- Перестроен конвейер сглаживания движений MEMC на 6-кадровый Lookahead-буфер с опережающим выпуском кадров декодера и точным распознаванием темпа 1s / 2s / 3s в аниме.
- Добавлена двойная буферизация нейросетевого потока RIFE с 2-чанковым разбиением на экранах 85 Гц и выше для устранения подёргиваний и холостых проходов GPU.
- Оптический поток на Vulkan Compute переведён на TV-уровень алгоритмов: предиктивный поиск 3DRS, многовекторный синтез OBMC, сглаженные маски артефактов и единый тракт апскейла после MEMC.
- Добавлены нейросетевой апскейлер ArtCNN C4F16 для аниме, быстрый однопроходный Qualcomm SGSR v1 для кино и отключаемый 16-битный Дебандинг градиентов перед апскейлом.

</details>

## [v4.9.15]

### 🇬🇧 English
- Enabled precision frame pacing with display timing and 2× LTPO refresh rates (48 / 96 Hz) for 24 fps RAW playback and ×2 motion smoothing without 3:2 judder or screen blackouts.
- Switched live RIFE flow timing to hardware GPU timestamps, restoring 448×256 working resolution in ×2 mode.
- Tightened static-background distortion halos around moving characters and objects.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.15)</b></summary>

<br>

- Подключён точный пейсинг кадров через адаптивную сетку и тайминг дисплея с режимом 2× LTPO (48 / 96 Гц) для 24 к/с в обычном просмотре и сглаживании ×2 без рывков 3:2 и мигания экрана.
- Исправлен аппаратный GPU-замер времени нейросети RIFE, возвращающий высокое разрешение 448×256 в режиме ×2.
- Сужен ореол искажений на статичном фоне вокруг движущихся персонажей и объектов.

</details>
