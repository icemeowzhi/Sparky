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
public class EnglishLanguageProvider extends LanguageProvider {

    public EnglishLanguageProvider(DataGenerator gen) {
        super(gen, Sparky.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(BlockInitializer.MOD_CHEST_BLOCK.get(),"simple mod chest");
        add("gui.sparky.mod_chest.no_record","No record yet.");
        add("gui.sparky.mod_chest.recorded","Recorded mod id: ");
    }
}
