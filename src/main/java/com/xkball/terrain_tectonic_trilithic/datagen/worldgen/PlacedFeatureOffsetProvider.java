package com.xkball.terrain_tectonic_trilithic.datagen.worldgen;

import com.xkball.terrain_tectonic_trilithic.api.worldgen.PlacedFeatureOffsetData;
import com.xkball.terrain_tectonic_trilithic.datagen.TTDataPackProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.CavePlacements;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlacedFeatureOffsetProvider extends TTDataPackProvider<PlacedFeatureOffsetData> {
    
    public PlacedFeatureOffsetProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, modid, registries, PlacedFeatureOffsetData.CODEC);
    }
    
    @Override
    protected void buildObjectList() {
        addObject("cave_vines",new PlacedFeatureOffsetData(
                CavePlacements.CAVE_VINES,new BlockPos(0,1,0)
        ));
    }
    
    @Override
    protected Path resolveDirectory(Path root) {
        return root.resolve("world_gen").resolve("placed_feature_offsets");
    }
    
    @Override
    public String getName() {
        return "placed_feature_offsets";
    }
}
