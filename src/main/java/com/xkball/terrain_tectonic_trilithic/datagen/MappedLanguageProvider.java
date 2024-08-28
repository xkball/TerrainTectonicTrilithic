package com.xkball.terrain_tectonic_trilithic.datagen;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;

public class MappedLanguageProvider extends LanguageProvider {
    
    private final Map<String,String> map;
    public MappedLanguageProvider(PackOutput output, Map<String,String> map, String locale) {
        super(output, TerrainTectonicTrilithic.MODID, locale);
        this.map = map;
    }
    
    @Override
    protected void addTranslations() {
        for(var entry : map.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }
}
