package commands;

import exceptions.NumberOfArgsException;
import utils.CmdData;
import utils.ConsolePrinter;
import managers.ScannerManager;
import collectionobject.*;
import client.Client;
import java.util.ArrayList;
import java.util.List;

public class UpdateByID extends Command{
    private final ScannerManager scannerManager;
    public UpdateByID(ScannerManager scannerManager) {
        super("update id {element}", "update element in collection which id equals given");
        this.scannerManager = scannerManager;
    }
    @Override
    public CmdData validate(String arg){
        try {
            if (arg.isEmpty()) {
                throw new NumberOfArgsException();
            }  
            Integer.parseInt(arg);
            List<Object> args = new ArrayList<>();
            args.add(arg);
            CmdData cmdData = new CmdData(Client.token, "update_by_id", args);
            String name = scannerManager.askForName("name");
            Long xCoordinate = scannerManager.askForLong("x coordinate");
            Float yCoordinate = scannerManager.askForFloat("y coordinate");
            Float height = scannerManager.askForFloat("height");
            long weight = scannerManager.askForLong("weight");
            Color hairColor = scannerManager.askEnumConstant(Color.class, "hair color");
            Country nationality = scannerManager.askEnumConstant(Country.class, "nationality");
            Integer xCoordinateForLocation = scannerManager.askForInteger("X coordinate for location");
            long yCoordinateForLocation = scannerManager.askForLong("Y coordinate for location");
            Float zCoordinateForLocation = scannerManager.askForFloat("Z coordinate for location");
            String nameForLocation = scannerManager.askForName("name for location");
            args.add(name);
            args.add(xCoordinate);
            args.add(yCoordinate);
            args.add(height);
            args.add(weight);
            args.add(hairColor);
            args.add(nationality);
            args.add(xCoordinateForLocation);
            args.add(yCoordinateForLocation);
            args.add(zCoordinateForLocation);
            args.add(nameForLocation);
            return cmdData;
        } catch (NumberOfArgsException e) {
            ConsolePrinter.printError("you must insert one integer arg");
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("incorrect id");
        }
        return null;
    }
}
