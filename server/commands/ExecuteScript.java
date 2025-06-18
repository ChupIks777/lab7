package commands;

import java.util.List;
import java.util.ArrayList;
import managers.CommandManager;
import utils.CmdData;
import exceptions.ScriptExeption;
import utils.ConsolePrinter;
import utils.CmdDataFromServer;

public class ExecuteScript extends Command{

    public ExecuteScript() {
        super("execute_script file_name", "read and execute commands from file");
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {   
        //System.out.println(11);
        List<Object> answer = new ArrayList<>();
        //System.out.println(args);
        try {
            for (Object arg : args) {
                //System.out.println(2);
                if (arg instanceof CmdData cmdData) {
                    //System.out.println(3);
                    CmdDataFromServer cmd = ((Command) CommandManager.getCommandMap().get(cmdData.getName()).get(0)).execute(username, cmdData.getArgs());
                    if (cmd == null || ((cmd.getArgs().get(0) instanceof String) && ((String) cmd.getArgs().get(0)).contains("Error"))) throw new ScriptExeption();
                    answer.add(cmd);
                } else {

                    throw new ScriptExeption();
                }
            }
        } catch (ScriptExeption e){
            ConsolePrinter.printError("unexpected exception");
            return new CmdDataFromServer("execute_script", List.of("Error: unexpected exception"));
        }
        return new CmdDataFromServer("execute_script", answer);
    }

    @Override
    public String toString(){
        return "execute_script file_name - read and execute commands from file";
    }
}