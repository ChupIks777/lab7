package commands;

import collectionobject.Person;
import exceptions.EmptyColletionEcxeption;
import managers.CollectionManager;
import utils.CmdDataFromServer;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class PrintDescending extends Command{
    private final CollectionManager collectionManager;
    public PrintDescending(CollectionManager collectionManager) {
        super("print_descending", "display elements in descending order");
        this.collectionManager = collectionManager;
    }

    @Override
    public CmdDataFromServer execute(String username, List<Object> args) {
        try {
            if (collectionManager.getCollection().isEmpty()) throw new EmptyColletionEcxeption();
            LinkedList<Integer> heightSort = collectionManager.sortByHeight();
            List<Object> answer = new ArrayList<>();
            for (Integer key : heightSort){
                Person person = collectionManager.getCollection().get(key);
                answer.add(person);
            }
            return new CmdDataFromServer("print_descending", answer);
        } catch (EmptyColletionEcxeption e) {
            return new CmdDataFromServer("print_descending", List.of("Error: collection is empty"));
        }
    }

    @Override
    public String toString(){
        return "print_descending - display elements in descending order";
    }
}
