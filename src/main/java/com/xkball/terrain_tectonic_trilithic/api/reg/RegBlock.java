package com.xkball.terrain_tectonic_trilithic.api.reg;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RegBlock<T extends Block> implements ItemLike {
    
    public static final Map<ResourceLocation, RegBlock<?>> REG_BLOCK_POOL = new Object2ReferenceOpenHashMap<>();
    public static final BiConsumer<BlockStateProvider,RegBlock<?>> SIMPLE_CUBE_ALL = (p,self) -> p.simpleBlock(self.get(),p.cubeAll(self.get()));
    private final AutoRegHolder<Block,T> holder;
    private final String name;
    private I18NEntry i18n;
    private RegItem<? extends BlockItem> blockItemHolder = null;
    private Class<? extends BlockEntity> blockEntityClass = null;
    private Class<? extends BlockEntityRenderer<?>> renderClass = null;
    private BiConsumer<BlockStateProvider,RegBlock<?>> blockStateProviderConsumer = null;
    private ResourceLocation itemModelLocation = null;
    private final List<TagKey<Block>> tagList = new ArrayList<>();
    
    public RegBlock(String name, Supplier<T> supplier) {
        this.name = name;
        this.holder = AutoRegHolder.<Block,T>create(supplier).bind(TTRegistries.BLOCK,name);
        REG_BLOCK_POOL.put(VanillaUtils.modRL(name),this);
    }
    
    public RegBlock<T> setBlockItem(Function<T,? extends BlockItem> function) {
        blockItemHolder = new RegItem<>(this.name,() -> function.apply(holder.get()));
        return this;
    }
    
    public RegBlock<T> setSimpleBlockItem(){
        blockItemHolder = new RegItem<>(this.name, () -> new BlockItem(holder.get(), new Item.Properties()));
        itemModelLocation = VanillaUtils.modRL(name);
        return this;
    }
    
    @SafeVarargs
    public final RegBlock<T> setTags(TagKey<Block>... tags){
        tagList.clear();
        tagList.addAll(List.of(tags));
        return this;
    }
    
    @SafeVarargs
    public final RegBlock<T> setItemTags(TagKey<Item>... tags){
        blockItemHolder.setTags(tags);
        return this;
    }
    
    public RegBlock<T> setI18n(String en_us,String zh_cn) {
        this.i18n = new I18NEntry("block."+ TerrainTectonicTrilithic.MODID+"."+name,en_us,zh_cn);
        return this;
    }
    
    public RegBlock<T> setDataGenBlockModel(BiConsumer<BlockStateProvider,RegBlock<?>> consumer){
        this.blockStateProviderConsumer = consumer;
        return this;
    }
    
    public RegBlock<T> setDefaultItemModelParent(){
        this.itemModelLocation = VanillaUtils.modRL(name);
        return this;
    }
    
    public RegBlock<T> setItemModelParent(ResourceLocation location){
        this.itemModelLocation = location;
        return this;
    }
    
    public RegBlock<T> setItemModelLocation(ResourceLocation location){
        this.blockItemHolder.setModelLocation(location);
        return this;
    }
    
    public RegBlock<T> setCreativeTab(Holder<CreativeModeTab> tab){
        this.blockItemHolder.setCreativeTab(tab);
        return this;
    }
    
    @Nullable
    public BiConsumer<BlockStateProvider, RegBlock<?>> getBlockStateProviderConsumer() {
        return blockStateProviderConsumer;
    }
    
    @Nullable
    public ResourceLocation getItemModelLocation() {
        return itemModelLocation;
    }
    
    @Nullable
    public RegItem<? extends BlockItem> getBlockItemHolder() {
        return blockItemHolder;
    }
    
    @Nullable
    public I18NEntry getI18n() {
        return i18n;
    }
    
    public List<TagKey<Block>> getTagList() {
        return tagList;
    }
    
    public T get(){
        return holder.get();
    }
    
    @Override
    public Item asItem() {
        assert blockItemHolder != null;
        return blockItemHolder.asItem();
    }
}
