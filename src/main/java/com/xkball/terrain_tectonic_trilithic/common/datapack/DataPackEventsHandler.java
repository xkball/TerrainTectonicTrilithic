package com.xkball.terrain_tectonic_trilithic.common.datapack;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class DataPackEventsHandler {
    
    @EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventHandler{
        @SubscribeEvent
        public static void onRegDataPack(DataPackRegistryEvent.NewRegistry event){
            event.dataPackRegistry(TTDataPacks.FEATURE_REPLACEMENT, FeatureReplacementData.CODEC);
        }
    }
    
    @EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, bus = EventBusSubscriber.Bus.GAME)
    public static class GameEventHandler{
        @SubscribeEvent
        public static void onDataPackReload(AddReloadListenerEvent event){
            event.addListener(
                    (PreparableReloadListener.PreparationBarrier preparationBarrier,
                         ResourceManager resourceManager,
                         ProfilerFiller preparationsProfiler,
                         ProfilerFiller reloadProfiler,
                         Executor backgroundExecutor,
                         Executor gameExecutor) ->
                            CompletableFuture.runAsync(() -> {},backgroundExecutor)
                                 .thenCompose(preparationBarrier::wait)
                                 .thenRunAsync(() -> FeatureReplacementData.runFeatureReplaceHook(event.getRegistryAccess()),gameExecutor)
            );
        }
    }
    

}
