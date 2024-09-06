package com.xkball.terrain_tectonic_trilithic.datagen.item;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModItemTagProvider extends ItemTagsProvider {
    
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, TerrainTectonicTrilithic.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(var item : RegItem.REG_ITEM_POOL.values()){
            var tagList = item.getTagList();
            tagList.forEach( tag -> this.tag(tag).add(item.get()));
        }
    }
}
