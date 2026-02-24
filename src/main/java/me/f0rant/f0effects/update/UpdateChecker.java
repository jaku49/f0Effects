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
                HttpURLConnection connection = (HttpURLConnection) new URL(versionURL).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                    String line = reader.readLine();
                    if (line == null || line.isBlank()) return;

                    String[] parts = line.split("\\|");
                    latestVersion = parts[0].trim();
                    if (parts.length > 1) {
                        downloadUrl = parts[1].trim();
                    }
                }

                String currentVersion = plugin.getDescription().getVersion().trim();
                if (isNewerVersion(currentVersion, latestVersion)) {
                    updateAvailable = true;
                }
                checked = true;
            } catch (Exception ignored) {}
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
        } catch (Exception ignored) {}
        return false;
    }

    public boolean isUpdateAvailable() { return updateAvailable; }
    public boolean hasChecked() { return checked; }
    public String getLatestVersion() { return latestVersion; }
    public String getDownloadUrl() { return downloadUrl; }
}