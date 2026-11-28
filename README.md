# Negai — Anime & Cinema 4K Video Player for Android

<p align="center">
  <img src="assets/ic_negai_launcher_fg.png" alt="Negai Logo" width="128" height="128" />
</p>

<p align="center">
  <strong><a href="README.md">English</a></strong> · <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-4.11.18-FF80AB?style=flat-square" alt="Version 4.11.18" />
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

**Negai** is a modern, flagship-grade Android 4K video player engineered for anime enthusiasts and cinema purists. It combines a native **Vulkan 1.1 (`SPIR-V`)** and **OpenGL ES 3.1 (`FP16`)** compute video pipeline, **Anime4K** + **ArtCNN C4F16** + **AMD FSR 1.0** + **Qualcomm SGSR v1** + **16-bit Debanding**, **10-frame Lookahead MEMC** (**RIFE v4.22-lite** & **Symmetric 3DRS L4–L3 TV-MEMC Optical Flow**), mobile **Adaptive Sync (`VRR / LFC`)**, natural **4K HDR10 / Dolby Vision**, zero-allocation **MIT KEMAR HRTF** spatial audio, refractive **Liquid & Frosted Glass** interfaces, **120+ world languages** for automatic track pairing, and direct **TorrServer P2P** streaming — with zero ads and zero tracking.

---

## 📥 Downloads & Installation

Install and update Negai automatically via **Obtainium** or download the universal APK:

