package commands;

import managers.CommandManager;
import utils.CmdDataFromServer;

import java.util.List;
import java.util.ArrayList;
public class Help extends Command{

    public Help() {
        super("help", "display list of commands with descriptions");
    }


    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        List<Object> answer = new ArrayList<>();
        for (List<Object> command : CommandManager.getCommandMap().values()) {
            answer.add(command.get(0).toString());
        }
        return new CmdDataFromServer("help", answer);
    }

    @Override
    public String toString(){
        return "help - display list of commands with descriptions";
    }
}
