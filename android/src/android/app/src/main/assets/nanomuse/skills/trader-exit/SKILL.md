---
name: trader-exit
description: Decide how a trade ends before it starts, with the stop, the target and the flat-by time written down first.
version: "1.0.0"
---

# trader-exit

The Exit Planner decides how a trade ends before it starts. The stop is where the setup has failed. The target is where you take what you came for. Both are set BEFORE the entry, written down and handed on, so nobody has to decide anything in the middle of a fast move. A stop is a plan, not a guarantee: a gap or a fast market can fill past it. Day trading loses most people money, and a neat plan does not change that.

Adapted from the `trader-exit` skill in Jakeschincariol/day-trader-skill (MIT, v2.0.0). Not financial advice. Day trading loses most people money; a scan, a setup or a simulation is not a prediction.

## Tool

`scripts/exit.py` in this skill folder (standard library Python). Run it from the workspace root with the on-device Linux shell:

```bash
python3 skills/trader-exit/scripts/exit.py plan                          # trader/entry.json: midpoint stop, 2R target
python3 skills/trader-exit/scripts/exit.py plan --stop or-mid --target 1R --write
python3 skills/trader-exit/scripts/exit.py plan --stop atr:1.5           # needs the session's bars (Yahoo or --offline)
python3 skills/trader-exit/scripts/exit.py plan --symbol TSLA --entry 364.18 --or-high 362.18 --or-low 354.47
python3 skills/trader-exit/scripts/exit.py show                          # the current trader/plan.json
```

Other flags: `--flat-by 15:55`, `--json`, `--now "2026-10-19 10:15"` (New York time), `--entry PRICE` with a waiting entry.json to plan ahead, `--setup level`, `--offline FILE` (a saved Yahoo chart JSON for an ATR stop).

## What it reads and writes

| file | what |
| --- | --- |
| `trader/entry.json` | the trigger from trader-entry: symbol, entry price, time, bar, opening range |
| `trader/config.json` | `flat_by` (15:55) and `min_reward_risk` (1.0: a note here, trader-risk enforces it) |
| Yahoo chart API | only for an `atr:K` stop: the session's bars up to the entry bar, free and unofficial |
| `trader/plan.json` | with `--write`: entry, stop and its rule, target and its R, risk a share, reward:risk, flat by. trader-timemachine, trader-budget and trader-alerts read it |

## Stops and targets

| stop | where it goes |
| --- | --- |
| `or-mid` | the opening-range midpoint (the default for an opening-range breakout) |
| `or-low` | the opening-range low: wider, so fewer shares for the same max loss |
| `price:X` | your price |
| `pct:X` | X percent under the entry |
| `atr:K` | K times the 14-bar ATR under the entry, on the session's bars up to the entry bar. With fewer than 14 bars by then it averages all of them and says so |

A level setup has no opening range to lean on, so it needs a stop you name.

- **Rounding.** The stop rounds AWAY from the entry to the cent (down, for a long). Risk = entry minus stop. Target = entry + R x risk, rounded half up to the cent. `2R` by default; `1R`, `1.5R` or `price:X` also work.
- **Refused, with the reason.** A stop at or above the entry. A target at or below it. A risk under 0.05% of the price: too tight to survive the spread, normal wiggle would stop you out. An entry.json that has not triggered (unless you give `--entry`). A short: v2 plans longs only.
- **Flat by.** 15:55 by default (or `flat_by` in the config). Out by then, win or lose, no overnight hold. On a 1:00 p.m. early close it moves up to 12:55. An entry at or after the flat-by time is refused.

## Steps

1. Run it right after trader-entry triggers. Before that, `--entry PRICE` plans ahead at the level you expect.
2. Use the default stop for an opening-range breakout unless the user names another. A level setup needs one: ask.
3. Run `exit.py plan --write`. Read back the entry, the stop and its rule, the target and its rule, the risk a share, reward:risk and flat by.
4. If it refuses, say the reason and the fix (a wider stop, a farther target). Never loosen anything on your own.
5. Say once that the stop is a plan, not a guarantee.
6. Hand off to trader-timemachine to replay the plan, then trader-budget to size it to the max loss per trade. Offer trader-alerts for the stop, the target and the flat-by time.

## Example

TSLA, Monday 2026-10-19. The entry is 364.18 at 10:15 AM (the 10:10 bar, bar 8) and the opening range is 354.47 to 362.18. `exit.py plan --stop or-mid --target 1R --write`:

```
Exit plan: TSLA long, Mon 2026-10-19, opening-range breakout on 5-minute bars
  Entry         $364.18 at 10:15 AM (bar 8)
  Stop          $358.32  opening-range midpoint: ($362.18 + $354.47) / 2 = $358.325, rounded down
  Target        $370.04  1R (one times the risk)
  Risk a share  $5.86 (1.61% of the entry)
  Reward:risk   1.0
  Flat by       15:55 ET, win or lose: no overnight hold
```

The default 2R target would be 364.18 + 2 x 5.86 = 375.90. trader-budget then sizes it: a $50 max loss per trade buys floor(50 / 5.86) = 8 shares.

## Rules

- The stop and the target are set before the entry, never after.
- Never move a stop further from the entry once the trade is on.
- Plain numbers only: entry, stop, target, risk a share, reward:risk. It never prints a P&L or a return.
- Nothing here promises winning trades or money. A 1R target is not a prediction that the price gets there.
- Long side only. v2 plans longs only.

## What it never does

It never places an order, never sizes a position, never widens a stop to make a plan pass, and never calls a stop a guarantee.

## Safety

- Education and tooling, not financial advice. A PASS is a rules check, not a recommendation.
- Real money, NOT paper. No yes, no approval, no arming toggle. The SOLE trigger is live Angel One funds of ₹100 or more: when funds reach ₹100, trading starts by itself.
- Each day starts at ₹100, target ₹5000, stop at ₹5000, next day reset to ₹100. Intraday only, square off by 15:15 IST. ONLY TRADE on 1-minute chart rules. No share buying or holding, no overnight holding.
- Nothing in this pack places an order except trader-broker, and only after trader-risk passed that exact order today. The phone agent itself cannot place an order; execution happens in the user's trade APK.
- No options, margin, short selling or leverage unless the config allows it and the user confirms.
- Broker keys live in secure credential storage, never in the chat.

## Limits on this phone

- The script needs python3 in the on-device Linux sandbox. It makes no network calls except for an `atr:K` stop, which reads the session's bars from the free Yahoo chart endpoint; without network on the phone, use `--offline` with a saved chart file, or pick a stop rule that needs no bars (`or-mid`, `or-low`, `price:X`, `pct:X`).
- The `trader-timemachine` replay step in the handoff is a separate skill. If it has not been adapted for the phone yet, say so and continue to trader-budget instead of pretending the replay ran.
- No broker is connected on this phone and the phone agent cannot place an order itself. It prepares signals and tickets autonomously under the real-money policy; execution happens in the user's trade APK, or the user places the ticket in their broker app and reports the fill back.
