package com.xkball.terrain_tectonic_trilithic.utils.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.function.Predicate;

public record PredicatePredicate<T>(Predicate<T> predicate) implements PredicateWithCodec<T>{
    
    @Override
    public boolean accept(T t) {
        return predicate.test(t);
    }
    
    @Override
    public PredicateWithCodecType getType() {
        return PredicateWithCodecType.PREDICATE;
    }
    
    public static <T> MapCodec<PredicatePredicate<T>> createCodec(Codec<Predicate<T>> predicateCodec) {
        return predicateCodec.xmap(PredicatePredicate::new,PredicatePredicate::predicate).fieldOf("value");
    }
}
