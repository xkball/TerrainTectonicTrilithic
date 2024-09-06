package com.xkball.terrain_tectonic_trilithic.api.worldgen;

import javax.annotation.Nullable;

public interface TTFeatureExtension {
    
    void terrainTectonicTrilithic$hook(@Nullable FeatureReplacementData data);
    
    @Nullable
    FeatureReplacementData terrainTectonicTrilithic$getFeatureReplacementData();
}
