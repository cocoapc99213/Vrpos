package vp.client.vrpos.command;

import vp.client.vrpos.util.Base;

public class Command extends Base {
    public String name , desc;

    public Command(String name , String desc){
        this.name = name;
        this.desc = desc;
    }

    public void execute(String[] args){
    }
}
