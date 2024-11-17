package com.xkball.terrain_tectonic_trilithic.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.xkball.terrain_tectonic_trilithic.api.item.component.IRepairableComponent;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ExperienceOrb.class)
public class MixinExpOrb {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "repairPlayerItems",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;modifyDurabilityToRepairFromXp(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;I)I"),
            cancellable = true
    )
    public void onGetItemStack(ServerPlayer player, int value, CallbackInfoReturnable<Integer> cir, @Local ItemStack itemstack){
        var outHaveEnchantment = EnchantmentHelper.has(itemstack,EnchantmentEffectComponents.REPAIR_WITH_XP);
        for(var entry : itemstack.getComponents()){
            DataComponentType type = entry.type();
            Object data = entry.value();
            if(!(data instanceof IRepairableComponent<?> component)) continue;
            if(!component.canReceiveXP()) continue;
            var res = component.receiveXP(value);
            itemstack.set(type,res.getFirst());
            cir.setReturnValue(res.getSecond());
            cir.cancel();
            return;
        }
        if(!outHaveEnchantment){
            cir.setReturnValue(value);
            cir.cancel();
        }
    }
    
    @Redirect(method = "repairPlayerItems",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getRandomItemWith(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Predicate;)Ljava/util/Optional;"))
    public Optional<EnchantedItemInUse> onChooseRepairItem(DataComponentType<?> holder, LivingEntity entity, Predicate<ItemStack> itemEnchantments){
        var result = EnchantmentHelper.getRandomItemWith(holder, entity, itemEnchantments);
        if(result.isPresent() || holder != EnchantmentEffectComponents.REPAIR_WITH_XP) return result;
        List<EnchantedItemInUse> list = new ArrayList<>();
        for(var slot : EquipmentSlot.values()){
            var itemStack = entity.getItemBySlot(slot);
            for(var entry : itemStack.getComponents()){
                if(entry.value() instanceof IRepairableComponent<?>){
                    list.add(new EnchantedItemInUse(itemStack,slot,entity));
                    break;
                }
            }
        }
        return Util.getRandomSafe(list,entity.getRandom());
    }
}
