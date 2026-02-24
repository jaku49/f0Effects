package me.f0rant.f0effects.listener;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerDeathListener implements Listener {
    private final f0Effects plugin;

    public PlayerDeathListener(f0Effects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null && killer != victim) {
            PlayerData data = plugin.getEffectManager().getPlayerData(killer.getUniqueId());
            String activeEffect = data.getSelectedEffect();

            if (activeEffect != null) {
                int level = data.getLevel(activeEffect);
                if (level > 0) {
                    applyEffect(killer, activeEffect, level);
                }
            }
        }
    }

    private void applyEffect(Player player, String effectName, int level) {
        String path = "effects." + effectName + ".levels." + level;
        if (!plugin.getConfig().contains(path)) return;

        int duration = plugin.getConfig().getInt(path + ".duration");
        int amplifier = plugin.getConfig().getInt(path + ".amplifier");
        PotionEffectType type = PotionEffectType.getByName(effectName);

        if (type != null) {
            player.addPotionEffect(new PotionEffect(type, duration, amplifier));
        }
    }
}