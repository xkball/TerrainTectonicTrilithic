package com.xkball.terrain_tectonic_trilithic.datagen;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.reg.RegBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockModelProvider extends BlockStateProvider {
    
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerrainTectonicTrilithic.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerStatesAndModels() {
        for(var block : RegBlock.REG_BLOCK_POOL.values()){
            var item = block.getBlockItemHolder();
            if(item == null) continue;
            if(block.getItemModelLocation() != null){
                this.simpleBlockItem(block.get(),models().getExistingFile(block.getItemModelLocation()));
            }
            else if(item.getModelLocation() != null){
                this.itemModels().basicItem(item.getModelLocation());
            }
        }
    }
}
