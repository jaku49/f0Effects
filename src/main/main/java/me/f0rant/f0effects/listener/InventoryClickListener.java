package me.f0rant.f0effects.listener;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class InventoryClickListener implements Listener {
    private final f0Effects plugin;

    public InventoryClickListener(f0Effects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        String mainTitle = ColorUtil.color(plugin.getConfig().getString("gui.main.title"));
        String upgTitle = ColorUtil.color(plugin.getConfig().getString("gui.upgrade.title"));
        String visTitle = ColorUtil.color(plugin.getConfig().getString("gui.visuals.title"));
        String viewTitle = event.getView().getTitle();
        int slot = event.getSlot();

        // --- OBSŁUGA MENU GŁÓWNEGO ---
        if (viewTitle.equals(mainTitle)) {
            event.setCancelled(true);
            
            if (slot == plugin.getConfig().getInt("gui.main.upgrade-item.slot")) {
                plugin.getUpgradeGUI().open(player);
                return;
            }
            
            if (slot == plugin.getConfig().getInt("gui.main.visuals-item.slot")) {
                plugin.getVisualEffectGUI().open(player);
                return;
            }
            
            if (slot == plugin.getConfig().getInt("gui.main.close-item.slot")) {
                player.closeInventory();
                return;
            }

            for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
                if (slot == plugin.getConfig().getInt("effects." + key + ".slot")) {
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    data.setSelectedEffect(key);
                    saveDataAsync(data);
                    
                    String message = plugin.getConfig().getString("messages.effect-selected")
                            .replace("%effect%", plugin.getConfig().getString("effects." + key + ".name"));
                    player.sendMessage(ColorUtil.color(message));
                    playConfigSound(player, "effect-selected"); 
                    plugin.getEffectGUI().open(player);
                    return;
                }
            }
        } 
        
        else if (viewTitle.equals(upgTitle)) {
            event.setCancelled(true);
            if (slot == plugin.getConfig().getInt("gui.upgrade.back-item.slot")) {
                plugin.getEffectGUI().open(player);
                return;
            }

            for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
                List<Integer> slots = plugin.getConfig().getIntegerList("effects." + key + ".upgrade-slots");
                if (slots.contains(slot)) {
                    int clickedLevel = slots.indexOf(slot) + 1;
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    int currentLevel = data.getLevel(key);

                    if (currentLevel >= clickedLevel) return;
                    if (currentLevel != clickedLevel - 1) {
                        playConfigSound(player, "missing-previous-level"); 
                        return;
                    }

                    int cost = plugin.getConfig().getInt("effects." + key + ".levels." + clickedLevel + ".cost");
                    if (plugin.getEconomy().getBalance(player) < cost) {
                        player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.not-enough-money").replace("%cost%", String.valueOf(cost))));
                        playConfigSound(player, "not-enough-money"); 
                        return;
                    }

                    plugin.getEconomy().withdrawPlayer(player, cost);
                    data.setLevel(key, clickedLevel);
                    saveDataAsync(data);
                    player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.level-up")
                            .replace("%effect%", plugin.getConfig().getString("effects." + key + ".name"))
                            .replace("%level%", String.valueOf(clickedLevel))));
                    playConfigSound(player, "level-up"); 
                    plugin.getUpgradeGUI().open(player);
                    return;
                }
            }
        }

        else if (viewTitle.equals(visTitle)) {
            event.setCancelled(true);
            

            if (slot == plugin.getConfig().getInt("gui.visuals.back-item.slot")) {
                plugin.getEffectGUI().open(player);
                return;
            }
            if (slot == plugin.getConfig().getInt("gui.visuals.close-item.slot")) {
                player.closeInventory();
                return;
            }

            for (String key : plugin.getConfig().getConfigurationSection("visual-effects").getKeys(false)) {
                if (slot == plugin.getConfig().getInt("visual-effects." + key + ".slot")) {
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    
                    if (data.getUnlockedVisuals().contains(key)) {
                        data.setSelectedVisual(key);
                        saveDataAsync(data);
                        player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.visual-selected")
                                .replace("%visual%", plugin.getConfig().getString("visual-effects." + key + ".name"))));
                        playConfigSound(player, "effect-selected");
                        plugin.getVisualEffectGUI().open(player);
                    } else {
                        // Próba zakupu
                        int cost = plugin.getConfig().getInt("visual-effects." + key + ".cost");
                        if (plugin.getEconomy().getBalance(player) < cost) {
                            player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.not-enough-money").replace("%cost%", String.valueOf(cost))));
                            playConfigSound(player, "not-enough-money");
                            return;
                        }

                        plugin.getEconomy().withdrawPlayer(player, cost);
                        data.unlockVisual(key);
                        data.setSelectedVisual(key); 
                        saveDataAsync(data);
                        player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.visual-bought")
                                .replace("%visual%", plugin.getConfig().getString("visual-effects." + key + ".name"))));
                        playConfigSound(player, "level-up");
                        plugin.getVisualEffectGUI().open(player);
                    }
                    return;
                }
            }
        }
    }

    private void playConfigSound(Player player, String path) {
        if (!plugin.getConfig().contains("sounds." + path)) return;
        try {
            Sound sound = Sound.valueOf(plugin.getConfig().getString("sounds." + path + ".sound").toUpperCase());
            float volume = (float) plugin.getConfig().getDouble("sounds." + path + ".volume");
            float pitch = (float) plugin.getConfig().getDouble("sounds." + path + ".pitch");
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (Exception ignored) {}
    }

    private void saveDataAsync(PlayerData data) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });
    }
}