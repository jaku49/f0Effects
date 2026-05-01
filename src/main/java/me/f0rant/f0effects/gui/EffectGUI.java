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

import java.util.List;

public class EffectGUI {
    private final f0Effects plugin;

    public EffectGUI(f0Effects plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        int size = plugin.getConfig().getInt("gui.main.size", 45);
        String title = plugin.getLanguageManager().getMsg("gui.main.title");
        Inventory inv = Bukkit.createInventory(null, size, title);

        PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
        String selectedEffect = data.getSelectedEffect();

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(ColorUtil.color("&7"));
            glassMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, glass);

        if (plugin.getConfig().getConfigurationSection("effects") != null) {
            for (String key : plugin.getConfig().getConfigurationSection("effects").getKeys(false)) {
                Material mat;
                try {
                    mat = Material.valueOf(plugin.getConfig().getString("effects." + key + ".material", "STONE").toUpperCase());
                } catch (Exception e) { mat = Material.STONE; }

                ItemStack item = new ItemStack(mat);
                ItemMeta meta = item.getItemMeta();
                
                meta.setDisplayName(plugin.getLanguageManager().getMsg("effects." + key + ".name"));
                
                List<String> lore = plugin.getLanguageManager().getLore("effects." + key + ".lore");
                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, lore.get(i).replace("%level%", String.valueOf(data.getLevel(key))));
                }
                if (!lore.isEmpty()) meta.setLore(lore);

                if (key.equals(selectedEffect)) {
                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);

                inv.setItem(plugin.getConfig().getInt("effects." + key + ".slot"), item);
            }
        }

        setCustomItem(inv, "gui.main.info-book");
        setCustomItem(inv, "gui.main.upgrade-item");
        setCustomItem(inv, "gui.main.clear-item");
        setCustomItem(inv, "gui.main.visuals-item");
        setCustomItem(inv, "gui.main.settings-item"); 
        setCustomItem(inv, "gui.main.close-item");

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
        if (meta != null) {
            meta.setDisplayName(plugin.getLanguageManager().getMsg(path + ".name"));
            List<String> lore = plugin.getLanguageManager().getLore(path + ".lore");
            if (!lore.isEmpty()) meta.setLore(lore);
            
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }

        inv.setItem(plugin.getConfig().getInt(path + ".slot"), item);
    }
}