- **Universal APK (`Community Edition`)**: [`negai.apk`](https://github.com/Poison5820/Negai/releases/latest/download/negai.apk)
- **Supported CPU ABIs**: `arm64-v8a`, `armeabi-v7a`, `x86_64`, `x86` (16 KB memory-page aligned)
- **Minimum OS**: Android 8.0+ (API 26+; Android 10+ API 29 required for native Vulkan 1.1 `AHardwareBuffer` zero-copy path)
- **Automatic Updates via Obtainium**: Tap the badge above or add `https://github.com/Poison5820/Negai` in [Obtainium](https://github.com/ImranR98/Obtainium).
- **Release Notes & Privacy**: [GitHub Releases](https://github.com/Poison5820/Negai/releases) · [`PRIVACY.md`](PRIVACY.md)

---

## ✨ What's New in Release 4.11.18

- **Adaptive 4-Block Color OBMC & Optical Flow Bugfixes (`v4.11.18`)**: Upgraded 160×90 symmetric Optical Flow synthesis to discrete 4-block color OBMC with continuous SVP artifact masking, fixed odd-shift Census checkerboard bias and dense 3×3 search patches, corrected full-step 3DRS temporal advection and `dupSpan` resets, and restored pre-flow quality readback on 90–120 Hz displays.
- **Symmetric 160×90 TV-MEMC Optical Flow (`v4.11.17`)**: Replaced the dual-field 320×180 pipeline with a fast single-field De Haan symmetric 3DRS (`L4 → L3`, `160×90`) motion estimator and Astola vector-median regularization for lower GPU load.
- **Dual-Field Smoothed Optical Flow & Manual Release Workflow (`v4.11.16`)**: Streamlined release publishing to manual dispatch with clean release notes and SHA-256 verification.

---

## Core Engineering Pillars

### 1. Dual-Backend GPU Compute Video Engine (`Vulkan 1.1` & `OpenGL ES 3.1 FP16`)
- **Native Vulkan 1.1 (`NDK 27 / C++20 / SPIR-V`) & OpenGL ES 3.1 (`FP16`)**: Select `Auto`, `Vulkan 1.1`, or `OpenGL ES 3.1` live during playback with automatic zero-crash driver fallback. Hardware-decoded SDR, HDR10/HDR10+/HLG, and 3D frames enter Vulkan directly via zero-copy `AImageReader` / `AHardwareBuffer` (`VkSamplerYcbcrConversion`), output natively to 10-bit PQ/HLG swapchains or `ITU-R BT.2390` SDR tone mapping, and pace presentation via `VK_GOOGLE_display_timing`, while Dolby Vision and unenhanced HDR route directly to **Hardware Composer (`HWC`)**.
- **Multi-Engine Spatial Upscaling (`Auto / Anime4K v4 / ArtCNN C4F16 / AMD FSR 1.0 / Qualcomm SGSR v1`) & 16-bit Debanding**:
  - **16-bit Debanding**: Optional lightweight FP16 gradient smoother strictly after MEMC and before spatial upscaling (0 ms bypass when disabled).
  - **Anime4K (`Fast / HQ / Ultra`)**: Preserves 100% natural ink-line thickness via symmetric S-curve edge steepening, 4-tap Catmull-Rom tangent anti-aliasing, highlight clamping, and triangular dithering.
  - **ArtCNN C4F16 (`MIT`)**: Compact 2× neural anime upscaler running in FP16 on Vulkan 1.1 Compute with lazy GPU tensor allocation for crisp contour restoration.
  - **AMD FidelityFX Super Resolution 1.0 (`EASU + RCAS`) & Qualcomm SGSR v1 (`BSD-3-Clause`)**: Choose between 2-pass elliptical Lanczos2 + RCAS or ultra-fast single-pass 12-tap directional mobile upscaling for cinema, series, and 3D CGI.
  - **Asynchronous GPU Style Probe (`Auto`)**: Classifies cel-shaded animation vs live-action gradients on the GPU with zero pipeline stalls (`Auto → Anime4K / AMD FSR`).
- **10-Frame Lookahead MEMC (`RIFE v4.22-lite` & `Symmetric 3DRS L4–L3 TV-MEMC Optical Flow`)**: Ahead-of-time decoder FIFO with exact `1s / 2s / 3s` anime cadence detection, double-buffered RIFE neural flow with 2-chunk scheduling at `>= 85 Hz`, and symmetric De Haan 3DRS (`160×90`) optical flow with vector-median regularization, adaptive 4-block color OBMC + SVP artifact-masked synthesis, and crisp scene-cut snapping (`×2 / 60 FPS / Max Hz`), plus interactive **Split Compare** slider and **0ms instant live & paused redraws**.

### 2. Adaptive Display Sync (`VRR / LFC`), 4K HDR/DV & Transport Formats
- **Mobile Adaptive Sync (`VRR / LFC`) & Hybrid `VFR` Tracking**: Classifies your screen panel (`LTPO ARR`, `Multi-Hz Seamless VRR`, `Multi-Hz LFC`, or `Fixed 60 Hz`), locks onto peak harmonic refresh containers (such as `120 Hz` `5×` for `23.976 fps`) to eliminate `3:2 pulldown` judder and OLED mode-switch gamma flicker, and tracks hybrid anime `VFR` transitions (`23.976 ↔ 59.94 ↔ 29.97 fps`) in real time.
- **Natural YouTube-Style 4K HDR & Dolby Vision**: Hardware-accelerated 10-bit `HDR10`, `HDR10+`, `HLG`, and **Dolby Vision** (including zero-allocation Profile 7 Enhancement Layer NAL sanitization) using native `COLOR_MODE_HDR` without blinding system brightness overrides.
- **Broad Codec, Blu-ray BDAV & 3D Support**: Hardware-first `H.264`, `HEVC Main10`, `VP9`, and `AV1` (`Auto / HW+ / SW`), dedicated 192-byte **Blu-ray BDAV (`.m2ts` / `.ts`)** transport demuxer with PGS bitmap subtitles, and **3D Side-by-Side (`SBS`) / Top-and-Bottom (`TAB`)** playback.

### 3. Zero-Allocation 32-Bit Float Audio, 10-Band EQ & Spatial DSP
- **32-Bit Float Audio Pipeline & 10-Band Biquad Equalizer**: Low-latency `PCM_FLOAT` internal signal path with a single final soft-clip limiter, paired with a 10-band ISO equalizer (`31 Hz – 16 kHz`), genre presets, automatic headroom compensation, and instant session A/B bypass.
- **3D Spatial Audio (`MIT KEMAR HRTF`, `2.0 Stereo Virtualizer` & `XTC`)**: Real-time 5.1/7.1 binaural headphone convolution (`MIT Media Lab KEMAR HRTF`), dedicated 2.0 stereo headphone stage widening, and `< 220 Hz` bass-protected stereo speaker **Crosstalk Cancellation (`XTC`)** with smart **Auto** output routing.
- **Voice De-Artifact, 300% Volume Boost, Lossless Surround & Bluetooth Sync**: 4-mode zero-allocation voice restoration (`Off`, `Soft`, `Standard`, `BWE Upscale`), clipping-guarded volume amplification up to **300%**, **FFmpeg** multichannel decoding (`DTS`, `DTS-HD Master Audio 7.1`, `Dolby TrueHD + Atmos`, `EAC3`, `FLAC`, `Opus`), and **Bluetooth Audio Delay** compensation (`-5000 ms` to `+5000 ms`).

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

## 🗺️ Roadmap (Android)

- **`v4.11.x` (Current — Core Polish, Audio Inspector & Scalers)**:
  - Field validation of the direct `SurfaceView` + `AHardwareBuffer` glass backdrop, MEMC PLL pacing, live 2.0 Stereo Virtualizer toggle, and Audio Spectrum Inspector (Pre/Post-DSP + stereo width);
  - High-quality 2-pass separable scaling filters (`Catmull-Rom`, `Mitchell-Netravali`, `Spline36 + AR`, `Lanczos3 + AR`) for upscaling and 4K downscaling while keeping zero-overhead direct hardware presentation on Bilinear;
  - Optional 2-tier RIFE model selection (default Fast `v4.22-lite` 4-scale + downloadable Quality `v4.25-lite`/`v4.26` 5-scale for flagship GPUs).
- **`v4.12.0` (Next-Gen Subtitles, Translation, Local TTS & Color Engine)**:
  - Native NDK `libass` subtitle renderer for full ASS/SSA typesetting, vector drawings, animations, and embedded fonts;
  - Dual subtitles (simultaneous two-track display);
  - Subtitle auto-translation: offline on-device translation (`ML Kit` / `Bergamot` / `Opus-MT`) plus optional cached batch online API translation;
  - Local neural subtitle voiceover (TTS) via `Sherpa-ONNX` with downloadable `Piper` / `Kokoro` voice models and automatic original-audio ducking;
  - Real-time picture & color adjustments (brightness, contrast, saturation, gamma, color temperature, presets) and configurable `BT.2390` HDR→SDR target luminance.
- **`v4.13.0` (Neural Audio Super-Resolution, Local Network & Media Servers)**:
  - Neural Audio Super-Resolution (`NovaSR` / `LavaSR v2` via `ONNX Runtime` / `LiteRT`, MIT / Apache-2.0) for phase-coherent high-frequency reconstruction and low-bitrate compression artifact removal;
  - Built-in SMB (v2/v3), WebDAV, and FTP/SFTP local network sources;
  - Native Jellyfin, Emby, and Plex media server integration;
  - Online subtitle search/download (OpenSubtitles / SubDL) and optional watch-progress scrobbling (Shikimori / AniList / MAL / Trakt).
- **`v4.14.0` (Form Factors, Thermal Guard & Backup)**:
  - Adaptive Tablet & Foldable multi-pane layouts;
  - Full keyboard, mouse, gamepad, and D-Pad navigation;
  - Automatic GPU thermal step-down profiles (graceful shader/MEMC scaling under sustained heat);
  - JSON backup/restore for settings, EQ presets, audio/video profiles, and quick-action layouts.
- **`v5.0` (Google Play Release & Cross-GPU Polish)**:
  - Full Google Play (`com.negai.player`) release audit (Play Billing edge cases, strict store edition isolation, 16 KB page alignment, Android 15/16 compliance, clean open-source/commercial license verification);
  - First-run onboarding wizard;
  - Cross-vendor GPU verification matrix (Adreno, Mali, Xclipse).

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
│ • ArtCNN • SGSR • Anime4K • FSR • MEMC │  │ • 120+ Lang Matcher & Subtitles│
│ • BDAV .m2ts • FFmpeg • KEMAR HRTF DSP │  │ • TorrServer & DoH Client      │
└────────────────────────────────────────┘  └────────────────────────────────┘
```
</details>

---

## 📄 Licensing, Privacy & Open-Source Attributions

- **Negai Player**: Proprietary, closed-source software. Copyright © 2026 Negai Player. All Rights Reserved. This public repository serves as the official release distribution hub, issue tracker, and open-source attribution notice. Read our [`PRIVACY.md`](PRIVACY.md).
- **Anime4K (MIT License)**: Real-time anime line reconstruction shaders based on [bloc97/Anime4K](https://github.com/bloc97/Anime4K) (Copyright © 2019–2021 bloc97). See [`LICENSE_ANIME4K.txt`](LICENSE_ANIME4K.txt).
- **ArtCNN C4F16 (MIT License)**: Compact neural 2× anime upscaling architecture and weights converted from [Artoriuz/ArtCNN](https://github.com/Artoriuz/ArtCNN) (Copyright © 2024 Joao Chrisostomo, Kacper Michajłow). See [`LICENSE_ARTCNN.txt`](LICENSE_ARTCNN.txt).
- **AMD FidelityFX Optical Flow & Super Resolution 1.0 (MIT License)**: Hierarchical 32-bit Census optical flow motion estimation and edge-adaptive elliptical spatial upsampling (`EASU + RCAS`) adapted from the [AMD FidelityFX SDK](https://github.com/GPUOpen-LibrariesAndSDKs/FidelityFX-SDK) (Copyright © 2021–2026 Advanced Micro Devices, Inc.). See [`LICENSE_AMD_FIDELITYFX.txt`](LICENSE_AMD_FIDELITYFX.txt).
- **Qualcomm Snapdragon Game Super Resolution v1 (BSD-3-Clause License)**: Single-pass mobile directional spatial upscaler adapted from [SnapdragonStudios/snapdragon-gsr](https://github.com/SnapdragonStudios/snapdragon-gsr) (Copyright © 2023 Qualcomm Innovation Center, Inc.). See [`LICENSE_QUALCOMM_SGSR.txt`](LICENSE_QUALCOMM_SGSR.txt).
- **RIFE / Practical-RIFE (MIT License)**: Neural frame interpolation architecture based on [megvii-research/ECCV2022-RIFE](https://github.com/megvii-research/ECCV2022-RIFE) and [hzwer/Practical-RIFE](https://github.com/hzwer/Practical-RIFE) (Copyright © 2020–2024 Megvii Research & Zhewei Huang). Converted model weights (`negai_rife_v422_lite_fp16.bin` and legacy `negai_rife_v426_fp16.bin`) are distributed under the MIT License in [Poison5820/Negai](https://github.com/Poison5820/Negai). See [`LICENSE_RIFE.txt`](LICENSE_RIFE.txt).
- **MIT KEMAR HRTF (MIT License)**: Binaural spatial audio impulse responses derived from the [MIT Media Lab KEMAR HRTF Dataset](https://sound.media.mit.edu/resources/KEMAR.html) (Bill Gardner & Keith Martin). See [`LICENSE_MIT_KEMAR_HRTF.txt`](LICENSE_MIT_KEMAR_HRTF.txt).
- **FFmpeg (GNU LGPL v2.1+)**: Multichannel software audio decoding via AndroidX Media3 extension compiled strictly without GPL modules. See [`LICENSE_FFMPEG.txt`](LICENSE_FFMPEG.txt).
- **AndroidX Media3 (Apache License 2.0)**: Core media playback framework by Google LLC.
- **AndroidLiquidGlass (Backdrop & Capsule, Apache License 2.0)**: Real-time lens refraction and continuous superellipse geometry by [Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).
