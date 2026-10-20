# Reference application verification

## Checkpoint 1 — login

Tested source: `b721631ad4fea6163b897a3e9e0ca8384bd8adb5`.
Android CI: https://github.com/KennyPVTHax/KennyPvtHax-UI-Suite/actions/runs/39443047208

The debug APK compiled and its actual Views/Canvas UI ran on an accelerated API 30 x86_64 emulator. The existing gallery interaction tests and its four capture states also passed. Login tests verified masked input, AES-GCM ciphertext with no plaintext test key, save/restore across process death, explicit forget, an unconfigured-authentication error, four translated locales, Arabic mirrored header, locale persistence, and cancellation of a real pending development-provider request. The local Java state-machine tests and the original menu/token/Aurora parity checks passed.

Actual captures: `shared/visual-reference/phase2/app/`. Reference-video crops: `shared/visual-reference/phase2/reference/`. Arabic, language dialog and loading captures were visually inspected after correcting bidirectional alignment, password-field typography and loading-slot stability. These are emulator screenshots, not drawn mockups. The visible `local-ui-fixture` is an unaccepted test string, not a credential.

No authentication API was provided. Normal Sign in cannot authenticate any key. The loading capture uses an explicitly selected debug-only pending provider; no artificial timer or successful production response is invented. No physical-device audio, GPU performance or production authentication was verified. Original `menu-ui/` is unchanged from the supplied foundation.
