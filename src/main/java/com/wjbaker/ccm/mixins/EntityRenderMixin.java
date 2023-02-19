package com.wjbaker.ccm.mixins;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.style.CrosshairStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin {

    /**
     * @author Sparkless101
     */
    @Overwrite
    private void renderWorldDirections(final float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.gameSettings.hideGUI || mc.thePlayer.hasReducedDebug() || mc.thePlayer.hasReducedDebug())
            return;

        boolean isDebugStyle = CustomCrosshairMod.INSTANCE.properties().getCrosshair().style.get() == CrosshairStyle.DEBUG;

        if (!isDebugStyle)
            return;

        Entity entity = mc.getRenderViewEntity();
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
}