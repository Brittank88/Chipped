package earth.terrarium.chipped.client.screens;

import earth.terrarium.chipped.Chipped;
import earth.terrarium.chipped.common.menus.WorkbenchMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SlotWidget extends AbstractWidget {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Chipped.MOD_ID, "textures/gui/sprites/slot.png");

    private final ItemStack stack;
    private final WorkbenchMenu menu;
    private final int minY;
    private final int maxY;

    public SlotWidget(ItemStack stack, WorkbenchMenu menu, int minY, int maxY) {
        super(0, 0, 18, 18, CommonComponents.EMPTY);
        this.stack = stack;
        this.menu = menu;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(TEXTURE, getX(), getY(), 0, 0, 18, 18, 18, 18);

        graphics.renderItem(stack, getX() + 1, getY() + 1);
        if (isMouseOver(mouseX, mouseY)) {
            AbstractContainerScreen.renderSlotHighlight(graphics, getX() + 1, getY() + 1, 0);
        }
    }

    public void renderTooltip(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            if (!stack.isEmpty()) {
                graphics.renderTooltip(font, Screen.getTooltipFromItem(Minecraft.getInstance(), stack), stack.getTooltipImage(), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) && mouseY >= minY && mouseY <= maxY;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.clicked(mouseX, mouseY)) {
            if (stack.isEmpty() || mouseY < minY || mouseY > maxY) return false;
            menu.setChosenStack(stack);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
