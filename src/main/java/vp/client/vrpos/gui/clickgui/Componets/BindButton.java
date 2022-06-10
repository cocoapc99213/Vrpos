package vp.client.vrpos.gui.clickgui.Componets;

import org.lwjgl.input.Keyboard;
import vp.client.vrpos.gui.clickgui.Component;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.util.ColorUtil;
import vp.client.vrpos.util.MouseUtil;
import vp.client.vrpos.util.RenderUtil;

import java.awt.*;

public class BindButton extends Component {
    public Module module;
    public boolean keyWaiting;

    public BindButton(Module module){
        this.module = module;
        keyWaiting = false;
        this.width = 100;
        this.height = 17;
    }

    @Override
    public Float drawScreen(int mouseX, int mouseY, float x , float y) {
        this.x = x;
        this.y = y;
        RenderUtil.drawRect(x , y , width , height , ColorUtil.toRGBA(new Color(70,90,100 , 210)));
        if(keyWaiting)
            RenderUtil.drawRect(x + 1 , y + 1, width + (-1 * 2) , height + (-1 * 2) , enable);
        if(isMouseHovering(mouseX , mouseY))
            RenderUtil.drawRect(x + 1 , y + 1, width + (-1 * 2) , height + (-1 * 2) , hovering);

        float keyY = RenderUtil.getCenter(y , height , RenderUtil.getStringHeight());
        String str = "";

        if(module.bind == Keyboard.KEY_NONE || module.bind == -1){
            str = "Bind : None";
        }
        else{
            str = "Bind : " + Keyboard.getKeyName(module.bind);
        }

        if(keyWaiting) {
            str = "Bind : ...";
        }

        RenderUtil.drawString(str
                , x + 5, keyY,  ColorUtil.toRGBA(new Color(255 , 255 , 255)) , true);

        return height;
    }

    @Override
    public void onMouseClicked(int mouseX , int mouseY , int mouseButton){
        if(isMouseHovering(mouseX , mouseY) && mouseButton == MouseUtil.MOUSE_LEFT) keyWaiting = !keyWaiting;
    }

    @Override
    public void onKeyTyped(char typedChar , int key){
        if(keyWaiting){
            if(key == Keyboard.KEY_BACK) this.module.bind = -1;
            else this.module.bind = key;
        }
        keyWaiting = false;
    }
}
