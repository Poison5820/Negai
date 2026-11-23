# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

> Older versions (v4.9.6 and earlier) are archived in `CHANGELOG_ARCHIVE.md`.

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

## [v4.9.14]

### 🇬🇧 English
- Accelerated neural motion smoothing on mobile GPUs for smoother high-refresh anime playback.
- Eliminated jelly-like distortion on fast dark scenes in neural smoothing while keeping smooth pans.
- Prevented fast-moving foreground objects from shattering in optical flow with cohesive sharp fallback.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.14)</b></summary>

<br>

- Ускорено нейросетевое сглаживание движений на мобильных видеоускорителях для плавного воспроизведения при высокой герцовке.
- Устранено «желейное» искажение на быстрых тёмных сценах в нейросетевом сглаживании без потери плавности панорам.
- Убрано рассыпание быстро движущихся объектов в оптическом потоке за счёт цельного резкого отката.

</details>

## [v4.9.13]

### 🇬🇧 English
- Eliminated RIFE neural motion smoothing stutters in ×2 and 60 Hz modes with non-blocking real-frame presentation, post-present flow scheduling, and dedicated flow-time tracking.
- Fixed fast-moving object breakup in ×2 optical flow at mid-interval with deeper 3-stage coarse refinement and stricter sharp-background fallback.
- Restored automatic RIFE neural activation for anime in Auto mode on native FP16 GPUs, with silent fallback to optical flow under sustained load or severe thermal throttling.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.13)</b></summary>

<br>

- Устранены рывки нейросетевого сглаживания RIFE в режимах ×2 и 60 Гц за счёт опережающего расчёта движения после показа реального кадра и точного контроля времени потока.
- Убрано рассыпание быстро движущихся объектов в оптическом потоке ×2 на середине интервала за счёт расширенного многошагового поиска и более строгого отката к резкому фону.
- Восстановлена автоматическая активация нейросети RIFE для аниме в режиме «Авто» на видеоускорителях с поддержкой FP16, с тихим переходом на оптический поток при нехватке запаса или перегреве.

</details>

## [v4.9.12]

### 🇬🇧 English
- Added Night Mode dialogue compression, low-volume loudness compensation, and instant original-sound bypass.
- Combined all sound tools under the Audio Enhancement button and split Audio tab into Tracks and Enhancements.
- Fixed Android 16 system volume popup conflicts and restored Bluetooth headset volume control.
- Made the hold-to-speed badge compact and centered, and fixed slow playback sticking after closing Frame Inspector.
- Reduced stair-stepping and harsh edges in Anime4K Ultra while simplifying the automatic motion smoothing label.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.12)</b></summary>

<br>

- Добавлены Ночной режим для чётких диалогов, тонкомпенсация на низкой громкости и мгновенный A/B-обход улучшений.
- Объединены все звуковые фильтры в кнопке «Улучшение звука», а вкладка «Звук» разделена на «Дорожки» и «Улучшения».
- Исправлено наложение системного окна громкости на Android 16 и восстановлена регулировка громкости с Bluetooth-наушников.
- Капсула ускорения по удержанию стала компактной по центру, а также исправлено залипание скорости 0.5× после Инспектора.
- Смягчены ступеньки и перешарп в режиме Anime4K Ultra, а подпись автоматического сглаживания упрощена до «Авто».

</details>

## [v4.9.11]

### 🇬🇧 English
- Kept fast-moving thin objects intact in optical flow and separated anime and cinema profiles.
- Eliminated frame-time spikes at maximum refresh rate and made optical flow the default automatic engine.
- Cleaned up neural frame blending to follow the model's native mask without extra artifacts.
- Improved automatic anime versus cinema detection over longer clips and after dark intros.
- Fixed the black left side in split comparison when enabled while playback is paused.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.11)</b></summary>

<br>

- Сохранена цельность тонких объектов в быстром движении и разделены профили оптического потока для аниме и кино.
- Убраны пики времени кадра в режиме максимальной герцовки, а оптический поток стал движком по умолчанию в «Авто».
- Очищено смешивание кадров нейросети от лишних правок для более ровной картинки без искажений.
- Повышена точность автоопределения стиля «Аниме» и «Кино» на тёмных заставках и при смене настроек.
- Исправлена чёрная левая половина шторки сравнения при её включении на паузе.

