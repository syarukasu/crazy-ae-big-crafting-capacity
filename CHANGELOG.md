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

Alpha: full in-game wide-job lifecycle acceptance is pending.
