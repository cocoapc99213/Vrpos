package vp.client.vrpos.gui.clickgui.Componets;

import vp.client.vrpos.gui.clickgui.Component;
import vp.client.vrpos.module.Setting;
import vp.client.vrpos.util.ColorUtil;
import vp.client.vrpos.util.MouseUtil;
import vp.client.vrpos.util.RenderUtil;

import java.awt.*;

public class IntegerSlider extends Component {
    public Setting<Integer> setting;
    public float ratio;
    public boolean changing;

    public IntegerSlider(Setting setting){
        this.setting = setting;
        this.width = 100;
        this.height = 17;
        this.ratio = getRatio(this.setting.getValue() , this.setting.maxValue , this.setting.minValue);
        this.changing = false;
    }

    @Override
    public Float drawScreen(int mouseX, int mouseY, float x , float y) {
        boolean visible = setting.isVisible();
        if(visible){
            this.x = x;
            this.y = y;
            RenderUtil.drawRect(x , y , width , height , ColorUtil.toRGBA(new Color(70,90,100 , 210)));
            RenderUtil.drawRect(x + 1 , y + 1, ((width + (-1 * 2)) * ratio) , height + (-1 * 2) , enable);
            float nameY = RenderUtil.getCenter(y , height , RenderUtil.getStringHeight());
            RenderUtil.drawString(setting.name + " : " + setting.getValue()
                    , x + 5, nameY,  ColorUtil.toRGBA(new Color(255 , 255 , 255)) , true);

            return height;
        }
        return 0.0F;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isMouseHovering(mouseX , mouseY) && mouseButton == MouseUtil.MOUSE_LEFT){
            setValue(mouseX);
            changing = true;
        }
    }

    @Override
    public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton){
        if(changing && clickedMouseButton == MouseUtil.MOUSE_LEFT) setValue(mouseX);
    }

    @Override
    public void onMouseReleased(int mouseX , int mouseY , int state){
        changing = false;
    }

    public void setValue(float mouseX) {
        float v = (mouseX - this.x) / (width);
        if(v > 1.0F) v = 1.0F;
        if(v < 0.0F) v = 0.0F;
        this.ratio = v;
        float newValue = ((setting.maxValue - setting.minValue) * ratio) + setting.minValue;
        setting.setValue(Math.round(newValue));
    }

    public float getRatio(float value , float max , float min){
        return (value - min) / (max - min);
    }
}
