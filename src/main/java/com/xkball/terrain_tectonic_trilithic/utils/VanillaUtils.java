package com.xkball.terrain_tectonic_trilithic.utils;

import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;

public class VanillaUtils {
    
    public static ResourceLocation modRL(String path)  {
        return rLOf(TerrainTectonicTrilithic.MODID, path);
    }
    
    public static ResourceLocation rLOf(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
    
    public static EquipmentSlot equipmentSlotFromHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }
    
    public static ItemInteractionResult itemInteractionFrom(InteractionResult result) {
        return switch (result) {
            case SUCCESS, SUCCESS_NO_ITEM_USED -> ItemInteractionResult.SUCCESS;
            case CONSUME -> ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL;
            case PASS -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            case FAIL -> ItemInteractionResult.FAIL;
        };
    }
    
    //server only
    public static void runCommand(String command, LivingEntity livingEntity) {
        // Raise permission level to 2, akin to what vanilla sign does
        CommandSourceStack cmdSrc = livingEntity.createCommandSourceStack().withPermission(2);
        var server = livingEntity.level().getServer();
        if (server != null) {
            server.getCommands().performPrefixedCommand(cmdSrc, command);
        }
    }
    
    //irrelevant vanilla(笑)
    public static int getColor(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    public static boolean haveEnoughDamageToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    public static Vec2 rotate90FormBlockCenterYP(Vec2 point, int times) {
        times = times % 4;
        if (times == 0) return point;
        var x = point.x;
        var y = point.y;
        if (times == 1) return new Vec2(16 - y, x);
        if (times == 2) return new Vec2(16 - x, 16 - y);
        return new Vec2(y, 16 - x);
    }
    
    public static boolean destroyBlock(ServerPlayer player, BlockPos pos, ItemStack tool) {
        var level = player.serverLevel();
        var gameMode = player.gameMode;
        BlockState blockState1 = level.getBlockState(pos);
        var event = net.neoforged.neoforge.common.CommonHooks.fireBlockBreak(level, gameMode.getGameModeForPlayer(), player, pos, blockState1);
        if (event.isCanceled()) {
            return false;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            Block block = blockState1.getBlock();
            if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
                level.sendBlockUpdated(pos, blockState1, blockState1, 3);
                return false;
            } else if (player.blockActionRestricted(level, pos, gameMode.getGameModeForPlayer())) {
                return false;
            } else {
                BlockState blockstate = block.playerWillDestroy(level, pos, blockState1, player);
                
                if (gameMode.isCreative()) {
                    gameMode.removeBlock(pos, blockstate, false);
                    return true;
                } else {
                    ItemStack toolCopy = tool.copy();
                    //neoforge事件不能指定itemStack
                    boolean flag1 = tool.isCorrectToolForDrops(blockState1);
                    tool.mineBlock(level, blockstate, pos, player);
                    boolean flag = gameMode.removeBlock(pos, blockstate, flag1);
                    
                    if (flag1 && flag) {
                        block.playerDestroy(level, player, pos, blockstate, blockentity, toolCopy);
                    }
                    
                    if (tool.isEmpty() && !toolCopy.isEmpty()) {
                        net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, toolCopy, InteractionHand.MAIN_HAND);
                    }
                    
                    return true;
                }
            }
        }
    }
}
