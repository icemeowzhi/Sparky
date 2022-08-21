package dev.sora.sparky.common.block;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.block.entity.BlockEntityInitializer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

        MOD_CHEST_ITEM = ITEMS.register("mod_chest",()->new BlockItem(MOD_CHEST_BLOCK.get(),new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    }
}
