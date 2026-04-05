package me.f0rant.f0effects.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EffectsExpansion extends PlaceholderExpansion {
    private final f0Effects plugin;

    public EffectsExpansion(f0Effects plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "f0effects";
    }

    @Override
    public @NotNull String getAuthor() {
        return "f0rant";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        PlayerData data = plugin.getEffectManager().getPlayerData(player.getUniqueId());
        String selected = data.getSelectedEffect();

        // %f0effects_selected%
        if (params.equalsIgnoreCase("selected")) {
            if (selected == null || selected.isEmpty()) return ColorUtil.color("&c---");
            return ColorUtil.color(plugin.getConfig().getString("effects." + selected + ".name", selected));
        }

        // %f0effects_selected_level%
        if (params.equalsIgnoreCase("selected_level")) {
            if (selected == null || selected.isEmpty()) return "0";
            return String.valueOf(data.getLevel(selected));
        }

        // %f0effects_selected_format% (np. ⚡ SPEED (Lvl 2))
        if (params.equalsIgnoreCase("selected_format")) {
            if (selected == null || selected.isEmpty()) return ColorUtil.color("&c---");
            String name = plugin.getConfig().getString("effects." + selected + ".name", selected);
            int lvl = data.getLevel(selected);
            return ColorUtil.color(name + " &8(&eLvl " + lvl + "&8)");
        }

        // %f0effects_selected_duration%
        if (params.equalsIgnoreCase("selected_duration")) {
            if (selected == null || selected.isEmpty()) return "0s";
            int lvl = data.getLevel(selected);
            if (lvl == 0) return "0s";
            int durationTicks = plugin.getConfig().getInt("effects." + selected + ".levels." + lvl + ".duration", 0);
            return (durationTicks / 20) + "s";
        }

        // %f0effects_level_<EFFECT>% (np. %f0effects_level_SPEED%)
        if (params.toLowerCase().startsWith("level_")) {
            String effectName = params.substring(6).toUpperCase();
            return String.valueOf(data.getLevel(effectName));
        }

        // %f0effects_next_cost_<EFFECT>% (np. %f0effects_next_cost_SPEED%)
        if (params.toLowerCase().startsWith("next_cost_")) {
            String effectName = params.substring(10).toUpperCase();
            if (!plugin.getConfig().contains("effects." + effectName)) return "---";
            
            int currentLvl = data.getLevel(effectName);
            if (currentLvl >= 3) return ColorUtil.color("&aMAX"); 
            
            int cost = plugin.getConfig().getInt("effects." + effectName + ".levels." + (currentLvl + 1) + ".cost", 0);
            return String.valueOf(cost);
        }

        // %f0effects_is_max_<EFFECT>% (np. %f0effects_is_max_SPEED%)
        if (params.toLowerCase().startsWith("is_max_")) {
            String effectName = params.substring(7).toUpperCase();
            int currentLvl = data.getLevel(effectName);
            return currentLvl >= 3 ? ColorUtil.color("&aYes") : ColorUtil.color("&cNo");
        }

        return "---";
    }
}