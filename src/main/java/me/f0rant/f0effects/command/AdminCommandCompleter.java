package me.f0rant.f0effects.command;

import me.f0rant.f0effects.f0Effects;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminCommandCompleter implements TabCompleter {
    private final f0Effects plugin;

    public AdminCommandCompleter(f0Effects plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (!sender.hasPermission("f0effects.admin")) {
            return completions;
        }

        if (args.length == 1) {
            // Dodano "reload" do listy akcji
            commands.addAll(Arrays.asList("set", "clear", "reload"));
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                commands.add(p.getName());
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length == 3 && !args[0].equalsIgnoreCase("reload")) {
            commands.addAll(plugin.getConfig().getConfigurationSection("effects").getKeys(false));
            StringUtil.copyPartialMatches(args[2], commands, completions);
        } else if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
            commands.addAll(Arrays.asList("0", "1", "2", "3"));
            StringUtil.copyPartialMatches(args[3], commands, completions);
        }

        return completions;
    }
}