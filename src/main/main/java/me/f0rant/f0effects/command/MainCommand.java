package me.f0rant.f0effects.command;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    private final f0Effects plugin;

    public MainCommand(f0Effects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This menu can only be used by players!");
                return true;
            }
            Player player = (Player) sender;

            if (!player.hasPermission("f0effects.use")) {
                player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }


            plugin.getEffectGUI().open(player);
            return true;
        }

        if (!sender.hasPermission("f0effects.admin")) {
            sender.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        String action = args[0].toLowerCase();

        if (action.equals("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ColorUtil.color("&#00FFCC✔ &aSucccesfully reloaded config!"));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ColorUtil.color("&#ffaa00Usage: /" + label + " <set|clear|reload> <player> <effect> [level]"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ColorUtil.color("&#ff0000Player not found: " + args[1] + "!"));
            return true;
        }

        String effect = args[2].toUpperCase();
        if (!plugin.getConfig().contains("effects." + effect)) {
            sender.sendMessage(ColorUtil.color("&#ff0000Unknown effect! Available: SPEED, RESISTANCE, REGENERATION, STRENGTH"));
            return true;
        }

        PlayerData data = plugin.getEffectManager().getPlayerData(target.getUniqueId());

        if (action.equals("set")) {
            if (args.length < 4) {
                sender.sendMessage(ColorUtil.color("&#ff0000Provide level to set (0-3)!"));
                return true;
            }
            try {
                int level = Integer.parseInt(args[3]);
                if (level < 0 || level > 3) {
                    sender.sendMessage(ColorUtil.color("&#ff0000Level must be in range 0-3!"));
                    return true;
                }
                data.setLevel(effect, level);
                sender.sendMessage(ColorUtil.color("&#00ff00Successfully set level &e" + level + " &afor effect &e" + effect + " &afor player &e" + target.getName()));
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorUtil.color("&#ff0000Provided level is not a number!"));
                return true;
            }
        } else if (action.equals("clear")) {
            data.setLevel(effect, 0);
            sender.sendMessage(ColorUtil.color("&#00ff00Successfully cleared effect &e" + effect + " &afor player &e" + target.getName()));
        } else {
            sender.sendMessage(ColorUtil.color("&#ffaa00Usage: /" + label + " <set|clear|reload> <player> <effect> [level]"));
            return true;
        }

        // Zapis do bazy danych w tle
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });

        return true;
    }
}