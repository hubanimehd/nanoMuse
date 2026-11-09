---
name: trader-pelosi
description: Read House Periodic Transaction Reports the moment they appear, and report what a member disclosed, with the filing delay.
version: "1.0.0"
---

# trader-pelosi

Members of Congress have to report their trades, their spouse's and their dependent children's. The House Clerk posts every report as a PDF and lists them all in one index file per year. This skill reads that index, notices the moment a new report appears, and reads the report for you.

It is fast. It is not early. A PTR is due within 30 days of the member learning of the trade and never more than 45 days after it, so by the time anyone can read it the trade is days or weeks old. There is no inside information here: only public filings, read quickly. Say that every time.

Adapted from the `trader-pelosi` skill in Jakeschincariol/day-trader-skill (MIT, v2.0.0). Not financial advice. Day trading loses most people money; a scan, a setup or a simulation is not a prediction.

## Tool

`scripts/ptr.py` in this skill folder (standard library Python; `pdftotext` from poppler is optional, to read the PDFs locally). Run it from the workspace root with the on-device Linux shell:

```bash
python3 skills/trader-pelosi/scripts/ptr.py --help
```

## What it reads and writes

- Reads the House Clerk financial disclosures index per year (one entry per filing: name, district, filing type, filing date, DocID) and each PTR PDF from the Clerk's site.
- Writes `trader/state/pelosi.json` (the DocIDs already shown, so each PTR alerts once) and keeps downloaded PDFs in `trader/state/ptr-pdfs/`.
- Reads `trader/portfolio.json` (if it exists) to flag trades in what you hold.

## The rules

- **Quote the filing, not a tracker.** Every row comes from the PTR PDF on the Clerk's site, with its DocID and link. Third-party trackers are fine for browsing but can be off (Capitol Trades showed Pelosi's trade dates one day earlier than her PTRs). The PTR wins.
- **Amounts are ranges.** "$1,000,001 - $5,000,000", never "$3 million".
- **Owner matters.** SP is the spouse, JT joint, DC a dependent child, blank the member. Pelosi's 2026 trades are all SP: Paul Pelosi.
- **Say the delay.** Each row shows how many days passed between the trade and the filing; over 45 is flagged as late.
- **Options are options.** A call bought with a strike and an expiry is not a share purchase. The tool reads contracts, strike and expiry from the description and marks exercises.

## Step 1: what has been filed

```bash
python3 skills/trader-pelosi/scripts/ptr.py index --member pelosi
python3 skills/trader-pelosi/scripts/ptr.py index --since 2026-10-01        # every member
python3 skills/trader-pelosi/scripts/ptr.py index --member "Josh Gottheimer" --year 2025
```

`--member` takes a last name, "First Last" or "Last, First", and repeats. `--type ''` lists every filing type (annual reports are type O).

## Step 2: watch for new ones

```bash
python3 skills/trader-pelosi/scripts/ptr.py new --member pelosi --read
```

Shows only PTRs not seen at the last check (the first run shows the last 45 days), downloads and reads them with `--read`, and records them in the state file. Exit code 1 means nothing new. In January it also checks last year's index. The Clerk rebuilds the index file about once a day (the copy read on 2026-10-13 was stamped 09:00 that morning), so checking every hour on weekdays is plenty: run it on a repeat check while the agent is running.

## Step 3: read a report

```bash
python3 skills/trader-pelosi/scripts/ptr.py fetch 20035143
python3 skills/trader-pelosi/scripts/ptr.py parse ptr.txt --filed 2026-08-21 --json
```

`fetch` saves the PDF and, if `pdftotext` is installed, prints the rows. If it is not installed, open the saved PDF and read it directly. `parse` turns `pdftotext -layout` text into rows. Check the row count against the PDF when a report runs over several pages.

## Example: a real alert

```
NEW Periodic Transaction Report: Hon. Nancy Pelosi (CA11) filed 2026-08-21, doc 20035143
  - spouse purchase, Bloom Energy Corporation Class A Common Stock (BE): 100 call
    options, $100 strike, expires 2027-06-17, $1,000,001 - $5,000,000, traded
    2026-07-24, disclosed 28 days later
```

The same PTR also shows 10,000 and 5,000 BE shares, a second block of 100 BE calls on 07/28, Intel shares and 50 Intel calls ($50 strike, 6/17/27), and a private real estate LLC with no ticker. Her other 2026 PTRs: 20033725 (01/23), 20034836 (06/23, Intel and Uber calls) and 20035553 (10/02, a real estate LLC). Then: "This is a public disclosure filed 28 days after the trade. It says what Paul Pelosi bought in July, not what anyone will buy next."

If the user wants to act on it, it goes the long way: a disclosure is weeks old, so it is a reason to look at a chart, not a setup. trader-scanner and trader-entry decide if there is a trade today; then shares only (the config has `allow_options: false`), sized by trader-budget, checked by trader-risk, placed by trader-broker under the autonomous real-money policy, with no yes asked.

## The Senate: the manual route

Senate reports live at the Senate financial disclosures search site. Before searching, the site asks you to tick a box agreeing to the legal limits on how the reports may be used. That is an agreement for the person to accept, so this skill does not click it or automate it. The route:

1. The user opens the Senate search site in their browser and accepts the agreement themselves.
2. They pick the report type "Periodic Transactions", type the senator's last name, set the date range and search.
3. They open the report: electronic filings show a table of transactions, paper filings are scanned images.
4. They paste the table (or a screenshot, or the saved page) into the chat, and you write it up in the same shape as the House rows, with the report link, and the same 45-day caveat.

## What it never does

- Never says or implies a member has inside information, or that copying a politician beats the market. No return claims.
- Never presents a range as an exact dollar figure or a tracker's date as the trade date.
- Never places an order, and never copies options trades.
- Never automates the Senate agreement or any login.

## Safety

- Education and tooling, not financial advice. Nothing in this pack is a recommendation to buy or sell anything.
- Real money, NOT paper. No yes, no approval, no arming toggle. The SOLE trigger is live Angel One funds of ₹100 or more: when funds reach ₹100, trading starts by itself.
- Each day starts at ₹100, target ₹5000, stop at ₹5000, next day reset to ₹100. Intraday only, square off by 15:15 IST. ONLY TRADE on 1-minute chart rules. No share buying or holding, no overnight holding.
- Nothing places an order except trader-broker, and only after trader-risk passes for that exact order. The phone agent itself cannot place an order; execution happens in the user's trade APK.
- No options, margin, shorting or leverage unless the config allows it and the user confirms.
- Congressional trades are PUBLIC disclosures filed up to 45 days after the trade (STOCK Act). This skill reads them fast; it has no inside information, and it says so.

## Limits on this phone

- The script needs python3 in the on-device Linux sandbox and network access to the House Clerk disclosures site. If the sandbox has no network or no python3, the index and PDF reads cannot run on the phone and need a server or relay.
- `pdftotext` (poppler) is optional. Without it, `fetch` still saves the PDF, and the agent reads the saved PDF directly; `parse` needs `pdftotext` output, so without it, read the PDF by hand instead.
- The Senate route is manual by design: the user accepts the agreement and searches in their own browser, then pastes the result into the chat. The agent never automates that agreement or any login.
- Repeat checks only run while the agent is running. There is no background scheduler on the phone yet, so the hourly weekday check is a manual re-run, not an automatic loop.
- No broker is connected on this phone and the phone agent cannot place an order itself. It prepares signals and tickets autonomously under the real-money policy; execution happens in the user's trade APK, or the user places the ticket in their broker app and reports the fill back.
