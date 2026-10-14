package com.syaru.crazycapacity.block;

import com.syaru.crazycapacity.BridgeContent;
import com.syaru.crazycapacity.CapacityMath;
import com.syaru.crazycapacity.CrazyCapacityBridge;
import com.syaru.crazycapacity.ExactStorageProvider;
import com.syaru.crazycapacity.StorageCapacity;
import dev.beecube31.crazyae2.common.tile.networking.TileCraftingUnitsCombiner;
import appeng.api.inventories.InternalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import java.math.BigInteger;

public final class ExactCombinerBlockEntity extends TileCraftingUnitsCombiner implements ExactStorageProvider {
    private BigInteger exactCapacity = BigInteger.ZERO;
    private boolean dirtyCapacity = true;
    private long configRevision = -1;

    public ExactCombinerBlockEntity(BlockPos pos, BlockState state) {
        super(BridgeContent.COMBINER_ENTITY.get(), pos, state);
    }

    public boolean canChangeUnits() { return getCluster() == null || !getCluster().isBusy(); }

    @Override
    protected net.minecraft.world.item.Item getItemFromBlockEntity() { return BridgeContent.COMBINER_ITEM.get(); }

    @Override
    public void onChangeInventory(InternalInventory inventory, int slot) {
        super.onChangeInventory(inventory, slot);
        dirtyCapacity = true;
    }

    @Override
    public void loadTag(CompoundTag tag) {
        super.loadTag(tag);
        dirtyCapacity = true;
    }

    @Override
    public BigInteger getBigIntegerStorageBytes() {
        if (dirtyCapacity || configRevision != CrazyCapacityBridge.configRevision()) {
            BigInteger total = BigInteger.ZERO;
            for (var stack : getStorageInv())
                total = CapacityMath.addStack(total, StorageCapacity.stackCapacity(stack), stack.getCount());
            exactCapacity = total;
            configRevision = CrazyCapacityBridge.configRevision();
            dirtyCapacity = false;
        }
        return exactCapacity;
    }
}
