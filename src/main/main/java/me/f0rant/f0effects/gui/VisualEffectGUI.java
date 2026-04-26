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

public class VisualEffectGUI {
    private final f0Effects plugin;

    public VisualEffectGUI(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        int size = plugin.getConfig().getInt("gui.visuals.size", 45);
        String title = ColorUtil.color(plugin.getConfig().getString("gui.visuals.title"));
        Inventory inv = Bukkit.createInventory(null, size, title);

        PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
        String selectedVisual = data.getSelectedVisual();

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(ColorUtil.color("&7"));
            glassMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, glass);

        if (plugin.getConfig().getConfigurationSection("visual-effects") == null) return;

        for (String key : plugin.getConfig().getConfigurationSection("visual-effects").getKeys(false)) {
            String path = "visual-effects." + key;
            
            Material mat;
            try {
                mat = Material.valueOf(plugin.getConfig().getString(path + ".material", "PAPER").toUpperCase());
            } catch (IllegalArgumentException e) {
                mat = Material.PAPER;
            }

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

            meta.setDisplayName(ColorUtil.color(plugin.getConfig().getString(path + ".name")));
            List<String> lore = new ArrayList<>();
            
            if (data.getUnlockedVisuals().contains(key)) {
                lore.add(ColorUtil.color(plugin.getConfig().getString("gui.visuals.format.unlocked")));
                
                if (key.equals(selectedVisual)) {
                    lore.add(ColorUtil.color(plugin.getConfig().getString("gui.visuals.format.selected")));
                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else {
                    lore.add(ColorUtil.color(plugin.getConfig().getString("gui.visuals.format.click-to-select")));
                }
            } else {
                int cost = plugin.getConfig().getInt(path + ".cost");
                lore.add(ColorUtil.color(plugin.getConfig().getString("gui.visuals.format.cost").replace("%cost%", String.valueOf(cost))));
                lore.add(ColorUtil.color(plugin.getConfig().getString("gui.visuals.format.click-to-buy")));
            }

            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);

            inv.setItem(plugin.getConfig().getInt(path + ".slot"), item);
        }
        
        setCustomItem(inv, "gui.visuals.back-item");
        setCustomItem(inv, "gui.visuals.close-item");

        player.openInventory(inv);
    }

    private void setCustomItem(Inventory inv, String path) {
        if (!plugin.getConfig().contains(path)) return;
        
        Material mat;
        try {
            mat = Material.valueOf(plugin.getConfig().getString(path + ".material", "BARRIER").toUpperCase());
        } catch (Exception e) { 
            mat = Material.BARRIER; 
        }
        
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtil.color(plugin.getConfig().getString(path + ".name", "Item")));
            
            if (plugin.getConfig().contains(path + ".lore")) {
                List<String> lore = new ArrayList<>();
                for (String s : plugin.getConfig().getStringList(path + ".lore")) {
                    lore.add(ColorUtil.color(s));
                }
                meta.setLore(lore);
            }
            
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
            
            item.setItemMeta(meta); 
        }
        
        inv.setItem(plugin.getConfig().getInt(path + ".slot"), item);
    }
}