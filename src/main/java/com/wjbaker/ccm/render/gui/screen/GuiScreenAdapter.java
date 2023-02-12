package com.wjbaker.ccm.render.gui.screen;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class GuiScreenAdapter extends net.minecraft.client.gui.GuiScreen implements IGuiScreen {
    
    private int prevMouseX;
    private int prevMouseY;

    protected GuiScreenAdapter(final String title) {
        super();
        
        this.prevMouseX = Integer.MAX_VALUE;
        this.prevMouseY = Integer.MAX_VALUE;
    }

    @Override
    public void updateScreen() {
        this.update();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        this.close();
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) throws IOException {
        this.onMouseDown((int)mouseX, (int)mouseY, button);
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int button) {
        this.onMouseUp((int)mouseX, (int)mouseY, button);
        super.mouseReleased(mouseX, mouseY, button);
    }
    
    public void handleMouseInput() throws IOException {
    	int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
    	int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
    	
        if (mouseX != this.prevMouseX || mouseY != this.prevMouseY) {
        	this.onMouseMove(mouseX, mouseY);
        	
        	this.prevMouseX = mouseX;
        	this.prevMouseY = mouseY;
        }
        
        if (Keyboard.getEventKeyState()) {
        	this.onKeyDown(Keyboard.getEventKey());
        }
        else {
        	this.onKeyUp(Keyboard.getEventKey());
        }
        
        int scrollAmount = Mouse.getEventDWheel();
        if (Mouse.hasWheel() && scrollAmount != 0) {
            if (scrollAmount > 0)
                this.onMouseScrollUp();
            else
                this.onMouseScrollDown();
        }
        
    	super.handleMouseInput();
    }

    @Override
    public void mouseClickMove(
        final int mouseX,
        final int mouseY,
        final int button,
        final long dragTime) {
    	
    	int scaledMouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
    	int scaledMouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        this.onMouseDrag(mouseX, mouseY, scaledMouseX, scaledMouseY);
        super.mouseClickMove(mouseX, mouseY, button, 0);
    }
}
