package com.xkball.terrain_tectonic_trilithic.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface LevelPosPredicate extends BiPredicate<Level, BlockPos> {

}
