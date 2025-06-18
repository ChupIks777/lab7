package commands;


import exceptions.NumberOfArgsException;
import utils.ConsolePrinter;
import client.*;

public class Exit {

    Client client;
    public Exit(Client client) {
        this.client = client;
    }
    
    public void execute(String arg){
        try {
            if (!arg.isEmpty()) {
                throw new NumberOfArgsException();
            }
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("too many args");
        }
    }
}
