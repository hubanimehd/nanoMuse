---
name: trader-entry
description: Turn a setup into one entry moment, the first closed bar that closes above the level, and write it down for the exit plan.
version: "1.0.0"
---

# trader-entry

The Entry Timer turns a setup into one moment: the bar that confirms it. It reads bars, 1-minute or 5-minute, and waits for each one to finish. A price that pokes above the high in the middle of a bar is not an entry. A bar that CLOSES above it is. Then it says the time and the price, writes them down for the Exit Planner, and stops. It never buys anything. Day trading loses most people money, and a clean signal does not change that.

Adapted from the `trader-entry` skill in Jakeschincariol/day-trader-skill (MIT, v2.0.0). Not financial advice. Day trading loses most people money; a scan, a setup or a simulation is not a prediction.

## Tool

`scripts/entry.py` in this skill folder (standard library Python). Run it from the workspace root with the on-device Linux shell:

```bash
python3 skills/trader-entry/scripts/entry.py check TSLA                  # 15-minute opening range, 5-minute bars
python3 skills/trader-entry/scripts/entry.py check TSLA --write          # also save trader/entry.json
python3 skills/trader-entry/scripts/entry.py check TSLA --setup level --level 371.20
python3 skills/trader-entry/scripts/entry.py check TSLA --interval 1m --min-volume-x 1.5
python3 skills/trader-entry/scripts/entry.py watch TSLA --until 15:30    # one line per closed bar, stops on the trigger
python3 skills/trader-entry/scripts/entry.py watchlist                   # every hit in trader/watchlist.json
```

Other flags: `--or-minutes N` (default 15), `--json`, `--offline FILE` (a saved Yahoo chart JSON, `.json` or `.json.gz`, or a folder of `<TICKER>.json`), `--now "2026-10-19 10:15"` (New York time) to replay a day. `watch` with `--now` or `--offline` runs on a simulated clock and replays the day bar by bar without waiting.

## What it reads and writes

| file | what |
| --- | --- |
| `trader/watchlist.json` | the scanner's hits (`hits[].symbol`), for `watchlist` |
| `trader/config.json` | `no_entries_before` (09:45) and `no_entries_after` (15:30), New York time. Optional |
| Yahoo chart API | today's bars, `range=1d&interval=5m` (or `1m`): free, unofficial, can lag |
| `trader/entry.json` | with `check --write`, or when `watch` triggers: symbol, setup, status, time, bar, price, level, opening range |

## The rule

- **Opening range (orb).** The high and low of the first 15 minutes: the 9:30, 9:35 and 9:40 five-minute bars, or fifteen 1-minute bars. The level to beat is the range high.
- **Level.** A price you give, like a 20-day high from trader-scanner.
- **The trigger** is the first completed bar after the range that CLOSES above the level. A wick above is not a trigger. A close exactly at the level is not above it.
- **The time** is that bar's close time (the 10:10 bar closes at 10:15 AM). The price is its close, to the cent.
- **Only closed bars count.** A bar is closed when the clock passes its start plus 5 (or 1) minutes and Yahoo has caught up to that moment (it waits a minute at most). Yahoo's live tick, like 10:49:45, is never a bar.
- **Volume, if you want it.** `--min-volume-x 1.5` also needs the trigger bar's volume at 1.5 times the range bars' average. The 9:30 bar holds the opening auction, so 1.0 is already strict.
- **Time limits.** No entries before 9:45 or after 15:30 (config). On a 1:00 p.m. early close the cutoff moves up with the close, to 12:30.
- **What it says.** Waiting (and how far the last close is from the level), triggered, expired, or no data. Before 9:30: not open yet. Before 9:45: the range is still forming. Weekends and NYSE holidays: the market is closed.

## Steps

