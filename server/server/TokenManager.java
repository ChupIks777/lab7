package server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

public class TokenManager {
    private static final long TOKEN_VALIDITY_PERIOD = 150000; // 2.5 минуты в миллисекундах
    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> expirationTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Server server;

    private static class TokenInfo {
        final String username;
        volatile long expirationTime;

        TokenInfo(String username, long expirationTime) {
            this.username = username;
            this.expirationTime = expirationTime;
        }
    }

    public void getServer(Server server){
        this.server = server;
    }

    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD;
        tokens.put(token, new TokenInfo(username, expirationTime));

        ScheduledFuture<?> task = scheduler.schedule(() -> {
            if (server != null) {
                //System.out.println("111");
                server.broadcastNotification("Пользователь " + getUsernameByToken(token) + " отключён (токен истёк)");
            }
            tokens.remove(token);
        }, TOKEN_VALIDITY_PERIOD, TimeUnit.MILLISECONDS);

        expirationTasks.put(token, task);

        return token;
    }

    public boolean validateToken(String token) {
        TokenInfo info = tokens.get(token);
        if (info == null) {
            return false;
        }

        long now = System.currentTimeMillis();
        if (now > info.expirationTime) {
            tokens.remove(token);
            expirationTasks.remove(token);
            return false;
        }
        return true;
    }

    public String deleteToken(String token){
        TokenInfo info = tokens.get(token);
        if (info != null) {
            String name  = getUsernameByToken(token);
            ScheduledFuture<?> oldTask = expirationTasks.get(token);
            if (oldTask != null) {
                oldTask.cancel(false);
            } else{
                return "User " + name + " was already disconnected";
            }
            return "User " + name + " disconnected";
        }else{
            return "Error: user was already disconnected";
        }
    }

    public void refreshToken(String token) {
        TokenInfo info = tokens.get(token);
        if (info != null) {
            ScheduledFuture<?> oldTask = expirationTasks.get(token);
            if (oldTask != null) {
                oldTask.cancel(false);
            }

            info.expirationTime = System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD;

            ScheduledFuture<?> newTask = scheduler.schedule(() -> {
                if (server != null) {

                    server.broadcastNotification("Пользователь " + info.username + " отключён (токен истёк)");
                }
                tokens.remove(token);
            }, TOKEN_VALIDITY_PERIOD, TimeUnit.MILLISECONDS);

            expirationTasks.put(token, newTask);
        }
    }

    public  String getUsernameByToken(String token) {
        TokenInfo info = tokens.get(token);
        return info != null ? info.username : null;
    }
} 