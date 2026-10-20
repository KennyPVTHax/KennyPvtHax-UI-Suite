# Motion specifications

Authority: KennyMotion.h, ClaudeTheme.h, KennyControls.h and KennyAurora.h.

| Motion | Native rate | 95% settling time |
|---|---:|---:|
| Hover |18|167ms|
| Tab selection |15|200ms|
| Check |22|136ms|
| Slider display |24|125ms|
| Touch position |14|214ms|
| Touch illumination rise |7|428ms|
| Illumination release |2.8|1070ms|

Interpolation a+(b−a)(1−exp(−rate×dt)); dt≤.1. App checkbox follows rate22; Android buttons use rate18 emphasis and circular checks use rate22. Lab navigation updates locally and is not claimed a faithful moving underline port. Aurora model is fixed120 Hz; reduced motion stops ambient simulation. Reference-based application loading now follows actual authentication controller state; the video blackout is not copied as an invented transition. See Aurora guide for geometry and timing formulas.

## Application page entrance

Login/dashboard route changes use a 180ms native View-property opacity entrance (0.72 → 1) over the same Aurora instance. Authentication state and touch input change immediately; this is no network timer or deliberate waiting period. Reduced Motion skips the entrance. Pause/destruction, deterministic capture mode and enabling Reduced Motion cancel the animator and restore full opacity. System animations may be disabled by the emulator runner, so these capture tests do not establish the visual timing on physical hardware.
