---
name: opencut
description: Use the OpenCut video editor, choosing between the working classic editor and the in-progress rewrite, and know exactly which one can edit today.
version: "1.0.0"
---

# OpenCut: video editing (two repos, kept separate)

Use OpenCut for video-editing work. This skill covers TWO different repos. They are not one editor, and they must not be merged into a single workflow.

## The two projects

- Rewrite: clone of https://github.com/OpenCut-app/OpenCut.git, HEAD `e668010` at last check. License: MIT, Copyright 2026 OpenCut.
- Classic: clone of https://github.com/OpenCut-app/opencut-classic.git, HEAD `cf5e79e` at last check. License: MIT, Copyright 2025-2026 OpenCut.
- Nothing was pushed to either upstream. The clones are local inspection and reference copies only. On the phone, keep reference copies under workspace-relative paths (for example `workspace/opencut` and `workspace/opencut-classic`) and never use absolute VM paths.

## Which one to use

### Default for real editing: opencut-classic

Classic is archived and no longer maintained, but it is the FULL working editor today, and it is what opencut.app still runs.

- Stack: Next.js web app (`apps/web`) + Rust/WASM core (`rust/`), Bun 1.2.18, Turborepo. No `packages/` directory in this checkout.
- Editor code found in `apps/web/src`: timeline, canvas, effects, masks, export, rendering, media, project, sounds, speed, retime, panels.
- Run locally (from its README, verify before use):
  1. `cp apps/web/.env.example apps/web/.env.local`
  2. Optional: `docker compose up -d db redis serverless-redis-http` (only for the database/Redis parts; frontend-only work can skip it)
  3. `bun install`
  4. `bun dev:web` then open http://localhost:3000
- Hosted alternative: https://opencut.app
- Limits: archived means no upstream fixes are coming. Do not promise maintenance, and do not claim a native Android or phone-app version of classic exists. It is a web editor, used in a browser or WebView.

### Only for exploration / future work: OpenCut rewrite

The rewrite is in progress and is NOT a working editor in this checkout. Do not claim it can edit video end-to-end.

- Stack: Moon monorepo; web app `apps/web` is Vite + React 19 + TanStack Start (package `@opencut/web`, private), API `apps/api` is a Cloudflare Worker stub (Elysia), desktop `apps/desktop` is GPUI.
- Verified current state: the `/editor` route renders only "Coming soon", the home route renders only "hello world". `apps/web/src` holds a large shadcn-style UI component set, not timeline/export logic.
- Its README lists as coming (not present): Editor API, first-class plugins, desktop/mobile/browser from one Rust-core codebase, an MCP server for AI agents, headless mode, scripting tab.
- Run/inspect locally (per its README, verify before use): `proto use`, then `moon run web:dev` for localhost:5173 and `moon run api:dev` for localhost:8787.
- Hosted preview: https://new.opencut.app (rewrite preview, not the working classic editor).

## Rules

- For an actual edit (cut, timeline, export), use classic, locally or at opencut.app, and say so. For the rewrite, only explore UI components or track its progress; never present its "Coming soon" editor as finished.
- Do not merge the two codebases, do not copy one repo's code into the other, and do not claim either is a native Android editor.
- No payment, no account creation, and no credential is part of this skill. Local dev uses the repos' own example environment files only.
- Nothing uploads or publishes without the user's explicit approval of the draft first.

## Limits on this phone

- Neither repo is a native Android editor. On the phone, classic runs only as a web editor in the browser or a WebView, either hosted at opencut.app or served from a machine that can run Bun and the dev server.
- Building or running either repo from source (Bun, Moon, Rust/WASM, Docker) needs a server, relay or VM. The phone agent can open the hosted editor, prepare media and an edit plan, and inspect the repos, but it cannot build or serve the editor itself.
- The rewrite has no working editor at all. Do not offer it as an editing path.
