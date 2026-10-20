# Foundation verification

Baseline menu commit: `aa3411056ccd747787c80078c52165b80020482c`.

Completed locally:

- All 3,460 original tracked files copied.3,459 preserve Git blob identity (source-defined CRLF checkout rules are respected); only menu README gains a guide prefix. All native source, 40 static archives and 16 embedded resources are retained.
- All 16 original host suites passed with ASan/UBSan, including37 control binding/range fixture assertions, menu/input, launcher, audio, theme, Aurora, Electric, characters, diagnostics, presentation, embedding, supplied media and audio recovery.
- Original C++ and Java Aurora model:1,575 sample outputs at 3 seconds matched exactly at printed9decimal precision (maximum absolute difference0). Deterministic initialization,30/120 Hz stepping, attraction, release, bounds, reduced motion, NaN handling and stall clamp passed.
- Relocated native clean ARM64 build: NDK25.2.9519653, ELF64AArch64,21,085,376bytes, SHA256 `0b57661096b079174e8000341642b389b42bc82f9554b83495453f53fb4bafd1`. All 16 payloads verified at actual ELF offsets. This is a relocation build, not a newly device-tested release.
- Android app compiles through javac/d8/aapt2 and passes APKv2/v3 signature checks. Build script uses official AndroidSDK35/build-tools35.0.0/JDK17.
- `shared/visual-reference/menu-aurora.png` is actual native menu ImGui draw data, CPU-rasterized with its real atlas. It is not an Android screenshot. Its host harness has no supplied logo texture, so original K fallback can appear in that reference.

Current evidence: Android screenshot capture and app interaction checks passed in the accelerated GitHub emulator, detailed below. No physical-device appearance, GPU performance, speaker playback, TalkBack navigation or final visual parity is asserted. The app contains documented geometry/rasterization adaptations; numeric parity is not pixel parity. The original foundation preceded the reference video; Phase 2 now implements its observed pages, tracked in REFERENCE_VIDEO_ANALYSIS.md.

Evidence: source-verification.json, menu-host-regressions.json, menu-embedded-verification.json. Logs/build intermediates are excluded from public source. Original Backup repository and live Anilxkenny manifest are not modified by suite work.

## GitHub and real emulator evidence

The new public repository is `KennyPVTHax/KennyPvtHax-UI-Suite`. Original Backup main was verified at `aa3411056ccd747787c80078c52165b80020482c`. This task made no push to Backup or Anilxkenny. The final read-only Anilxkenny check returned `da8b94a4e982e5816e4cbce460c9ec4f6a9e37e7`; external changes are outside this suite integration, so no claim that its branch stayed at the earlier deployment SHA is made.

Native workflow [39429193438](https://github.com/KennyPVTHax/KennyPvtHax-UI-Suite/actions/runs/39429193438) passed its host suites and ARM64 build from suite source 08cc359. Its downloaded library is 21,083,968 bytes, SHA256 `4902367b2adc1e245a55013b42cd6dbf4ba4bf7c998d41df40c9f556d186fca0`; all 16 payloads were independently checked again after download. Different build metadata/toolchain environment can change the whole-file hash without changing embedded source assets.

Android workflow39430301136 passed APK compilation, installation, launch, real button/checkbox/slider interaction and preference persistence in an accelerated Android 30 x86_64 emulator. Initial screenshot inspection exposed collapsed slider measurement and checkbox alignment; both were fixed, captured again, and inspected. A subsequent popup refinement uses the original opaque elevated surface to avoid foreground/background text bleed. Final artifact provenance records its exact source/run separately.

The local unaccelerated emulator could not finish boot reliably in the available capture window; no app failure is inferred from that. Actual capture evidence comes from the successful accelerated CI emulator. Four genuine Android captures and a native t=3 host capture are composed by tools/package_visual_reference.py; the comparison sheet is a labelled montage, not an artificial UI.

App uses Android installed sans-serif because the recovered Microsoft Segoe UI font carries restricted embedding terms. Font identity, geometry adaptation, Canvas blending, switch shape and navigation indicator behavior are explicit parity limitations. No pixel-perfect claim, Android speaker audibility claim, physical-device GPU/FPS claim or finished business application claim is made.

Final app verification: [run39431048298](https://github.com/KennyPVTHax/KennyPvtHax-UI-Suite/actions/runs/39431048298), source `acb6044ceebfb21f64f47dd6ff4ef506e8d07f30`, both build and real-emulator jobs passed. Downloaded APK is49,616bytes, SHA256 `689994feb644c6fb7e68467947ad0aac40ba4eb7dbabe929d900459fb3904ff8`; APKv2/v3 signature and ZIP integrity checks passed. No font/OGG/video/nativeSO is present in its payload. It contains only the original tap sample among application audio/media resources. The final documentation/evidence commit does not change its application source; per-file identities are recorded in build-provenance.json.

Final screenshots were individually inspected: the slider is visible and accepts input, checkbox labels align, card/control labels remain readable, and the popup uses an opaque source-derived elevated color. The comparison remains qualitative across different viewports; no measured pixel-distance threshold or exhaustive accessibility/device matrix has been passed.

Fresh remote clone successfully ran the source/40-library/token/model verification and SDK-only APK compilation/signing. Exact Gradle execution was not tested; the supported SDK-only route and GitHub workflow were tested. To regenerate native t=3reference: run menu regressions with `--label suite-relocation`, then from root run `python3 tools/capture_menu_reference.py`. After actual APK capture run `python3 tools/package_visual_reference.py` (Pillow and Linux DejaVu Sans label font required).
