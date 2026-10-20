# Future project template

Copy the independent app component sources and source-derived token JSON, subject to upstream font/licensing terms. Do not copy native runtime integration or character media into an unrelated app.

Integration checklist:

- Record source commit and token provenance.
- Preserve near-black base, graphite/burgundy hierarchy, restrained accents and alpha.
- Match10/8/12-unit control/field/card radii; use accessible hit targets.
- Reuse AuroraModel, lifecycle-aware background, fixed-capacity arrays and bounded deltas.
- Use KennyComponents button/panel/check/slider/field; keep state/business logic outside painting.
- Configure tap sound independently, default restrained volume, no voice/media dependency.
- Supply real page video/source before implementing navigation/business behavior.
- Run numerical/interaction checks and capture actual APK output.
- Profile small/large viewports, keyboard, font scale and reduced motion.
- Document every visual adaptation and unverified device behavior.

Copyable Codex instruction:

> Read this suite's DESIGN_SYSTEM.md and AURORA_ENGINE.md, inspect the actual sources, and reuse its independent Android presentation components. Preserve source-derived colors, alpha, geometry and motion. Do not import game SDK/hooks, Anime media or voices. Inspect my app's supplied video/source before adding pages. Keep existing backend/state behavior, run tests, capture actual screenshots, and report evidence and limitations.

From the suite root run `python3 tools/verify_suite.py`; from app-ui run `python3 build-debug.py --sdk "$ANDROID_HOME"`. Add real workflow-specific tests when actual pages are supplied; do not make static specimens appear to perform remote work.
