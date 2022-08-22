package dev.sora.sparky.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.inventory.ModChestMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
@OnlyIn(Dist.CLIENT)
public class ModChestScreen extends AbstractContainerScreen<ModChestMenu> implements MenuAccess<ModChestMenu> {

    private static final int textureXSize = 256;
    private static final int textureYSize = 256;
    private static final ResourceLocation guiTexture = new ResourceLocation("minecraft","textures/gui/container/generic_54.png");
    private static final ResourceLocation widgetTexture  = new ResourceLocation(Sparky.MODID,"textures/gui/container/widget.png");
    private static final TranslatableContents noRecordContent = new TranslatableContents("gui.sparky.mod_chest.no_record");
    private static final TranslatableContents recordedContent = new TranslatableContents("gui.sparky.mod_chest.recorded");

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
        TranslatableContents title;
        MutableComponent titleComponent;
        if (getMenu().recordSlot.getItem().isEmpty()){
            title = noRecordContent;
            titleComponent = MutableComponent.create(title);
        }else {
            ItemStack itemStack = getMenu().recordSlot.getItem();
            title = recordedContent;
            titleComponent = MutableComponent.create(title).append(Objects.requireNonNull(itemStack.getItem().getCreatorModId(itemStack)));
        }
        this.font.draw(matrixStack, titleComponent, 8.0F, 6.0F, 4210752);

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
