package com.syaru.crazycapacity.mixin;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.crafting.ICraftingSubmitResult;
import appeng.api.networking.security.IActionSource;
import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.crafting.execution.CraftingSubmitResult;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import com.syaru.ae2craftingoptimizer.api.big.BigCraftingEngineApi;
import com.syaru.crazycapacity.CapacityMath;
import com.syaru.crazycapacity.ExactCpuCapacity;
import com.syaru.crazycapacity.ExactStorageProvider;
import com.syaru.crazycapacity.StorageCapacity;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.math.BigInteger;
import java.util.List;

@Mixin(value = CraftingCPUCluster.class, remap = false, priority = 1600)
public abstract class CpuCapacityMixin implements ExactCpuCapacity {
    @Shadow @Final private List<CraftingBlockEntity> blockEntities;
    @Shadow private long storage;
    @Unique private boolean ccb$containsCombiner;
    @Unique private BigInteger ccb$exactCapacity = BigInteger.ZERO;
    @Unique private String ccb$capacityLabel = "";

    @Inject(method = "addBlockEntity", at = @At("RETURN"))
    private void ccb$recordExactCapacity(CraftingBlockEntity entity, CallbackInfo ci) {
        ccb$containsCombiner |= entity instanceof ExactStorageProvider;
        if (!ccb$containsCombiner) return;
        BigInteger total = BigInteger.ZERO;
        for (CraftingBlockEntity block : blockEntities) total = total.add(StorageCapacity.exactContribution(block));
        ccb$exactCapacity = total;
        storage = CapacityMath.longFacade(total);
        String value = total.toString();
        ccb$capacityLabel = value.length() <= 16 ? value + " B"
                : value.charAt(0) + "." + value.substring(1, 4) + " x 10^" + (value.length() - 1) + " B";
    }

    @Inject(method = "getAvailableStorage", at = @At("HEAD"), cancellable = true)
    private void ccb$exposeLongFacade(CallbackInfoReturnable<Long> cir) {
        if (ccb$containsCombiner) cir.setReturnValue(CapacityMath.longFacade(ccb$exactCapacity));
    }

    @Inject(method = "submitJob", at = @At("HEAD"), cancellable = true)
    private void ccb$validateExactPlan(IGrid grid, ICraftingPlan plan, IActionSource source,
            ICraftingRequester requester, CallbackInfoReturnable<ICraftingSubmitResult> cir) {
        if (!ccb$containsCombiner) return;
        var exact = BigCraftingEngineApi.inspectBigIntegerPlan(plan);
        if (exact.isEmpty()) return;
        if (exact.get().simulation()) {
            cir.setReturnValue(CraftingSubmitResult.INCOMPLETE_PLAN);
        } else if (exact.get().exactBytes().compareTo(ccb$exactCapacity) > 0) {
            cir.setReturnValue(CraftingSubmitResult.CPU_TOO_SMALL);
        }
    }

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void ccb$showExactCapacity(CallbackInfoReturnable<Component> cir) {
        if (!ccb$containsCombiner || ccb$exactCapacity.bitLength() <= 63
                || ModList.get().isLoaded("insaneae")) return;
        Component name = cir.getReturnValue();
        var label = Component.literal(" [" + ccb$capacityLabel + "]").withStyle(ChatFormatting.GRAY);
        cir.setReturnValue(name == null ? label : name.copy().append(label));
    }

    public BigInteger ccb$getExactStorageCapacity() {
        return ccb$containsCombiner ? ccb$exactCapacity : BigInteger.valueOf(Math.max(0L, storage));
    }
}
