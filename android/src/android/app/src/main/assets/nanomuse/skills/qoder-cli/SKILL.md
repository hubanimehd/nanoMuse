---
name: qoder-cli
description: Use the Qoder CLI coding agent safely from the shell, with sign-in or a user supplied key first and no permission bypass without approval.
version: "1.0.0"
---

# Qoder CLI

Use this skill when the user asks for coding work through the Qoder CLI.

## What it is

- Qoder CLI is a command line coding agent. The desktop IDE is not used here, and the user is phone-only, so all work runs through the shell.
- Nothing here pays, publishes, or uploads on its own.

## Run it

Run from the on-device Linux shell, inside the project directory the user named:

```bash
qodercli -p "your coding task" --cwd <project-dir>
qodercli --list-models
```

Note: `--list-models` needs sign-in or a user supplied key first.

## Before a real model run

Check sign-in or a user supplied key first. Qoder needs either:

- a Qoder account sign-in (email, Google or GitHub), or
- a user supplied key (bring your own key)

If neither is set up, stop and ask the user for that one step instead of guessing or inventing a key. Never invent, guess or reuse a key from another service. Never write a key into a file, a log or a report.

## Safety

- Never use `--dangerously-skip-permissions` or `--permission-mode bypass_permissions` without the user's explicit approval for that exact run.
- Default permission mode is the safe default. Keep edits inside the project directory the user named.
- Never pay for anything.

## Standing rules

- Nothing uploads or publishes without the user's explicit approval of the draft first.
- Never pay for anything.
- Trading: no order without the user's explicit yes for that exact order plus a risk PASS. Paper trading is the default.

## Limits on this phone

- Qoder CLI is a VM and desktop tool. It is not installed on this phone and there is no on-device Qoder app here. Coding runs through this skill need a server, relay or VM where the CLI is installed, or the user installs it on a machine they control.
- On this phone, do the planning, file review and prompt preparation that need no CLI run. Hand the actual CLI run to a server route, and verify the returned files on device before delivery.
- Do not claim a Qoder run succeeded unless you can see its output files or its run conclusion at the source of truth.
