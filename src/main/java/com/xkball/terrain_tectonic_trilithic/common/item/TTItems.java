package com.xkball.terrain_tectonic_trilithic.common.item;

import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import net.minecraft.world.item.Item;

public class TTItems {
    
    public static final RegItem<IconItem> THE_ICON = new RegItem<>("icon",() -> new IconItem(new Item.Properties()))
            .setI18n("icon","测试物品")
            .setCreativeTab(TTRegistries.MISC_TAB)
            .setModelLocation(VanillaUtils.modRL("icon"));
    
    
    //仅用于触发类加载
    public static void init(){}
}
