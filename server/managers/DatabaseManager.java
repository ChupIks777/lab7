package managers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Base64;
import org.bouncycastle.crypto.generators.SCrypt;
import collectionobject.Person;
import collectionobject.Coordinates;
import collectionobject.Location;
import collectionobject.Color;
import collectionobject.Country;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final int SCRYPT_N = 16384;
    private static final int SCRYPT_r = 8;
    private static final int SCRYPT_p = 1;
    private static final int SALT_LENGTH = 32;
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            connect();
        } catch (SQLException e) {
            System.err.println("Ошибка при инициализации соединения с БД: " + e.getMessage());
        }
    }

    private void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(true);
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            byte[] hash = SCrypt.generate(
                password.getBytes(StandardCharsets.UTF_8),
                salt,
                SCRYPT_N,
                SCRYPT_r,
                SCRYPT_p,
                32
            );
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при хешировании пароля", e);
        }
    }

    public boolean registerUser(String username, String password) {
        if (!doesUserExist(username)) {
            String sql = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
            try {

                byte[] salt = generateSalt();
                String hashedPassword = hashPassword(password, salt);

                try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashedPassword);
                    stmt.setBytes(3, salt);
                    stmt.executeUpdate();
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при регистрации пользователя: " + e.getMessage());
                return false;
            }
        } else{
            return false;
        }
    }

    public boolean doesUserExist(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке существования пользователя: " + e.getMessage());
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        String sql = "SELECT password_hash, salt FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                byte[] salt = rs.getBytes("salt");
                String inputHash = hashPassword(password, salt);
                return storedHash.equals(inputHash);
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения с БД: " + e.getMessage());
            }
        }
    }

    public boolean addItem(Person item, String username, Integer key) throws SQLException {
        String coordSql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        Long coordId;
        try (PreparedStatement stmt = getConnection().prepareStatement(coordSql)) {
            stmt.setLong(1, item.getCoordinates().getX());
            stmt.setFloat(2, item.getCoordinates().getY());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Не удалось добавить координаты");
            }
            coordId = rs.getLong(1);
        }

        String locSql = "INSERT INTO locations (x, y, z, name) VALUES (?, ?, ?, ?) RETURNING id";
        Long locId;
        try (PreparedStatement stmt = getConnection().prepareStatement(locSql)) {
            stmt.setInt(1, item.getLocation().getX());
            stmt.setLong(2, item.getLocation().getY());
            stmt.setFloat(3, item.getLocation().getZ());
            stmt.setString(4, item.getLocation().getName());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Не удалось добавить локацию");
            }
            locId = rs.getLong(1);
        }

        String personSql = """
            INSERT INTO persons 
            (key, owner_id, name, coordinates_id, height, weight, hair_color, nationality, location_id)
            VALUES 
            (?, (SELECT id FROM users WHERE username = ?), ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

        try (PreparedStatement stmt = getConnection().prepareStatement(personSql)) {
            stmt.setInt(1, key);
            stmt.setString(2, username);
            stmt.setString(3, item.getName());
            stmt.setLong(4, coordId);
            stmt.setFloat(5, item.getHeight());
            stmt.setLong(6, item.getWeight());
            stmt.setString(7, item.getHairColor() != null ? item.getHairColor().name() : null);
            stmt.setString(8, item.getNationality() != null ? item.getNationality().name() : null);
            stmt.setLong(9, locId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean updateItem(Person item, String username) throws SQLException {
        String coordSql = """
            UPDATE coordinates 
            SET x = ?, y = ? 
            WHERE id = (SELECT coordinates_id FROM persons WHERE id = ? AND owner_id = (SELECT id FROM users WHERE username = ?))
        """;
        try (PreparedStatement stmt = getConnection().prepareStatement(coordSql)) {
            stmt.setLong(1, item.getCoordinates().getX());
            stmt.setFloat(2, item.getCoordinates().getY());
            stmt.setInt(3, item.getId());
            stmt.setString(4, username);
            stmt.executeUpdate();
        }


        String locSql = """
            UPDATE locations 
            SET x = ?, y = ?, z = ?, name = ? 
            WHERE id = (SELECT location_id FROM persons WHERE id = ? AND owner_id = (SELECT id FROM users WHERE username = ?))
        """;
        try (PreparedStatement stmt = getConnection().prepareStatement(locSql)) {
            stmt.setInt(1, item.getLocation().getX());
            stmt.setLong(2, item.getLocation().getY());
            stmt.setFloat(3, item.getLocation().getZ());
            stmt.setString(4, item.getLocation().getName());
            stmt.setInt(5, item.getId());
            stmt.setString(6, username);
            stmt.executeUpdate();
        }


        String personSql = """
            UPDATE persons 
            SET name = ?, height = ?, weight = ?, hair_color = ?, nationality = ?
            WHERE id = ? AND owner_id = (SELECT id FROM users WHERE username = ?)
        """;

        try (PreparedStatement stmt = getConnection().prepareStatement(personSql)) {
            stmt.setString(1, item.getName());
            stmt.setFloat(2, item.getHeight());
            stmt.setLong(3, item.getWeight());
            stmt.setString(4, item.getHairColor() != null ? item.getHairColor().name() : null);
            stmt.setString(5, item.getNationality() != null ? item.getNationality().name() : null);
            stmt.setInt(6, item.getId());
            stmt.setString(7, username);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean removeItem(long itemKey, String username) throws SQLException {
        String sql = """
            WITH deleted_person AS (
                DELETE FROM persons 
                WHERE key = ? AND owner_id = (SELECT id FROM users WHERE username = ?)
                RETURNING coordinates_id, location_id
            )
            SELECT coordinates_id, location_id FROM deleted_person
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, itemKey);
            stmt.setString(2, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                long coordId = rs.getLong("coordinates_id");
                long locId = rs.getLong("location_id");

                try (PreparedStatement coordStmt = getConnection().prepareStatement("DELETE FROM coordinates WHERE id = ?");
                     PreparedStatement locStmt = getConnection().prepareStatement("DELETE FROM locations WHERE id = ?")) {
                    coordStmt.setLong(1, coordId);
                    locStmt.setLong(1, locId);
                    coordStmt.executeUpdate();
                    locStmt.executeUpdate();
                }
                return true;
            }
            return false;
        }
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        Coordinates coords = new Coordinates(
            rs.getLong("coord_x"),
            rs.getFloat("coord_y")
        );

        Location location = new Location(
            rs.getInt("loc_x"),
            rs.getLong("loc_y"),
            rs.getFloat("loc_z"),
            rs.getString("loc_name")
        );

        String hairColorStr = rs.getString("hair_color");
        Color hairColor = hairColorStr != null ? Color.valueOf(hairColorStr) : null;

        String nationalityStr = rs.getString("nationality");
        Country nationality = nationalityStr != null ? Country.valueOf(nationalityStr) : null;

        return new Person(
            rs.getInt("id"),
            rs.getString("name"),
            coords,
            rs.getTimestamp("creation_date").toInstant().atZone(java.time.ZoneId.systemDefault()),
            rs.getFloat("height"),
            rs.getLong("weight"),
            hairColor,
            nationality,
            location
        );
    }

    private int getKeyFromResultSet(ResultSet rs) throws SQLException {
        return rs.getInt("key");
    }

    public Map<Person, Integer> getAllItems() throws SQLException {
        String sql = """
            SELECT p.*, u.username,
                   c.x as coord_x, c.y as coord_y,
                   l.x as loc_x, l.y as loc_y, l.z as loc_z, l.name as loc_name
            FROM persons p
            JOIN users u ON p.owner_id = u.id
            JOIN coordinates c ON p.coordinates_id = c.id
            JOIN locations l ON p.location_id = l.id
            ORDER BY p.id
        """;

        Map<Person, Integer> itemsWithKeys = new HashMap<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Person person = mapResultSetToPerson(rs);
                int key = getKeyFromResultSet(rs);
                itemsWithKeys.put(person, key);
            }
        }
        return itemsWithKeys;
    }

    public Map<Person, Integer> getItemsByUsername(String name) throws SQLException {
        String sql = """
            SELECT p.*, u.username,
                   c.x as coord_x, c.y as coord_y,
                   l.x as loc_x, l.y as loc_y, l.z as loc_z, l.name as loc_name
            FROM persons p
            JOIN users u ON p.owner_id = u.id
            JOIN coordinates c ON p.coordinates_id = c.id
            JOIN locations l ON p.location_id = l.id
            WHERE u.username = ?
            ORDER BY p.id
        """;

        Map<Person, Integer> itemsWithKeys = new HashMap<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            stmt.setString(1, name);

            while (rs.next()) {
                Person person = mapResultSetToPerson(rs);
                int key = getKeyFromResultSet(rs);
                itemsWithKeys.put(person, key);
            }
        }
        return itemsWithKeys;
    }

    public String clearUserItems(String username) throws SQLException {
        String sql = """
            WITH deleted_persons AS (
                DELETE FROM persons 
                WHERE owner_id = (SELECT id FROM users WHERE username = ?)
                RETURNING coordinates_id, location_id
            )
            SELECT coordinates_id, location_id FROM deleted_persons
        """;

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            List<Long> coordIds = new ArrayList<>();
            List<Long> locIds = new ArrayList<>();
            while (rs.next()) {
                coordIds.add(rs.getLong("coordinates_id"));
                locIds.add(rs.getLong("location_id"));
            }

            if (!coordIds.isEmpty()) {
                String coordIds_str = String.join(",", coordIds.stream().map(String::valueOf).toList());
                String locIds_str = String.join(",", locIds.stream().map(String::valueOf).toList());
                
                try (Statement batchStmt = getConnection().createStatement()) {
                    batchStmt.executeUpdate("DELETE FROM coordinates WHERE id IN (" + coordIds_str + ")");
                    batchStmt.executeUpdate("DELETE FROM locations WHERE id IN (" + locIds_str + ")");
                }
                return "";
            }
            return "Error with data base";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
} 