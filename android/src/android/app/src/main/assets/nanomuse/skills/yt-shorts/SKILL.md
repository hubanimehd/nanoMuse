---
name: yt-shorts
description: Find self-contained 20 to 55 second moments in a long video transcript and rewrite each as a standalone Short.
version: "1.0.0"
---

# yt-shorts

Adapted from the Muse skill `yt_shorts`, which is the Muse copy of the original `yt-shorts` skill from Jakeschincariol/youtube-agent-skill (MIT). Ask for the task in words. The original `/yt-shorts` name is kept here as an alias only.

A Short cut out of a long video is not a clip of the best moment. It is a moment that survives without the video around it, which is a much smaller set.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first. This skill writes Short scripts. It never publishes a video.
- Never pay for anything.
- No thumbnail for any Short video, ever. Shorts upload with the YouTube auto thumbnail only.
- Trading, if it ever comes up in this work: no order without the user's explicit yes for that exact order plus a risk PASS, and paper trading is the default.

## Before you write

Read `youtube/voice.md` in the workspace if it exists. That is the user's voice profile: how they talk on camera, the words they never use, who they are talking to, what they will not claim. If it does not exist, ask for three of the user's own videos, read or transcribe them, infer the voice, and write the file. A script in the wrong voice is worse than no script, because they have to read it out loud. Never invent a number, a result or a source.

## Picking

Read the transcript and find spans of 20-55 seconds where all three are true:

1. It opens on a complete thought. If the first sentence needs the previous minute, it is not a Short.
2. There is a turn in it: a claim, then something that complicates or proves it.
3. It ends on a line, not a trail-off.

Rank the candidates and show the user the top five with their timecodes and first line, so they can reject one without reading the whole transcript.

## Writing each one

- **A NEW first line.** The long video's line assumes context this viewer does not have. Write the replacement and run it through the `yt-script` skill's `hookscore.py`.
- **On-screen text for the first two seconds**, different words from the spoken line.
- **A loop point**: what the last line sets up so the first line answers it.
- Vertical framing note: what gets cropped out of a 16:9 frame and whether that matters.

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: ship it, or change it?

## Limits on this phone

- The phone cannot transcribe the long video or cut the Short on its own. A transcript file must come from the user, from YouTube auto captions, or from a server. The actual vertical cut and render need the user's editor or a server, since ffmpeg-heavy rendering is not available on the phone.
- Scoring a new first line uses `hookscore.py` from the `yt-script` skill folder. That script and its `hooks.json` must be present in the workspace for the score to run. If they are missing, write the first line without a score and say so plainly.
- The phone cannot upload the finished Short. The user uploads it and confirms the title, description, language, and AI disclosure in Studio.
