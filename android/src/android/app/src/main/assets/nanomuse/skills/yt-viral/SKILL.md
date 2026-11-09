---
name: yt-viral
description: Find what is actually working in a YouTube niche and rank videos by how far each beat its own channel median, then name the title formula.
version: "1.0.0"
---

# yt-viral

Find what is actually working in the user's niche on YouTube and rank it by how far each video beat its own channel, then name the formula. Use for "what is working right now", "find viral videos in my niche", "why did this blow up", competitor research, or a swipe file.

## Standing rules

- Nothing here publishes. This skill writes and the user uploads. Nothing uploads without the user's explicit approval of the draft first.
- Never pay for anything. Never sign in to any account for this work.
- Read, do not scrape. Use public listings only, never a logged-in session, never the user's own account credentials.

## Voice profile

Read `workspace/youtube/voice.md` if it is filled in. If it is only a blank template, ask for three of the user's own videos before writing in their voice.

## The ranking idea

Raw view counts rank channel size, not ideas. Rank by **multiple over each channel's own median**, which is the only version of the question that is about the video.

```bash
python3 swipe.py collected.json --min 2.0
```

The `swipe.py` script bundled with this skill is pure standard library Python and runs in the on-device Linux shell. It looks for a `hooks.json` file beside itself for title formulas; if that file is absent, titles classify as "Unclassified" and the ranking still works.

## Collecting the input

You need at least **four videos per channel** or a median means nothing, and the tool will skip the channel and tell you it did. Collect them however the user prefers: a channel URL listing, the public channel page, or a manual list the user gives you.

```json
[{"channel":"...","title":"...","views":412000,"url":"...","duration":613}]
```

## Reading the output

The multiple is the signal. The formula line is a judgement about the TITLE, matched against the hook formulas. It is not a claim about why the video worked, and you should say so when you present it.

What to hand back: the top five with their multiples, the formula each used, and the ONE structural thing they share. Then the harder line: which of those the user could actually make this week, in their voice, with what they have.

## The gate

Nothing here publishes. This skill writes and the user publishes. Every output ends in a block the user copies, and the last line of every run is the question: **ship it, or change it?**

## Limits on this phone

- Bulk collection with `yt-dlp` against a channel URL needs a server, relay or VM; the phone shell cannot be assumed to have it. On the phone, collect from the public channel page in the browser or from a list the user provides.
- The bundled `swipe.py` runs on the phone. The title-formula naming needs `hooks.json` beside it; without that file the ranking still works but formulas read "Unclassified".
