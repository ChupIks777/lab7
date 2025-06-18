package client;

import managers.ScannerManager;
import managers.CommandManager;
import java.io.IOException;
public class Verifier {

    private final ScannerManager scannerManager;
    private final Client client;
    private final Checker checker;
    private final CommandManager commandManager;
    static boolean isRunning = true;
    
    public Verifier(ScannerManager scannerManager, Client client, CommandManager commandManager, Checker checker) throws IOException {
        this.scannerManager = scannerManager;
        this.client = client;
        this.checker = checker;
        this.commandManager = commandManager;
    }

    public void verify() throws IOException {
        
        try {
            while (isRunning){
            String[] userData = scannerManager.verify();      
            byte[] response = client.sendObject(userData);
            if (!checker.check(response)){
                scannerManager.manualInputMode(commandManager);
            }
        }
        } catch (IOException e) {   
            throw new IOException("Ошибка при отправке данных на сервер : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IOException("Ошибка при определении типа данных: " + e.getMessage());
        }
    }
}