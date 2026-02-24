package me.f0rant.f0effects.listener;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
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

        // Tworzymy "pusty" profil gracza w RAMie
        PlayerData data = plugin.getEffectManager().getPlayerData(uuid);

        // Asynchronicznie ładujemy poziomy z bazy danych
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().loadPlayerSync(data);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Pobieramy dane wychodzącego gracza
        PlayerData data = plugin.getEffectManager().getPlayerData(uuid);

        // Zapisujemy je asynchronicznie do bazy MySQL przed wyjściem
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });

        // ✖ ŁATANIE MEMORY LEAK ✖ : Usuwamy gracza z HashMapy (z RAMu)!
        plugin.getEffectManager().unloadPlayer(uuid);
    }
}