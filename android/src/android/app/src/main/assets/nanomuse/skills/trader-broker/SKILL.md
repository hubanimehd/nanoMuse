---
name: trader-broker
description: Prepares real-money trade tickets autonomously under the user's policy, and records every signal, ticket, fill and failure. It cannot place an order itself.
version: "1.0.0"
---

# trader-broker

The Order Executor is the only skill in this pack that can lead to a real order, so it is the strictest. It works under the user's autonomous real-money policy: real money, NOT paper, no yes, no approval, no arming toggle. The SOLE trigger is live Angel One funds of ₹100 or more. When funds reach ₹100, trading starts by itself. Each day starts at ₹100, target ₹5000, stop at ₹5000, next day reset to ₹100. Intraday only, square off by 15:15 IST. ONLY TRADE on 1-minute chart rules. No share buying or holding, no overnight holding. The one gate that stays is trader-risk: an order goes forward only if trader-risk passed that exact order today (matched by fingerprint). This skill prepares the ticket and records the outcome; it cannot place an order itself. Execution happens in the user's trade APK, or the user places the ticket in their broker app.

Adapted from the `trader-broker` skill in Jakeschincariol/day-trader-skill (MIT, v2.0.0). Not financial advice. Day trading loses most people money; a scan, a setup or a simulation is not a prediction.

## Tool

`scripts/ticket.py` in this skill folder (standard library Python). It never talks to a broker. It renders tickets, builds the flatten ticket, and keeps the log in `trader/state/broker.json`. Run it from the workspace root with the on-device Linux shell:

```bash
python3 skills/trader-broker/scripts/ticket.py show                     # every ticket, READY or not
python3 skills/trader-broker/scripts/ticket.py result --index 1 --status placed --broker-id ID --filled-qty 8 --filled-price 364.20
python3 skills/trader-broker/scripts/ticket.py fill --symbol TSLA --side sell --qty 8 --price 370.04
python3 skills/trader-broker/scripts/ticket.py flatten --symbol TSLA --qty 8
python3 skills/trader-broker/scripts/ticket.py log
```

`--now "2026-10-19 10:15"` pins the clock (New York time).

## What it reads and writes

| file | what |
| --- | --- |
| `trader/orders.json` | the proposed orders (trader-budget writes it; `flatten` replaces it) |
| `trader/state/risk.json` | trader-risk's verdicts; only a PASS from today is READY |
| `trader/state/broker.json` | every signal, ticket, placed, failed and filled, with each entry's max loss |
| `trader/portfolio.json` | cash and shares, updated by `fill` and by `result --filled-qty` |
| `trader/plan.json` | the flat-by time for `flatten` |

## Broker setup

No broker is connected by this skill on the phone. The ticket tool only prepares and records orders; placing one needs a broker connection, which does not exist on the phone and is not invented here (see Limits on this phone). Use this skill to build the ticket, get the risk PASS, hand it to the trade APK for execution, and record the outcome reported back. Never invent a broker order ID or a fill.

When a broker integration is added later, it must follow these rules, adapted from the desktop setups in the original skill:

- Real money, NOT paper. This is the user's standing order; the agent never switches the policy back to paper on its own.
- The order flow never asks the user for approval. The trader-risk PASS is the only gate before a ticket goes to the trade APK.
- There is no order tool on the phone at all: no `place_stock_order`, cancel, replace or close-position tool exists here, and none is invented. Read-only tools (account info, positions, orders, clock, quotes) may be always allowed.
- Option, crypto, close-all-positions and cancel-all-orders tools stay blocked unless the user has a reason and says so.
- **Keys never go in the chat.** Never ask for them, never read a config file containing them back into the conversation, and if the user pastes a key anyway, tell them to delete that message and make new keys in the broker dashboard. Keys live in the device's secure credential storage or the broker's own sign-in flow, never in a chat message or a workspace file.

**Bracket orders.** A bracket order takes a take-profit limit price and a stop-loss stop price (and an optional stop-loss limit price). When either is there, the order class is "bracket"; trader-budget writes it anyway so the ticket shows it. A bracket is whole shares only, with time in force day or gtc. After the entry fills, the stop-loss and the take-profit wait at the broker as two orders that cancel each other. They are day orders too, so they end at the close: one more reason to be flat by then.

## Steps: the entry

