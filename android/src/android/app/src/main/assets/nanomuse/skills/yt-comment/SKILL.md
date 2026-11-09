---
name: yt-comment
description: Triage YouTube comments and draft short replies, pick one comment to pin, and hand back copy-ready text.
version: "1.0.0"
---

# yt-comment

Adapted from the Muse skill `yt_comment`, which is the Muse copy of the original `yt-comment` skill from Jakeschincariol/youtube-agent-skill (MIT). Ask for the task in words. The original `/yt-comment` name is kept as an alias only.

The comment section is a retention surface, not a chore. Replies in the first few hours are what decide whether a thread becomes a conversation other people read.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first. This skill only writes replies. The user posts them.
- Never pay for anything.
- Trading, if it ever comes up in this work: no order without the user's explicit yes for that exact order plus a risk PASS, and paper trading is the default.

## Before you write

1. Read `youtube/voice.md` in the workspace if it exists. That is the user's voice profile: how they talk on camera, the words they never use, who they are talking to, what they will not claim. If it does not exist, ask for three of the user's own videos, read or transcribe them, infer the voice, and write the file. A script in the wrong voice is worse than no script, because they have to read it out loud.
2. Never invent a number, a result or a source. If a figure would strengthen a reply and you do not have one, ask for it or write the line without it.

## Triage first, always

Sort what the user pastes into four piles and say how many are in each before writing anything:

1. **Questions**: answer them. These are the next video's topics, so note the repeats.
2. **Corrections**: if they are right, say so plainly and thank them. Never argue a fact you cannot check.
3. **Praise**: reply to a few, briefly, with something specific from their comment. A wall of identical "thank you!" replies reads as automated because it is.
4. **Bait**: do not reply. Say so and move on. Never write a comeback, however deserved.

## Writing the reply

- Under 30 words. A long reply in a comment thread is a blog post nobody asked for.
- Answer the actual question in the first sentence.
- One question back, only when it is real.
- No emoji unless the user's own replies use them. Check their voice file.
- Never promise a video you have not agreed to make.

## The heart and the pin

Say which ONE comment to pin and why. Pin the question the most people also have, not the nicest one. Heart generously. It costs nothing and it is visible.

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: ship it, or change it?

## Limits on this phone

- This skill cannot post replies, pin comments, or heart comments on YouTube itself. There is no YouTube posting integration on the phone. The user copies the replies and posts them.
- The phone cannot fetch a video's comments on its own. The user must paste the comments, or supply a file with them.
- Transcription of the user's own videos for the voice profile needs a transcript file or a server. If no transcript is available, ask the user for the three videos and work from what they provide.
