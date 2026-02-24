package me.f0rant.f0effects.listener;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerJoinQuitListener implements Listener {

    private final f0Effects plugin;

    public PlayerJoinQuitListener(f0Effects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerData data = plugin.getEffectManager().getPlayerData(uuid);
        data.setNickname(player.getName());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().loadPlayerSync(data);
        });

        if (player.hasPermission("f0effects.admin")
                && plugin.getUpdateChecker() != null
                && plugin.getUpdateChecker().hasChecked()
                && plugin.getUpdateChecker().isUpdateAvailable()) {

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                String versionMsg = plugin.getConfig().getString("messages.update-available")
                        .replace("%latest%", plugin.getUpdateChecker().getLatestVersion())
                        .replace("%current%", plugin.getDescription().getVersion());

                player.sendMessage(ColorUtil.color(versionMsg));

                String downloadUrl = plugin.getUpdateChecker().getDownloadUrl();
                if (downloadUrl != null && !downloadUrl.isBlank()) {
                    String linkMsg = plugin.getConfig().getString("messages.update-link")
                            .replace("%url%", downloadUrl);
                    player.sendMessage(ColorUtil.color(linkMsg));
                }
            }, 40L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        plugin.getBossBarManager().removeBossBar(player);

        PlayerData data = plugin.getEffectManager().getPlayerData(uuid);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });
        plugin.getEffectManager().unloadPlayer(uuid);
    }
}