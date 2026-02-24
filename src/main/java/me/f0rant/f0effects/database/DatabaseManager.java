package me.f0rant.f0effects.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private final f0Effects plugin;
    private HikariDataSource dataSource;
    private String tablePrefix;

    public DatabaseManager(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        String host = plugin.getConfig().getString("mysql.host");
        int port = plugin.getConfig().getInt("mysql.port");
        String database = plugin.getConfig().getString("mysql.database");
        String username = plugin.getConfig().getString("mysql.username");
        String password = plugin.getConfig().getString("mysql.password");
        this.tablePrefix = plugin.getConfig().getString("mysql.table_prefix", "f0ranking_");

        // Konfiguracja puli połączeń HikariCP
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10); // Max 10 jednoczesnych połączeń
        config.setMinimumIdle(2); // Zawsze 2 połączenia w gotowości
        config.setConnectionTimeout(10000); // 10 sekund timeoutu

        dataSource = new HikariDataSource(config);

        try {
            createTable();
            plugin.getLogger().info("Podlaczono do bazy MySQL (HikariCP - Zoptymalizowano)!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Blad podczas laczenia z MySQL!");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().info("Rozlaczono z baza MySQL.");
        }
    }

    private void createTable() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS " + tablePrefix + "data (" +
                             "uuid VARCHAR(36) PRIMARY KEY, " +
                             "selected_effect VARCHAR(32), " +
                             "speed_level INT DEFAULT 0, " +
                             "resistance_level INT DEFAULT 0, " +
                             "regeneration_level INT DEFAULT 0" +
                             ")")) {
            ps.executeUpdate();
        }
    }

    public void savePlayerSync(PlayerData data) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "REPLACE INTO " + tablePrefix + "data (uuid, selected_effect, speed_level, resistance_level, regeneration_level) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, data.getUuid().toString());
            ps.setString(2, data.getSelectedEffect());
            ps.setInt(3, data.getLevel("SPEED"));
            ps.setInt(4, data.getLevel("RESISTANCE"));
            ps.setInt(5, data.getLevel("REGENERATION"));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerSync(PlayerData data) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM " + tablePrefix + "data WHERE uuid = ?")) {
            ps.setString(1, data.getUuid().toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data.setSelectedEffect(rs.getString("selected_effect"));
                    data.setLevel("SPEED", rs.getInt("speed_level"));
                    data.setLevel("RESISTANCE", rs.getInt("resistance_level"));
                    data.setLevel("REGENERATION", rs.getInt("regeneration_level"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}