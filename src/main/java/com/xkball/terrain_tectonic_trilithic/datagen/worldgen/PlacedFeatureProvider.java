package com.xkball.terrain_tectonic_trilithic.datagen.worldgen;

import com.xkball.terrain_tectonic_trilithic.datagen.TTDataPackProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlacedFeatureProvider extends TTDataPackProvider<PlacedFeature> {
    
    public PlacedFeatureProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, modid, registries, PlacedFeature.DIRECT_CODEC);
    }
    
    @Override
    protected void buildObjectList() {
        addObject("lake_bottom_block",new PlacedFeature(Holder.direct(ConfiguredFeatureProvider.LAKE_BOTTOM_BLOCK_CONFIGURED_FEATURE), List.of(
                RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), BiomeFilter.biome()
        )));
    }
    
    @Override
    protected Path resolveDirectory(Path root) {
        return root.getParent().resolve("worldgen").resolve("placed_feature");
    }
    
    @Override
    public String getName() {
        return "placed_feature";
    }
}
