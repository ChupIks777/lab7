package utils;


public class ConsolePrinter {
    public static void print(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }
}
