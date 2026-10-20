# Recovered KennyPvtHax design system

Baseline: `aa3411056ccd747787c80078c52165b80020482c`. Authority: `menu-ui/jni/ClaudeTheme.h`, `KennyControls.h`, `KennyMenu.h`, `KennyPaint.h`, `KennyMotion.h`, `KennyAurora.h`. The [machine-readable tokens](../shared/design-tokens/tokens.json) retain provenance. These are extracted values, not approximated palette choices.

## Palette

| Token | RGB |
|---|---|
| bg | `#060609` |
| surface | `#100B0F` |
| surfaceElev | `#1A1014` |
| surfaceSubtle | `#241A1F` |
| outline | `#665059` |
| textPrimary | `#F6F2F3` |
| textSecondary | `#ADA3A8` |
| textTertiary | `#867579` |
| accent | `#C1122B` |
| glow | `#FF5664` |
| accentPressed | `#EA233B` |
| onAccent | `#060609` |
| accentSubtle | `#30141E` |
| selectedInk | `#F4C8D0` |
| error | `#E5776A` |
| success | `#86B096` |
| warning | `#D2A24C` |

Alpha matters: base .97, surface .72, outline .42, accentSubtle .85. Elevated surface is opaque; window opacity preference clamps85–100%. Frame hover #32191F; active #442029. Primary button accent changes to #EA233B under pointer emphasis. Secondary button is transparent with1px outline. Tab selected text #F4C8D0, inactive #ADA3A8. Pill base #100B0E alpha.6; hover #39212B alpha.9; selected #30141E alpha.85. Pills fade a #F4C8D0 seam at alpha.18. `ClaudeTheme.h` has no button texture image: depth comes from primitive colors, alpha, seams and vertex interpolation. Do not invent textured assets.

## Geometry and hierarchy

Native units before per-window scaling: button radius10, field8, card12, window34; pad16, row gap8, section24, nav40. Frame padding12×7, inner spacing6×6, outline1, scrollbar8, grab minimum12. `KennyMenu::Measure`: scale=min(viewportHeight/872,width/1440), clamped.55–1.5; padding26×scale; window height82% viewport. These window proportions are preserved in menu, intentionally replaced with inset-aware full-screen scrolling in app.

Circular checkbox radius=.46×size; state rate22; bright check on crimson disc with muted ring, partial top arc highlight. Slider track thin, radius=max(7,10×scale); visual rate24 but native input snaps while active. App uses a7dp handle and48dp input row as a documented accessibility adaptation. Native panel child regions are transparent; the app adapts them into translucent raised cards so full-screen content remains readable.

## Type

The exact menu font bytes are `jni/ANOY/CustomFont.h`, baked32px for the menu with per-window scale. Default overlay font16px; Font Awesome glyphs merge at30px in menu. The app uses Android installed sans-serif because the recovered Segoe UI font has restricted embedding terms; Android sp text sizing is (title26, section17, control15, helper12–14). No claim of identical font hinting, line-height, weight or fallback glyph rasterization across ImGui and Android. Font name/license metadata remains upstream; do not infer a new license from extraction. Android system font scaling is honored.

## Motion and lighting

`KennyMotion::Step`: a+(b−a)(1−exp(−rate×clamp(dt,0,.1))). Hover18, tab15, check22, slider24. 95% settling times are167,200,136,125ms respectively; these are calculated exponential settling times, not hard durations. Native cache192entries. Menu discs use32 cached angular vertices; seam uses transparent endpoints, no expensive blur. Aurora and star formulas are described in [AURORA_ENGINE.md](AURORA_ENGINE.md).

## Adaptations requiring visual review

Android Canvas meshes blend/rasterize differently from ImGui triangles. Android full-screen portrait composition cannot preserve the landscape floating menu's exact spatial ratios. Touch targets increase to48dp. Native hover has pressed/focus Android equivalents; buttons use rate18 pressed/focus emphasis with Android semantic input handling. The platform switch is a themed semantic adaptation; native original uses circular checks. This foundation is not asserted pixel-perfect. Evidence must separate numeric parity, host renders, emulator captures and real-device captures.

Token-versus-call-site distinction: RadiusBtn10 is a declared theme token; the original SecondaryButton helper inherits RadiusInput8 unless a caller overrides rounding. The app's10dp button radius follows the declared button token and is an explicit adaptation. Native selected-indicator movement and original full typography are not yet exact Android equivalents. The verification tool checks both token JSON and Java RGB constants against the authoritative source.
