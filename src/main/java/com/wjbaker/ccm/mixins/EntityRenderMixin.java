package com.wjbaker.ccm.mixins;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.render.CrosshairRenderManager;
import com.wjbaker.ccm.crosshair.style.CrosshairStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public final class EntityRenderMixin {

    private static final CustomCrosshair crosshair = CustomCrosshairMod.INSTANCE.properties().getCrosshair();

    @Shadow
    private final Minecraft mc = Minecraft.getMinecraft();

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

        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat))
            return;

        ScaledResolution resolution = new ScaledResolution(mc);
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        int x = Math.round(width / 2.0F);
        int y = Math.round(height / 2.0F);

        new CrosshairRenderManager().draw(crosshair, x, y);

        if (crosshair.style.get() == CrosshairStyle.DEBUG)
            this.renderWorldDirections(partialTicks);
    }

    @Shadow
    @Final
    private void renderWorldDirections(float partialTicks) {}

    @Inject(method = "renderWorldDirections", at = @At("HEAD"), cancellable = true)
    private void onRenderWorldDirections(final float partialTicks, final CallbackInfo callbackInfo) {
        boolean isReducedDebug = mc.gameSettings.reducedDebugInfo || mc.thePlayer.hasReducedDebug();
        boolean showInF3 = mc.gameSettings.showDebugInfo && !isReducedDebug && crosshair.isKeepDebugEnabled.get();

        if (crosshair.style.get() != CrosshairStyle.DEBUG && !showInF3)
            callbackInfo.cancel();
    }

    @Redirect(
        method = "renderWorldDirections",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;showDebugInfo:B",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean forceDebugCrosshairToShow_showDebugInfo(final GameSettings originalValue) {
        return true;
    }

    @Redirect(
        method = "renderWorldDirections",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;hideGUI:B",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean forceDebugCrosshairToShow_hideGUI(final GameSettings originalValue) {
        return false;
    }

    @Redirect(
        method = "renderWorldDirections",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;hasReducedDebug()B"
        )
    )
    private boolean forceDebugCrosshairToShow_hasReducedDebug() {
        return false;
    }

    @Redirect(
        method = "renderWorldDirections",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;reducedDebugInfo:B",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean forceDebugCrosshairToShow_reducedDebugInfo(final GameSettings originalValue) {
        return false;
    }
}