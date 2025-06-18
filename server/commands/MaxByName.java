package commands;

import collectionobject.Person;
import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;
import utils.CmdDataFromServer;

import java.util.List;
public class MaxByName extends Command{
    private final CollectionManager collectionManager;
    public MaxByName(CollectionManager collectionManager) {
        super("max_by_name", "display element with maximum name");
        this.collectionManager = collectionManager;
    }



    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try{
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            String maxName = "";
            Person maxPerson = null;
            Integer maxKey = null;
            for (Integer key : collectionManager.getCollection().keySet()){
                if (maxName.compareTo(collectionManager.getCollection().get(key).getName())<0){
                    maxName = collectionManager.getCollection().get(key).getName();
                    maxPerson = collectionManager.getCollection().get(key);
                    maxKey = key;
                }
            }
            return new CmdDataFromServer("max_by_name", List.of(maxKey, maxPerson));

        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("max_by_name", List.of("Error: collection is empty"));
        } 
    }

    @Override
    public String toString(){
        return "max_by_name - display element with maximum name";
    }
}
