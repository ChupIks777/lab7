package commands;

import collectionobject.*;
import exceptions.SameKeyElementException;
import managers.CollectionManager;
import managers.DatabaseManager;
import utils.PersonValidator;

import java.sql.SQLException;
import java.util.List;

import utils.CmdDataFromServer;
public class InsertElement extends Command{
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    public InsertElement(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("insert key, {element}", "insert new element with given key");
        this.collectionManager = collectionManager;
        this.databaseManager= databaseManager;
    }



    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            Integer intKey = Integer.parseInt((String) args.get(0));
            if (collectionManager.getCollection().containsKey(intKey)) throw new SameKeyElementException();
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
            if (PersonValidator.checkPerson(person, collectionManager.getCollection()).equals("correct")){
                if(!databaseManager.addItem(person, username, intKey)) throw new SQLException();
                collectionManager.addPersonWithKey(intKey, person);
                return new CmdDataFromServer("insert", List.of("Element was inserted"));
            } else{
                return new CmdDataFromServer("insert", List.of(PersonValidator.checkPerson(person, collectionManager.getCollection())));
            }
           
        } catch (SameKeyElementException e) {
            return new CmdDataFromServer("insert", List.of("Error: there is element with same key"));
        } catch (SQLException e) {
            return new CmdDataFromServer("insert", List.of("Error: can't insert element in data base "));
        }
    }

    @Override
    public String toString(){
        return "insert key, {element} - insert new element with given key";
    }
}
