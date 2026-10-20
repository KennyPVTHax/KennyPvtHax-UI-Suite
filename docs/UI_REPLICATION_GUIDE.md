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
