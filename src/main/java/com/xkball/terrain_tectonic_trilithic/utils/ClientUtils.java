package com.xkball.terrain_tectonic_trilithic.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Vector2i;

import java.util.List;

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
    
    public static Vector2i tooltipSize(Font font,List<ClientTooltipComponent> components){
        int i = 0;
        int j = components.size() == 1 ? -2 : 0;
        
        for (ClientTooltipComponent clienttooltipcomponent : components) {
            int k = clienttooltipcomponent.getWidth(font);
            if (k > i) {
                i = k;
            }
            
            j += clienttooltipcomponent.getHeight();
        }
        return new Vector2i(i, j);
    }
    
    public static Vector2i tooltipSize(Font font, ItemStack itemStack){
        var tooltips = ClientHooks.gatherTooltipComponents(itemStack, Screen.getTooltipFromItem(Minecraft.getInstance(),itemStack), itemStack.getTooltipImage(),0, 114514, 1919810, font);
        return tooltipSize(font, tooltips);
    }
}
