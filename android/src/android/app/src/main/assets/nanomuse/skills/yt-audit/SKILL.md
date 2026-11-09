---
name: yt-audit
description: Audit a YouTube channel and end with ONE fix: titles, thumbnails at feed size, first 15 seconds, upload rhythm, and retention. Use when the user asks for a channel audit or what to fix next.
version: "1.0.0"
---

# yt-audit

## Standing rules

- Nothing here publishes. This skill writes and the user uploads, matching the standing rule that nothing uploads without the user's draft approval.
- Never pay for anything, and never upgrade any service.

An audit that lists twenty problems is a way of avoiding the one that matters. This ends in ONE fix.

## Before you write

1. Read the voice profile at `youtube/voice.md` in the workspace if it exists. That is the user's voice profile: how they talk on camera, the words they never use, who they are talking to, what they will not claim. If it does not exist, ask for **three of their own videos**, read or transcribe them, infer the voice, and write the file. A script in the wrong voice is worse than no script, because they have to read it out loud.
2. Never invent a number, a result, or a source. If a figure would strengthen it and you do not have one, ask for it or write the line without it.

## What to look at, in this order

1. **The last ten titles, as a set.** Read them as a list, the way the channel page shows them. Do they promise different things? Score each title for clarity, curiosity, and specificity, and note repeated shapes. A channel where every title is the same shape has a format problem, not a title problem.
2. **The thumbnails, at feed size.** Shrink them. What survives? If three of them are unreadable at that size, that is the fix and nothing else matters yet.
3. **The first fifteen seconds of the three most recent.** Transcribe them and score the hook: does the opening promise something specific, does it start with the point, does it waste time on introductions? This is where most channels lose.
4. **Upload rhythm.** Not frequency, CONSISTENCY. Six videos in one week and then nothing for a month is worse than one a fortnight forever.
5. **The retention shape**, if they can export it. Look for where viewers leave and whether the drop lines up with a slow opening, a long aside, or a weak payoff.

## What to hand back

- The single biggest fix, named, with what to do this week.
- Three things that are already working, so they do not break them. Be specific; "your energy is good" is not an observation.
- What NOT to do yet, and why.

Never open an audit with praise you do not mean, and never end one with a list of twenty things.

## The gate

Nothing here publishes. This skill writes and you publish. Every output ends in a block the user copies, and the last line of every run is the question: **ship it, or change it?**

## Limits on this phone

- **Title and hook scoring scripts are not bundled.** The original skill scored titles and hooks with helper scripts from two other skills (a title scorer and a hook scorer). Those scripts are not part of this skill and are not copied here. On this phone, score titles and hooks by hand against the rules above: clarity, specificity, one promise per title, and a hook that starts with the point.
- **Thumbnails at feed size** need the phone agent's device integrations or browser automation to fetch the channel page and images. If thumbnails cannot be fetched, say so and audit from titles and transcripts alone.
- **Transcripts** need a transcription route (on-device speech-to-text if the sandbox provides one, or a server, relay, or VM). Do not invent transcript text.
- **Retention data** comes from YouTube Studio export only. If the user cannot export it, skip step 5 and say so.
