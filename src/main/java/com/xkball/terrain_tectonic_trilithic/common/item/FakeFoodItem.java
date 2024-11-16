package com.xkball.terrain_tectonic_trilithic.common.item;

import com.mojang.serialization.Codec;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

@NonNullByDefault
public class FakeFoodItem extends Item {
    
    public FakeFoodItem(Properties properties) {
        super(properties);
    }
    
    public record FakeItem(ItemStack itemStack){
        public static final Codec<FakeItem> CODEC = ItemStack.CODEC.xmap(FakeItem::new,FakeItem::itemStack);
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FakeItem fakeItem = (FakeItem) o;
            return ItemStack.isSameItemSameComponents(itemStack,fakeItem.itemStack);
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(itemStack);
        }
    }
    
    
    
}
