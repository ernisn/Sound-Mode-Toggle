# Sound Mode Toggle

A minimalist Android app that cycles through sound, vibrate and silent modes with a single tap.

## Features

- Cycle through sound, vibrate and silent modes with one tap
- Choose which modes to cycle through, e.g. only sound and vibrate
- Lightweight
- No UI when toggling
- No background services or continuous processes
- No impact on battery life

## Installation & Usage

1. Download and install the APK from the [Releases](https://github.com/ernisn/Sound-Mode-Toggle/releases) section
2. On first launch, the app will request permission to modify Do Not Disturb settings. Grant the permission to ensure the app works even when DND is enabled
3. Simply tap the app icon to switch to the next mode (Sound → Vibrate → Silent → Sound). The app will provide a brief vibration and show the new mode as feedback
4. To leave a mode out of the cycle, long-press the app icon and choose **Settings** (Android 7.1+), or open the app's settings from its App info page in system settings (Android 7.0+). At least two modes stay checked

## Requirements

- Android 5.0 (API level 21) or higher
- Permission to modify audio settings
- Permission to access notification policy (for Do Not Disturb functionality)
