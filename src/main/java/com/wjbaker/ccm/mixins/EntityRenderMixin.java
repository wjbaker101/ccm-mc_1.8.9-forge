package com.wjbaker.ccm.mixins;

import com.wjbaker.ccm.CustomCrosshairMod;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin {

    @Inject(
        at = @At("HEAD"),
        method = "renderWorldDirections",
        cancellable = true
    )
    private void renderWorldDirections(final CallbackInfo info) {
        if (!CustomCrosshairMod.INSTANCE.properties().getIsModEnabled().get())
            return;

        info.cancel();
    }
}