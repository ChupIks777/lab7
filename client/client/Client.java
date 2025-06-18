package client;

import utils.ConsolePrinter;
import java.io.*;
import java.net.*;

public class Client {
    public static String token;
    private final int PORT;
    private final DatagramSocket commandSocket;
    private final DatagramSocket notificationSocket;
    private final InetAddress address;
    private static final int BUFFER_SIZE = 4096;
    private static final int TIMEOUT = 5000;
    private volatile boolean isListening = true;
    private Thread notificationThread;

    public Client(int PORT) throws IOException {
        this.PORT = PORT;
        this.commandSocket = new DatagramSocket();
        this.notificationSocket = new DatagramSocket();
        this.commandSocket.setSoTimeout(TIMEOUT);
        this.notificationSocket.setSoTimeout(TIMEOUT);
        this.address = InetAddress.getByName("localhost");
        if (!pingServer()) {
            throw new IOException("Сервер не отвечает");
        }
        registerNotificationPort();
        startReceivingNotifications();
    }

    public boolean pingServer() {
        try {
            byte[] pingData = new byte[0];
            DatagramPacket pingPacket = new DatagramPacket(pingData, pingData.length, address, PORT);
            commandSocket.send(pingPacket);
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            try {
                commandSocket.receive(receivePacket);
                return true;
            } catch (SocketTimeoutException e) {
                return false;
            }
        } catch (IOException e) {
            System.err.println("Ошибка при пинге сервера: " + e.getMessage());
            return false;
        }
    }

    public byte[] sendObject(Object object) throws IOException {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            //System.out.println(object);
            objectStream.writeObject(object);
            objectStream.flush();
            byte[] data = byteStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, PORT);
            commandSocket.send(sendPacket);
            return receiveData();
        } catch (IOException e) {
            throw new IOException("Ошибка отправки объекта: " + e.getMessage());
        }
    }

    public byte[] receiveData() throws IOException {
        byte[] receiveBuffer = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            commandSocket.receive(receivePacket);
            byte[] data = new byte[receivePacket.getLength()];
            System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
            return data;
        } catch (SocketTimeoutException e) {
            throw new IOException("Время ожидания ответа от сервера истекло");
        }
    }

    public void registerNotificationPort() throws IOException {
        String msg = "REGISTER_NOTIFY_PORT " + notificationSocket.getLocalPort();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(msg);
        byte[] data = byteStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
        commandSocket.send(packet);
    }

    public void startReceivingNotifications() {
        notificationThread = new Thread(() -> {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (isListening) {
                try {
                    notificationSocket.receive(packet);
                    ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Object received = ois.readObject();
                    if (received instanceof String notification) {
                        ConsolePrinter.print("\n[Уведомление] " + notification);
                    } else {
                        ConsolePrinter.printError("Неизвестный тип уведомления");
                    }
                } catch (SocketTimeoutException ignored) {
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Ошибка получения уведомления: " + e.getMessage());
                }
            }
        });
        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    public void stopListeningForNotifications() {
        isListening = false;
        if (notificationThread != null) {
            notificationThread.interrupt();
        }
    }

    public void exit() throws IOException {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject("EXIT " + token);
            objectStream.flush();
            byte[] data = byteStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
            commandSocket.send(packet);
            close();
            System.out.println("Клиент завершил работу");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Ошибка при отправке команды выхода: " + e.getMessage());
            System.exit(1);
        }
    }

    public void close() {
        stopListeningForNotifications();
        commandSocket.close();
        notificationSocket.close();
    }
}