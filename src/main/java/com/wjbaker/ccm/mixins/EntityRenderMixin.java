package com.wjbaker.ccm.mixins;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.style.CrosshairStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public final class EntityRenderMixin {

    @Inject(method = "renderWorldDirections", at = @At("HEAD"), cancellable = true)
    private void cancelDebugMixin(float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        CustomCrosshair crosshair = CustomCrosshairMod.INSTANCE.properties().getCrosshair();

        boolean isDebugStyle = crosshair.style.get() == CrosshairStyle.DEBUG;
        boolean isReducedDebug = mc.gameSettings.reducedDebugInfo || mc.thePlayer.hasReducedDebug();
        boolean showInF3 = mc.gameSettings.showDebugInfo && !isReducedDebug && crosshair.isKeepDebugEnabled.get();

        if (!isDebugStyle && !showInF3) {
            ci.cancel();
        }
    }
}