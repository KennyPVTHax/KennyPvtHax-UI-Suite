# KennyPvtHax UI Suite

Two independent projects preserve the completed native menu and adapt its presentation into an ordinary Android application. The original Backup repository and live loader are unchanged.

- `menu-ui/`: complete tracked snapshot of Backup commit `aa3411056ccd747787c80078c52165b80020482c`, including SDK, forty static libraries, three themes and sixteen embedded resources. Original development history remains reachable. Read its build and licensing documentation before reuse.
- `app-ui/`: standalone Java/Android Views app with the reference-based login screen, four-language selection, encrypted local key storage, and preserved developer component gallery. Aurora only; no Anime media or character recordings. Production authentication is not configured.
- `shared/`: measured tokens, motion specification and genuine render evidence.
- `docs/`: design extraction, portability and future reuse guides.

```sh
git clone https://github.com/KennyPVTHax/KennyPvtHax-UI-Suite.git
cd KennyPvtHax-UI-Suite
# JDK 17+, Python 3, g++, zlib/OpenSSL/Pillow for menu tests
python3 tools/verify_suite.py
cd app-ui
python3 build-debug.py --sdk "$ANDROID_HOME"
adb install -r build/manual/KennyPvtHax-UI-Suite-debug.apk
```

Alternatively use Gradle 8.9 with `gradle -p app-ui :app:assembleDebug`; no wrapper is claimed or required by the SDK-only build. Android SDK platform 35/build-tools 35.0.0, JDK17+, min Android26. For Windows use PowerShell `$env:ANDROID_HOME` or pass the SDK path explicitly.

Menu build: from `menu-ui`, run the NDK r25c `ndk-build NDK_PROJECT_PATH=. APP_BUILD_SCRIPT=jni/Android.mk NDK_APPLICATION_MK=jni/Application.mk -j2`. Output is `libs/arm64-v8a/libAnoy.so`, not an APK. It needs the existing Android host; this suite invents no loader/install mechanism.

Read [app guide](app-ui/README.md), [menu guide](menu-ui/README.md), [design system](docs/DESIGN_SYSTEM.md), and [replication guide](docs/UI_REPLICATION_GUIDE.md). The supplied application video has been analyzed in docs/REFERENCE_VIDEO_ANALYSIS.md. Actual screens are implemented incrementally from those observations; the gallery remains a separate testing tool. New APK and relocated library require device verification; compilation does not establish runtime parity.

Actual [gallery screenshot](shared/visual-reference/app-gallery.png), [controls](shared/visual-reference/app-controls.png), [motion settings](shared/visual-reference/app-motion.png), and [native/app comparison](shared/visual-reference/menu-app-comparison.png) are available individually. Download `shared/visual-reference/index.html` for an offline self-contained viewer. See [verification](docs/VERIFICATION.md) and [asset/font provenance](docs/ASSET_PROVENANCE.md). The original Segoe UI font is not separately distributed in the app; Android sans-serif is an explicit typography limitation.


## Reference application — Phase 2

The normal launch is now the KennyPvtHax login page. Save/forget uses Android Keystore AES-GCM in app-private storage. English, Chinese, Hindi and Arabic are actual resource bundles; Arabic mirrors layout. No production API was supplied, so Sign in reports configuration unavailable rather than accepting fabricated credentials.

```sh
python3 tools/check_app_screens.py
# Emulator only: explicitly clears the test application's data.
python3 app-ui/tests/phase2_device.py --allow-data-reset
# Preserved component gallery in a debuggable build:
adb shell am start -n com.kennypvthax.uisuite/.MainActivity --ez gallery true
```

Selected source frames: shared/visual-reference/phase2/reference/. New real-emulator captures: shared/visual-reference/phase2/app/ as checkpoints pass. Loading capture uses an explicit debug-only pending request fixture, not a simulated successful login or artificial production delay. See app-ui/README.md for backend boundaries and secure-storage details.
