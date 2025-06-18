package commands;

import java.util.List;

import utils.CmdDataFromServer;

public abstract class Command {

    public Command(String name, String desc){
    }


    public abstract CmdDataFromServer execute(String username, List<Object> args);
}