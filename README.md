# negai — Hardware 4K / HDR Video & Anime Player for Android

[![Version](https://img.shields.io/badge/version-2.5-00E5FF?style=flat-square)](#)
[![Platform](https://img.shields.io/badge/platform-Android_8.0%2B_(API_26%2B)-3DDC84?style=flat-square&logo=android)](#)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.x_%7C_Jetpack_Compose-7F52FF?style=flat-square&logo=kotlin)](#)
[![Media3](https://img.shields.io/badge/Media3-ExoPlayer_%2B_OpenGL_ES-FF6D00?style=flat-square)](#)

<a href="https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/Poison5820/Negai">
  <img src="https://raw.githubusercontent.com/ImranR98/Obtainium/main/assets/graphics/badge_obtainium.png" alt="Get it on Obtainium" height="75">
</a>

---

## 📐 Architecture Overview

**negai** is built using modern Android architecture patterns (MVVM + Jetpack Compose + Kotlin Coroutines & Flow).

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

## 🎨 Themes & UI Customization

- **Paired Themes**: Light and Dark variants for each color scheme:
  - **Clean OLED**: Absolute `#000000` AMOLED Dark & High-Contrast Light Cyan
  - **Deep Navy**: Ocean Graphite Dark & Sky Blue Light
  - **Cyber Sakura**: OLED Magenta Dark & Soft Pink Light
  - **Cyber Mint**: Emerald Dark & Fresh Mint Light
  - **Slate Grey**: Matte Dark & Paper Light
  - **Material You**: Dynamic wallpaper accent extraction (Android 12+)
  - **System Auto**: Smooth dynamic light/dark resolution following system OS settings
- **Independent Scaling**: Separate App UI Scale (75%–140%) and Player HUD Scale (75%–140%).
- **Frosted Glass Popups**: Radial gradient translucency with 0.5dp ultra-thin borders.

---

## 🎬 Core Video & Audio Pipeline

### Video Processing & Shaders
- **Media3 / ExoPlayer Pipeline**: HW+, HW, and SW decoding modes supporting H.264, HEVC (10-bit HDR10/HLG), VP9, and AV1.
- **Anime4K Shader Engine**: Real-time GLSL upscaling (Anime4K_Fast, CNN, CNNX) with auto-downgrade when MEMC frame interpolation is active to prevent frame drops.
- **Aspect Ratio & Orientation**: Fit, Zoom, and Stretch scale modes + long-press orientation lock badge to prevent unexpected orientation flips during episode transitions.

### Audio & Bluetooth Latency Compensation
- **Auto Audio Matching**: Scans `Audio/`, `Rus/`, `Dub/` subfolders for external `.mka`, `.aac`, `.flac`, `.mp3` tracks by episode number.
- **Bluetooth A2DP / LE Audio Sync**: Real-time `AudioDeviceCallback` monitoring applying non-destructive `MediaClock` A/V offset without dropping PCM audio samples.
- **Audio Boost**: Software volume boost up to 200% with soft-knee `tanh` limiter.

---

## 🛠 Build & Development Instructions

### Environment Requirements
- Android SDK 35 / Build Tools 35.0.0
- JDK 17+
- Gradle 8.x

### Build Commands

```bash
# Build Debug APK
gradle assembleDebug

# Build Release APK
gradle assembleRelease

# Execute Local JVM Unit & Robolectric Tests
gradle :app:testDebugUnitTest
```

---

## 🔗 Repository & Updates

- **Official Repository**: [https://github.com/Poison5820/Negai](https://github.com/Poison5820/Negai)
- **Obtainium App Integration**: Direct release tracking supported via `https://github.com/Poison5820/Negai`.
