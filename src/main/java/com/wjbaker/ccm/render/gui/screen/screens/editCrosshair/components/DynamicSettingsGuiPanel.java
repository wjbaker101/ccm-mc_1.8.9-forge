package com.wjbaker.ccm.render.gui.screen.screens.editCrosshair.components;

import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.render.gui.component.components.CheckBoxGuiComponent;
import com.wjbaker.ccm.render.gui.component.components.HeadingGuiComponent;
import com.wjbaker.ccm.render.gui.component.components.PanelGuiComponent;
import com.wjbaker.ccm.render.gui.screen.GuiScreen;

public final class DynamicSettingsGuiPanel extends PanelGuiComponent {

    public DynamicSettingsGuiPanel(
        final GuiScreen parentGuiScreen,
        final int x,
        final int y,
        final int width,
        final int height,
        final CustomCrosshair crosshair) {

        super(parentGuiScreen, x, y, width, height);

        HeadingGuiComponent heading = new HeadingGuiComponent(this.parentGuiScreen, -1, -1, "Dynamic Crosshair Settings");

        CheckBoxGuiComponent isDynamicBowEnabledCheckBox = new CheckBoxGuiComponent(
            this.parentGuiScreen,
            -1, -1,
            "Enable Dynamic Pull Progress",
            crosshair.isDynamicBowEnabled.get());
        isDynamicBowEnabledCheckBox.bind(crosshair.isDynamicBowEnabled);

        this.addComponent(heading);
        this.addComponent(isDynamicBowEnabledCheckBox);
        this.pack();
    }
}
