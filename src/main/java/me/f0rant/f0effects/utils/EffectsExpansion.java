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
        if (data == null) return "";

        String selected = data.getSelectedEffect();
        String selectedVisual = data.getSelectedVisual();

        // Pobieranie zwrotów i natychmiastowe ich kolorowanie pod kątem kompatybilności z TAB
        String noneSelected = plugin.getLanguageManager().getMsg("placeholders.none-selected");
        String trueFormat = plugin.getLanguageManager().getMsg("placeholders.boolean-true");
        String falseFormat = plugin.getLanguageManager().getMsg("placeholders.boolean-false");
        String maxFormat = plugin.getLanguageManager().getMsg("placeholders.max-level");

        if (params.equalsIgnoreCase("test")) {
            return ColorUtil.color("&aF0Effects dziala!");
        }

        if (params.equalsIgnoreCase("selected")) {
            if (selected == null || selected.equals("NONE")) return ColorUtil.color(noneSelected);
            return ColorUtil.color(plugin.getLanguageManager().getMsg("effects." + selected + ".name"));
        }

        if (params.equalsIgnoreCase("selected_visual")) {
            if (selectedVisual == null || selectedVisual.equals("NONE")) return ColorUtil.color(noneSelected);
            return ColorUtil.color(plugin.getLanguageManager().getMsg("visual-effects." + selectedVisual + ".name"));
        }

        if (params.equalsIgnoreCase("unlocked_visuals_count")) {
            return String.valueOf(data.getUnlockedVisuals().size());
        }

        if (params.equalsIgnoreCase("selected_level")) {
            if (selected == null || selected.equals("NONE")) return "0";
            return String.valueOf(data.getLevel(selected));
        }

        if (params.equalsIgnoreCase("selected_format")) {
            if (selected == null || selected.equals("NONE")) return ColorUtil.color(noneSelected);
            
            String name = plugin.getLanguageManager().getMsg("effects." + selected + ".name");
            int lvl = data.getLevel(selected);
            String format = plugin.getLanguageManager().getMsg("placeholders.format");
            
            return ColorUtil.color(format.replace("%effect_name%", name).replace("%level%", String.valueOf(lvl)));
        }

        if (params.equalsIgnoreCase("selected_duration")) {
            if (selected == null || selected.equals("NONE")) return "0s";
            
            int lvl = data.getLevel(selected);
            if (lvl == 0) return "0s";
            
            int durationTicks = plugin.getConfig().getInt("effects." + selected + ".levels." + lvl + ".duration", 0);
            int seconds = durationTicks / 20;
            String format = plugin.getLanguageManager().getMsg("placeholders.duration-format");
            
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
            return ColorUtil.color(currentLvl >= 3 ? trueFormat : falseFormat);
        }

        if (params.equalsIgnoreCase("setting_own_effects")) {
            return ColorUtil.color(data.isOwnEffectsEnabled() ? trueFormat : falseFormat);
        }
        if (params.equalsIgnoreCase("setting_other_effects")) {
            return ColorUtil.color(data.isOtherEffectsEnabled() ? trueFormat : falseFormat);
        }
        if (params.equalsIgnoreCase("setting_chat")) {
            return ColorUtil.color(data.isChatMessagesEnabled() ? trueFormat : falseFormat);
        }
        if (params.equalsIgnoreCase("setting_bossbar")) {
            return ColorUtil.color(data.isBossBarEnabled() ? trueFormat : falseFormat);
        }
        
        if (params.equalsIgnoreCase("setting_volume")) {
            return (int) (data.getVolume() * 100) + "%";
        }

        return null; 
    }
}