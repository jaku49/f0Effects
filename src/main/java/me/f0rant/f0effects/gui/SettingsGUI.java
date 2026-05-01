package me.f0rant.f0effects.gui;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SettingsGUI {
    private final f0Effects plugin;

    public SettingsGUI(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        int size = plugin.getConfig().getInt("gui.settings.size", 45);
        String title = plugin.getLanguageManager().getMsg("gui.settings.title");
        Inventory inv = Bukkit.createInventory(null, size, title);

        PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(ColorUtil.color("&7"));
            glassMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, glass);

        inv.setItem(10, createToggleItem("own-effects", data.isOwnEffectsEnabled()));
        inv.setItem(12, createToggleItem("other-effects", data.isOtherEffectsEnabled()));
        inv.setItem(14, createToggleItem("chat-messages", data.isChatMessagesEnabled()));
        inv.setItem(16, createToggleItem("bossbar", data.isBossBarEnabled()));

        float currentVol = data.getVolume();
        float[] thresholds = {0.0f, 0.25f, 0.50f, 0.75f, 1.0f};
        int[] volSlots = {28, 29, 30, 31, 32};
        
        for (int i = 0; i < 5; i++) {
            float th = thresholds[i];
            boolean isSelected = (currentVol == th);
            boolean isFilled = (currentVol >= th);

            Material mat = isFilled ? (th == 0.0f ? Material.RED_DYE : Material.LIME_DYE) : Material.GRAY_DYE;
            ItemStack volItem = new ItemStack(mat);
            ItemMeta volMeta = volItem.getItemMeta();

            int percent = (int) (th * 100);
            volMeta.setDisplayName(plugin.getLanguageManager().getMsg("gui.settings.volume-title.name").replace("%percent%", String.valueOf(percent)));
            
            List<String> lore = new ArrayList<>(plugin.getLanguageManager().getLore("gui.settings.volume-title.lore"));
            lore.add("");
            lore.add(plugin.getLanguageManager().getMsg(isSelected ? "gui.settings.volume-segment.selected" : "gui.settings.volume-segment.not-selected"));
            
            if (isSelected) {
                volMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
                volMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            volMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
            volMeta.setLore(lore);
            volItem.setItemMeta(volMeta);
            inv.setItem(volSlots[i], volItem);
        }

        setCustomItem(inv, "gui.settings.back-item");
        setCustomItem(inv, "gui.settings.close-item");

        player.openInventory(inv);
    }

    private ItemStack createToggleItem(String path, boolean state) {
        Material mat = state ? Material.LIME_DYE : Material.GRAY_DYE;
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(plugin.getLanguageManager().getMsg("gui.settings." + path + ".name"));
        
        List<String> lore = new ArrayList<>();
        String statusText = plugin.getLanguageManager().getMsg(state ? "gui.settings.status-on" : "gui.settings.status-off");
        
        for (String line : plugin.getLanguageManager().getLore("gui.settings." + path + ".lore")) {
            lore.add(line.replace("%status%", statusText));
        }
        
        if (state) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private void setCustomItem(Inventory inv, String path) {
        if (!plugin.getConfig().contains(path)) return;
        Material mat;
        try {
            mat = Material.valueOf(plugin.getConfig().getString(path + ".material", "BARRIER").toUpperCase());
        } catch (Exception e) { mat = Material.BARRIER; }
        
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(plugin.getLanguageManager().getMsg(path + ".name"));
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        inv.setItem(plugin.getConfig().getInt(path + ".slot"), item);
    }
}