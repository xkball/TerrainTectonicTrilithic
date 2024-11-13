package com.xkball.terrain_tectonic_trilithic.common.item;

import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class TTItems {
    
    public static final RegItem<IconItem> THE_ICON = new RegItem<>("icon", withDefaultProperties(IconItem::new))
            .setI18n("icon", "测试物品")
            .setCreativeTab(TTRegistries.MISC_TAB);
    //.setModelLocation(VanillaUtils.modRL("icon"));
    
    public static final RegItem<Item> CHLOROPHYLL_SHARD = new RegItem<>("chlorophyll_shard", withDefaultProperties(Item::new))
            .setI18n("Chlorophyll Shard", "叶绿碎片")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<Item> GLACIED_SHARD = new RegItem<>("glacied_shard", withDefaultProperties(Item::new))
            .setI18n("Glacied Shard", "坚冰碎片")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<Item> BLAZE_SHARD = new RegItem<>("blaze_shard", () -> new Item(new Item.Properties().fireResistant()))
            .setI18n("Blaze Shard", "炽焱碎片")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<Item> BLAZE_PICKAXE_HEAD = new RegItem<>("blaze_pickaxe_head", () -> new Item(new Item.Properties().fireResistant().stacksTo(1)))
            .setI18n("Blaze Pickaxe Head", "炽焱镐头")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<Item> GLACIED_PICKAXE_HEAD = new RegItem<>("glcaied_pickaxe_head", () -> new Item(new Item.Properties().fireResistant().stacksTo(1)))
            .setI18n("Glcaied Pickaxe Head", "坚冰镐头")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<ThePickaxe> THE_PICKAXE = new RegItem<>("the_pickaxe", ThePickaxe::new)
            .setI18n("The Pickaxe", "镐子")
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<FakeFoodItem> FAKE_FOOD = new RegItem<>("fake_food",withDefaultProperties(FakeFoodItem::new))
            .setI18n("Mimicry Food","拟态食物");
    
    public static <T extends Item> Supplier<T> withDefaultProperties(Function<Item.Properties, T> itemFunction) {
        return () -> itemFunction.apply(new Item.Properties());
    }
    
    //仅用于触发类加载
    public static void init() {
    }
}
