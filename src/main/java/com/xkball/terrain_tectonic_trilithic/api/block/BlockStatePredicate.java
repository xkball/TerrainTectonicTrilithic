package com.xkball.terrain_tectonic_trilithic.api.block;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public record BlockStatePredicate(BlockPredicate predicate) implements Predicate<BlockState> {
   public static final Codec<BlockStatePredicate> CODEC = BlockPredicate.CODEC.xmap(BlockStatePredicate::new, BlockStatePredicate::predicate);
    @Override
    public boolean test(BlockState blockState) {
        return predicate.matchesState(blockState);
    }
}
