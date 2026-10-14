# 0.2.1 (Alpha)

- Register a dedicated BigInteger Crafting creative tab with the upper Combiner as its icon and content.
- 専用クリエイティブタブ「BigInteger クラフト」を新設。

# 0.2.0 (Alpha candidate)

- Add a separate BigInteger Crafting Units Combiner block, item, recipe, loot and localized screen.
- Sync exact capacity as a decimal string, with a compact label and complete decimal tooltip.
- Prefer block-entity exact capacity over a saturated unit-type facade.
- Keep existing blocks and inventory saves intact; reject unit extraction while the CPU is busy.
- 容量をBigIntegerで扱う上位統合器を新規追加。旧画面の8 EB表示を使わず、正確値を同期・表示します。
- 既存の配置済みブロックやワールドは置換しません。

# 0.1.1 (Alpha)

- Add `pack.mcmeta` to fix Forge's missing resource-pack metadata warning.
- Require only ACO and CrazyAE as addon dependencies; InsaneAE and AQE are optional.
- Keep Combiner and CPU capacity as BigInteger, including stacked storage items.
- Refresh the CPU on exact-capacity changes and reject exact plans above its capacity.
- Publish checksum-pinned build dependencies, CI and English/Japanese documentation.
- No new crafting-transfer rate limit or planner.

## 日本語

- `pack.mcmeta`不足によるForge起動警告を修正。
- 必須アドオンをACOとCrazyAEに整理。InsaneAE・AQEは任意。
- Combinerに入れたストレージの個数を含め、CPU容量をBigIntegerで合算。
- 正確な容量の変化をCPUへ反映し、実容量超過の計画を拒否。
- 公開依存でのビルド手順、CI、日英説明を追加。
- クラフト搬入出の独自速度制限や計算器は追加しません。

