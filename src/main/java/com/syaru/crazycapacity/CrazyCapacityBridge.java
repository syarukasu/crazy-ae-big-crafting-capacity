package com.syaru.crazycapacity;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import java.util.concurrent.atomic.AtomicLong;

@Mod("crazy_capacity_bridge")
public final class CrazyCapacityBridge {
    private static final AtomicLong CONFIG_REVISION = new AtomicLong();

    public CrazyCapacityBridge() {
        for (String id : java.util.List.of("advanced_quantum_engineering", "ae2_crafting_optimizer", "insaneae")) {
            ModList.get().getModContainerById(id).ifPresent(container -> {
                if (container instanceof FMLModContainer javaMod) {
                    javaMod.getEventBus().addListener(this::configReloaded);
                }
            });
        }
    }

    private void configReloaded(ModConfigEvent.Reloading event) {
        CONFIG_REVISION.incrementAndGet();
    }

    public static long configRevision() {
        return CONFIG_REVISION.get();
    }
}
