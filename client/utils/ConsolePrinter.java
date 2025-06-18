package utils;

import collectionobject.Person;

public class ConsolePrinter {
    static public void printError(String message){
        System.out.println("\u001B[31m" + "Error: " + message + "\u001B[0m");
    }

    static public void print(String message){
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    static public void askForCommand(String message){
        System.out.println("\u001B[33m" + message + "\u001B[0m");
    }

    static public void askForArgument (String message){
        System.out.println("\u001B[34m" + message + "\u001B[0m");
    }

    static public void printHelp(String message){
        System.out.println("\u001B[35m" + message + "\u001B[0m");
    }

    static public void printCmd(String message1, String message2){
        System.out.println("\u001B[1;96mEntered command: \u001B[0m" + "\u001B[3;36m" + message1 + " " + message2 + "\u001B[0m");
    }

    static public void printArg(String message){
        System.out.println("\u001B[1;96mEntered argument: \u001B[0m" + "\u001B[3;36m" + message + "\u001B[0m");
    }

    static public void printPerson(Person person){
        System.out.println("\u001B[1;96m" + "Id" + ": \u001B[0m" + "\u001B[3;35m" + person.getId() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Name" + ": \u001B[0m" + "\u001B[3;35m" + person.getName() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "X coordinate for Coordinates" + ": \u001B[0m" + "\u001B[3;35m" + person.getCoordinates().getX() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Y coordinate for Coordinates" + ": \u001B[0m" + "\u001B[3;35m" + person.getCoordinates().getY() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Date" + ": \u001B[0m" + "\u001B[3;35m" + person.getDate() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Height" + ": \u001B[0m" + "\u001B[3;35m" + person.getHeight() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Weight" + ": \u001B[0m" + "\u001B[3;35m" + person.getWeight() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Hair color" + ": \u001B[0m" + "\u001B[3;35m" + person.getHairColor() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Nationality" + ": \u001B[0m" + "\u001B[3;35m" + person.getNationality() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "X coordinate for Location" + ": \u001B[0m" + "\u001B[3;35m" + person.getLocation().getX() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Y coordinate for Location" + ": \u001B[0m" + "\u001B[3;35m" + person.getLocation().getY() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Z coordinate for Location" + ": \u001B[0m" + "\u001B[3;35m" + person.getLocation().getZ() + "\u001B[0m");
        System.out.println("\u001B[1;96m" + "Name for Location" + ": \u001B[0m" + "\u001B[3;35m" + person.getLocation().getName() + "\u001B[0m");
    }

    static public void printReceivedData(CmdDataFromServer data){
        System.out.println("Server tried to do cmd: " + data.getName());
        if (data.getName().equals("execute_script")){
            for (Object o : data.getArgs()){
                printReceivedData((CmdDataFromServer) o);
            }
        }else {
            System.out.println("\u001B[31m" + "Server's responce:" + "\u001B[0m");
            for (Object o : data.getArgs()) {
                if (o instanceof Person){
                    printPerson((Person) o);
                } else {
                    System.out.println(o);
                }
            }
        }
    }
}
