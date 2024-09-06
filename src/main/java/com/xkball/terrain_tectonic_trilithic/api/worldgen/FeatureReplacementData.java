package com.xkball.terrain_tectonic_trilithic.api.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xkball.terrain_tectonic_trilithic.api.loot.SimpleLootTable;
import com.xkball.terrain_tectonic_trilithic.api.loot.SingleLootTable;
import com.xkball.terrain_tectonic_trilithic.common.datapack.TTDataPacks;
import com.xkball.terrain_tectonic_trilithic.utils.CodecUtils;
import com.xkball.terrain_tectonic_trilithic.api.loot.WightLootTable;
import com.xkball.terrain_tectonic_trilithic.utils.predicate.PredicateWithCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FeatureReplacementData {
    
    public static final Codec<FeatureReplacementData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.STRING.fieldOf("featureName").forGetter(o -> o.featureName),
            SingleReplacementData.CODEC.listOf().fieldOf("replacementData").forGetter(o -> o.replacementData)
    ).apply(ins, FeatureReplacementData::new));
    
    public final String featureName;
    public final List<SingleReplacementData> replacementData;
    private final Map<BlockState, SimpleLootTable<BlockState>> cache = new Reference2ObjectArrayMap<>();

    public FeatureReplacementData(String featureName, List<SingleReplacementData> replacementData) {
        this.featureName = featureName;
        this.replacementData = replacementData;
    }
    
    public BlockState getReplacementBlockState(BlockState origin, RandomSource random) {
        return cache.computeIfAbsent(origin,state -> {
            var alternatives = this.replacementData
                    .stream()
                    .filter(d -> d.predicate().accept(state))
                    .flatMap(d -> d.getAllStatesAndWeight(state).stream())
                    .toList();
            if (alternatives.isEmpty()) {
                return new SingleLootTable<>(origin);
            }
            return FeatureReplacementData.SingleReplacementData.createLootList(alternatives);
        }).roll(random);
    }
    
    public static void runFeatureReplaceHook(RegistryAccess registryAccess){
        var logger = LogUtils.getLogger();
        var features = registryAccess.registry(Registries.FEATURE).orElseThrow();
        var replacements = registryAccess.registry(TTDataPacks.FEATURE_REPLACEMENT).orElseThrow();
        
        features.forEach(feature -> ((TTFeatureExtension)feature).terrainTectonicTrilithic$hook(null));
        for(var replacement : replacements){
            var feature = (TTFeatureExtension) features.get(ResourceLocation.parse(replacement.featureName));
            if(feature == null) {
                logger.warn("FeatureReplacementData: missing feature {}", replacement.featureName);
                continue;
            }
            feature.terrainTectonicTrilithic$hook(replacement.combine(feature.terrainTectonicTrilithic$getFeatureReplacementData()));
        }
    }
    
    public FeatureReplacementData combine(@Nullable FeatureReplacementData other) {
        if(other == null) return this;
        assert featureName.equals(other.featureName);
        return new FeatureReplacementData(featureName, Stream.of(replacementData,other.replacementData).flatMap(List::stream).toList());
    }
    
    public record SingleReplacementData(PredicateWithCodec<BlockState> predicate, List<Pair<BlockState,Integer>> statesAndWeight, int originBlockWeight){
        
        public static final Codec<SingleReplacementData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                CodecUtils.BLOCK_STATE_PREDICATE_CODEC.fieldOf("predicate").forGetter(o -> o.predicate),
                Codec.pair(
                        BlockState.CODEC.fieldOf("block_state").codec(),
                        Codec.INT.fieldOf("weight").codec()
                ).listOf().fieldOf("stateAndWeights").forGetter(o -> o.statesAndWeight),
                Codec.INT.fieldOf("originBlockWeight").forGetter(o -> o.originBlockWeight)
        ).apply(ins, SingleReplacementData::new));
        
        public List<Pair<BlockState,Integer>> getAllStatesAndWeight(BlockState defaultBlockState) {
            var result = new ArrayList<>(statesAndWeight);
            result.add(new Pair<>(defaultBlockState, originBlockWeight));
            return result;
        }
        
        public static WightLootTable<BlockState> createLootList(List<Pair<BlockState,Integer>> statesAndWeight){
            var dataMap = statesAndWeight.stream().collect(Pair.toMap());
            return new WightLootTable<>(
                    statesAndWeight.stream().map(Pair::getFirst).toList(),
                    dataMap::get);
        }
    }
}
