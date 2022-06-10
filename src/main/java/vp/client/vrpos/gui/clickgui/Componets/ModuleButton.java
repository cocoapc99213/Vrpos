package vp.client.vrpos.gui.clickgui.Componets;

import vp.client.vrpos.gui.clickgui.Component;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.module.Setting;
import vp.client.vrpos.util.ColorUtil;
import vp.client.vrpos.util.MouseUtil;
import vp.client.vrpos.util.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


public class ModuleButton extends Component {
    public List<Component> setting = new ArrayList<>();
    public Module module;
    public boolean open;

    public ModuleButton(Module module){
        this.module = module;
        this.open = false;
        this.width = 100;
        this.height = 17;
        module.settings.forEach(this::addSetting);
        setting.add(new BindButton(module));
    }

    @Override
    public Float drawScreen(int mouseX, int mouseY, float x , float y) {
        this.x = x;
        this.y = y;
        RenderUtil.drawRect(x , y , width , height , base);
        if(this.module.enable)
            RenderUtil.drawRect(x + 1 , y + 1, width + (-1 * 2) , height + (-1 * 2) , enable);
        if(isMouseHovering(mouseX , mouseY))
            RenderUtil.drawRect(x + 1 , y + 1, width + (-1 * 2) , height + (-1 * 2) , hovering);

        float nameY = RenderUtil.getCenter(y , height , RenderUtil.getStringHeight());
        RenderUtil.drawString(module.name
                , x + 3, nameY,  ColorUtil.toRGBA(new Color(255 , 255 , 255)) , true);

        AtomicReference<Float> f = new AtomicReference<>(y + height);
        if(open){
            callEvent(s -> {
                float h = s.drawScreen(mouseX , mouseY , x , f.get());
                f.updateAndGet(v -> v + h);
            });
        }

        return f.get() - y;
    }

    @Override
    public void onMouseClicked(int mouseX , int mouseY , int mouseButton){
        if(open) callEvent(s ->  s.onMouseClicked(mouseX , mouseY , mouseButton));
        if(isMouseHovering(mouseX , mouseY)){
            if(MouseUtil.MOUSE_LEFT == mouseButton) this.module.toggle();
            if(MouseUtil.MOUSE_RIGHT == mouseButton) open = !open;
        }
    }

    @Override
    public void onMouseReleased(int mouseX , int mouseY , int state){
        if(open) callEvent(s -> s.onMouseReleased(mouseX , mouseY , state));
    }

    @Override
    public void onMouseClickMove(int mouseX , int mouseY , int clickedButton){
        if(open) callEvent(s -> s.onMouseClickMove(mouseX , mouseY , clickedButton));
    }

    @Override
    public void onKeyTyped(char typedChar , int key){
        if(open) callEvent(s -> s.onKeyTyped(typedChar , key));
    }

    public void callEvent(Consumer<? super Component> event){
        setting.forEach(event);
    }

    public void addSetting(Setting setting){
        Object value = setting.getValue();
        if(value instanceof Boolean) this.setting.add(new BooleanButton(setting));
        if(value instanceof Integer) this.setting.add(new IntegerSlider(setting));
        if(value instanceof Double) this.setting.add(new DoubleSlider(setting));
        if(value instanceof Enum) this.setting.add(new EnumButton(setting));
    }
}
