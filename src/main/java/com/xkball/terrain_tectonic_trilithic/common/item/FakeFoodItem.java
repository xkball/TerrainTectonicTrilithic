package com.xkball.terrain_tectonic_trilithic.common.item;

import com.mojang.serialization.Codec;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@NonNullByDefault
public class FakeFoodItem extends Item {
    
    public FakeFoodItem(Properties properties) {
        super(properties);
    }
    
    public record FakeItem(ItemStack itemStack){
        public static final Codec<FakeItem> CODEC = ItemStack.CODEC.xmap(FakeItem::new,FakeItem::itemStack);
    }
    
    
    
}
