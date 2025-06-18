package commands;

import exceptions.NumberOfArgsException;
import client.Client;
import utils.CmdData;
import utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;

public class FilterGreaterThanHeight extends Command{
    public FilterGreaterThanHeight() {
        super("filter_greater_than_height height", "display every element with height greater than given");
    }
    @Override
    public CmdData validate(String arg){
        try {
            if (arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
            Float.parseFloat(arg);
            List<Object> args = new ArrayList<>();
            args.add(arg);
            CmdData cmdData = new CmdData(Client.token, "filter_greater_than_height", args);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("you must insert one integer arg");
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("incorrect height");
        }
        return null;
    }
}
