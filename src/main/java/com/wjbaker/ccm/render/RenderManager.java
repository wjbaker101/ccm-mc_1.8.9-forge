package com.wjbaker.ccm.render;

import com.wjbaker.ccm.crosshair.custom.CustomCrosshairDrawer;
import com.wjbaker.ccm.render.type.GuiBounds;
import com.wjbaker.ccm.render.type.IDrawInsideWindowCallback;
import com.wjbaker.ccm.type.RGBA;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class RenderManager {

    private void setGlProperty(final int property, final boolean isEnabled) {
        if (isEnabled)
            GL11.glEnable(property);
        else
            GL11.glDisable(property);
    }

    private void setColour(final RGBA colour) {
        GL11.glColor4f(
            colour.getRed() / 255.0F,
            colour.getGreen() / 255.0F,
            colour.getBlue() / 255.0F,
            colour.getOpacity() / 255.0F);
    }

    private void preRender() {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    private void postRender() {
    	GlStateManager.enableTexture2D();
    	GlStateManager.disableBlend();
    	this.setColour(ModTheme.WHITE);
        GL11.glPopMatrix();
    }

    public void drawLines(float[] points, final float thickness, final RGBA colour) {
    	this.preRender();
        this.setGlProperty(GL11.GL_LINE_SMOOTH, false);
        setColour(colour);

        GL11.glLineWidth(thickness);
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        
        for (int i = 0; i < points.length; i += 2) {
            worldrenderer.pos(points[i], points[i + 1], 0.0D).endVertex();
        }
        
        tessellator.draw();

        this.postRender();
    }

    public void drawFilledShape(final float[] points, final RGBA colour) {
    	this.preRender();
        this.setGlProperty(GL11.GL_LINE_SMOOTH, false);
        setColour(colour);
        
    	Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        
        for (int i = 0; i < points.length; i += 2) {
            worldrenderer.pos(points[i], points[i + 1], 0.0D).endVertex();
        }
        
        tessellator.draw();

        this.postRender();
    }

    public void drawLine(
        final float x1, final float y1,
        final float x2, final float y2,
        final float thickness,
        final RGBA colour) {

        this.drawLines(new float[] {
            x1, y1,
            x2, y2
        }, thickness, colour);
    }

    public void drawRectangle(
        final float x1, final float y1,
        final float x2, final float y2,
        final float thickness,
        final RGBA colour) {

        this.drawLines(new float[] {
            x1, y1, x2, y1,
            x2, y1, x2, y2,
            x1, y2, x2, y2,
            x1, y1, x1, y2
        }, thickness, colour);
    }

    public void drawFilledRectangle(
        final float x1, final float y1,
        final float x2, final float y2,
        final RGBA colour) {

        this.drawFilledShape(new float[] {
            x1, y1,
            x1, y2,
            x2, y2,
            x2, y1
        }, colour);
    }

    public void drawBorderedRectangle(
        final float x1, final float y1,
        final float x2, final float y2,
        final float borderThickness,
        final RGBA borderColour,
        final RGBA fillColour) {

        this.drawFilledRectangle(x1, y1, x2, y2, fillColour);
        this.drawRectangle(x1, y1, x2, y2, borderThickness, borderColour);
    }
    
    public void drawPartialCircle(
            final float x, final float y,
            final float radius,
            final int startAngleAt,
            final int endAngleAt,
            final float thickness,
            final RGBA colour) {

    	this.preRender();
        this.setGlProperty(GL11.GL_LINE_SMOOTH, true);
        this.setColour(colour);

        int startAngle = Math.max(0, Math.min(startAngleAt, endAngleAt));
        int endAngle = Math.min(360, Math.max(startAngleAt, endAngleAt));

        GL11.glLineWidth(thickness);

        float ratio = (float)Math.PI / 180.F;
    	
    	Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        
        for (int i = startAngle; i <= endAngle; ++i) {
            float radians = (i - 90) * ratio;

            worldrenderer.pos(x + (float)Math.cos(radians) * radius, y + (float)Math.sin(radians) * radius, 0.0D).endVertex();
        }
        
        tessellator.draw();

        this.postRender();
    }

    public void drawCircle(
        final float x, final float y,
        final float radius,
        final float thickness,
        final RGBA colour) {

        this.drawPartialCircle(x, y, radius, 0, 360, thickness, colour);
    }

    public void drawFilledCircle(
        final float x, final float y,
        final float radius,
        final RGBA colour) {

        float[] points = new float[361 * 2];

        float ratio = (float)Math.PI / 180.F;

        for (int i = 0; i <= 360; ++i) {
            float radians = (i - 90) * ratio;

            points[i * 2] = x + (float)Math.cos(radians) * radius;
            points[i * 2 + 1] = y + (float)Math.sin(radians) * radius;
        }

        this.drawFilledShape(points, colour);
    }

    public void drawTorus(
        final int x, final int y,
        final int innerRadius,
        final int outerRadius,
        final RGBA colour) {

    	this.preRender();
        this.setGlProperty(GL11.GL_LINE_SMOOTH, true);
        this.setColour(colour);

        float ratio = (float)Math.PI / 180.F;
    	
    	Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        
        for (int i = 0; i <= 360; ++i) {
	        float radians = (i - 90) * ratio;

            worldrenderer.pos(x + (float)Math.cos(radians) * innerRadius, y + (float)Math.sin(radians) * innerRadius, 0.0D).endVertex();
            worldrenderer.pos(x + (float)Math.cos(radians) * outerRadius, y + (float)Math.sin(radians) * outerRadius, 0.0D).endVertex();
        }
        
        tessellator.draw();

        this.postRender();
    }

    public void drawImage(
        final int x, final int y,
        final CustomCrosshairDrawer image,
        final RGBA colour,
        final boolean isCentered) {

        float offsetX = isCentered ? image.getWidth() / 2.0F : 0;
        float offsetY = isCentered ? image.getHeight() / 2.0F : 0;

        int width = image.getWidth();
        int height = image.getHeight();

        for (int imageX = 0; imageX < width; ++imageX) {
            for (int imageY = 0; imageY < height; ++imageY) {
                if (image.getAt(imageX, imageY) == 1) {
                    float drawX = x + imageX - offsetX;
                    float drawY = y + imageY - offsetY;

                    this.drawFilledRectangle(drawX, drawY, drawX + 1, drawY + 1, colour);
                }
            }
        }
    }
    
    public void drawDot(final float x, final float y, final float size, final RGBA colour) {
    	this.preRender();
        this.setGlProperty(GL11.GL_LINE_SMOOTH, true);
        this.setGlProperty(GL11.GL_POINT_SMOOTH, true);
        this.setColour(colour);
        
        GL11.glPointSize(size);
        
        GL11.glBegin(GL11.GL_POINTS);

        GL11.glVertex2f(x, y);

        GL11.glEnd();
        
        this.postRender();
    }

    public void drawText(final String text, final int x, final int y, final RGBA colour, final boolean hasShadow) {
        int colourAsInt = this.rgbaAsInt(colour);

        if (hasShadow)
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, colourAsInt);
        else
            Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, colourAsInt);
    }

    public void drawSmallText(final String text, final int x, final int y, final RGBA colour, final boolean hasShadow) {
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        this.drawText(text, x * 2, y * 2, colour, hasShadow);
        GL11.glScalef(2.0F, 2.0F, 1.0F);
    }

    public void drawBigText(final String text, final int x, final int y, final RGBA colour, final boolean hasShadow) {
        GL11.glScalef(1.5F, 1.5F, 1.0F);
        this.drawText(text, (int)(x * 0.666F), (int)(y * 0.666F), colour, hasShadow);

        float scale = 1 / 1.5F;
        GL11.glScalef(scale, scale, 1.0F);
    }

    private int rgbaAsInt(final RGBA colour) {
        return (colour.getOpacity() << 24) + (colour.getRed() << 16) + (colour.getGreen() << 8) + colour.getBlue();
    }

    public int textWidth(final String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    public void drawInsideBounds(final GuiBounds bounds, final IDrawInsideWindowCallback callback) {
        this.setGlProperty(GL11.GL_SCISSOR_TEST, true);

        ScaledResolution window = new ScaledResolution(Minecraft.getMinecraft());
        double scale = window.getScaleFactor();

        GL11.glScissor(
            (int)Math.round(bounds.x() * scale),
            (int)Math.round(window.getScaledHeight() - bounds.y() - bounds.height() + 1),
            (int)Math.round(bounds.width() * scale),
            (int)Math.round(bounds.height() * scale));

        callback.draw();

        this.setGlProperty(GL11.GL_SCISSOR_TEST, false);
    }
}