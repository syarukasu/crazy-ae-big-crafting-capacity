package com.syaru.crazycapacity.test;

import appeng.block.crafting.CraftingUnitBlock;
import appeng.block.crafting.ICraftingUnitType;
import com.syaru.crazycapacity.BridgeContent;
import com.syaru.crazycapacity.ExactStorageProvider;
import com.syaru.crazycapacity.block.ExactCombinerBlockEntity;
import java.math.BigInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("ccb_test")
@GameTestHolder("ccb_test")
@PrefixGameTestTemplate(false)
public class CombinerGameTests {
    private static final BigInteger UNIT = BigInteger.TEN.pow(1024).subtract(BigInteger.ONE);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "ccb_test");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "ccb_test");
    private static final RegistryObject<Block> UNIT_BLOCK = BLOCKS.register("wide_storage", () -> new CraftingUnitBlock(new WideType()));
    private static final RegistryObject<Item> UNIT_ITEM = ITEMS.register("wide_storage", () -> new BlockItem(UNIT_BLOCK.get(), new Item.Properties()));

    public CombinerGameTests() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    private static class WideType implements ICraftingUnitType, ExactStorageProvider {
        public BigInteger getBigIntegerStorageBytes() { return UNIT; }
        public long getStorageBytes() { return Long.MAX_VALUE; }
        public int getAcceleratorThreads() { return 0; }
        public Item getItemFromType() { return UNIT_ITEM.get(); }
    }

    @GameTest(templateNamespace = "forge", template = "empty", timeoutTicks = 100)
    public static void stackedCapacityAndInventoryReload(GameTestHelper helper) {
        var combinerItem = new ItemStack(BridgeContent.COMBINER_ITEM.get());
        helper.assertTrue(combinerItem.hasFoil(), "Combiner item must have an enchantment glint");
        helper.assertTrue(!combinerItem.isEnchanted(), "Glint must not add an actual enchantment");
        net.minecraft.world.item.CreativeModeTabs.tryRebuildTabContents(helper.getLevel().enabledFeatures(), true, helper.getLevel().registryAccess());
        var ownTab = helper.getLevel().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB)
                .get(new net.minecraft.resources.ResourceLocation(BridgeContent.ID, "main"));
        helper.assertTrue(ownTab != null && ownTab.getDisplayItems().stream().anyMatch(s -> s.is(BridgeContent.COMBINER_ITEM.get())),
                "Dedicated creative tab must contain the upper Combiner");
        helper.assertTrue(ownTab.getIconItem().hasFoil(), "Dedicated tab icon must have glint");
        var state = BridgeContent.COMBINER.get().defaultBlockState();
        helper.setBlock(new BlockPos(1, 1, 1), BridgeContent.COMBINER.get());
        var original = (ExactCombinerBlockEntity) helper.getBlockEntity(new BlockPos(1, 1, 1));
        var stack = new ItemStack(UNIT_ITEM.get(), 64);
        helper.assertTrue(original.getStorageInv().isItemValid(0, stack), "Exact storage must pass the actual slot filter");
        original.getStorageInv().setItemDirect(0, stack);
        helper.assertTrue(original.getBigIntegerStorageBytes().equals(UNIT.multiply(BigInteger.valueOf(64))), "Full capacity must count every unit");
        var tag = new CompoundTag();
        original.saveAdditional(tag);
        helper.setBlock(new BlockPos(1, 1, 2), BridgeContent.COMBINER.get());
        var restored = (ExactCombinerBlockEntity) helper.getBlockEntity(new BlockPos(1, 1, 2));
        restored.loadTag(tag);
        helper.assertTrue(restored.getBigIntegerStorageBytes().equals(UNIT.multiply(BigInteger.valueOf(64))), "NBT reload must retain exact capacity");
        restored.getStorageInv().setItemDirect(0, new ItemStack(UNIT_ITEM.get(), 63));
        helper.assertTrue(restored.getBigIntegerStorageBytes().equals(UNIT.multiply(BigInteger.valueOf(63))), "Removal must update saturated capacity");
        var player = net.minecraftforge.common.util.FakePlayerFactory.getMinecraft(helper.getLevel());
        var menu = new com.syaru.crazycapacity.menu.ExactCombinerMenu(BridgeContent.COMBINER_MENU.get(), 1, player.getInventory(), restored);
        menu.broadcastChanges();
        helper.assertTrue(new BigInteger(menu.exactCapacity).equals(UNIT.multiply(BigInteger.valueOf(63))), "Menu sync must use exact decimal");
        helper.assertTrue(!menu.slots.get(12).mayPlace(new ItemStack(net.minecraft.world.item.Items.DIRT)), "Storage slots must reject non-units");
        var cluster = new appeng.me.cluster.implementations.CraftingCPUCluster(BlockPos.ZERO, BlockPos.ZERO);
        try {
            var add = cluster.getClass().getDeclaredMethod("addBlockEntity", appeng.blockentity.crafting.CraftingBlockEntity.class);
            add.setAccessible(true);
            add.invoke(cluster, restored);
        } catch (ReflectiveOperationException failure) { throw new AssertionError(failure); }
        helper.assertTrue((Object) cluster instanceof com.syaru.crazycapacity.ExactCpuCapacity, "Exact CPU hook must be installed");
        helper.assertTrue(((com.syaru.crazycapacity.ExactCpuCapacity) (Object) cluster).ccb$getExactStorageCapacity()
                .equals(UNIT.multiply(BigInteger.valueOf(63))), "CPU must receive full BigInteger capacity");
        helper.succeed();
    }
}
