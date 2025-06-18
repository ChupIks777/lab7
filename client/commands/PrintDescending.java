package commands;


import exceptions.NumberOfArgsException;
import utils.CmdData;
import utils.ConsolePrinter;
import client.Client;
import java.util.ArrayList;
import java.util.List;

public class PrintDescending extends Command{
    public PrintDescending() {
        super("print_descending", "display elements in descending order");
    }
    @Override
    public CmdData validate(String arg){
        try {   
            if (!arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
            List<Object> args = new ArrayList<>();
            CmdData cmdData = new CmdData(Client.token, "print_descending", args);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("too many args");
        }
        return null;
    }
}
