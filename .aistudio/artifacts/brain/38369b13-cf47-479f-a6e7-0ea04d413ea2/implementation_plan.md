# Release v3.1 — Unified Frosted Glass HUD, Pinned Library Header, Finger-Tracked Mini-Menus & History Thumbnails

Release v3.1 unifies the player interface with a cohesive Frosted Glass aesthetic, introduces a sticky library search and continue-watching header, upgrades hold-and-swipe mini-menus to direct finger position hit-testing, adds video thumbnail caching in watch history, and refines screen orientation locking.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural decisions incorporate the user's confirmed preferences:

- **Confirmed Decision 1 (Mini-Menu Finger Tracking)**: Instead of a relative virtual drag offset, mini-menus will use **absolute window touch Y-coordinates**. Because the bottom bar button is already at the bottom of the screen, the initial finger position directly corresponds to the bottom item («Откл.» / "Off"). Moving the finger up highlights options in the menu above; resting on the button or moving downward keeps «Откл.» selected, completely eliminating the bottom-screen-edge boundary issue.
- **Confirmed Decision 2 (Sticky Library Header)**: The Continue Watching banner and search bar are extracted out of the scrolling `LazyColumn` into a dedicated, pinned top header bar with compact heights. The folder grid/list scrolls smoothly underneath with zero layout shift.
- **Confirmed Decision 3 (Orientation Lock)**: Long-pressing the orientation button toggles hard lock (`isOrientationLocked`) with a visible lock badge and accent border. Single-tap orientation toggling cycles modes without setting `isOrientationLocked = true`, preventing accidental lock-in.

---

## 1. Overview & Core Concept

