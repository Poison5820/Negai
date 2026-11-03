# Negai — Anime Video Player for Android

<p align="center">
  <img src="app/src/main/res/drawable/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-3.2-00E5FF?style=flat-square" alt="Version 3.2" />
  <img src="https://img.shields.io/badge/package-app.negai.player-00B8D4?style=flat-square" alt="Package ID" />
  <img src="https://img.shields.io/badge/platform-Android_8.0%2B_(API_26%2B)-3DDC84?style=flat-square&logo=android" alt="Platform" />
  <img src="https://img.shields.io/badge/Kotlin-2.x_%7C_Jetpack_Compose-7F52FF?style=flat-square&logo=kotlin" alt="Kotlin & Compose" />
</p>

<p align="center">
  <a href="https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/Poison5820/Negai">
    <img src="https://raw.githubusercontent.com/ImranR98/Obtainium/main/assets/graphics/badge_obtainium.png" alt="Get it on Obtainium" height="64" />
  </a>
</p>

---

## Overview

**Negai** (`app.negai.player`) is a native Android video player engineered specifically for anime series, episodic libraries, and multi-track releases. It combines hardware-accelerated decoding, real-time OpenGL ES shaders for line reconstruction and motion interpolation, automatic companion track binding, and direct P2P torrent streaming.

---

## Key Capabilities

### 1. Video Pipeline & OpenGL ES Shaders
- **Decoding Engine**: Media3 / ExoPlayer pipeline with selectable **HW+**, **HW**, and **SW** decoder modes supporting H.264, 10-bit HEVC (HDR10 / HLG / Dolby Vision profiles), VP9, and AV1, plus a built-in **Hardware & MediaCodec Inspector** in Settings.
- **Anime4K Real-Time Upscaling**: Custom GLSL shader stages (`Anime4K_Fast`, `Anime4K_Upscale_CNN`, `Anime4K_Upscale_CNNX`) with Sobel gradient line reconstruction, dark-contour preservation, live split-screen comparison, and automatic `Anime4K_Fast` load-balancing when paired with MEMC.
- **Frame Interpolation (MEMC)**: Phase-consistent bidirectional motion estimation and frame synthesis (`ANIME 2x`, `BALANCED`, `MAX_FPS`) with perceptual luminance preservation for simultaneous operation alongside Anime4K without frame flickering.
- **LTPO & High Refresh Rate Cadence**: Automatic display refresh rate matching (`24Hz`, `48Hz`, `60Hz`, `90Hz`, `120Hz`, `144Hz`, `165Hz`) with harmonic cadence locking.
- **Aspect-Aware Orientation**: Automatic landscape/portrait switching per video aspect ratio, manual 1-tap orientation lock preserved across episode transitions, and buffered video-size transitions that prevent premature screen rotation before an episode ends.

### 2. Multi-Track Audio, De-Artifact DSP, Subtitles & Chapters
- **Automatic Companion Track Binding**: Recursive scanner automatically pairs external `.mka`, `.aac`, `.flac`, `.mp3`, `.ogg` audio tracks and `.ass`, `.ssa`, `.srt`, `.vtt` subtitle files from nested voiceover/subtitle folders (`RUS/`, `Audio/`, `Dub/`, `Subs/`) by episode number.
- **Low-Bitrate Audio De-Artifact DSP**: Real-time 3-stage PCM restoration pipeline (`Soft`, `Standard`, `Aggressive`, `Off` + `Auto ≤ 128 kbps`) featuring dynamic high-frequency de-ringing, warm harmonic voice presence restoration, and cubic soft-knee de-clipping for compressed AAC/MP3/Opus voiceover tracks.
- **Audio Synchronization & Bluetooth Compensation**:
  - Manual audio delay adjustment from **-5000 ms to +5000 ms** using non-destructive `MediaClock` offset for negative delays and PCM ring buffering for positive delays.
  - Automatic Bluetooth A2DP / LE Audio latency compensation with real-time Gaming Mode (~45 ms) detection and manual presets (`-45 ms`, `-60 ms`, `-150 ms`).
  - Software DSP Audio Boost up to **200%** (+12 dB) with a soft-knee `tanh` limiter.
- **Subtitle Renderer**: Styled ASS/SSA/SRT/VTT rendering with customizable scale, vertical offset, background opacity, and ±30s timing shift.
- **Embedded Chapters**: Native Matroska (`MKV` EBML `ChapterAtom`) and `MP4` (`chpl`) chapter extraction with interactive timeline markers and dedicated Episodes / Chapters navigation.

