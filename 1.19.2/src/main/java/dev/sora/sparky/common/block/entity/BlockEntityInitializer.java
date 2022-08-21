package dev.sora.sparky.common.block.entity;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.block.BlockInitializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class BlockEntityInitializer {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Sparky.MODID);

    public static final RegistryObject<BlockEntityType<ModChestBlockEntity>> MOD_CHEST;

    static {
        MOD_CHEST = BLOCK_ENTITIES.register("mod_chest",()->BlockEntityType.Builder.of(ModChestBlockEntity::new, BlockInitializer.MOD_CHEST_BLOCK.get()).build(null));
    }
}
