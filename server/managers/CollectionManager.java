package managers;

import collectionobject.Person;

import java.util.*;
import java.time.ZonedDateTime;
import java.sql.SQLException;

public class CollectionManager {
    private final Hashtable<Integer, Person> collection;
    private final LinkedList<Integer> collectionId = new LinkedList<>();
    private final LinkedList<Integer> collectionHeight = new LinkedList<>();
    private final ZonedDateTime creationDate = ZonedDateTime.now();
    private final DatabaseManager databaseManager;
    //static List<String> scanners = new ArrayList<>();

    public CollectionManager(DatabaseManager databaseManager) {
        this.collection = new Hashtable<>();
        this.databaseManager = databaseManager;
        loadCollectionFromDatabase();
    }

    public void loadCollectionFromDatabase() {
        try {
            Map<Person, Integer> itemsWithKeys = databaseManager.getAllItems();
            collection.clear();
            for (Map.Entry<Person, Integer> entry : itemsWithKeys.entrySet()) {
                collection.put(entry.getValue(), entry.getKey());
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке коллекции из базы данных: " + e.getMessage());
        }
    }

    public Hashtable<Integer, Person> getCollectionByName(String username){
        try {
            Map<Person, Integer> itemsWithKeys = databaseManager.getItemsByUsername(username);
            collection.clear();
            for (Map.Entry<Person, Integer> entry : itemsWithKeys.entrySet()) {
                collection.put(entry.getValue(), entry.getKey());
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке коллекции из базы данных: " + e.getMessage());
            return null;
        }
        return collection;
    }

    public String clearCollection() {
            // Перезагружаем коллекцию из базы данных
            loadCollectionFromDatabase();
            return "Коллекция успешно очищена";

    }

    public void addPersonWithKey(Integer key, Person person){
        collection.put(key, person);
    }

    public Hashtable<Integer, Person> getCollection() {
        return collection;
    }

    public ZonedDateTime getTime(){
        return creationDate;
    }

    public LinkedList<Integer> sortById(){
        collectionId.clear();
        collectionId.addAll(collection.keySet());
        collectionId.sort((key1, key2) -> (collection.get(key1).getId() - collection.get(key2).getId()));
        return collectionId;
    }

    public LinkedList<Integer> sortByHeight(){
        collectionHeight.clear();
        collectionHeight.addAll(collection.keySet());
        collectionId.sort((key1, key2) -> Float.compare(collection.get(key1).getHeight(), collection.get(key2).getHeight()));
        return collectionHeight;
    }

    public int getId(){
        if (collection.isEmpty()) return 1;
        return collection.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }

    public String getCollectionType() {
        return collection.getClass().getName();
    }

    public int getCollectionSize() {
        return collection.size();
    }
}