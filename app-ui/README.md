# Android component foundation

A reference-based independently launchable Android app using Java17, platform Views, a hardware-accelerated Canvas and cached SoundPool sample. No Compose/Material component library, JNI, SDK, game hooks, Anime assets or network permission. The manifest's platform window theme only supplies Activity chrome; visible buttons/panels/checkboxes/sliders are custom-painted.

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

`MainActivity` owns lifecycle; `Gallery` owns the scrollable component lab and local preferences. `KennyComponents` exposes styled button/panel factories, circular `Check`, `Slider`, and text field. Native semantic controls retain Android keyboard, focus, touch, selection and accessibility behavior; geometry is drawn directly instead of using stock button skins. `Tokens` contains source-derived palette and geometry. `AuroraModel` has no Android dependency; `AuroraBackground` owns reusable mesh buffers and Choreographer scheduling. `TapFeedback` owns one SoundPool stream and a 45 ms original synthesized sample; never a character recording.

The default entry is the reference-based login screen; the original gallery remains accessible using `--ez gallery true` in debuggable builds. Gallery tabs navigate within the component lab. A local dialog demonstrates the same panel composition. Pressed/disabled specimens are labelled; no backend is implied. Add a page after the reference video defines its behavior. Define page state separately from Views; network/business logic should sit outside these presentation classes.

## Reuse examples

```java
Button save = KennyComponents.button(context, "Save", () -> saveLocalPreference());
LinearLayout card = KennyComponents.panel(context);
card.addView(save);
```

Create a section in `Gallery` or a future screen with a panel and ordinary layout parameters; keep controls at least48 dp high. Add navigation by selecting a local screen state and a corresponding View; never attach business side effects to `onDraw`. New animations should use `AuroraModel.approach`, finite bounded delta and reduced-motion snapping. Persist preferences through app-private SharedPreferences with safe defaults. Current persisted values: tap sounds, reduced motion, checkbox/toggle specimens and slider intensity. Intensity is a gallery slider specimen, not an Aurora brightness preference.

## Rendering and portability

Aurora directly adapts unsigned seeded noise, curtain parameters, spring/damping/speed cap and fixed120 Hz stepping from `KennyAurora.h`. It uses three97×19 vertex grids, cached triangle arrays and 40 stars. Android Canvas mesh blending, full-screen portrait composition, font hinting, platform switch geometry and48 dp hit targets are documented differences. No claim of pixel-perfect matching or device FPS.

The recovered menu font is Microsoft Segoe UI; its restrictive embedding terms do not establish standalone-app redistribution rights. The app explicitly uses Android installed sans-serif instead; this is a known typography mismatch. Text sizes adapt to sp and Android font-scale. Window insets and scrolling preserve access on small screens; fields use the system keyboard. Semantic button/checkbox/seekbar classes retain accessibility actions. Custom switch styling is an Android adaptation, not an exact native component (the menu uses circular checkboxes).

`onPause` removes frame callbacks; resume clears previous frame time to avoid hidden-time catch-up. Reduced motion stops Aurora updates while retaining static ribbons/stars. Tap sample loads once, bounded single stream,65 ms throttle, sound setting persisted; actual speaker output requires device testing. All model arrays and renderer buffers are fixed-capacity. Canvas/framework internal allocations are not claimed zero.

## Tests and debugging

```sh
cd ..
python3 tools/verify_suite.py
# Requires a running emulator/device for capture:
python3 tools/capture_app.py --adb "$ANDROID_HOME/platform-tools/adb"
```

Tests compare actual C++ and Java numeric outputs, deterministic stars, bounds, touch/release, frame-rate behavior, NaN handling and reduced motion. APK compilation/signature checks differ from emulator UI tests and physical-device performance/audio tests. Use `adb logcat -s AndroidRuntime` for Java crashes. Screenshot capture runs the actual installed APK; never a mockup. See `shared/visual-reference` for evidence. The supplied video now defines the login/dashboard page inventory. Production backend behavior remains unavailable without API/source specifications.

