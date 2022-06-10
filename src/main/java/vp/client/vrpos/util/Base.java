package vp.client.vrpos.util;

import vp.client.vrpos.Vrpos;
import vp.client.vrpos.manager.EventManager;
import vp.client.vrpos.manager.FontManager;
import vp.client.vrpos.manager.ModuleManager;

public class Base implements Util{
    public static ModuleManager moduleManager = Vrpos.moduleManager;

    public static boolean nullCheck(){
        return mc.player == null || mc.world == null;
    }
}
