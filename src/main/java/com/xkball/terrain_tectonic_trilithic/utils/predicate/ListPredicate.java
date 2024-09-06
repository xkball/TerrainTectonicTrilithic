package com.xkball.terrain_tectonic_trilithic.utils.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record ListPredicate<T>(List<PredicateWithCodec<T>> predicates,ListOps listOps) implements PredicateWithCodec<T> {
    
    @SafeVarargs
    public ListPredicate(ListOps listOps, PredicateWithCodec<T>... predicates) {
        this(List.of(predicates),listOps);
    }
    
    @Override
    public boolean accept(T t) {
        return listOps.accept(predicates.stream().map(p -> p.accept(t)));
    }
    
    @Override
    public PredicateWithCodecType getType() {
        return PredicateWithCodecType.LIST;
    }
    
    public static <T> MapCodec<ListPredicate<T>> createCodec(Codec<PredicateWithCodec<T>> codec){
        return RecordCodecBuilder.mapCodec(ins -> ins.group(
                codec.listOf().fieldOf("value").forGetter(ListPredicate::predicates),
                ListOps.CODEC.fieldOf("list_op").forGetter(ListPredicate::listOps)
        ).apply(ins, ListPredicate::new));
    }
    
    public enum ListOps implements StringRepresentable {
        ALL_MATCH(s -> s.allMatch(b -> b)),
        ANY_MATCH(s -> s.anyMatch(b -> b)),
        NONE_MATCH(s -> s.noneMatch(b -> b)),;
        
        private final Predicate<Stream<Boolean>> innerPredicate;
        
        ListOps(Predicate<Stream<Boolean>> innerPredicate) {
            this.innerPredicate = innerPredicate;
        }
        
        public boolean accept(Stream<Boolean> stream) {
            return innerPredicate.test(stream);
        }
        
        public static final Codec<ListOps> CODEC = StringRepresentable.fromEnum(ListOps::values);
        
        @Override
        @NotNull
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
