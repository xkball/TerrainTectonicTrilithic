package com.xkball.terrain_tectonic_trilithic.recipe.crafing;

import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.item.FakeFoodItem;
import com.xkball.terrain_tectonic_trilithic.common.item.TTDataComponents;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.recipe.TTRecipes;
import com.xkball.terrain_tectonic_trilithic.recipe.ingredient.FoodIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Objects;

@NonNullByDefault
public class FakeFoodRecipe extends CustomRecipe {
    
    private static final FoodIngredient FOOD_INGREDIENT = new FoodIngredient();
    
    public FakeFoodRecipe(CraftingBookCategory category) {
        super(category);
    }
    
    @Override
    public boolean matches(CraftingInput input, Level level) {
        var items = input.items().stream().filter(it -> !it.isEmpty()).toList();
        var foodList = new ArrayList<ItemStack>(2);
        var oreList = new ArrayList<ItemStack>(8);
        for (ItemStack item : items) {
            if (FOOD_INGREDIENT.test(item)) foodList.add(item);
            if (item.is(TTItems.CHLOROPHYLL_SHARD.get())) oreList.add(item);
        }
        if(foodList.size() != 1 || foodList.size() + oreList.size() < items.size()) return false;
        var foodProperties = foodList.getFirst().get(DataComponents.FOOD);
        return foodProperties != null && foodProperties.nutrition() == oreList.size();
    }
    
    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        var result = new ItemStack(TTItems.FAKE_FOOD);
        var item = input.items().stream()
                .filter(FOOD_INGREDIENT::test)
                .findFirst().orElseThrow();
        var food = Objects.requireNonNull(item.get(DataComponents.FOOD));
        result.set(DataComponents.FOOD,new FoodProperties.Builder().nutrition(food.nutrition()).saturationModifier(food.saturation()/(2*food.nutrition())).build());
        result.set(TTDataComponents.FAKE_ITEM,new FakeFoodItem.FakeItem(item.copyWithCount(1)));
        result.set(DataComponents.ITEM_NAME, Component.literal("\"").append(item.getHoverName()).append("\""));
        return result;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        
        for (int i = 0; i < nonnulllist.size(); i++) {
            ItemStack item = input.getItem(i);
            if (FOOD_INGREDIENT.test(item)) {
                nonnulllist.set(i, item.copy());
            }
        }
        
        return nonnulllist;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height > 1;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return TTRecipes.FAKE_FOOD_SERIALIZER.get();
    }
}
