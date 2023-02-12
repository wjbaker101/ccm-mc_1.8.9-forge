package com.wjbaker.ccm.render.gui.screen;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.wjbaker.ccm.CustomCrosshairMod;
import com.wjbaker.ccm.helper.ExternalHelper;
import com.wjbaker.ccm.render.ModTheme;
import com.wjbaker.ccm.render.RenderManager;
import com.wjbaker.ccm.render.gui.component.GuiComponent;
import com.wjbaker.ccm.render.gui.component.components.ButtonGuiComponent;
import com.wjbaker.ccm.render.gui.component.event.IOnClickEvent;

public abstract class GuiScreen extends GuiScreenAdapter {

    protected final RenderManager renderManager;

    protected final GuiScreen parentGuiScreen;
    protected final List<GuiComponent> components;
    protected final int headerHeight;

    private final ButtonGuiComponent newVersionButton;

    public GuiScreen(final String title) {
        this(title, null);
    }

    public GuiScreen(final String title, final GuiScreen parentGuiScreen) {
        super(title);

        this.renderManager = new RenderManager();

        this.parentGuiScreen = parentGuiScreen;
        this.components = new ArrayList<GuiComponent>();
        this.headerHeight = 35;

        this.newVersionButton = new ButtonGuiComponent(this, -1, -1, 125, 25, "New Version Available!");
        this.newVersionButton.setBaseBackgroundColour(ModTheme.TERTIARY);
        this.newVersionButton.setHoverBackgroundColour(ModTheme.TERTIARY_DARK);
        this.newVersionButton.setBaseTextColour(ModTheme.BLACK);
        this.newVersionButton.setHoverTextColour(ModTheme.BLACK);
        this.newVersionButton.addEvent(IOnClickEvent.class, new IOnClickEvent() {
			@Override
			public void invoke() {
			    if (!CustomCrosshairMod.INSTANCE.properties().isLatestVersion().get())
			        new ExternalHelper().openInBrowser(CustomCrosshairMod.CURSEFORGE_PAGE);
			}
		});
    }

    @Override
    public void update() {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.update();
			}
		});

        this.newVersionButton.setPosition(
            this.width - this.newVersionButton.getWidth() - 5,
            (this.headerHeight / 2) - (this.newVersionButton.getHeight() / 2));
    }

    @Override
    public void draw() {
        this.renderManager.drawFilledRectangle(0, 0, this.width, this.height, ModTheme.BLACK.setOpacity(140));

        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.draw();
			}
		});

        this.drawHeader();
    }

    private void drawHeader() {
        this.renderManager.drawFilledRectangle(0, 0, this.width, this.headerHeight, ModTheme.PRIMARY);
        this.renderManager.drawLine(0, this.headerHeight, this.width, this.headerHeight, 2.0F, ModTheme.DARK_GREY);

        int titleWidth = this.renderManager.textWidth(CustomCrosshairMod.TITLE);
        int centreY = (this.headerHeight / 2) - (7 / 2);

        this.renderManager.drawText(CustomCrosshairMod.TITLE, 5, centreY, ModTheme.WHITE, true);

        this.renderManager.drawSmallText(
            "v" + CustomCrosshairMod.VERSION,
            8 + titleWidth,
            (headerHeight / 2),
            ModTheme.DARK_GREY,
            false);

        if (!CustomCrosshairMod.INSTANCE.properties().isLatestVersion().get())
            this.newVersionButton.draw();
    }

    @Override
    public void onMouseDown(final int mouseX, final int mouseY, final int button) {
        this.components
            .stream()
            .filter(new Predicate<GuiComponent>() {
				@Override
				public boolean test(GuiComponent x) {
					return x.isInsideComponent(mouseX, mouseY);
				}
			})
            .forEach(new Consumer<GuiComponent>() {
				@Override
				public void accept(GuiComponent x) {
					x.onMouseDown(mouseX, mouseY, button);
				}
			});

        this.newVersionButton.onMouseDown(mouseX, mouseY, button);
    }

    @Override
    public void onMouseUp(final int mouseX, final int mouseY, final int button) {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseUp(mouseX, mouseY, button);
			}
		});

        this.newVersionButton.onMouseUp(mouseX, mouseY, button);
    }

    @Override
    public void onMouseMove(final int mouseX, final int mouseY) {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseMove(mouseX, mouseY);
			}
		});

        this.newVersionButton.onMouseMove(mouseX, mouseY);
    }

    @Override
    public void onMouseDrag(final int startX, final int startY, final int mouseX, final int mouseY) {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseDrag(startX, startY, mouseX, mouseY);
			}
		});
    }

    @Override
    public void onMouseScrollUp() {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseScrollUp();
			}
		});
    }

    @Override
    public void onMouseScrollDown() {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseScrollDown();
			}
		});
    }

    @Override
    public void onKeyDown(final int keyCode) {
        if (keyCode == 256 && this.parentGuiScreen != null)
            Minecraft.getMinecraft().displayGuiScreen(this.parentGuiScreen);
    }

    @Override
    public void onKeyUp(final int keyCode) {}

    @Override
    public void close() {
        CustomCrosshairMod.INSTANCE.configManager().write();
    }
}
