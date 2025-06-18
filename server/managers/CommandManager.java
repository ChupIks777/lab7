package managers;

import commands.*;
import java.util.*;

public class CommandManager {
    private static final  Map<String, List<Object>> commandMap = new HashMap<>();
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public CommandManager(CollectionManager collectionManager, DatabaseManager databaseManager)
    {
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
        commandMap.put("help", new ArrayList<>(List.of(new Help(), 0)));
        commandMap.put("clear", new ArrayList<>(List.of(new Clear(collectionManager, databaseManager), 0)));
        commandMap.put("execute_script", new ArrayList<>(List.of(new ExecuteScript(), 1)));
        commandMap.put("filter_greater_than_height", new ArrayList<>(List.of(new FilterGreaterThanHeight(collectionManager), 1)));
        commandMap.put("info", new ArrayList<>(List.of(new Info(collectionManager), 0)));
        commandMap.put("insert", new ArrayList<>(List.of(new InsertElement(collectionManager, databaseManager), 11)));
        commandMap.put("max_by_name", new ArrayList<>(List.of(new MaxByName(collectionManager), 0)));
        commandMap.put("print_descending", new ArrayList<>(List.of(new PrintDescending(collectionManager), 0)));
        commandMap.put("remove_key", new ArrayList<>(List.of(new RemoveByKey(collectionManager, databaseManager), 1)));
        commandMap.put("remove_greater", new ArrayList<>(List.of(new RemoveGreater(collectionManager, databaseManager), 11)));
        commandMap.put("remove_greater_key", new ArrayList<>(List.of(new RemoveGreaterKey(collectionManager, databaseManager), 1)));
        commandMap.put("replace_if_lower", new ArrayList<>(List.of(new ReplaceIfLower(collectionManager, databaseManager), 12)));
        commandMap.put("show", new ArrayList<>(List.of(new Show(collectionManager), 0)));
        commandMap.put("update", new ArrayList<>(List.of(new UpdateByID(collectionManager, databaseManager), 12)));
        commandMap.put("print_H_L", new ArrayList<>(List.of(new PrintHighestAndLowest(collectionManager), 0)));
    }

    public static Map<String, List<Object>> getCommandMap() {
        return commandMap;
    }

}
