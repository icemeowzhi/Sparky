package dev.sora.sparky.common.data.recipe;

import dev.sora.sparky.Sparky;
import dev.sora.sparky.common.block.BlockInitializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

/**
 * @author icemeowzhi
 * @date 22/8/2022
 * @apiNote
 */
public class SparkyRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public SparkyRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(BlockInitializer.MOD_CHEST_ITEM.get())
                .define('S', Items.COBBLED_DEEPSLATE)
                .define('C' , Tags.Items.INGOTS_COPPER)
                .define('H' , Tags.Items.CHESTS_WOODEN)
                .pattern("SCS")
                .pattern("CHC")
                .pattern("SCS")
                .unlockedBy("has_copper_ingot",has(Tags.Items.INGOTS_COPPER))
                .save(consumer, location("mod_chest_deepslate"));

        ShapedRecipeBuilder.shaped(BlockInitializer.MOD_CHEST_ITEM.get())
                .define('S', Items.BLACKSTONE)
                .define('C' , Tags.Items.INGOTS_COPPER)
                .define('H' , Tags.Items.CHESTS_WOODEN)
                .pattern("SCS")
                .pattern("CHC")
                .pattern("SCS")
                .unlockedBy("has_copper_ingot",has(Tags.Items.INGOTS_COPPER))
                .save(consumer, location("mod_chest_blackstone"));

        ShapedRecipeBuilder.shaped(BlockInitializer.MOD_CHEST_ITEM.get())
                .define('S', Items.BASALT)
                .define('C' , Tags.Items.INGOTS_COPPER)
                .define('H' , Tags.Items.CHESTS_WOODEN)
                .pattern("SCS")
                .pattern("CHC")
                .pattern("SCS")
                .unlockedBy("has_copper_ingot",has(Tags.Items.INGOTS_COPPER))
                .save(consumer, location("mod_chest_basalt"));
    }



    private static ResourceLocation location(String id) {
        return new ResourceLocation(Sparky.MODID, id);
    }

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
}
