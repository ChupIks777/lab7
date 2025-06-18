package commands;

import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;
import utils.CmdDataFromServer;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class PrintHighestAndLowest extends Command{

    private final CollectionManager collectionManager;
    public PrintHighestAndLowest(CollectionManager collectionManager) {
        super("print_H_L", "prints highest and lowest people from collection");
        this.collectionManager = collectionManager;

    }

    @Override   
    public CmdDataFromServer execute(String username, List<Object> args) {
        try{
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            LinkedList<Integer> sortedCollectionKeys = collectionManager.sortByHeight();
            List<Object> answer = new ArrayList<>();
            if (sortedCollectionKeys.size() == 1){
                answer.add(sortedCollectionKeys.getLast());
                answer.add(collectionManager.getCollection().get(sortedCollectionKeys.getLast()));
            } else{
                answer.add(sortedCollectionKeys.getLast());
                answer.add(collectionManager.getCollection().get(sortedCollectionKeys.getLast()));
                answer.add(sortedCollectionKeys.getFirst());
                answer.add(collectionManager.getCollection().get(sortedCollectionKeys.getFirst()));
            }
            return new CmdDataFromServer("print_H_L", answer);
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("print_H_L", List.of("Error: collection is empty"));
        }
    }

    @Override
    public String toString(){
        return "print_H_L - prints highest and lowest people from collection";
    }
}
