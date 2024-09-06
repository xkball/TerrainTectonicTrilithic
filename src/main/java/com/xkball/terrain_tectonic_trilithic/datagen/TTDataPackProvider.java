package com.xkball.terrain_tectonic_trilithic.datagen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class TTDataPackProvider<T> implements DataProvider {
    
    private final PackOutput output;
    private final String modid;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final Codec<T> codec;
    private final List<Pair<String,T>> tList = new ArrayList<>();
    
    public TTDataPackProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> registries, Codec<T> codec) {
        this.output = output;
        this.modid = modid;
        this.registries = registries;
        this.codec = codec;
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        this.tList.clear();
        var path = this.resolveDirectory(this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(modid).resolve(TerrainTectonicTrilithic.MODID));
        return registries
                .thenCompose(registries_ -> {
                    this.buildObjectList();
                    return CompletableFuture.completedFuture(registries_);
                })
                .thenCompose(
                        registries_ -> CompletableFuture.allOf(this.tList
                        .stream()
                        .map(f -> writeObject(output,registries_,f.getSecond(),path.resolve(f.getFirst()+".json")))
                        .toArray(CompletableFuture[]::new)));
    }
    
    protected abstract void buildObjectList();
    
    protected void addObject(String fileName,T t){
        tList.add(Pair.of(fileName,t));
    }
    
    private CompletableFuture<?> writeObject(CachedOutput cachedOutput, HolderLookup.Provider registries_, T data, Path path) {
        return DataProvider.saveStable(cachedOutput,registries_,codec,data,path);
    }
    
    protected abstract Path resolveDirectory(Path root);
    
}
