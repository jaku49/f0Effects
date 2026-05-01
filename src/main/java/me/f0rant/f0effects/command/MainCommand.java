package me.f0rant.f0effects.command;
import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private final f0Effects plugin;

    public MainCommand(f0Effects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getLanguageManager().getMsg("messages.player-only"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("f0effects.use")) {
                player.sendMessage(plugin.getLanguageManager().getMsg("messages.no-permission"));
                return true;
            }
            plugin.getEffectGUI().open(player);
            return true;
        }

        if (!sender.hasPermission("f0effects.admin")) {
            sender.sendMessage(plugin.getLanguageManager().getMsg("messages.no-permission"));
            return true;
        }

        String action = args[0].toLowerCase();

        if (action.equals("reload")) {
            plugin.reloadConfig();
            plugin.getLanguageManager().loadLanguage();
            sender.sendMessage(plugin.getLanguageManager().getMsg("messages.reload-success"));
            return true;
        }

        if (action.equals("about")) {
            List<String> aboutLines = plugin.getLanguageManager().getLore("messages.about-command");
            
            String version = plugin.getDescription().getVersion();
            String versionColor = (plugin.getUpdateChecker() != null && plugin.getUpdateChecker().isUpdateAvailable()) ? "&c" : "&a";
            String serverType = plugin.getServer().getName();
            String serverVersion = plugin.getServer().getBukkitVersion();

            List<String> hooksList = new ArrayList<>();
            if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) hooksList.add("Vault");
            if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) hooksList.add("PlaceholderAPI");
            String hooks = hooksList.isEmpty() ? "Brak" : String.join(", ", hooksList);

            boolean ucEnabled = plugin.getConfig().getBoolean("update-checker.enabled");
            String updateChecker = ucEnabled ? "Enabled" : "Disabled";

            boolean dbStatus = plugin.getDatabaseManager() != null;
            String dbStatusStr = dbStatus ? "Connected" : "Error";
            String dbStatusColor = dbStatus ? "&a" : "&c";
            String dbType = plugin.getConfig().getString("storage-type", "SQLITE");

            int effectsNum = plugin.getConfig().getConfigurationSection("effects") != null ? 
                    plugin.getConfig().getConfigurationSection("effects").getKeys(false).size() : 0;
            int visualsNum = plugin.getConfig().getConfigurationSection("visual-effects") != null ? 
                    plugin.getConfig().getConfigurationSection("visual-effects").getKeys(false).size() : 0;

            for (String line : aboutLines) {
                line = line.replace("%version%", version)
                           .replace("%version_color%", versionColor)
                           .replace("%server_type%", serverType)
                           .replace("%server_version%", serverVersion)
                           .replace("%hooks%", hooks)
                           .replace("%update_checker%", updateChecker)
                           .replace("%database_status%", dbStatusStr)
                           .replace("%database_status_color%", dbStatusColor)
                           .replace("%database_type%", dbType)
                           .replace("%effects_number%", String.valueOf(effectsNum))
                           .replace("%effects_visual_number%", String.valueOf(visualsNum));
                sender.sendMessage(ColorUtil.color(line));
            }
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ColorUtil.color("&cUsage: /" + label + " <set|clear|reload|about> <player> <effect> [level]"));
            return true;
        }
        

        return true;
    }
}