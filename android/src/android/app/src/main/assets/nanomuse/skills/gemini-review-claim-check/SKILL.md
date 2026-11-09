---
name: gemini-review-claim-check
description: Check each Gemini video review claim against an objective measurement before starting any fix, so a disproved claim never blocks delivery.
version: "1.0.0"
---

# Gemini Review Claim Check

Use this skill when a Gemini review of a video flags a problem. Treat each flagged point as a claim to check, not as a fix order.

## Verify each claim before you fix it

- Read each flagged point and name the measurement that would prove or disprove it before you change the edit.
- Check a missing music claim against the mix itself. Compare the final mix with the voiceover only track and with the music only track, listen in dialogue free pauses, and report the numbers you measured.
- Check a burned in text claim against the pre edit source. Open the source frame at the flagged time and decide whether the text was already baked into the source or was added during the edit.
- Check a loudness claim with a fresh measurement of the delivered file. Report integrated loudness and true peak from that file and compare them with the project ceiling instead of quoting the edit report.

## Record a disproved claim instead of looping

- Write the proof down with the file name, the flagged time, the measurement and the result. A claim that was measured and disproved is a record, not a new defect on the next review.
- Carry a source baked artefact forward as a known limitation of that source. Name it in the delivery note so the next review does not present the same artefact as if it were newly introduced.
- Fix a claim only when the measurement confirms it. When the measurement disproves the claim, present the proof to the user and let the user decide whether to proceed on a self check for that delivery.

## Keep the review gate intact

- Never report a video as passing review when it did not pass. Say plainly which points were disproved, which points were fixed, and which points remain open.
- Treat a user self check as a decision for that one delivery only. The review gate stays in place for the next video unless the user changes it.
- Re-review after a real fix. A corrected mix or a cleaned frame earns a fresh verdict, and a disproved claim does not excuse an unmeasured change elsewhere.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.

## Tips

- Listen to the mix in its quietest pauses before you trust a claim about missing music, since a ducked bed is hardest to hear under dialogue.
- Inspect the source for baked in text before the edit starts, so a known source limitation is recorded before review instead of discovered during review.

## Limits on this phone

- Audio measurement needs an on-device audio tool in the Linux sandbox, or a server route. If no measurement tool is available on the phone, do not guess numbers. State which claim could not be measured and ask the user how to proceed, or hand the measurement to a server route.
- Gemini review itself runs in the browser or in the user's AI Studio session. The phone agent cannot sign in to that session on the user's behalf. Hand sign-in and bot checks to the user.
- Heavy re-renders and re-edits need a server, relay or VM. On this phone, limit fixes to checks, notes and light edits, and hand heavy fixes to a server route.
