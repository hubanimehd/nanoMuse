---
name: ve-captions
description: Builds clean, readable burned-in style subtitles from the verbatim transcript. Stutters and fillers removed, one short line at a time.
version: "1.0.0"
---

# Captions

Goal: people watching **on mute** can follow; the captions are just there, never in the way.

This skill is adapted from a video-editor skill pack written for DaVinci
Resolve Studio and talking-head vlogs. There is no Resolve on this phone, so
the craft rules below are kept and the Resolve import step is replaced by
writing a versioned SRT file in the workspace. Nothing uploads or publishes
without the user's explicit approval of the draft first. Never pay for
anything.

## Source
The verbatim transcript mapped onto the **current** timeline (follow the creator's trims), not a second auto-caption pass (a second, less accurate transcription). Drop words on B-roll (radio lyrics, background talk).

## Clean, not verbatim
Captions show what they *mean*, written correctly:
1. **Stutters:** "pro-product" becomes "product", "da-da," becomes "da,".
2. **Cut-off words:** "es--", "A--" are gone.
3. **Filler sounds:** drawn-out "uuum", "haaa", "eee" are gone. Careful with real words that look like fillers ("um" means "a" in Portuguese, "e" means "is").
4. **Self-corrections:** "get a, an appointment" becomes "get an appointment".
5. **Spoken short forms become correct written forms**, if the creator asks. Brazilian Portuguese example: ce/ces becomes voce/voces, ta becomes esta, to becomes estou, tava becomes estava, tamo becomes estamos, vamo becomes vamos, pra/pro becomes para/para o, ne becomes nao e, "o que que" becomes "o que", "por que que" becomes "por que", "tu consegue" becomes "voce consegue"; "tao" becomes "estao" only before a verb in -ndo ("tao bonito" stays). Keep **expressions and swear words** (fix grammar, not voice). Keep capital letters at sentence starts when you shorten.
6. **Names and spellings checked on the web** (transcripts garble product names), and the creator's own name exactly as they write it.
7. Don't reveal what the creator wants kept secret (a place, a company) even if they say it.

Keep two lists: global rules (word map plus phrases) in config, per-video fixes in `caption_fixes.json`. Apply phrase fixes *after* the word map.

## Lines
- **One line, max 42 characters**; a sentence's last word may stretch it to about 50 rather than sit alone.
- New cue at a pause of 0.6 s or more, at sentence ends (if the line is already substantial), or after 3.2 s.
- Min 0.9 s on screen, end 0.25 s after the last word, never overlapping the next.
- A lone small word after a pause ("que", "vai", "not") joins the next line. Single-word cues are fine only when they stand alone ("Calm down.", "Bye.").
- **First cue starts at 00:00:00,000** (some importers shift everything relative to the first cue).
- Write a **new file name per version** (`captions_v3.srt`).

## Read every cue
Print every line and actually read it before handing it over. Grep for leftover short forms, double punctuation (",,"), lowercase after a full stop, words lost by your own filters. Fix rules, regenerate, read again.

## Into the editor
Write the SRT file into the workspace and hand it to the phone editor or
the render relay for burn-in. Delete the old subtitle clips but keep the
track so its style stays, when the editor supports tracks.

## Style
Record the creator's style once (font, size, position, shadow) from their timeline and save it in config as the house style, for example a light sans at size 68, centred about 11% up from the bottom, soft drop shadow. Overlays must stay out of that band (see the motion overlays skill).

## Limits on this phone

- No DaVinci Resolve on the phone. Caption burn-in needs a render step:
  the phone editor if it supports SRT import, otherwise a server or VM
  relay. The agent still writes and proofreads the SRT on device.
- Font rendering for non-Latin scripts (for example Devanagari) can show
  as boxes in some phone editors. Use a font the editor supports for that
  script, and check one rendered frame before approving the full burn-in.
