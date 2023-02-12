package com.wjbaker.ccm.crosshair.render;

import com.google.common.collect.ImmutableSet;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.style.CrosshairStyle;
import com.wjbaker.ccm.crosshair.style.CrosshairStyleFactory;
import com.wjbaker.ccm.crosshair.style.ICrosshairStyle;
import com.wjbaker.ccm.render.RenderManager;
import com.wjbaker.ccm.type.RGBA;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.Set;

public final class CrosshairRenderManager {

    private final CustomCrosshair crosshair;
    private final RenderManager renderManager;
    private final CrosshairStyleFactory crosshairStyleFactory;

    private final Set<Item> itemCooldownItems = ImmutableSet.of(
        Items.ender_pearl
    );

    public CrosshairRenderManager(final CustomCrosshair crosshair) {
        this.crosshair = crosshair;
        this.renderManager = new RenderManager();
        this.crosshairStyleFactory = new CrosshairStyleFactory();
    }

    public void draw(final int x, final int y) {
        ComputedProperties computedProperties = new ComputedProperties(this.crosshair);

        if (!computedProperties.isVisible())
            return;

        RenderGameOverlayEvent eventParent = new RenderGameOverlayEvent(
            1.0F,
            new ScaledResolution(Minecraft.getMinecraft()));

        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(
            eventParent,
            RenderGameOverlayEvent.ElementType.CROSSHAIRS));

        ICrosshairStyle style = this.crosshairStyleFactory.from(this.crosshair.style.get(), this.crosshair);
        boolean isDotEnabled = this.crosshair.isDotEnabled.get();
        
        if (Minecraft.getMinecraft().gameSettings.hideGUI) {
        	ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		}

        if (isDotEnabled && this.crosshair.style.get() != CrosshairStyle.DEFAULT)
            this.renderManager.drawDot(x, y, 3.0F, this.crosshair.dotColour.get());

        int renderX = x + crosshair.offsetX.get();
        int renderY = y + crosshair.offsetY.get();
        
        this.preTransformation(renderX, renderY);

        style.draw(renderX, renderY, computedProperties);

        this.postTransformation();

        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(
            eventParent,
            RenderGameOverlayEvent.ElementType.CROSSHAIRS));
    }

    private void preTransformation(final int x, final int y) {
        int rotation = this.crosshair.rotation.get();
        int scale = this.crosshair.scale.get();

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale / 100.0F, scale / 100.0F, 1.0F);
        GL11.glRotatef(rotation, x, y, 8000);
        GL11.glTranslatef(-x, -y, 0);
    }

    private void postTransformation() {
        GL11.glPopMatrix();
    }
}