Deterministic rendering mode for visual review: launch the gallery with `adb shell am start -n com.kennypvthax.uisuite/.MainActivity --ef preview_time 3` after force-stopping the process. This freezes presentation time only; it is a component-lab capture feature, not an application page or configuration action. `tools/capture_app.py` uses this mode and saves real gallery, controls, dialog and reduced-motion/audio-settings captures.


## Actual login screen

`MainActivity` keeps the original Java/Views/Canvas framework and lifecycle. Its normal content is now `AppShell`, which owns a single original `AuroraBackground`, scrollable page content and the unchanged `TapFeedback`. The app preserves the original `Gallery` behind the explicit debug gallery entry. New files:

- `AppShell`: page layout/status, language selection, bounded background IO and lifecycle ownership.
- `AuthController`, `AuthProvider`: Android-independent request/result/cancellation state; no endpoint or accepted key.
- `UnconfiguredAuthProvider`: normal provider, returning a truthful configuration error.
- `DebugPendingAuthProvider`: only selected by an explicitly requested pending fixture in a debuggable Activity; holds an actual request until cancellation without timers.
- `CredentialVault`: Android Keystore AES-GCM; only IV/ciphertext saved under app-private `kenny-credentials` preferences.
- `LocaleSettings`: validated English/Chinese/Hindi/Arabic resource configuration and persistent language preference.
- `LicenseField`: native masked EditText with selection/cursor/IME support and animated source-derived focus border. Autofill and saved-instance text are disabled to avoid silently copying license keys to those systems.
- `BrandMark`: the existing `KennyIdentity::Emblem` primitive geometry, with no Anime image.
- `LoadingMark`: lightweight state-driven ring, respecting Reduced Motion and lifecycle.

Buttons keep their original custom paint. Their text now delegates to Android TextView layout for proper Arabic/Hindi shaping and wrapping; no Material button visuals are introduced. Circular checkboxes use relative padding and mirrored indicator positions. These extensions preserve the existing gallery tests.

Saving is an explicit button action, as observed in the recording; entering a key does not silently save or submit it. Forget removes the encrypted record and clears the field. IO is serialized off the rendering thread, with duplicate operations disabled while pending. Missing/corrupt records or Keystore failures report a recoverable status. No credentials are logged or uploaded. Saved keys reload after process death. Unsaved input is not written into Activity saved-state; it can be cleared by Activity recreation. Android framework strings can exist transiently in memory; memory-erasure or hardware-backed Keystore guarantees are not claimed.

Normal sign-in has no configured service and never accepts a test key. `AuthProvider.authenticate` must consume/copy its borrowed key before returning; the controller clears its temporary buffer, ignores stale callbacks and cancels on destruction. A future real provider must implement its verified protocol off the UI thread, sanitize errors, and deliver only verified session results. Add actual network permission/configuration only with a supplied, authorized backend contract. No production authentication, device-limit enforcement or server logout has been implemented.

## Login verification and captures

```sh
python3 tools/check_app_screens.py
python3 app-ui/build-debug.py --sdk "$ANDROID_HOME"
adb install -r app-ui/build/manual/KennyPvtHax-UI-Suite-debug.apk
# Isolated emulator only: this intentionally clears the test app's data.
python3 app-ui/tests/phase2_device.py --allow-data-reset
```

The SDK-only script now explicitly marks its generated debug manifest debuggable, matching Gradle debug behavior; the source manifest does not enable debugging for release. A loading screenshot is produced through an explicit `auth_fixture=pending` debug intent and an actual pending controller request; this is not production authentication. Gallery/fixture intents are ignored in non-debuggable builds. Screenshot timing may use the existing `preview_time` mode while preserving real View rendering/input.

Localization uses complete `values`, `values-zh`, `values-hi`, `values-ar` strings. Language dialogs show endonyms and persist selection. Activity recreation applies locale/layout direction to real controls. RTL tests compare header placement, and native text selection remains LTR for license tokens. Debug APK UI captures verify font fallback, clipping and keyboard behavior; physical-device and speaker verification remain separate.
