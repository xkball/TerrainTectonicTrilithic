package com.xkball.terrain_tectonic_trilithic.client.item.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.item.TTDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.Nullable;

@NonNullByDefault
public class FakeFoodWrapper extends BakedModelWrapper<BakedModel> {
    
    private final StableItemOverrides override;
    
    public FakeFoodWrapper(BakedModel originalModel) {
        super(originalModel);
        override = new StableItemOverrides();
    }
    
    @Override
    public boolean isCustomRenderer() {
        return false;
    }
    
    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        return this;
    }
    
    @Override
    public ItemOverrides getOverrides() {
        return override;
    }
    
    public static class StableItemOverrides extends ItemOverrides {
        @Nullable
        @Override
        public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            var fakeItem = stack.get(TTDataComponents.FAKE_ITEM);
            if(fakeItem != null) return Minecraft.getInstance().getItemRenderer().getModel(fakeItem.itemStack(),level,entity,seed);
            return model;
        }
    }
}
