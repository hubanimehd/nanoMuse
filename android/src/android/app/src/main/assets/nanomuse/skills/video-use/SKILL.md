---
name: video-use
description: Edit any video by conversation: transcribe, cut, color grade, generate overlay animations, and burn subtitles. Use when the user wants a video edited from a transcript and a plain-English plan.
version: "1.0.0"
---

# Video Use

## Standing rules

- Never touch the cut until the user has approved the strategy in plain English.
- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything, and never upgrade any service.

## Principle

1. **Reason from raw transcript plus on-demand visuals.** The only derived artifact that earns its keep is a packed phrase-level transcript (`takes_packed.md`). Everything else, filler tagging, retake detection, shot classification, emphasis scoring, you derive at decision time.
2. **Audio is primary, visuals follow.** Cut candidates come from speech boundaries and silence gaps. Drill into visuals only at decision points.
3. **Ask, confirm, execute, iterate, persist.** Never touch the cut until the user has confirmed the strategy in plain English.
4. **Generalize.** Do not assume what kind of video this is. Look at the material, ask the user, then edit.
5. **Artistic freedom is the default.** Every specific value, preset, font, color, duration, pitch structure, and technique in this document is a worked example from one proven video, not a mandate. Read them to understand what is possible and why each worked. Then make your own taste calls based on what the material actually is and what the user actually wants. **The only things you MUST do are in the Hard Rules section below.** Everything else is yours.
6. **Invent freely.** If the material calls for a technique not described here, split-screen, picture-in-picture, lower-third identity cards, reaction cuts, speed ramps, freeze frames, crossfades, match cuts, L-cuts, J-cuts, speed ramps over breath, whatever, build it if the tools on the phone can do it. Do not wait for permission.
7. **Verify your own output before showing it to the user.** If you would not ship it, do not present it.

## Hard Rules (production correctness, non-negotiable)

These are the things where deviation produces silent failures or broken output. They are not taste, they are correctness. Memorize them.

1. **Subtitles are applied LAST in the filter chain**, after every overlay. Otherwise overlays hide captions. Silent failure.
2. **Per-segment extract, then lossless `-c copy` concat**, not single-pass filtergraph. Otherwise you double-encode every segment when overlays are added.
3. **30ms audio fades at every segment boundary** (`afade=t=in:st=0:d=0.03,afade=t=out:st={dur-0.03}:d=0.03`). Otherwise audible pops at every cut.
4. **Overlays use `setpts=PTS-STARTPTS+T/TB`** to shift the overlay's frame 0 to its window start. Otherwise you see the middle of the animation during the overlay window.
5. **Master SRT uses output-timeline offsets**: `output_time = word.start - segment_start + segment_offset`. Otherwise captions misalign after segment concat.
6. **Never cut inside a word.** Snap every cut edge to a word boundary from the transcript.
7. **Pad every cut edge.** Working window: 30 to 200ms. Transcript timestamps drift 50 to 100ms, padding absorbs the drift. Tighter for fast-paced, looser for cinematic.
8. **Word-level verbatim ASR only.** Never SRT or phrase mode (loses sub-second gap data). Never normalized fillers (loses editorial signal).
9. **Cache transcripts per source.** Never re-transcribe unless the source file itself changed.
10. **Parallel sub-agents for multiple animations.** Never sequential. Spawn them at once; total wall time is about the slowest one.
11. **Strategy confirmation before execution.** Never touch the cut until the user has approved the plain-English plan.
12. **All session outputs in `<videos_dir>/edit/`.** Never write inside the skill directory.

Everything else in this document is a worked example. Deviate whenever the material calls for it.

## Directory layout

User footage lives wherever they put it. All session outputs go into `<videos_dir>/edit/`, using workspace-relative paths on this phone (never absolute VM paths).

