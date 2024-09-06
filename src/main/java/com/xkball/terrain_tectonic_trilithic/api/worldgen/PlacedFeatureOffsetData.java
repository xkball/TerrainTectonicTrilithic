package com.xkball.terrain_tectonic_trilithic.api.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record PlacedFeatureOffsetData(ResourceKey<PlacedFeature> placedFeatureKey, BlockPos offset) {
    
    public static final Codec<PlacedFeatureOffsetData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ResourceKey.codec(Registries.PLACED_FEATURE).fieldOf("placedFeatureKey").forGetter(o -> o.placedFeatureKey),
            BlockPos.CODEC.fieldOf("offset").forGetter(o -> o.offset)
    ).apply(ins, PlacedFeatureOffsetData::new));
    
}
