package me.f0rant.f0effects.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseManager {
    private final f0Effects plugin;
    private HikariDataSource dataSource;
    private String tablePrefix;

    public DatabaseManager(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        String type = plugin.getConfig().getString("storage-type", "SQLITE").toUpperCase();
        this.tablePrefix = plugin.getConfig().getString("mysql.table_prefix", "f0effects_");
        
        HikariConfig config = new HikariConfig();

        if (type.equals("MYSQL")) {
            config.setJdbcUrl("jdbc:mysql://" + plugin.getConfig().getString("mysql.host") + ":" + 
                    plugin.getConfig().getInt("mysql.port") + "/" + plugin.getConfig().getString("mysql.database"));
            config.setUsername(plugin.getConfig().getString("mysql.username"));
            config.setPassword(plugin.getConfig().getString("mysql.password"));
        } else {
            File dbFile = new File(plugin.getDataFolder(), "database.db");
            config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        }

        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);

        try {
            createTables();
            plugin.getLogger().info("Successfully connected to " + type + " database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }

    private void createTables() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "data (" +
                    "uuid VARCHAR(36) PRIMARY KEY, nickname VARCHAR(16), selected_effect VARCHAR(32), selected_visual VARCHAR(32))").executeUpdate();
            
            try {
                conn.prepareStatement("ALTER TABLE " + tablePrefix + "data ADD COLUMN selected_visual VARCHAR(32)").executeUpdate();
            } catch (SQLException ignored) {
            }

            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "levels (" +
                    "uuid VARCHAR(36), effect_key VARCHAR(32), level INT, PRIMARY KEY(uuid, effect_key))").executeUpdate();

            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "visuals (" +
                    "uuid VARCHAR(36), visual_key VARCHAR(32), PRIMARY KEY(uuid, visual_key))").executeUpdate();
        }
    }

    public void savePlayerSync(PlayerData data) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement psData = conn.prepareStatement(
                    "REPLACE INTO " + tablePrefix + "data (uuid, nickname, selected_effect, selected_visual) VALUES (?, ?, ?, ?)");
            psData.setString(1, data.getUuid().toString());
            psData.setString(2, data.getNickname());
            psData.setString(3, data.getSelectedEffect());
            psData.setString(4, data.getSelectedVisual());
            psData.executeUpdate();

            // Zapis poziomów mikstur
            PreparedStatement psLevel = conn.prepareStatement(
                    "REPLACE INTO " + tablePrefix + "levels (uuid, effect_key, level) VALUES (?, ?, ?)");
            for (Map.Entry<String, Integer> entry : data.getEffectLevels().entrySet()) {
                psLevel.setString(1, data.getUuid().toString());
                psLevel.setString(2, entry.getKey());
                psLevel.setInt(3, entry.getValue());
                psLevel.addBatch();
            }
            psLevel.executeBatch();

            // Zapis odblokowanych wizualiów
            if (!data.getUnlockedVisuals().isEmpty()) {
                PreparedStatement psVisual = conn.prepareStatement(
                        "REPLACE INTO " + tablePrefix + "visuals (uuid, visual_key) VALUES (?, ?)");
                for (String visual : data.getUnlockedVisuals()) {
                    psVisual.setString(1, data.getUuid().toString());
                    psVisual.setString(2, visual);
                    psVisual.addBatch();
                }
                psVisual.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerSync(PlayerData data) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement psData = conn.prepareStatement("SELECT * FROM " + tablePrefix + "data WHERE uuid = ?");
            psData.setString(1, data.getUuid().toString());
            ResultSet rsData = psData.executeQuery();
            if (rsData.next()) {
                data.setNickname(rsData.getString("nickname"));
                data.setSelectedEffect(rsData.getString("selected_effect"));
                
                String selectedVisual = rsData.getString("selected_visual");
                if (selectedVisual != null) data.setSelectedVisual(selectedVisual);
            }

            PreparedStatement psLevel = conn.prepareStatement("SELECT * FROM " + tablePrefix + "levels WHERE uuid = ?");
            psLevel.setString(1, data.getUuid().toString());
            ResultSet rsLevel = psLevel.executeQuery();
            while (rsLevel.next()) {
                data.setLevel(rsLevel.getString("effect_key"), rsLevel.getInt("level"));
            }

            PreparedStatement psVisual = conn.prepareStatement("SELECT * FROM " + tablePrefix + "visuals WHERE uuid = ?");
            psVisual.setString(1, data.getUuid().toString());
            ResultSet rsVisual = psVisual.executeQuery();
            while (rsVisual.next()) {
                data.unlockVisual(rsVisual.getString("visual_key"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}