package commands;

import exceptions.DataBaseException;
import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import managers.DatabaseManager;
import utils.CmdDataFromServer;
public class Clear extends Command{

    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;
    public Clear(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("clear", "clear collection");
        this.databaseManager = databaseManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            if (!Objects.equals(databaseManager.clearUserItems(username), "")) throw new DataBaseException();
            String result = collectionManager.clearCollection();
            if (result != null) {
                return(new CmdDataFromServer("clear", List.of(result)));
            }
            return(new CmdDataFromServer("clear", List.of("collection was successfully cleared")));
        }catch (SQLException | DataBaseException e){
            return (new CmdDataFromServer("clear", List.of("Error: can't delete elements in data base")));
        } catch (EmptyColletionEcxeption e) {
            return(new CmdDataFromServer("clear", List.of("Error: collection is empty")));
        }

    }

    @Override
    public String toString(){
        return "clear - clear collection";
    }
}
