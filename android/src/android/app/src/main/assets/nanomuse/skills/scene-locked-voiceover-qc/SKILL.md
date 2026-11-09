---
name: scene-locked-voiceover-qc
description: Lock every voiceover line to its scene timestamp, apply the pro voice treatment, test the music bed in the gaps, and QC the assembled video.
version: "1.0.0"
---

# Scene-Locked Voiceover and Delivery QC

Use this skill for any voiceover video where dialogue must land on its scene, and for the final QC before delivery.

## Lock every line to its scene

- Build the voiceover from the segment timestamps first. Pull each line's start time from the scene map (such as segments.json) and place the audio at exactly that offset.
- Never fit voice to video with one uniform speed change. A single rate multiplier stacks drift line by line, and the observed failure ran up to ten seconds off scene.
- Keep the video at its natural speed and move the audio to the scenes, not the other way around. When a line overruns its slot, shorten the wording and regenerate that line instead of stretching the timeline.
- Generate every line with the project's standing voice rule and never substitute another TTS source mid-project. A swapped voice breaks a dub as surely as a drift does.
- Fit each line into its own scene window before assembling. Check that the last word of every line lands before the next scene's start.

## Lock scenes to a finished continuous voiceover

- Use this section instead of moving audio lines when the voiceover is already a finished continuous track. Lock each scene to the voiceover line it describes, rather than forcing the voiceover to fit the scenes.
- Derive each line start from the finished voiceover audio itself. Place each scene at the line start you measured in that audio, not at a time copied from an untested map.
- Check a transcript map against the voiceover audio before you assemble anything. Listen to or measure line starts near the beginning, near the middle, and near the end, and also at any scene change that matters.
- Discard a transcript map that drifts in one direction across those checks and derive the line starts again from the voiceover audio. A map that is early by seconds in the middle will place every later scene early, even if its first line looks right.
- Hold the last frame of a scene until the next voiceover line starts when the scene would otherwise end early. Keep the video at its natural speed and keep the finished voiceover wording and timing untouched.
- Verify the rebuilt boundaries on the timeline before you render the full video. Confirm that each checked line starts on its scene and that no held frame runs past the next line start.

## Apply the pro voice treatment

- Make every voiceover pro level by default. The user rejects flat or unprocessed voiceover, so no voiceover ships without this treatment.
- Trim silence inside every line. Keep internal pauses at or under 0.15 seconds and head and tail trims near 0.03 seconds, so no dead air survives between words.
- Pace the delivery at about 1.10x speed. Apply a per-line compressor and loudness-normalize each line to about I=-15 LUFS before assembly.
- Keep the bed alive under dialogue for original-language work. Run a dynaudnorm music bed plus a second offset layer so dialogue-free gaps stay filled and the track never goes silent.
- For a dubbed video, do not fill gaps with original-video bed. Use a clean music bed without source voice and test the result with the gap test below.
- Master the final mix to I=-13, TP=-0.8 and LRA=8 with a limiter. This is the measured chain that passed the user's Gemini review.
- Measure before delivery. Delivered voiceover should show silence under one percent of runtime and a mean level near -15 dB. Regenerate and retrim any line that misses these numbers.
- When the standing voice source is a browser site, generate chunk by chunk and leave its required cooldown between batches. Read the approved voice and a sample from the project notes first.

## Test the bed in the gaps before delivery

- Treat original-video bed as a source-language carrier in a dub. Do not add a second layer of original bed in gaps.
- Measure the bed in at least two inter-line pauses, one early and one late, against dubbed speech.
- Pass only when the bed is at least 20 dB below dubbed speech in each pause.
- On fail, rebuild audio only. Try a side-channel bed first if the voice is centred, else use a full-mix bed at -26 dB.
- Apply sidechain with threshold -40, ratio 12, and release 1000 ms, then repeat the gap test at the same pauses.
- Do not accept a Gemini READY verdict as a pass for this test.
- Record pause times, bed level, and speech level with delivery.

## QC the assembled video before it ships

- Watch or scrub the full render at least once per delivery, checking dialogue against the scene it describes, segment by segment. Ship nothing you have not checked yourself.
- Time SFX to scene content, not to global markers. A battle sound during a dialogue scene means the placement map drifted, so fix the map, not the mix.
- Sweep every frame for text the user did not ask for: burned-in labels, scene names, watermarks and platform logos. Regenerate or clean any frame that fails this sweep before delivery.
- Verify character correctness against the approved reference (portrait, outfit, language of any label). One wrong character costs more trust than one late delivery.
- Check joins, transitions and the audio tail. Glitches cluster at segment boundaries and under-fades at the end.

## Re-deliver with proof

- Save every fix under a new filename, never overwriting the old one. The user may be watching a cached copy of the old build.
- When the user reports a flaw you already fixed, confirm which version they are seeing before rebuilding. A stale cache is not a new failure.
- Report what you verified, not just what you did. Name the segments checked, the sweep results and the exact durations that line up.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.

## Limits on this phone

- VoiceStudio TTS, CapCut CLI and ffmpeg-heavy renders are VM or desktop tools and are not available on this phone. Voiceover generation, per-line processing, loudness mastering and full video assembly need a server, relay or VM.
- On this phone, do the planning, timestamp mapping, gap-test planning, script checks and QC review that need no heavy rendering. Hand generation, mastering and assembly to a server route, and verify the returned files on device before delivery.
- If no on-device audio measurement tool is available, do not invent loudness or gap numbers. State what could not be measured and hand that check to a server route.
- When assets came from a browser task, run the verify-browser-task-artifacts skill first so the QC checks files that actually exist on device.
