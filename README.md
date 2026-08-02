# stockapp-imports

Kotlin Multiplatform (KMP) + Compose Multiplatform module of [StockApp](https://github.com/dgbarreto/stockapp-app) — an investment tracking app (learning project).

Client for bulk-importing orders from a B3 trade statement (XLSX/CSV): picks the file, uploads it to `POST /orders/import` on [`stockapp-backend`](https://github.com/dgbarreto/stockapp-backend), and shows the result (orders created, rows skipped and why). The module is intentionally thin — all parsing, asset-type resolution and the transactional order creation happen server-side; this module owns only the file picker and the upload/result screens.

## Structure

- `imports/` — the only module in this repo, targeting Android (via `com.android.kotlin.multiplatform.library`) + iOS (static framework `Imports`), shared code in `imports/src/commonMain`.
- `sample/` + `sample-android/` — dev-only sample apps used to validate the module in isolation (login via `stockapp-auth` + a placeholder screen until the import screens exist).

## Status

**Phase 6 — Orders and import** (see roadmap in `docs/roadmap.md` in the planning repo): scaffold generated from the `stockapp-orders` template. Backend endpoint (`POST /orders/import`, file upload, ticker-type resolution via `known_tickers` cache + suffix heuristic, transactional batch creation) already implemented and tested. This module's domain/data/presentation are not implemented yet — that's the next guided step.

## Stack

- Kotlin 2.4.0 · Compose Multiplatform 1.11.1 · AGP 9.0.1

## Running

```
./gradlew :imports:build
./gradlew :imports:testAndroidHostTest
./gradlew :imports:iosSimulatorArm64Test
```

---

_Progress kept up to date manually as the project moves forward._
