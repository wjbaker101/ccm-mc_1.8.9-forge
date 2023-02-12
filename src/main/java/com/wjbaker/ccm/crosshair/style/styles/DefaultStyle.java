package com.wjbaker.ccm.crosshair.style.styles;

import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.render.ComputedProperties;
import com.wjbaker.ccm.crosshair.style.AbstractCrosshairStyle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public final class DefaultStyle extends AbstractCrosshairStyle {

    private final ResourceLocation guiIconsLocation;

    public DefaultStyle(final CustomCrosshair crosshair) {
        super(crosshair);

        this.guiIconsLocation = new ResourceLocation("textures/gui/icons.png");
    }

    @Override
    public void draw(final int x, final int y, final ComputedProperties computedProperties) {
        GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);

        Minecraft.getMinecraft().getTextureManager().bindTexture(this.guiIconsLocation);

        int size = 15;
        this.drawDefaultCrossahir(x - Math.round(size / 2.0F), y - Math.round(size / 2.0F), 0, 0, 16, 16);
        
        GlStateManager.disableBlend();
    }
    
    private void drawDefaultCrossahir(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), (double)10).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), (double)10).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), (double)10).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), (double)10).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f)).endVertex();
        tessellator.draw();
    }
}
