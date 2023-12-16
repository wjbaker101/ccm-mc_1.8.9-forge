package com.wjbaker.ccm.mixins;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.render.CrosshairRenderManager;
import com.wjbaker.ccm.crosshair.style.CrosshairStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public final class EntityRenderMixin {

    @Inject(
        at = @At(
            value = "CONSTANT",
            args = "stringValue=gui"
        ),
        method = "updateCameraAndRender"
    )
    private void updateCameraAndRender(
        final float partialTicks,
        final long nanoseconds,
        final CallbackInfo callbackInfo) {

        if (!CustomCrosshairMod.INSTANCE.properties().getIsModEnabled().get())
            return;

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat))
            return;

        ScaledResolution resolution = new ScaledResolution(mc);
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        int x = Math.round(width / 2.0F);
        int y = Math.round(height / 2.0F);

        CustomCrosshair crosshair = CustomCrosshairMod.INSTANCE.properties().getCrosshair();

        new CrosshairRenderManager().draw(crosshair, x, y);

        boolean isReducedDebug = mc.gameSettings.reducedDebugInfo || mc.thePlayer.hasReducedDebug();
        boolean showInF3 = mc.gameSettings.showDebugInfo && !isReducedDebug && crosshair.isKeepDebugEnabled.get();

        if (showInF3 || crosshair.style.get() == CrosshairStyle.DEBUG)
            manuallyRenderDebugCrosshair(partialTicks);
    }

    private void manuallyRenderDebugCrosshair(final float partialTicks) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(1.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.orientCamera(partialTicks);
        GlStateManager.translate(0.0F, entity.getEyeHeight(), 0.0F);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Shadow
    private void orientCamera(float partialTicks) {}

    @Inject(method = "renderWorldDirections", at = @At("HEAD"), cancellable = true)
    private void renderWorldDirections(final float partialTicks, final CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}