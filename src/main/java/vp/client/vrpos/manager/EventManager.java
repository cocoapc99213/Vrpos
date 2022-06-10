package vp.client.vrpos.manager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import vp.client.vrpos.Vrpos;
import vp.client.vrpos.event.events.PacketEvent;
import vp.client.vrpos.util.Base;

public class EventManager extends Base {
    public void init(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        moduleManager.onTick();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onUpdate(LivingEvent.LivingUpdateEvent event){
        if(nullCheck()) return;
        moduleManager.onUpdate();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL , receiveCanceled = true)
    public void onInput(InputEvent.KeyInputEvent event){
        if(nullCheck()) return;
        if(Keyboard.getEventKeyState()){
            int key = Keyboard.getEventKey();
            moduleManager.onKeyInput(key);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event){
        if(nullCheck()) return;
        moduleManager.onPacketSend(event);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event){
        if(nullCheck()) return;
        moduleManager.onPacketReceive(event);
    }
}
