package com.xkball.terrain_tectonic_trilithic.utils;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.xkball.terrain_tectonic_trilithic.api.block.BlockStatePredicate;
import com.xkball.terrain_tectonic_trilithic.utils.predicate.PredicateWithCodec;
import net.minecraft.world.level.block.state.BlockState;

public class CodecUtils {
    
    public static final Codec<PredicateWithCodec<BlockState>> BLOCK_STATE_PREDICATE_CODEC = PredicateWithCodec.codec(BlockState.CODEC, BlockStatePredicate.CODEC);
    
    public static <T> Codec<T> errorCodec(String reason){
        return new Codec<>() {
            @Override
            public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
                return DataResult.error(() -> reason);
            }
            
            @Override
            public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
                return DataResult.error(() -> reason);
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public static <T> MapCodec<T> errorMapCodec(String reason){
        return (MapCodec<T>) errorCodec(reason).fieldOf("error");
    }
}
