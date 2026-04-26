package me.f0rant.f0effects.update;

import me.f0rant.f0effects.f0Effects;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateChecker {

    private final f0Effects plugin;
    private final String versionURL;
    private volatile String latestVersion = null;
    private volatile String downloadUrl = null;
    private volatile boolean updateAvailable = false;
    private volatile boolean checked = false;

    public UpdateChecker(f0Effects plugin, String versionURL) {
        this.plugin = plugin;
        this.versionURL = versionURL;
    }

    public void check() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                plugin.getLogger().info("[f0Effects] Starting update check... Fetching version info from: " + versionURL);
                
                String noCacheUrl = versionURL + "?t=" + System.currentTimeMillis();
                HttpURLConnection connection = (HttpURLConnection) new URL(noCacheUrl).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                    String line = reader.readLine();
                    if (line == null || line.isBlank()) {
                        plugin.getLogger().warning("[f0Effects] Update file on GitHub is empty!");
                        checked = true;
                        return;
                    }

                    plugin.getLogger().info("[f0Effects] Read line from GitHub: " + line);

                    String[] parts = line.split("\\|");
                    latestVersion = parts[0].trim();
                    if (parts.length > 1) {
                        downloadUrl = parts[1].trim();
                    }
                }

                String currentVersion = plugin.getDescription().getVersion().trim();
                
                plugin.getLogger().info("[f0Effects] Latest version from GitHub: '" + latestVersion + "'");
                plugin.getLogger().info("[f0Effects] Installed version (plugin.yml): '" + currentVersion + "'");

                if (isNewerVersion(currentVersion, latestVersion)) {
                    updateAvailable = true;
                    plugin.getLogger().info("[f0Effects] RESULT: A newer version is available!");
                } else {
                    plugin.getLogger().info("[f0Effects] RESULT: Plugin is up to date. No update available.");
                }
                
                checked = true;
            } catch (Exception e) {
                plugin.getLogger().warning("[f0Effects] ERROR while checking for updates: " + e.getMessage());
                checked = true;
            }
        });
    }

    private boolean isNewerVersion(String current, String latest) {
        try {
            String[] currentParts = current.split("\\.");
            String[] latestParts = latest.split("\\.");
            int maxLength = Math.max(currentParts.length, latestParts.length);

            for (int i = 0; i < maxLength; i++) {
                int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i].replaceAll("[^0-9]", "")) : 0;
                int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i].replaceAll("[^0-9]", "")) : 0;
                
                if (latestPart > currentPart) return true;
                if (latestPart < currentPart) return false;
            }
        } catch (Exception e) {
            plugin.getLogger().warning("[f0Effects] ERROR while comparing versions: " + e.getMessage());
        }
        return false;
    }

    public boolean isUpdateAvailable() { return updateAvailable; }
    public boolean hasChecked() { return checked; }
    public String getLatestVersion() { return latestVersion; }
    public String getDownloadUrl() { return downloadUrl; }
}