---
name: yt-edit
description: Build an edit decision list from a timestamped transcript, finding dead air, filler and repeated takes.
version: "1.0.0"
---

# yt-edit

Adapted from the Muse skill `yt_edit`, which is the Muse copy of the original `yt-edit` skill from Jakeschincariol/youtube-agent-skill (MIT). Ask for the task in words. The original `/yt-edit` name is kept here as an alias only.

An edit decision list from a timestamped transcript. It prints the cuts. The user applies them.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first. This skill writes an edit list. It never publishes a video.
- Never pay for anything.
- Trading, if it ever comes up in this work: no order without the user's explicit yes for that exact order plus a risk PASS, and paper trading is the default.

## The tool

The helper script `deadair.py` is included in this skill folder. It is pure standard library Python and runs in the on-device Linux shell with `python3`, using workspace-relative paths:

```bash
python3 deadair.py transcript.srt              # srt, vtt or whisper json
python3 deadair.py transcript.srt --floor 0.35 --json
```

No transcript yet? Ask for one, or produce one first. The caption track YouTube generates on an unlisted upload works, and so does a transcript file the user supplies. Do not guess at timings.

## What it finds

- **DEAD**: gaps longer than the floor, trimmed from the MIDDLE so both sides keep a breath. Cutting flush against speech is what makes a tightened take sound gasping.
- **FILLER**: cues that are nothing but "um", "so yeah", "basically".
- **REPEAT**: a sentence restarted. Compared against the last cue that was actually speech, not the literal previous cue, because most retakes have an "um" between the two attempts.

## What it will not do

It does not touch media. It has no opinion about B-roll. A 40% cut on the report is a 40% cut of SPEECH, and if the video has a long silent demo in it that number is wrong. Check the report against the footage before you trust the runtime at the bottom.

## Voice profile

Read `youtube/voice.md` in the workspace if it is filled in. If it is only the blank template, ask for three of the user's own videos before writing in their voice.

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: ship it, or change it?

## Limits on this phone

- `deadair.py` only prints a cut list. It does not cut audio or video, and the phone has no video editor wired to this skill. The user applies the cuts in their editor.
- The phone cannot transcribe audio on its own. Whisper and faster-whisper are not available on the phone. A transcript must come from the user, from YouTube auto captions, or from a server or relay.
- Heavy rendering with ffmpeg is not available on the phone. Any actual re-render of the edited video needs a server, a relay, or the user's editor.