- **What It Delivers**:
  - **Unified Frosted Glass Player HUD**: All player pop-up modal sheets (Audio, Subtitles, Decoder, Speed, Tools, Episodes, Extras, TorrServer), top HUD action buttons, Skip Intro button, and the resolution badge share the exact same frosted glass aesthetic (deep dark glass base `Color(0xC80B1220)`, subtle accent-colored luminance gradient, specular glass borders, and compact horizontal width bounds).
  - **Pinned Library Search & Continue Watching Header**: Instant access to search and the resume card without scrolling back up across large libraries.
  - **Absolute Finger-Position Mini-Menu Selection**: Holding a bottom bar button opens the mini-menu, and sliding the finger over any menu item directly highlights and selects it upon release.
  - **Video Thumbnails in History**: Watch history displays actual extracted video frame thumbnails (via Coil's `VideoFrameDecoder`) with graceful fallbacks.
  - **Orientation Lock Control**: Clear distinction between temporary orientation toggle and enforced orientation locking.
  - **Official Release v3.1**: Version bump to `3.1` (code `9`) with release highlights comparing against `v3.1-pre1`.

---

## 2. User Experience & Visual Design

### Unified Frosted Glass Aesthetics
```
┌─────────────────────────────────────────────────────────────┐
│  [<- Back]   Episode 01 - Title        [HD / 4K] [Episodes] │  <- Frosted Glass Top Bar
│                                                             │
│                                                             │
│                                                             │
│                                           [+85s Intro]      │  <- Frosted Glass Skip Intro
│  ───────────────────────── ● ─────────────────────────────  │
│  [Audio] [Subs] [1.0x]   | [MEMC] [Anime4K] |   [HW+] [::]  │  <- Symmetric Pinned Bar
└─────────────────────────────────────────────────────────────┘
```

- **Color & Material Tokens**:
  - Base glass container: `Color(0xC80B1220)` (78% opacity dark graphite-obsidian).
  - Surface highlight: `Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.16f), accent.copy(alpha = 0.08f), Color.Transparent))`.
  - Glass specular border: `BorderStroke(0.85.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.45f), accent.copy(alpha = 0.65f), Color.White.copy(alpha = 0.20f))))`.
  - Max horizontal constraints: Modal cards and sheets constrained to `widthIn(max = 520.dp)` on tablets and landscape phones to prevent edge-to-edge stretching.
  - Resolution Badge: Rendered as a compact frosted pill with sharp contrast text and accent border.

### Pinned Library Header (Folders Screen)
```
┌─────────────────────────────────────────────────────────────┐
│  [▶ Continue: Episode 12 • 14:22 / 24:10]                  │  <- Compact Resume Card
│  [🔍 Search folders & episodes...                         ] │  <- Compact Search Bar
├─────────────────────────────────────────────────────────────┤
│  [📁 Anime Season 1]                                        │  <- Scrollable LazyColumn
│  [📁 Movies]                                                │     (scrolls underneath)
│  [📁 Downloads]                                             │
└─────────────────────────────────────────────────────────────┘
```

- Pinned at the top of `FoldersTabContent`, with padding `12.dp` and background matching `MaterialTheme.colorScheme.background`.
- Continue watching card: Compact height (44.dp), 1-line title, progress bar, play icon.
- Search bar: Compact height (48.dp), rounded shape (14.dp).

### History Screen Video Thumbnails
- Each item in `RecentTabContent` displays a 64x44dp 16:9 thumbnail frame extracted from the video file (`data = item.videoUri`, `decoderFactory = VideoFrameDecoder.Factory()`, `videoFrameMillis = 20000L`).
- Progress bar placed directly along the bottom of the thumbnail.
- Fallback to the stylish play-icon surface if thumbnail cannot be decoded or file is missing.

---

## 3. Key Product Decisions & Trade-Offs

- **Finger Window Position Hit-Testing**:
  - *Decision*: Each mini-menu item registers its `topY..bottomY` in window coordinates. When dragging after hold, the finger's `windowY` is checked against item bounds.
  - *Why*: Eliminates the virtual offset calculation that required dragging downwards off the physical screen edge to hit "Откл.". When holding the button, the touch is already at the bottom item; sliding upward steps up the menu.
- **Compact Sheet Width Limits (`520.dp`)**:
  - *Decision*: In landscape video playback, sheets no longer span 100% of the screen width; instead they are centered with a max width of 520.dp.
  - *Why*: Enhances one-handed ergonomics on tablets and large landscape phones, matching modern video player UX.
- **Orientation Lock via Long-Press**:
  - *Decision*: Tap toggles landscape/portrait according to user settings; Long-press toggles hard lock (`isOrientationLocked = !isOrientationLocked`) with a lock icon.
  - *Why*: Solves the bug where tapping orientation permanently trapped the player in lock mode.

---

## 4. Technical Architecture & Data Strategy

```
┌───────────────────────────────────────────────────────────────┐
│                      UI / Compose Layer                       │
├───────────────────────┬───────────────────────┬───────────────┤
│    LibraryScreen      │      PlayerScreen     │ RecentScreen  │
│  ┌─────────────────┐  │  ┌─────────────────┐  │ ┌───────────┐ │
│  │ Pinned Header   │  │  │ Frosted Sheets  │  │ │ Video     │ │
│  │ (Search+Resume) │  │  │ Frosted Badges  │  │ │ Thumbnails│ │
│  │ LazyColumn      │  │  │ Lock On Hold    │  │ │ (Coil)    │ │
│  └─────────────────┘  │  └─────────────────┘  │ └───────────┘ │
├───────────────────────┴───────────────────────┴───────────────┤
│                   PlayerQuickActionsBar                       │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │ Absolute Window Y Hit-Testing for Mini Pop-up Items     │  │
│  └─────────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────┘
```

- **File Modifications**:
  - `app/build.gradle.kts`: Bump `versionCode = 9`, `versionName = "3.1"`.
  - `com.example.model.PlayerModels.kt`: Update `VERSION_NAME = "3.1"`, `RELEASE_TAG = "v3.1"`.
  - `com.example.ui.screens.VersionPlanetaryModalSheet.kt`: Add v3.1 release notes comparing with v3.1-pre1.
  - `com.example.ui.screens.PlayerControlSheets.kt` & other player sheets: Apply frosted glass container styling, accent tint, and horizontal max-width `520.dp`.
  - `com.example.ui.screens.PlayerScreen.kt`: Update Skip Intro button and top HUD buttons to frosted glass; update orientation button to lock only on long-press.
  - `com.example.ui.components.PlayerControlsThemedComponents.kt`: Update `ResolutionBadgeChip` with frosted glass background and vivid accent text.
  - `com.example.ui.screens.PlayerQuickActionsBar.kt`: Implement direct window Y hit-testing for hold-and-drag mini popup menus.
  - `com.example.ui.screens.LibraryScreen.kt`: Pin continue watching and search in a sticky header above the folders `LazyColumn`.
  - `com.example.ui.screens.RecentTabScreen.kt`: Render Coil video thumbnails with progress indicator.
  - `app/src/test/java/com/example/NegaiV2FeaturesTest.kt`: Update version assertion to `"3.1"`.
