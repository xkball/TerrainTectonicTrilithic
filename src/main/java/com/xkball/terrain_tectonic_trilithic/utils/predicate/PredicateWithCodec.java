package com.xkball.terrain_tectonic_trilithic.utils.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xkball.terrain_tectonic_trilithic.utils.CodecUtils;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface PredicateWithCodec<T> extends Predicate<T> {
    
    boolean accept(T t);
    
    PredicateWithCodecType getType();
    
    @Override
    default boolean test(T t){
        return accept(t);
    }
    
    //参数不能同时为null
    static <T> Codec<PredicateWithCodec<T>> codec(@Nullable Codec<T> codec,@Nullable Codec<? extends Predicate<T>> predicateCodec) {
        return Codec.recursive(
                PredicateWithCodec.class.getSimpleName(),
                recursedCodec -> PredicateWithCodecType.CODEC.dispatch(PredicateWithCodec::getType, type -> type.createCodec(recursedCodec,codec,predicateCodec))
        );
    }
    
    enum PredicateWithCodecType implements StringRepresentable {
        SINGLE_EQUAL((r,c,p) -> c == null ? CodecUtils.errorMapCodec("require codec of T"):SingleEqualPredicate.createCodec(c)),
        LIST((r,c,p) -> ListPredicate.createCodec(r)),
        BINARY_OP((r,c,p) -> BinaryOpPredicate.createCodec(r)),
        PREDICATE((r,c,p) -> p == null ? CodecUtils.errorMapCodec("require predicate codec of T"):PredicatePredicate.createCodec(p));
        
        public static final Codec<PredicateWithCodecType> CODEC = StringRepresentable.fromEnum(PredicateWithCodecType::values);
        
        private final TypeCodec<?> codecCreator;
        
        PredicateWithCodecType(TypeCodec<?> codecCreator) {
            this.codecCreator = codecCreator;
        }
        
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <T> MapCodec<? extends PredicateWithCodec<T>> createCodec(Codec<PredicateWithCodec<T>> recursedCodec,@Nullable Codec<T> codec,@Nullable Codec<? extends Predicate<T>> predicateCodec) {
            if(codec == null && predicateCodec == null) return CodecUtils.errorMapCodec("require at least a codec of T or a predicate codec of T");
            return (MapCodec<? extends PredicateWithCodec<T>>) ((TypeCodec)codecCreator).apply(recursedCodec,codec,predicateCodec);
        }
        
        @Override
        @NotNull
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
    
    @FunctionalInterface
    interface TypeCodec<T> extends TriFunction<Codec<PredicateWithCodec<T>>,@Nullable Codec<T>,@Nullable Codec<Predicate<T>>, MapCodec<? extends PredicateWithCodec<T>>> {
    
    }
}
