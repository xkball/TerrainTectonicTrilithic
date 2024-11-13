package com.xkball.terrain_tectonic_trilithic.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.recipe.TTRecipes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.stream.Stream;

@NonNullByDefault
public class FoodIngredient implements ICustomIngredient {
    
    public static final MapCodec<FoodIngredient> CODEC = MapCodec.unit(FoodIngredient::new);
    
    public static final StreamCodec<ByteBuf,FoodIngredient> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FoodIngredient decode(ByteBuf buffer) {
            return new FoodIngredient();
        }
        
        @Override
        public void encode(ByteBuf buffer, FoodIngredient value) {
        
        }
    };
    
    @Override
    public boolean test(ItemStack stack) {
        return stack.has(DataComponents.FOOD);
    }
    
    @Override
    public Stream<ItemStack> getItems() {
        return BuiltInRegistries.ITEM.stream()
            .filter(item -> item.getDefaultInstance().has(DataComponents.FOOD))
            .map(ItemStack::new);
    }
    
    @Override
    public boolean isSimple() {
        return false;
    }
    
    @Override
    public IngredientType<?> getType() {
        return TTRecipes.FOOD_INGREDIENT.get();
    }
}
