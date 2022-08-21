package dev.sora.sparky.common.item;

import dev.sora.sparky.Sparky;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class ItemInitializer {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Sparky.MODID);
}