```
<videos_dir>/
├── <source files, untouched>
└── edit/
    ├── project.md               ← memory; appended every session
    ├── takes_packed.md          ← phrase-level transcripts, the primary reading view
    ├── edl.json                 ← cut decisions
    ├── transcripts/<name>.json  ← cached raw transcript JSON
    ├── animations/slot_<id>/    ← per-animation source + render + reasoning
    ├── clips_graded/            ← per-segment extracts with grade + fades
    ├── master.srt               ← output-timeline subtitles
    ├── downloads/               ← downloaded source videos
    ├── verify/                  ← debug frames / timeline PNGs
    ├── preview.mp4
    └── final.mp4
```

## Setup

On cold start, verify:

- A transcription route is available. Word-level verbatim transcripts are required (Hard Rule 8). If the only route is a cloud transcription service, it needs the user's API key, collected through the secure flow, never pasted in chat. Never invent a key and never spend credits without the user's explicit yes.
- `ffmpeg` plus `ffprobe` on PATH in the on-device Linux shell.
- Python available in the sandbox for the bundled helper.
- Node.js plus npm available only if the session needs web-based animation slots. Install extra animation tools (HyperFrames, Remotion, Manim, yt-dlp) only on first use, and only if the phone sandbox can run them.

## Helpers

Only one helper ships with this phone skill, because the rest need tools the phone does not have (see Limits on this phone):

- **`pack_transcripts.py --edit-dir <dir>`**: `transcripts/*.json` to `takes_packed.md` (phrase-level, break on silence of 0.5s or more). Pure standard-library Python, bundled beside this file. Run it with the on-device shell, for example `python3 pack_transcripts.py --edit-dir <videos_dir>/edit`.

Transcription, rendering, grading, and timeline views are done with whatever the phone sandbox provides (ffmpeg and ffprobe), or are handed to a server or VM (see Limits).

## The process

1. **Inventory.** `ffprobe` every source. Transcribe the directory. Run `pack_transcripts.py` to produce `takes_packed.md`. Sample one or two frames for a visual first impression.
2. **Pre-scan for problems.** One pass over `takes_packed.md` to note verbal slips, obvious mis-speaks, or phrasings to avoid. Plain list, feed into the editor brief.
3. **Converse.** Describe what you see in plain English. Ask questions shaped by the material. Collect: content type, target length and aspect, aesthetic and brand direction, pacing feel, must-preserve moments, must-cut moments, animation and grade preferences, subtitle needs. Do not use a fixed checklist, the right questions are different every time.
4. **Propose strategy.** 4 to 8 sentences: shape, take choices, cut direction, animation plan, grade direction, subtitle style, length estimate. **Wait for confirmation.**
5. **Execute.** Produce `edl.json`. Drill into frames at ambiguous moments. Build animations in parallel sub-agents. Apply grade per-segment. Compose the render.
6. **Preview.** Render a 720p fast preview.
7. **Self-eval (before showing the user).** Check the rendered output (not the sources) at every cut boundary (plus or minus 1.5s window). Check each image for:
   - Visual discontinuity, flash, or jump at the cut
   - Waveform spike at the boundary (audio pop that slipped past the 30ms fade)
   - Subtitle hidden behind an overlay (Rule 1 violation)
   - Overlay misaligned or showing wrong frames (Rule 4 violation)

   Also sample: first 2s, last 2s, and 2 to 3 mid-points. Check grade consistency, subtitle readability, overall coherence. Run `ffprobe` on the output to verify duration matches the EDL expectation.

   Measure the audio, do not assume it: `ffmpeg -i out.mp4 -af ebur128=peak=true -f null -` for integrated loudness and true peak, plus RMS per section (dialogue, music-only, end card). An end card 15 dB under the dialogue, or effects louder than speech, is a bug. You cannot listen: say so, and report the numbers.

   For anything the user will publish (launch, promo, ad), also run one **critic pass** with the rendered file, the EDL, and any reference videos the user gave. Brief it to roast, not to praise: a verdict, ranked problems with timecodes and evidence (frames, levels), and the 5 fixes to do first. Fresh eyes catch what the author stopped seeing: cut-off payoff lines, 0.5s memes, unreadable 28px text at phone size.

   If anything fails: fix, re-render, re-eval. **Cap at 3 self-eval passes.** If issues remain after 3, flag them to the user rather than looping forever. Only present the preview once the self-eval passes.
