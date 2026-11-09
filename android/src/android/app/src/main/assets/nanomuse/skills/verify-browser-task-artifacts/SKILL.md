---
name: verify-browser-task-artifacts
description: Verify files a browser task produced actually exist on device with the expected count, size and dimensions before reporting success.
version: "1.0.0"
---

# Verify Browser Task Artifacts

Use this skill after any browser task that should have produced files, and before announcing any async external result.

## Pace the task output

- Instruct the browser task to produce and download one file at a time, waiting for each download to finish before starting the next.
- Name the exact destination folder in the task instructions, such as a folder under the workspace for that run, and keep every run's files in its own folder.
- Split a large batch into smaller passes so one stalled download does not leave a half-finished set.

## Verify files on device before reporting

- After the task reports, list the destination folder with the on-device Linux shell and compare the file count against the number you expected.
- Reject zero-byte or truncated files. Open each image to confirm its dimensions and content, and check each video for real duration and a matching frame before calling the batch complete.
- Spot check content, not just presence. Open a frame or a contact sheet and confirm the subject, orientation and format match what the task was asked to make.
- Treat the task's own report as a claim, not a fact. A failed report can hide completed files on device, and a done report can hide missing ones.
- Quote only numbers you measured from the disk check, such as file counts, sizes and durations, never from the task's summary.

## Reconcile the service's own state

- When disk and report disagree, check the service's library, project list or dashboard. A vanished project or a repeated 404 explains failed downloads better than another retry does.
- When the source is gone, stop the retry loop, tell the user exactly what survived on device and what is unrecoverable, and offer regeneration as the explicit choice instead of silently retrying.

## Verify async external state before announcing

- Read the source of truth before you announce an async result. For a CI build that means the run's conclusion on the provider, for a deploy it means the live state, and for a scheduled artifact it means the file on device or the service dashboard.
- Announce nothing from a partial or stale view. A queued run, a cached page or your own draft report is not a result, and a green badge can belong to a step you misread.
- When the work depends on being signed in as a specific account, read the account indicator from the live page and name the account you actually see before claiming a sign-in or an account-bound action. The account you intended is not evidence.
- Call it done only when the artifact is in your hand or the source of truth is explicit. Hold the file on device at a non-zero size and check its duration or dimensions, or quote the run conclusion you read.
- Correct a wrong read to the user in plain words and name the true state. A fast plain correction protects more trust than a quiet fix.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.

## Tips

- When downloads keep landing in the wrong place, have the task report the full path of each saved file and diff that list against the folder you checked.
- When a connected service already covers the work, prefer its export or read operation over scraping downloads through the browser.

## Limits on this phone

- File checks use the on-device Linux shell and the phone file system, with workspace-relative paths. Never use absolute VM paths.
- CI, deploy and dashboard checks run through browser automation or a connected service on the phone. If the service needs a sign-in the phone cannot complete, hand that step to the user instead of claiming the result.
