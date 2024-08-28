package com.xkball.terrain_tectonic_trilithic.registry;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforgespi.language.ModFileScanData;

//过去用于自动注册 现在改为处理注解
public enum AutoRegManager {
    INSTANCE;
    
    public static void init(ModContainer container) {
        if(container instanceof FMLModContainer fmlModContainer) {
            try {
                var scanResultField = FMLModContainer.class.getDeclaredField("scanResults");
                scanResultField.setAccessible(true);
                AutoRegManager.INSTANCE._init(container.getModId(),(ModFileScanData) scanResultField.get(fmlModContainer));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private void _init(String modid,ModFileScanData modFileScanData){
        //todo 对应component的I18N注解处理(仅DataGen时)
    }
}