8. **Iterate and persist.** Natural-language feedback, re-plan, re-render. Never re-transcribe. Final render on confirmation. Append to `project.md`.

## Cut craft (techniques)

- **Audio-first.** Candidate cuts from word boundaries and silence gaps.
- **Preserve peaks.** Laughs, punchlines, emphasis beats. Extend past punchlines to include reactions. The laugh IS the beat.
- **Speaker handoffs** benefit from air between utterances. Common values: 400 to 600ms. Less for fast-paced, more for cinematic. Taste call.
- **Audio events as signals.** `(laughs)`, `(sighs)`, `(applause)` mark beats. Extend past them.
- **Silence gaps are cut candidates.** Silences of 400ms or more are usually the cleanest. 150 to 400ms phrase boundaries are usable with a visual check. Under 150ms is unsafe (mid-phrase).
- **Example cut padding** (the launch video shipped with this): 50ms before the first kept word, 80ms after the last. Tighter for montage energy, looser for documentary. Stay in the 30 to 200ms working window (Hard Rule 7).
- **Never reason audio and video independently.** Every cut must work on both tracks.

## The packed transcript (primary reading view)

`pack_transcripts.py` reads all `transcripts/*.json` and produces one markdown file where each take is a list of phrase-level lines, each prefixed with its `[start-end]` time range. Phrases break on any silence of 0.5s or more OR speaker change. This is the artifact the editor reads to pick cuts. It gives word-boundary precision from text alone at a tenth the tokens of raw JSON.

Example line:

```
## C0103  (duration: 43.0s, 8 phrases)
  [002.52-005.36] S0 Ninety percent of what a web agent does is completely wasted.
  [006.08-006.74] S0 We fixed this.
```

## Editor brief (for multi-take selection)

When the task is "pick the best take of each beat across many clips," use a brief shaped like this. The structure is load-bearing; the pitch-shape example is not.

```
You are editing a <type> video. Pick the best take of each beat and
assemble them chronologically by beat, not by source clip order.

INPUTS:
  - takes_packed.md (time-annotated phrase-level transcripts of all takes)
  - Product/narrative context: <2 sentences from the user>
  - Speaker(s): <name, role, delivery style note>
  - Expected structure: <pick an archetype or invent one>
  - Verbal slips to avoid: <list from the pre-scan pass>
  - Target runtime: <seconds>

Common structural archetypes (pick, adapt, or invent):
  - Tech launch / demo:   HOOK, PROBLEM, SOLUTION, BENEFIT, EXAMPLE, CTA
  - Tutorial:             INTRO, SETUP, STEPS, GOTCHAS, RECAP
  - Interview:            (QUESTION, ANSWER, FOLLOWUP) repeat
  - Travel / event:       ARRIVAL, HIGHLIGHTS, QUIET MOMENTS, DEPARTURE
  - Documentary:          THESIS, EVIDENCE, COUNTERPOINT, CONCLUSION
  - Music / performance:  INTRO, VERSE, CHORUS, BRIDGE, OUTRO
  - Or invent your own.

RULES:
  - Start/end times must fall on word boundaries from the transcript.
  - Pad cut boundaries (working window 30 to 200ms).
  - Prefer silences of 400ms or more as cut targets.
  - Unavoidable slips are kept if no better take exists. Note them in "reason".
  - If over budget, revise: drop a beat or trim tails. Report total and self-correct.

OUTPUT (JSON array, no prose):
  [{"source": "C0103", "start": 2.42, "end": 6.85, "beat": "HOOK",
    "quote": "...", "reason": "..."}, ...]

Return the final EDL and a one-line total runtime check.
```

## Color grade (when requested)

Your job is to **reason about the image**, not apply a preset. Look at a frame, decide what is wrong, adjust one thing, look again.

