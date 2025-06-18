package commands;

import exceptions.NumberOfArgsException;
import utils.CmdData;
import utils.ConsolePrinter;
import client.Client;
import java.util.ArrayList;
import java.util.List;

public class Help extends Command{

    public Help() {
        super("help", "display list of commands with descriptions");
    }
    @Override
    public CmdData validate(String arg){
        try {
            if (!arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
            List<Object> args = new ArrayList<>();
            CmdData cmdData = new CmdData(Client.token, "help", args);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("too many args");
        }
        return null;
    }
}
