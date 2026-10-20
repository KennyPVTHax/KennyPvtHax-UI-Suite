# Asset provenance

Menu assets, SDK, static archives and upstream notices are preserved exactly from the owner's public Backup snapshot; see menu-ui/assets/PUBLIC_MEDIA_AUTHORIZATION.md and its inventories. The owner's recorded media authorization does not grant a new blanket license to upstream third-party dependencies.

App assets:

- `menu.ttf`: byte-extracted from `menu-ui/jni/ANOY/CustomFont.h`. Font metadata identifies **Segoe UI Regular**. This is the actual existing menu font, not a generated substitute. No separate font redistribution grant has been recovered; retaining the original reference does not establish an independent font license for other applications. Review applicable Microsoft font embedding terms before redistributing standalone app builds beyond the owner's project. No new permissive license is asserted.
- `res/raw/tap.wav`: original45ms, monoPCM16/48kHz synthesized decaying1150Hz tap, created for this suite. No sampled or character dialogue.
- No Anime video, artwork, launcher photograph or voice recordings are copied into app-ui. Those remain in the preserved menu only.

Do not infer clearance merely from a public GitHub URL. Retain upstream notices and obtain any further rights required for your distribution context.
