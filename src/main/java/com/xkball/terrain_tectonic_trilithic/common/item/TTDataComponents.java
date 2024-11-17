package com.xkball.terrain_tectonic_trilithic.common.item;

import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TTDataComponents {
    
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<FakeFoodItem.FakeItem>> FAKE_ITEM = TTRegistries.DATA_COMPONENT.register(
            "fake_item", () -> DataComponentType.<FakeFoodItem.FakeItem>builder().persistent(FakeFoodItem.FakeItem.CODEC).build()
    );
    
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<PlateShieldItem.ShieldDataMap>> SHIELD_DATA_MAP = TTRegistries.DATA_COMPONENT.register("shield_data_map",
            () -> DataComponentType.<PlateShieldItem.ShieldDataMap>builder().persistent(PlateShieldItem.ShieldDataMap.CODEC).networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(PlateShieldItem.ShieldDataMap.CODEC)).build()
    );
    
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<PlateShieldItem.ShieldData>> SHIELD_DATA = TTRegistries.DATA_COMPONENT.register("shield_data",
            () -> DataComponentType.<PlateShieldItem.ShieldData>builder().persistent(PlateShieldItem.ShieldData.CODEC).build()
    );
    
    public static void init(){}
}
