---
name: flow-generation-ops
description: Run Google Flow image and video generation with discipline, recognise abuse flags and content refusals, and recover safely.
version: "1.0.0"
---

# Google Flow Generation Operations

Use this skill when generating images or video with Google Flow through browser automation on this phone.

## Submit with discipline

- Before submitting, check the credit quote in the composer. When Flow defaults to a higher tier and quotes more than seven credits for a ten second 360p clip, pick the 360p option that costs seven.
- Generate multi-part video one clip at a time. Finish clip N and download it before submitting N+1.
- Never queue several prompts at once. Queued parts drift into separate videos instead of one continuous video.
- When the tool offers to continue from the previous clip's last frame at no cost, take it. The parts stitch together like one video.
- Quote only verified credit figures to the user. Images are free, a ten second 360p Omni 1.1 Flash clip costs seven credits, and an eight second 9:16 Veo 3.1 Lite clip costs ten. The daily pool refreshes in the evening, and the UI only says reset daily.

## Recognise the abuse flag

- Learn the block. Flow refuses at the planning step with a server-side "unusual activity" message, before any generation starts. Flagged attempts spend zero credits.
- Do not confuse "unusual activity" with "The agent is experiencing high demand". High demand is a transient capacity error, not the abuse flag. One retry through Try again is permitted for it and usually succeeds. For "unusual activity", never retry. Stop and cool down.
- Expect it after rapid automated submissions. The observed pattern is about three to four generations per thirty to forty minute window, then a block.
- Do not treat the flag as tier, browser or project specific. It persists on paid tiers, in fresh browser sessions and in brand new Flow projects. Switching browsers or routing through another service does not bypass it.
- Read the flag from the screen or the task report and stop submitting at once. Extra submissions only deepen the block.

## Recognise a content refusal

- Flow refuses a prompt with a prominent people message before generation starts. Refused attempts spend zero credits.
- Do not confuse a content refusal with unusual activity. Unusual activity blocks the session, and a content refusal blocks that prompt content. Switching session or project does not clear a content refusal.
- Expect the refusal to follow the content, not one ingredient or one project. On 2026-11-07, 26 refusals hit one batch of scenes, only one scene succeeded, and a reworded probe with no ingredients still failed.
- Try a refused scene at most twice in one day. Reword the preamble once and drop character ingredients on the second try.
- When both tries fail, stop that scene for the day. Record the scene as blocked, keep the clips already on device, and move to a different task.
- Use the text only fallback when a scene keeps refusing. Earlier text only prompts passed for scenes where ingredient prompts failed.
- Do not run the abuse-flag recovery sequence for a content refusal.

## Recover with discipline

- Run the recovery sequence once: press Try again, then open a new session, then reload the page. When all three fail, stop.
- Never loop retries. Report the exact state: what is safe on device, what is blocked, and the two options of a cooldown wait or a manual takeover.
- After a thirty to forty minute cooldown, probe with one submission. When it succeeds, continue at the slower pace. When it fails, stop and switch routes.
- For images at scale, submit in agent mode batches of about ten prompts when normal submission stalls. That route completed a stalled batch at zero credits.
- Carry the full style block in every prompt when the agent session does not carry it. A "STYLE +" placeholder in prompts silently produced a wrong style (3D instead of flat 2D) in a fresh session. Reuse an existing session whose history holds the style, and still include the full style text in each prompt.
- Verify downloads on device. Flow can show "Your image has been downloaded!" while the file never lands. After a download task, count the files on device and re-download any missing ones before filing.

## Route around a persistent block

- When the flag persists across cooldowns, move image work to another image generator the user already uses. That fallback produced a full remaining batch with zero rate limit failures.
- Move video work to the user for manual generation in the live browser. Human clicks do not trip the flag.
- Or schedule a detached retry for a time the user is awake to approve sign-in. Detached runs cannot tap approvals.

## Schedule detached work safely

- Verify the browser Google session is alive in the same sitting that creates a Google-dependent schedule. A password challenge that appears at night stalls with nobody to approve it.
- Do not schedule Google-dependent work for hours when nobody can approve. A run that hits a sign-in wall fails and burns its slot.
- When a scheduled run pauses with "needs an action only the user can take", surface the exact approval step to the user. Do not silently retry the same detached run.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.
- For encodes longer than about fifteen minutes, render in parts that skip completed segments and concatenate at the end. Two long renders were killed by runtime restarts, and the part-wise driver recovered without losing progress.
- When handing video generation to the user, give them the prepared prompt file first. They generate manually, you download and assemble after.

## Limits on this phone

- Google Flow has no on-device app or API here. Generation runs only through browser automation in the phone browser, with the user's own signed-in Google session.
- Heavy video assembly and long renders need a server, relay or VM. On this phone, keep assembly light, work in small parts, and hand heavy renders to the user or a server route instead of attempting them on device.
- The phone agent cannot tap sign-in approvals or bot checks itself. Hand those to the user, as described in the browser-auth-session-discipline skill.
