import client.Checker;
import managers.*;
import utils.ConsolePrinter;
import java.util.Scanner;
import client.Client;
import client.Verifier;
import java.io.IOException;
public class Main {

    public static void main(String[] args) {
        try {
            String port = "8080";
            Checker checker = new Checker();
            Client client = new Client(Integer.parseInt(port));
            ScannerManager scannerManager = new ScannerManager();
            ScannerManager.scanners.add(new Scanner(System.in));
            CommandManager commandManager = new CommandManager(scannerManager, client, checker);

            Verifier verifier = new Verifier(scannerManager, client, commandManager, checker);
            verifier.verify();
        } catch (IOException e) {
            
            ConsolePrinter.printError(e.getMessage());
        }
    }
    
}