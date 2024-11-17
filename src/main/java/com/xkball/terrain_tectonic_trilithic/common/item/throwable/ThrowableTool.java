package com.xkball.terrain_tectonic_trilithic.common.item.throwable;

import com.xkball.terrain_tectonic_trilithic.common.entity.ThrownToolEntity;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ThrowableTool extends TieredItem {
    
  
    public static final Tier TIER = new Tier() {
        @Override
        public int getUses() {
            return 4095;
        }
        
        @Override
        public float getSpeed() {
            return 12;
        }
        
        @Override
        public float getAttackDamageBonus() {
            return 1;
        }
        
        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        }
        
        @Override
        public int getEnchantmentValue() {
            return 15;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(TTItems.CHLOROPHYLL_SHARD);
        }
    };
    
    public ThrowableTool(Item.Properties properties) {
        super(TIER, properties);
    }
    
    public static Item.Properties pickaxeProperties() {
        return new Properties()
                .fireResistant()
                .stacksTo(1)
                .attributes(DiggerItem.createAttributes(Tiers.NETHERITE, 3, -1))
                .component(DataComponents.TOOL, new Tool(List.of(
                        Tool.Rule.deniesDrops(BlockTags.INCORRECT_FOR_NETHERITE_TOOL),
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_AXE, 12),
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_PICKAXE, 12),
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_HOE, 12),
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_SHOVEL, 12)
                ), 12, 0));
    }
    
    public static Item.Properties sickleProperties() {
        return new Properties()
                .fireResistant()
                .stacksTo(1)
                .attributes(DiggerItem.createAttributes(Tiers.NETHERITE, 9, -1));
    }
    
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }
    
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }
    
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 36000;
    }
    
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return true;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (VanillaUtils.haveEnoughDamageToUse(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player && level instanceof ServerLevel serverLevel) {
            int i = this.getUseDuration(stack, livingEntity) - timeCharged;
            if (i > 0) {
                player.getCooldowns().addCooldown(this, 4);
                if (!level.isClientSide()) {
                    var pe = new ThrownToolEntity(level);
                    pe.setPos(livingEntity.getEyePosition());
                    pe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, Math.min(1+i/30f,5f), 1.0F);
                    if (player instanceof ServerPlayer sp) pe.setShooterAndTool(sp,stack);
                    pe.setBehavior(getBehaviorType());
                    level.addFreshEntity(pe);
                    stack.hurtAndBreak(2, serverLevel, livingEntity,item -> {});
                }
            }
        }
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }
    
    public abstract ThrownToolEntity.BehaviorType getBehaviorType();
}
