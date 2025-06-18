package utils;

import collectionobject.Person;

import java.util.Hashtable;
import java.util.Objects;

public class PersonValidator {

    public PersonValidator(Hashtable<Integer, Person> collection){
    }
    public static String checkPerson (Person person, Hashtable<Integer, Person> collection){
        for (Integer key : collection.keySet()) {
            if (collection.get(key).getId() == person.getId() || person.getId() <= 0) {
                return "incorrect id";
            }
        }
        if (person.getHeight() <= 0){
            return "incorrect height. It must be greater than 0";
        }
        if (person.getWeight() <= 0){
            return "incorrect weight. It must be greater than 0";
        }
        if (Objects.equals(person.getName(), "")){
            return "incorrect name. It can't be empty";
        }
        if (!person.getCoordinates().validate()){
            return "incorrect coordinates";
        }
        if (!person.getLocation().validate()){
            return "incorrect location";
        }
        return "correct";
    }
}