### 3. Library Management & TorrServer Streaming
- **Fast Binary Library Cache & Smart Resume**: Instant cold start with background folder synchronization, natural episode sorting, resolution badges (`4K`, `1440p`, `1080p`, `720p`), and smart playback resume that automatically restarts completed episodes (`>= 95%` watched or `<= 15s` remaining) from `00:00` without auto-skipping to the next episode.
- **TorrServer P2P Integration**: Built-in torrent release search with separated voiceover (`RUS/Dub/MVO/JacRed`) and subtitle tag parsing, magnet/torrent handoff to local or remote TorrServer instances, and automatic multi-episode playlist assembly with external `.mka`/`.ass` track binding.

### 4. Adaptive Interface & Theme Architecture
- **Separated Base Theme Modes & Accent Palettes**:
  - **5 Base Theme Badges**: `Light`, `Dark` (`#0C111B`), `OLED` (`#000000` pure black), `Auto` (System Light/Dark), and `Auto OLED` (System Light/Pure Black).
  - **7 Accent Palettes**: `Monet` (Android 12+ dynamic colors), `Electric Cyan`, `Cyber Sakura`, `Deep Navy`, `Cyber Mint`, `Slate Indigo`, and `Sunset Amber`.
  - **Algorithmic WCAG 2.1 Auto-Contrast**: Dynamic relative-luminance contrast solver (`>= 4.5:1` / `7.0:1`) automatically adjusts foreground text and badge legibility across all theme and accent combinations.
- **Frosted Glass HUD & 120Hz Ergonomics**: Unified translucent Frosted Glass styling across both top-right player controls and the bottom quick-actions bar, non-blocking disk-cached episode thumbnails, scrollable compact navigation rail, wide multi-column layouts (`1200dp`), and independent App UI and Player HUD scaling (`75%–140%`).

---

## Release 3.2 Summary

- **Smart Restart of Completed Episodes (95% Threshold)**: Fixed an issue where completed or near-completed episodes (`>= 95%` watched or `<= 15s` left) could retain an end-of-file timestamp and immediately skip to the next episode upon reopening. Completed episodes now reset to `00:00` on finish and reopen cleanly from the start while keeping their watched badge in the folder list.
- **120Hz List Scrolling Optimization**: Removed synchronous UI-thread file checks during thumbnail request building, unified `RecentTabScreen` and `FolderDetailScreen` onto the shared disk-backed `VideoThumbnailCache`, added stable keys in `TorrentsTabScreen`, lazily composed context menus, and memoized card shapes/borders/brushes across all screens.
- **Frosted Glass Top-Right Player HUD**: Redesigned the top-right player buttons (Episodes/Chapters, Orientation Lock, Screen Touch Lock, and Picture-in-Picture) with the same translucent Frosted Glass surface (`#88101828`), subtle border, and top inner highlight sheen as the bottom `PlayerQuickActionsBar`.
- **Updated Planetary Easter Egg & Synchronized Translations**: Updated the interactive planetary model (`VersionPlanetaryModalSheet`) with current v3.2 orbital specifications (`Auto-Fast with MEMC`, `165Hz`, Bluetooth Gaming presets, voiceover parser) and unified RU/EN terminology across Settings, Player sheets, and dialogs.
- **Hardware Inspector & Torrent Voiceover Separation**: Added device display/HDR/MediaCodec hardware inspection in Settings and dedicated voiceover vs. subtitle parsing on torrent release cards.

---

## Architecture

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                        │
│   LibraryScreen • FolderDetailScreen • PlayerScreen • SettingsPages    │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                             NegaiViewModel                             │
│       Exposes StateFlow<PlaybackRuntimeState> & PlayerSettings         │
└───────────────────┬────────────────────────────────┬───────────────────┘
                    │                                │
┌───────────────────▼───────────┐        ┌───────────▼───────────────────┐
│     PlayerEngine (Media3)     │        │ PlayerPreferencesRepository   │
│ ExoPlayer • OpenGL ES Shaders │        │ DataStore • Boot Snapshot     │
└───────────────────────────────┘        └───────────────────────────────┘
```

---

## Build & Verification

### Requirements
- Android SDK 35 / Build Tools 35.0.0
- JDK 17+
- Gradle 8.x

### Commands
```bash
# Build Debug APK
gradle assembleDebug

# Build Release APK
gradle assembleRelease

# Run Unit & Robolectric Tests
gradle :app:testDebugUnitTest
```

---

## Repository & Obtainium Updates

- **Package ID**: `app.negai.player`
- **Version**: `3.2` (`versionCode = 10`)
- **GitHub Repository**: [https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai)
