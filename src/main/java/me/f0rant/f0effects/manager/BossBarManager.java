package me.f0rant.f0effects.manager;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    private final f0Effects plugin;
    private final Map<UUID, BossBar> activeBars = new HashMap<>();
    private final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();

    public BossBarManager(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void showBossBar(Player player, String effectKey, int level, int durationTicks) {
        if (!plugin.getConfig().getBoolean("bossbar.enabled", true)) return;

        UUID uuid = player.getUniqueId();
        removeBossBar(player);

        String effectName = plugin.getConfig().getString("effects." + effectKey + ".name", effectKey);
        String title = plugin.getConfig().getString("bossbar.title")
                .replace("%effect%", effectName)
                .replace("%level%", String.valueOf(level));

        BarColor color = BarColor.valueOf(plugin.getConfig().getString("bossbar.color", "RED").toUpperCase());
        BarStyle style = BarStyle.valueOf(plugin.getConfig().getString("bossbar.style", "SOLID").toUpperCase());

        BossBar bossBar = Bukkit.createBossBar(ColorUtil.color(title), color, style);
        bossBar.addPlayer(player);
        bossBar.setProgress(1.0);
        activeBars.put(uuid, bossBar);

        BukkitRunnable task = new BukkitRunnable() {
            int remaining = durationTicks;

            @Override
            public void run() {
                if (!player.isOnline() || remaining <= 0) {
                    removeBossBar(player);
                    this.cancel();
                    return;
                }

                double progress = (double) remaining / durationTicks;
                if (progress < 0) progress = 0;
                if (progress > 1) progress = 1;
                
                bossBar.setProgress(progress);
                remaining--;
            }
        };

        task.runTaskTimer(plugin, 0L, 1L);
        activeTasks.put(uuid, task);
    }

    public void removeBossBar(Player player) {
        UUID uuid = player.getUniqueId();
        if (activeTasks.containsKey(uuid)) {
            activeTasks.get(uuid).cancel();
            activeTasks.remove(uuid);
        }
        if (activeBars.containsKey(uuid)) {
            BossBar bar = activeBars.get(uuid);
            bar.removeAll();
            activeBars.remove(uuid);
        }
    }
}