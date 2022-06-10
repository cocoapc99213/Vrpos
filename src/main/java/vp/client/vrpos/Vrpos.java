package vp.client.vrpos;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import vp.client.vrpos.manager.ConfigManager;
import vp.client.vrpos.manager.EventManager;
import vp.client.vrpos.manager.FontManager;
import vp.client.vrpos.manager.ModuleManager;

@Mod(modid = Vrpos.modID , name = Vrpos.name , version = Vrpos.version)
public class Vrpos {
    public static final String modID = "vrpos";
    public static final String name = "Vrpos";
    public static final String version = "0.1.1";

    public static ModuleManager moduleManager = new ModuleManager();
    public static EventManager eventManager = new EventManager();
    public static FontManager fontManager = new FontManager();
    public static ConfigManager configManager = new ConfigManager();

    public static Vrpos INSTANCE;
    public static Logger logger = LogManager.getLogger(name);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        info("Loading Vrpos client");
        moduleManager.init();
        eventManager.init();
        fontManager.init();
        configManager.loadConfigs();
        Display.setTitle(name + " v" + version);
        INSTANCE = this;
        info("Vrpos loaded");
    }

    public void unload(){
        info("Unloading Vrpos client");
        MinecraftForge.EVENT_BUS.unregister(this);
        configManager.saveConfigs();
        info("Vrpos unloaded");
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void log(Level level , String msg)
    {
        logger.log(level , msg);
    }
}
