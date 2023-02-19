package com.wjbaker.ccm.render.gui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.wjbaker.ccm.render.gui.event.IMouseEvents;

public abstract class GuiComponentWithComponents extends GuiComponentTheme implements IMouseEvents {

    protected List<GuiComponent> components = new ArrayList<GuiComponent>();

    public void draw() {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.draw();
			}
		});
    }

	public void clearComponents() {
		this.components.clear();
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
    }

    @Override
    public void onMouseUp(final int mouseX, final int mouseY, final int button) {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseUp(mouseX, mouseY, button);
			}
		});
    }

    @Override
    public void onMouseMove(final int mouseX, final int mouseY) {
        this.components.forEach(new Consumer<GuiComponent>() {
			@Override
			public void accept(GuiComponent x) {
				x.onMouseMove(mouseX, mouseY);
			}
		});
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
}
