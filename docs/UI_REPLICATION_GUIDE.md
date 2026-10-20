# Recreate this design

1. Read `shared/design-tokens/tokens.json`, not guessed red/black values. Preserve alpha independently of RGB.
2. Start #060609. Separate quiet graphite header, translucent burgundy surface and opaque elevated controls.
3. Use12-unit card/34-unit window radii and1-unit outline; adapt hit targets without enlarging every decorative stroke.
4. Buttons use primitive fills/seams, not texture images. Keep selectedInk #F4C8D0 for selection rather than coloring every label red.
5. Reproduce circular checks with geometry and a centered crisp mark. Preserve actual semantic input handling.
6. Use finite-delta exponential interpolation; update settings immediately, animate only presentation.
7. Port the original Aurora field, not a generic red gradient. Reuse the source-defined hash, curtain mesh and springs.
8. Put stars behind content; do not intercept touch from foreground controls. Layer background first and normal controls last.
9. Recover font bytes and check attribution. Compare real render output, not a designer's mockup.
10. Separate screenshot color/geometry evidence from numerical model tests. Test insets, font scaling, keyboard, small screens and reduced motion.
11. Profile CPU/GPU/frame allocations on Android; do not infer FPS from desktop models.

Actual implementations: `app-ui/app/src/main/java/com/kennypvthax/uisuite/`, original references under `menu-ui/jni/`. Build and capture commands are in app and root READMEs. Full-screen adaptation and remaining parity gaps are listed in DESIGN_SYSTEM.md. Future pages require the owner's video/source and verified functionality; do not invent backend behavior.

## Applying a real screen reference

Use docs/REFERENCE_VIDEO_ANALYSIS.md and its timestamped, cropped frames. Recreate observed organization first, then reuse KennyComponents, BrandMark, LicenseField and the shared Aurora instance. Keep localization in Android resource bundles and use relative layout parameters. Never recover authentication or file-management behavior from button labels: wire the UI to explicit interfaces, with honest unavailable states until real source/protocol evidence exists. Capture actual APK states with app-ui/tests/phase2_device.py; clearly label its debug-only pending-provider loading frame. Preserve the gallery as a separate debug regression route.

## Adding a dashboard card or page

1. Define immutable display data separately from actions (`GameCatalog` is the current example).
2. Compose existing `KennyComponents` paint and ordinary semantic Views; use 48dp or larger touch targets.
3. Own async work at the page shell, never inside `onDraw`; compare page generation before applying results.
4. Keep authentication and package/file actions behind explicit interfaces. Missing integration must show unavailable, never fabricated success.
5. Route verified sessions separately from explicitly labelled debug previews. Gate debug entry in the Activity using actual debuggability.
6. Keep one Aurora/SoundPool owner across local navigation and release both with Activity destruction.
7. Add all four string bundles, RTL checks, real emulator capture and a backend-boundary note.

Current preview: debug Login → Preview dashboard → scroll four cards → Log out. Production Login → verified provider result → dashboard is an available integration route only; no real authentication provider is configured.
