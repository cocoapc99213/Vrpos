package vp.client.vrpos.manager;

import vp.client.vrpos.event.events.PacketEvent;
import vp.client.vrpos.module.Category;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.module.combat.CrystalAura;
import vp.client.vrpos.module.misc.FakePlayer;
import vp.client.vrpos.module.render.ClickGui;
import vp.client.vrpos.module.render.NoRender;
import vp.client.vrpos.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModuleManager implements Util {
    public List<Module> modules = new ArrayList<>();

    public void init(){
        //combat
        registerModule(new CrystalAura());
        //misc
        registerModule(new FakePlayer());
        //render
        registerModule(new ClickGui());
        registerModule(new NoRender());
    }

    public void onTick(){ callEvent(Module::onTick); }

    public void onUpdate(){
        callEvent(Module::onUpdate);
    }

    public void onKeyInput(int key){
        modules.forEach(m -> m.bindCheck(key));
        callEvent(m -> m.onKeyInput(key));
    }

    public void onPacketSend(PacketEvent.Send event){
        callEvent(m -> m.onPacketSend(event));
    }

    public void onPacketReceive(PacketEvent.Receive event){
        callEvent(m -> m.onPacketReceive(event));
    }

    public void callEvent(Consumer<? super Module> event){
        modules.stream().filter(m -> m.enable).forEach(event);
    }

    public void registerModule(Module module){
        modules.add(module);
    }

    public List<Module> getModulesWithCategory(Category category){
        List<Module> r = new ArrayList<>();
        for(Module m : modules){
            if(m.category == category) r.add(m);
        }
        return r;
    }

    public Module getModuleWithClass(Class clazz){
        Module r = null;
        for(Module m : modules){
            if(m.getClass() == clazz) r = m;
        }
        return r;
    }
}
