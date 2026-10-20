# Android porting boundaries

The app is Java17/Canvas with ordinary Android Activities and Views, not an injected native menu. It links no SDK, hook library, audio engine, character registry or existing native renderer. The completed menu remains separate and byte-preserved. AGP8.7.3/Gradle8.9 is an optional IDE/CI build route; SDK-only build-debug.py is also supplied and uses JDK17+ and official build-tools35.

Custom geometry wraps semantic Button/CompoundButton/SeekBar/EditText rather than replacing accessibility/input behavior with a giant opaque canvas. Text uses sp, layout dp; Android system insets and ScrollView adapt portrait/landscape. Window background is dark, no overlay permissions. No network/auth/API integration is claimed.

Model state is separate from rendering. Lifecycle removes animation callbacks and sound pool releases in destruction. Configuration changes recreate the gallery; local preferences survive. Aurora time intentionally restarts on Activity recreation rather than serializing every particle. Background pause/resume preserves the same View model without catch-up. This is a documented continuity limitation, not a persistence claim for particle state.

Tap sound is an original45ms synthesized UI sample, cached SoundPool/maxStreams1, low volume and65ms cooldown. No character audio. Java/framework resource failures should be checked on device; no Android speaker test is implied by a host unit test. Future production audio focus/mute policies can be added when the app's real workflow is supplied.

See app-ui/README for build/install/debug commands and docs/VERIFICATION.md for actual results.
