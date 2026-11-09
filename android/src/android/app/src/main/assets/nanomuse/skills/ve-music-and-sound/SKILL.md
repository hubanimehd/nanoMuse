---
name: ve-music-and-sound
description: Scores a vlog with user-supplied music. One cue per section, a ducking envelope baked into one bed, and tasteful transition sound.
version: "1.0.0"
---

# Music and sound design

Only when asked. The creator doesn't want a list to choose from: **pick, place, and name two alternates per cue** in one line each so they can say "swap to the second one".

This skill is adapted from a video-editor skill pack written for DaVinci
Resolve Studio and talking-head vlogs. There is no Resolve on this phone,
and no music library service is connected, so music comes only from files
the user already has or licensed files already on the device. Never fetch
music from a subscription service, never pay for anything, and never use a
track the user does not have the right to use. Nothing uploads or publishes
without the user's explicit approval of the draft first.

## 1. Read the timeline as it is now
The creator has usually trimmed the cut. Read the B-roll windows from the
current timeline (times in seconds), and the speech around them.

## 2. Cues by section (moods)
Read the transcript and split it into 2 to 3 sections. A vlog that worked:
- **Opening / journey** (hopeful, dreamy ambient beats about 100 BPM), swelling on the travel B-roll;
- **The talk** (laid-back lo-fi about 73 BPM, sparse, nothing with a busy lead melody);
- **Ending** (warm, hopeful indie about 95 BPM) from "to close this video..." through the outro.

Pick from the user's own music files, instrumental only, in a matching BPM
range; avoid corporate stock sounds. Then cut each cue to the exact section
length, with your own short fade at the end. **Check the loudness over
time**: one edit started fading 25 s before its end, killing the B-roll it
was meant to lift. Fix: use a longer cut and trim it yourself with your own
short fade.

## 3. One bed with a baked envelope
Normalize each cue to the **full level**, place them with 2.5 to 3 s crossfades, then apply a gain envelope:
- **Full on B-roll** (and other speech-free moments): **-16 LUFS**. -19 was not enough; the creator asked for louder.
- **Under speech: -14 dB** below full (about -30 LUFS against a -16 LKFS voice).
- **Rise 1.2 s, starting about 0.9 s before the B-roll** (inside the speaker's tail room) so the music *flows into* the B-roll; fall 1.0 s ending as the next sentence starts.
- Write a plain **16-bit PCM WAV** with a **new file name per render** (`bed_v3.wav`) so the editor never serves a cached old file. Verify levels per section after rendering (speech about -30, B-roll about -15 LUFS).

## 4. Sound effects: taste over literal
What failed: a zipper when the bag opens, cloth swooshes, a jet pass-by on the plane shot. The creator called it "the most cheesy and weird". Literal foley that mimics objects on screen feels cheap in a reflective vlog.
What works:
- **One soft swell into each B-roll**: an untuned reversed cymbal ("riser, no impact") that **peaks exactly on the cut**, about 10 dB under the music. Untuned so it never clashes with the music's key.
- **Quiet room tone** only where it sets the place (airport lounge, a city street), about -37 LUFS, long fades.
- The **B-roll's own natural sound** is real and usually better than library SFX.
- Find where the event happens inside each SFX file (loudness envelope) and align that point, not the file start.
- **Pre-mix each scene into one mono clip** (a track that cannot hold overlapping clips), placed at the scene start.

## 5. Place and verify
Place the bed on its own audio track and the scene clips on a second track
(the creator's own timeline uses the same layout). Re-placing must never
bring back clips the creator deleted: place "bed only" or "SFX only" when
updating one of them.
Then make sure the bed is actually audible (render a few seconds and check
the mix): an editor template's music track can be silenced by a track-level
music setting.

## Report
Each cue (section, title and artist, two alternates), the envelope numbers, each SFX scene and why, and that you couldn't listen: "this is a judgement from the files, tell me if it feels off".

## Limits on this phone

- No music library service is connected on the phone. There is no
  Epidemic Sound, no search, no edit-to-length service and no download.
  All music and SFX must already be on the device as files the user
  supplied or licensed.
- Building the bed (normalizing cues, crossfades, gain envelope, loudness
  check) needs audio tooling such as ffmpeg with loudnorm. If the
  on-device shell cannot do it, plan the cues and envelope on device and
  render the bed through a server or VM relay.
- Loudness numbers above are targets to verify after the render. If the
  phone cannot measure LUFS, say so in the report instead of guessing.
