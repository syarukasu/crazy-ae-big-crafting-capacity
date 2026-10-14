package com.syaru.crazycapacity.client;

import com.syaru.crazycapacity.CapacityDisplay;
import com.syaru.crazycapacity.menu.ExactCombinerMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import java.util.ArrayList;
import java.util.List;

public final class ExactCombinerScreen extends AbstractContainerScreen<ExactCombinerMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("crazyae", "textures/guis/crafting_units_combiner.png");
    public ExactCombinerScreen(ExactCombinerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 197;
        titleLabelY = inventoryLabelY = -1000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        graphics.blit(new ResourceLocation("crazyae", "textures/guis/sprites/crafting_accelerator.png"),
                leftPos + 14, topPos + 10, 0, 0, 40, 40, 40, 40);
        graphics.blit(new ResourceLocation("crazyae", "textures/guis/sprites/crafting_storage.png"),
                leftPos + 14, topPos + 66, 0, 0, 40, 40, 40, 40);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        int x = mouseX - leftPos, y = mouseY - topPos;
        if (x < 10 || x >= 54) return;
        List<Component> tooltip = new ArrayList<>();
        if (y >= 5 && y < 53) {
            tooltip.add(Component.translatable("gui.crazyae.combiner.threads").append(" " + menu.threads));
        } else if (y >= 62 && y < 110) {
            tooltip.add(Component.translatable("gui.crazy_capacity_bridge.capacity").append(" " + CapacityDisplay.compact(menu.exactCapacity)));
            tooltip.add(Component.translatable("gui.crazy_capacity_bridge.exact_bytes"));
            int columns = Math.max(1, (width - 24) / Math.max(1, font.width("0")));
            CapacityDisplay.exactLines(menu.exactCapacity, columns).forEach(line -> tooltip.add(Component.literal(line)));
        }
        if (!tooltip.isEmpty()) graphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
    }
}
