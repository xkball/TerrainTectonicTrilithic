package com.xkball.terrain_tectonic_trilithic.datagen;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.reg.RegBlock;
import com.xkball.terrain_tectonic_trilithic.api.reg.RegItem;
import com.xkball.terrain_tectonic_trilithic.datagen.block.ModBlockModelProvider;
import com.xkball.terrain_tectonic_trilithic.datagen.block.ModBlockTagProvider;
import com.xkball.terrain_tectonic_trilithic.datagen.item.ModItemModelProvider;
import com.xkball.terrain_tectonic_trilithic.datagen.item.ModItemTagProvider;
import com.xkball.terrain_tectonic_trilithic.datagen.worldgen.ConfiguredFeatureProvider;
import com.xkball.terrain_tectonic_trilithic.datagen.worldgen.FeatureReplacementProvider;
import com.xkball.terrain_tectonic_trilithic.datagen.worldgen.PlacedFeatureProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TTDataGen {
    
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        var existingFileHelper = event.getExistingFileHelper();
        var dataGenerator = event.getGenerator();
        var runClient = event.includeClient();
        var runServer = event.includeServer();
        var packOutput = dataGenerator.getPackOutput();
        var registries = event.getLookupProvider();
        
        dataGenerator.addProvider(runClient,LangUtils.getEN_US(packOutput));
        dataGenerator.addProvider(runClient,LangUtils.getZH_CN(packOutput));
        dataGenerator.addProvider(runClient,new ModBlockModelProvider(packOutput,existingFileHelper));
        dataGenerator.addProvider(runClient,new ModItemModelProvider(packOutput,existingFileHelper));
        dataGenerator.addProvider(runClient,new ModLootTableProvider(packOutput,registries));
        var blockTagProvider = dataGenerator.addProvider(runClient,new ModBlockTagProvider(packOutput,registries,existingFileHelper));
        dataGenerator.addProvider(runClient,new ModItemTagProvider(packOutput,registries,blockTagProvider.contentsGetter(),existingFileHelper));
        dataGenerator.addProvider(runServer,new FeatureReplacementProvider(packOutput,TerrainTectonicTrilithic.MODID,registries));
        dataGenerator.addProvider(runServer,new ConfiguredFeatureProvider(packOutput,TerrainTectonicTrilithic.MODID,registries));
        dataGenerator.addProvider(runServer,new PlacedFeatureProvider(packOutput,TerrainTectonicTrilithic.MODID,registries));
    }
    
    public static class LangUtils{
        public static final Map<String,String> EN_US = new HashMap<>();
        public static final Map<String,String> ZH_CN = new HashMap<>();
        private static boolean init = false;
        
        private static void init(){
            if(init) return;
            init = true;
            addLangKey("itemGroup.tin_tea_tech.blocs","TinTeaTech: Blocks","锡茶科技: 方块");
            addLangKey("itemGroup.tin_tea_tech.misc","TinTeaTech: Misc","锡茶科技: 杂项");
            for(var item : RegItem.REG_ITEM_POOL.values()){
                if(item.getI18n() == null) continue;
                var i18n = item.getI18n();
                addLangKey(i18n.key(),i18n.en_us(),i18n.zh_cn());
            }
            for(var block : RegBlock.REG_BLOCK_POOL.values()){
                if(block.getI18n() == null) continue;
                var i18n = block.getI18n();
                addLangKey(i18n.key(),i18n.en_us(),i18n.zh_cn());
            }
        }
        
        public static void addLangKey(String key, String en_us, String zh_cn){
            EN_US.put(key, en_us);
            ZH_CN.put(key, zh_cn);
        }
        
        public static MappedLanguageProvider getEN_US(PackOutput packOutput){
            init();
            return new MappedLanguageProvider(packOutput,EN_US,"en_us");
        }
        
        public static MappedLanguageProvider getZH_CN(PackOutput packOutput){
            init();
            return new MappedLanguageProvider(packOutput,ZH_CN,"zh_cn");
        }
    }
}
