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

        String noneSelected = plugin.getConfig().getString("placeholders.none-selected", "&c---");
        String trueFormat = plugin.getConfig().getString("placeholders.boolean-true", "&aYes");
        String falseFormat = plugin.getConfig().getString("placeholders.boolean-false", "&cNo");
        String maxFormat = plugin.getConfig().getString("placeholders.max-level", "&aMAX");

        if (params.equalsIgnoreCase("selected")) {
            if (selected == null || selected.isEmpty()) return ColorUtil.color(noneSelected);
            return ColorUtil.color(plugin.getConfig().getString("effects." + selected + ".name", selected));
        }

        if (params.equalsIgnoreCase("selected_level")) {
            if (selected == null || selected.isEmpty()) return "0";
            return String.valueOf(data.getLevel(selected));
        }

        if (params.equalsIgnoreCase("selected_format")) {
            if (selected == null || selected.isEmpty()) return ColorUtil.color(noneSelected);
            
            String name = plugin.getConfig().getString("effects." + selected + ".name", selected);
            int lvl = data.getLevel(selected);
            String format = plugin.getConfig().getString("placeholders.format", "%effect_name% &8(&eLvl %level%&8)");
            
            return ColorUtil.color(format
                    .replace("%effect_name%", name)
                    .replace("%level%", String.valueOf(lvl)));
        }

        if (params.equalsIgnoreCase("selected_duration")) {
            if (selected == null || selected.isEmpty()) return "0s";
            
            int lvl = data.getLevel(selected);
            if (lvl == 0) return "0s";
            
            int durationTicks = plugin.getConfig().getInt("effects." + selected + ".levels." + lvl + ".duration", 0);
            int seconds = durationTicks / 20;
            String format = plugin.getConfig().getString("placeholders.duration-format", "%seconds%s");
            
            return ColorUtil.color(format.replace("%seconds%", String.valueOf(seconds)));
        }

        if (params.toLowerCase().startsWith("level_")) {
            String effectName = params.substring(6).toUpperCase();
            return String.valueOf(data.getLevel(effectName));
        }

        if (params.toLowerCase().startsWith("next_cost_")) {
            String effectName = params.substring(10).toUpperCase();
            if (!plugin.getConfig().contains("effects." + effectName)) return ColorUtil.color(noneSelected);
            
            int currentLvl = data.getLevel(effectName);
            if (currentLvl >= 3) return ColorUtil.color(maxFormat);
            
            int cost = plugin.getConfig().getInt("effects." + effectName + ".levels." + (currentLvl + 1) + ".cost", 0);
            return String.valueOf(cost);
        }

        if (params.toLowerCase().startsWith("is_max_")) {
            String effectName = params.substring(7).toUpperCase();
            int currentLvl = data.getLevel(effectName);
            return currentLvl >= 3 ? ColorUtil.color(trueFormat) : ColorUtil.color(falseFormat);
        }

        return "---";
    }
}