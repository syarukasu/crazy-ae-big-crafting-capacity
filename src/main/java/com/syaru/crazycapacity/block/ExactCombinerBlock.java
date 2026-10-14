package com.syaru.crazycapacity.block;

import com.syaru.crazycapacity.BridgeContent;
import appeng.menu.locator.MenuLocators;
import dev.beecube31.crazyae2.common.block.crafting.BlockCraftingUnitsCombiner;
import dev.beecube31.crazyae2.common.util.ContainerOpener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class ExactCombinerBlock extends BlockCraftingUnitsCombiner {
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown() && level.getBlockEntity(pos) instanceof ExactCombinerBlockEntity entity) {
            if (!level.isClientSide()) ContainerOpener.open(BridgeContent.COMBINER_MENU.get(), player, MenuLocators.forBlockEntity(entity));
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}
