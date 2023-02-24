package com.wjbaker.ccm.render.gui.screen.screens.drawCrosshair;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.crosshair.custom.CustomCrosshairDrawer;
import com.wjbaker.ccm.crosshair.property.IntegerProperty;
import com.wjbaker.ccm.render.gui.component.components.ButtonGuiComponent;
import com.wjbaker.ccm.render.gui.component.components.HeadingGuiComponent;
import com.wjbaker.ccm.render.gui.component.components.PanelGuiComponent;
import com.wjbaker.ccm.render.gui.component.event.IOnClickEvent;
import com.wjbaker.ccm.render.gui.screen.GuiScreen;
import com.wjbaker.ccm.render.gui.screen.screens.drawCrosshair.components.DrawCrosshairGuiComponent;

public final class DrawCrosshairGuiScreen extends GuiScreen {

    public DrawCrosshairGuiScreen() {
        super("Draw Crosshair");

        HeadingGuiComponent titleHeading = new HeadingGuiComponent(this, -1, -1, "Draw Crosshair");

        final CustomCrosshairDrawer customCrosshairDrawer = CustomCrosshairMod.INSTANCE.properties().getCustomCrosshairDrawer();
        IntegerProperty imageSize = new IntegerProperty("fake_image_size", customCrosshairDrawer.getWidth());
        DrawCrosshairGuiComponent drawCrosshair = new DrawCrosshairGuiComponent(this, -1, -1, imageSize);

        ButtonGuiComponent resetButton = new ButtonGuiComponent(this, -1, -1, 50, 15, "Reset");
        resetButton.addEvent(IOnClickEvent.class, new IOnClickEvent() {
            @Override
            public void invoke() {
                customCrosshairDrawer.reset(customCrosshairDrawer.getWidth(), customCrosshairDrawer.getHeight());
            }
        });

        PanelGuiComponent mainPanel = new PanelGuiComponent(this, 7, this.headerHeight + 8, 300, 300);
        mainPanel.addComponent(titleHeading);
        mainPanel.addComponent(drawCrosshair);
        mainPanel.addComponent(resetButton);
        mainPanel.pack();

        this.components.add(mainPanel);
    }

    @Override
    public void close() {
        super.close();
        CustomCrosshairMod.INSTANCE.properties().getCustomCrosshairDrawer().saveImage();
    }
}