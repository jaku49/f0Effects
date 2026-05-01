package me.f0rant.f0effects.listener;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import org.bukkit.Bukkit;
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
import java.util.List;

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
            PlayerData killerData = plugin.getEffectManager().getPlayerData(killer.getUniqueId());

            String activeEffect = killerData.getSelectedEffect();
            if (activeEffect != null && !activeEffect.equals("NONE")) {
                int level = killerData.getLevel(activeEffect);
                if (level > 0) {
                    applyEffect(killer, killerData, activeEffect, level);
                }
            }

            String activeVisual = killerData.getSelectedVisual();
            if (activeVisual != null && !activeVisual.equals("NONE")) {
                applyVisuals(killer, victim, killerData, activeVisual);
            }
        }
    }

    private void applyEffect(Player player, PlayerData data, String effectName, int level) {
        String path = "effects." + effectName + ".levels." + level;
        if (!plugin.getConfig().contains(path)) return;

        int duration = plugin.getConfig().getInt(path + ".duration");
        int amplifier = plugin.getConfig().getInt(path + ".amplifier");
        PotionEffectType type = PotionEffectType.getByName(effectName);

        if (type != null) {
            player.addPotionEffect(new PotionEffect(type, duration, amplifier));
            
            if (data.isBossBarEnabled()) {
                plugin.getBossBarManager().showBossBar(player, effectName, level, duration);
            }
        }
    }

    private void applyVisuals(Player killer, Player victim, PlayerData killerData, String visualKey) {
        String path = "visual-effects." + visualKey + ".visuals";
        if (!plugin.getConfig().contains(path)) return;

        if (plugin.getConfig().contains(path + ".sound")) {
            float playerVolume = killerData.getVolume();
            if (playerVolume > 0.0f) {
                try {
                    Sound sound = Sound.valueOf(plugin.getConfig().getString(path + ".sound").toUpperCase());
                    killer.playSound(killer.getLocation(), sound, playerVolume, 1.0f);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Unknown sound: " + plugin.getConfig().getString(path + ".sound"));
                }
            }
        }

        boolean doLightning = plugin.getConfig().getBoolean(path + ".lightning", false);
        boolean doGuardian = plugin.getConfig().getBoolean(path + ".elder-guardian", false);
        boolean doFirework = plugin.getConfig().getBoolean(path + ".firework", false);
        List<String> particles = plugin.getConfig().contains(path + ".particles") ? plugin.getConfig().getStringList(path + ".particles") : null;
        
        for (Player onlinePlayer : victim.getWorld().getPlayers()) {
            
            boolean shouldShow = false;
            
            if (onlinePlayer.equals(killer)) {
                shouldShow = killerData.isOwnEffectsEnabled();
            } else {
                PlayerData observerData = plugin.getEffectManager().getPlayerData(onlinePlayer.getUniqueId());
                shouldShow = observerData.isOtherEffectsEnabled();
            }

            if (shouldShow && onlinePlayer.getLocation().distanceSquared(victim.getLocation()) < 10000) {
                
                if (doGuardian && onlinePlayer.equals(killer)) {
                    onlinePlayer.spawnParticle(Particle.ELDER_GUARDIAN, killer.getLocation(), 1);
                }

                if (particles != null) {
                    for (String particleName : particles) {
                        try {
                            Particle particle = Particle.valueOf(particleName.toUpperCase());
                            onlinePlayer.spawnParticle(particle, victim.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
                        } catch (IllegalArgumentException ignored) {}
                    }
                }
            }
        }

        // --- Efekty globalne ---
        if (doLightning) {
             if (killerData.isOwnEffectsEnabled()) {
                 victim.getWorld().strikeLightningEffect(victim.getLocation());
             }
        }

        if (doFirework) {
             if (killerData.isOwnEffectsEnabled()) {
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
        }
    }
}