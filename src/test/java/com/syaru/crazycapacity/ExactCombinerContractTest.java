package com.syaru.crazycapacity;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;

class ExactCombinerContractTest {
    @Test void millionStackQuantumCoreRetainsEveryByteAcrossDisplaySync() {
        BigInteger perCore = BigInteger.TEN.pow(1024).subtract(BigInteger.ONE);
        BigInteger expected = perCore.multiply(BigInteger.valueOf(1048576));
        BigInteger capacity = CapacityMath.addStack(BigInteger.ZERO, perCore, 1048576);
        assertEquals(expected, capacity);
        assertEquals(expected, new BigInteger(String.join("", CapacityDisplay.exactLines(capacity.toString()))));
        assertFalse(CapacityDisplay.compact(capacity.toString()).contains("8 EB"));
        assertTrue(capacity.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0);
        assertEquals(expected.subtract(perCore), capacity.subtract(perCore));
    }

    @Test void menuSyncsTheCapacityAsStringAndClientCodeIsSideGated() throws Exception {
        ClassNode menu = read("menu/ExactCombinerMenu");
        var field = menu.fields.stream().filter(f -> f.name.equals("exactCapacity")).findFirst().orElseThrow();
        assertEquals("Ljava/lang/String;", field.desc);
        assertTrue(field.visibleAnnotations.stream().anyMatch(a -> a.desc.equals("Lappeng/menu/guisync/GuiSync;")));
        ClassNode client = read("client/BridgeClient");
        assertTrue(client.visibleAnnotations.stream().anyMatch(a -> a.desc.contains("EventBusSubscriber")));
        try (var model = getClass().getResourceAsStream("/assets/crazy_capacity_bridge/models/block/big_integer_combiner.json");
             var loot = getClass().getResourceAsStream("/data/crazy_capacity_bridge/loot_tables/blocks/big_integer_combiner.json")) {
            assertNotNull(model);
            assertNotNull(loot);
        }
    }

    private ClassNode read(String name) throws Exception {
        ClassNode node = new ClassNode();
        try (var in = getClass().getResourceAsStream("/com/syaru/crazycapacity/" + name + ".class")) {
            assertNotNull(in);
            new ClassReader(in).accept(node, 0);
        }
        return node;
    }
}
