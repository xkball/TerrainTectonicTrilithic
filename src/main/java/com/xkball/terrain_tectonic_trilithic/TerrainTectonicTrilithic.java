package com.xkball.terrain_tectonic_trilithic;

import com.xkball.terrain_tectonic_trilithic.common.block.TTBlocks;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.registry.AutoRegManager;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import com.xkball.terrain_tectonic_trilithic.test.PredicateTest;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(TerrainTectonicTrilithic.MODID)
@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID)
public class TerrainTectonicTrilithic {
    
    public static final String MODID = "terrain_tectonic_trilithic";

    private static final Logger LOGGER = LogUtils.getLogger();


    public TerrainTectonicTrilithic(IEventBus modEventBus, ModContainer modContainer) {
        TTBlocks.init();
        TTItems.init();
        TTRegistries.init(modEventBus);
        AutoRegManager.init(modContainer);
        modEventBus.addListener(this::commonSetup);
        
    }
    
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    
    }
    
    
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if(!FMLEnvironment.production) PredicateTest.runTestAfterServerInit(event.getServer());
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
