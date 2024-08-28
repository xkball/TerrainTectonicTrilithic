package com.xkball.terrain_tectonic_trilithic.api.reg;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public class RegItem<T extends Item> implements ItemLike {
    
    public static final Map<ResourceLocation,RegItem<?>> REG_ITEM_POOL = new Object2ReferenceOpenHashMap<>();
    
    private final AutoRegHolder<Item, T> holder;
    private final String name;
    private Holder<CreativeModeTab> tab;
    private I18NEntry i18n;
    private ResourceLocation modelLocation;
    
    public RegItem(String name, Supplier<T> supplier) {
        this.name = name;
        this.holder = AutoRegHolder.<Item,T>create(supplier).bind(TTRegistries.ITEM,name);
        REG_ITEM_POOL.put(VanillaUtils.modRL(name),this);
    }
    
    public RegItem<T> setCreativeTab(Holder<CreativeModeTab> tab) {
        this.tab = tab;
        return this;
    }
    
    public RegItem<T> setI18n(String en_us,String zh_cn) {
        this.i18n = new I18NEntry("item."+ TerrainTectonicTrilithic.MODID+"."+name,en_us,zh_cn);
        return this;
    }
    
    public RegItem<T> setModelLocation(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        return this;
    }
    
    public AutoRegHolder<Item, T> getHolder() {
        return holder;
    }
    
    public T get(){
        return holder.get();
    }
    
    @Nullable
    public Holder<CreativeModeTab> getTab() {
        return tab;
    }
    
    @Nullable
    public I18NEntry getI18n() {
        return i18n;
    }
    
    @Nullable
    public ResourceLocation getModelLocation() {
        return modelLocation;
    }
    
    @Override
    public Item asItem() {
        return holder.get();
    }
}
