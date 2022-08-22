package dev.sora.sparky.common.block;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.client.renderer.BlockItemRenderer;
import dev.sora.sparky.common.block.entity.BlockEntityInitializer;
import dev.sora.sparky.common.block.entity.ModChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class BlockInitializer {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Sparky.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Sparky.MODID);

    public static final RegistryObject<Block> MOD_CHEST_BLOCK;
    public static final RegistryObject<Item> MOD_CHEST_ITEM;

    static {
        MOD_CHEST_BLOCK = BLOCKS.register("mod_chest",()->new ModChestBlock(BlockBehaviour.Properties.of(Material.WOOD), BlockEntityInitializer.MOD_CHEST::get));

        MOD_CHEST_ITEM = ITEMS.register("mod_chest",()->new BlockItem(MOD_CHEST_BLOCK.get(),new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)){
            @Override
            public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                super.initializeClient(consumer);
                consumer.accept(new IClientItemExtensions(){
                    Supplier<BlockEntity> model = ()->new ModChestBlockEntity(BlockPos.ZERO,BlockInitializer.MOD_CHEST_BLOCK.get().defaultBlockState());
                    /**
                     * Queries this item's renderer.
                     * <p>
                     * Only used if {@link BakedModel#isCustomRenderer()} returns {@code true} or {@link BlockState#getRenderShape()}
                     * returns {@link RenderShape#ENTITYBLOCK_ANIMATED}.
                     * <p>
                     * By default, returns vanilla's block entity renderer.
                     */
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return new BlockItemRenderer<>(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), model);
                    }
                });
            }
        });
    }
}
