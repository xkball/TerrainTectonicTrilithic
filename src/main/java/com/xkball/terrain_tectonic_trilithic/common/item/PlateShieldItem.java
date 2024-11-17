package com.xkball.terrain_tectonic_trilithic.common.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.api.item.component.IRepairableComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@NonNullByDefault
@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID)
public class PlateShieldItem extends Item {
    public static final int BASE_DURABILITY = 128;
    
    public PlateShieldItem(TagKey<DamageType> tagKey) {
        super(new Properties()
                .stacksTo(1));
        var builder = DataComponentMap.builder().addAll(this.components);
        builder.set(TTDataComponents.SHIELD_DATA,ShieldData.create(tagKey,this));
        this.components = builder.build();
    }
    
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return true;
    }
    
    @SubscribeEvent
    public static void onIncomingHurt(LivingIncomingDamageEvent event){
        var entity = event.getEntity();
        var level = entity.level();
        if(!(level instanceof ServerLevel serverLevel)) return;
        var damage = event.getContainer();
        var source = event.getSource();
        var type = source.typeHolder();

        //有点怀疑这里用stream的性能
        var itemsWithShield = new ArrayList<Pair<TagKey<DamageType>,ItemStack>>(4);
        for(var is: entity.getArmorSlots()){
            if(is.has(TTDataComponents.SHIELD_DATA_MAP)){
                var key = Objects.requireNonNull(is.get(TTDataComponents.SHIELD_DATA_MAP)).keySet().stream().filter(type::is).findFirst();
                key.ifPresent(_key -> itemsWithShield.add(Pair.of(_key,is)));
            }
        }

        if(itemsWithShield.isEmpty()) return;
        var entry = itemsWithShield.get(TerrainTectonicTrilithic.GLOBAL_RANDOM.get().nextInt(itemsWithShield.size()));
        var itemStack = entry.getSecond();
        var dataMap = Objects.requireNonNull(itemStack.get(TTDataComponents.SHIELD_DATA_MAP)).copy();
        var tagkey = entry.getFirst();
        var data = dataMap.get(tagkey);
        var shadowItemStack = data.shadowItemStack.copy();
        var dDurability = EnchantmentHelper.processDurabilityChange(serverLevel,shadowItemStack, (int) Math.min(damage.getNewDamage(),20));
        var newDurability = data.durability - dDurability;
        if(newDurability < 0) dataMap.remove(tagkey);
        else dataMap.put(tagkey,new ShieldData(data.type,data.maxDurability,newDurability,data.shadowItemStack.copy()));
        if(!dataMap.isEmpty()) itemStack.set(TTDataComponents.SHIELD_DATA_MAP,dataMap);
        else itemStack.remove(TTDataComponents.SHIELD_DATA_MAP);
        damage.setNewDamage(0);
    }
    
    public record ShieldData(TagKey<DamageType> type, int maxDurability, int durability, ItemStack shadowItemStack) implements IRepairableComponent<ShieldData> {
        
        public static final Codec<TagKey<DamageType>> DAMAGE_TYPE_TAG_CODEC = TagKey.codec(Registries.DAMAGE_TYPE);
        public static final Codec<ShieldData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                DAMAGE_TYPE_TAG_CODEC.fieldOf("type").forGetter(o -> o.type),
                Codec.INT.fieldOf("max_durability").forGetter(o -> o.maxDurability),
                Codec.INT.fieldOf("durability").forGetter(o -> o.durability),
                ItemStack.CODEC.fieldOf("shadowItemStack").forGetter(o -> o.shadowItemStack)
        ).apply(ins, ShieldData::new));
        
        public static ShieldData create(TagKey<DamageType> type, ItemLike item) {
            return new ShieldData(type,BASE_DURABILITY,BASE_DURABILITY,item.asItem().getDefaultInstance());
        }
        
        @Nullable
        public static ShieldData fromItemStack(ItemStack stack) {
            var data = stack.get(TTDataComponents.SHIELD_DATA);
            if(data == null) return null;
            return new ShieldData(data.type,data.maxDurability,data.durability,stack);
        }
        
        @Override
        public boolean canReceiveXP() {
            return durability < maxDurability;
        }
        
        @Override
        public Pair<ShieldData, Integer> receiveXP(int value) {
            var rev = Math.min(value,maxDurability-durability);
            return Pair.of(new ShieldData(type,maxDurability,durability+rev,shadowItemStack),value-rev);
        }
    }
    
    public static class ShieldDataMap implements Map<TagKey<DamageType>,PlateShieldItem.ShieldData>, IRepairableComponent<ShieldDataMap> {
        
        public static final Codec<ShieldDataMap> CODEC = Codec.unboundedMap(PlateShieldItem.ShieldData.DAMAGE_TYPE_TAG_CODEC,PlateShieldItem.ShieldData.CODEC)
                .xmap(ShieldDataMap::new,ShieldDataMap::getInnerMap);
        private final Map<TagKey<DamageType>,PlateShieldItem.ShieldData> inner;
        
        public ShieldDataMap(Map<TagKey<DamageType>, ShieldData> inner) {
            this.inner = new HashMap<>(inner);
        }
        
        public ShieldDataMap(){
            this.inner = new HashMap<>();
        }
        
        public ShieldDataMap copy(){
            return new ShieldDataMap(inner);
        }
        
        @Override
        public int size() {
            return inner.size();
        }
        
        @Override
        public boolean isEmpty() {
            return inner.isEmpty();
        }
        
        @Override
        public boolean containsKey(Object key) {
            return inner.containsKey(key);
        }
        
        @Override
        public boolean containsValue(Object value) {
            return inner.containsValue(value);
        }
        
        @Override
        public ShieldData get(Object key) {
            return inner.get(key);
        }
        
        @Nullable
        @Override
        public ShieldData put(TagKey<DamageType> key, ShieldData value) {
            return inner.put(key,value);
        }
        
        @Override
        public ShieldData remove(Object key) {
            return inner.remove(key);
        }
        
        @Override
        public void putAll(@NotNull Map<? extends TagKey<DamageType>, ? extends ShieldData> m) {
            inner.putAll(m);
        }
        
        @Override
        public void clear() {
            inner.clear();
        }
        
        @NotNull
        @Override
        public Set<TagKey<DamageType>> keySet() {
            return inner.keySet();
        }
        
        @NotNull
        @Override
        public Collection<ShieldData> values() {
            return inner.values();
        }
        
        @NotNull
        @Override
        public Set<Entry<TagKey<DamageType>, ShieldData>> entrySet() {
            return inner.entrySet();
        }
        
        public Map<TagKey<DamageType>, ShieldData> getInnerMap(){
            return inner;
        }
        
        @Override
        public boolean canReceiveXP() {
            return inner.values().stream().anyMatch(ShieldData::canReceiveXP);
        }
        
        @Override
        public Pair<ShieldDataMap, Integer> receiveXP(int value) {
            var v = value;
            for(var entry: inner.entrySet()) {
                if(v <= 0) break;
                var res = entry.getValue().receiveXP(v);
                inner.put(entry.getKey(),res.getFirst());
                v = res.getSecond();
            }
            return Pair.of(this,value);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShieldDataMap that = (ShieldDataMap) o;
            return Objects.equals(inner, that.inner);
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(inner);
        }
    }
    
}
