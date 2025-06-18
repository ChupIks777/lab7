package server;

import utils.ConsolePrinter;
import utils.CmdDataFromServer;
import utils.CmdData;
import commands.Command;
import managers.CommandManager;
import managers.DatabaseManager;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final DatabaseManager dbManager;
    private final TokenManager tokenManager;
    private final ExecutorService executorService;
    private final DatagramSocket commandSocket;
    private volatile boolean isRunning = true;
    private final Map<SocketAddress, String> connectedClients = new ConcurrentHashMap<>();

    public Server(int port, DatabaseManager dbManager, TokenManager tokenManager) throws IOException {
        this.dbManager = dbManager;
        this.tokenManager = tokenManager;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        this.commandSocket = new DatagramSocket(port);
        ConsolePrinter.print("Сервер запущен на порту: " + port);
    }

    public void start() {
        try {
            byte[] buffer = new byte[65507];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (isRunning) {
                packet.setLength(buffer.length);
                commandSocket.receive(packet);
                byte[] dataCopy = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, dataCopy, 0, packet.getLength());
                DatagramPacket requestCopy = new DatagramPacket(dataCopy, dataCopy.length, packet.getSocketAddress());
                executorService.submit(() -> processRequest(requestCopy));
            }
        } catch (IOException e) {
            ConsolePrinter.printError("Ошибка сервера: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void processRequest(DatagramPacket request) {
        if (request.getLength() == 0) {
            ConsolePrinter.print("Get ping from " + request.getSocketAddress());
            sendResponse(request, "OK");
            return;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(request.getData(), 0, request.getLength());
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            Object received = ois.readObject();

            if (received instanceof String[]) {
                handleAuthRequest(request, (String[]) received);
            } else if (received instanceof CmdData) {
                handleCommandRequest(request, (CmdData) received);
            } else if (received instanceof String str) {
                if (str.contains("EXIT")) {
                    String token = str.split(" ")[1];
                    String message = tokenManager.deleteToken(token);
                    if (message.contains("Error")) {
                        sendResponse(request, message);
                    } else {
                        broadcastNotification(message);
                    }
                } else if (str.startsWith("REGISTER_NOTIFY_PORT")) {
                    int port = Integer.parseInt(str.split(" ")[1]);
                    SocketAddress addr = new InetSocketAddress(request.getAddress(), port);
                    connectedClients.put(addr, "");
                }
            }

        } catch (Exception e) {
            ConsolePrinter.printError("Ошибка при обработке запроса: " + e.getMessage());
        }
    }

    private void handleAuthRequest(DatagramPacket request, String[] credentials) {
        if (credentials.length != 3) {
            sendResponse(request, "Error: incorrect auth");
            return;
        }
        String action = credentials[0];
        String username = credentials[1];
        String password = credentials[2];

        if ("register".equals(action)) {
            if (dbManager.registerUser(username, password)) {
                String token = tokenManager.generateToken(username);
                sendResponse(request, new String[]{"token", token});
                broadcastNotification("Пользователь " + username + " зарегистрирован и подключён");
            } else {
                sendResponse(request, "Error: User exists or registration error");
            }
        } else if ("log_in".equals(action)) {
            if (dbManager.validateUser(username, password)) {
                String token = tokenManager.generateToken(username);
                sendResponse(request, new String[]{"token", token});
                broadcastNotification("Пользователь " + username + " вошёл в систему");
            } else {
                sendResponse(request, "Error: Неверные учетные данные");
            }
        }
    }

    private void handleCommandRequest(DatagramPacket request, CmdData cmdData) {
        String token = cmdData.getToken();
        if (token == null || !tokenManager.validateToken(token)) {
            sendResponse(request, "Error: Недействительный токен");
            //broadcastNotification();
            return;
        }

        try {
            tokenManager.refreshToken(token);
            Command command = ((Command) CommandManager.getCommandMap().get(cmdData.getName()).get(0));
            CmdDataFromServer result = command.execute(tokenManager.getUsernameByToken(token), cmdData.getArgs());
            //System.out.println(result);
            //System.out.println(1);
            sendResponse(request, result);
        } catch (Exception e) {
            sendResponse(request, "Error: Ошибка выполнения команды: " + e.getMessage());
        }
    }

    private void sendResponse(DatagramPacket original, Object response) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            byte[] data = baos.toByteArray();
            DatagramPacket reply = new DatagramPacket(data, data.length, original.getSocketAddress());
            commandSocket.send(reply);
        } catch (IOException e) {
            ConsolePrinter.printError("Ошибка отправки ответа: " + e.getMessage());
        }
    }

    public void broadcastNotification(String msg) {
        for (SocketAddress address : connectedClients.keySet()) {
            //System.out.println("pp");
            sendResponse(new DatagramPacket("".getBytes(), 0, address), msg);
        }
    }

    public void shutdown() {
        isRunning = false;
        broadcastNotification("Сервер завершает работу");
        commandSocket.close();
        executorService.shutdownNow();
        dbManager.close();
    }
}