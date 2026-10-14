package com.syaru.crazycapacity.mixin;

import appeng.me.cluster.implementations.CraftingCPUCluster;
import org.spongepowered.asm.mixin.Mixin;

// Runs after InsaneAE has installed the existing exact capacity ledger.
@Mixin(value = CraftingCPUCluster.class, remap = false, priority = 500)
public abstract class CpuCapacityHookMixin {}
