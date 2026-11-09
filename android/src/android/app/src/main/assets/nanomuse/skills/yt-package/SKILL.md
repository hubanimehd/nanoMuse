---
name: yt-package
description: Write and score YouTube titles with a matching thumbnail brief, as one click unit, using title.py.
version: "1.0.0"
---

# yt-package

Adapted from the Muse skill `yt_package`, which is the Muse copy of the original `yt-package` skill from Jakeschincariol/youtube-agent-skill (MIT). Ask for the task in words. The original `/yt-package` name is kept here as an alias only.

The title and the thumbnail are ONE unit. Writing them separately is why most packaging fails: the thumbnail repeats the title, and half the click surface says the same thing twice.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first. This skill writes titles and a thumbnail brief. It never publishes a video.
- Never pay for anything.
- Trading, if it ever comes up in this work: no order without the user's explicit yes for that exact order plus a risk PASS, and paper trading is the default.

## The tool

The helper script `title.py` is included in this skill folder. It is pure standard library Python and runs in the on-device Linux shell with `python3`, using workspace-relative paths:

```bash
python3 title.py --title "..." --thumb "AI RAN IT"
python3 title.py titles.txt            # one per line, ranked
```

## Before you write

1. Read `youtube/voice.md` in the workspace if it exists. That is the user's voice profile: how they talk on camera, the words they never use, who they are talking to, what they will not claim. If it does not exist, ask for three of the user's own videos, read or transcribe them, infer the voice, and write the file. A script in the wrong voice is worse than no script, because they have to read it out loud.
2. Never invent a number, a result or a source. If a figure would strengthen it and you do not have one, ask for it or write the line without it.

## Rules the tool enforces, and why

- **60 characters** is where desktop search truncates, **40** is a mobile home feed. Both are reported because they fail differently: a desktop cut loses the tail, a mobile cut can lose the subject.
- **The thumbnail must not repeat the title.** Different words, same promise.
- **Three words maximum on the thumbnail.** At feed size a fourth word is a grey smear.
- **A number, a name or a date** beats every adjective available to you.
- **Two all-caps words is the ceiling** before a title reads as spam.

## Write ten, keep two

Generate ten titles, run them all through `title.py`, show the user the top three with their scores and the specific issue on each. For the winner, write the thumbnail brief: the expression, the framing, the three words, and what the background has to do to hold contrast at feed size.

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: ship it, or change it?

## Limits on this phone

- `title.py` only scores text. It cannot draw, render, or generate the thumbnail image. The thumbnail itself must be made by the user or by an image tool on a server or relay.
- The phone cannot check live search results or trending titles on its own. Any check against what is currently ranking needs the user or browser automation on a server.
- No thumbnail is ever made for a Short video. Shorts upload with the YouTube auto thumbnail only.