</details>

## [v4.9.10]

### 🇬🇧 English
- Fixed motion smoothing stutters across ×2, 60 fps, and maximum refresh rate modes.
- Added background motion preparation on high-refresh screens to keep real frames instant.
- Eliminated translucent double-image ghosts when optical flow falls back on fast motion.
- Removed background jiggling on anime camera pans and dark jelly distortion in neural smoothing.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.10)</b></summary>

<br>

- Устранены рывки сглаживания движений в режимах ×2, 60 к/с и максимальной герцовки.
- Добавлен фоновый предрасчёт движения на экранах 120 Гц для вывода кадров без задержки.
- Убраны полупрозрачные дубли контуров при сбое оптического потока на быстрых сценах.
- Исправлено подёргивание фона на панорамах аниме и тёмное «желе» при нейросетевом сглаживании.

</details>

## [v4.9.9]

### 🇬🇧 English
- Eliminated double-image ghosting on neural motion smoothing edges and sped up frame blending.
- Stabilized smooth 48 fps ×2 anime playback on high-refresh screens without duplicate frames.
- Locked the Frame Inspector slider in ×2 mode to three exact frame phases.
- Tightened automatic intro and ending detection boundaries to trim extra dialogue seconds.
- Updated remaining neural model labels to RIFE v4.26 and fixed benchmark text clipping.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.9)</b></summary>

<br>

- Устранено двоение контуров при сбое нейросетевого сглаживания и ускорен проход смешивания кадров.
- Стабилизирована плавность 48 к/с в режиме ×2 на экранах 120 Гц без дублей.
- Ползунок инспектора кадров в режиме ×2 закреплён на трёх реальных фазах кадра.
- Повышена точность границ опенингов и эндингов с отсечением лишних секунд диалогов.
- Обновлены подписи нейросетевой модели до RIFE v4.26 и убрана обрезка текста бенчмарка.

</details>

## [v4.9.8]

### 🇬🇧 English
- Upgraded bundled anime neural motion smoothing to the newer RIFE v4.26 model.
- Routed automatic motion smoothing to use neural interpolation for anime ×2 and optical flow for movies or high frame rates.
- Enforced a strict two-phase ×2 frame cadence for smooth, even 48 fps motion.
- Added static background lock and edge-aware flow checks to reduce jelly distortion on anime.
- Expanded optical flow search range for cleaner fast camera pans and large movements.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.8)</b></summary>

<br>

- Обновлена встроенная нейросетевая модель сглаживания аниме до официальной версии RIFE v4.26.
- В авторежиме нейросеть теперь работает для аниме в ×2, а для кино и 60/Макс. Гц выбирается оптический поток.
- Закреплён строгий двухфазный ритм режима ×2 для ровной плавности 48 кадров в секунду.
- Добавлена фиксация статичного фона и защита контуров от эффекта «желе» на движущемся аниме.
- Расширен радиус поиска оптического потока для более чистых быстрых панорам и резких движений.

</details>

## [v4.9.7]

### 🇬🇧 English
- Accelerated local intro and ending audio scanning dramatically with in-process decoding and unthrottled codecs.
- Restored full high-refresh smoothness for neural motion smoothing on 90–120 Hz screens.
- Reduced contour ghosting and increased detail in anime neural motion smoothing with sharper occlusion blending.
- Refined player interface animations with snappy fade-out and decelerated fade-in curves.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.7)</b></summary>

<br>

- В разы ускорен локальный аудиопоиск опенингов и эндингов за счёт быстрого встроенного декодирования.
- Восстановлена высокая частота кадров нейросетевого сглаживания в режиме максимальной герцовки.
- Уменьшено двоение контуров и повышена детализация нейросетевой интерполяции на аниме за счёт чёткой маски границ.
- Улучшена плавность появления и исчезновения элементов интерфейса плеера за счёт асимметричных кривых анимации.

</details>