1. Get the symbol from the user, or run `entry.py watchlist` after trader-scanner. Check trader-news for it first.
2. Run `entry.py check SYMBOL`. Tell the user the status in one line: waiting and how far, triggered at what time and price, or expired.
3. To wait for it, run `entry.py watch SYMBOL --until 15:30` in the background while the agent is running, or `entry.py check SYMBOL --write` on a repeat check every 5 minutes. `watch` wakes just after each bar closes, prints one line per bar, and exits 0 on the trigger (it writes `trader/entry.json`) or 1 when the setup expires. Keep the phone awake while it watches: if the phone sleeps or the app is closed, the watch pauses and catches up late when it resumes.
4. When it triggers, say the time and the price, and that nothing was placed. If the trigger is more than a bar old, say that too: a late entry is a different trade.
5. Hand off to trader-exit to set the stop and the target BEFORE anything is bought.

## Example

TSLA, Monday 2026-10-19, 5-minute bars. The opening range is 354.47 to 362.18. At 10:12 the 10:10 bar is still open, so it waits:

```
WAITING  last close $361.68 (10:05 bar), $0.50 under the range high $362.18 (-0.14%).
The 10:10 bar is still forming ($364.18 so far, closes 10:15 AM); it does not count until it closes.
```

At 10:15 the bar has closed:

```
TSLA  Mon 2026-10-19 10:15 ET  opening-range breakout (15-minute range) on 5-minute bars (Yahoo, free, can lag)
Opening range 9:30-9:45: high $362.18, low $354.47 (3 of 3 bars).
TRIGGERED 10:15 AM at $364.18: the 10:10 bar (bar 8) closed above the range high $362.18 (+0.55%).
This is the moment to get in, if you take the trade. Nothing was placed.
Next: set the stop and target BEFORE you buy: trader-exit (exit.py plan --from trader/entry.json).
```

With `--min-volume-x 1` the 10:10 bar is too thin (0.60x the range's volume) and the trigger moves to 10:35 AM at 365.75 (the 10:30 bar, 7.98x).

## Rules

- Bar by bar, on free Yahoo 1-minute or 5-minute bars. They are unofficial and can lag. Say so when a minute matters.
- A signal is information, not an order, and not a promise. Nothing here promises winning trades or money.
- Long side only. v2 plans longs only. If the user wants a short, say that.
- One trigger per setup per day: the first close above. Later closes do not reset it.
- Never call an entry early because a bar looks like it will close above.

## What it never does

It never places an order, never sizes a position, never sets the stop (that is trader-exit), never moves the level after the fact, and never counts a bar that has not closed. It never waits for a yes: under the autonomous policy a triggered entry goes on to trader-exit by itself.

## Safety

- Education and tooling, not financial advice. A PASS is a rules check, not a recommendation.
- Real money, NOT paper. No yes, no approval, no arming toggle. The SOLE trigger is live Angel One funds of ₹100 or more: when funds reach ₹100, trading starts by itself.
- Each day starts at ₹100, target ₹5000, stop at ₹5000, next day reset to ₹100. Intraday only, square off by 15:15 IST. ONLY TRADE on 1-minute chart rules. No share buying or holding, no overnight holding.
- Nothing in this pack places an order except trader-broker, and only after trader-risk passed that exact order today. The phone agent itself cannot place an order; execution happens in the user's trade APK.
- No options, margin, short selling or leverage unless the config allows it and the user confirms.
- Broker keys live in secure credential storage, never in the chat.

## Limits on this phone

- The script needs python3 in the on-device Linux sandbox and network access to the free Yahoo chart endpoint. If the sandbox has no network or no python3, bars cannot be read on the phone and the check needs a server or relay.
- `watch` only runs while the agent is running and the phone is awake. It cannot watch on its own while the app is closed or the phone sleeps; use repeated `check` runs instead, or a server.
- Free Yahoo bars are unofficial and can lag by a minute or more. Say so whenever a minute matters, and never call an entry on a bar that has not closed.
- No broker is connected on this phone and the phone agent cannot place an order itself. It prepares signals and tickets autonomously under the real-money policy; execution happens in the user's trade APK, or the user places the ticket in their broker app and reports the fill back.
