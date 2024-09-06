package com.xkball.terrain_tectonic_trilithic.mixin.worldgen;

import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.TTFeatureExtension;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.TTFeaturePlaceContextExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Feature.class)
public class TTMixinFeature implements TTFeatureExtension {
    
    @Unique
    private boolean terrainTectonicTrilithic$hooked = false;
    @Unique
    private FeatureReplacementData terrainTectonicTrilithic$featureReplacementData;
    
    @Unique
    private TTFeaturePlaceContextExtension terrainTectonicTrilithic$featurePlaceContext;
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/Feature;place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z"))
    public <FC extends FeatureConfiguration> boolean onPlaceHead(Feature instance, FeaturePlaceContext<FC> context){
        if(terrainTectonicTrilithic$hooked){
            ((TTFeaturePlaceContextExtension) context).terrainTectonicTrilithic$hookTheWorld(terrainTectonicTrilithic$featureReplacementData);
            terrainTectonicTrilithic$featurePlaceContext = (TTFeaturePlaceContextExtension) context;
        }
        return instance.place(context);
    }
    
    @Inject(method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
            at = @At("RETURN"))
    public <FC extends FeatureConfiguration> void onPlaceEnd(FC config, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir){
        if(!terrainTectonicTrilithic$hooked) return;
        if(terrainTectonicTrilithic$featurePlaceContext != null) terrainTectonicTrilithic$featurePlaceContext.terrainTectonicTrilithic$finishHook();
        terrainTectonicTrilithic$featurePlaceContext = null;
    }
    
    public void terrainTectonicTrilithic$hook(@Nullable FeatureReplacementData data){
        terrainTectonicTrilithic$featureReplacementData = data;
        terrainTectonicTrilithic$hooked = data != null;
    }
    
    @Nullable
    @Override
    public FeatureReplacementData terrainTectonicTrilithic$getFeatureReplacementData() {
        return terrainTectonicTrilithic$featureReplacementData;
    }
}
