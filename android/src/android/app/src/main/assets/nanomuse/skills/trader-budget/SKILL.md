---
name: trader-budget
description: Turn a trade plan into an exact bracket order size inside the user's budget, max loss and position rules.
version: "1.0.0"
---

# trader-budget

The Budget Manager turns a plan ("buy TSLA at 364.18, stop 358.32") into an exact order ("buy 8 shares"), inside rules the user sets once. The stop decides the size: the further away the stop, the fewer shares. It writes a proposal only. trader-risk checks it and trader-broker hands the ready ticket to the trade APK. No yes is asked. Day trading loses most people money. Sizing to a max loss keeps each loss small; it does not make a trade win.

Adapted from the `trader-budget` skill in Jakeschincariol/day-trader-skill (MIT, v2.0.0). Not financial advice. Day trading loses most people money; a scan, a setup or a simulation is not a prediction.

## Tool

`scripts/size.py` in this skill folder (standard library Python, no network). Run it from the workspace root with the on-device Linux shell:

```bash
python3 skills/trader-budget/scripts/size.py                        # trader/plan.json plus trader/config.json
python3 skills/trader-budget/scripts/size.py --write                # write trader/orders.json
python3 skills/trader-budget/scripts/size.py --limit --write        # a limit order at the plan's entry
python3 skills/trader-budget/scripts/size.py --limit 364.00 --json  # a limit order at 364.00
python3 skills/trader-budget/scripts/size.py --weights AAPL=40,KO=60 --config split.json   # weights mode, below
```

`--now "2026-10-19 10:15"` pins the clock (New York time). A plan for another day is refused: make a new one with trader-exit.

## What it reads and writes

| file | what |
| --- | --- |
| `trader/config.json` | the rules (start from `references/config.example.json` in this folder) |
| `trader/plan.json` | the trade: entry, stop, target, setup, flat-by time (trader-exit writes it) |
| `trader/prices.json` | optional: a fresh price above the plan's entry means a bigger risk (trader-alerts writes it) |
| `trader/portfolio.json` | optional: its cash caps the size (trader-broker keeps it) |
| `trader/orders.json` | the one proposed bracket order it writes with `--write` |

## Step 1: set the rules once

If `trader/config.json` does not exist, copy `references/config.example.json` into `trader/config.json` and ask the user for each value in plain words:

- **budget**: how much money this is for. Only money they can afford to lose.
- **cash_buffer_pct**: how much always stays in cash (default 10).
- **max_loss_per_trade_usd**: the most one trade may lose if the stop fills at the stop (default 50).
- **max_position_pct**, **max_order_usd**, **min_order_usd**: the biggest position (75% of the budget), the biggest order ($4,000) and the smallest order ($5).
- The day limits trader-risk enforces: **max_daily_loss_usd** (150), **max_trades_per_day** (3), **min_reward_risk** (1.0), **require_stop** (true), **no_entries_before** / **no_entries_after** ("09:45" / "15:30") and **flat_by** ("15:55"), all New York time.
- **blocked**: tickers they never want to trade.
- Real money, NOT paper: the standing policy is autonomous real-money trading (sole trigger live Angel One funds of ₹100 or more).

## Step 2: size the trade

```bash
python3 skills/trader-budget/scripts/size.py --write
```

Whole shares only: a bracket order takes no fractions. The size is the smallest of four limits:

| limit | shares |
| --- | --- |
| max loss | `max_loss_per_trade_usd` / risk a share (entry minus stop) |
| position cap | budget x `max_position_pct` / entry |
| max order | `max_order_usd` / entry |
| cash | (budget minus the cash buffer) / entry, never more than the cash in `trader/portfolio.json` |

The entry it sizes at: the limit price with `--limit`. For a market order, the plan's entry, or the price in `trader/prices.json` if it is fresh (under 15 minutes old) and higher. A price that ran up since the plan means a bigger loss at the same stop, so fewer shares. A lower price never shrinks the risk.

It refuses, and writes no order, when: the size is zero shares, the order is under the minimum, reward:risk is under `min_reward_risk`, the stop is not below the entry, the target is not above it (the move already happened), the plan is for another day, the ticker is blocked, or the plan is a short. Say the reason to the user in one line.

## Step 3: show the user

Show the four limits, which one decided the size, the shares, the dollars and the max loss at the stop. Say plainly that a stop is an order, not a promise: in a fast market it can fill below the stop. Then hand over to trader-risk.

## Example

