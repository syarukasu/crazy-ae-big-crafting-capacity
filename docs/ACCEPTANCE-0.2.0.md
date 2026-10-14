# 0.2.0 Evidence

- Forge 47.4.18, AE2 15.4.10, CrazyAE 1.0, ACO refs.8, Java 17.
- Isolated GameTest world: `build/gametest-run`. Production files are untouched.
- Registered and placed `crazy_capacity_bridge:big_integer_combiner` in that world.
- Actual storage filter accepts the controlled fixture and rejects dirt through the new menu.
- Fixture per-unit capacity is `10^1024 - 1`; 64 units are counted without long overflow.
- Actual inventory NBT save/load retains the capacity. Removing a unit updates it to 63 units.
- Server menu decimal string equals that capacity.
- Actual AE2 CPU cluster, with the bridge mixin installed, receives the same full BigInteger capacity.
- Separately, the numeric/display regression covers 1,048,576 units and reconstructs the entire decimal value from display lines.

Boundaries: no actual AQE network order or client GUI rendering was observed in this acceptance run. No production deployment, restart, wide-job completion or global lifecycle guarantee. GameTest source is a separate development source set and is excluded from the distributable JAR.
