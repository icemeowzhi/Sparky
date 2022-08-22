package dev.sora.sparky;

import com.mojang.logging.LogUtils;
import dev.sora.sparky.client.renderer.ModChestRenderer;
import dev.sora.sparky.client.screen.ModChestScreen;
import dev.sora.sparky.common.block.BlockInitializer;
import dev.sora.sparky.common.block.entity.BlockEntityInitializer;
import dev.sora.sparky.common.data.lang.EnglishLanguageProvider;
import dev.sora.sparky.common.data.lang.HKTraditionalChineseLanguageProvider;
import dev.sora.sparky.common.data.lang.SimplifiedChineseLanguageProvider;
import dev.sora.sparky.common.data.lang.TWTraditionalChineseLanguageProvider;
import dev.sora.sparky.common.data.recipe.SparkyRecipeProvider;
import dev.sora.sparky.common.inventory.ContainerTypeInitializer;
import dev.sora.sparky.common.item.ItemInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Sparky.MODID)
public class Sparky
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "sparky";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public Sparky()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // Client setup
            modEventBus.addListener(this::setupClient);
        });

        // BLOCK
        BlockInitializer.BLOCKS.register(modEventBus);
        // ITEM
        BlockInitializer.ITEMS.register(modEventBus);
        ItemInitializer.ITEMS.register(modEventBus);
        //BLOCK_ENTITY
        BlockEntityInitializer.BLOCK_ENTITIES.register(modEventBus);
        //CONTAINER_TYPE
        ContainerTypeInitializer.MENU_TYPES.register(modEventBus);

        // DataGen
        modEventBus.addListener(this::onGatherData);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        MenuScreens.register(ContainerTypeInitializer.MOD_CHEST.get(), ModChestScreen::new);

        BlockEntityRenderers.register(BlockEntityInitializer.MOD_CHEST.get(), ModChestRenderer::new);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("Sparky is loading...");
    }

    private void onGatherData(final GatherDataEvent event){
        var gen = event.getGenerator();
        var helper = event.getExistingFileHelper();
        // language
        gen.addProvider(true, new EnglishLanguageProvider(gen));
        gen.addProvider(true, new SimplifiedChineseLanguageProvider(gen));
        gen.addProvider(true, new HKTraditionalChineseLanguageProvider(gen));
        gen.addProvider(true, new TWTraditionalChineseLanguageProvider(gen));
        // recipe
        gen.addProvider(true, new SparkyRecipeProvider(gen));
    }

}
