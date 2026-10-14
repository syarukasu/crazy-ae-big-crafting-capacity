# CrazyAE Big Crafting Capacity

A small Forge 1.20.1 compatibility addon for the **CrazyAE ME Crafting Units Combiner** and **ACO's public BigInteger API**.

## English

- Adds up the capacity of inserted crafting-storage items as `BigInteger`, including their stack counts.
- Preserves exact capacity above `Long.MAX_VALUE`. Only the original AE2 `long` accessor exposes a saturated compatibility value; exact capacity checks use the full value.
- Rebuilds the CPU when its exact capacity changes, even if both old and new long values are saturated.
- Rejects an ACO exact plan that exceeds the Combiner CPU's actual capacity. Busy checks, job execution, transfers, cancellation and persistence remain with AE2/ACO and the installed CPU addon.
- Supports public exact-capacity getters from optional storage addons. Includes a targeted compatibility patch for InsaneAE 1.1.1 when present; InsaneAE and AQE are **not required**.
- Does not add a throughput limit, duplicate ACO's API classes, enlarge the individual storage items, or introduce a new planner.

### Install

Required addon projects: [CrazyAE](https://www.curseforge.com/minecraft/mc-mods/crazyae) and [AE2 Crafting Optimizer](https://www.curseforge.com/minecraft/mc-mods/ae2-crafting-optimizer). Their own dependencies, including Applied Energistics 2, must also be installed. Use the ACO 2.0 refs.8 snapshot or a compatible build, CrazyAE 1.0, AE2 15.4.10 and Forge 47.4.18+. Install this same JAR on the server and clients. Java 17 is required.

**0.1.1 is an Alpha.** Exact arithmetic and the packaged resources have automated checks. Huge-job completion, cancellation/refunds, restart recovery and simultaneous orders have not completed in-game acceptance. A large capacity does not guarantee that every wide plan is executable by the installed ACO version. Back up worlds before testing.

### Build

```powershell
./scripts/fetch-dependencies.ps1
./gradlew.bat clean test build
```

Dependencies are downloaded from their original public distribution endpoints and checked against SHA-256. They are not redistributed in this repository or bundled into this addon. You may instead provide an installed dependency directory using `-PmodsDir=C:/path/to/mods`.

The optional InsaneAE bytecode fixture test runs only when `insaneae-1.20.1-1.1.1.jar` is in that directory. Its absence skips that one fixture test, not the capacity or packaging checks.

## 日本語

CrazyAEの**ME Crafting Units Combiner**へ入れたクラフトストレージを、ACOのBigInteger計画で正確に扱うための互換MODです。

- ストレージ1個の容量とスタック数をBigIntegerで合算します。
- long上限を超えても正確な容量を保持します。AE2本来のlong取得口だけは互換表示値で、実際の容量判定では正確値を使用します。
- long取得値が変わらなくても、正確な容量が変わればCPUを再構築します。
- 実容量を超えるACOの計画を拒否します。クラフト実行・搬入出・取消・保存は既存のAE2/ACO・CPU側の処理を使います。
- InsaneAEやAQEは任意連携です。必須アドオンは**ACOとCrazyAEの2つ**です。その2つが要求するAE2などの前提も導入してください。
- 独自の搬入速度制限、別の計算器、ACO APIの複製は追加しません。

Forge 1.20.1・Java 17向けです。サーバーとクライアントへ同じJARを導入してください。ACOは2.0 refs.8スナップショット以降の互換版を使用します。

**0.1.1はAlphaです。** 容量の数値検証と配布物の確認は実施していますが、巨大注文の完了・取消返却・再起動復旧・同時注文の実機受入は未完了です。容量が大きくても、ACO側がすべての巨大計画を実行できる保証ではありません。

## Credits / License

Bridge code: syarukasu, MIT. CrazyAE is by Beecube31 and its contributors, not this project's author. [Original source](https://github.com/beecupbe/crazyae/tree/1.20.1), [source fork](https://github.com/syarukasu/crazyae/tree/1.20.1). AE2, ACO, InsaneAE and other addons retain their respective authors and licenses. This addon does not include their binaries.
