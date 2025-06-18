package commands;

import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;
import java.util.List;
import java.util.ArrayList;

import utils.CmdDataFromServer;
public class Info extends Command{
    private final CollectionManager collectionManager;
    public Info(CollectionManager collectionManager) {
        super("info", "display information about collection");
        this.collectionManager = collectionManager;
    }



    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            List<Object> answer = new ArrayList<>();
            answer.add("Collection info:");
            answer.add("Time: " + collectionManager.getTime().toString());
            answer.add("Size: " + collectionManager.getCollectionSize());
            answer.add("Type: " + collectionManager.getCollectionType());
            return new CmdDataFromServer("info", answer);
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("info", List.of("Error: collection is empty"));
        }
    }

    @Override
    public String toString(){
        return "info - display information about collection";
    }
}
