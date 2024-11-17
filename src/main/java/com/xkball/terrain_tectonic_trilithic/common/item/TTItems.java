package com.xkball.terrain_tectonic_trilithic.common.item;

import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import com.xkball.terrain_tectonic_trilithic.common.item.throwable.ThrowablePickaxe;
import com.xkball.terrain_tectonic_trilithic.common.item.throwable.ThrowableSickle;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
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
    
    public static final RegItem<ThrowablePickaxe> THROWABLE_PICKAXE = new RegItem<>("throwable_pickaxe", ThrowablePickaxe::new)
            .setI18n("Throwable Pickaxe", "可投掷镐子")
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<ThrowableSickle> THROWABLE_SICKLE = new RegItem<>("throwable_sickle", ThrowableSickle::new)
            .setI18n("Throwalbe Sickle", "可投掷镰刀")
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<FakeFoodItem> FAKE_FOOD = new RegItem<>("fake_food",withDefaultProperties(FakeFoodItem::new))
            .setI18n("Mimicry Food","拟态食物");
    
    public static final RegItem<PlateShieldItem> BLAZE_SHIELD = new RegItem<>("blaze_shield",() -> new PlateShieldItem(DamageTypeTags.IS_FIRE))
            .setI18n("Blaze Shield","烈焰屏障")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<PlateShieldItem> CHLOROPHYLL_SHIELD = new RegItem<>("chlorophyll_shield",() -> new PlateShieldItem(DamageTypeTags.IS_EXPLOSION))
            .setI18n("Blaze Shield","叶绿屏障")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    public static final RegItem<PlateShieldItem> GLACIED_SHIELD = new RegItem<>("glacied_shield",() -> new PlateShieldItem(DamageTypeTags.IS_FREEZING))
            .setI18n("Glacied Shield","坚冰屏障")
            .setDefaultSimpleModel()
            .setCreativeTab(TTRegistries.MISC_TAB);
    
    
    
    public static <T extends Item> Supplier<T> withDefaultProperties(Function<Item.Properties, T> itemFunction) {
        return () -> itemFunction.apply(new Item.Properties());
    }
    
    //仅用于触发类加载
    public static void init() {
    }
}
