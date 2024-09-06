package com.xkball.terrain_tectonic_trilithic.common.block;

import com.xkball.terrain_tectonic_trilithic.api.reg.RegBlock;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;

public class TTBlocks {
    
//    public static final RegBlock<ScaffoldingBlock> SCAFFOLDING_BLOCK = new RegBlock<>(ScaffoldingBlock.NAME,ScaffoldingBlock::new)
//            .setBlockItem(ScaffoldingBlockItem::new)
//            .setI18n("Scaffolding Block","脚手架")
//            .setCreativeTab(TTRegistries.BLOCK_TAB);
    
    public static final RegBlock<StateRequireRenewableOre> PHYLLORE= new RegBlock<>("phyllore",() -> new StateRequireRenewableOre(BlockBehaviour.Properties.of().strength(3.0f,3.0f).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_GREEN),
            (level,pos) -> level.getBlockState(pos).is(Blocks.MOSS_BLOCK),
            (level,pos) -> level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,pos.getX(),pos.getZ()) <= pos.getY()+1
            ))
        .setSimpleBlockItem()
        .setI18n("Phyllore","叶绿矿")
        .setTags(BlockTags.MINEABLE_WITH_PICKAXE,BlockTags.NEEDS_IRON_TOOL)
        .setCreativeTab(TTRegistries.MISC_TAB)
        .setDataGenBlockModel(RegBlock.SIMPLE_CUBE_ALL);
        
    public static final RegBlock<StateRequireRenewableOre> GLACIED_ORE = new RegBlock<>("glacied_ore",() -> new StateRequireRenewableOre(BlockBehaviour.Properties.of().strength(3.0f,3.0f).requiresCorrectToolForDrops().mapColor(MapColor.ICE),
            (level,pos) -> level.getBlockState(pos).is(BlockTags.ICE),
            (level,pos) -> {
                var mutPos = pos.mutable();
                var hasIce = false;
                var hasSnow = false;
                for(var dir : Direction.values()){
                    mutPos.setWithOffset(pos,dir);
                    if(level.getBlockState(mutPos).is(Blocks.BLUE_ICE)) {
                        hasIce = true;
                    }
                    if(level.getBlockState(mutPos).is(BlockTags.SNOW)) {
                        hasSnow = true;
                    }
                    if(hasIce && hasSnow) return true;
                }
                return false;
            }))
            .setSimpleBlockItem()
            .setI18n("Glacied Ore","坚冰矿")
            .setTags(BlockTags.MINEABLE_WITH_PICKAXE,BlockTags.NEEDS_IRON_TOOL)
            .setCreativeTab(TTRegistries.MISC_TAB)
            .setDataGenBlockModel(RegBlock.SIMPLE_CUBE_ALL);
    
    public static final RegBlock<StateRequireRenewableOre> BLAZE_ORE = new RegBlock<>("blaze_ore",() -> new StateRequireRenewableOre(BlockBehaviour.Properties.of().strength(3.0f,3.0f).requiresCorrectToolForDrops().mapColor(MapColor.ICE),
            (level,pos) -> level.getBlockState(pos).is(Blocks.NETHERRACK),
            (level,pos) -> {
                var mutPos = pos.mutable();
                for(var dir : Direction.values()){
                    mutPos.setWithOffset(pos,dir);
                    if(level.getFluidState(mutPos).is(FluidTags.LAVA)) {
                        return true;
                    }
                }
                return false;
            }
    ))
            .setBlockItem((b) -> new BlockItem(b,new Item.Properties().fireResistant()))
            .setDefaultItemModelParent()
            .setI18n("Blaze Ore","炽焱矿")
            .setTags(BlockTags.MINEABLE_WITH_PICKAXE,BlockTags.NEEDS_IRON_TOOL)
            .setCreativeTab(TTRegistries.MISC_TAB)
            .setDataGenBlockModel(RegBlock.SIMPLE_CUBE_ALL);
    
    //仅用于触发类加载
    public static void init(){}
}
