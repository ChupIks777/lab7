package managers;

import client.Checker;
import utils.CmdData;
import commands.*;
import exceptions.EmptyLineException;
import utils.ConsolePrinter;
import client.Client;
import java.util.*;
import java.io.IOException;
public class CommandManager {

    private static final  Map<String, List<Object>> commandMap = new HashMap<>();
    private final Checker checker;
    private final Client client;
    public CommandManager(ScannerManager scannerManager, Client client, Checker checker)
    {
        this.checker = checker;
        this.client = client;
        commandMap.put("help", new ArrayList<>(List.of(new Help(), 0)));
        commandMap.put("clear", new ArrayList<>(List.of(new Clear(), 0)));
        commandMap.put("execute_script", new ArrayList<>(List.of(new ExecuteScript(), 1)));
        commandMap.put("filter_greater_than_height", new ArrayList<>(List.of(new FilterGreaterThanHeight(), 1)));
        commandMap.put("info", new ArrayList<>(List.of(new Info(), 0)));
        commandMap.put("insert", new ArrayList<>(List.of(new InsertElement(scannerManager), 11)));
        commandMap.put("max_by_name", new ArrayList<>(List.of(new MaxByName(), 0)));
        commandMap.put("print_descending", new ArrayList<>(List.of(new PrintDescending(), 0)));
        commandMap.put("remove_key", new ArrayList<>(List.of(new RemoveByKey(), 1)));
        commandMap.put("remove_greater", new ArrayList<>(List.of(new RemoveGreater(scannerManager), 11)));
        commandMap.put("remove_greater_key", new ArrayList<>(List.of(new RemoveGreaterKey(), 1)));
        commandMap.put("replace_if_lower", new ArrayList<>(List.of(new ReplaceIfLower(scannerManager), 12)));
        commandMap.put("show", new ArrayList<>(List.of(new Show(), 0)));
        commandMap.put("update", new ArrayList<>(List.of(new UpdateByID(scannerManager), 12)));
        commandMap.put("print_H_L", new ArrayList<>(List.of(new PrintHighestAndLowest(), 0)));
        commandMap.put("exit", new ArrayList<>(List.of(new Exit(client), 0)));
    }

    public static Map<String, List<Object>> getCommandMap() {
        return commandMap;
    }


    public boolean defineCommand(String enteredCommand) {
        String[] userCommand = (enteredCommand.strip() + " ").split(" ", 2);
        String command = userCommand[0];
        String argument = userCommand[1].strip();
        try {
            ConsolePrinter.printCmd(command, argument);
            if (commandMap.containsKey(command)) {
                if (command.isEmpty()) throw new EmptyLineException();

                
                if (command.equals("exit") && argument.isEmpty()) {  
                    ConsolePrinter.print("Bye-bye");
                    client.exit();
                    return true;
                }else {
                    CmdData data = ((Command) commandMap.get(command).get(0)).validate(argument);
                    if (data != null) {
                        //System.out.println(0);
                        return checker.check(client.sendObject(data));
                    }
                    return false;

                }
            } else {
                ConsolePrinter.printError("no such command");
                return ScannerManager.modeType;
            }
        } catch (EmptyLineException e) {
            ConsolePrinter.printError("empty line");
            return ScannerManager.modeType;
        } catch (IOException e) {
            ConsolePrinter.printError("error: " + e.getMessage());
            return ScannerManager.modeType;
        } catch (ClassNotFoundException e) {
            return ScannerManager.modeType;
        }
    }
}
