package commands;

import collectionobject.*;
import exceptions.EmptyColletionEcxeption;
import exceptions.NoSuchKeyElementException;
import managers.CollectionManager;
import managers.DatabaseManager;
import utils.PersonValidator;

import java.sql.SQLException;
import java.util.List;

import utils.CmdDataFromServer;
public class ReplaceIfLower extends Command{
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    public ReplaceIfLower(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("replace_if_lower key {element}", "replace old element by key if new one is lower");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }



    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            if (collectionManager.getCollectionByName(username).isEmpty() ||collectionManager.getCollectionByName(username) == null) throw new EmptyColletionEcxeption();
            Integer intKey = Integer.parseInt((String) args.get(0));
            if (!collectionManager.getCollection().containsKey(intKey)) throw new NoSuchKeyElementException();
            Person person = new Person(
                    collectionManager.getId(),
                    (String) args.get(1),
                    new Coordinates(
                            (Long) args.get(2),
                            (Float) args.get(3)),
                    collectionManager.getTime(),
                    (Float) args.get(4),    
                    (long) args.get(5),
                    (Color) args.get(6),
                    (Country) args.get(7),
                    new Location(
                        (Integer) args.get(8),
                        (Long) args.get(9),
                        (Float) args.get(10),
                        (String) args.get(11)));
            if (PersonValidator.checkPerson(person, collectionManager.getCollection()).equals("correct")) {
                if (collectionManager.getCollectionByName(username).get(intKey).getHeight() > person.getHeight()) {
                    databaseManager.removeItem(intKey, username);
                    databaseManager.addItem(person, username, intKey);
                    collectionManager.loadCollectionFromDatabase();
                    return new CmdDataFromServer("replace_if_lower", List.of("people were replaced"));
                } else{
                    return new CmdDataFromServer("replace_if_lower", List.of("new person is higher"));
                }
            } else{
                return new CmdDataFromServer("replace_if_lower", List.of(PersonValidator.checkPerson(person, collectionManager.getCollection())));
            }
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("replace_if_lower", List.of("Error: collection is empty"));
        } catch (NoSuchKeyElementException e) {
            return new CmdDataFromServer("replace_if_lower", List.of("Error: no element with this key"));
        } catch (SQLException e) {
            return new CmdDataFromServer("replace_if_lower", List.of("Error: can't load items from data base"));
        }
    }

    @Override
    public String toString(){
        return "replace_if_lower key {element} - replace old element by key if new one is lower";
    }
}
