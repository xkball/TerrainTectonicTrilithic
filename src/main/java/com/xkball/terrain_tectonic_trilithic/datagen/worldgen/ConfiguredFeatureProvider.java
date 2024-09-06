package com.xkball.terrain_tectonic_trilithic.datagen.worldgen;

import com.xkball.terrain_tectonic_trilithic.common.worldgen.feature.LakeBottomBlockFeature;
import com.xkball.terrain_tectonic_trilithic.datagen.TTDataPackProvider;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConfiguredFeatureProvider extends TTDataPackProvider<ConfiguredFeature<?, ?>>{
    
    public static final ConfiguredFeature<NoneFeatureConfiguration, LakeBottomBlockFeature> LAKE_BOTTOM_BLOCK_CONFIGURED_FEATURE = new ConfiguredFeature<>(TTRegistries.LAKE_BOTTOM_BLOCK_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);
    
    public ConfiguredFeatureProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, modid, registries, ConfiguredFeature.DIRECT_CODEC);
    }
    
    @Override
    protected void buildObjectList() {
        addObject("lake_bottom_block", LAKE_BOTTOM_BLOCK_CONFIGURED_FEATURE);
    }
    
    @Override
    protected Path resolveDirectory(Path root) {
        return root.getParent().resolve("worldgen").resolve("configured_feature");
    }
    
    @Override
    public String getName() {
        return "configured_feature";
    }
}
