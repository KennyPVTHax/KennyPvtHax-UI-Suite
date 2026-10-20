# Component library

| Component | Menu authority | Android implementation |
|---|---|---|
| Primary/secondary buttons | ClaudeTheme::PrimaryButton/SecondaryButton | KennyComponents.button, state-list dark/crimson surfaces |
| Glass panel | KennyPanels and transparent child regions | KennyComponents.panel,12dp corners |
| Circular check | KennyControls::Checkbox | KennyComponents.Check, semantic CompoundButton |
| Slider | KennyControls::SliderFloat/SliderInt | KennyComponents.Slider, semantic SeekBar |
| Header | KennyIdentity::Draw | Gallery wordmark with Android installed sans-serif (documented font-rights gap) |
| Navigation | Claude::NavItem/PillTab | Gallery component/motion lab navigation |
| Field/dialog | Native ImGui field/popup tokens | KennyComponents.field / Gallery local Dialog |
| Aurora | KennyAurora::State/Draw | AuroraModel/AuroraBackground |
| Tap feedback | Native audio is not imported | TapFeedback, single cached synthesized sample |

Public component factory example is in app-ui/README. Android defaults provide semantic input behavior, not stock button visuals. Original menu uses no photographic button texture. Android switch geometry, press transition timing and full-screen spacing remain documented adaptations. Do not mislabel the gallery as a completed application workflow.

Actual emulator inspection found and corrected a collapsed slider measurement and misaligned checkbox label. The custom SeekBar now explicitly measures48dp, and checks center text vertically. Check discs cache the original #241B21→#0C0B0F inactive and #D72A45→#A40F29 checked gradients when their size changes; transitions crossfade those cached materials without allocating shaders every frame.
