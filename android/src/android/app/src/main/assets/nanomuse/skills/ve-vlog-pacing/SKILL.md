---
name: ve-vlog-pacing
description: Numbers and rules that make a transcript-based cut feel natural. Silence detection, word pads, pause shortening, room at scene edges, zoom.
version: "1.0.0"
---

# Pacing: how much air to leave

A transcript cut is precise; the danger is that it is *too* precise. Every cut becomes a jump cut and the video "pops". These values came from real feedback.

This skill is adapted from a video-editor skill pack written for DaVinci
Resolve Studio and talking-head vlogs. There is no Resolve on this phone, so
the numbers below are kept as the plan and applied by the phone editor or a
render relay. Nothing uploads or publishes without the user's explicit
approval of the draft first. Never pay for anything.

## Cut from the transcript, never from volume
A volume-only silence cut (silence detection on the whole file) gave about 800 choppy pieces on a 34-minute class. Cutting from the word timestamps gave 120 pieces the creator loved. Use the waveform only to *refine* edges.

## Planner algorithm (in words)
1. **Silence map** per clip: below **-43 dB** for at least **0.25 s**.
2. **Pause candidates** inside kept speech: gap between two words 0.25 s or more **and** 85% or more of it is silence on the waveform (an untranscribed sound is not a pause).
3. **Shorten** only pauses longer than `max_pause`, down to `pause_keep`. Split the leftover air 40% before / 60% after.
4. **Word edges:** start = word start minus **0.08 s**, end = word end plus **0.12 s**, refined to the nearest silence edge within 0.25 / 0.3 s so soft consonants aren't clipped; never reach into a word that was cut.
5. **Scene edges:** the first range of a clip gets **0.5 s** before the first word, the last range **0.6 s** after the last word (only *spoken* words limit this; a lighter click or ambience may stay). Override per range in the keep-list (`lead_in`, `tail`). Look at frames first: stop before the creator reaches for the camera.
6. Snap to frames (start floors, end ceils, end exclusive), remove overlaps, merge touching pieces.
7. Write a review file: the transcript with cut words struck through plus a timeline list with times.

## Settings that worked

| Content | max_pause | pause_keep | Feel |
|---|---|---|---|
| Class / tutorial | 0.5 s | 0.3 s | tight, loved |
| Vlog | **1.0 s** | **0.6 s** | breathing room; 0.5/0.3 felt "popping" |

Rough check with a pause report ("what if max_pause = ...: N cuts, saves X s") before choosing. On a dramatic slow opening, 0.5 s gave 7 jump cuts in 20 seconds; 1.0 s gave 3 in 53 seconds.

## Framing (zoom)
- Every **jump cut inside one scene** alternates **100% / 107.5%**, so it reads as a camera change.
- A **new scene** (new clip, or after B-roll) always opens at **100%**. Toggling zoom on scene changes adds pops.
- **Punch-in 115%** only for the punchline itself: split the piece at the line's first word and again right after its last word. Without the second split one punch-in held 83 seconds.
- B-roll is always 100%.

## When they say "it's choppy"
Measure first (how many cuts come from pause shortening vs. scene edges vs. zoom), then fix the actual cause: usually loosen pauses, add scene room, and stop zooming on scene changes. Don't guess.

## Limits on this phone

- Silence mapping, waveform edge refinement and frame snapping need audio
  and video tooling such as ffmpeg. If the on-device shell cannot do
  them, the agent computes the plan from word timestamps alone and hands
  the waveform refinement and the render to a server or VM relay.
- Zoom alternation (100% / 107.5%) and the 115% punch-in need an editor
  that supports keyframed scale. If the phone editor cannot do them, note
  that in the report and keep the zoom plan in the cut files for the relay.
