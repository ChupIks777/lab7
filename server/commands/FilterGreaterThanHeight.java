package commands;

import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;
import utils.CmdDataFromServer;

import java.util.ArrayList;
import java.util.List;

public class FilterGreaterThanHeight extends Command{
    private final CollectionManager collectionManager;
    public FilterGreaterThanHeight(CollectionManager collectionManager) {
        super("filter_greater_than_height height", "display every element with height greater than given");
        this.collectionManager = collectionManager;
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try{    
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            int intHeight = (Integer) args.get(0);

            
            List<Object> answerList = new ArrayList<>();
            for (Integer key : collectionManager.getCollection().keySet()){
                if (intHeight < collectionManager.getCollection().get(key).getHeight()){
                    answerList.add(key);
                    answerList.add(collectionManager.getCollection().get(key));
                }
            }
            return new CmdDataFromServer("filter_greater_than_height", answerList);
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("filter_greater_than_height", List.of("Error: collection is empty"));
        }
    }

    @Override
    public String toString(){
        return "filter_greater_than_height height - display every element with height greater than given";
    }
}