Mental model is ASC CDL. Per channel: `out = (in * slope + offset) ** power`, then global saturation. `slope` is highlights, `offset` is shadows, `power` is midtones.

**Example filter chains** (use them as starting points or mix your own):

- **`warm_cinematic`**: retro/technical, subtle teal/orange split, desaturated. Shipped in a real launch video. Safe for talking heads.
- **`neutral_punch`**: minimal corrective: contrast bump plus gentle S-curve. No hue shifts.
- **`none`**: straight copy. Default when the user has not asked.

For anything else, portraiture, nature, product, music video, documentary, invent your own chain. Any raw ffmpeg filter string is accepted.

Hard rules: apply **per-segment during extraction** (not post-concat, which re-encodes twice). Never go aggressive without testing skin tones.

## Subtitles (when requested)

Subtitles have three dimensions worth reasoning about: **chunking** (1, 2, 3, or sentence per line), **case** (UPPER, Title, Natural), and **placement** (margin from bottom). The right combo depends on content.

**Worked styles**, pick, adapt, or invent:

**`bold-overlay`**: short-form tech launch, fast-paced social. About 2-word chunks, UPPERCASE, break on punctuation and pauses of 0.3s or more, grow to 3 words rather than flash a cue under 0.35s, Helvetica 18 Bold, white-on-outline, `MarginV=35`.

```
FontName=Helvetica,FontSize=18,Bold=1,
PrimaryColour=&H00FFFFFF,OutlineColour=&H00000000,BackColour=&H00000000,
BorderStyle=1,Outline=2,Shadow=0,
Alignment=2,MarginV=35
```

**`natural-sentence`**: narrative, documentary, education. 4 to 7 word chunks, sentence case, break on natural pauses, `MarginV=60 to 80`, larger font for readability, slightly wider max-width. No shipped style, design one if you need it.

Invent a third style if neither fits. Hard rules: subtitles LAST (Rule 1), output-timeline offsets (Rule 5).

## Animations (when requested)

Animations match the content and the brand. **Get the palette, font, and visual language from the conversation**, never assume a default. If the user has not told you, propose a palette in the strategy phase and wait for confirmation before building anything.

**Tool options:**

Pick the engine per animation slot. Do not default to one engine just because the animation is web-adjacent.

- **Web compositions (HTML, CSS, GSAP)**: product UI motion, website-to-video or mockup-to-video captures, kinetic typography, landing-page or storyboard promos, data-driven UI states, transparent WebM overlays, and clips that need deterministic frame capture. Best when the animation should be authored and verified like a web composition.
- **React compositions**: component state, reusable primitives, or an existing React brand system. Best when the user specifically asks for React or when React composition is the simpler authoring model.
- **Manim**: formal diagrams, state machines, equation derivations, graph morphs.
- **PIL plus PNG sequence plus ffmpeg**: simple overlay cards: counters, typewriter text, single bar reveals, progressive draws. Fast to iterate, any aesthetic you want.

None is mandatory. Invent hybrids if useful.

**Duration rules of thumb, context-dependent:**

- **Sync-to-narration explanations.** A viewer needs to parse the content at 1x. Rough floor 3s, typical 5 to 7s for simple cards, 8 to 14s for complex diagrams.
- **Beat-synced accents** (music video, fast montage). 0.5 to 2s is fine, they are visual accents, not information. The "readable at 1x" rule becomes "recognizable at 1x", not "fully parseable."
- **Hold the final frame for 1s or more** before the cut (universal).
- **Over voiceover:** total duration is at least `narration_length + 1s` (universal).
- **Never parallel-reveal independent elements.** The eye cannot track two new things at once. One thing, pause, next thing.

**Animation payoff timing (rule for sync-to-narration):** get the payoff word's timestamp. Start the overlay `reveal_duration` seconds earlier so the landing frame coincides with the spoken payoff word. Without this sync the animation feels disconnected.

**Easing** (universal, never `linear`, it looks robotic):

