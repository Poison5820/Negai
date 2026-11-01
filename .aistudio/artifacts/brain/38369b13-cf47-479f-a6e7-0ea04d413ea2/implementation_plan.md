# Implementation Plan - Negai Applet Improvements

This plan outlines the architecture changes and bug fixes to improve theme contrast, add paired Light/Dark themes + Material You, fix bottom bar accent colors, introduce compact frosted glass popups, implement independent App/Player UI scaling, fix video orientation transitions with long-press lock, update GitHub repository links to `https://github.com/Poison5820/Negai`, and restructure the README with technical release notes.

## Proposed Changes

### 1. Light Theme Contrast & Paired Light/Dark Theme Modes
- **File**: `app/src/main/java/com/example/model/PlayerModels.kt`, `app/src/main/java/com/example/ui/theme/Theme.kt`
- Fix readable content colors, card containers, and high-contrast text tokens for Light mode schemes in `NegaiTheme`.
- Expand `AppThemeMode` enum to support:
  - System Auto Dark/Light theme switching (`SYSTEM_AUTO`)
  - Standalone Material You dynamic theme (`MATERIAL_YOU`)
  - Paired themes with explicit Light and Dark options for OLED Pitch Black, Deep Navy, Anime Purple, Cyberpunk Mint, Slate Grey, and Sunset Amber.
- **File**: `app/src/main/java/com/example/ui/screens/SettingsCategoryPages.kt`, `app/src/main/java/com/example/ui/screens/QuickThemeBottomSheet.kt`
- Update theme selector UI to allow both system auto-switch and manual choice of paired Light/Dark variants.

### 2. Accent-Colored Navigation Elements Across All Themes
- **File**: `app/src/main/java/com/example/ui/screens/LibraryScreen.kt`
- Update `NavigationBarItem` and `NavigationRailItem` color definitions so active indicator pills and selected icons/labels utilize the active theme's primary accent color across all themes (not just Material You).

### 3. Compact Frosted Glass Popup Sheets & Dialogs
- **File**: `app/src/main/java/com/example/ui/screens/PlayerControlSheets.kt`, `app/src/main/java/com/example/ui/screens/PlayerQuickActionsBar.kt`
- Reduce internal padding and container dimensions of bottom sheets and modals.
- Set ultra-thin accent borders (`0.5.dp` with low alpha).
- Implement center-to-edge radial/linear gradient background brushes for a frosted glass effect (more transparent in center, slightly darker toward edges).

### 4. Independent App UI Scale & Player HUD Scale
- **File**: `app/src/main/java/com/example/model/PlayerModels.kt`, `app/src/main/java/com/example/data/PlayerPreferencesRepository.kt`, `app/src/main/java/com/example/ui/NegaiViewModel.kt`
- Add `appUiScalePercent` (75%–140%) alongside `playerUiScalePercent` (75%–140%).
- Apply `appUiScalePercent` to general app density (`LocalDensity` in `MainActivity.kt`) and `playerUiScalePercent` to player controls.
- **File**: `app/src/main/java/com/example/ui/screens/SettingsCategoryPages.kt`
- Add two independent slider rows with reset buttons in `AppearanceSettingsPage`.

### 5. Video Orientation Transitions & Long-Press Lock
- **File**: `app/src/main/java/com/example/player/PlayerEngine.kt`, `app/src/main/java/com/example/ui/screens/PlayerScreen.kt`
- **End-of-video flip fix**: Prevent aspect ratio calculation updates when playback reaches the final second of an episode or during buffering transitions.
- **Orientation override & long-press lock**:
  - Add `isOrientationLocked` state to player orientation.
  - Tapping orientation button toggles orientation mode (`Auto` -> `Landscape` -> `Portrait`).
  - Long-pressing orientation button locks/unlocks orientation lock.
  - Display a lock badge on the orientation quick chip when locked.
  - When unlocked, switching episodes automatically resets orientation to the next video's aspect ratio / default setting; when locked, manual orientation persists across playlist episodes.

### 6. Repository Link Updates & Obtainium Sync
- **Files**: `README.md`, `app/src/main/java/com/example/ui/screens/SettingsCategoryPages.kt`, `app/src/main/java/com/example/model/AppVersionInfo.kt`, and obtainium link builders
- Replace all legacy repository links with `https://github.com/Poison5820/Negai`.

### 7. Release Notes, Structured README & Git Hygiene
- **File**: `README.md`
- Restructure `README.md` into a structured technical format (Architecture, Features, Tech Stack, Build & Export Instructions).
- Format version info and release notes in technical, structured language.
- Ensure build artifacts and temporary files (such as `.aistudio`, `implementation_plan.md`, build outputs) are excluded from export.

## Verification Plan

### Automated Unit Tests
- Run `gradle :app:testDebugUnitTest` to verify theme state, preference persistence, and orientation logic tests.

### Compilation
- Run `compile_applet` to confirm zero compilation errors.
