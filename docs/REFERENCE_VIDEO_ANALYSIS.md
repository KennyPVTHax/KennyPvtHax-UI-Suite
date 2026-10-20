# Observed application reference

Source: user-supplied split ZIP (ui.z01, ui.z02, ui.zip), containing `VID_20261020_181101_205.mp4`. All three supplied parts were required. The original raw DEFLATE stream was reconstructed in disk order and checked against the central-directory uncompressed length49,798,458 and CRC2049671634. The system ZIP conversion produced a bad intermediate; that intermediate is not a reference source.

Video:18.755333seconds,1080×2400portrait,H.264 at60000/1001fps, AAC audio. See shared/visual-reference/phase2/reference/inventory.json for hashes and exact capture times. Selected reference frames are cropped to remove system chrome; no original video or notification panel is included in the app.

## Full timeline and observed screens

- 0–~1s: English login. Small circular brand mark/name plus Local badge; full-width four-language selector; large glass branding panel; welcome headline; masked key input; Save key and Forget saved key buttons; Sign in button; existing error text `MAX DEVICE REACHED`.
- ~1–5.8s: language dialog, observed row order Arabic/English/Chinese/Hindi. English, Chinese, Hindi and Arabic UI states are shown. Arabic changes reading direction and header alignment.
- ~5.8–8s: Arabic login, key focus and Android/autofill interaction.
- ~8–12s: substantial recording blackout with pointer/paste context UI. This is consistent with a secure-input/capture restriction; its cause is unknown. It is **not evidence of an intentional black application loading screen**. The app's new loading indicator is driven by its authentication state instead.
- ~12.4–14s: dashboard appears after sign-in interaction. Repeated header/language/branding area, welcome/status, game editions, installed/not-installed labels, package IDs and two buttons per game.
- ~14–17s: dashboard scroll reveals all four game editions, footer information and logout control. Actual logout activation/server revocation is not observed.
- ~17.6–18.76s: system notification/quick-settings panel. This is unrelated Android chrome, not an application page.

Observed editions/package labels: GLOBAL `com.tencent.ig`, KOREA `com.pubg.krmobile`, TAIWAN `com.rekoo.pubgm`, VIETNAM `com.vng.pubgmobile`. Card actions are delete-file and launch-game labels. No actual file target, authorization protocol, successful file operation, API request or authentication implementation is recoverable from this recording.

## Implemented vs unknown

Checkpoint1 implements the actual login organization, with KennyPvtHax branding, source-derived crimson controls and the existing Aurora. Language translations are actual Android resources, not image replacements. Save/forget is local AndroidKeystore AES-GCM persistence, not proof of the reference application's storage design.

No original app source/API was attached. Normal sign-in therefore returns a real configuration-unavailable result and never accepts hardcoded credentials. A debug-only explicitly selected pending provider allows a genuine in-flight state to be held for UI tests without artificial timers. Loading screenshots are labelled development fixtures. Production validation, account/device limits and sessions remain unknown. In particular, the reference's MAX DEVICE REACHED error is not fabricated as a response from this app.

Dashboard implementation follows only after login verification. File modification logic will not be invented from a button label. Gallery remains separately accessible in debuggable builds through `--ez gallery true`. No menu-ui or live loader change is part of this phase.

## Implemented dashboard boundary

The app now follows the observed four-card order, package identifiers, welcome/status hierarchy, two action slots, scrolling footer and logout placement using the KennyPvtHax crimson components. Original third-party game images are replaced by neutral edition badges; no reuse rights or source images were supplied. Actual installed state is obtained through Android package APIs. Public launcher intents are supported for installed editions. The observed file deletion cannot be reproduced responsibly without its target/contract, so it remains visibly unavailable. Local development preview and Appearance & feedback access are explicitly labelled implementation extensions. No API, login success, device-limit enforcement or server revocation has been inferred from the recording.
