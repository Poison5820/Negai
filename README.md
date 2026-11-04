# Negai — Anime Video Player for Android

<p align="center">
  <img src="assets/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-4.0.2-00E5FF?style=flat-square" alt="Version 4.0.2" />
  <img src="https://img.shields.io/badge/package-app.negai.player-00B8D4?style=flat-square" alt="Package ID" />
  <img src="https://img.shields.io/badge/platform-Android_8.0%2B_(API_26%2B)-3DDC84?style=flat-square&logo=android" alt="Platform" />
  <img src="https://img.shields.io/badge/license-Proprietary_(Closed_Source)-FF4D8D?style=flat-square" alt="Proprietary License" />
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

## What's New in Release 4.0.2 (Build 24)

- **Top-Bar Negai Premium Star Button & Unified Premium Branding**:
  - Compact golden star button in the top bar right beside the palette icon opening the `Negai Premium` levitating purchase dialog (Google Play Billing); unified rebranding from `PRO` to `Premium`; smaller right-aligned star indicators on paid palettes and dialog styles that automatically disappear once Premium is unlocked.
- **Blu-ray BDAV (`.m2ts`) Demuxer & DTS-HD Master Audio 7.1 (`0x86`) Playback**:
  - Dedicated 192-byte Blu-ray BDAV packet extractor (`BdavM2tsExtractor`) and ETSI TS 102 114 parser (`HdmvDtsHdReader`) coalescing `[DTS Core + DTS-HD XLL Extension]` into unified 8-channel access units for FFmpeg (`libdcadec`), eliminating `Source Error` on `.m2ts` Blu-ray sound-check streams.
  - Instant `STATE_IDLE` recovery across play/pause, seek, episode switching, and interactive **Retry** / **SW Decoder (FFmpeg)** buttons on the error banner so playback recovers in-place without exiting the player.
- **3D IMAX Playback, Right-Pinned Skip Intro & Full-Width Camera Cutout HUD**:
  - Fixed Full-SBS (`3840×1080`) and Full-TAB (`1920×2160`) 3D IMAX aspect ratio calculation and prevented Full-TAB 3D frames from triggering portrait screen rotation.
  - Fixed the Skip Intro (`+85s Intro`) button alignment so it remains strictly pinned to the right edge even when embedded chapter pills are active.
  - Extended the Player Top Bar and Bottom Controls across the full width of the screen including the camera cutout zone (`LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES`).
- **Strict External Audio & Subtitle Auto-Matching**:
  - External audio and subtitle tracks now require strict base-filename stem matching (exact stem or prefix followed by a delimiter `.`, `_`, `-`, `[`, `(`, or space), preventing unrelated music albums (e.g., `.flac` tracks) in the same folder from auto-binding to videos.
- **Built-in 3D HRTF Binaural Spatial Audio (MIT KEMAR)**:
  - Real-time 5.1 / 7.1 surround-to-binaural headphone convolution engine (`HrtfBinauralAudioProcessor`) using Interaural Time Difference (ITD) delay lines and minimum-phase KEMAR pinna FIR filters derived from the **MIT Media Lab KEMAR HRTF dataset** (Bill Gardner & Keith Martin, MIT License).
- **Accurate Hardware / Software Decoder Fallback Indicator**:
  - When a hardware decoder is unavailable or fails and playback falls back to software (FFmpeg / CPU), the active decoder state automatically updates and highlights the `SW` badge in the Top Bar, Quick Actions Bar, Audio sheet, and Video Decoder sheet.
- **Levitating Multi-Profile Dialogs Outside the Player**:
  - Replaced flat `AlertDialog` windows outside the player with `FloatingElevatedDialog` featuring spring-animated elevation and selectable profiles (`Standard Floating`, `Glassmorphic Blur PRO`, `Cyber Neon Glow PRO`).
- **Live Wallpaper Monet Icon, Dual-Tone Split Swatches & PRO Complementary Palettes**:
  - The `Monet` palette icon now dynamically displays the actual Primary & Secondary colors extracted from your live Android 12+ wallpaper.
  - All accent swatches now use a clear **Primary (left) + Secondary (right) split badge** instead of ambiguous concentric circles.
  - Added 4 high-contrast **Complementary Color Palettes** (`Sunset Horizon`, `Cyber Neon`, `Tokyo Sakura Pro`, `Midnight Amethyst`) in a dedicated **PRO** section.

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

## Licensing & Third-Party Open-Source Attributions

- **Negai Player — Proprietary Software (All Rights Reserved)**:
  - **Negai Player (`app.negai.player`) is proprietary, closed-source software. Copyright © 2026 Negai Player. All Rights Reserved.** This public repository serves exclusively as the official release distribution showcase and third-party license compliance notice.
- **Anime4K (MIT License)**: Real-time anime video upscaling shaders and line reconstruction algorithms are based on [bloc97/Anime4K](https://github.com/bloc97/Anime4K) (Copyright © 2019–2021 bloc97). Full license text: [`LICENSE_ANIME4K.txt`](LICENSE_ANIME4K.txt).
- **MIT KEMAR Binaural HRTF Dataset (MIT License)**: 3D HRTF binaural spatial audio convolution filters for 5.1 / 7.1 surround sound are derived from the [MIT Media Lab KEMAR Dummy-Head HRTF Dataset](https://sound.media.mit.edu/resources/KEMAR.html) by **Bill Gardner and Keith Martin** (Copyright © 1994 MIT Media Laboratory). Full license text: [`LICENSE_MIT_KEMAR_HRTF.txt`](LICENSE_MIT_KEMAR_HRTF.txt).
- **FFmpeg (GNU LGPL v2.1+)**: Software audio decoding (AC3, E-AC3, DTS, DTS-HD MA, Dolby TrueHD, FLAC, Opus) is provided through the Jellyfin Media3 extension dynamically loading shared `.so` libraries (`libffmpeg.so`, `libffmpegJNI.so`) without GPL extensions (`--enable-gpl` is omitted). Upstream project: [https://ffmpeg.org](https://ffmpeg.org). Full compliance details and license text: [`LICENSE_FFMPEG.txt`](LICENSE_FFMPEG.txt).
- **AndroidX Media3 / ExoPlayer (Apache License 2.0)**: Core media playback pipeline developed by Google LLC ([https://developer.android.com/media/media3](https://developer.android.com/media/media3)), licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

---

## Repository & Obtainium Updates

- **Package ID**: `app.negai.player`
- **Version**: `4.0.2` (`versionCode = 24`)
- **Software License**: Proprietary (Closed-Source, All Rights Reserved)
- **Anime4K Upstream Attribution**: [https://github.com/bloc97/Anime4K](https://github.com/bloc97/Anime4K) (MIT License, Copyright © 2019–2021 bloc97)
- **MIT KEMAR HRTF Upstream Attribution**: [https://sound.media.mit.edu/resources/KEMAR.html](https://sound.media.mit.edu/resources/KEMAR.html) (MIT License, Bill Gardner & Keith Martin, MIT Media Lab)
- **FFmpeg Upstream**: [https://ffmpeg.org](https://ffmpeg.org) (GNU LGPL v2.1+)
- **GitHub Release Showcase**: [https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai)
