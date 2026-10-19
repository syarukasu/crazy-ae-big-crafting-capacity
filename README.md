# CrazyAE Big Crafting Capacity

## 日本語

CrazyAEのクラフトストレージを、ACOのBigInteger APIで正確に扱うためのForge 1.20.1アドオンです。0.2.1では上位版の **BigInteger クラフトユニット統合器** と専用クリエイティブタブを提供します。

### 主な機能

- 挿入したクラフトストレージの容量と収納個数をBigIntegerで合算します。
- long上限を超える容量も保持し、ACOの計画をCPUの実容量で判定します。
- 新しい統合器の画面では、容量を指数表記と省略なしの数値で表示します。
- 専用の **BigInteger クラフト** タブから上位統合器を取り出せます。
- 独自の搬入出速度制限や別のクラフト計算器は追加しません。クラフト実行は既存のAE2・ACO・CPU側の処理を使用します。

### 上位統合器の作成

中央にCrazyAEの元のCombiner、上下左右にAE2シンギュラリティ、四隅にクォーツガラスを配置します。

ブロックID: `crazy_capacity_bridge:big_integer_combiner`

既存のCombinerは自動で置き換わりません。旧ブロックからクラフトストレージを取り出し、新しい統合器に入れてください。容量を無制限に増やす機能ではなく、挿入したストレージの実容量を使用します。

### 導入条件

**必須アドオンは[ACO](https://www.curseforge.com/minecraft/mc-mods/ae2-crafting-optimizer)と[CrazyAE](https://www.curseforge.com/minecraft/mc-mods/crazyae)の2つです。** それぞれが要求するAE2などの前提MODも導入してください。InsaneAE・AQEは任意です。

Forge 1.20.1 / Java 17。対象構成はCrazyAE 1.0、ACO 2.0 refs.8または互換版、AE2 15.4.10、Forge 47.4.18以降です。サーバーとクライアントに同じバージョンのJARを導入してください。

**Alpha版です。** 上位統合器・専用タブは0.2.1の機能です。既存Combinerの容量互換機能を提供する0.1.1には含まれません。

## English

A Forge 1.20.1 addon connecting CrazyAE crafting storage to ACO's BigInteger API. Version 0.2.1 includes an upgraded **BigInteger Crafting Units Combiner** and a dedicated creative tab.

### Features

- Sums inserted crafting-storage capacities and item counts using BigInteger.
- Preserves capacity above Long.MAX_VALUE and checks ACO plans against the CPU's actual capacity.
- Displays the new Combiner's capacity in scientific notation and as a complete decimal value.
- Provides a dedicated **BigInteger Crafting** creative tab.
- Adds no transfer-speed limit or separate planner. Crafting execution stays with AE2, ACO and the installed CPU addon.

### Upgrade recipe

Place the original CrazyAE Combiner in the centre, AE2 singularities on its four sides and quartz glass in the corners.

Block ID: `crazy_capacity_bridge:big_integer_combiner`

Existing Combiners are not replaced automatically. Move their crafting-storage items into the new block. This addon uses the capacity of the inserted storage; it does not grant unlimited capacity.

### Requirements

**Required addon mods: [ACO](https://www.curseforge.com/minecraft/mc-mods/ae2-crafting-optimizer) and [CrazyAE](https://www.curseforge.com/minecraft/mc-mods/crazyae).** Install their dependencies, including AE2, as well. InsaneAE and AQE are optional.

Forge 1.20.1 / Java 17. Target versions: CrazyAE 1.0, ACO 2.0 refs.8 or a compatible build, AE2 15.4.10 and Forge 47.4.18+. Install the same addon version on the server and clients.

**Alpha.** The upgraded block and dedicated tab are available in 0.2.1. Version 0.1.1 provides capacity compatibility for the original Combiner and does not include these additions.

## Source / Credits

[Source code and issues](https://github.com/syarukasu/crazy-ae-big-crafting-capacity)

Addon code: syarukasu, MIT. CrazyAE is by Beecube31 and its contributors. This is a separate addon, not a rebranding of CrazyAE. The icon renders the original CrazyAE Combiner model with CrazyAE/AE2 textures; those assets retain their original authors and licenses. Dependency binaries are not bundled.

[Original CrazyAE source](https://github.com/beecupbe/crazyae/tree/1.20.1) | [Source fork](https://github.com/syarukasu/crazyae/tree/1.20.1)

## Build

```powershell
./scripts/fetch-dependencies.ps1
./gradlew.bat clean build
```

Dependencies are downloaded from their original public distribution endpoints and verified against SHA-256. They are not redistributed in this repository or bundled into the addon. An installed dependency directory can be supplied using `-PmodsDir=C:/path/to/mods`.
