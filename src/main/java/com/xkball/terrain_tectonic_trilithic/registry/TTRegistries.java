package com.xkball.terrain_tectonic_trilithic.registry;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import com.xkball.terrain_tectonic_trilithic.common.entity.ThrownPickaxeEntity;
import com.xkball.terrain_tectonic_trilithic.common.item.TTDataComponents;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.common.worldgen.feature.LakeBottomBlockFeature;
import com.xkball.terrain_tectonic_trilithic.recipe.TTRecipes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TTRegistries {
    
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(Registries.ITEM, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(Registries.BLOCK, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TerrainTectonicTrilithic.MODID);
    
    public static final DeferredRegister<Feature<?>> FEATURE = DeferredRegister.create(Registries.FEATURE, TerrainTectonicTrilithic.MODID);
    
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(Registries.ENTITY_TYPE, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, TerrainTectonicTrilithic.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TerrainTectonicTrilithic.MODID);
    
    //    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> BLOCK_TAB = CREATIVE_TAB.register("blocks",() -> CreativeModeTab.builder()
//            .title(Component.translatable("itemGroup.tin_tea_tech.blocs"))
//            .icon(Items.APPLE::getDefaultInstance)
//            .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS, CreativeModeTabs.INGREDIENTS, CreativeModeTabs.SPAWN_EGGS)
//            .build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC_TAB = CREATIVE_TAB.register("misc", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tin_tea_tech.misc"))
            .icon(TTItems.THE_PICKAXE.get()::getDefaultInstance)
            .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS, CreativeModeTabs.INGREDIENTS, CreativeModeTabs.SPAWN_EGGS)
            .build());
    
    public static final DeferredHolder<Feature<?>, LakeBottomBlockFeature> LAKE_BOTTOM_BLOCK_FEATURE = FEATURE.register("lake_bottom_block", () -> new LakeBottomBlockFeature(NoneFeatureConfiguration.CODEC));
    
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownPickaxeEntity>> THROWN_PICKAXE_ENTITY_TYPE = ENTITY_TYPE.register("thrown_pickaxe", () ->
            EntityType.Builder.<ThrownPickaxeEntity>of(ThrownPickaxeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(10)
                    .build("thrown_pickaxe"));
    
    public static void init(IEventBus bus) {
        TTDataComponents.init();
        TTRecipes.init();
        ITEM.register(bus);
        BLOCK.register(bus);
        CREATIVE_TAB.register(bus);
        BLOCK_ENTITY_TYPE.register(bus);
        DATA_COMPONENT.register(bus);
        FEATURE.register(bus);
        ENTITY_TYPE.register(bus);
        ENTITY_DATA_SERIALIZER.register(bus);
        INGREDIENT_TYPES.register(bus);
        RECIPE_SERIALIZER.register(bus);
    }
    
    @SubscribeEvent
    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        RegItem.CREATED_ORDER_LIST.stream()
                .filter(item -> item.getTab() != null && item.getTab().value() == event.getTab())
                .forEach(event::accept);
    }
}
