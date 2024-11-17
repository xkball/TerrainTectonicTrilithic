package com.xkball.terrain_tectonic_trilithic.common.entity;

import com.mojang.serialization.Codec;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ThrownToolEntity extends AbstractArrow {
    
    private static final EntityDataAccessor<BehaviorType> BEHAVIOR = SynchedEntityData.defineId(ThrownToolEntity.class,TTRegistries.THROWN_TOOL_TYPE.get());
    public float renderZRotation = 114514;
    public float renderZRotationDelta = 0;
    public float renderRoll = 114514;
    public float renderPitch = 0;
    public int breakCount = 0;
    public ServerPlayer shooter = null;
    public ItemStack tool = ItemStack.EMPTY;
    public int lifetime = 0;
    
    public ThrownToolEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }
    
    public ThrownToolEntity(Level level) {
        this(TTRegistries.THROWN_TOOL_ENTITY_TYPE.get(), level);
        this.setBaseDamage(8 + this.random.triangle(-1,1));
    }
    
    @Override
    public void tick() {
        var level = this.level();
        if (level.isClientSide()) {
            renderZRotationDelta = renderZRotation;
            renderZRotation += (float) (Math.max(0, Math.log(this.getDeltaMovement().length() * 6) * 40));
            renderZRotationDelta = renderZRotation - renderZRotationDelta;
            renderZRotation %= 360;
        }
        super.tick();
        lifetime++;
        if (this.inGround) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
        }
        shakeTime = 0;
    }
    
    public void breakBlock(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            var block = level.getBlockState(pos);
            if (canBreakBlock(block) && this.getDeltaMovement().length() > 0.3) {
                if (_breakBlock(pos)) {
                    var d = breakCount*0.05;
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7 - d, 0.5 - d, 0.7 - d));
                    this.inGround = false;
                }
            }
        }
    }
    
    public boolean _breakBlock(BlockPos pos) {
        if (shooter != null && breakCount<15+lifetime/40) {
        breakCount+=1;
        return VanillaUtils.destroyBlock(shooter,pos,tool);
        } else {
            return false;
        }
    }
    
    public void setShooterAndTool(ServerPlayer shooter,ItemStack tool) {
        this.shooter = shooter;
        this.tool = tool;
    }
    
    public void setBehavior(BehaviorType behavior) {
        this.getEntityData().set(BEHAVIOR, behavior);
    }
    
    private static boolean canBreakBlock(BlockState block) {
        return block.is(BlockTags.MINEABLE_WITH_AXE) || block.is(BlockTags.MINEABLE_WITH_PICKAXE) || block.is(BlockTags.MINEABLE_WITH_SHOVEL) || block.is(BlockTags.MINEABLE_WITH_HOE);
    }
    
    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEHAVIOR,BehaviorType.PICKAXE);
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(this.getEntityData().get(BEHAVIOR) == BehaviorType.SICKLE){
            super.onHitEntity(result);
        }
    }
    
    @Override
    protected void onHitBlock(BlockHitResult result) {
        if(this.getEntityData().get(BEHAVIOR) == BehaviorType.PICKAXE){
            var d = this.getDeltaMovement();
            super.onHitBlock(result);
            this.setDeltaMovement(d);
            var level = this.level();
            this.breakBlock(level, result.getBlockPos());
        }
        else {
            super.onHitBlock(result);
        }
        this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
    }
    
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }
    
    
    
    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
    
    }
    
    public ItemStack renderAs(){
        var behavior = this.getEntityData().get(BEHAVIOR);
        if(behavior == BehaviorType.PICKAXE) return TTItems.THROWABLE_PICKAXE.asItem().getDefaultInstance();
        if(behavior == BehaviorType.SICKLE) return TTItems.THROWABLE_SICKLE.asItem().getDefaultInstance();
        return ItemStack.EMPTY;
    }
    
    @Override
    protected ItemStack getDefaultPickupItem() {
        var behavior = this.getEntityData().get(BEHAVIOR);
        if(behavior == BehaviorType.PICKAXE) return TTItems.THROWABLE_PICKAXE.asItem().getDefaultInstance();
        return TTItems.THROWABLE_SICKLE.asItem().getDefaultInstance();
    }
    
    public enum BehaviorType implements StringRepresentable  {
        PICKAXE,
        SICKLE;
        
        public static final Codec<BehaviorType> CODEC = StringRepresentable.fromEnum(BehaviorType::values);
        public static final StreamCodec<ByteBuf,BehaviorType> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
        
        @Override
        @NotNull
        public String getSerializedName() {
            return name();
        }
    }
}
