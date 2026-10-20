# Living Crimson Aurora

Original: `menu-ui/jni/KennyAurora.h`; hash/noise dependency `KennyEffects.h`. Port: `app-ui/.../AuroraModel.java` and `AuroraBackground.java`.

## Model retained

40 stars,3layers,96columns×18rows. Hash uses uint32 xor shifts16/15/16 and multipliers0x7feb352d/0x846ca68b; Java logical shift and integer overflow preserve those bits. Unit=(hash&65535)/65535. Noise smoothstep-interpolates seeded adjacent cells. Field=.57noise(x+t)+.28noise(2.17x−.73t)+.15noise(4.31x+.37t), with seed increments71/131.

Curtain layer seed137+313layer; drift=t(.105+.031layer). Sweep samples4.6x, folds14.3x and rays47.7x. Lower hem=.67+.10layer+.28(sweep−.5)+.035(fold−.5); height=.27+.31field(5.1x,.64drift). Lean=.115(field(3.7x,.43drift)−.5). Illumination=.16+.28fold+.56ray², modulated by slow multiscale noise. Mesh y interpolates lower−height(1−v); x adds lean(1−v)². Fine hem Gaussian centered.86,width.115; smooth fades remove hard sheet edges. Palette #75142C,#B01938,#D12B43→#F35B73. These are flowing noise-advected curtains, not substituted radial blooms or sine strips.

## Stars and interaction

Seeded homes occupy.09–.91 normalized interior plus tiny continuous noise drift. Spring3.5 toward ambient home, touch attraction22 within radius.38 normalized height-space, dampingexp(−4.8dt), aspect-aware speed limit.28, bounds.065–.935. Touch response rate7, release2.8; input smoothing14. State advances at1/120s with12steps maximum after dt clamp.1. No wrap/teleport/respawn on release. Star radii.55–1.4, luminosity.25–.73; twinkle=.57+.43field(i×3.3,t×.55), rare shimmer noise(t×.28)^12.

## Renderer and lifecycle

Native uses rounded-shell projected vertices and ImGui clip rectangles. Android uses Canvas rounded-path clipping and cached vertex/triangle arrays, with three original meshes. Android portrait shelf positions are adapted; the numeric curtain and particle model is retained and cross-language checked. Canvas clipping replaces native projected shell geometry near corners, so edge triangles are not claimed identical. Android scales halos with density; native scales with menu scale.

Reduced motion freezes time/particles and clears touch response; background callbacks stop. Activity pause removes Choreographer callbacks, clears input/time baseline; resume does not catch up hidden time. No threads, textures, media decoding or shaders are required. Arrays are allocated once; Canvas/driver allocation behavior is not asserted zero. API hardware behavior and cost require device profiling.

## Porting guidance

For Compose, maintain the same model in remember, draw cached mesh-equivalent paths on Canvas and drive with lifecycle-aware frame time. This is guidance only: no Compose implementation exists here. For C++ reuse original presentation headers without the game integration. For another Canvas system retain unsigned hash semantics, double time, exact fixed stepping and normalized aspect-aware attraction.

Run `python3 tools/verify_suite.py` from root to compare seeded homes and all291 curtain samples against C++. Use actual device screenshots before claiming output parity.
