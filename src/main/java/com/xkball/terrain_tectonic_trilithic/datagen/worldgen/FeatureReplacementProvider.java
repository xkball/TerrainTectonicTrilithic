package com.xkball.terrain_tectonic_trilithic.datagen.worldgen;

import com.mojang.datafixers.util.Pair;
import com.xkball.terrain_tectonic_trilithic.api.block.BlockStatePredicate;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import com.xkball.terrain_tectonic_trilithic.common.block.TTBlocks;
import com.xkball.terrain_tectonic_trilithic.datagen.TTDataPackProvider;
import com.xkball.terrain_tectonic_trilithic.utils.predicate.PredicatePredicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FeatureReplacementProvider extends TTDataPackProvider<FeatureReplacementData> {
    
    public FeatureReplacementProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, modid, registries, FeatureReplacementData.CODEC);
    }
    
    @Override
    protected void buildObjectList() {
        addObject("blue_ice",new FeatureReplacementData("blue_ice",List.of(
                new FeatureReplacementData.SingleReplacementData(
                        new PredicatePredicate<>(new BlockStatePredicate(BlockPredicate.Builder.block().of(Blocks.BLUE_ICE).build())),
                        List.of(new Pair<>(TTBlocks.GLACIED_ORE.get().defaultBlockState(),5)),
                        50
                )
        )));
        addObject("iceberg",new FeatureReplacementData("iceberg",List.of(
                new FeatureReplacementData.SingleReplacementData(
                        new PredicatePredicate<>(new BlockStatePredicate(BlockPredicate.Builder.block().of(Blocks.BLUE_ICE).build())),
                        List.of(new Pair<>(TTBlocks.GLACIED_ORE.get().defaultBlockState(),5)),
                        50
                )
        )));
    }
    
    @Override
    protected Path resolveDirectory(Path root) {
        return root.resolve("world_gen").resolve("feature_replacements");
    }
    
    @Override
    public String getName() {
        return "feature_replacements";
    }
}
