# Negai — Anime Video Player for Android

<p align="center">
  <img src="app/src/main/res/drawable/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-3.0_Release-00E5FF?style=flat-square" alt="Version 3.0 Release" />
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
- **Decoding Engine**: Media3 / ExoPlayer pipeline with selectable **HW+**, **HW**, and **SW** decoder modes supporting H.264, 10-bit HEVC (HDR10 / HLG / Dolby Vision profiles), VP9, and AV1.
- **Anime4K Real-Time Upscaling**: Custom GLSL shader stages (`Anime4K_Fast`, `Anime4K_Upscale_CNN`, `Anime4K_Upscale_CNNX`) with Sobel gradient line reconstruction and dark-contour preservation.
- **Frame Interpolation (MEMC)**: Phase-consistent bidirectional motion estimation and frame synthesis (`ANIME 2x`, `BALANCED`, `MAX_FPS`) with perceptual luminance preservation for simultaneous operation alongside Anime4K without frame flickering.
- **LTPO & High Refresh Rate Cadence**: Automatic display refresh rate matching (`24Hz`, `48Hz`, `60Hz`, `90Hz`, `120Hz`) with harmonic cadence locking.
- **Aspect-Aware Orientation**: Automatic landscape/portrait switching per video aspect ratio, manual 1-tap orientation lock preserved across episode transitions, and buffered video-size transitions that prevent premature screen rotation before an episode ends.

### 2. Multi-Track Audio, Subtitles & Chapters
- **Automatic Companion Track Binding**: Recursive scanner automatically pairs external `.mka`, `.aac`, `.flac`, `.mp3`, `.ogg` audio tracks and `.ass`, `.ssa`, `.srt`, `.vtt` subtitle files from nested voiceover/subtitle folders (`RUS/`, `Audio/`, `Dub/`, `Subs/`) by episode number.
- **Audio Synchronization & Bluetooth Compensation**:
  - Manual audio delay adjustment from **-5000 ms to +5000 ms** using non-destructive `MediaClock` offset for negative delays and PCM ring buffering for positive delays.
  - Automatic Bluetooth A2DP / LE Audio latency compensation with real-time Gaming Mode (~45 ms) detection.
  - Software DSP Audio Boost up to **200%** (+12 dB) with a soft-knee `tanh` limiter.
- **Subtitle Renderer**: Styled ASS/SSA/SRT/VTT rendering with customizable scale, vertical offset, background opacity, and ±30s timing shift.
- **Embedded Chapters**: Native Matroska (`MKV` EBML `ChapterAtom`) and `MP4` (`chpl`) chapter extraction with interactive timeline markers and chapter list navigation.

### 3. Library Management & TorrServer Streaming
- **Fast Binary Library Cache**: Instant cold start with background folder synchronization, natural episode sorting, resolution badges (`4K`, `1440p`, `1080p`, `720p`), and smart playback resume.
- **TorrServer P2P Integration**: Built-in torrent release search with voiceover/subtitle tag parsing, magnet/torrent handoff to local or remote TorrServer instances, and automatic multi-episode playlist assembly with external `.mka`/`.ass` track binding.

### 4. Adaptive Interface & Theme Architecture
- **Separated Base Theme Modes & Accent Palettes**:
  - **5 Base Theme Badges**: `Light`, `Dark` (`#0C111B`), `OLED` (`#000000` pure black), `Auto` (System Light/Dark), and `Auto OLED` (System Light/Pure Black).
  - **7 Accent Palettes**: `Monet` (Android 12+ dynamic colors), `Electric Cyan`, `Cyber Sakura`, `Deep Navy`, `Cyber Mint`, `Slate Indigo`, and `Sunset Amber`.
  - **Algorithmic WCAG 2.1 Auto-Contrast**: Dynamic relative-luminance contrast solver (`>= 4.5:1` / `7.0:1`) automatically adjusts foreground text and badge legibility across all theme and accent combinations.
- **20:9 Landscape & Tablet Ergonomics**: Scrollable compact navigation rail, wide multi-column layouts (`1200dp`), independent App UI and Player HUD scaling (`75%–140%`), and zero-jank permanently mounted player controls that keep Android system bars hidden when opening modal sheets.

---

## Release 3.0 Summary

- **Combined Anime4K + MEMC Pipeline**: Unified luma propagation (`synLuma` in synthesized frame alpha channel) eliminates alternating-frame brightness flickering when running Frame Interpolation and Anime4K simultaneously; tuned `Anime4K_Fast` contour preservation to sharpen edges without thinning dark line art.
- **Zero-Freeze Player HUD & Immersive Sheets**: Permanently mounted player HUD composition with GPU alpha transitions and immersive `ModalBottomSheet` window flags prevent frame drops and keep system bars hidden during playback.
- **Orientation & Exit Stability**: Buffered end-of-episode video size changes prevent premature screen rotation before episode transitions; synchronous orientation and inset restoration eliminates layout shifts when exiting the player via system back gestures.
- **Separated Theme & Accent System**: Split appearance settings into 5 base theme mode badges (`Light`, `Dark`, `OLED`, `Auto`, `Auto OLED`) and 7 accent palettes with algorithmic WCAG 2.1 text contrast.
- **20:9 Landscape Layout & Folder View Refinements**: Optimized landscape navigation and wide screen layouts outside the player; streamlined open-folder top bar and folder-only metadata inspection.
- **Bilingual Localization**: Centralized Russian (`RU`) and English (`EN`) localization across all screens, dialogs, and player sheets.

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
- **Version**: `3.0` (`versionCode = 7`)
- **GitHub Repository**: [https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai)
