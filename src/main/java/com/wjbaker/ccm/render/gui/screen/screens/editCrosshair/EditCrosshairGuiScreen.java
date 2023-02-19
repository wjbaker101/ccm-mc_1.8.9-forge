package com.wjbaker.ccm.render.gui.screen.screens.editCrosshair;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.render.gui.component.components.ButtonGuiComponent;
import com.wjbaker.ccm.render.gui.component.components.ScrollPanelGuiComponent;
import com.wjbaker.ccm.render.gui.component.custom.CrosshairPreviewGuiComponent;
import com.wjbaker.ccm.render.gui.component.event.IOnClickEvent;
import com.wjbaker.ccm.render.gui.screen.GuiScreen;
import com.wjbaker.ccm.render.gui.screen.screens.editCrosshair.components.*;

public final class EditCrosshairGuiScreen extends GuiScreen {

    private final ScrollPanelGuiComponent mainPanel;
    private final CrosshairPreviewGuiComponent crosshairPreviewPanel;
    private final ButtonGuiComponent resetButton;

    private final int panelWidth;

    public EditCrosshairGuiScreen() {
        super("Edit Crosshair");

        this.panelWidth = 300;

        this.mainPanel = new ScrollPanelGuiComponent(this, 0, this.headerHeight + 1, 1000, 1000);
        this.buildComponents();
        this.components.add(mainPanel);

        this.crosshairPreviewPanel = new CrosshairPreviewGuiComponent(
            this,
            -1, -1,
            CustomCrosshairMod.INSTANCE.properties().getCrosshair());

        this.resetButton = new ButtonGuiComponent(this, -1, -1, 80, 15, "Reset Settings");
        this.resetButton.addEvent(IOnClickEvent.class, new IOnClickEvent() {
            @Override
            public void invoke() {
                CustomCrosshairMod.INSTANCE.properties().getCrosshair().resetProperties();
                buildComponents();
            }
        });
    }

    private void buildComponents() {
        GeneralSettingsGuiPanel generalSettingsPanel = new GeneralSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        ShapeSettingsGuiPanel shapeSettingsPanel = new ShapeSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        VisibilitySettingsGuiPanel visibilitySettingsPanel = new VisibilitySettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        OutlineSettingsGuiPanel outlineSettingsPanel = new OutlineSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        DotSettingsGuiPanel dotSettingsPanel = new DotSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        DynamicSettingsGuiPanel dynamicSettingsPanel = new DynamicSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        HighlightSettingsGuiPanel highlightSettingsPanel = new HighlightSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);
        RainbowSettingsGuiPanel rainbowSettingsPanel = new RainbowSettingsGuiPanel(this, -1, -1, this.panelWidth, -1);

        this.mainPanel.clearComponents();
        this.mainPanel.addComponent(generalSettingsPanel);
        this.mainPanel.addComponent(shapeSettingsPanel);
        this.mainPanel.addComponent(visibilitySettingsPanel);
        this.mainPanel.addComponent(outlineSettingsPanel);
        this.mainPanel.addComponent(dotSettingsPanel);
        this.mainPanel.addComponent(dynamicSettingsPanel);
        this.mainPanel.addComponent(highlightSettingsPanel);
        this.mainPanel.addComponent(rainbowSettingsPanel);
        this.mainPanel.pack();
    }

    @Override
    public void update() {
        super.update();

        this.mainPanel.setSize(this.width - 1, this.height - this.headerHeight - 1);

        if (this.width > this.panelWidth + this.crosshairPreviewPanel.getWidth() + 50) {
            int x = this.panelWidth + ((this.width - this.panelWidth) / 2) + 15 - (this.crosshairPreviewPanel.getWidth() / 2);
            int y = this.headerHeight + ((this.height - this.headerHeight) / 2) + 7 - (this.crosshairPreviewPanel.getHeight() / 2);

            this.crosshairPreviewPanel.setPosition(x, y);

            this.resetButton.setPosition(
                x + this.crosshairPreviewPanel.getWidth() - this.resetButton.getWidth(),
                y + this.crosshairPreviewPanel.getHeight() + 7);
        }
        else {
            int x = this.width - this.crosshairPreviewPanel.getWidth() - 20;
            int y = this.headerHeight + 7;

            this.crosshairPreviewPanel.setPosition(x, y);

            this.resetButton.setPosition(
                x + this.crosshairPreviewPanel.getWidth() - this.resetButton.getWidth(),
                y + this.crosshairPreviewPanel.getHeight() + 7);
        }
    }

    @Override
    public void draw() {
        super.draw();

        this.crosshairPreviewPanel.draw();
        this.resetButton.draw();
    }

    @Override
    public void onMouseMove(int mouseX, int mouseY) {
        super.onMouseMove(mouseX, mouseY);
        this.resetButton.onMouseMove(mouseX, mouseY);
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY, int button) {
        super.onMouseDown(mouseX, mouseY, button);
        this.resetButton.onMouseDown(mouseX, mouseY, button);
    }

    @Override
    public void onMouseUp(int mouseX, int mouseY, int button) {
        super.onMouseUp(mouseX, mouseY, button);
        this.resetButton.onMouseUp(mouseX, mouseY, button);
    }
}
