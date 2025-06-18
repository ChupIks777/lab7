package commands;

import collectionobject.*;
import exceptions.DataBaseException;
import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;
import managers.DatabaseManager;
import utils.PersonValidator;
import java.util.List;

import utils.CmdDataFromServer;

import javax.xml.crypto.Data;

public class RemoveGreater extends Command{
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    public RemoveGreater(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_greater {element}", "remove every element greater than given");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try{
            if (collectionManager.getCollectionByName(username) == null || collectionManager.getCollectionByName(username).isEmpty()) throw new EmptyColletionEcxeption();
            Person person = new Person(
                    collectionManager.getId(),
                    (String) args.get(0),
                    new Coordinates(
                            (Long) args.get(1),
                            (Float) args.get(2)),
                    collectionManager.getTime(),
                    (Float) args.get(3),
                    (long) args.get(4),
                    (Color) args.get(5),
                    (Country) args.get(6),
                    new Location(
                        (Integer) args.get(7),
                        (Long) args.get(8),
                        (Float) args.get(9),
                        (String) args.get(10)));
            if (PersonValidator.checkPerson(person, collectionManager.getCollection()).equals("correct")) {
                if (collectionManager.getCollectionByName(username) == null) throw new DataBaseException();
                for (Integer key : collectionManager.getCollectionByName(username).keySet()) {
                    if (collectionManager.getCollection().get(key).getHeight() > person.getHeight()) {
                        collectionManager.getCollection().remove(key);
                    }
                }
                return new CmdDataFromServer("remove_greater", List.of("All higher people were removed"));
            } else{
                return new CmdDataFromServer("remove_greater", List.of(PersonValidator.checkPerson(person, collectionManager.getCollection())));
            }

        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("remove_greater", List.of("Error: collection is empty"));
        } catch (DataBaseException e){
            return new CmdDataFromServer("remove_greater", List.of("Error: collection is empty or can't load items from data base"));
        }

    }

    @Override
    public String toString(){
        return "remove_greater {element} - remove every element greater than given";
    }
}
