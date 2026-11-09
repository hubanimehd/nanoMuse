---
name: ve-broll
description: Finds, watches and cuts B-roll in a vlog. Silent clips placed by time between talking clips, chosen by action and story, not fixed length.
version: "1.0.0"
---

# B-roll

A transcript-only cut silently drops every clip without words. Don't.

This skill is adapted from a video-editor skill pack written for DaVinci
Resolve Studio and talking-head vlogs. There is no Resolve on this phone, so
the craft rules below are kept and the Resolve build steps are replaced by
planning the cut on device and rendering with the phone editor or a relay.
Nothing uploads or publishes without the user's explicit approval of the
draft first. Never pay for anything.

## Spot it
- A clip with **0 words** is B-roll.
- A clip whose "words" are **song lyrics or another language** (radio, a cafe) is B-roll too: check what the transcript says before treating it as speech.
- All clips recorded in sequence: keep the recorded order; B-roll sits where it was shot.

## Watch it (the step people skip)
Pulling 5 frames per clip led to bad cuts. **Look at 2 frames per second** (a contact sheet, 8 per row, small) for every B-roll clip, and read what the creator says right before and after it. Questions:
- What is the action? Where does it start and resolve?
- Is there a natural transition? Example from a real edit: the camera goes *into* a backpack (frames go black) in one clip, and the next clip starts black and comes *out* of the bag onto an airport table. Cutting black-to-black makes a free, perfect transition; cutting by fixed length destroyed it.
- Which part is steady and well framed? Which part is out of focus or the operator fumbling?
- Is it vertical (phone held upright)? It will crop to the middle in a horizontal or 4:3 frame: flag it.

## Cut it
- Segment by time: `{"clip": "...", "broll": true, "in": s, "out": s, "why": "..."}`.
- Length follows the action: a quick cutaway 2 to 4 s, a transition sequence 6 to 8 s, an outro over a drive 10 to 15 s. A soft-focus shot gets the shortest slot.
- 100% framing, colour the timeline pieces green so A-roll and B-roll read at a glance.
- Natural sound stays on the voice track, but: a B-roll clip that is **silent** gets normalized up by +30 dB (pure hiss), so set it back to 0 dB; a clip with a **radio or music** in it fights the music bed, so lower it about 8 dB.
- The music rises on B-roll (see `ve-music-and-sound`).

## Report
Each B-roll: timeline time, what it shows, why that range, anything flagged (vertical, soft focus).

## Limits on this phone

- No DaVinci Resolve on the phone. The agent plans the B-roll segments
  (clip, in, out, why) and hands the plan to the phone editor or to a
  server or VM relay for the render.
- Building a contact sheet at 2 frames per second needs frame extraction
  from the video. If the on-device shell has no video tooling for that,
  extract frames through a relay and review the sheet on device.
- The phone editor may not support per-clip gain changes of +30 dB or
  minus 8 dB exactly. Apply the closest gain it offers, and note any
  difference in the report.
