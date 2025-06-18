package commands;


import exceptions.NumberOfArgsException;
import utils.ConsolePrinter;
import utils.CmdData;
import java.util.ArrayList;
import java.util.List;
import client.Client;

public class Clear extends Command{

    public Clear() {
        super("clear", "clear collection");
    }
    @Override
    public CmdData validate(String arg){
        try {
            if (!arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
            List<Object> args = new ArrayList<>();
            CmdData cmdData = new CmdData(Client.token, "clear", args);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("too many args");
        }
        return null;
    }
}
