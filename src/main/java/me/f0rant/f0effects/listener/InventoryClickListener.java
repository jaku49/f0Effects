package me.f0rant.f0effects.listener;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
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
        
        String mainTitle = plugin.getLanguageManager().getMsg("gui.main.title");
        String upgTitle = plugin.getLanguageManager().getMsg("gui.upgrade.title");
        String visTitle = plugin.getLanguageManager().getMsg("gui.visuals.title");
        String setTitle = plugin.getLanguageManager().getMsg("gui.settings.title");
        
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
            if (slot == plugin.getConfig().getInt("gui.main.settings-item.slot", 38)) {
                plugin.getSettingsGUI().open(player);
                return;
            }
            // NOWE: Guzik czyszczenia efektów (Wiadro mleka)
            if (slot == plugin.getConfig().getInt("gui.main.clear-item.slot", 39)) {
                PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                data.setSelectedEffect("NONE");
                saveDataAsync(data);
                
                if (data.isChatMessagesEnabled()) {
                    player.sendMessage(plugin.getLanguageManager().getMsg("messages.effect-cleared"));
                }
                
                playConfigSound(player, "effect-selected", data.getVolume());
                plugin.getEffectGUI().open(player);
                return;
            }
            if (slot == plugin.getConfig().getInt("gui.main.close-item.slot")) {
                player.closeInventory();
                return;
            }

            if (plugin.getConfig().getConfigurationSection("effects") == null) return;
            for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
                if (slot == plugin.getConfig().getInt("effects." + key + ".slot")) {
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    data.setSelectedEffect(key);
                    saveDataAsync(data);
                    
                    if (data.isChatMessagesEnabled()) {
                        String effectName = plugin.getLanguageManager().getMsg("effects." + key + ".name");
                        player.sendMessage(plugin.getLanguageManager().getMsg("messages.effect-selected").replace("%effect%", effectName));
                    }
                    
                    playConfigSound(player, "effect-selected", data.getVolume()); 
                    plugin.getEffectGUI().open(player);
                    return;
                }
            }
        } 
        
        // --- OBSŁUGA MENU ULEPSZEŃ ---
        else if (viewTitle.equals(upgTitle)) {
            event.setCancelled(true);
            if (slot == plugin.getConfig().getInt("gui.upgrade.back-item.slot")) {
                plugin.getEffectGUI().open(player);
                return;
            }

            if (plugin.getConfig().getConfigurationSection("effects") == null) return;
            for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
                List<Integer> slots = plugin.getConfig().getIntegerList("effects." + key + ".upgrade-slots");
                if (slots.contains(slot)) {
                    int clickedLevel = slots.indexOf(slot) + 1;
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    int currentLevel = data.getLevel(key);

                    if (currentLevel >= clickedLevel) return;
                    if (currentLevel != clickedLevel - 1) {
                        playConfigSound(player, "missing-previous-level", data.getVolume()); 
                        return;
                    }

                    int cost = plugin.getConfig().getInt("effects." + key + ".levels." + clickedLevel + ".cost");
                    if (plugin.getEconomy().getBalance(player) < cost) {
                        if (data.isChatMessagesEnabled()) {
                            player.sendMessage(plugin.getLanguageManager().getMsg("messages.not-enough-money").replace("%cost%", String.valueOf(cost)));
                        }
                        playConfigSound(player, "not-enough-money", data.getVolume()); 
                        return;
                    }

                    plugin.getEconomy().withdrawPlayer(player, cost);
                    data.setLevel(key, clickedLevel);
                    saveDataAsync(data);
                    
                    if (data.isChatMessagesEnabled()) {
                        String effectName = plugin.getLanguageManager().getMsg("effects." + key + ".name");
                        player.sendMessage(plugin.getLanguageManager().getMsg("messages.level-up")
                                .replace("%effect%", effectName).replace("%level%", String.valueOf(clickedLevel)));
                    }
                    playConfigSound(player, "level-up", data.getVolume()); 
                    plugin.getUpgradeGUI().open(player);
                    return;
                }
            }
        }

        // --- OBSŁUGA MENU KOSMETYKÓW ---
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

            if (plugin.getConfig().getConfigurationSection("visual-effects") == null) return;
            for (String key : plugin.getConfig().getConfigurationSection("visual-effects").getKeys(false)) {
                if (slot == plugin.getConfig().getInt("visual-effects." + key + ".slot")) {
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    String visualName = plugin.getLanguageManager().getMsg("visual-effects." + key + ".name");
                    
                    if (data.getUnlockedVisuals().contains(key)) {
                        data.setSelectedVisual(key);
                        saveDataAsync(data);
                        
                        if (data.isChatMessagesEnabled()) {
                            player.sendMessage(plugin.getLanguageManager().getMsg("messages.visual-selected").replace("%visual%", visualName));
                        }
                        playConfigSound(player, "effect-selected", data.getVolume());
                        plugin.getVisualEffectGUI().open(player);
                    } else {
                        int cost = plugin.getConfig().getInt("visual-effects." + key + ".cost");
                        if (plugin.getEconomy().getBalance(player) < cost) {
                            if (data.isChatMessagesEnabled()) {
                                player.sendMessage(plugin.getLanguageManager().getMsg("messages.not-enough-money").replace("%cost%", String.valueOf(cost)));
                            }
                            playConfigSound(player, "not-enough-money", data.getVolume());
                            return;
                        }

                        plugin.getEconomy().withdrawPlayer(player, cost);
                        data.unlockVisual(key);
                        data.setSelectedVisual(key); 
                        saveDataAsync(data);
                        
                        if (data.isChatMessagesEnabled()) {
                            player.sendMessage(plugin.getLanguageManager().getMsg("messages.visual-bought").replace("%visual%", visualName));
                        }
                        playConfigSound(player, "level-up", data.getVolume());
                        plugin.getVisualEffectGUI().open(player);
                    }
                    return;
                }
            }
        }
        
        else if (viewTitle.equals(setTitle)) {
            event.setCancelled(true);
            
            if (slot == plugin.getConfig().getInt("gui.settings.back-item.slot", 43)) {
                plugin.getEffectGUI().open(player);
                return;
            }
            if (slot == plugin.getConfig().getInt("gui.settings.close-item.slot", 44)) {
                player.closeInventory();
                return;
            }

            PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
            boolean changed = false;

            if (slot == 10) { data.setOwnEffectsEnabled(!data.isOwnEffectsEnabled()); changed = true; }
            if (slot == 12) { data.setOtherEffectsEnabled(!data.isOtherEffectsEnabled()); changed = true; }
            if (slot == 14) { data.setChatMessagesEnabled(!data.isChatMessagesEnabled()); changed = true; }
            if (slot == 16) { data.setBossBarEnabled(!data.isBossBarEnabled()); changed = true; }

            if (slot == 28) { data.setVolume(0.0f); changed = true; }
            if (slot == 29) { data.setVolume(0.25f); changed = true; }
            if (slot == 30) { data.setVolume(0.50f); changed = true; }
            if (slot == 31) { data.setVolume(0.75f); changed = true; }
            if (slot == 32) { data.setVolume(1.0f); changed = true; }

            if (changed) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.4f, 1.2f);
                saveDataAsync(data);
                plugin.getSettingsGUI().open(player); 
            }
        }
    }

    private void playConfigSound(Player player, String path, float playerVolume) {
        if (!plugin.getConfig().contains("sounds." + path)) return;
        if (playerVolume <= 0.0f) return; 
        
        try {
            Sound sound = Sound.valueOf(plugin.getConfig().getString("sounds." + path + ".sound").toUpperCase());
            float baseVolume = (float) plugin.getConfig().getDouble("sounds." + path + ".volume", 1.0);
            float pitch = (float) plugin.getConfig().getDouble("sounds." + path + ".pitch", 1.0);
            
            float finalVolume = baseVolume * playerVolume;
            player.playSound(player.getLocation(), sound, finalVolume, pitch);
        } catch (Exception ignored) {}
    }

    private void saveDataAsync(PlayerData data) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });
    }
}