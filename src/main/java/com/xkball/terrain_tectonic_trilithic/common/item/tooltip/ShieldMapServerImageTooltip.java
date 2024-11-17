package com.xkball.terrain_tectonic_trilithic.common.item.tooltip;

import com.mojang.serialization.Codec;
import com.xkball.terrain_tectonic_trilithic.common.item.PlateShieldItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ShieldMapServerImageTooltip(List<ItemStack> shadowItem) implements TooltipComponent {
    public static final Codec<ShieldMapServerImageTooltip> CODEC = Codec.list(ItemStack.CODEC).xmap(ShieldMapServerImageTooltip::new,ShieldMapServerImageTooltip::shadowItem);
    
    public static ShieldMapServerImageTooltip fromShieldMap(Map<TagKey<DamageType>, PlateShieldItem.ShieldData> map){
        var result = new ArrayList<ItemStack>();
        for(var value : map.values()) {
            var item = value.shadowItemStack().copy();
            item.set(DataComponents.MAX_DAMAGE,value.maxDurability());
            item.set(DataComponents.DAMAGE,value.maxDurability()-value.durability());
            result.add(item);
        }
        return new ShieldMapServerImageTooltip(result);
    }
}
