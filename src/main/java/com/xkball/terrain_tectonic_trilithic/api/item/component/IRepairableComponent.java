package com.xkball.terrain_tectonic_trilithic.api.item.component;

import com.mojang.datafixers.util.Pair;

public interface IRepairableComponent<T extends IRepairableComponent<T>>{
    
    boolean canReceiveXP();
    
    Pair<T,Integer> receiveXP(int value);
}
