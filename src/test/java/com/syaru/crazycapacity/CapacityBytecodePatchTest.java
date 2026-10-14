package com.syaru.crazycapacity;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import java.nio.file.Path;
import java.util.jar.JarFile;
import static org.junit.jupiter.api.Assertions.*;

class CapacityBytecodePatchTest {
    @Test void doesNotRequireInsaneAeToBeInstalled() {
        assertDoesNotThrow(() -> CapacityBytecodePatch.apply(new ClassNode()));
    }
    @Test void patchesTheActualInstalledInsaneAeAndPreservesTheQuantumOwnerRoute() throws Exception {
        Path jar = Path.of(System.getProperty("bridge.modsDir"), "insaneae-1.20.1-1.1.1.jar");
        org.junit.jupiter.api.Assumptions.assumeTrue(java.nio.file.Files.isRegularFile(jar),
                "Optional InsaneAE fixture is not installed");
        ClassNode node = new ClassNode();
        try (JarFile source = new JarFile(jar.toFile())) {
            var entry = source.getJarEntry("jp/main/taikun/insaneae/mixin/CraftingCPUClusterMixin.class");
            new ClassReader(source.getInputStream(entry)).accept(node, 0);
        }
        var method = node.methods.stream().filter(m -> m.name.equals("insaneae$recountStorage")).findFirst().orElseThrow();
        long ownerCalls = java.util.Arrays.stream(method.instructions.toArray())
                .filter(i -> i instanceof MethodInsnNode c && c.name.equals("cpuExactStorage")).count();
        CapacityBytecodePatch.apply(node);
        assertEquals(1, java.util.Arrays.stream(method.instructions.toArray())
                .filter(i -> i instanceof MethodInsnNode c && c.name.equals("exactContribution")).count());
        assertEquals(ownerCalls, java.util.Arrays.stream(method.instructions.toArray())
                .filter(i -> i instanceof MethodInsnNode c && c.name.equals("cpuExactStorage")).count());
        assertEquals(1, ownerCalls);
        new Analyzer<>(new BasicVerifier()).analyze(node.name, method);
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        assertDoesNotThrow(() -> new ClassReader(writer.toByteArray()));
        assertDoesNotThrow(() -> CapacityBytecodePatch.apply(new ClassNode()));
    }
}
