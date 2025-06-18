package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import utils.CmdDataFromServer;
import utils.ConsolePrinter;
public class Checker {

    public Checker(){

    }

    public boolean check(byte[] response) throws IOException, ClassNotFoundException {
        if (response == null || response.length == 0) {
            throw new IOException("Получены пустые данные");
        }
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(response);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
             
            Object receivedObject = objectStream.readObject();
            
            if (receivedObject == null) {
                throw new IOException("Получен null");
            } else {
                String className = receivedObject.getClass().getName();

                switch (receivedObject) {
                    case String ignored -> {
                        ConsolePrinter.print(receivedObject.toString());
                        return receivedObject.toString().startsWith("Error");
                    }
                    case String[] received -> {
                        Client.token = received[1];
                        return false;
                    }
                    case CmdDataFromServer cmdDataFromServer -> {
                        ConsolePrinter.printReceivedData(cmdDataFromServer);
                        return false;
                    }
                    default -> {
                        ConsolePrinter.print("Получен объект типа: " + className);
                        return false;
                    }
                }
            }
        }
    }
}
