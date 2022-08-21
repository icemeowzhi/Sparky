package dev.sora.sparky.client.model;

import dev.sora.sparky.Sparky;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
@Mod.EventBusSubscriber(modid = Sparky.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelInitializer {
    public static final ResourceLocation MOD_CHEST = new ResourceLocation(Sparky.MODID,"model/mod_chest");

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (!event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            return;
        }

        event.addSprite(MOD_CHEST);
    }
}
