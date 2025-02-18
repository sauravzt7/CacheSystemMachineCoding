package com.machinecoding.backend;

import com.machinecoding.model.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLiteStorageBackend<K, V> implements StorageBackend<K, CacheEntry<V>> {
    private final String databaseUrl;
    private final String tableName;
    private static final Logger logger = LoggerFactory.getLogger(SQLiteStorageBackend.class);


    public SQLiteStorageBackend(String databaseUrl, String tableName) {
        this.databaseUrl = databaseUrl;
        this.tableName = tableName;
        initializeDatabase();
    }

    // Initialize the database with the new schema
    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             Statement statement = connection.createStatement()) {
            // Add expiration_time column to store expiration timestamps
            String createTableQuery = String.format(
                    "CREATE TABLE IF NOT EXISTS %s (key TEXT PRIMARY KEY, value TEXT, expiration_time INTEGER)", tableName
            );
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }

    @Override
    public void put(K key, CacheEntry<V> entry) {
        String query = String.format("INSERT OR REPLACE INTO %s (key, value, expiration_time) VALUES (?, ?, ?)", tableName);
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, key.toString());
            statement.setString(2, entry.getValue().toString());
            statement.setLong(3, entry.getExpirationTime()); // Add expiration time
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert data into SQLite database", e);
        }
    }

    @Override
    public CacheEntry<V> get(K key) {
        String query = String.format("SELECT value, expiration_time FROM %s WHERE key = ?", tableName);
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, key.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                @SuppressWarnings("unchecked")
                V value = (V) resultSet.getString("value"); // Adjust type casting as needed
                long expirationTime = resultSet.getLong("expiration_time");
                return new CacheEntry<>(value, expirationTime);
            }
            logger.info("Fetched value for key {} from SQLite table: {}", key, tableName);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve data from SQLite database", e);
        }
        return null; // Entry not found
    }

    @Override
    public void remove(K key) {
        String query = String.format("DELETE FROM %s WHERE key = ?", tableName);
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, key.toString());
            statement.executeUpdate();
            logger.info("Removed key {} from SQLite table: {}", key, tableName);
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to delete data from SQLite database", e);
        }
    }

    @Override
    public void clear() {
        String query = String.format("DELETE FROM %s", tableName);
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            logger.info("Cleared SQLite table: {}", tableName);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear SQLite database", e);
        }
    }

    @Override
    public Map<K, CacheEntry<V>> load() {
        Map<K, CacheEntry<V>> data = new HashMap<>();
        String query = String.format("SELECT key, value, expiration_time FROM %s", tableName);
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                @SuppressWarnings("unchecked")
                K key = (K) resultSet.getString("key");
                @SuppressWarnings("unchecked")
                V value = (V) resultSet.getString("value");
                long expirationTime = resultSet.getLong("expiration_time");

                data.put(key, new CacheEntry<>(value, expirationTime)); // Create CacheEntry with expiration time
            }
            logger.info("Loaded {} entries from SQLite table: {}", data.size(), tableName);
        } catch (SQLException e) {
            logger.error("Failed to load data from SQLite database", e);
            throw new RuntimeException("Failed to load data from SQLite database", e);
        }
        return data;
    }
}
