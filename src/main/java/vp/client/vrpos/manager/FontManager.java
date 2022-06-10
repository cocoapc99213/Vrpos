package vp.client.vrpos.manager;

import vp.client.vrpos.gui.font.CFont;
import vp.client.vrpos.gui.font.CFontRenderer;
import vp.client.vrpos.util.Base;

import java.awt.*;

public class FontManager {
    public static CFontRenderer fontRenderer;

    public void init(){
        fontRenderer = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Comfortaa-Regular.ttf", 18f, Font.PLAIN), true, false);
    }

    public int getWidth(String str){
        return fontRenderer.getStringWidth(str);
    }

    public int getHeight(){
        return fontRenderer.getHeight() + 2;
    }

    public void draw(String str, int x, int y, int color) {
        fontRenderer.drawString(str, x, y, color);
    }

    public void draw(String str, int x, int y, Color color) {
        fontRenderer.drawString(str, x, y, color.getRGB());
    }
}
