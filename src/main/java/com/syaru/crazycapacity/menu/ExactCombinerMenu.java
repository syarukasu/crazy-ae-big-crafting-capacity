package com.syaru.crazycapacity.menu;

import appeng.menu.guisync.GuiSync;
import appeng.menu.SlotSemantics;
import com.syaru.crazycapacity.block.ExactCombinerBlockEntity;
import dev.beecube31.crazyae2.common.containers.CrazyAEBaseContainer;
import dev.beecube31.crazyae2.common.enums.CrazySemanticsContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ExactCombinerMenu extends CrazyAEBaseContainer {
    @GuiSync(0) public String exactCapacity = "0";
    @GuiSync(1) public int threads;
    private final ExactCombinerBlockEntity host;

    public ExactCombinerMenu(MenuType<ExactCombinerMenu> type, int id, Inventory inventory,
            ExactCombinerBlockEntity host) {
        super(type, id, inventory, host);
        this.host = host;
        addUnits(host.getAcceleratorsInv().toContainer(), CrazySemanticsContainer.COMBINER_ACCELERATORS, 12);
        addUnits(host.getStorageInv().toContainer(), CrazySemanticsContainer.COMBINER_STORAGE, 68);
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, 115 + row * 18), SlotSemantics.PLAYER_INVENTORY);
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col, 8 + col * 18, 173), SlotSemantics.PLAYER_HOTBAR);
    }

    private void addUnits(net.minecraft.world.Container inventory, appeng.menu.SlotSemantic semantic, int top) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            addSlot(new Slot(inventory, i, 62 + (i % 6) * 18, top + (i / 6) * 19) {
                public boolean mayPlace(ItemStack stack) { return host.canChangeUnits() && inventory.canPlaceItem(getContainerSlot(), stack); }
                public boolean mayPickup(Player player) { return host.canChangeUnits() && super.mayPickup(player); }
            }, semantic);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 24 && !host.canChangeUnits()) return ItemStack.EMPTY;
        return super.quickMoveStack(player, index);
    }

    @Override
    public void broadcastChanges() {
        if (isServerSide()) {
            exactCapacity = host.getBigIntegerStorageBytes().toString();
            threads = host.getAcceleratorThreads();
        }
        super.broadcastChanges();
    }
}
