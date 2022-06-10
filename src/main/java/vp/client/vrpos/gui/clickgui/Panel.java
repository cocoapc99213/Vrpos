package vp.client.vrpos.gui.clickgui;

import vp.client.vrpos.gui.clickgui.Componets.ModuleButton;
import vp.client.vrpos.module.Category;
import vp.client.vrpos.util.Base;
import vp.client.vrpos.util.ColorUtil;
import vp.client.vrpos.util.MouseUtil;
import vp.client.vrpos.util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Panel extends Base {
    public float x, y, width, height;
    public Category category;
    public List<ModuleButton> buttonList = new ArrayList<>();
    public boolean open;

    public Panel(float x , float y , Category category){
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 19;
        open = true;
        this.category = category;
        moduleManager.getModulesWithCategory(category)
                .forEach(m -> buttonList.add(new ModuleButton(m)));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x , y , width , height , ColorUtil.toRGBA(new Color(50,60,90 , 210)));
        float nameX = RenderUtil.getCenter(x, width , RenderUtil.getStringWidth(getName()));
        float nameY = RenderUtil.getCenter(y , height , RenderUtil.getStringHeight());
        RenderUtil.drawString(getName()
                , nameX, nameY,  ColorUtil.toRGBA(new Color(255 , 255 , 255)) , true);

        if(open){
            AtomicReference<Float> f = new AtomicReference<>(y + height);
            callEvent(m -> {
                f.updateAndGet(v -> (v + m.drawScreen(mouseX, mouseY, x , f.get())));
            });
        }
    }

    public boolean moving = false;
    public float diffx , diffy = 0.0F;

    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == MouseUtil.MOUSE_LEFT && isMouseHovering(mouseX , mouseY)){
            moving = true;
            diffx = this.x - mouseX;
            diffy = this.y - mouseY;
        }
        if(mouseButton == MouseUtil.MOUSE_RIGHT && isMouseHovering(mouseX , mouseY)){
            open = !open;
        }

        if(open) callEvent(m -> m.onMouseClicked(mouseX , mouseY , mouseButton));
    }

    public void onMouseReleased(int mouseX, int mouseY, int state) {
        moving = false;
        if(open) callEvent(m -> m.onMouseReleased(mouseX , mouseY , state));
    }

    public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton){
        if(clickedMouseButton == MouseUtil.MOUSE_LEFT && moving){
            this.x = mouseX + diffx;
            this.y = mouseY + diffy;
        }
        if(open) callEvent(m -> m.onMouseClickMove(mouseX , mouseY , clickedMouseButton));
    }

    public void onKeyTyped(char typedChar , int key){
        if(open) callEvent(m -> m.onKeyTyped(typedChar , key));
    }

    public Boolean isMouseHovering(int mouseX , int mouseY)
    {
        if(x < mouseX && x + width > mouseX)
        {
            return y < mouseY && y + height > mouseY;
        }
        return false;
    }

    public String getName(){
        String r = "";
        boolean a = false;
        for(char c : category.name().toCharArray()){
            if(!a)
                r += String.valueOf(c).toUpperCase();
            else
                r += String.valueOf(c).toLowerCase();
            a = true;
        }
        return r;
    }

    public void callEvent(Consumer<? super ModuleButton> event){
        buttonList.forEach(event);
    }
}
