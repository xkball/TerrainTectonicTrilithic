package com.xkball.terrain_tectonic_trilithic.common.item.throwable;

import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.entity.ThrownToolEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;

@NonNullByDefault
public class ThrowablePickaxe extends ThrowableTool{
    
    public static final Set<ItemAbility> THE_PICKAXE_ABILITIES = Set.of(ItemAbilities.AXE_DIG, ItemAbilities.SWORD_DIG, ItemAbilities.PICKAXE_DIG, ItemAbilities.HOE_DIG, ItemAbilities.SHOVEL_DIG);
    
    public ThrowablePickaxe() {
        super(pickaxeProperties());
    }
    
    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return THE_PICKAXE_ABILITIES.contains(itemAbility);
    }
    
    @Override
    public ThrownToolEntity.BehaviorType getBehaviorType() {
        return ThrownToolEntity.BehaviorType.PICKAXE;
    }
}
