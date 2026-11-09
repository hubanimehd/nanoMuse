---
name: yt-chapters
description: Build YouTube chapters from a transcript: find section boundaries from pauses and vocabulary shifts, then retitle each chapter as its promise. Use when a video needs chapters in its description.
version: "1.0.0"
---

# yt-chapters

## Standing rules

- Nothing here publishes. This skill writes and the user uploads, matching the standing rule that nothing uploads without the user's draft approval.
- Never pay for anything, and never upgrade any service.

## What you need

A timestamped transcript of the video (SRT, VTT, or word-level JSON). Without a transcript there are no boundaries to find. If no transcript exists, get one first through the phone's transcription route or a server, relay, or VM. Do not invent timestamps.

## The rules, which are not optional

A chapter list that breaks any of these silently does not become chapters at all, the block just sits in the description doing nothing:

- The first entry must be **00:00**.
- There must be **at least three**.
- Each must be **at least 10 seconds** long.

Check all three before handing the block to the user, and say plainly when a list is invalid rather than letting them paste it.

## How to find the boundaries

Score the pauses the speaker actually took by how long the pause was and how much the vocabulary changes across it. Long pauses with a clear topic shift are section boundaries. Short pauses inside a topic are not.

Worked method:

1. Read the transcript and list every pause of 1 second or more with its timestamp.
2. For each pause, compare the words before and after it. A big vocabulary change means a new section.
3. Pick a target count (7 is a good default, the user may ask for more or fewer) and keep the strongest boundaries.
4. Draft each chapter title from the topic words around its start time.

## Retitle every line

The boundary finder gives you topic words, and they are a draft. A chapter called "Thumbnails Titles Packaging" is a placeholder. Rewrite each one as the promise of that section, in the user's voice, three to five words.

Read the voice profile at `youtube/voice.md` in the workspace if it exists. If it is only a blank template, ask for three of the user's own videos before writing in their voice.

Chapters are also a retention diagnostic: if a section cannot be named in five words, it is two sections or it is filler.

## The gate

Nothing here publishes. This skill writes and you publish. Every output ends in a block the user copies, and the last line of every run is the question: **ship it, or change it?**

## Limits on this phone

- **The chapters script is not bundled.** The original `chapters.py` depended on a parser module from a sibling skill that is not part of this batch, so it cannot run standalone on the phone. The boundary-finding method above replaces it: score pauses and vocabulary shifts by hand from the transcript. If a transcript is long, work through it in sections and keep a running list of candidate boundaries.
- **Transcripts** need a transcription route (on-device speech-to-text if the sandbox provides one, or a server, relay, or VM). Do not invent timestamps.
