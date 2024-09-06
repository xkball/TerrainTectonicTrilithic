package com.xkball.terrain_tectonic_trilithic.mixin.worldgen;

import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.TTFeaturePlaceContextExtension;
import com.xkball.terrain_tectonic_trilithic.common.level.WorldGenFakeLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FeaturePlaceContext.class)
public abstract class TTMixinFeaturePlaceContext implements TTFeaturePlaceContextExtension {
    
    @Shadow public abstract WorldGenLevel level();
    
    @Shadow public abstract RandomSource random();
    
    @Unique
    private boolean terrainTectonicTrilithic$hooked;
    @Unique
    private WorldGenFakeLevel terrainTectonicTrilithic$fakeLevel = null;
    
    @Override
    public void terrainTectonicTrilithic$hookTheWorld(FeatureReplacementData data) {
        if (terrainTectonicTrilithic$hooked) return;
        terrainTectonicTrilithic$fakeLevel = new WorldGenFakeLevel(this.level(),data);
        terrainTectonicTrilithic$hooked = true;
    }
    
    @Override
    public void terrainTectonicTrilithic$finishHook() {
        if (!terrainTectonicTrilithic$hooked) return;
        terrainTectonicTrilithic$fakeLevel.doAllSetBlock(this.random());
        terrainTectonicTrilithic$hooked = false;
    }
    
    @Inject(method = "level",at = @At("HEAD"),cancellable = true)
    public void onGetLevel(CallbackInfoReturnable<WorldGenLevel> cir) {
        if(!terrainTectonicTrilithic$hooked) return;
        cir.setReturnValue(terrainTectonicTrilithic$fakeLevel);
        cir.cancel();
    }
    
}
