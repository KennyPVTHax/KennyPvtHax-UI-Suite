# KennyPvtHax UI Suite

Two independent projects preserve the completed native menu and adapt its presentation into an ordinary Android application. The original Backup repository and live loader are unchanged.

- `menu-ui/`: complete tracked snapshot of Backup commit `aa3411056ccd747787c80078c52165b80020482c`, including SDK, forty static libraries, three themes and sixteen embedded resources. Original development history remains reachable. Read its build and licensing documentation before reuse.
- `app-ui/`: standalone Java/Android Canvas component gallery. Aurora only; no game integration, Anime media, character recordings, networking or authentication.
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

Read [app guide](app-ui/README.md), [menu guide](menu-ui/README.md), [design system](docs/DESIGN_SYSTEM.md), and [replication guide](docs/UI_REPLICATION_GUIDE.md). Add pages only after the owner's application reference video is supplied. The gallery is a testing tool, not a speculative app workflow. New APK and relocated library require device verification; compilation does not establish runtime parity.
