---
name: youtube-publish-completeness
description: Verify a YouTube upload is complete: title, description, hashtags, tags, language, AI disclosure, and approved thumbnail. Use before and after every publish, and to audit published videos.
version: "1.0.0"
---

# YouTube Publish Completeness Check

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Verify the Studio channel name on screen before you publish or edit anything. Never trust the signed-in session alone.
- Confirm the draft and the thumbnail have user approval before you treat an upload as ready.
- Never pay for anything, and never upgrade any service.

## Check every field before you report completion

- Verify the Studio channel name on screen before you publish or edit anything.
- Confirm the draft and the thumbnail have user approval before you treat an upload as ready.
- Check the title, the description, the hashtags, the tags, the language setting, and the AI disclosure for the video.
- Read the first two description lines and confirm they state what the video gives the viewer in searchable words.
- Record the published URL only after every field above passes, and never report an upload as complete with a field still missing.

## Audit videos that are already published

- List the recent published videos for the channel you verified and check each one against the same field list.
- Count how many videos pass fully and how many miss at least one field, and report both counts before you change anything.
- Treat a missing description, missing hashtags, or missing tags as incomplete even when the video is already public and performing.
- Leave every title unchanged during an audit unless the user approves a new title for that video.

## Backfill missing metadata safely

- Generate only the fields that are missing, using the user's approved model routes. Do not generate them through any other model route.
- Show the user the exact generated description, hashtags, and tags for each video and obtain the user's approval for that exact text before you apply anything.
- Apply the approved backfill to the verified channel only and recheck each edited video against the full field list.
- Report each video you changed, each field you added, and each video that already passed, so the user can see what was new and what was already complete.

## How the check runs on this phone

- Use the phone agent's browser automation and device integrations to open YouTube Studio, read the channel name on screen, and inspect each video's fields.
- If Studio cannot be reached from the phone (sign-in, bot check, or missing access), stop and tell the user exactly which step is blocked. Do not report fields as checked when they were not seen.
- Keep the field list in the workspace so audits stay consistent across sessions.

## Alternatives for better results

- Run the packaging check for the title and thumbnail pairing before you run the completeness check, so a weak title does not pass on metadata alone.
- Reuse an approved description and tag set for a series only when the topic matches, and rewrite the first two lines for each new video.

## Limits on this phone

- YouTube Studio access depends on the phone's browser automation and the user's signed-in session. If the session is missing or a bot check blocks the page, the check cannot run on the phone and needs the user's manual step or a server relay. Report the block plainly.
- Metadata edits apply only after the user approves the exact text. The phone agent never edits a published video on its own.
