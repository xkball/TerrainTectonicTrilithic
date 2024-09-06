package com.xkball.terrain_tectonic_trilithic.utils.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import javax.annotation.Nonnull;


public class SingleEqualPredicate<T> implements PredicateWithCodec<T> {

    private final T value;
    
    public SingleEqualPredicate(@Nonnull T value) {
        this.value = value;
    }
    
    @Override
    public boolean accept(T t) {
        return value.equals(t);
    }
    
    @Override
    public PredicateWithCodecType getType() {
        return PredicateWithCodecType.SINGLE_EQUAL;
    }
    
    protected T getValue() {
        return value;
    }
    
    public static <T> MapCodec<SingleEqualPredicate<T>> createCodec(Codec<T> codec) {
        return codec.xmap(SingleEqualPredicate::new, SingleEqualPredicate::getValue).fieldOf("value");
    }
}
