package commands;

import utils.CmdData;
public abstract class Command {

    public Command(String name, String desc){
    }

    public abstract CmdData validate(String arg);
}