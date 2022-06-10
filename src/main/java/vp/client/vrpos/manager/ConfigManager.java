package vp.client.vrpos.manager;

import vp.client.vrpos.module.Category;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.util.Base;

import java.io.File;
import java.util.List;

public class ConfigManager extends Base {
    public void saveConfigs()
    {
        String folder = "vrpos/";
        File dir = new File(folder);
        if(!dir.exists()) dir.mkdirs();
        for(Category category : Category.values())
        {
            File categoryDir = new File(folder + category.name().toLowerCase());
            if(!categoryDir.exists()) categoryDir.mkdirs();
        }
        List<Module> modules = moduleManager.modules;
        for(Module module : modules)
        {
            try {
                module.saveConfig();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadConfigs()
    {
        List<Module> modules = moduleManager.modules;
        for(Module module : modules)
        {
            try {
                module.loadConfig();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
