package com.wjbaker.ccm.crosshair.style.styles;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.custom.CustomCrosshairDrawer;
import com.wjbaker.ccm.crosshair.render.ComputedProperties;
import com.wjbaker.ccm.crosshair.style.AbstractCrosshairStyle;
import com.wjbaker.ccm.type.RGBA;

public final class DrawnStyle extends AbstractCrosshairStyle {

    public DrawnStyle(final CustomCrosshair crosshair) {
        super(crosshair);
    }

    @Override
    public void draw(final int x, final int y, final ComputedProperties computedProperties) {
        CustomCrosshairDrawer image = CustomCrosshairMod.INSTANCE.properties().getCustomCrosshairDrawer();
        RGBA baseColour = computedProperties.colour();

        this.renderManager.drawImage(x, y, image, baseColour, true);
    }
}