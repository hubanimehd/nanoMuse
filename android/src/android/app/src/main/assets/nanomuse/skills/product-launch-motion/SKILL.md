---
name: product-launch-motion
description: Direct and build a one-shape motion-design launch film from HTML with a cursor, morphing UI, springs and beat-synced music, rendered to MP4.
version: "1.0.0"
---

# Motion design launch videos

How to make launch films in the one-shape motion style: a single shape that never cuts, morphing through every scene, driven by a cursor, with springs, real data, logo slams, and music-first timing. The prompts themselves, verbatim and in order, are in `references/prompts.md`. Read them before starting; they are the spec. The code patterns are in `references/technique.md`.

Brand, palette and story always come from the user, or from a choice the user explicitly picks. Example brands and palettes in the original prompts are the original client's example only, never the default for this user.

## Standing rules

- Show the creative plan first, then a low-resolution draft for approval, and only then the full render. Nothing publishes or uploads without the user's draft approval.
- Music must be a licensed track the user supplies, or an original score. Never download a track, stock asset or brand asset without asking.
- Real numbers versus invented story details must be labelled separately in the report.
- Never pay for anything.

## The stance: you are the director

- **No constraints on creativity.** Make creative choices, log them as DECISIONs, and list them at the end so the user can overturn any in one reply.
- **Original, never a template, never a copy.** Do not lift the product website's copy, UI, stats or images wholesale. Invent the scenes. Anything that looks like a template is banned.
- **Launch-film energy.** Modern, futuristic, premium, the way top motion designers do it (Linear, Apple, Arc). Something happens on every beat; there is no dead time.
- **Attention to detail is the product.** Micro-interactions (button press, knob stretch, name pill lag, click ripple, odometer settle, sticker tilt) are what make it read as expensive.
- **Make it easy to follow.** One clear takeaway per scene, short prompts and answers, and enough settled time to read the result. Keep the beat alive through secondary motion and sound while the main idea holds; simplify the content or add beats rather than rushing the viewer.

## Non-negotiable motion rules

1. **One shape, never cut.** Every state is the same element morphing its size, radius and colour while its content swaps with a short blur. Cuts are not encouraged; transitions are morphs, camera moves, zooms into an element that becomes the next scene (a tile opens into an inbox, a reply card becomes the order, the cursor becomes the logo star).
2. **Content swaps with a short blur**, and text inside a morphing container gets its own enter and exit timing. Exit short and early, or old and new text overlap.
3. **Springs everywhere, a tiny overshoot at most.** No bouncy easing. Closed-form damped spring step responses only (see technique).
4. **Leading/trailing edges on different springs** so things stretch: tab indicators, toggle knobs, liquid pills. The leading edge runs ahead of the trailing one.
5. **A cursor drives every change with real clicks and drags.** Drags are direct manipulation: while held, the value is computed from the cursor position; on release it springs back from wherever it was. The product's agent can be its own cursor (a gradient multiplayer cursor with a name pill, a short motion trail, lean with velocity, press scale + ripple on clicks) handing off from the user's cursor ("You").
6. **The camera zooms so each state fills the frame.** Empty frames are a bug; frame the content, then push, whip or pull back. Pull-backs reveal scale (one email to a grid of hundreds; one post to a wall).
7. **Music-first timing.** Choose the soundtrack before choreographing: energetic, brand-appropriate, with a build, a clear drop and a satisfying ending. BPM alone is not energy; do not default to a repetitive synthesized loop. Use a licensed track or an original score with that musical arc. Aim for about 120 BPM, analyse the song for the beat grid, start on a downbeat, cut the song window so the drop lands on the product's big moment. Something happens on every beat; typing rides 32nds on a snare build; slams accelerate beats to 8ths to 16ths.
8. **Every UI sound is synthesized and placed by its measured peak** on the kick-peak grid. Treat sound design as seriously as the visuals: original, layered sound effects that make the film feel rich and alive. Be creative with it.
9. **Loops:** when the piece loops, the last frame is the first frame, cursor position and speed included, or the loop stutters.

