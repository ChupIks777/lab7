package commands;

import collectionobject.*;
import exceptions.EmptyColletionEcxeption;
import exceptions.NoSuchIdElementException;
import managers.CollectionManager;
import managers.DatabaseManager;
import utils.PersonValidator;

import java.sql.SQLException;
import java.util.List;

import utils.CmdDataFromServer;
public class UpdateByID extends Command{
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    public UpdateByID(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("update id {element}", "update element in collection which id equals given");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }



    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            if (collectionManager.getCollectionByName(username).isEmpty()) throw new EmptyColletionEcxeption();
            int intId = Integer.parseInt((String) args.get(0));
            for (Integer key : collectionManager.getCollectionByName(username).keySet()){
                if (collectionManager.getCollection().get(key).getId() == intId){
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
                        if (!databaseManager.updateItem(person, username)) throw new SQLException();
                        return new CmdDataFromServer("update_by_id", List.of("Successfully updated element!"));
                    } else{
                        return new CmdDataFromServer("update_by_id", List.of(PersonValidator.checkPerson(person, collectionManager.getCollection())));
                    }   
                }
            }
            throw new NoSuchIdElementException();
        } catch (NoSuchIdElementException e) {
            return new CmdDataFromServer("update_by_id", List.of("Error: no element with this id"));
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("update_by_id", List.of("Error: collection is empty"));
        } catch (SQLException e) {
            return new CmdDataFromServer("update_by_id", List.of("Error: can't load items from data base"));
        }
    }

    @Override
    public String toString(){
        return "update id {element} - update element in collection which id equals given";
    }
}
