package dev.sora.sparky.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.inventory.ModChestMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class ModChestScreen extends AbstractContainerScreen<ModChestMenu> implements MenuAccess<ModChestMenu> {

    private static final int textureXSize = 256;
    private static final int textureYSize = 256;
    private static final ResourceLocation guiTexture = new ResourceLocation("minecraft","textures/gui/container/generic_54.png");
    private static final ResourceLocation widgetTexture  = new ResourceLocation(Sparky.MODID,"textures/gui/container/widget.png");

    public ModChestScreen(ModChestMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, 8.0F, 6.0F, 4210752);

        this.font.draw(matrixStack, this.playerInventoryTitle, 8.0F, (float) (this.imageHeight - 96 + 2), 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, guiTexture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight, this.textureXSize, this.textureYSize);

        RenderSystem.setShaderTexture(0, widgetTexture);

        blit(poseStack, x + 176, y + 25, 0, 0, 25, 26, this.textureXSize, this.textureYSize);

    }

}
