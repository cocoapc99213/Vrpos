package vp.client.vrpos.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import vp.client.vrpos.command.Command;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.util.ChatMessage;

public class BindCommand extends Command {
    public BindCommand(){
        super("Bind" , ">bind [Module] [Key]");
    }

    @Override
    public void execute(String[] args){
        if(args.length < 3){
            mc.player.sendMessage(new ChatMessage(ChatFormatting.RED + "[ERROR] " + ChatFormatting.DARK_GRAY + "Please enter the correct syntax."));
            return;
        }
    }
}
