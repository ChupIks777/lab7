package commands;


import exceptions.NumberOfArgsException;
import utils.CmdData;
import utils.ConsolePrinter;
import client.Client;
import java.util.ArrayList;
import java.util.List;

public class RemoveByKey extends Command {
    public RemoveByKey() {
        super("remove_key key", "remove element with given key");
    }

    @Override
    public CmdData validate(String arg){
        try {
            if (arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
            int key = Integer.parseInt(arg);
            List<Object> args = new ArrayList<>();
            args.add(key);
            CmdData cmdData = new CmdData(Client.token, "remove_key", args);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("you must insert one integer arg");
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("incorrect key");
        }
        return null;
    }
}
