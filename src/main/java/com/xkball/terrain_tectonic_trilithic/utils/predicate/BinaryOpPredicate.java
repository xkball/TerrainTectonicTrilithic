package com.xkball.terrain_tectonic_trilithic.utils.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xkball.terrain_tectonic_trilithic.utils.BooleanOps;

public record BinaryOpPredicate<T>(PredicateWithCodec<T> first, PredicateWithCodec<T> second, BooleanOps booleanOp) implements PredicateWithCodec<T>{
    
    @Override
    public boolean accept(T t) {
        return booleanOp.apply(first.accept(t),second.accept(t) );
    }
    
    @Override
    public PredicateWithCodecType getType() {
        return PredicateWithCodecType.BINARY_OP;
    }
    
    public static <T> MapCodec<BinaryOpPredicate<T>> createCodec(Codec<PredicateWithCodec<T>> codec) {
        return RecordCodecBuilder.mapCodec(ins -> ins.group(
                codec.fieldOf("first").forGetter(o -> o.first),
                codec.fieldOf("second").forGetter(o -> o.second),
                BooleanOps.CODEC.fieldOf("boolean_op").forGetter(o -> o.booleanOp)
        ).apply(ins, BinaryOpPredicate::new));
    }
    
}
