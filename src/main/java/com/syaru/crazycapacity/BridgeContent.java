package com.syaru.crazycapacity;

import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import com.syaru.crazycapacity.block.ExactCombinerBlock;
import com.syaru.crazycapacity.block.ExactCombinerBlockEntity;
import com.syaru.crazycapacity.menu.ExactCombinerMenu;
import com.syaru.crazycapacity.item.GlintCombinerItem;
import dev.beecube31.crazyae2.core.builder.ContainerTypeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BridgeContent {
    public static final String ID = "crazy_capacity_bridge";
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    private static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID);
    public static final RegistryObject<ExactCombinerBlock> COMBINER = BLOCKS.register("big_integer_combiner", ExactCombinerBlock::new);
    public static final RegistryObject<Item> COMBINER_ITEM = ITEMS.register("big_integer_combiner", () -> new GlintCombinerItem(COMBINER.get(), new Item.Properties()));
    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.crazy_capacity_bridge"))
            .icon(() -> new ItemStack(COMBINER_ITEM.get()))
            .displayItems((parameters, output) -> output.accept(COMBINER_ITEM.get()))
            .build());
    public static final RegistryObject<BlockEntityType<CraftingBlockEntity>> COMBINER_ENTITY = ENTITIES.register("big_integer_combiner", () -> {
        BlockEntityType<CraftingBlockEntity> type = BlockEntityType.Builder.<CraftingBlockEntity>of(
                (pos, state) -> new ExactCombinerBlockEntity(pos, state), COMBINER.get()).build(null);
        COMBINER.get().setBlockEntity(CraftingBlockEntity.class, type, null, null);
        AEBaseBlockEntity.registerBlockEntityItem(type, COMBINER_ITEM.get());
        return type;
    });
    public static final RegistryObject<MenuType<ExactCombinerMenu>> COMBINER_MENU = MENUS.register("big_integer_combiner",
            () -> ContainerTypeBuilder.create(ExactCombinerMenu::new, ExactCombinerBlockEntity.class).build("big_integer_combiner"));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        MENUS.register(bus);
        TABS.register(bus);
    }
}
