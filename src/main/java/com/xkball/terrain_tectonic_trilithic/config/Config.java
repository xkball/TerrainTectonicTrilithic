package com.xkball.terrain_tectonic_trilithic.config;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;


@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{


    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    
    }
}
