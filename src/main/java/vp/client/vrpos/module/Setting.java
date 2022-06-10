package vp.client.vrpos.module;

import vp.client.vrpos.util.EnumUtil;

import java.util.function.Predicate;

public class Setting<T> {
    public String name;
    public T value;
    public T maxValue , minValue;
    public Predicate<T> visible;

    public Setting(String name , T value){
        this.name = name;
        this.value = value;
        this.visible = null;
    }

    public Setting(String name , T value , T maxValue , T minValue){
        this.name = name;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.visible = null;
    }

    public Setting(String name , T value , Predicate<T> visible){
        this.name = name;
        this.value = value;
        this.visible = visible;
    }

    public Setting(String name , T value , T maxValue , T minValue , Predicate<T> visible){
        this.name = name;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.visible = visible;
    }

    public T getValue(){
        return value;
    }

    public T setValue(T value){
        return this.value = value;
    }

    public boolean isVisible(){
        if(visible == null) return true;
        return visible.test(this.value);
    }

    public int currentEnum() {
        return EnumUtil.currentEnum((Enum) this.value);
    }
    public void increaseEnum() {
        this.value = (T) EnumUtil.increaseEnum((Enum) this.value);
    }
    public void setEnum(int index) {
        this.value = (T) EnumUtil.setEnum((Enum) this.value , index);
    }
}
