---
name: capcut-edit
description: Plan and prepare CapCut edits for the phone, with a CapCut-ready kit of clips, SRT captions and a step recipe, plus phone-legible text and safe-zone standards.
version: "1.0.0"
---

# capcut-edit

Build and prepare CapCut/JianYing edits: video/audio tracks, captions from SRT, trim, speed, volume, transitions, text animations, Ken Burns keyframes, long-form to shorts cuts. On this phone the agent prepares the edit and the user performs it in the CapCut app; the agent does not drive the app and never publishes.

Original source: capcut-cli (renezander030/capcut-cli v0.31.1, MIT, independent project, NOT affiliated with CapCut/ByteDance).

## Standing rules

- Nothing publishes or uploads without the user's explicit approval of the draft first. The user reviews and exports/publishes.
- Never purchase paid assets or subscriptions, never auto-post, never delete original media. All need the user's explicit say-so.
- Never use cloud captioning that uploads audio to a third party. Only with the user's explicit approval for that upload.
- Edit order: structural edit first (trim/split, speed, timing), then transitions only after clip boundaries are stable, then text/effects, then audio + captions, then export check (resolution/fps), then user approval before any publish.

## Trigger

Use when the user mentions CapCut, JianYing, subtitles/captions, cutting a video, or a CapCut-ready edit, in English, Hindi/Hinglish, or Chinese.

## What the phone workflow is

The desktop draft format (template 6.5.0, macOS/Windows draft store) opens editable in desktop CapCut, but the phone CapCut app has no import path for a desktop draft folder. So do NOT promise "phone par CapCut me khul jayega" for a draft built elsewhere.

The phone workflow that DOES work: prepare a CapCut-ready kit for manual phone edits, consisting of:

1. The trimmed clips, in order, with filenames that carry their sequence.
2. An SRT file with the real voiceover timings for captions.
3. A short step recipe: clip order, trim points, speed, transitions, text layers with sizes and positions, audio levels, export settings (1080x1920, 9:16).
4. A rendered preview video for the user's approval, when a render is available from the server or relay.

Captioning: use `import-srt` style captions from a real SRT (the user's voiceover timings). Never auto-caption by uploading audio to a cloud service without the user's explicit approval.

## Desktop draft reference (server or VM only)

When a server or VM with capcut-cli is available, a real editable desktop draft can be built from the terminal. The commands below are for that machine, not the phone:

```bash
CAPCUT=<path to capcut-cli>/dist/index.js
node $CAPCUT quickstart my-short --video clip.mp4 --srt captions.srt \
  --drafts <drafts dir> --ratio 9:16
node $CAPCUT info <drafts dir>/my-short -H
node $CAPCUT render <drafts dir>/my-short \
  --all-video-tracks --burn-captions --out preview.mp4
```

- `capcut describe --compact` lists commands; fetch a full contract with `capcut describe --command <name>` before using a new command.
- `capcut render` gives an ffmpeg preview (proxy, not CapCut's final render). Use it to show the user a timed preview for approval.
- There is no default CapCut draft directory on Linux: always pass `--drafts <dir>` explicitly.
- Never use `--force-write` unless the user explicitly accepts overwriting changed-on-disk state or an open editor. Close the project in CapCut before editing a draft it owns; reopen after.

## Phone-legible text standards (user-approved)

### Canvas

- Vertical projects: set the canvas explicitly to 1080x1920, 9:16. Without an explicit canvas, a preview can default to 16:9.

### Text sizes: visual targets for a phone screen, not raw flags

CapCut's internal size values and ffmpeg points are NOT the same scale, so never copy a number blindly: set the size, render or preview, and check the frame. If it is not effortlessly readable on a phone screen, enlarge it.

| Text type | ffmpeg / pt guide | CapCut size guide |
| --- | --- | --- |
| Main title / hero hook | 80 to 135 pt | 9.0 to 12.0 |
| Secondary sub-headline | 68 to 85 pt | 8.0 to 9.5 |
| Subtitles / explanations | 56 to 64 pt | 6.5 to 7.5 |
| Micro labels / badges | 42 to 48 pt | 5.0 to 5.5 |

- Multi-line text: line spacing 0.25 to 0.32. The 0.05 default is too cramped and lines collide.
- Separate distinct text layers by at least 115 to 140 px vertically in 1080x1920 space.

### Contrast: never plain text over moving footage

- Multi-layer drop shadow: angle -45, distance 4.0 to 6.0, alpha 0.95, diffuse 0.08.
- Backdrop plate: semi-transparent dark box, black at 0.35 opacity, border width 16 (ffmpeg: `box=1:boxcolor=black@0.35:boxborderw=16`).
- If the background varies, text gets a shadow AND a plate, not one or the other.

### Safe zones (1080x1920)

- NEVER place text at y below 15% (header / notch area) or y above 85% (bottom bar / caption area).
- Lower-third subtitles: y = 75 to 80%.
- Upper-third placement (when the subject sits at the bottom): y = 20 to 25%.

### Frame check before delivery

- Extract a frame at every scene boundary and at each text segment's midpoint; verify text centering and legibility on the frame itself before delivering. A draft file that "looks right" is not proof. The frame is.
- Devanagari note: some render proxies show Devanagari as boxes (default font has no Devanagari). The draft stores correct Unicode. For a readable preview, overlay with a Noto Sans Devanagari font.

### Desktop-owned drafts (CapCut 9.x)

- CapCut 9.x+ reads `Timelines/<UUID>/draft_content.json` as the active timeline, not the root file alone. Writing only the root file can open an empty timeline.
- While a project is open, CapCut holds it in memory (`.locked`) and has no live file watcher: its autosave can overwrite external disk edits. Rule: close CapCut before editing a draft it owns, then reopen to reload.

### Audio

- The standing mix stays in force: the user's pro voiceover standard, and BGM as a light filler that is felt, not loud. Voice on top, music ducked under speech, SFX time-aligned to cuts, 0.8 to 1.2 s fade-out.

## Limits on this phone

- capcut-cli is a Node tool that runs on a desktop or VM. It does not run in the phone sandbox, and the phone agent cannot open, tap or drive the CapCut app. On the phone this skill plans the edit and prepares the CapCut-ready kit (clips + SRT + step recipe) for the user to apply by hand.
- Desktop drafts built on a server or VM do not import into the phone CapCut app. Never promise that they will.
- ffmpeg preview renders also need a server, relay or VM. If no render is available, deliver the kit and the timings, and say plainly that no preview was rendered.
