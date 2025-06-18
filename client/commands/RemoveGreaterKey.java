package commands;

import exceptions.NumberOfArgsException;
import utils.CmdData;
import utils.ConsolePrinter;
import client.Client;
import java.util.ArrayList;
import java.util.List;

public class RemoveGreaterKey extends Command{
    public RemoveGreaterKey() {
        super("remove_greater_key key", "remove every element with key greater than given");
    }
    @Override
    public CmdData validate(String arg){
        try {
            if (arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
            Integer.parseInt(arg);
            List<Object> args = new ArrayList<>();
            args.add(arg);
            CmdData cmdData = new CmdData(Client.token, "remove_greater_key", args);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("you must insert one integer arg");
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("incorrect key");
        }
        return null;
        }
}
