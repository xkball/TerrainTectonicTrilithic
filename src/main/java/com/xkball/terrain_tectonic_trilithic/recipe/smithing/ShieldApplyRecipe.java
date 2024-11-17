package com.xkball.terrain_tectonic_trilithic.recipe.smithing;

import com.mojang.serialization.MapCodec;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.item.PlateShieldItem;
import com.xkball.terrain_tectonic_trilithic.common.item.TTDataComponents;
import com.xkball.terrain_tectonic_trilithic.recipe.TTRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Objects;

@NonNullByDefault
public class ShieldApplyRecipe implements SmithingRecipe {
    
    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return stack.is(ItemTags.TRIMMABLE_ARMOR);
    }
    
    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.has(TTDataComponents.SHIELD_DATA);
    }
    
    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        if(!(isBaseIngredient(input.base()) && isAdditionIngredient(input.addition()) && input.template().isEmpty())) return false;
        if(!input.base().has(TTDataComponents.SHIELD_DATA_MAP)) return true;
        var dataMap = Objects.requireNonNull(input.base().get(TTDataComponents.SHIELD_DATA_MAP)).copy();
        var inputData = Objects.requireNonNull(PlateShieldItem.ShieldData.fromItemStack(input.addition()));
        if(!dataMap.containsKey(inputData.type())) return true;
        var data = Objects.requireNonNull(dataMap.get(inputData.type()));
        return ItemStack.isSameItemSameComponents(data.shadowItemStack(), inputData.shadowItemStack());
    }
    
    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        var result = input.base().copy();
        var data = Objects.requireNonNull(input.addition().get(TTDataComponents.SHIELD_DATA));
        PlateShieldItem.ShieldDataMap dataMap;
        if(!result.has(TTDataComponents.SHIELD_DATA_MAP)) dataMap = new PlateShieldItem.ShieldDataMap();
        else dataMap = Objects.requireNonNull(result.get(TTDataComponents.SHIELD_DATA_MAP));
        dataMap.computeIfPresent(data.type(),
                (k, v) -> new PlateShieldItem.ShieldData(data.type(), v.maxDurability() + data.maxDurability(), v.durability() + data.durability(), v.shadowItemStack()));
        dataMap.putIfAbsent(data.type(),new PlateShieldItem.ShieldData(data.type(),data.maxDurability(),data.durability(),input.addition().copy()));
        result.set(TTDataComponents.SHIELD_DATA_MAP,dataMap);
        return result;
    }
    
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return Items.IRON_CHESTPLATE.getDefaultInstance();
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return TTRecipes.SHIELD_APPLY_SERIALIZER.get();
    }
    
    public static class Serializer implements RecipeSerializer<ShieldApplyRecipe> {
        
        public static final MapCodec<ShieldApplyRecipe> CODEC = MapCodec.unit(ShieldApplyRecipe::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, ShieldApplyRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());
        @Override
        public MapCodec<ShieldApplyRecipe> codec() {
            return CODEC;
        }
        
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShieldApplyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
