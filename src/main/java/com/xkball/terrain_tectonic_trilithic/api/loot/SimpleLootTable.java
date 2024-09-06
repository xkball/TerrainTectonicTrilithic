package com.xkball.terrain_tectonic_trilithic.api.loot;

import net.minecraft.util.RandomSource;

public interface SimpleLootTable<T> {
    T roll(RandomSource random);
}
