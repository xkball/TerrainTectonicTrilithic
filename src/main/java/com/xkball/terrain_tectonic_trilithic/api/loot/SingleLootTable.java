package com.xkball.terrain_tectonic_trilithic.api.loot;

import net.minecraft.util.RandomSource;

public record SingleLootTable<T>(T t) implements SimpleLootTable<T> {
    
    @Override
    public T roll(RandomSource random) {
        return t;
    }
}
