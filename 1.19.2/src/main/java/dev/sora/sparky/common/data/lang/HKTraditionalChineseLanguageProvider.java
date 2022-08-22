package dev.sora.sparky.common.data.lang;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.block.BlockInitializer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * @author icemeowzhi
 * @date 22/8/2022
 * @apiNote
 */
public class HKTraditionalChineseLanguageProvider extends LanguageProvider {

    public HKTraditionalChineseLanguageProvider(DataGenerator gen) {
        super(gen, Sparky.MODID, " zh_hk");
    }

    @Override
    protected void addTranslations() {
        add(BlockInitializer.MOD_CHEST_BLOCK.get(),"模組箱");
        add("gui.sparky.mod_chest.no_record","未記錄");
        add("gui.sparky.mod_chest.recorded","記錄的模組: ");
    }
}
