---
name: yt-script
description: Turn one idea into a finished YouTube script with five scored hooks, beats, on-screen notes, and runtime.
version: "1.0.0"
---

# yt-script

Adapted from the Muse skill `yt_script`, which is the Muse copy of the original `yt-script` skill from Jakeschincariol/youtube-agent-skill (MIT). Ask for the task in words. The original `/yt-script` name is kept here as an alias only.

One idea into a script somebody finishes.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first. This skill writes a script. It never publishes a video.
- Never pay for anything.
- Trading, if it ever comes up in this work: no order without the user's explicit yes for that exact order plus a risk PASS, and paper trading is the default.

## The tools

Two tools live in this skill folder and both actually run. Use them. Do not eyeball the hook. Both are pure standard library Python and run in the on-device Linux shell with `python3`, using workspace-relative paths. `hookscore.py` reads `hooks.json` from the same folder, so keep both files together:

```bash
python3 hookscore.py hooks.txt              # rank your hook options
python3 hookscore.py --hook "one line"      # score a single one
```

`hooks.json` holds 21 hook formulas, each with its shape, a worked example, its failure mode, and the patterns `hookscore.py` classifies transcribed speech against. Read it when you need a formula's exact shape or example.

## Before you write

1. Read `youtube/voice.md` in the workspace if it exists. That is the user's voice profile: how they talk on camera, the words they never use, who they are talking to, what they will not claim. If it does not exist, ask for three of the user's own videos, read or transcribe them, infer the voice, and write the file. A script in the wrong voice is worse than no script, because they have to read it out loud.
2. Never invent a number, a result or a source. If a figure would strengthen it and you do not have one, ask for it or write the line without it.

## The shape

**The first 15 seconds is the whole job.** It does three things or the video leaks: confirm the click the title promised, open a question the viewer cannot close, and prove the payoff exists.

1. **Hook.** Write FIVE against the 21 formulas in `hooks.json`, run them through `hookscore.py`, keep the top two, and show the user both with their scores. Never hand over one hook.
2. **The turn** (0:15-0:45). Say what the video is going to do, in one sentence, and start doing it. No channel intro, no "before we get started", no subscribe pitch. Those are the single most common cause of the 0:30 cliff.
3. **The body.** One idea per beat. Mark each beat with what is ON SCREEN, not just what is said. A talking head with nothing to look at is a podcast.
4. **The payoff.** Deliver the thing the hook promised, explicitly, and say that you are delivering it: "that is the prompt, it is in the description".
5. **The close.** One ask. Not three.

## What to hand back

- The two best hooks with their scored panels
- The script, beat by beat, with `[ON SCREEN: ...]` on every beat
- The runtime estimate at 150 words per minute
- One line naming which formula the winning hook used and why it fits this idea

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: ship it, or change it?

## Limits on this phone

- `hookscore.py` scores text only. A high score is not a promise. Its own note says it separates deliberately bad hooks from real ones well, and separates a creator's own hits from their own misses barely at all. Treat a low score as a reason to look again, never a high score as a guarantee.
- The phone cannot record or generate the voiceover for the script. Voiceover needs the user's recording or a text-to-speech service on a server or relay. VoiceStudio TTS is not available on the phone.
- The phone cannot fetch the user's own videos or transcribe them on its own. Transcripts must come from the user or a server.
