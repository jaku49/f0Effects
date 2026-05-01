package me.f0rant.f0effects.listener;
import me.f0rant.f0effects.f0Effects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.List;

public class PlayerJoinQuitListener implements Listener {

    private final f0Effects plugin;

    public PlayerJoinQuitListener(f0Effects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("f0effects.admin") && plugin.getUpdateChecker() != null) {
            if (plugin.getUpdateChecker().isUpdateAvailable()) {
                
                List<String> updateMessage = plugin.getLanguageManager().getLore("messages.update-available");
                
                String current = plugin.getDescription().getVersion();
                String latest = plugin.getUpdateChecker().getLatestVersion();
                String link = plugin.getUpdateChecker().getDownloadUrl();
                if (link == null) link = "https://github.com/jaku49/f0Effects";

                for (String line : updateMessage) {
                    player.sendMessage(line.replace("%current%", current)
                                           .replace("%latest%", latest)
                                           .replace("%link%", link));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getEffectManager() != null && plugin.getDatabaseManager() != null) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    me.f0rant.f0effects.model.PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    plugin.getDatabaseManager().savePlayerSync(data);
                    
                }
            });
        }
    }
}