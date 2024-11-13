package com.xkball.terrain_tectonic_trilithic.recipe;

import com.xkball.terrain_tectonic_trilithic.recipe.crafing.FakeFoodRecipe;
import com.xkball.terrain_tectonic_trilithic.recipe.ingredient.FoodIngredient;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TTRecipes {
    
    public static final DeferredHolder<IngredientType<?>,IngredientType<FoodIngredient>> FOOD_INGREDIENT = TTRegistries.INGREDIENT_TYPES.register(
            "food_ingredient",() -> new IngredientType<>(FoodIngredient.CODEC,FoodIngredient.STREAM_CODEC)
    );
    
    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<FakeFoodRecipe>> FAKE_FOOD_SERIALIZER = TTRegistries.RECIPE_SERIALIZER.register(
            "fake_food_serializer",() -> new SimpleCraftingRecipeSerializer<>(FakeFoodRecipe::new)
    );
    
    public static void init() {}
}