TSLA on Monday 2026-10-19. Opening-range breakout: entry 364.18 at 10:15 AM, stop 358.32 (the opening-range midpoint), target 370.04 (1R). Risk 5.86 a share. The example config: $5,000 budget, 10% cash buffer, $50 max loss.

| limit | the math | shares |
| --- | --- | --- |
| max loss | $50 / 5.86 | **8 (decides it)** |
| position cap | 75% of $5,000 = $3,750 / 364.18 | 10 |
| max order | $4,000 / 364.18 | 10 |
| cash | $5,000 minus $500 = $4,500 / 364.18 | 12 |

8 shares = $2,913.44. Max loss at the stop: 8 x 5.86 = $46.88. It writes:

```json
{"symbol": "TSLA", "side": "buy", "qty": "8", "type": "market", "time_in_force": "day",
 "order_class": "bracket", "take_profit_limit_price": "370.04", "stop_loss_stop_price": "358.32",
 "why": "opening-range breakout on TSLA: 8 shares, bound by your $50 max loss per trade (8 x 5.86 = $46.88 at the stop)",
 "plan": {"entry": 364.18, "stop": 358.32, "target": 370.04, "risk_per_share": 5.86, "max_loss": 46.88,
          "setup": "orb", "entry_time": "10:15 AM", "flat_by": "15:55"}}
```

`why` and `plan` are notes for trader-risk and trader-broker. They are never sent to a broker. With the stop at 300.00 instead, one share would risk 64.18, more than the $50 max loss: no order. At 10:52 the real quote was 366.48, so the same plan sizes to 6 shares at 8.16 a share, and reward:risk falls to 0.44: refused, because the move it was waiting for has mostly happened.

## Weights mode (kept from v1)

`--weights AAPL=22.04,KO=10.86` or `--targets FILE` (`references/targets.example.json` here) splits a budget across a list by weight: the cash buffer comes off the top, weights are renormalised to 100, any stock over `max_position_pct` is pinned at the cap and its excess shared out, slices under the minimum order are dropped, and amounts round to whole dollars so nothing is left idle by accident. With `"max_position_pct": 25` and `"fractional": true`, the example file gives AAPL $1,250 (capped), AXP $1,119, GOOGL $823, KO $708, BAC $600. With `"fractional": false` (the example config) it buys whole shares and needs prices (`--prices trader/prices.json`). These are plain buys with no stop: trader-risk blocks them while `require_stop` is true and counts each one against `max_trades_per_day`. Use a separate config (`--config`) for a long-term split.

## Rules

- Proposals only. This skill never calls a broker tool.
- Never size money the user has not said is for this. Ask for the budget and the max loss.
- Every number comes from the config, the plan or the prices. Nothing is guessed.
- No projections: never say what a trade will make. A target is a plan, not a forecast.
- Free Yahoo prices are unofficial and can lag. The broker's price is the one that fills.

## What it never does

It never places, approves or schedules an order, never raises a limit on its own, never shrinks a stop to fit more shares, and never sizes options, margin or short positions.

## Safety

- Education and tooling, not financial advice. A PASS is a rules check, not a recommendation.
- Real money, NOT paper. No yes, no approval, no arming toggle. The SOLE trigger is live Angel One funds of ₹100 or more: when funds reach ₹100, trading starts by itself.
- Each day starts at ₹100, target ₹5000, stop at ₹5000, next day reset to ₹100. Intraday only, square off by 15:15 IST. ONLY TRADE on 1-minute chart rules. No share buying or holding, no overnight holding.
- Nothing in this pack places an order except trader-broker, and only after trader-risk passed that exact order today. The phone agent itself cannot place an order; execution happens in the user's trade APK.
- No options, margin, short selling or leverage unless the config allows it and the user confirms.
- Broker keys live in secure credential storage, never in the chat.

## Limits on this phone

- The script needs python3 in the on-device Linux sandbox. It makes no network calls itself, but it reads `trader/prices.json`, which only stays fresh if trader-alerts can fetch quotes on the phone. If the sandbox has no network, use prices the user reports from their broker app, and say the price source.
- The example config in `references/config.example.json` has the SEC User-Agent field blanked to a placeholder with no real email. The user must enter their own name and email there before trader-analyst or trader-news can read SEC filings.
- No broker is connected on this phone and the phone agent cannot place an order itself. It prepares signals and tickets autonomously under the real-money policy; execution happens in the user's trade APK, or the user places the ticket in their broker app and reports the fill back.
