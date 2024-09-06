package com.xkball.terrain_tectonic_trilithic.datagen.block;

import com.google.common.collect.Iterables;
import com.xkball.terrain_tectonic_trilithic.common.block.TTBlocks;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

@MethodsReturnNonnullByDefault
public class ModBlockLootProvider extends BlockLootSubProvider {
    
    public ModBlockLootProvider( HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }
    
    @Override
    protected void generate() {
        this.add(TTBlocks.BLAZE_ORE.get(),this.createOreDrop(TTBlocks.BLAZE_ORE.get(), TTItems.BLAZE_SHARD.get()));
        this.add(TTBlocks.GLACIED_ORE.get(),this.createOreDrop(TTBlocks.GLACIED_ORE.get(), TTItems.GLACIED_SHARD.get()));
        this.add(TTBlocks.PHYLLORE.get(),this.createOreDrop(TTBlocks.PHYLLORE.get(),TTItems.CHLOROPHYLL_SHARD.get()));
    }
    
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Iterables.transform(TTRegistries.BLOCK.getEntries(), DeferredHolder::get);
    }
}
