---
name: remotion-motion-graphics
description: Build kinetic-typography, logo and brand promo motion graphics as code with Remotion and ship an MP4, verified frame by frame.
version: "1.0.0"
---

# Motion graphics with Remotion

Code-driven video: every frame is a React render of `useCurrentFrame()`, so timing is exact, edits are cheap, and the same scenes can output any aspect ratio. The craft is in three places: **reading the source material well**, **a project skeleton that keeps timing and layout in one place**, and **a verification loop that looks at real frames**. This file is the workflow; the details live in `references/` and reusable code in `assets/` and `scripts/`.

## Standing rules

- Nothing publishes or uploads without the user's explicit approval of the draft first.
- Never pay for anything. Do not download assets from third-party hosts without asking.
- Hindi text: prefer a font that actually renders Devanagari (system Noto Sans Devanagari) and verify it on a rendered frame, not on the code alone.

## 0. Ground rules from Remotion (read before writing scenes)

`references/` holds the rule summaries. The non-negotiables:
- All motion derives from `useCurrentFrame()`; write times in seconds x fps. CSS transitions/animations and Tailwind animation classes do not render.
- Entrances: `interpolate` + `Easing.bezier(0.16, 1, 0.3, 1)` (or `spring` with damping 10-15 when you want a visible bounce). Exits accelerate (`Easing.in`). Clamp everything.
- Typewriter is string slicing, never per-character opacity.
- `<Sequence premountFor>` / `<Series.Sequence premountFor>` so fonts and images are ready; `<Img>` (never `<img>`); `random("seed")` (never `Math.random`).
- The full official rule files (animations, timing, text-animations, sequencing, transitions, compositions, fonts, images, charts, audio, sfx, videos, light-leaks) are NOT bundled here; the essentials above are folded into this file. If a topic needs the full rules, fetch them from the Remotion docs on the render machine.

## 1. Read the brief and the material (before any code)

1. **Reference clip** (if given): break it into contact sheets and view every sheet. Write a beat map: for each 0.5-2 s beat note background (light/dark), what enters, how (rise, spring, wipe, type), what leaves, and the transition. This map becomes `SCENES` in theme.ts with one scene component per beat. Match its rhythm; you are cloning motion, not content.
2. **Brand**: fetch the site for the tagline, product names, real numbers, and font, and the site CSS for colour tokens. If the site is neutral, take colours from the logo SVG fills. Real copy beats invented copy; when you must invent lines, say so at the end.
3. **Artwork**: `node scripts/svg_to_paths.mjs --report logo.svg` prints every path's index, fill, position and size. Decide which indices are the wordmark letters, the character, the eyes, etc. (letters are usually same-fill, mid-sized, arranged along an arc or line; the big outline path is the body). Then `node scripts/svg_to_paths.mjs --out src/logo/paths.ts name=logo.svg`. Sort letter indices in reading order (along an arc: by angle around a point below the arc). Verify splits with a Remotion still.
4. Decide orientation(s) and length. Default 1080x1920 at 30 fps for social; also produce 1920x1080 when asked for "horizontal". The layout hook makes that a second Composition, not a second codebase.
5. Do not download assets from third-party hosts without asking; styled React cards with the real course/product names look on-brand and need no permission.

## 2. Scaffold and skeleton

```bash
npx create-video@latest --yes --blank --no-tailwind <name>
cd <name> && npm install && npx remotion add @remotion/google-fonts
```

Copy from `assets/` into `src/`:
- `theme.template.ts` to `theme.ts`: colours (`C`), `PORTRAIT`/`LANDSCAPE`, `SCENES` (named durations in frames, playback order), `TOTAL`.
- `layout.ts`: `useLayout()` gives `{W,H,cx,cy,wide,diag,scale}` derived from `useVideoConfig()`. Every position in every scene comes from here (`cx - 218`, `H * 0.615`, `wide ? ... : ...`), never from literal 1080/1920. The wide version is designed at 1600x900 and scaled 1.2x in `Main`.
- `anim.ts`: `EASE_OUT/IN/INOUT/POP`, `prog(frame,start,dur,easing)`, `springAt(frame,fps,start,{damping,stiffness})`, `riseStyle`, `typed`.
- `fonts.ts`, `Main.template.tsx` to `Main.tsx` (Series + design-scale wrapper), `Root.template.tsx` to `Root.tsx` (two compositions).
- `components/Backgrounds.tsx` (LightBg with brand glow, DarkBg with glow), `components/Cursors.tsx` (arrow + dot), `components/Words.tsx` (word-by-word reveal with optional exit), `components/SvgArt.tsx` (animate SVG artwork per path: `parts` with `popEach` for letters, `characterStyle()` for bounce/bob/sway, `fadeLeft` for edge-cropped art).
Delete the scaffold's `Composition.tsx`. Keep inline styles.

