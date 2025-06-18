package utils;

import collectionobject.Person;

import java.util.Hashtable;
import java.util.Objects;

public class PersonValidator {

    public PersonValidator(Hashtable<Integer, Person> collection){
    }
    public static boolean checkPerson (Person person, Hashtable<Integer, Person> collection){
        boolean flag = false;
        for (Integer key : collection.keySet()) {
            if (collection.get(key).getId() == person.getId() || person.getId() <= 0) {
                ConsolePrinter.printError("incorrect id");
                flag = true;
            }
        }
        if (person.getHeight() <= 0){
            ConsolePrinter.printError("incorrect height. It must be greater than 0");
            flag =  true;
        }
        if (person.getWeight() <= 0){
            ConsolePrinter.printError("incorrect weight. It must be greater than 0");
            flag =  true;
        }
        if (Objects.equals(person.getName(), "")){
            ConsolePrinter.printError("incorrect name. It can't be empty");
            flag =  true;
        }
        if (!person.getCoordinates().validate()){
            ConsolePrinter.printError("incorrect coordinates");
            flag =  true;
        }
        if (!person.getLocation().validate()){
            ConsolePrinter.printError("incorrect location");
            flag =  true;
        }
        return !flag;
    }
}
