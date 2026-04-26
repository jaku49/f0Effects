package me.f0rant.f0effects.listener;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;
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

            // 1. Mikstury
            String activeEffect = data.getSelectedEffect();
            if (activeEffect != null) {
                int level = data.getLevel(activeEffect);
                if (level > 0) {
                    applyEffect(killer, activeEffect, level);
                }
            }

            // 2. Efekty wizualne (NAPRAWIONE)
            String activeVisual = data.getSelectedVisual();
            if (activeVisual != null) {
                applyVisuals(killer, victim, activeVisual);
            }
        }
    }

    private void applyVisuals(Player killer, Player victim, String visualKey) {
        // NAPRAWA: Zmiana ścieżki na 'visual-effects'
        String path = "visual-effects." + visualKey + ".visuals";
        if (!plugin.getConfig().contains(path)) return;

        if (plugin.getConfig().getBoolean(path + ".lightning", false)) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
        }

        if (plugin.getConfig().getBoolean(path + ".elder-guardian", false)) {
            killer.spawnParticle(Particle.ELDER_GUARDIAN, killer.getLocation(), 1);
        }

        if (plugin.getConfig().getBoolean(path + ".firework", false)) {
            Firework fw = victim.getWorld().spawn(victim.getLocation(), Firework.class);
            FireworkMeta meta = fw.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder()
                    .withColor(Color.AQUA, Color.WHITE)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withFlicker()
                    .build());
            meta.setPower(0);
            fw.setFireworkMeta(meta);
        }

        if (plugin.getConfig().contains(path + ".particles")) {
            for (String particleName : plugin.getConfig().getStringList(path + ".particles")) {
                try {
                    Particle particle = Particle.valueOf(particleName.toUpperCase());
                    victim.getWorld().spawnParticle(particle, victim.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Nieznana czasteczka: " + particleName);
                }
            }
        }

        if (plugin.getConfig().contains(path + ".sound")) {
            try {
                Sound sound = Sound.valueOf(plugin.getConfig().getString(path + ".sound").toUpperCase());
                killer.playSound(killer.getLocation(), sound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Nieznany dzwiek: " + plugin.getConfig().getString(path + ".sound"));
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
            plugin.getBossBarManager().showBossBar(player, effectName, level, duration);
        }
    }
}