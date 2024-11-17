package com.xkball.terrain_tectonic_trilithic.datagen.recipe;

import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.recipe.crafing.FakeFoodRecipe;
import com.xkball.terrain_tectonic_trilithic.recipe.smithing.ShieldApplyRecipe;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

@NonNullByDefault
public class TTRecipeProvider extends RecipeProvider {
    
    public TTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        SpecialRecipeBuilder.special(FakeFoodRecipe::new).save(recipeOutput, VanillaUtils.modRL("fake_food"));
        recipeOutput.accept(VanillaUtils.modRL("shield_apply"),new ShieldApplyRecipe(),null);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TTItems.BLAZE_PICKAXE_HEAD,1)
                .define('x',TTItems.BLAZE_SHARD)
                .define('y', Items.SPECTRAL_ARROW)
                .pattern("xxx")
                .pattern(" yx")
                .pattern("y x")
                .unlockedBy("get_blaze_shard",has(TTItems.BLAZE_SHIELD))
                .unlockedBy("get_special_arrow",has(Items.SPECTRAL_ARROW))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TTItems.GLACIED_PICKAXE_HEAD,1)
                .define('x',TTItems.GLACIED_SHARD)
                .define('y', Items.SPECTRAL_ARROW)
                .pattern("xxx")
                .pattern(" yx")
                .pattern("y x")
                .unlockedBy("get_glacied_shard",has(TTItems.GLACIED_SHARD))
                .unlockedBy("get_special_arrow",has(Items.SPECTRAL_ARROW))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TTItems.THROWABLE_PICKAXE,1)
                .define('x',TTItems.BLAZE_PICKAXE_HEAD)
                .define('y', TTItems.GLACIED_PICKAXE_HEAD)
                .define('z',Items.STICK)
                .pattern("  x")
                .pattern(" z ")
                .pattern("y  ")
                .unlockedBy("get_head1",has(TTItems.GLACIED_PICKAXE_HEAD))
                .unlockedBy("get_head2",has(TTItems.BLAZE_PICKAXE_HEAD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TTItems.THROWABLE_SICKLE,1)
                .define('x',TTItems.GLACIED_PICKAXE_HEAD)
                .define('y', TTItems.CHLOROPHYLL_SHARD)
                .define('z',Items.STICK)
                .pattern("yyy")
                .pattern(" xy")
                .pattern("z  ")
                .unlockedBy("get_head1",has(TTItems.GLACIED_PICKAXE_HEAD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TTItems.BLAZE_SHIELD,1)
                .define('X',Items.AMETHYST_SHARD)
                .define('Y', TTItems.BLAZE_SHARD)
                .pattern("XYX")
                .pattern("YYY")
                .pattern("XYX")
                .unlockedBy("get_shard",has(TTItems.BLAZE_SHARD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TTItems.GLACIED_SHIELD,1)
                .define('X',Items.AMETHYST_SHARD)
                .define('Y', TTItems.GLACIED_SHARD)
                .pattern("XYX")
                .pattern("YYY")
                .pattern("XYX")
                .unlockedBy("get_shard",has(TTItems.BLAZE_SHARD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TTItems.CHLOROPHYLL_SHIELD,1)
                .define('X',Items.AMETHYST_SHARD)
                .define('Y', TTItems.CHLOROPHYLL_SHARD)
                .pattern("XYX")
                .pattern("YYY")
                .pattern("XYX")
                .unlockedBy("get_shard",has(TTItems.BLAZE_SHARD))
                .save(recipeOutput);
    }
}
