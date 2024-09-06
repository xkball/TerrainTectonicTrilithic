package com.xkball.terrain_tectonic_trilithic.common.worldgen.feature;

import com.mojang.serialization.Codec;
import com.xkball.terrain_tectonic_trilithic.common.block.TTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LakeBottomBlockFeature extends Feature<NoneFeatureConfiguration> {
    
    private static final int DETECT_START_Y = 15;
    
    public LakeBottomBlockFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var level = context.level();
        var random = context.random();
        var pos = context.origin();
        if(level.getFluidState(pos).is(FluidTags.LAVA)) return false;
        var ox = pos.getX();
        var oz = pos.getZ();
        var genPos = isPosUnderLake(level,ox,oz);
        int setCount = 0;
        while (genPos != null){
            setCount++;
            this.setBlock(level,genPos, TTBlocks.BLAZE_ORE.get().defaultBlockState());
            var rx = genPos.getX()+random.nextInt(6)-3;
            var rz = genPos.getZ()+random.nextInt(6)-3;
            if(setCount >= 12 || Math.abs(ox-rx) >= 16 || Math.abs(oz-rz) >= 16 || random.nextInt(688) <= 64) break;
            genPos = isPosUnderLake(level,rx,rz);
        }
        return setCount>0;
    }
    
    @Nullable
    public BlockPos.MutableBlockPos isPosUnderLake(LevelAccessor level,int x,int z) {
        var pos = new BlockPos.MutableBlockPos(x, DETECT_START_Y, z);
        while (level.getBlockState(pos).is(BlockTags.NETHER_CARVER_REPLACEABLES)) {
            pos.move(0,1,0);
            if(pos.getY()>32) return null;
        }
        var checkLava = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        int lavaThickness = 1;
        while(level.getFluidState(checkLava).is(FluidTags.LAVA) && lavaThickness<5) {
            checkLava.move(0,1,0);
            lavaThickness++;
        }
        if(lavaThickness>=5) {
            return pos.move(0,-1,0);
        }
        return null;
    }
}
