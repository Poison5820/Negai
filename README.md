# Negai — Anime & Cinema 4K Video Player for Android

<p align="center">
  <img src="assets/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-4.7.25-FF80AB?style=flat-square" alt="Version 4.7.25" />
  <img src="https://img.shields.io/badge/package-app.negai.player-00B8D4?style=flat-square" alt="Package ID" />
  <img src="https://img.shields.io/badge/platform-Android_8.0%2B_(API_26%2B)-3DDC84?style=flat-square&logo=android" alt="Platform" />
  <img src="https://img.shields.io/badge/GPU-Vulkan_1.1_%E2%80%A2_GLES_3.1_FP16-7C4DFF?style=flat-square" alt="GPU Compute" />
  <img src="https://img.shields.io/badge/license-Proprietary_(Closed_Source)-FF4D8D?style=flat-square" alt="Proprietary License" />
</p>

<p align="center">
  <a href="https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/Poison5820/Negai">
    <img src="https://raw.githubusercontent.com/ImranR98/Obtainium/main/assets/graphics/badge_obtainium.png" alt="Get it on Obtainium" height="64" />
  </a>
</p>

---

## Overview

**Negai** is a modern, flagship-grade Android 4K video player engineered for anime enthusiasts and cinema purists. It combines a native **Vulkan 1.1 (`SPIR-V`)** and **OpenGL ES 3.1 (`FP16`)** compute video pipeline, **Anime4K** + **AMD FidelityFX (`FSR 1.0` & `Optical Flow MEMC`)**, mobile **Adaptive Sync (`VRR / LFC`)**, natural **4K HDR10 / Dolby Vision**, zero-allocation **MIT KEMAR HRTF** spatial audio, refractive **Liquid & Frosted Glass** interfaces, **120+ world languages** for automatic track pairing, and direct **TorrServer P2P** streaming — with zero ads and zero tracking.

---

## 📥 Downloads & Installation

Install and update Negai automatically via **Obtainium** or download the universal APK:

