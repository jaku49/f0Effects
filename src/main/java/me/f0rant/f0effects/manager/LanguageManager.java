package me.f0rant.f0effects.manager;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageManager {
    private final f0Effects plugin;
    private FileConfiguration langConfig;

    public LanguageManager(f0Effects plugin) {
        this.plugin = plugin;
        loadLanguage();
    }

    public void loadLanguage() {
        String lang = plugin.getConfig().getString("language", "en").toLowerCase();
        String fileName = "messages_" + lang + ".yml";
        File langFile = new File(plugin.getDataFolder(), fileName);

        if (!langFile.exists()) {
            try {
                plugin.saveResource(fileName, false);
                plugin.getLogger().info("Created language file: " + fileName);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Built-in language not found: " + lang + ". Switching to default (en).");
                plugin.saveResource("messages_en.yml", false);
                langFile = new File(plugin.getDataFolder(), "messages_en.yml");
            }
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public String getMsg(String path) {
        String msg = langConfig.getString(path);
        if (msg == null) return ColorUtil.color("&cMissing path: " + path);
        
        String prefix = langConfig.getString("prefix", ""); 
        return ColorUtil.color(msg.replace("%prefix%", prefix));
    }

    public List<String> getLore(String path) {
        if (!langConfig.contains(path)) return new ArrayList<>(); 
        
        List<String> lore = langConfig.getStringList(path);
        String prefix = langConfig.getString("prefix", "");
        
        return lore.stream()
                .map(line -> ColorUtil.color(line.replace("%prefix%", prefix)))
                .collect(Collectors.toList());
    }
}