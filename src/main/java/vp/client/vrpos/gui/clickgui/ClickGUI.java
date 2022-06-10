package vp.client.vrpos.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import scala.Char;
import vp.client.vrpos.module.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClickGUI extends GuiScreen {
    public static List<Panel> panelList = new ArrayList<>();

    @Override
    public void initGui(){
        if(panelList.size() == 0){
            float x = 30;
            for(Category category : Category.values()){
                panelList.add(new Panel(x , 30 , category));
                x += 117;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        scroll();
        callEvent(p -> p.drawScreen(mouseX , mouseY , partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX , int mouseY , int mouseButton){
        callEvent(p -> p.onMouseClicked(mouseX , mouseY , mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX , int mouseY , int state){
        callEvent(p -> p.onMouseReleased(mouseX , mouseY , state));
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton , long aaa){
        callEvent(p -> p.onMouseClickMove(mouseX , mouseY , clickedMouseButton));
    }

    @Override
    public void keyTyped(char typedChar , int key){
        if(key == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(null);
        callEvent(p -> p.onKeyTyped(typedChar , key));
    }

    public void scroll()
    {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0)
            callEvent(p -> p.y -= 15);
        else if (dWheel > 0)
            callEvent(p -> p.y += 15);
    }

    public void callEvent(Consumer<? super Panel> event){
        panelList.forEach(event);
    }

}
