package vp.client.vrpos.module;

import com.google.gson.Gson;
import com.mojang.realmsclient.gui.ChatFormatting;
import joptsimple.util.EnumConverter;
import vp.client.vrpos.event.events.PacketEvent;
import vp.client.vrpos.util.Base;
import vp.client.vrpos.util.ChatMessage;
import vp.client.vrpos.util.EnumUtil;
import vp.client.vrpos.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Module extends Base {
    public String name;
    public Category category;
    public int bind;
    public boolean enable;
    public List<Setting> settings = new ArrayList<>();

    public Module(String name , Category category){
        this.name = name;
        this.category = category;
        this.enable = false;
    }

    public Module(String name , Category category , boolean status){
        this.name = name;
        this.category = category;
        this.enable = status;
    }

    public Module(String name , Category category , int bind){
        this.name = name;
        this.category = category;
        this.bind = bind;
        this.enable = false;
    }

    public Module(String name , Category category , int bind , boolean status){
        this.name = name;
        this.category = category;
        this.bind = bind;
        this.enable = status;
    }

    public Setting register(Setting setting){
        settings.add(setting);
        return setting;
    }

    //event
    public void onEnable(){
    }

    public void onDisable(){
    }

    public void onTick(){
    }

    public void onUpdate(){
    }

    public void onKeyInput(int key){
    }

    public void onPacketSend(PacketEvent.Send event){
    }

    public void onPacketReceive(PacketEvent.Receive event){
    }

    //methods
    public void enable(){
        if(!this.enable) {
            onEnable();
            sendMessage(name + " : " + ChatFormatting.GREEN + "Enabled");
        }
        this.enable = true;
    }

    public void disable() {
        if (this.enable) {
            onDisable();
            sendMessage(name + " : " + ChatFormatting.RED + "Disabled");
        }
        this.enable = false;
    }

    public void toggle(){
        this.enable = !this.enable;
        if(this.enable) {
            onEnable();
            sendMessage(name + " : " + ChatFormatting.GREEN + "Enabled");
        }
        if(!this.enable) {
            onDisable();
            sendMessage(name + " : " + ChatFormatting.RED + "Disabled");
        }
    }

    public void bindCheck(int key){
        if(this.bind == -1) return;
        if(this.bind == key) toggle();
    }

    public void sendMessage(String msg){
        mc.player.sendMessage(new ChatMessage(ChatFormatting.AQUA + "<" + modName + "> " + ChatFormatting.GRAY + msg));
    }

    public void sendMessageWithName(String msg){
        mc.player.sendMessage(new ChatMessage(ChatFormatting.AQUA + "<" + modName + "> " + ChatFormatting.RED + "[" + this.name + "] " + ChatFormatting.GRAY + msg));
    }


    public void saveConfig() throws IOException
    {
        Gson gson = new Gson();
        Map<String , Object> mappedSettings = new HashMap<String , Object>();
        for(Setting s : settings)
        {
            if(s.value instanceof Enum)
            {
                mappedSettings.put(s.name , EnumUtil.currentEnum((Enum)s.value) + "N");
                continue;
            }
            if(s.value instanceof Integer)
            {
                mappedSettings.put(s.name , (Integer)s.value + "I");
                continue;
            }
            mappedSettings.put(s.name , s.value);
        }
        Map<String , Object> moduleConfig = new HashMap<>();
        moduleConfig.put("enable" , enable);
        moduleConfig.put("bind" , bind);
        moduleConfig.put("setting" , mappedSettings);

        String json = gson.toJson(moduleConfig);
        File config = new File("vrpos/" + category.name().toLowerCase() + "/" + name.toLowerCase() + ".json");
        FileWriter writer = new FileWriter(config);
        writer.write(json);
        writer.close();
    }

    public void loadConfig() throws Exception
    {
        Path path = Paths.get("vrpos/" + category.name().toLowerCase() + "/" + name.toLowerCase() + ".json");
        if(!Files.exists(path))
        {
            return;
        }
        String context = readAll(path);
        Gson gson = new Gson();
        Map<String , Object> moduleConfig = gson.fromJson(context, Map.class);
        enable = (Boolean) moduleConfig.get("enable");
        bind = ((Double) moduleConfig.get("bind")).intValue();
        Map<String , Object> mappedSettings = (Map<String, Object>) moduleConfig.get("setting");
        mappedSettings.forEach(this::setConfig);
    }

    public void setConfig(String name , Object value)
    {
        List<Setting> settings = new ArrayList<>(this.settings);
        for (int i = 0; i < settings.size(); i++)
        {
            Setting setting = settings.get(i);
            String n = setting.name;
            if(Objects.equals(n, name))
            {
                char c = value.toString().charAt(value.toString().length() - 1);
                if(c == 'N')
                {
                    String enumValue = value.toString().replace("N" , "");
                    setting.setEnum(Integer.parseInt(enumValue));
                    continue;
                }
                if(c == 'I')
                {
                    String intValue = value.toString().replace("I" , "");
                    setting.setValue((Double.valueOf(intValue)).intValue());
                    continue;
                }
                setting.setValue(value);
            }
        }
        this.settings = new ArrayList<>(settings);
    }

    public static String readAll(Path path) throws IOException {
        return Files.lines(path)
                .reduce("", (prev, line) ->
                        prev + line + System.getProperty("line.separator"));
    }
}
