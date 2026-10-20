# Android component foundation

An independently launchable Android app using Java17, platform Views, a hardware-accelerated Canvas and cached SoundPool sample. No Compose/Material component library, JNI, SDK, game hooks, Anime assets or network permission. The manifest's platform window theme only supplies Activity chrome; visible buttons/panels/checkboxes/sliders are custom-painted.

## Build and run

JDK17+, Python3, Android SDK platform35 and build-tools35.0.0. Minimum Android8/API26; target35. A device/emulator is required for actual playback and screenshot checks.

```sh
cd app-ui
sdkmanager 'platforms;android-35' 'build-tools;35.0.0' 'platform-tools'
python3 build-debug.py --sdk "$ANDROID_HOME"
adb install -r build/manual/KennyPvtHax-UI-Suite-debug.apk
adb shell am start -n com.kennypvthax.uisuite/.MainActivity
# Optional alternative: Gradle8.9, AGP8.7.3
# gradle :app:assembleDebug
```

SDK-only script compiles resources with aapt2, Java with javac, DEX with d8, aligns and signs a debug APK. Its generated debug key lives under the developer's home `.android`, never in Git; public Android debug password is not a production signing credential. No release signing is configured. Windows uses the same Python script with JDK tools on PATH and `--sdk C:/.../Android/Sdk`.

## Architecture

`MainActivity` owns lifecycle; `Gallery` owns the scrollable component lab and local preferences. `KennyComponents` exposes styled button/panel factories, circular `Check`, `Slider`, and text field. Native semantic controls retain Android keyboard, focus, touch, selection and accessibility behavior; geometry is drawn directly instead of using stock button skins. `Tokens` contains source-derived palette and geometry. `AuroraModel` has no Android dependency; `AuroraBackground` owns reusable mesh buffers and Choreographer scheduling. `TapFeedback` owns one SoundPool stream and a 45ms original synthesized sample; never a character recording.

Gallery tabs navigate within the component lab, not invented application screens. A local dialog demonstrates the same panel composition. Pressed/disabled specimens are labelled; no backend is implied. Add a page after the reference video defines its behavior. Define page state separately from Views; network/business logic should sit outside these presentation classes.

## Reuse examples

```java
Button save = KennyComponents.button(context, "Save", () -> saveLocalPreference());
LinearLayout card = KennyComponents.panel(context);
card.addView(save);
```

Create a section in `Gallery` or a future screen with a panel and ordinary layout parameters; keep controls at least48dp high. Add navigation by selecting a local screen state and a corresponding View; never attach business side effects to `onDraw`. New animations should use `AuroraModel.approach`, finite bounded delta and reduced-motion snapping. Persist preferences through app-private SharedPreferences with safe defaults. Current persisted values: tap sounds, reduced motion, checkbox/toggle specimens and slider intensity. Intensity is a gallery slider specimen, not an Aurora brightness preference.

## Rendering and portability

Aurora directly adapts unsigned seeded noise, curtain parameters, spring/damping/speed cap and fixed120Hz stepping from `KennyAurora.h`. It uses three97×19 vertex grids, cached triangle arrays and 40 stars. Android Canvas mesh blending, full-screen portrait composition, font hinting, platform switch geometry and48dp hit targets are documented differences. No claim of pixel-perfect matching or device FPS.

The recovered menu font is Microsoft Segoe UI; its restrictive embedding terms do not establish standalone-app redistribution rights. The app explicitly uses Android installed sans-serif instead; this is a known typography mismatch. Text sizes adapt to sp and Android font-scale. Window insets and scrolling preserve access on small screens; fields use the system keyboard. Semantic button/checkbox/seekbar classes retain accessibility actions. Custom switch styling is an Android adaptation, not an exact native component (the menu uses circular checkboxes).

`onPause` removes frame callbacks; resume clears previous frame time to avoid hidden-time catch-up. Reduced motion stops Aurora updates while retaining static ribbons/stars. Tap sample loads once, bounded single stream,65ms throttle, sound setting persisted; actual speaker output requires device testing. All model arrays and renderer buffers are fixed-capacity. Canvas/framework internal allocations are not claimed zero.

## Tests and debugging

```sh
cd ..
python3 tools/verify_suite.py
# Requires a running emulator/device for capture:
python3 tools/capture_app.py --adb "$ANDROID_HOME/platform-tools/adb"
```

Tests compare actual C++ and Java numeric outputs, deterministic stars, bounds, touch/release, frame-rate behavior, NaN handling and reduced motion. APK compilation/signature checks differ from emulator UI tests and physical-device performance/audio tests. Use `adb logcat -s AndroidRuntime` for Java crashes. Screenshot capture runs the actual installed APK; never a mockup. See `shared/visual-reference` for evidence. No application pages/backend are implemented until the owner supplies the video/source.
