package com.xkball.terrain_tectonic_trilithic.common.item;

import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TTDataComponents {
    
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<FakeFoodItem.FakeItem>> FAKE_ITEM = TTRegistries.DATA_COMPONENT.register(
            "fake_item", () -> DataComponentType.<FakeFoodItem.FakeItem>builder().persistent(FakeFoodItem.FakeItem.CODEC).build()
    );
    
    public static void init(){}
}