```python
def ease_out_cubic(t):    return 1 - (1 - t) ** 3
def ease_in_out_cubic(t):
    if t < 0.5: return 4 * t ** 3
    return 1 - (-2 * t + 2) ** 3 / 2
```

`ease_out_cubic` for single reveals (slow landing). `ease_in_out_cubic` for continuous draws.

**Typing text anchor trick:** center on the FULL string's width, not the partial-string width, otherwise text slides left during reveal.

**Example palette** (the launch video, one aesthetic among infinite):
- Background `(10, 10, 10)` near-black
- Accent `#FF5A00` / `(255, 90, 0)` orange
- Labels `(110, 110, 110)` dim gray
- Font: Menlo Bold
- At most 2 accent colors, about 40% empty space, minimal chrome
- Result: terminal / retro tech feel

This is one style. If the brand is warm and serif, use that. If it is colorful and playful, use that. If the user handed you a style guide, follow it. If they did not, propose one and confirm.

**Fonts fail silently.** A web font that did not load renders in a fallback face with no error, the video ships in "almost Arial". Await the font load and then assert it before rendering. With PIL, pass an explicit font path; never rely on the default.

**Worked example, "show the edit" hook** (a launch video for this tool). Instead of a title card, the first 3s visualize the editing itself: each transcript word pops in on its timestamp, with bars under it drawn from the real audio envelope; a filler ("ummm") grows letter by letter while it is spoken, turns orange and is cut out on screen at the same frame the audio cuts, and the next line lands immediately. It works because the picture is driven by the same data as the sound. Use the idea whenever the story is "we removed something": make the removal visible.

**Parallel sub-agent brief**, each animation is one sub-agent. Each prompt is self-contained (sub-agents have no parent context). Include:

1. One-sentence goal: "Build ONE animation: [spec]. Nothing else."
2. Absolute output path (`<edit>/animations/slot_<id>/render.mp4`, workspace-relative on this phone)
3. Exact technical spec: resolution, fps, codec, pix_fmt, CRF, duration
4. Style palette as concrete values (RGB tuples, hex, or reference to a design system)
5. Font path with index
6. Frame-by-frame timeline (what happens when, with easing)
7. Anti-list ("no chrome, no extras, no titles unless specified")
8. Code pattern reference (copy helpers inline, do not import across slots)
9. Deliverable checklist (script, render, verify duration via ffprobe, report)
10. "Do not ask questions. If anything is ambiguous, pick the most obvious interpretation and proceed."

One sub-agent is one file (unique filenames, parallel agents do not overwrite each other).

## Music and sound effects (when requested)

Sound is where generated videos sound cheap. Worked rules from launch edits:

- **Fewer effects.** Every effect is tied to something visible (a cut, a landing, a click). About 20 stock whooshes, risers, or impacts in 18s reads as generic; about 8 reads as designed.
- **Hit on the frame.** Most effects have an attack (silence or a build before the transient). Measure it (first sample above about -30 dBFS of the peak) and start the file `attack` seconds before the visible contact frame.
- **Duck music under speech** (roughly -12 to -15 dB relative to its music-only level), and ramp it out before a stinger or end card instead of letting its own tail decay under your CTA.
- **Master once:** mix to PCM, then two-pass loudnorm (-14 LUFS, true peak at most -1 dBTP) on the final mix. Then measure per section (see Self-eval).
- **Music taste is the user's call.** Generated music defaults to "hype". Offer two contrasting beds and let the user listen. Do not claim a mix sounds good, you can only measure it.

## Output spec

Match the source unless the user asked for something specific. Common targets: `1920x1080@24` cinematic, `1920x1080@30` screen content, `1080x1920@30` vertical social, `3840x2160@24` 4K cinema, `1080x1080@30` square. Default the scale to 1080p from any source; ask the user which delivery format matters.

## EDL format

