package dev.sora.sparky.client;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.client.renderer.ModChestRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
@Mod.EventBusSubscriber(modid = Sparky.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LayerDefinitionInitializer {

    public static final ModelLayerLocation MOD_CHEST = new ModelLayerLocation(new ResourceLocation(Sparky.MODID, "mod_chest"), "main");

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MOD_CHEST, ModChestRenderer::createBodyLayer);
    }

}
