package com.syaru.crazycapacity.mixin;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.block.crafting.ICraftingUnitType;
import appeng.util.inv.AppEngInternalInventory;
import com.syaru.crazycapacity.ExactStorageProvider;
import com.syaru.crazycapacity.CapacityMath;
import com.syaru.crazycapacity.CrazyCapacityBridge;
import com.syaru.crazycapacity.StorageCapacity;
import dev.beecube31.crazyae2.common.tile.networking.TileCraftingUnitsCombiner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.math.BigInteger;

@Mixin(value = TileCraftingUnitsCombiner.class, remap = false)
public abstract class CombinerMixin implements ExactStorageProvider {
    @Shadow private long storageBytes;
    @Shadow private boolean recalculate;
    @Shadow public abstract AppEngInternalInventory getStorageInv();
    @Shadow private void recalculate() { throw new AssertionError("Mixin shadow"); }
    @Shadow public abstract void rebuildCluster(boolean force);
    @Unique private BigInteger ccb$capacity = BigInteger.ZERO;
    @Unique private boolean ccb$extraRebuild;
    @Unique private long ccb$configRevision = -1;

    @Inject(method = "unitTypeOf", at = @At("RETURN"), cancellable = true)
    private static void ccb$acceptAdvancedStorage(ItemStack stack, CallbackInfoReturnable<ICraftingUnitType> cir) {
        if (cir.getReturnValue() == null) {
            var type = StorageCapacity.advancedType(stack);
            if (type != null) cir.setReturnValue(type);
        }
    }

    @Inject(method = "recalculate", at = @At("RETURN"))
    private void ccb$sumExactCapacity(CallbackInfo ci) {
        BigInteger previous = ccb$capacity;
        BigInteger total = BigInteger.ZERO;
        for (ItemStack stack : getStorageInv()) {
            total = CapacityMath.addStack(total, StorageCapacity.stackCapacity(stack), stack.getCount());
        }
        ccb$capacity = total;
        storageBytes = CapacityMath.longFacade(total);
        ccb$extraRebuild |= !previous.equals(total);
        ccb$configRevision = CrazyCapacityBridge.configRevision();
    }

    @Inject(method = "getStorageBytes", at = @At("HEAD"))
    private void ccb$refreshFacade(CallbackInfoReturnable<Long> cir) {
        ccb$refreshIfChanged();
    }

    @Inject(method = "tickingRequest", at = @At("HEAD"))
    private void ccb$refreshConfiguration(IGridNode node, int elapsed, CallbackInfoReturnable<TickRateModulation> cir) {
        if (ccb$configRevision != CrazyCapacityBridge.configRevision()) recalculate = true;
    }

    @Inject(method = "tickingRequest", at = @At("RETURN"))
    private void ccb$rebuildOnExactOnlyChange(IGridNode node, int elapsed,
            CallbackInfoReturnable<TickRateModulation> cir) {
        if (ccb$extraRebuild) {
            ccb$extraRebuild = false;
            rebuildCluster(true);
        }
    }

    @Inject(method = "rebuildCluster", at = @At("RETURN"))
    private void ccb$clearRebuildAfterNativeRebuild(boolean force, CallbackInfo ci) {
        if (force) ccb$extraRebuild = false;
    }

    @Unique
    private void ccb$refreshIfChanged() {
        if (recalculate || ccb$configRevision != CrazyCapacityBridge.configRevision()) recalculate();
    }

    @Override
    public BigInteger getBigIntegerStorageBytes() {
        ccb$refreshIfChanged();
        return ccb$capacity;
    }
}