1. `ticket.py show`. Only READY tickets (trader-risk PASS today) go forward to the trade APK. A ticket that is NOT READY or BLOCKED goes back to trader-risk.
2. Hand the ticket to the trade APK for execution, with exactly the JSON the ticket printed. The phone agent has no broker connection and cannot place the order itself. No yes is asked at any point.
3. Record what happened: `ticket.py result --index N --status placed --broker-id <id> --filled-qty Q --filled-price P` (the fill from the broker response or the open position), or `--status failed` with `--note`. A failed order is not retried without a new risk check. The log keeps the entry's max loss for trader-risk's daily limit.

## Steps: while it is open, and out by the close

- trader-alerts watches the stop, the target and the flat-by time (`alerts.py add --from-plan`). The bracket's own orders do the stopping.
- When the stop-loss or the take-profit fills, record it: `ticket.py fill --symbol TSLA --side sell --qty 8 --price 358.32`.
- By `flat_by` (15:55 New York), anything still open gets closed:
  1. Read how many shares are held from `trader/portfolio.json` (kept by this skill from reported fills).
  2. `ticket.py flatten --symbol TSLA --qty 8` writes a market sell for those shares to `trader/orders.json`.
  3. trader-risk. A sell that only closes what the user holds is never blocked by the daily limits or the entry window.
  4. `ticket.py show`, then hand the ticket to the trade APK for execution. No yes is asked.
  5. The bracket's stop-loss and take-profit still hold those shares, so the broker rejects a new sell until they are canceled: list the open orders, cancel each bracket leg, then hand the order to the trade APK, then `result`.

## Example

```
ORDER 1 of 1, REAL-MONEY account
  BUY 8 TSLA, market, good for today
  stop-loss 358.32
  take-profit 370.04
  max loss about $46.88 if the stop fills at 358.32 (it can fill worse in a fast market)
  why: opening-range breakout on TSLA: 8 shares, bound by your $50 max loss per trade (8 x 5.86 = $46.88 at the stop)
  READY: trader-risk PASS, goes to the trade APK
  The agent will call place_stock_order with exactly:
    {"symbol": "TSLA", "side": "buy", "qty": "8", "type": "market", "time_in_force": "day",
     "order_class": "bracket", "take_profit_limit_price": "370.04", "stop_loss_stop_price": "358.32"}
```

The agent hands the ticket to the trade APK with exactly that JSON. The trade APK executes it under the real-money policy, and the agent records the fill when it is reported back. The `why` and the plan stay in the file; they are never sent.

## What it never does

- Never places an order without a trader-risk PASS for that exact order today, and never places an order itself at all. Execution is the trade APK's job.
- Never asks the user for a yes, and never waits for one: the policy runs by itself.
- Never asks for, reads, stores or repeats API keys or passwords.
- Never switches the policy back to paper on its own. Real money is the user's standing order.
- Never calls close-all-positions, cancel-all-orders, option or crypto order tools on its own.
- Never promises a fill price: a market order and a stop fill at whatever the market gives.

## Safety

- Education and tooling, not financial advice. A PASS is a rules check, not a recommendation.
- Real money, NOT paper. No yes, no approval, no arming toggle. The SOLE trigger is live Angel One funds of ₹100 or more: when funds reach ₹100, trading starts by itself.
- Each day starts at ₹100, target ₹5000, stop at ₹5000, next day reset to ₹100. Intraday only, square off by 15:15 IST. ONLY TRADE on 1-minute chart rules. No share buying or holding, no overnight holding.
- Nothing in this pack places an order except trader-broker, and only after trader-risk passed that exact order today. The phone agent itself cannot place an order; execution happens in the user's trade APK.
- No options, margin, short selling or leverage unless the config allows it and the user confirms.
- Broker keys live in secure credential storage, never in the chat.

## Limits on this phone

- No broker is connected on the phone, and the phone agent cannot place an order itself. There is no broker integration and none is invented here. The ticket tool renders tickets and records signals, tickets, placed, failed and filled. Execution happens in the user's trade APK under the autonomous real-money policy; or the user places the ticket manually in their broker app and reports the fill back.
- The original Alpaca desktop setup (a local server process plus a desktop config file) and the desktop connector setup are desktop only and do not run on the phone. Do not try to recreate them in the on-device sandbox.
- The desktop config example from the original skill was not copied: it holds placeholder key fields for a desktop app and does not apply here.
- The script needs python3 in the on-device Linux sandbox. If python3 is not available, tickets must be rendered by hand from the same rules, and the log kept in `trader/state/broker.json`.
