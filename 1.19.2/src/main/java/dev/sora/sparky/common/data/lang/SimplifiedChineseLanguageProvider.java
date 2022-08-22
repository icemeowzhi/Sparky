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
public class SimplifiedChineseLanguageProvider extends LanguageProvider {

    public SimplifiedChineseLanguageProvider(DataGenerator gen) {
        super(gen, Sparky.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(BlockInitializer.MOD_CHEST_BLOCK.get(),"模组箱");
        add("gui.sparky.mod_chest.no_record","未记录");
        add("gui.sparky.mod_chest.recorded","记录的模组: ");
    }
}
