package com.xkball.terrain_tectonic_trilithic.common.block;

import com.xkball.terrain_tectonic_trilithic.api.block.LevelPosPredicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StateRequireRenewableOre extends RenewableOreBlock{
    
    private final LevelPosPredicate canPlaceOre;
    private final LevelPosPredicate canGrowAge;
    
    public StateRequireRenewableOre(Properties properties, LevelPosPredicate canPlaceOre, LevelPosPredicate canGrowAge) {
        super(properties);
        this.canPlaceOre = canPlaceOre;
        this.canGrowAge = canGrowAge;
    }
    
    @Override
    protected void tryPlaceNewOre(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var mutPos = pos.mutable();
        for (int i = 0; i < 9; i++) {
            mutPos.setWithOffset(pos,random.nextInt(5)-3,random.nextInt(5)-3,random.nextInt(5)-3);
            if(canPlaceOre.test(level, mutPos)) {
                level.setBlock(mutPos,this.defaultBlockState(), Block.UPDATE_ALL);
                return;
            }
        }
    }
    
    @Override
    protected boolean canGrow(BlockState state, ServerLevel level, BlockPos pos) {
        return canGrowAge.test(level, pos);
    }
}
