# Reference application verification

## Checkpoint 1 — login

Tested source: `b721631ad4fea6163b897a3e9e0ca8384bd8adb5`.
Android CI: https://github.com/KennyPVTHax/KennyPvtHax-UI-Suite/actions/runs/39443047208

The debug APK compiled and its actual Views/Canvas UI ran on an accelerated API 30 x86_64 emulator. The existing gallery interaction tests and its four capture states also passed. Login tests verified masked input, AES-GCM ciphertext with no plaintext test key, save/restore across process death, explicit forget, an unconfigured-authentication error, four translated locales, Arabic mirrored header, locale persistence, and cancellation of a real pending development-provider request. The local Java state-machine tests and the original menu/token/Aurora parity checks passed.

Actual captures: `shared/visual-reference/phase2/app/`. Reference-video crops: `shared/visual-reference/phase2/reference/`. Arabic, language dialog and loading captures were visually inspected after correcting bidirectional alignment, password-field typography and loading-slot stability. These are emulator screenshots, not drawn mockups. The visible `local-ui-fixture` is an unaccepted test string, not a credential.

No authentication API was provided. Normal Sign in cannot authenticate any key. The loading capture uses an explicitly selected debug-only pending provider; no artificial timer or successful production response is invented. No physical-device audio, GPU performance or production authentication was verified. Original `menu-ui/` is unchanged from the supplied foundation.

## Checkpoint 2 — dashboard and final login correction

Tested source: `14cf927dbd42d0fa13ca03e3afb6d50538f23cc6`.
Successful build and emulator run: https://github.com/KennyPVTHax/KennyPvtHax-UI-Suite/actions/runs/39445549575
Both jobs passed. The complete earlier dashboard run also passed at `feff297` in run 39445272952.

The final run repeated all login/gallery tests and verified all four observed package/card entries through scrolling, eight disabled unavailable actions on a clean emulator, explicit debug preview, logout/Back, Arabic dashboard recreation, pause/resume, and persisted Reduced Motion/tap-sound preferences. A second 360×640dp viewport retained reachable login and dashboard controls through scrolling. The smaller viewport and keyboard captures were visually inspected. Initial/scrolled dashboard captures, Arabic text/layout, elevated preference dialog, focus border, actual password dots and loading/error slots were inspected without evident overlapping content or unreadable labels. Viewport edges can naturally cut scroll content; its controls remain reachable.

Visual review found that Android `setSingleLine` had overridden the password transformation. The final source explicitly applies `PasswordTransformationMethod` after that call. Final focused/loading screenshots confirm masking; the earlier checkpoint's input-type flag test alone did not establish visual masking. Password selection/IME remain native Android behavior.

Final image inventory, byte sizes, SHA-256 and CI provenance: `shared/visual-reference/phase2/app/capture-provenance.json`. Seven requested states: login-default, login-focused, login-language-dialog, login-loading, login-error, dashboard-initial and dashboard-scrolled. Additional captures cover all login locales, Arabic dashboard, preferences and compact layouts. `shared/visual-reference/phase2/index.html` is an offline viewer when downloaded with its app/reference subfolders. Its contact sheet uses the actual captured pixels, resized only for the overview.

Host verification passed: 1,575 original C++/Java numeric samples with maximum absolute difference 0, deterministic model initialization, touch/release, bounds, 30/120Hz parity, stall/NaN handling, Reduced Motion, four complete resource bundles, controller cancellation/late callbacks/duplicate suppression, key-buffer ownership, development-preview distinction and catalog allowlist. Original tests verify 40 static libraries and original menu file coverage; an additional Git diff confirms the entire `menu-ui/` tree is unchanged from suite foundation `9492ce18a31e89365201c2bb531f77bf60005ebd`, including all 37 bindings/ranges. Credential-pattern scan passed for staged changes; it is not a mathematical guarantee that every possible secret is absent.

### Verified boundaries and remaining tests

- Normal Sign in returns configuration-unavailable, with no accepted credentials or invented server response. Production authentication/session revocation await an actual protocol/source. Backend checkpoint 3 has not begun.
- Dashboard preview exists only when the Activity is actually debuggable. Release entry has no preview button and ignores debug fixtures.
- Package installation inspection and ordinary Android launcher intents are implemented; CI has no real PUBG editions installed, so successful real-game launching remains a device test. No game file modification is implemented. File action stays disabled with an on-page explanation.
- Source-derived tokens, component geometry and Aurora simulation are retained. Portrait composition, Android sans-serif instead of the restricted original font, platform keyboard and neutral edition badges are documented differences. No pixel-perfect parity is claimed.
- Aurora model checks and emulator lifecycle tests passed. Physical-device frame time, thermal behavior, accessibility/font-scale coverage, keyboard variations and speaker audibility remain unverified. Emulator runs disable system animations and cannot establish device FPS or sound output.
- No `menu-ui/`, Anilxkenny, original native loading, hooks, SDK or gameplay changes were made. App media is the existing tap sample only; no Anime content or voices were added.
