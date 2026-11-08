# Negai — Anime & Cinema 4K Video Player for Android

<p align="center">
  <img src="assets/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-4.2.13-00E5FF?style=flat-square" alt="Version 4.2.13" />
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

## What is Negai?

**Negai** is a modern, high-performance Android video player designed for anime enthusiasts and movie lovers. It delivers a fluid, distraction-free viewing experience with real-time AI line reconstruction, motion smoothing, natural HDR, optical glass aesthetics, and automated subtitle and audio track pairing.

Whether you are enjoying high-bitrate 4K Blu-ray releases on your couch or watching your favorite episodic series on the go, Negai is built to make every frame look and sound spectacular.

---

## Why You'll Love Negai

- 🎬 **Crisp Anime with Real-Time Anime4K**: Reconstructs soft line art and sharpens details on the fly using neural GLSL shaders.
- ⚡ **Motion Smoothing (MEMC)**: Interpolates 24 fps anime and cinema up to 60 fps or maximum screen refresh rate (90–165 Hz) for buttery-smooth motion.
- 🌊 **Fluid Glass & Floating Navigation Dock**: Elegant Liquid Glass and Frosted Glass interfaces with optical reflections, specular gleams, and a floating rounded navigation dock tailored for modern gesture control.
- 🌈 **Natural YouTube-Style HDR**: Hardware HDR presentation (HDR10, HLG, Dolby Vision) that preserves your device's natural screen brightness without blinding overrides.
- 🎧 **Spatial Audio & De-Artifact DSP**: Immersive 3D binaural headphone audio (MIT KEMAR HRTF) for 5.1/7.1 tracks, volume boost up to 200%, and voice clarity restoration for low-bitrate dubs.
- 📂 **Automatic Subtitle & Audio Pairing**: Automatically pairs external voiceovers (`.mka`, `.aac`, `.flac`) and styled subtitles (`.ass`, `.srt`) from your series folders.
- 📡 **Direct P2P Torrent Streaming**: Integrated TorrServer support for streaming anime torrents with multi-episode playlists without waiting for full downloads.
- 🚫 **Zero Ads & Privacy-First**: Completely ad-free, tracker-free, and respectful of your device resources.

---

## 📥 Downloads & Installation

You can install and update Negai directly through **Obtainium** or grab the universal APK:

- **Universal APK**: [`negai.apk`](https://github.com/Poison5820/Negai/releases/latest/download/negai.apk)
- **Supported Architectures**: `arm64-v8a`, `armeabi-v7a`, `x86_64`, `x86`
- **Minimum Requirement**: Android 8.0+ (API level 26 or higher)
- **Automatic Updates via Obtainium**: Click the badge above or add `https://github.com/Poison5820/Negai` in [Obtainium](https://github.com/ImranR98/Obtainium).

---

## ✨ What's New in Release 4.2.13 (Build 60)

- **10-Component Atomic Design System**: Consolidated the entire UI across Library, Folders, Episodes, Recent, Torrents, Settings, and Player sheets onto 10 canonical components (`UnifiedSurfaceCard`, `UnifiedSelectionRow`, `UnifiedFloatingBar`, `UnifiedModalHeader/Footer`, `UnifiedActionButton`, `UnifiedIconButton`, `UnifiedFilterChip`, `UnifiedBadgeChip`, `UnifiedInputBar`, `UnifiedDropdownMenu`).
- **Zero 90° Corner Bleed & Smooth 135° Glass Bevels**: Eliminated white and gray rectangular halos protruding behind rounded card and chip corners via hardware shape clipping and contour-accurate outline rendering; unified all glass cards, buttons, and chips with the exact same smooth 135° specular bevel as the floating navigation dock.
- **Unified Modal Headers & "Done" Footers**: Standardized all dialogs, bottom sheets, and player control sheets onto `UnifiedModalHeader` and `UnifiedModalFooter` for consistent typography, icon badges, and confirmation buttons.
- **Pure Borderless M3 Flat Theme**: Guaranteed 0% borders on cards, bars, modals, rows, and chips in the Flat theme, keeping a 1dp `outlineVariant` stroke exclusively on `OUTLINED` buttons and text input bars.

---

<details>
<summary><b>🔬 Technical Specifications & Architecture (Detailed)</b></summary>

### Video & Codec Engine
- **Hardware Decoding**: Hardware-accelerated H.264, 10-bit HEVC (Main10), VP9, and AV1 via AndroidX Media3 / ExoPlayer with zero-copy `SurfaceView` routing.
- **Blu-ray BDAV (.m2ts)**: Dedicated 192-byte packet demuxer with DTS-HD Master Audio 7.1 and Dolby TrueHD + Atmos decoding.
- **Display Cadence**: Harmonic LTPO refresh rate matching (`24Hz`, `48Hz`, `60Hz`, `90Hz`, `120Hz`, `144Hz`, `165Hz`) with pause power-saving mode.

### Audio DSP Pipeline
- **MIT KEMAR HRTF**: Real-time 5.1/7.1 binaural spatial audio convolution with interaural time delay (ITD) filtering.
- **De-Artifact Filter**: 3-stage PCM reconstruction to eliminate compression ringing and harshness from compressed voiceovers.
- **Bluetooth Sync**: Latency offset ranging from -5000 ms to +5000 ms with gaming mode detection.

### Architecture Overview
```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                        │
│   LibraryScreen • FolderDetailScreen • PlayerScreen • FloatingDock     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                             NegaiViewModel                             │
│       Exposes StateFlow<PlaybackRuntimeState> & PlayerSettings         │
└───────────────────┬────────────────────────────────┬───────────────────┘
                    │                                │
┌───────────────────▼───────────┐        ┌───────────▼───────────────────┐
│     PlayerEngine (Media3)     │        │ PlayerPreferencesRepository   │
│ ExoPlayer • OpenGL ES Shaders │        │ DataStore • Fast Binary Cache │
└───────────────────────────────┘        └───────────────────────────────┘
```
</details>

---

## 📄 Licensing & Open-Source Attributions

- **Negai Player**: Proprietary, closed-source software. Copyright © 2026 Negai Player. All Rights Reserved. This repository serves exclusively as the official binary release showcase and third-party license notice.
- **Anime4K (MIT License)**: Real-time anime video upscaling shaders based on [bloc97/Anime4K](https://github.com/bloc97/Anime4K) (Copyright © 2019–2021 bloc97). See [`LICENSE_ANIME4K.txt`](LICENSE_ANIME4K.txt).
- **MIT KEMAR HRTF (MIT License)**: Binaural spatial audio impulse response filters derived from the [MIT Media Lab KEMAR HRTF Dataset](https://sound.media.mit.edu/resources/KEMAR.html) (Bill Gardner & Keith Martin). See [`LICENSE_MIT_KEMAR_HRTF.txt`](LICENSE_MIT_KEMAR_HRTF.txt).
- **FFmpeg (GNU LGPL v2.1+)**: Software audio decoders provided through Media3 extension without GPL components. See [`LICENSE_FFMPEG.txt`](LICENSE_FFMPEG.txt).
- **AndroidX Media3 (Apache 2.0)**: Core playback pipeline developed by Google LLC.
