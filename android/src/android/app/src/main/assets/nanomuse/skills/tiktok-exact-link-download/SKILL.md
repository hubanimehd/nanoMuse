---
name: tiktok-exact-link-download
description: Download a TikTok video from its exact video link, hand puzzle checks to the user, and verify the file on device before reporting success.
version: "1.0.0"
---

# Download a TikTok Video from Its Exact Link

Use this skill when the user wants a specific TikTok video downloaded.

## Start from an exact video link

- Open the exact video page from the link the user gave or from the link of the video now playing.
- Download only that single video and stop when the download finishes.
- Verify the downloaded file on device before you report success.

## Ask for the link when you only have a creator

- Ask the user for the exact video link when you know only a creator name or a video description.
- Offer one live browser step by the user as the alternative when the user cannot give a link.
- Do not browse the creator profile grid to hunt for the video.
- Wait for the exact link or for the user completed step before you try again.

## Hand a puzzle check to the user

- Stop at the first puzzle check or bot challenge.
- Hand the live browser to the user and name what broke in plain words.
- Take control back only after the user confirms the check is solved.
- Re-verify the session indicator before you continue after a solved check.

## Verify before you report

- Run the verify-browser-task-artifacts skill after every download.
- Quote only the file count, size and duration you measured on device.
- Report a download as failed when the expected file is missing, even when the browser task reported success.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.

## Limits on this phone

- Downloading runs through browser automation or a download tool available on the phone. There is no VM-only downloader here. If the site blocks automated download with a puzzle check, stop and hand the step to the user instead of retrying.
- TikTok: https://www.tiktok.com/ for the exact video page. Carry the exact video link into the browser step and verify the downloaded file on device before reporting.
- Follow the browser-auth-session-discipline skill when a sign-in or bot check breaks the session.
- Ask for the exact link early instead of spending further attempts on a grid that has already shown a puzzle check.
