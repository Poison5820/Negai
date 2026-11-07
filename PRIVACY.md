# Privacy Policy — Negai Media Player

**Effective Date:** November 7, 2026  
**Last Updated:** November 7, 2026  
**Application Name:** Negai Media Player (`negai`)  
**Public Privacy Policy URL:** [https://github.com/Poison5820/Negai/blob/main/PRIVACY.md](https://github.com/Poison5820/Negai/blob/main/PRIVACY.md)  
**Source & Issue Tracker:** [https://github.com/Poison5820/Negai/issues](https://github.com/Poison5820/Negai/issues)

---

## 1. Overview & Zero Data Collection Commitment

**Negai Media Player** ("Negai", "the Application", "we", "our") is a hardware-accelerated video and anime media player for Android featuring real-time Anime4K GLSL/Vulkan upscaling, MEMC frame interpolation, 3D HRTF binaural spatial audio, and SRT/ASS subtitle rendering.

**Negai does NOT collect, store, process, share, sell, or transmit any personal user data, personally identifiable information (PII), usage telemetry, analytics, crash reports, advertising identifiers, or media playback history to any external developer servers.**

All media indexing, video/audio decoding, shader rendering, and playback state persistence happen **100% locally on your Android device**. We do not operate any backend analytics servers, user accounts, or tracking SDKs.

---

## 2. Distribution Flavors & Monetization

Negai Media Player is built and distributed in two distinct, isolated application flavors from our codebase:

### 2.1. Google Play Edition (`com.negai.player`)
- **Application ID:** `com.negai.player`
- **Distribution Channel:** Official Google Play Store (`market` flavor).
- **In-App Purchases (Google Play Billing):** The Google Play version uses the official **Google Play Billing API** (`com.android.billingclient:billing-ktx`, one-time lifetime in-app product `negai_premium_lifetime`) to unlock optional **Negai Premium** capabilities (such as glassmorphic UI themes, complementary color palettes, and heavy neural/MEMC shader presets).
- **Payment Privacy:** All financial transactions, billing verification, and purchase restorations are processed directly and exclusively by **Google LLC** via Google Play Services. Negai never receives, accesses, processes, or stores your credit card numbers, billing address, Google account credentials, or personal payment details. Google's handling of billing information is governed by the [Google Privacy Policy](https://policies.google.com/privacy).
- **No External Payment Links:** All third-party donation or external support links are completely excluded from the `com.negai.player` binary at compile time.

### 2.2. GitHub Community Edition (`app.negai.player`)
- **Application ID:** `app.negai.player`
- **Distribution Channel:** Official Public GitHub Releases ([https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai), `github` flavor) and Obtainium.
- **No Billing SDKs:** The GitHub version has all Negai Premium features unlocked by default (`FEATURE_UNLOCKED = true`) and contains **zero** Google Play Billing libraries or proprietary store trackers.
- **External Support Link (Buy Me a Coffee / Community Support):** The GitHub edition includes an optional external community support link (such as **Buy Me a Coffee** or project sponsor pages). Tapping a support option simply opens the external URL in your device's default web browser via a standard Android `Intent.ACTION_VIEW`. No personal data is shared by the Application unless you voluntarily choose to interact with the external website in your browser.

---

## 3. Device Permissions & How They Are Used Locally

Negai requests only the minimum Android permissions strictly necessary to play your media files, apply audio/video enhancements, and stream user-requested content:

| Permission | Purpose & Local-Only Usage |
| :--- | :--- |
| **`READ_MEDIA_VIDEO`** & **`READ_MEDIA_AUDIO`** *(Android 13 / API 33+)* | Used exclusively on-device to discover and play local video files (`.mkv`, `.mp4`, `.m2ts`, `.avi`, `.webm`, `.mov`, `.ts`), automatically match external voiceover audio tracks (`.mka`, `.mp3`, `.flac`, `.ac3`, `.dts`, `.ogg`, `.aac`, `.opus`), and load external subtitle files (`.srt`, `.ass`, `.ssa`, `.vtt`). |
| **`READ_EXTERNAL_STORAGE`** *(Android 12 / API 32 and below)* & **`READ_MEDIA_VISUAL_USER_SELECTED`** | Used on older Android versions (or via Android Photo Picker / Storage Access Framework) to read local media folders, video episodes, external audio/subtitle files, and user-opened `.torrent` files stored on internal storage, SD cards, or USB OTG drives. Your filenames, folder structures, and media contents never leave your device. |
| **`INTERNET`** | Used strictly for **user-initiated** network actions: playing direct network streams (`http://`, `https://`, HLS `.m3u8`, DASH `.mpd`), connecting to a user-configured local or LAN **TorrServer** daemon (e.g., `http://127.0.0.1:8090`), and performing user-initiated P2P stream searches (including optional DNS-over-HTTPS resolution when enabled in Settings). Negai never transmits background telemetry or usage logs over the internet. |
| **`MODIFY_AUDIO_SETTINGS`** | Used locally by the audio engine to configure spatial audio routing (3D HRTF binaural convolution and XTC speaker crosstalk cancellation), detect headphone/Bluetooth audio routing changes, and apply hardware `LoudnessEnhancer` volume boost. |
| **`FOREGROUND_SERVICE`** & **`FOREGROUND_SERVICE_MEDIA_PLAYBACK`** | Used to keep media playback active in a standard Android `MediaSessionService` notification when using Picture-in-Picture (PiP) mode or when background audio playback is enabled by the user. |

---

## 4. Local Data Storage & User Control

All application data created during your use of Negai is stored exclusively inside the Application's private sandbox directory on your device:
- **Player Preferences (Jetpack DataStore):** Theme selection, UI scale, Anime4K / MEMC presets, default playback speed, subtitle styling, and TorrServer connection settings.
- **Playback History & Folder Cache (Room SQLite Database):** Last watched episode timestamps (for resuming playback), user-attached SAF folder URIs, hidden/excluded folder paths, and per-episode audio/subtitle track selections.

**How to Delete Your Local Data:**
- You can clear your playback history at any time inside the app via **Settings → Storage → Clear Playback History**.
- You can reset all player preferences to factory defaults via **Settings → Miscellaneous → Reset All Player Settings**.
- Uninstalling Negai or clearing storage via **Android Settings → Apps → Negai → Storage & Cache → Clear Storage** permanently deletes all local databases and preferences from your device.

---

## 5. Third-Party Services & User-Initiated Connections

- **TorrServer & P2P Streaming:** If you configure a TorrServer URL or open a magnet/`.torrent` link, your device connects directly to the TorrServer host or P2P indexers you specify. Negai acts solely as a client interface and does not proxy, log, or monitor your network traffic.
- **Open-Source Libraries:** Negai incorporates open-source components running strictly on-device, including **AndroidX Media3 / ExoPlayer** (Apache 2.0), **FFmpeg** (GNU LGPL v2.1+), **Anime4K Shaders** (MIT License), and **MIT KEMAR HRTF** (MIT License). None of these libraries collect or transmit user data.

---

## 6. Children's Privacy

Negai Media Player is a general-audience local media player tool and does not knowingly collect any personal information from anyone, including children under the age of 13 (or the applicable age of digital consent in your jurisdiction).

---

## 7. Changes to This Privacy Policy

If we update this Privacy Policy to reflect new features or regulatory requirements, we will publish the revised document in the root `PRIVACY.md` file of our public repository (`https://github.com/Poison5820/Negai/blob/main/PRIVACY.md`), updating the "Last Updated" date at the top.

---

## 8. Contact Us

If you have any questions, concerns, or requests regarding this Privacy Policy or Negai Media Player's privacy practices, please contact us via our official GitHub Issue Tracker:

- **GitHub Issues (Support & Privacy Inquiries):** [https://github.com/Poison5820/Negai/issues](https://github.com/Poison5820/Negai/issues)
- **Public Repository:** [https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai)

---
---

# Политика конфиденциальности — Медиаплеер Negai (На русском языке)

**Дата вступления в силу:** 7 ноября 2026 г.  
**Приложение:** Negai Media Player (`negai`)  
**Постоянная ссылка:** [https://github.com/Poison5820/Negai/blob/main/PRIVACY.md](https://github.com/Poison5820/Negai/blob/main/PRIVACY.md)  
**Обратная связь (GitHub Issues):** [https://github.com/Poison5820/Negai/issues](https://github.com/Poison5820/Negai/issues)

## 1. Отсутствие сбора персональных данных
**Negai Media Player** не собирает, не хранит на внешних серверах, не передаёт третьим лицам и не продаёт никакие персональные данные пользователей, аналитику, телеметрию, историю просмотров или рекламные идентификаторы. Вся обработка видео, аудио, субтитров и шейдеров (Anime4K, MEMC, 3D HRTF) выполняется исключительно локально на вашем устройстве.

## 2. Варианты распространения (Google Play и GitHub)
- **Версия для Google Play (`com.negai.player`):** Использует официальную систему **Google Play Billing** (разовая покупка `negai_premium_lifetime`) для разблокировки функций Negai Premium. Все платежи обрабатываются напрямую компанией Google LLC; приложение Negai не имеет доступа к вашим платёжным реквизитам. Внешние ссылки на сторонние сервисы донатов полностью вырезаны на этапе компиляции.
- **Версия для GitHub (`app.negai.player`):** Распространяется через публичный репозиторий `Poison5820/Negai`. Все функции разблокированы по умолчанию, библиотеки Google Play Billing отсутствуют. Содержит опциональную внешнюю ссылку поддержки разработчика (**Buy Me a Coffee** / страница поддержки сообщества), которая открывается в системном браузере только по явному нажатию пользователя.

## 3. Используемые разрешения Android
- **`READ_MEDIA_VIDEO`, `READ_MEDIA_AUDIO`, `READ_EXTERNAL_STORAGE`:** Используются исключительно для локального поиска и воспроизведения видеофайлов, внешних аудиодорожек (`.mka`, `.mp3`, `.flac`), субтитров (`.srt`, `.ass`) и открытия локальных `.torrent`-файлов на вашем устройстве.
- **`INTERNET`:** Используется только по инициативе пользователя для потокового воспроизведения по прямым ссылкам, связи с локальным или сетевым сервером **TorrServer** и поиска P2P-потоков.
- **`MODIFY_AUDIO_SETTINGS`:** Используется локально для работы пространственного аудио (3D HRTF / XTC), автосинхронизации Bluetooth и аппаратного усиления громкости (`LoudnessEnhancer`).

## 4. Контакты
По всем вопросам конфиденциальности обращайтесь через официальный трекер задач: [https://github.com/Poison5820/Negai/issues](https://github.com/Poison5820/Negai/issues).
