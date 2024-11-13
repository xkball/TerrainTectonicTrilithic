package com.xkball.terrain_tectonic_trilithic.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class ClientUtils {
    
    public static void renderAxis(MultiBufferSource bufferSource, PoseStack poseStack) {
        var buffer = bufferSource.getBuffer(RenderType.lines());
        var matrix = poseStack.last();
        buffer.addVertex(matrix, 0, 0, 0).setNormal(matrix, 1, 0, 0).setColor(0xFFFF0000);
        buffer.addVertex(matrix, 1, 0, 0).setNormal(matrix, 1, 0, 0).setColor(0xFFFF0000);
        buffer.addVertex(matrix, 0, 0, 0).setNormal(matrix, 0, 1, 0).setColor(0xFF00FF00);
        buffer.addVertex(matrix, 0, 1, 0).setNormal(matrix, 0, 1, 0).setColor(0xFF00FF00);
        buffer.addVertex(matrix, 0, 0, 0).setNormal(matrix, 0, 0, 1).setColor(0xFF0000FF);
        buffer.addVertex(matrix, 0, 0, 1).setNormal(matrix, 0, 0, 1).setColor(0xFF0000FF);
    }
}