```json
{
  "version": 1,
  "sources": {"C0103": "videos/C0103.MP4", "C0108": "videos/C0108.MP4"},
  "ranges": [
    {"source": "C0103", "start": 2.42, "end": 6.85,
     "beat": "HOOK", "quote": "...", "reason": "Cleanest delivery, stops before slip at 38.46."},
    {"source": "C0108", "start": 14.30, "end": 28.90,
     "beat": "SOLUTION", "quote": "...", "reason": "Only take without the false start."}
  ],
  "grade": "warm_cinematic",
  "overlays": [
    {"file": "edit/animations/slot_1/render.mp4", "start_in_output": 0.0, "duration": 5.0}
  ],
  "subtitles": "edit/master.srt",
  "total_duration_s": 87.4
}
```

`grade` is a preset name or raw ffmpeg filter. `overlays` are rendered animation clips. `subtitles` is optional and applied LAST.

## Memory, `project.md`

Append one section per session at `<edit>/project.md`:

```markdown
## Session N, YYYY-MM-DD

**Strategy:** one paragraph describing the approach
**Decisions:** take choices, cuts, grades, animations + why
**Reasoning log:** one-line rationale for non-obvious decisions
**Outstanding:** deferred items
```

On startup, read `project.md` if it exists and summarize the last session in one sentence before asking whether to continue.

## Anti-patterns

Things that consistently fail regardless of style:

- **Hierarchical pre-computed codec formats** with usability, tone tags, or shot layers. Over-engineering. Derive from the transcript at decision time.
- **Hand-tuned moment-scoring functions.** The agent picks better than any heuristic you will write.
- **Phrase-level ASR output.** Loses sub-second gap data. Always word-level verbatim.
- **Running ASR locally on CPU.** Slow and it normalizes fillers. Use a hosted transcription service.
- **Burning subtitles into base before compositing overlays.** Overlays hide them. (Hard Rule 1.)
- **Single-pass filtergraph when you have overlays.** Double re-encodes. Use per-segment extract, then concat.
- **Linear animation easing.** Looks robotic. Always cubic.
- **Unverified web fonts.** A failed load silently falls back to a system face. Assert the font loaded before rendering.
- **Stock SFX on every transition.** Tie each effect to a visible event; cap the count.
- **Hard audio cuts at segment boundaries.** Audible pops. (Hard Rule 3.)
- **Typing text centered on the partial string.** Text slides left as it grows.
- **Sequential sub-agents for multiple animations.** Always parallel.
- **Editing before confirming the strategy.** Never.
- **Re-transcribing cached sources.** Immutable outputs of immutable inputs.
- **Assuming what kind of video it is.** Look first, ask second, edit last.

## Limits on this phone

- **Transcription needs a cloud service.** The original helpers called a hosted word-level transcription API with a user API key. That helper is not bundled here. On this phone, transcription needs either an on-device speech-to-text tool the sandbox provides, or a server, relay, or VM that runs it. Do not claim a transcript exists until one was actually produced, and never paste or store an API key in chat. Collect keys only through the secure flow.
- **Rendering is ffmpeg-heavy.** Per-segment extract, concat, overlays, grading, and loudnorm all run through ffmpeg in the on-device Linux shell. If ffmpeg is not installed in the sandbox, no render can run on the phone and the job needs a server, relay, or VM. Long or 4K renders may be slow on the phone; prefer 720p previews for approval.
- **Animation engines are mostly unavailable on the phone.** Web composition tools, React renderers, and Manim need Node.js, browsers, or desktop installs the phone sandbox may not have. PIL plus PNG sequence plus ffmpeg is the most phone-friendly route. If an animation engine is missing, say so and offer the simpler route or a server render.
- **Bundled helper is limited to one script.** Only `pack_transcripts.py` (pure standard-library Python) ships with this skill. The transcribe, render, grade, and timeline-view helpers from the original skill are omitted because they depend on a cloud transcription API, ffmpeg pipelines, or image libraries that are not guaranteed on the phone.
- **No Manim slot is bundled.** The original vendored a Manim sub-skill with reference files. It is omitted here because Manim is not installed on the phone; a Manim render needs a server or VM.
