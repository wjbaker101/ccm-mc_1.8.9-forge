package com.wjbaker.ccm.render.gui.screen.screens.editCrosshair.components;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.style.CrosshairStyle;
import com.wjbaker.ccm.render.gui.component.components.*;
import com.wjbaker.ccm.render.gui.component.event.IOnClickEvent;
import com.wjbaker.ccm.render.gui.screen.GuiScreen;
import com.wjbaker.ccm.render.gui.screen.screens.drawCrosshair.DrawCrosshairGuiScreen;
import net.minecraft.client.Minecraft;

public final class GeneralSettingsGuiPanel extends PanelGuiComponent {

    public GeneralSettingsGuiPanel(
        final GuiScreen parentGuiScreen,
        final int x,
        final int y,
        final int width,
        final int height,
        final CustomCrosshair crosshair) {

        super(parentGuiScreen, x, y, width, height);

        HeadingGuiComponent heading = new HeadingGuiComponent(this.parentGuiScreen, -1, -1, "General Settings");

        CheckBoxGuiComponent isModEnabledCheckbox = new CheckBoxGuiComponent(
            this.parentGuiScreen,
            -1, -1,
            "Enable " + CustomCrosshairMod.TITLE,
            CustomCrosshairMod.INSTANCE.properties().getIsModEnabled().get());
        isModEnabledCheckbox.bind(CustomCrosshairMod.INSTANCE.properties().getIsModEnabled());

        EnumSliderGuiComponent<CrosshairStyle> crosshairStyleSlider = new EnumSliderGuiComponent<CrosshairStyle>(
            this.parentGuiScreen,
            -1, -1,
            50,
            "Crosshair Style",
            crosshair.style.get());
        crosshairStyleSlider.bind(crosshair.style);

        CheckBoxGuiComponent isKeepDebugEnabledCheckbox = new CheckBoxGuiComponent(
            this.parentGuiScreen,
            -1, -1,
            "Keep Default Debug Crosshair When HUD Is Visible",
            crosshair.isKeepDebugEnabled.get());
        isKeepDebugEnabledCheckbox.bind(crosshair.isKeepDebugEnabled);

        ButtonGuiComponent drawCrosshairButton = new ButtonGuiComponent(this.parentGuiScreen, -1, -1, 90, 15, "Draw Crosshair");
        drawCrosshairButton.addEvent(IOnClickEvent.class, new IOnClickEvent() {
            @Override
            public void invoke() {
                Minecraft.getMinecraft().displayGuiScreen(new DrawCrosshairGuiScreen());
            }
        });

        this.addComponent(heading);
        this.addComponent(isModEnabledCheckbox);
        this.addComponent(crosshairStyleSlider);
        this.addComponent(isKeepDebugEnabledCheckbox);
        this.addComponent(drawCrosshairButton);
        this.pack();
    }
}
