# Changelog / История изменений — Negai Player (`app.negai.player`)

Все заметные изменения в каждом релизе **Negai — Anime & Cinema 4K Player** фиксируются в этом файле для формирования описаний релизов GitHub и Google Play.
Шаблон версий: **`x.x.y`** (`x.x` меняется только вручную, `y` и `versionCode` увеличиваются автоматически при каждом изменении кода).

> Older versions (v4.9.3 and earlier) are archived in `CHANGELOG_ARCHIVE.md`.

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

## [v4.9.6]

### 🇬🇧 English
- Accelerated neural frame interpolation significantly with wider GPU weight reads and faster coarse layers.
- Fixed neural motion smoothing pacing on high-refresh screens and refined synthetic motion verification checks.
- Eliminated interface fade stutters on Frosted Glass by throttling backdrop refreshes during transitions.
- Fixed local intro and ending audio analysis timeouts on slow codecs and prevented duplicate scans.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.6)</b></summary>

<br>

- Существенно ускорена нейросетевая интерполяция кадров за счёт оптимизации чтения весов и грубых слоёв GPU.
- Улучшена плавность вывода кадров нейросети на экранах 120 Гц и исправлена проверка движения в тесте.
- Устранены рывки анимации интерфейса «Матового стекла» за счёт отключения лишних обновлений фона и частоты экрана.
- Исправлены тайм-ауты локального аудиопоиска опенингов на программных декодерах и убран лишний повторный запуск анализа.

</details>

## [v4.9.5]

### 🇬🇧 English
- Improved neural motion smoothing sharpness and contour clarity with anti-aliased multi-scale sampling, phase-accurate motion blending, and cleaner moving edges.
- Added aspect-preserving 448×256 working resolution for ×2 neural motion smoothing and eliminated unstable switching between neural and optical flow engines.
- Added predicted motion vector and static background verification checks to the standalone synthetic RIFE benchmark with a dedicated ×2 quality profile.
- Fixed local intro and ending audio detection being overwritten during playback updates and expanded diagnostic reporting for local series.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.5)</b></summary>

<br>

- Повышена чёткость и чистота контуров при нейросетевой интерполяции кадров за счёт сглаженного уменьшения масштаба, точного синтеза фазы и улучшенной маски границ.
- Добавлено рабочее разрешение 448×256 с сохранением пропорций для режима ×2 и устранено нестабильное переключение между нейросетью и оптическим потоком.
- В синтетический тест нейросети добавлена проверка точности векторов движения и статичного фона, а также отдельный профиль качества ×2.
- Исправлено затирание локального поиска опенингов и эндингов при обновлении плеера и расширена диагностика локального анализа серий.

</details>

## [v4.9.4]

### 🇬🇧 English
- Accelerated neural motion smoothing several times with cached frame flow and faster graphics math.
- Sped up automatic Anime and Cinema style detection so video switches accurately within seconds.
- Fixed cancelled local intro and ending scans and started local analysis immediately with online lookup.
- Updated the synthetic neural graphics benchmark with flow, blend, and amortized frame timing metrics.

<details>
<summary><b>🇷🇺 На русском (Что нового в v4.9.4)</b></summary>

<br>

- В разы ускорена нейросетевая интерполяция кадров за счёт кэширования потока и оптимизации вычислений.
- Ускорено автоопределение стиля «Аниме» и «Кино» для точного переключения уже на первых секундах серии.
- Исправлен сбой отмены локального поиска опенингов и запущен его параллельный старт с онлайн-базой.
- Обновлён синтетический тест нейросети с замером потока, смешивания и усреднённого времени кадра.

</details>