## Look

- **Palette:** the brand's own design system. One clean UI font.
- **Banned (from the original brief, still binding unless the user overrides):** bouncy easing, particle bursts, glows, gradients on UI chrome, mismatched icon strokes, dead time, template looks.

## Content

- **Use real data.** Pull real, defensible numbers read-only (creators reached, gifts sent, posts/views tracked) and show them as rolling odometers. Invent the micro-story (creator, brand, messages) but keep the big numbers true, and say which is which in the report.
- **Customer logos one by one, boom boom boom.** Never a website-style "trusted by" row. One shape re-sized/re-coloured on every hit, only brands that really ran campaigns.
- **Licensed media only.** Stock video/stills from free-licence sources, checked per item. Never the product's own marketing images as a shortcut.
- **Keep what tested well:** keep strong beats between versions and iterate the rest.

## Process

1. **Ask for inputs first** (unless already given or the user says "you decide"): 8 to 12 states the shape should become, palette (black/white, one accent, or the brand system), and a royalty-free about 120 BPM song. Offer "Director's cut / Director picks" options.
2. **Direct first, implement second.** Finalize a concise creative plan before writing animation code: the product story and payoff, brand treatment, soundtrack and musical arc, then a beat-by-beat scene plan covering framing, key interactions, transitions and sound cues. Show the plan, resolve gaps as the director, and only then implement it. Give each scene a clear takeaway and reading window, and carry a recognizable customer example through the demo. Simplify real product behavior for film without inventing actions or outcomes.
3. **Build** one HTML file where every style is computed from time inside `seek(t)`. No CSS transitions, no timers, no state between frames.
4. **Render one frame per beat before the full render**, contact-sheet it, and fix anything off the grid, cramped, empty, hard to read or overlapping. Then check transitions at plus/minus 0.1 s. Review a low-resolution moving draft at normal speed before the full render: verify readable holds, cursor actions and continuous morphs. Still frames alone cannot prove the timing works.
5. **Full render** with Playwright subframes, ffmpeg tmix motion blur, 60 fps H.264 + mix.
6. **Verify the MP4 itself** with a 1-per-second storyboard and a frame-difference scan for seams and glitches. Review its pacing and transitions at normal speed, check for flicker, and verify audio cue sync against the picture. Listen when playback is available; distinguish listening from measurements. Report: the deliverable, concept, scene list, real-vs-invented data, DECISIONs, caveats (whether audio was heard or only measured; logo/likeness permissions).

## Gotchas that cost hours

- Never put `will-change` on anything the camera scales. Text renders blurry.
- Springs start at their event: a target that must *arrive* at time T is issued one settle-lead early.
- A `hide()` that zeroes opacity must be undone by the draw function, or elements stay invisible.
- Proportional digits jump in odometers: size each slot to its final glyph, clip only vertically.
- On a render machine disk can be tight (render in 4 s chunks and delete PNGs), Chromium is the first thing the out-of-memory killer takes (auto-retry, relaunch, hard-exit when done), a stray `/tmp/inspect.py` shadows the stdlib (run Python from the project dir).

## Output

Deliverables in a `deliver/` folder: the MP4, a 1-per-second storyboard, key stills, and the previous version for comparison.

## Reference files

- `references/prompts.md`: the original creative-direction prompts, verbatim and in order. Read before starting.
- `references/technique.md`: the code patterns (pure `seek(t)`, closed-form springs, Playwright subframes, ffmpeg tmix, beat grid).

## Limits on this phone

- Rendering needs Playwright Chromium, numpy (beat grid) and ffmpeg at render time. That is a server, relay or VM job, not an on-phone render. Say so before a first render installs anything.
- On the phone, direct the film, write the plan and the single HTML file, and review the low-resolution draft and the final MP4. Hand the render off to the server or VM.
