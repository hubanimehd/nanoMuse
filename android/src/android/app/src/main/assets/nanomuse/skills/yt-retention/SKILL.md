---
name: yt-retention
description: Read a YouTube Studio audience-retention export and name the biggest leak, hook leak, cliff, or slide.
version: "1.0.0"
---

# yt-retention

Adapted from the Muse skill `yt_retention`, which is the Muse copy of the original `yt-retention` skill from Jakeschincariol/youtube-agent-skill (MIT). Ask for the task in words. The original `/yt-retention` name is kept here as an alias only.

The retention graph is the only honest feedback YouTube gives you. Almost nobody exports it.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first. This skill writes an analysis. It never publishes a video.
- Never pay for anything.
- Trading, if it ever comes up in this work: no order without the user's explicit yes for that exact order plus a risk PASS, and paper trading is the default.

## The tool

The helper script `retention.py` is included in this skill folder. It is pure standard library Python and runs in the on-device Linux shell with `python3`, using workspace-relative paths:

```bash
python3 retention.py retention.csv --duration 600
python3 retention.py retention.csv --transcript transcript.srt
```

Getting the file: Studio, then a video, then Analytics, then Engagement, then the audience-retention chart, then the download icon, then "Audience retention".

## Three different problems

- **HOOK LEAK**: what is lost in the first 30 seconds. Under 25% is healthy. This is always the first thing to fix and it is always the first fifteen seconds of script, never the edit.
- **CLIFFS**: single steep drops. A cliff is a moment: a topic change with no signposting, a sponsor read, a long setup. With `--transcript` the tool prints what was being said there, which is what makes the report actionable instead of interesting.
- **SLIDE**: the steady bleed across the middle. A flat slide is pacing. The fix is cutting, not rewriting.

## What to hand back

Name the single biggest leak and one change for it. Not a list of five. Then, only if asked, the rest. And if the hook leak is healthy and the slide is flat, say the video is fine and the problem is packaging. Send them to the `yt-package` skill.

## Voice profile

Read `youtube/voice.md` in the workspace if it is filled in. If it is only the blank template, ask for three of the user's own videos before writing in their voice.

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: ship it, or change it?

## Limits on this phone

- The phone cannot pull Analytics or the retention export from YouTube Studio on its own. The user must download the CSV in Studio and supply the file, plus the video duration and a transcript file if they want the cliff lines named.
- `retention.py` only reads the CSV. It cannot re-edit the video. Any fix that needs cutting or re-rendering needs the user's editor or a server.
