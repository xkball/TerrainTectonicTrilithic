package com.xkball.terrain_tectonic_trilithic.registry;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TTRegistries {
    
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(Registries.ITEM, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(Registries.BLOCK, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TerrainTectonicTrilithic.MODID);
    
    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> BLOCK_TAB = CREATIVE_TAB.register("blocks",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tin_tea_tech.blocs"))
            .icon(Items.APPLE::getDefaultInstance)
            .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS, CreativeModeTabs.INGREDIENTS, CreativeModeTabs.SPAWN_EGGS)
            .build());
    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> MISC_TAB = CREATIVE_TAB.register("misc",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tin_tea_tech.misc"))
            .icon(Items.APPLE::getDefaultInstance)
            .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS, CreativeModeTabs.INGREDIENTS, CreativeModeTabs.SPAWN_EGGS)
            .build());
    
    public static void init(IEventBus bus) {
        ITEM.register(bus);
        BLOCK.register(bus);
        CREATIVE_TAB.register(bus);
        BLOCK_ENTITY_TYPE.register(bus);
        DATA_COMPONENT.register(bus);
    }
    
    @SubscribeEvent
    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        RegItem.REG_ITEM_POOL.values()
                .stream()
                .filter(item -> item.getTab() != null && item.getTab().value() == event.getTab())
                .forEach(event::accept);
    }
}
