# Negai — Anime Video Player for Android

<p align="center">
  <img src="assets/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-3.6.6-00E5FF?style=flat-square" alt="Version 3.6.6" />
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
- **Anime4K Real-Time Upscaling (`bloc97/Anime4K` MIT)**: OpenGL ES 2.0 GLSL shader stages aligned with the official [bloc97/Anime4K](https://github.com/bloc97/Anime4K) architecture (**Mode C — Fast**, **Mode B — Soft CNN**, **Mode A — Restore CNN**) with `Clamp_Highlights` highlight pre-clamping, Difference of Gaussians (`DoG`), multi-orientation `CReLU` feature convolution, and live split-screen comparison.
- **Custom In-House Frame Interpolation (MEMC)**: Proprietary Negai OpenGL ES 2.0 shader implementation featuring 5-candidate diamond motion search (`MEMC_MOTION_ESTIMATE_FRAGMENT`), scene-cut suppression, and phase-consistent bidirectional frame warping (`ANIME`, `SMOOTH_60`, `MAX_FPS`, `CINEMA`) with perceptual luminance preservation in `.a` for simultaneous operation alongside Anime4K.
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

## Pre-Release 3.4.2-pre Summary

- **Display Cutout & Notch Handling in Landscape Mode**: Video canvas now fully occupies the notification bar and camera cutout areas (`LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES`), delivering true uninterrupted edge-to-edge playback in landscape.
- **Perfect Geometric Centering of Overlays**: Fixed asymmetric display-cutout padding that previously shifted the video buffering spinner (`CircularProgressIndicator`), seek notification pills (double-tap ±10s and horizontal swipe), and center transport controls off-center.
- **Fixed Scrub Bar Reset to 0 on Playback Stop/End**: Video playback state listeners now properly retain the last known valid position when entering `STATE_IDLE` or when playback ends (`STATE_ENDED`), showing full completion on the timeline rather than abruptly resetting to 00:00.
- **Vulkan Graphics Pipeline & Shader Architecture**:
  - Added Vulkan rendering pipeline abstraction (`VulkanPipelineEngine`, SPIR-V / Vulkan GLSL #version 450 core shaders for Anime4K and MEMC motion estimation).
  - Multi-threaded command pool recording (`VkCommandPool`, async compute queue family) and GPU video acceleration architecture with automatic fallback to OpenGL ES on devices lacking Vulkan Level 1.
  - Added Graphics Pipeline toggle in **Miscellaneous** (`Прочие`) settings with live device Vulkan capability detection.
- **Player Settings Reset with Confirmation**: Moved the reset action to **Miscellaneous** (`Прочие`) settings and wrapped it with an explicit confirmation `AlertDialog` to prevent accidental resets.
- **OLED & Dark Themes Overhaul**:
  - **OLED Theme**: Enhanced with pure `#000000` True Black surfaces and reduced container background tinting for maximum AMOLED power efficiency and crisp contrast.
  - **Dark Theme**: Transitioned to the rich deep obsidian-slate surface styling previously featured in the OLED theme.

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

## Licensing & Third-Party Components

- **Anime4K (MIT License)**: Real-time anime video upscaling shaders and line reconstruction algorithms are based on [bloc97/Anime4K](https://github.com/bloc97/Anime4K). Full license text and copyright notice: [`LICENSE_ANIME4K.txt`](LICENSE_ANIME4K.txt).
- **FFmpeg (GNU LGPL v2.1+)**: Software audio decoding (AC3, E-AC3, DTS, DTS-HD MA, Dolby TrueHD, FLAC, Opus) is provided through the Jellyfin Media3 extension dynamically loading shared `.so` libraries (`libffmpeg.so`, `libffmpegJNI.so`) without GPL extensions (`--enable-gpl` is omitted). Full compliance details and license text: [`LICENSE_FFMPEG.txt`](LICENSE_FFMPEG.txt).
- **Negai Application Source**: Open-source anime video player for Android.

---

## Repository & Obtainium Updates

- **Package ID**: `app.negai.player`
- **Version**: `3.6.6` (`versionCode = 21`)
- **Anime4K Upstream Attribution**: [https://github.com/bloc97/Anime4K](https://github.com/bloc97/Anime4K) (MIT License, Copyright © 2019–2021 bloc97)
- **FFmpeg Upstream**: [https://ffmpeg.org](https://ffmpeg.org) (LGPL v2.1+)
- **GitHub Repository**: [https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai)
