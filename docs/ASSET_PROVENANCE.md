# Asset provenance

Menu assets, SDK, static archives and upstream notices are preserved exactly from the owner's public Backup snapshot; see menu-ui/assets/PUBLIC_MEDIA_AUTHORIZATION.md and its inventories. The owner's recorded media authorization does not grant a new blanket license to upstream third-party dependencies.

App assets:

- Original font recovered from `menu-ui/jni/ANOY/CustomFont.h`: **Microsoft Segoe UI Regular**, copyright2022Microsoft, OS/2fsType8, embedded license restricts use to specified Microsoft product/content terms and permitted embedding. No standalone-app redistribution grant was established. The separately extracted font was removed from app-ui; its current default is Android's installed sans-serif. Original menu bytes/history are retained, not relicensed. This known typography gap requires a separately permitted matching font before claiming exact type parity. Initial suite commit e44e010 included an extracted copy; the following normal commit removes it without rewriting history. Do not treat that historical copy as cleared for reuse.
- `res/raw/tap.wav`: original45ms, monoPCM16/48kHz synthesized decaying1150Hz tap, created for this suite. No sampled or character dialogue.
- No Anime video, artwork, launcher photograph or voice recordings are copied into app-ui. Those remain in the preserved menu only.

Do not infer clearance merely from a public GitHub URL. Retain upstream notices and obtain any further rights required for your distribution context.
