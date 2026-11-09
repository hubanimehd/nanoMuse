---
name: browser-auth-session-discipline
description: Run sign-in dependent browser work without burning approvals, keep one sign-in flow at a time, and hand bot checks to the user.
version: "1.0.0"
---

# Browser Auth Session Discipline

Use this skill for any multi-step browser work behind a sign-in, and before scheduling detached work that depends on a signed-in session.

## Verify the session before the work

- Read the signed-in indicator on the live page in the same sitting before starting a multi-step task behind a sign-in. The indicator is the source of truth, not the account you intended to use.
- Request interactive approvals only while the user is live. Number prompts and email codes expire within minutes, and a prompt sent while the user is away stalls the whole task.
- Re-verify the session before scheduling detached work that depends on it. A schedule created on a stale session fails at the sign-in wall with nobody awake to approve.

## Keep one sign-in flow at a time

- Run one interactive sign-in flow per site at a time. Open the prompt once and resolve it before doing anything else on that site.
- Send the code or tap the number the moment it appears. Waiting lets the code expire and forces another prompt.
- Never open a second approval prompt while the first is unresolved. Two parallel flows for one site drop both sessions.
- Stop at two approval prompts per site per sitting. The third ask means the route is broken, so switch routes instead of burning more approvals.

## Hand bot checks to the human

- When a puzzle check or Cloudflare challenge breaks the session, stop opening new sessions. Each new attempt looks more automated than the last.
- Hand the live browser to the user to solve the check, then take control back. Tell the user plainly what broke and that no new prompt will come from you until they confirm it is solved.
- After a bot-check break, re-verify the session indicator before continuing. The session may need a fresh sign-in even after the check is solved.

## Fail a detached run at the wall

- When a scheduled run pauses with "needs an action only the user can take", surface the exact approval step to the user. Do not silently retry the same run.
- Treat the wall as the schedule design flaw, not the run failure. Reschedule so a verified session and an awake user overlap instead of queueing another attempt behind the same sign-in.
- For generation work that can wait for the user, hand the manual step over with the prepared prompts. The user generates by hand, you verify and assemble after.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.
- Trading: no order without the user's explicit yes for that exact order plus a risk PASS. Paper trading is the default.

## Tips

- When the work is Google Flow image or video generation, follow the flow-generation-ops skill for its abuse-flag discipline and route arounds instead of duplicating them here.
- When the task produces files, run the verify-browser-task-artifacts skill after the browser task so the rest of the work starts from files that exist on device.
- Search results never confirm a session is alive. Only the live page indicator counts.
- Google sign-in: https://accounts.google.com/ for Google gated work. Carry the expected account into the browser step and read the account indicator on the live page before starting.
- Claude: https://claude.ai/ for Claude gated work. Carry the expected login method into the browser step, and verify the session indicator before a multi-step task or a detached schedule.

## Limits on this phone

- The phone agent cannot tap sign-in approvals, number prompts or bot checks itself. Those steps always go to the user on the live browser.
- Detached runs cannot approve anything. Do not schedule sign-in dependent work for hours when the user cannot approve.
