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

public class UpgradeGUI {
    private final f0Effects plugin;

    public UpgradeGUI(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        int size = plugin.getConfig().getInt("gui.upgrade.size", 54);
        String title = ColorUtil.color(plugin.getConfig().getString("gui.upgrade.title"));
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

        for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
            Material mat;
            try {
                mat = Material.valueOf(plugin.getConfig().getString("effects." + key + ".material", "STONE").toUpperCase());
            } catch (Exception e) { mat = Material.STONE; }

            String name = plugin.getConfig().getString("effects." + key + ".name");
            List<Integer> slots = plugin.getConfig().getIntegerList("effects." + key + ".upgrade-slots");
            int currentLevel = data.getLevel(key);

            for (int level = 1; level <= 3; level++) {
                if (slots.size() < level) continue;

                ItemStack item = new ItemStack(mat);
                item.setAmount(level); 
                
                ItemMeta meta = item.getItemMeta();
                if (level > 1) meta.setMaxStackSize(99); 
                
                meta.setDisplayName(ColorUtil.color(name + " &8- &eLevel " + level));
                int cost = plugin.getConfig().getInt("effects." + key + ".levels." + level + ".cost");
                int durationTicks = plugin.getConfig().getInt("effects." + key + ".levels." + level + ".duration");
                int durationSeconds = durationTicks / 20;

                List<String> lore = new ArrayList<>();
                lore.add(ColorUtil.color(plugin.getConfig().getString("gui.upgrade.format.duration", "&7Duration: %duration%s").replace("%duration%", String.valueOf(durationSeconds))));
                lore.add(ColorUtil.color(plugin.getConfig().getString("gui.upgrade.format.cost", "&7Cost: %cost%$").replace("%cost%", String.valueOf(cost))));
                lore.add("");

                if (currentLevel >= level) {
                    lore.add(ColorUtil.color(plugin.getConfig().getString("gui.upgrade.format.unlocked", "&aUnlocked")));
                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else if (currentLevel == level - 1) {
                    lore.add(ColorUtil.color(plugin.getConfig().getString("gui.upgrade.format.can-buy", "&eClick to buy")));
                } else {
                    lore.add(ColorUtil.color(plugin.getConfig().getString("gui.upgrade.format.locked", "&cLocked").replace("%required_level%", String.valueOf(level - 1))));
                }

                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);

                inv.setItem(slots.get(level - 1), item);
            }
        }

        setCustomItem(inv, "gui.upgrade.info-book");
        setCustomItem(inv, "gui.upgrade.back-item");
        setCustomItem(inv, "gui.upgrade.close-item");

        player.openInventory(inv);
    }

    private void setCustomItem(Inventory inv, String path) {
        if (!plugin.getConfig().contains(path)) return; 
        
        Material mat;
        try {
            mat = Material.valueOf(plugin.getConfig().getString(path + ".material", "BARRIER").toUpperCase());
        } catch (Exception e) { mat = Material.BARRIER; }
        
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
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
        inv.setItem(plugin.getConfig().getInt(path + ".slot"), item);
    }
}