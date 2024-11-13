package com.xkball.terrain_tectonic_trilithic.datagen.recipe;

import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.recipe.crafing.FakeFoodRecipe;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;

import java.util.concurrent.CompletableFuture;

@NonNullByDefault
public class TTRecipeProvider extends RecipeProvider {
    
    public TTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        SpecialRecipeBuilder.special(FakeFoodRecipe::new).save(recipeOutput, VanillaUtils.modRL("fake_food"));
    }
}
