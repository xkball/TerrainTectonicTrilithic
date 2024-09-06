package com.xkball.terrain_tectonic_trilithic.common.datapack;

import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class TTDataPacks {
    
    public static final ResourceKey<Registry<FeatureReplacementData>> FEATURE_REPLACEMENT = ResourceKey.createRegistryKey(VanillaUtils.modRL("world_gen/feature_replacements"));
    
}
