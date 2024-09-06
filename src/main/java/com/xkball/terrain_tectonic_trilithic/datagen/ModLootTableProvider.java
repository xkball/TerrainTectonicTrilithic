package com.xkball.terrain_tectonic_trilithic.datagen;

import com.xkball.terrain_tectonic_trilithic.datagen.block.ModBlockLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModLootTableProvider extends LootTableProvider {
    
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output,Set.of(),List.of(new SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)), registries);
    }
    
    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    
    }
}
