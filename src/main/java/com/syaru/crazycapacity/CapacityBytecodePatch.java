package com.syaru.crazycapacity;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class CapacityBytecodePatch {
    private CapacityBytecodePatch() {}

    public static void apply(ClassNode target) {
        MethodNode recount = target.methods.stream()
                .filter(m -> m.name.equals("insaneae$recountStorage")
                        && m.desc.equals("()Ljava/math/BigInteger;"))
                .findFirst().orElse(null);
        if (recount == null) return;
        int blockLocal = -1;
        int changed = 0;
        for (AbstractInsnNode instruction : recount.instructions.toArray()) {
            if (instruction instanceof MethodInsnNode call
                    && call.owner.equals("appeng/blockentity/crafting/CraftingBlockEntity")
                    && call.name.equals("getStorageBytes") && call.desc.equals("()J")) {
                AbstractInsnNode load = previousCode(call);
                if (!(load instanceof VarInsnNode variable) || variable.getOpcode() != Opcodes.ALOAD) {
                    throw new IllegalStateException("Unrecognized storage provider load");
                }
                blockLocal = variable.var;
            }
            if (instruction instanceof MethodInsnNode call && call.owner.equals("java/math/BigInteger")
                    && call.name.equals("valueOf") && call.desc.equals("(J)Ljava/math/BigInteger;")) {
                AbstractInsnNode load = previousCode(call);
                if (blockLocal < 0 || !(load instanceof VarInsnNode variable)
                        || variable.getOpcode() != Opcodes.LLOAD) {
                    throw new IllegalStateException("Unrecognized exact storage conversion");
                }
                recount.instructions.set(load, new VarInsnNode(Opcodes.ALOAD, blockLocal));
                recount.instructions.set(call, new MethodInsnNode(Opcodes.INVOKESTATIC,
                        "com/syaru/crazycapacity/StorageCapacity", "exactContribution",
                        "(Lappeng/blockentity/crafting/CraftingBlockEntity;)Ljava/math/BigInteger;", false));
                changed++;
            }
        }
        if (changed != 1) throw new IllegalStateException("Expected one exact storage contribution, found " + changed);
    }

    private static AbstractInsnNode previousCode(AbstractInsnNode instruction) {
        AbstractInsnNode previous = instruction.getPrevious();
        while (previous != null && previous.getOpcode() < 0) previous = previous.getPrevious();
        return previous;
    }
}
