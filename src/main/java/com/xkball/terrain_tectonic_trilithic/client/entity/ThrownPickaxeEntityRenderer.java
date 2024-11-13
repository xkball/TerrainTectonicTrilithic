package com.xkball.terrain_tectonic_trilithic.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.xkball.terrain_tectonic_trilithic.common.entity.ThrownPickaxeEntity;
import com.xkball.terrain_tectonic_trilithic.common.item.TTItems;
import com.xkball.terrain_tectonic_trilithic.utils.VanillaUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ThrownPickaxeEntityRenderer extends EntityRenderer<ThrownPickaxeEntity> {
    
    private static final RandomSource RANDOM = RandomSource.create();
    private static final Axis PITCH = Axis.of(new Vector3f(-1, 1, 0));
    private static final Axis ROLL = Axis.of(new Vector3f(1, 1, 0));
    
    public ThrownPickaxeEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(ThrownPickaxeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        var dv = entity.getDeltaMovement();
        if (dv.lengthSqr() > 1) {
            entity.renderPitch = (float) Math.atan(dv.y / Math.sqrt(dv.x * dv.x + dv.z * dv.z));
        }
        if (entity.renderZRotation > 360) {
            entity.renderZRotation = RANDOM.nextBoolean() ? 0 : 180;
        }
        if (entity.renderRoll > 360) {
            entity.renderRoll = RANDOM.nextInt(60) - 30;
        }
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.25D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 45));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.mulPose(PITCH.rotation(entity.renderPitch));
        poseStack.mulPose(ROLL.rotationDegrees(entity.renderRoll));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.renderZRotation - entity.renderZRotationDelta * (1 - partialTick)));
        //ClientUtils.renderAxis(bufferSource,poseStack);
        Minecraft.getInstance().getItemRenderer().renderStatic(TTItems.THE_PICKAXE.get().getDefaultInstance(), ItemDisplayContext.NONE, packedLight, 0, poseStack, bufferSource, null, 42);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    
    @Override
    public ResourceLocation getTextureLocation(ThrownPickaxeEntity entity) {
        return VanillaUtils.modRL("");
    }
}
