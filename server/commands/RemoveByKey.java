package commands;

import exceptions.DataBaseException;
import exceptions.EmptyColletionEcxeption;
import exceptions.SameKeyElementException;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.List;

import managers.DatabaseManager;
import utils.CmdDataFromServer;
public class RemoveByKey extends Command {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public RemoveByKey(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_key key", "remove element with given key");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            Integer intKey = (Integer) args.get(0);
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            if (!collectionManager.getCollection().containsKey(intKey)) throw new SameKeyElementException();
            if (!databaseManager.removeItem(intKey, username)) throw new DataBaseException();
            collectionManager.getCollection().remove(intKey);
            return new CmdDataFromServer("remove_key", List.of("Element was removed"));
        } catch (SQLException | DataBaseException e){
            return new CmdDataFromServer("remove_key", List.of("Error: can't delete this element from data base"));
        } catch (SameKeyElementException e) {
            return new CmdDataFromServer("remove_key", List.of("no element with such key"));
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("remove_key", List.of("Error: collection is empty"));
        }
    }

    @Override
    public String toString(){
        return "remove_key key - remove element with given key";
    }
}
