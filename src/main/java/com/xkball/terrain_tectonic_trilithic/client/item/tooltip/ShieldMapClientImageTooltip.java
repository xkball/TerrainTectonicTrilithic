package com.xkball.terrain_tectonic_trilithic.client.item.tooltip;

import com.mojang.datafixers.util.Either;
import com.xkball.terrain_tectonic_trilithic.TerrainTectonicTrilithic;
import com.xkball.terrain_tectonic_trilithic.api.annotation.NonNullByDefault;
import com.xkball.terrain_tectonic_trilithic.common.item.TTDataComponents;
import com.xkball.terrain_tectonic_trilithic.common.item.tooltip.ShieldMapServerImageTooltip;
import com.xkball.terrain_tectonic_trilithic.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.Objects;

@NonNullByDefault
@EventBusSubscriber(modid = TerrainTectonicTrilithic.MODID, value = Dist.CLIENT)
public record ShieldMapClientImageTooltip(ShieldMapServerImageTooltip tooltip) implements ClientTooltipComponent {
    
    private static boolean rendering = false;
    
    @Override
    public int getHeight() {
        return tooltip.shadowItem().stream().mapToInt((is) -> ClientUtils.tooltipSize(Minecraft.getInstance().font, is).y).max().orElse(20)+16;
    }
    
    @Override
    public int getWidth(Font font) {
        return tooltip.shadowItem().stream().mapToInt((is) -> ClientUtils.tooltipSize(Minecraft.getInstance().font, is).x+6).sum()+4;
    }
    
    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        rendering = true;
        x+=2;
        for(var item : tooltip.shadowItem()) {
            guiGraphics.renderItem(item,x,y);
            guiGraphics.renderItemDecorations(font,item,x,y);
            var tooltips = ClientHooks.gatherTooltipComponents(item, Screen.getTooltipFromItem(Minecraft.getInstance(),item), item.getTooltipImage(),0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), font);
            guiGraphics.renderTooltipInternal(font,tooltips,x-8,y+32, DefaultTooltipPositioner.INSTANCE);
            x+= ClientUtils.tooltipSize(font,tooltips).x+6;
        }
        rendering = false;
    }
    
    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event){
        var item = event.getItemStack();
        if(!item.has(TTDataComponents.SHIELD_DATA_MAP)) return;
        event.getTooltipElements().add(1,Either.right(ShieldMapServerImageTooltip.fromShieldMap(Objects.requireNonNull(item.get(TTDataComponents.SHIELD_DATA_MAP)))));
    }
    
    @SubscribeEvent
    public static void onGetTooltipColor(RenderTooltipEvent.Color event){
        if(!rendering) return;
        event.setBackground(0);
    }
}
