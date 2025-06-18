package commands;

import exceptions.DataBaseException;
import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.List;

import managers.DatabaseManager;
import utils.CmdDataFromServer;
public class RemoveGreaterKey extends Command{
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    public RemoveGreaterKey(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_greater_key key", "remove every element with key greater than given");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }



    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
            try{
                if (collectionManager.getCollectionByName(username).isEmpty()) throw new EmptyColletionEcxeption();
                Integer intKey = Integer.parseInt((String) args.get(0));
                if (collectionManager.getCollectionByName(username) == null) throw new DataBaseException();
                for (Integer colKey : collectionManager.getCollectionByName(username).keySet()){
                    if (colKey > intKey){
                        databaseManager.removeItem(intKey, username);
                        collectionManager.loadCollectionFromDatabase();
                    }
                }
                return new CmdDataFromServer("remove_greater_key", List.of("Elements were removed"));
            } catch (EmptyColletionEcxeption e) {
                return new CmdDataFromServer("remove_greater_key", List.of("Error: collection is empty"));
            } catch (DataBaseException | SQLException e){
                return new CmdDataFromServer("remove_greater_key", List.of("Error: collection is empty or can't load items from data base"));
            }
    }

    @Override
    public String toString(){
        return "remove_greater_key key - remove every element with key greater than given";
    }
}
