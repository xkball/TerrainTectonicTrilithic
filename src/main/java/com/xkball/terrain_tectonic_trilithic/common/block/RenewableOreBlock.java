package com.xkball.terrain_tectonic_trilithic.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RenewableOreBlock extends Block {
    
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
    
    public RenewableOreBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
    
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var age = state.getValue(AGE);
        if (age == 5) {
            level.setBlockAndUpdate(pos,state.cycle(AGE));
            tryPlaceNewOre(state, level, pos, random);
        }
        else if(canGrow(state, level, pos)){
            level.setBlockAndUpdate(pos,state.cycle(AGE));
        }
    }
    
    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(AGE)*3;
    }
    
    protected void tryPlaceNewOre(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
    
    }
    
    protected boolean canGrow(BlockState state, ServerLevel level, BlockPos pos){
        return true;
    }
    
}
