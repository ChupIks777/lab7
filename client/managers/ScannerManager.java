package managers;

import client.Checker;
import exceptions.EmptyLineException;
import exceptions.NumberOfArgsException;
import utils.ConsolePrinter;
import java.util.*;

public class ScannerManager {
    public static final LinkedList<Scanner> scanners = new LinkedList<>();
    public static boolean modeType = false;

    public ScannerManager (){
    }

    public void manualInputMode(CommandManager commandManager) {

        while (true){
            try {
                Scanner scanner = new Scanner(System.in);
                modeType = false;
                ConsolePrinter.askForCommand("Enter the command:");
                String userCommand = scanner.nextLine();
                scanners.add(scanner);
                if (commandManager.defineCommand(userCommand)){
                    break;
                }
            } catch(NoSuchElementException e){
                ConsolePrinter.printError("incorrect line. Finishing process");
                break;
            }
        }
    }

    public String askForName(String message){
        Scanner scanner = scanners.getLast();
        String name = "";
        while (true){
            try {
                if (modeType) if (!scanner.hasNextLine()) break;
                ConsolePrinter.askForArgument("Enter " + message + ":");
                name = scanner.nextLine().strip();
                ConsolePrinter.printArg(name);
                if (name.split(" ").length > 1) throw new NumberOfArgsException();
                if (name.isEmpty()) throw new EmptyLineException();
                break;
            } catch (NumberOfArgsException e){
                ConsolePrinter.printError("too many arguments");
                if (modeType) break;
            } catch (EmptyLineException e){
                ConsolePrinter.printError("empty line");
                if (modeType) break;
            }
        }
        if (modeType) scanners.removeLast();
        return name;
    }

    public long askForLong(String message) {

        Scanner scanner = scanners.getLast();
        String number;
        long res = 0L;
        while (true) {
            try {
                if (ScannerManager.modeType){ if (!scanner.hasNextLine()) break;}
                ConsolePrinter.askForArgument("Enter " + message + ":");
                number = scanner.nextLine().strip();
                ConsolePrinter.printArg(number);
                if (number.split(" ").length > 1) throw new NumberOfArgsException();
                if (number.isEmpty()) throw new EmptyLineException();
                res = Long.parseLong(number);
                break;
            } catch (NumberOfArgsException e) {
                ConsolePrinter.printError("too many arguments");
                if (ScannerManager.modeType) break;
            } catch (EmptyLineException e) {
                ConsolePrinter.printError("empty line");
                if (ScannerManager.modeType) break;
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("you must enter long number");
                if (ScannerManager.modeType) break;
            }
        }
        if (modeType) scanners.removeLast();
        return res;
    }

    public Float askForFloat(String message) {
        Scanner scanner = scanners.getLast();
        String number;
        float res = 0F;
        while (true) {
            try {
                if (ScannerManager.modeType) if (!scanner.hasNextLine()) break;
                ConsolePrinter.askForArgument("Enter " + message + ":");
                number = scanner.nextLine().strip();
                ConsolePrinter.printArg(number);
                if (number.split(" ").length > 1) throw new NumberOfArgsException();
                if (number.isEmpty()) throw new EmptyLineException();
                res = Float.parseFloat(number);
                break;
            } catch (NumberOfArgsException e) {
                ConsolePrinter.printError("too many arguments");
                if (ScannerManager.modeType) break;
            } catch (EmptyLineException e) {
                ConsolePrinter.printError("empty line");
                if (ScannerManager.modeType) break;
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("you must enter float number");
                if (ScannerManager.modeType) break;
            }
        }
        if (modeType) scanners.removeLast();
        return res;
    }

    public Integer askForInteger(String message) {
        Scanner scanner = scanners.getLast();
        String number;
        int res = 0;
        while (true) {
            try {
                if (ScannerManager.modeType){ if (!scanner.hasNextLine()) break;}
                ConsolePrinter.askForArgument("Enter " + message + ":");
                number = scanner.nextLine().strip();
                ConsolePrinter.printArg(number);
                if (number.split(" ").length > 1) throw new NumberOfArgsException();
                if (number.isEmpty()) throw new EmptyLineException();
                res = Integer.parseInt(number);
                break;
            } catch (NumberOfArgsException e) {
                ConsolePrinter.printError("too many arguments");
                if (ScannerManager.modeType) break;
            } catch (EmptyLineException e) {
                ConsolePrinter.printError("empty line");
                if (ScannerManager.modeType) break;
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("you must enter integer");
                if (ScannerManager.modeType) break;
            }
        }
        if (modeType) scanners.removeLast();
        return res;
    }

    public <T extends Enum<T>> T askEnumConstant(Class<T> enumClass, String message){
        Scanner scanner = scanners.getLast();
        String name = "";
        while (true) {
            try {
                if (ScannerManager.modeType){ if (!scanner.hasNextLine()) break;}
                ConsolePrinter.askForArgument("Enter " + message + ".");
                ConsolePrinter.print("Available constants: " + Arrays.toString(enumClass.getEnumConstants()));
                name = scanner.nextLine().strip();
                ConsolePrinter.printArg(name);
                if (name.split(" ").length > 1) throw new NumberOfArgsException();
                if (name.isEmpty()) throw new EmptyLineException();
                return Enum.valueOf(enumClass, name.strip().toUpperCase());
            } catch (NumberOfArgsException e) {
                ConsolePrinter.printError("too many arguments");
                if (ScannerManager.modeType) break;
            } catch (EmptyLineException e) {
                ConsolePrinter.printError("empty line");
                if (ScannerManager.modeType) break;
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError("constant '" + name + "' was not found in enum " + enumClass.getSimpleName());
                if (ScannerManager.modeType) break;
            }
        }
        if (modeType) scanners.removeLast();
        return null;
    }

    public String[] verify(){
        String logInOrRegister = "";
        while (!(logInOrRegister.equalsIgnoreCase("log_in") || logInOrRegister.equalsIgnoreCase("register"))){
            logInOrRegister = askForString("log_in or register");
        }
        if (logInOrRegister.equalsIgnoreCase("log_in") || logInOrRegister.equalsIgnoreCase("register")){
            String name = askForString("username");
            String password = askForString("password");
            return new String[]{logInOrRegister, name, password};
        } else {
            return new String[]{"", "", ""};
        }
    }

    public String askForString(String message){
        Scanner scanner = scanners.getLast();
        String ans = "";
        while (true){
            try{
                ConsolePrinter.askForArgument("Enter " + message + ":");
                ans = scanner.nextLine().strip();
                if (ans.isEmpty()) throw new EmptyLineException();
                if (ans.split(" ").length > 1) throw new NumberOfArgsException();
                return ans;
            } catch (EmptyLineException e){
                ConsolePrinter.printError("empty line");
            } catch (NumberOfArgsException e){
                ConsolePrinter.printError("too many arguments");
            }
        }
    }

}
