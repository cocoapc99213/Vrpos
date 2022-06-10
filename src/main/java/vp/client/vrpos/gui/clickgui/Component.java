package vp.client.vrpos.gui.clickgui;

import vp.client.vrpos.util.ColorUtil;

import java.awt.*;

public class Component {
    public float x, y, width, height;
    public int base , enable , hovering;

    public Component() {
        base = ColorUtil.toRGBA(new Color(70,90,100 , 210));
        enable = ColorUtil.toRGBA(new Color(100,120,190 , 210));
        hovering = ColorUtil.toRGBA(new Color(190,190,190 , 100));
    }

    public Float drawScreen(int mouseX, int mouseY, float x, float y) {
        return 0.0F;
    }

    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void onMouseReleased(int mouseX, int mouseY, int state) {
    }

    public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
    }

    public void onKeyTyped(char typedChar , int key){
    }

    public Boolean isMouseHovering(int mouseX , int mouseY)
    {
        if(x < mouseX && x + width > mouseX)
            return y < mouseY && y + height > mouseY;
        return false;
    }
}