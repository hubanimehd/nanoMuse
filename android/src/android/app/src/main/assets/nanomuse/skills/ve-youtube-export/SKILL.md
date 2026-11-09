---
name: ve-youtube-export
description: YouTube export settings for great quality without huge files: bitrate, profile, audio, burned-in subtitles, and colour tags that stop the faded look. Use when asked about export or render settings.
version: "1.0.0"
---

# YouTube export

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything, and never upgrade any service.

"Quality: Automatic" with H.264 picked about 300 Mb/s: **25 GB for an 11-minute video**. YouTube re-encodes everything. Anything far above its recommendation is wasted upload time.

## Video

- Format **QuickTime**, codec **H.264** (hardware acceleration on, Network Optimization on).
- Resolution and frame rate: **timeline**.
- Quality: **Restrict to 50,000 Kb/s** for 4K-class frames (about 4.2 GB for 11 min). 40,000 is fine too (about 3.3 GB). Smaller: **H.265 at about 30,000** (about 2.5 GB) at the same quality.
- Encoding profile **High**, **Multi-pass** on (if available), key frames Automatic.

With ffmpeg in the on-device Linux shell, the equivalent is roughly: H.264, CRF 18 to 20 or a capped bitrate near 50,000k for 4K, preset slow, profile high, audio AAC 320k at 48 kHz. Match the source resolution and frame rate unless the user asked for something specific.

## Colour tags (check the project first)

Read the project's timeline colour space. If the project is **Rec.709-A** (common on Macs), set Color Space Tag and Gamma Tag to **Same as Project**. Tagging Rec.709 / Gamma 2.4 makes the video look lighter and washed out on Macs, iPhones and Chrome compared with the editor viewer. Data levels Auto. Do not retain sub-black or super-white.

With ffmpeg, tag the output with the matching colour metadata (for example `colorspace`, `color_primaries`, `color_trc` flags) so the file carries the same tags the editor would have written.

## Audio

AAC, **320 kb/s**, 48 kHz.

## Subtitles

If the user wants captions always visible (people watching on mute): burn the subtitle track into the video, and check the subtitle track is actually enabled (if it is unticked, nothing is exported). Optionally also export the SRT and upload it to YouTube as closed captions.

## File

Output to the workspace folder `export edited video/`. Save these settings as the user's preset so every export is one step.

## Limits on this phone

- DaVinci Resolve Studio and its Deliver page are not available on this phone. There is no preset to save in Resolve here. Keep the settings above as the standing export spec in the workspace, and apply them with ffmpeg in the on-device Linux shell or with the user's own editor.
- If ffmpeg is not installed in the on-device sandbox, rendering and re-encoding cannot run on the phone and need a server, relay, or VM. Report that plainly.
- Full 4K renders are heavy on a phone. Prefer 1080p previews for approval, and only run the full-quality export when the user has approved the draft and the device can handle it.
