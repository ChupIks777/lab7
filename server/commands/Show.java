package commands;

import collectionobject.Person;
import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;

import java.util.List;
import java.util.ArrayList;
import utils.CmdDataFromServer;

public class Show extends Command{
    private final CollectionManager collectionManager;
    public Show(CollectionManager collectionManager) {
        super("show", "display every element in collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {


            if (collectionManager.getCollectionSize() == 0) throw new EmptyColletionEcxeption();
            List<Object> answer = new ArrayList<>();
            for (Integer key : collectionManager.getCollection().keySet()){
                Person person = collectionManager.getCollection().get(key);
                answer.add(person);
            }
            return new CmdDataFromServer("show", answer);
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("show", List.of("Error: collection is empty"));
        }
    }

    @Override
    public String toString(){
        return "show - display every element in collection";
    }
}
