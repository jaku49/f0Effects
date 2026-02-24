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
        String viewTitle = event.getView().getTitle();
        int slot = event.getSlot();

        // LOGIKA GŁÓWNEGO MENU (Wybór efektu)
        if (viewTitle.equals(mainTitle)) {
            event.setCancelled(true);
            
            if (slot == plugin.getConfig().getInt("gui.main.upgrade-item.slot")) {
                plugin.getUpgradeGUI().open(player);
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
                    
                    playConfigSound(player, "effect-selected"); // Odtworzenie dźwięku wyboru
                    
                    plugin.getEffectGUI().open(player);
                    return;
                }
            }
        } 
        
        // LOGIKA MENU ULEPSZEŃ
        else if (viewTitle.equals(upgTitle)) {
            event.setCancelled(true);

            if (slot == plugin.getConfig().getInt("gui.upgrade.back-item.slot")) {
                plugin.getEffectGUI().open(player);
                return;
            }
            
            if (slot == plugin.getConfig().getInt("gui.upgrade.close-item.slot")) {
                player.closeInventory();
                return;
            }

            for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
                List<Integer> slots = plugin.getConfig().getIntegerList("effects." + key + ".upgrade-slots");
                
                if (slots.contains(slot)) {
                    int clickedLevel = slots.indexOf(slot) + 1;
                    PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
                    int currentLevel = data.getLevel(key);

                    // Posiada już ten poziom
                    if (currentLevel >= clickedLevel) {
                        player.sendMessage(ColorUtil.color("&#ff0000Posiadasz juz to ulepszenie!"));
                        playConfigSound(player, "missing-previous-level"); // Dźwięk błędu
                        return;
                    }

                    // Próba zakupu z pominięciem poziomu
                    if (currentLevel != clickedLevel - 1) {
                        player.sendMessage(ColorUtil.color("&#ff0000Musisz najpierw kupic poprzedni poziom!"));
                        playConfigSound(player, "missing-previous-level"); // Dźwięk błędu
                        return;
                    }

                    int cost = plugin.getConfig().getInt("effects." + key + ".levels." + clickedLevel + ".cost");
                    
                    // Brak pieniędzy
                    if (plugin.getEconomy().getBalance(player) < cost) {
                        String msg = plugin.getConfig().getString("messages.not-enough-money")
                                .replace("%cost%", String.valueOf(cost));
                        player.sendMessage(ColorUtil.color(msg));
                        playConfigSound(player, "not-enough-money"); // Dźwięk błędu (wieśniak)
                        return;
                    }

                    // Sukces
                    plugin.getEconomy().withdrawPlayer(player, cost);
                    data.setLevel(key, clickedLevel);
                    saveDataAsync(data);

                    String successMsg = plugin.getConfig().getString("messages.level-up")
                            .replace("%effect%", plugin.getConfig().getString("effects." + key + ".name"))
                            .replace("%level%", String.valueOf(clickedLevel));
                    player.sendMessage(ColorUtil.color(successMsg));

                    playConfigSound(player, "level-up"); // Dźwięk ulepszenia (Level-up)

                    plugin.getUpgradeGUI().open(player);
                    return;
                }
            }
        }
    }

    // Metoda pomocnicza do odtwarzania dźwięków
    private void playConfigSound(Player player, String path) {
        if (!plugin.getConfig().contains("sounds." + path)) return;
        try {
            Sound sound = Sound.valueOf(plugin.getConfig().getString("sounds." + path + ".sound").toUpperCase());
            float volume = (float) plugin.getConfig().getDouble("sounds." + path + ".volume");
            float pitch = (float) plugin.getConfig().getDouble("sounds." + path + ".pitch");
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Nie znaleziono dzwieku: " + plugin.getConfig().getString("sounds." + path + ".sound"));
        }
    }

    private void saveDataAsync(PlayerData data) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });
    }
}