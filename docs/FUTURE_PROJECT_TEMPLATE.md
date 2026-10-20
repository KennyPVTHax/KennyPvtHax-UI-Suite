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

## Reference-based login template

Use MainActivity/AppShell as a presentation example, AuthController/AuthProvider as a backend boundary and CredentialVault for app-owned encrypted local key persistence. Copying this UI does not supply a production authentication service. Supply the real protocol and test credentials separately; never hardcode accepted keys. Keep actual translations, relative RTL layouts, lifecycle cancellation and the original Aurora. Preserve masked native text editing and avoid saving secrets into View state. Use explicit development-only preview/fixtures when backend-dependent states must be inspected; never expose them as verified production behavior.

## Copyable page integration checklist

Copy `AppShell`, `GameCard` composition, `AuthController`/`AuthProvider` boundary and the original token/component/Aurora classes into a Java 17 Android Views module. Rename package/resources consistently. Keep app-private encrypted credentials only if your real backend needs them; implement its verified protocol before enabling production access. Replace the immutable catalog with authorized project data and implement real actions through `GameActions` or a new page-specific interface. Never copy a debug preview into a production authentication success path.

Build: `python3 app-ui/build-debug.py --sdk "$ANDROID_HOME"`. Host checks: `python3 tools/check_app_screens.py` and `python3 tools/verify_suite.py`. Isolated emulator: `python3 app-ui/tests/phase2_device.py --allow-data-reset --dashboard`. Capture and inspect the actual translated pages, keyboard, cancellation and navigation. The original menu source is not an Android authentication or game-management backend.
