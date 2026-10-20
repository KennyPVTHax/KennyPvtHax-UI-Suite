# Foundation verification

Baseline menu commit: `aa3411056ccd747787c80078c52165b80020482c`.

Completed locally:

- All3460 original tracked files copied.3459 preserve Git blob identity (source-defined CRLF checkout rules are respected); only menu README gains a guide prefix. All native source,40static archives and16embedded resources are retained.
- All16 original host suites passed with ASan/UBSan, including37control binding/range fixture assertions, menu/input, launcher, audio, theme, Aurora, Electric, characters, diagnostics, presentation, embedding, supplied media and audio recovery.
- Original C++ and Java Aurora model:1575sample outputs at3seconds matched exactly at printed9decimal precision (maximum absolute difference0). Deterministic initialization,30/120Hz stepping, attraction, release, bounds, reduced motion, NaN handling and stall clamp passed.
- Relocated native clean ARM64 build: NDK25.2.9519653, ELF64AArch64,21,085,376bytes, SHA256 `0b57661096b079174e8000341642b389b42bc82f9554b83495453f53fb4bafd1`. All16payloads verified at actual ELF offsets. This is a relocation build, not a newly device-tested release.
- Android app compiles through javac/d8/aapt2 and passes APKv2/v3 signature checks. Build script uses official AndroidSDK35/build-tools35.0.0/JDK17.
- `shared/visual-reference/menu-aurora.png` is actual native menu ImGui draw data, CPU-rasterized with its real atlas. It is not an Android screenshot. Its host harness has no supplied logo texture, so original K fallback can appear in that reference.

Pending/limited: Android screenshot capture and app interaction checks are attempted with a software-only emulator. No physical-device appearance, GPU performance, speaker playback, TalkBack navigation or final visual parity is asserted. The app contains documented geometry/rasterization adaptations; numeric parity is not pixel parity. Application pages await the owner's reference video.

Evidence: source-verification.json, menu-host-regressions.json, menu-embedded-verification.json. Logs/build intermediates are excluded from public source. Original Backup repository and live Anilxkenny manifest are not modified by suite work.
