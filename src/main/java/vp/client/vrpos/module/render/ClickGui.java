package vp.client.vrpos.module.render;

import org.lwjgl.input.Keyboard;
import vp.client.vrpos.gui.clickgui.ClickGUI;
import vp.client.vrpos.module.Category;
import vp.client.vrpos.module.Module;

public class ClickGui extends Module {
    public ClickGui(){
        super("ClickGui" , Category.RENDER , Keyboard.KEY_Y);
    }

    @Override
    public void onEnable(){
        if(!(mc.currentScreen instanceof ClickGUI)) mc.displayGuiScreen(new ClickGUI());
    }

    @Override
    public void onUpdate(){
        if(!(mc.currentScreen instanceof ClickGUI)) disable();
    }
}