One file per scene (`scenes/S01Hook.tsx` and so on), each an `<AbsoluteFill>` with its own background so hard cuts are intentional. Local frames start at 0 in every scene; timings live at the top of the file as small constants.

## 3. Write the scenes

`references/scene-patterns.md` is a catalogue of 16 patterns with timings that worked (phrase swap, pill to button to click, flying collage, circle wipe, ticker + panel wipe, shape hand-off between scenes, route drawing, UI toggle moment, gradient statement, character + letter pop, bubble rows, chart card, spotlight, burst to lockup, fade out). Pick the ones the beat map calls for; reuse the shapes rather than inventing new mechanics per scene.

Guidelines that keep it looking designed:
- Few colours: ink, white, one brand accent, one secondary. Light scenes share one `LightBg`, dark scenes one `DarkBg`, so cuts do not flash.
- Type: headline 58-80 px, labels 28 px or more at 1080 wide; weight 600-700; letter-spacing -0.5 to -1.2 on big lines.
- Stagger words 5-7 frames; entrances 12-16 frames; exits 8-10 frames; a beat holds 20 frames or more after its last element lands.
- Hand elements between scenes (pill to marker, button position to next scene's start) through shared helpers so the cut is invisible.
- Characters: spring in from below (damping 12), then bob + 2-3 degree sway; pop wordmark letters 2 frames apart with damping 10.
- Anything positional uses fractions of W/H or offsets from cx/cy, with a `wide` branch when a tall element must sit beside its text.

## 4. Verify like an editor, not a compiler

`references/verification-and-gotchas.md` has the loop and the traps. Short form: `npx tsc --noEmit`, then a low-res render plus contact sheets, view every sheet, still-render suspicious frames at 0.5, fix, repeat. Only then `npx remotion render <CompId> out/final.mp4 --codec=h264 --crf=18`, and pull a frame or two from the final file as proof. When adding a second orientation, re-check the first one's sheet to prove nothing moved.

## 5. Deliver

- Deliver the MP4 file itself, one per meaningful version.
- In the summary: what each scene does in one line, which copy lines were invented, what was not done (no music, no downloaded photos), and the one-line re-render command.

## Files

- `assets/`: theme, layout hook, anim helpers, fonts, Main/Root templates, Backgrounds, Cursors, Words, SvgArt (copy into `src/`).
- `scripts/svg_to_paths.mjs`: SVG to TS path data with bbox report (pure Node standard library, runs on the phone).
- `references/scene-patterns.md`: the pattern catalogue with timings.
- `references/verification-and-gotchas.md`: check loop and traps.

## Limits on this phone

- Remotion rendering needs Node, npm packages, a browser download and ffmpeg. That is a server, relay or VM job, not an on-phone render. The first real render is the test: if the render machine needs a browser download or an npm install, say so before running it. On the phone, author the project and verify the returned stills and MP4s.
- Remotion Studio runs only on the render machine and the phone user cannot open it. Deliver the MP4 file, with the re-render command in the summary.
- The ffmpeg-based helper scripts from the original (`analyze_reference.sh`, `check_render.sh`, `still_frame.sh`) are not bundled here; they need ffmpeg and Remotion on the render machine. Only `svg_to_paths.mjs` is bundled, because it is pure Node with no extra dependencies.
- The full official Remotion rule files are not bundled here (they were large). The essentials are in section 0 above; fetch the full rules from the Remotion docs when a topic needs them.
