package commands;

import exceptions.EmptyArgException;
import exceptions.NoAccessToFileException;
import exceptions.NumberOfArgsException;
import exceptions.RecursionException;
import managers.*;
import utils.ConsolePrinter;
import utils.CmdData;
import client.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ExecuteScript extends Command{
    
    public ExecuteScript() {
        super("execute_script file_name", "read script from file and execute");

    }

    @Override
    public CmdData validate(String arg){
        try {
            List<Object> data = new ArrayList<>();
            CmdData cmdData = new CmdData(Client.token, "execute_script", data);  
            if (arg.isEmpty()) throw new EmptyArgException();
            if (arg.split(" ").length > 1) throw new NumberOfArgsException();
            ConsolePrinter.print("Starting doing script! Path: " + arg);
            ScriptManager.addScript(arg);  
                File file = new File(arg);
                if (file.exists() && !file.canRead()) throw new NoAccessToFileException();
                Scanner scannerScript = new Scanner(file);
                
                while (scannerScript.hasNextLine()) {
                    ScannerManager.modeType = true;
                    String userCommand = scannerScript.nextLine();

                    ScannerManager.scanners.add(scannerScript);
                    if (Objects.equals(userCommand.strip().split(" ", 2)[0], "execute_script")) {
                        for (String scriptPath : ScriptManager.getScriptPaths())
                            if (userCommand.strip().split(" ", 2)[1].equals(scriptPath)) {
                                throw new RecursionException();
                            }
                    }

                    //ConsolePrinter.print(userCommand + "      hhh");
                    String cmdArg = "";
                    if (userCommand.strip().split(" ").length > 1) {
                        cmdArg = userCommand.strip().split(" ", 2)[1];
                    }
                   // System.out.println(userCommand);
                    if (!userCommand.isEmpty() && CommandManager.getCommandMap().containsKey(userCommand.strip().split(" ", 2)[0])) {
                        CmdData cmdDataTemp = ((Command) CommandManager.getCommandMap().get(userCommand.strip().split(" ", 2)[0]).get(0)).validate(cmdArg);
                        if (cmdDataTemp != null) {
                            if (cmdDataTemp.getName().equals("execute_script")){
                                for (Object o : cmdDataTemp.getArgs()){
                                    CmdData temp = (CmdData) o;
                                    cmdData.getArgs().add(temp);
                                    //System.out.println(temp);
                                }
                            }else {
                                cmdData.getArgs().add(cmdDataTemp);
                            }
                        } else{
                            ConsolePrinter.printError("incorrect command: " + userCommand);
                            return cmdData;
                        }
                    } else{
                        ConsolePrinter.printError("incorrect command: " + userCommand);
                        return null;
                    }

                }
//            System.out.println(cmdData.getName());
//            for (Object o : cmdData.getArgs()){
//                CmdData c = (CmdData) o;
//                System.out.println(c.getName() + "     " + c.getArgs());
//            }
//            System.out.println();
                return cmdData;

        } catch (EmptyArgException e) {
            ConsolePrinter.printError("argument is empty");
            return  null;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("too many args");
            return null;
        } catch (NoAccessToFileException e) {
            ConsolePrinter.printError("no access to file");
            return null;
        } catch (FileNotFoundException exception) {
            ConsolePrinter.printError("no such file");
            return null;
        } catch (NoSuchElementException e) {
            ConsolePrinter.printError("incorrect line. Finishing process");
            return null;
        } catch (RecursionException e) {
            ConsolePrinter.printError("scripts have recursion. Finishing process");
            return null;
        } finally {
            ScriptManager.removeLastScript();
            ScannerManager.modeType = false; 
        }
    }
}