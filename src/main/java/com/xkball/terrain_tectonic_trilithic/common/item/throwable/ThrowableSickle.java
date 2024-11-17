package com.xkball.terrain_tectonic_trilithic.common.item.throwable;

import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.entity.ThrownToolEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;

@NonNullByDefault
public class ThrowableSickle extends ThrowableTool{
    
    public static final Set<ItemAbility> SICKLE_ABILITIES = ItemAbilities.DEFAULT_SWORD_ACTIONS;
    
    
    public ThrowableSickle() {
        super(ThrowableTool.sickleProperties());
    }
    
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return SICKLE_ABILITIES.contains(itemAbility);
    }
    
    @Override
    public ThrownToolEntity.BehaviorType getBehaviorType() {
        return ThrownToolEntity.BehaviorType.SICKLE;
    }
}