- **Universal APK (`Community Edition`)**: [`negai.apk`](https://github.com/Poison5820/Negai/releases/latest/download/negai.apk)
- **Supported CPU ABIs**: `arm64-v8a`, `armeabi-v7a`, `x86_64`, `x86` (16 KB memory-page aligned)
- **Minimum OS**: Android 8.0+ (API 26+; Android 10+ API 29 required for native Vulkan 1.1 `AHardwareBuffer` zero-copy path)
- **Automatic Updates via Obtainium**: Tap the badge above or add `https://github.com/Poison5820/Negai` in [Obtainium](https://github.com/ImranR98/Obtainium).
- **Release Notes & Privacy**: [GitHub Releases](https://github.com/Poison5820/Negai/releases) · [`PRIVACY.md`](PRIVACY.md)

---

## ✨ What's New in Release 4.7.25

- Restored library thumbnails for dark scenes, high-contrast logos, and heavy surround disc streams.
- Improved preview decoding on Blu-ray transport streams with non-standard keyframes and hardware fallback.
- Prevented automatic opening skip from disappearing on later episodes due to incomplete pre-calibration.
- Guaranteed smart skip diagnostic export always records analyzed episodes even after quick switches.

---

## Core Engineering Pillars

### 1. Dual-Backend GPU Compute Video Engine (`Vulkan 1.1` & `OpenGL ES 3.1 FP16`)
- **Native Vulkan 1.1 (`NDK 27 / C++20 / SPIR-V`) & OpenGL ES 3.1 (`FP16`)**: Select `Auto`, `Vulkan 1.1`, or `OpenGL ES 3.1` live during playback with automatic zero-crash driver fallback. Hardware-decoded SDR and 3D frames enter Vulkan directly via zero-copy `AImageReader` / `AHardwareBuffer` (`VkSamplerYcbcrConversion`) and pace presentation via `VK_GOOGLE_display_timing`, while 2D 10-bit HDR routes directly to **Hardware Composer (`HWC`)**.
- **Dual-Engine Spatial Upscaling (`Auto / Anime4K v4 / AMD FSR 1.0`)**:
  - **Anime4K (`Fast / HQ / Ultra`)**: Preserves 100% natural ink-line thickness via symmetric S-curve edge steepening, 4-tap Catmull-Rom tangent anti-aliasing, highlight clamping, and triangular dithering.
  - **AMD FidelityFX Super Resolution 1.0 (`EASU + RCAS`)**: 12-tap elliptical Lanczos2 upsampling + contrast-adaptive sharpening tailored for live-action cinema, TV series, and 3D CGI.
  - **Asynchronous GPU Style Probe (`Auto`)**: Classifies cel-shaded animation vs live-action gradients on the GPU every 24 frames with zero pipeline stalls (`Auto → Anime4K / AMD FSR`).
- **AMD FidelityFX Optical Flow MEMC (`×2 / 60 FPS / Max Hz`)**: 3-level hierarchical 16-tap Ternary Census optical flow (`48/60 fps` cinema doubling, `60 FPS`, or `Max Hz` `90–165 Hz`) with bilateral contour propagation for flat 2D anime fills, interactive **Split Compare** slider, and **0ms instant live & paused redraws** without seeks or surface reloads.

### 2. Adaptive Display Sync (`VRR / LFC`), 4K HDR/DV & Transport Formats
- **Mobile Adaptive Sync (`VRR / LFC`) & Hybrid `VFR` Tracking**: Classifies your screen panel (`LTPO ARR`, `Multi-Hz Seamless VRR`, `Multi-Hz LFC`, or `Fixed 60 Hz`), locks onto peak harmonic refresh containers (such as `120 Hz` `5×` for `23.976 fps`) to eliminate `3:2 pulldown` judder and OLED mode-switch gamma flicker, and tracks hybrid anime `VFR` transitions (`23.976 ↔ 59.94 ↔ 29.97 fps`) in real time.
- **Natural YouTube-Style 4K HDR & Dolby Vision**: Hardware-accelerated 10-bit `HDR10`, `HDR10+`, `HLG`, and **Dolby Vision** (including zero-allocation Profile 7 Enhancement Layer NAL sanitization) using native `COLOR_MODE_HDR` without blinding system brightness overrides.
- **Broad Codec, Blu-ray BDAV & 3D Support**: Hardware-first `H.264`, `HEVC Main10`, `VP9`, and `AV1` (`Auto / HW+ / SW`), dedicated 192-byte **Blu-ray BDAV (`.m2ts` / `.ts`)** transport demuxer with PGS bitmap subtitles, and **3D Side-by-Side (`SBS`) / Top-and-Bottom (`TAB`)** playback.

### 3. Zero-Allocation Spatial Audio & Voice Restoration DSP
- **3D Spatial Audio (`MIT KEMAR HRTF` & `XTC`)**: Real-time 5.1/7.1 binaural headphone convolution (`MIT Media Lab KEMAR HRTF`) with interaural time delay and stereo speaker **Crosstalk Cancellation (`XTC`)**.
- **Voice De-Artifact & Bandwidth Extension (`BWE`) DSP**: 4-mode zero-allocation PCM voice restoration (`Off`, `Soft`, `Standard`, `BWE Upscale`) with automatic activation on low-bitrate lossy voiceovers while bypassing lossless codecs.
- **300% Volume Boost, Lossless Surround & Bluetooth Sync**: Clipping-guarded volume amplification up to **300%**, **FFmpeg** multichannel decoding (`DTS`, `DTS-HD Master Audio 7.1`, `Dolby TrueHD + Atmos`, `EAC3`, `FLAC`, `Opus`), and **Bluetooth Audio Delay** compensation (`-5000 ms` to `+5000 ms`).

### 4. Multi-Engine Glass UI, Smart Playback, 120+ Languages & P2P Streaming
- **Three Optical UI Engines & Palettes**: Choose **Flat** (Material 3 Expressive), **Frosted Glass** (matte blur + micro-crystal grain), or **Liquid Glass** (physical lens refraction + `135°` specular bevel) across **Light**, warm charcoal **Dark (`#131417`)**, and true **OLED (`#000000`)** modes, paired with 4 player HUD themes (`Classic OLED`, `Glass Capsule`, `Cyber Neon`, `Pure Icons`) and elastic **Material 3 Expressive Pull-to-Refresh**.
- **Ambient Video Glow, 120 Hz Seek Preview & 3-Tier Smart Skip**: Outer-rim **2D Spatial Ambient Video Glow** on letterbox/pillarbox bars, instant touch-down **Timeline Seek Preview** (`4:3`, `16:9`, `21:9`, `9:16`), and **Smart Intro / Recap / Credits Skip** (embedded chapters + optional privacy-first text-only online timestamps + manual fallback with 4-second Undo and post-credits protection).
- **120+ ISO 639 World Languages & Smart Track Pairing**: Automatic pairing of external voiceovers (`.mka`, `.flac`, `.aac`, `.ac3`, `.dts`) and styled subtitles (`.ass`, `.ssa`, `.srt`, `.vtt`) across **120+ world and regional languages**, with intelligent `Signs & Songs / Forced / Full / SDH` subtitle selection (`Off`, `Signs Only`, `Prefer Signs`, `Full`).
- **Integrated TorrServer P2P Streaming**: Stream multi-episode magnet and `.torrent` releases directly via local or LAN **TorrServer** with universal **Continue Watching**, interactive source categories, recognized `DUB` / `SUB` badges, and persistent **Track Presence & Follow-Settings Language Filters**.

### 5. Live Telemetry & Built-In 15-Stage Benchmark
- **Live `Stream HUD` Overlay**: Real-time inspection of active `Vulkan 1.1` / `GLES 3.1` compute mode, decoded vs presented output FPS, live display refresh rate (`Hz`) and `VRR / LFC` multiplier, internal shader resolution, hardware GPU pass timing (`ms`), and live network download speed + buffer duration.
- **15-Stage Hardware Video & UI Benchmark**: Continuous-motion pipeline stress testing across decoders, `Anime4K`, `AMD FSR 1.0`, `FidelityFX MEMC`, `7.1 HRTF + BWE DSP`, and `Liquid / Frosted Glass` UI arenas with bottleneck verdicts and JSON/Markdown report export.

---

## 📦 Editions (`GitHub Community` vs `Google Play Store`)

| Feature | Community Edition (`app.negai.player`) | Store Edition (`com.negai.player`) |
| :--- | :--- | :--- |
| **Distribution** | GitHub Releases & Obtainium | Google Play Store |
| **Core 4K HDR / Vulkan 1.1 / GLES 3.1 / `HRTF` Player** | Included | Included |
| **120+ World Languages & External Track Pairing** | Included | Included |
| **Negai Premium (`HQ/Ultra` Upscale, `60/Max Hz` MEMC, `BWE` Audio, Glass Themes & Dual Palettes)** | Unlocked out of the box | Optional one-time **Negai Premium** unlock via Google Play Billing |
| **Built-in P2P Indexer Search Catalog** | Included (configurable + custom APIs) | Omitted for Play Store policy compliance (user-configured endpoints only) |
| **Ads & User Tracking** | None | None |

---

<details>
<summary><b>🔬 Architecture Overview</b></summary>

```
┌────────────────────────────────────────────────────────────────────────────┐
│                         Jetpack Compose UI Layer                           │
│    Flat Expressive • Frosted Glass • Liquid Glass (120+ Hz Adaptive UI)    │
│    Library • Folder Detail • P2P Filter Bar • Player HUD & Tabbed Sheets   │
└─────────────────────────────────────┬──────────────────────────────────────┘
                                      │
┌─────────────────────────────────────▼──────────────────────────────────────┐
│                         NegaiViewModel & Controllers                       │
│   PlaybackRuntimeState • PlayerSettings • TorrentsController • SmartSkip   │
└───────────────────┬───────────────────────────────────┬────────────────────┘
                    │                                   │
┌───────────────────▼────────────────────┐  ┌───────────▼────────────────────┐
│     Media3 / ExoPlayer + GPU/DSP       │  │   Persistence & Network Layer  │
│ • Vulkan 1.1 NDK / SPIR-V (Zero-Copy)  │  │ • DataStore + Fast Boot Cache  │
│ • OpenGL ES 3.1 FP16 / Direct HWC HDR  │  │ • Room History & Resume Store  │
│ • Anime4K v4 • AMD FSR 1.0 • FFX MEMC  │  │ • 120+ Lang Matcher & Subtitles│
│ • BDAV .m2ts • FFmpeg • KEMAR HRTF DSP │  │ • TorrServer & DoH Client      │
└────────────────────────────────────────┘  └────────────────────────────────┘
```
</details>

---

## 📄 Licensing, Privacy & Open-Source Attributions

- **Negai Player**: Proprietary, closed-source software. Copyright © 2026 Negai Player. All Rights Reserved. This public repository serves as the official release distribution hub, issue tracker, and open-source attribution notice. Read our [`PRIVACY.md`](PRIVACY.md).
- **Anime4K (MIT License)**: Real-time anime line reconstruction shaders based on [bloc97/Anime4K](https://github.com/bloc97/Anime4K) (Copyright © 2019–2021 bloc97). See [`LICENSE_ANIME4K.txt`](LICENSE_ANIME4K.txt).
- **AMD FidelityFX Optical Flow & Super Resolution 1.0 (MIT License)**: Hierarchical 32-bit Census optical flow motion estimation and edge-adaptive elliptical spatial upsampling (`EASU + RCAS`) adapted from the [AMD FidelityFX SDK](https://github.com/GPUOpen-LibrariesAndSDKs/FidelityFX-SDK) (Copyright © 2021–2026 Advanced Micro Devices, Inc.). See [`LICENSE_AMD_FIDELITYFX.txt`](LICENSE_AMD_FIDELITYFX.txt).
- **MIT KEMAR HRTF (MIT License)**: Binaural spatial audio impulse responses derived from the [MIT Media Lab KEMAR HRTF Dataset](https://sound.media.mit.edu/resources/KEMAR.html) (Bill Gardner & Keith Martin). See [`LICENSE_MIT_KEMAR_HRTF.txt`](LICENSE_MIT_KEMAR_HRTF.txt).
- **FFmpeg (GNU LGPL v2.1+)**: Multichannel software audio decoding via AndroidX Media3 extension compiled strictly without GPL modules. See [`LICENSE_FFMPEG.txt`](LICENSE_FFMPEG.txt).
- **AndroidX Media3 (Apache License 2.0)**: Core media playback framework by Google LLC.
- **AndroidLiquidGlass (Backdrop & Capsule, Apache License 2.0)**: Real-time lens refraction and continuous superellipse geometry by [Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).
