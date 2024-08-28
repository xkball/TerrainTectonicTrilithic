package com.xkball.terrain_tectonic_trilithic.common.block;

import com.xkball.terrain_tectonic_trilithic.api.reg.RegBlock;
import com.xkball.terrain_tectonic_trilithic.common.item.blockitem.ScaffoldingBlockItem;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;

public class TTBlocks {
    
    public static final RegBlock<ScaffoldingBlock> SCAFFOLDING_BLOCK = new RegBlock<>(ScaffoldingBlock.NAME,ScaffoldingBlock::new)
            .setBlockItem(ScaffoldingBlockItem::new)
            .setI18n("Scaffolding Block","脚手架")
            .setCreativeTab(TTRegistries.BLOCK_TAB);
    
    
    //仅用于触发类加载
    public static void init(){}
}
