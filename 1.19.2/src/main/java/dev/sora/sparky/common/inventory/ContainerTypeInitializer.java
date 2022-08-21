package dev.sora.sparky.common.inventory;

import dev.sora.sparky.Sparky;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author icemeowzhi
 * @date 20/8/2022
 * @apiNote
 */
public class ContainerTypeInitializer {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Sparky.MODID);
    public static final RegistryObject<MenuType<ModChestMenu>> MOD_CHEST;
    static {
        MOD_CHEST = MENU_TYPES.register("mod_chest", () -> new MenuType<>(ModChestMenu::createContainer));
    }
}
