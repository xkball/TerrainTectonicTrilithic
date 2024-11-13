package com.xkball.terrain_tectonic_trilithic.common.entity;

import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.registry.TTRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ThrownPickaxeEntity extends AbstractArrow {
    
    public float renderZRotation = 114514;
    public float renderZRotationDelta = 0;
    public float renderRoll = 114514;
    public float renderPitch = 0;
    public ServerPlayer shooter = null;
    
    public ThrownPickaxeEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }
    
    public ThrownPickaxeEntity(Level level) {
        this(TTRegistries.THROWN_PICKAXE_ENTITY_TYPE.get(), level);
    }
    
    @Override
    public void tick() {
        var level = this.level();
        if (level.isClientSide()) {
            renderZRotationDelta = renderZRotation;
            renderZRotation += (float) (Math.max(0, Math.log(this.getDeltaMovement().length() * 3) * 60));
            renderZRotationDelta = renderZRotation - renderZRotationDelta;
            renderZRotation %= 360;
        }
        super.tick();
        if (this.inGround) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.6));
        }
    }
    
    public void breakBlock(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            var block = level.getBlockState(pos);
            if (canBreakBlock(block) && this.getDeltaMovement().length() > 0.3) {
                if (_breakBlock(level, pos)) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, 0.5, 0.7));
                    this.inGround = false;
                }
            }
        }
    }
    
    public boolean _breakBlock(Level level, BlockPos pos) {
        if (shooter != null && shooter.level() == level) {
            return shooter.gameMode.destroyBlock(pos);
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
    }
    
    public void setShooter(ServerPlayer shooter) {
        this.shooter = shooter;
    }
    
    private static boolean canBreakBlock(BlockState block) {
        return !block.isAir() && !block.is(BlockTags.WITHER_IMMUNE) && !(block.getBlock() instanceof GameMasterBlock);
    }
    
    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        //no-op
    }
    
    @Override
    protected void onHitBlock(BlockHitResult result) {
        var d = this.getDeltaMovement();
        super.onHitBlock(result);
        this.setDeltaMovement(d);
        var level = this.level();
        this.breakBlock(level, result.getBlockPos());
    }
    
    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
    
    }
    
    @Override
    protected ItemStack getDefaultPickupItem() {
        return TTItems.THE_PICKAXE.asItem().getDefaultInstance();
    }
}
