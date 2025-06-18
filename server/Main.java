import managers.*;
import server.Server;
import server.TokenManager;
import java.io.IOException;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {

            DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://192.168.10.80:5432/studs", "s468030", "osgp44WPnRR9MSSK");
            CollectionManager collectionManager = new CollectionManager(dbManager);
            CommandManager commandManager = new CommandManager(collectionManager, dbManager);
            TokenManager tokenManager = new TokenManager();
            Server server = new Server(PORT, dbManager, tokenManager);
            tokenManager.getServer(server);

            server.start();
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
            System.exit(1);
        }
    }
}