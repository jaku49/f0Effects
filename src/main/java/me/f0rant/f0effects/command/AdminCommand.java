package me.f0rant.f0effects.command;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import me.f0rant.f0effects.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    private final f0Effects plugin;

    public AdminCommand(f0Effects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("f0effects.admin")) {
            sender.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        // Obsługa przeładowania configu
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ColorUtil.color("&#00FFCC✔ &aPomyslnie przeladowano plik config.yml!"));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ColorUtil.color("&#ffaa00Uzycie: /f0ef <set|clear|reload> <gracz> <efekt> [poziom]"));
            return true;
        }

        String action = args[0].toLowerCase();
        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            sender.sendMessage(ColorUtil.color("&#ff0000Nie znaleziono gracza o nicku " + args[1] + "!"));
            return true;
        }

        String effect = args[2].toUpperCase();
        if (!plugin.getConfig().contains("effects." + effect)) {
            sender.sendMessage(ColorUtil.color("&#ff0000Nieznany efekt! Dostepne: SPEED, RESISTANCE, REGENERATION"));
            return true;
        }

        PlayerData data = plugin.getEffectManager().getPlayerData(target.getUniqueId());

        if (action.equals("set")) {
            if (args.length < 4) {
                sender.sendMessage(ColorUtil.color("&#ff0000Podaj poziom do ustawienia (0-3)!"));
                return true;
            }
            try {
                int level = Integer.parseInt(args[3]);
                if (level < 0 || level > 3) {
                    sender.sendMessage(ColorUtil.color("&#ff0000Poziom musi byc w przedziale 0-3!"));
                    return true;
                }
                data.setLevel(effect, level);
                sender.sendMessage(ColorUtil.color("&#00ff00Pomyslnie ustawiono poziom &e" + level + " &aefektu &e" + effect + " &adla gracza &e" + target.getName()));
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorUtil.color("&#ff0000Podany poziom nie jest liczba!"));
                return true;
            }
        } else if (action.equals("clear")) {
            data.setLevel(effect, 0);
            sender.sendMessage(ColorUtil.color("&#00ff00Pomyslnie wyzerowano efekt &e" + effect + " &adla gracza &e" + target.getName()));
        } else {
            sender.sendMessage(ColorUtil.color("&#ffaa00Uzycie: /f0ef <set|clear|reload> <gracz> <efekt> [poziom]"));
            return true;
        }

        // Zapis do bazy danych w tle
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().savePlayerSync(data);
        });

        return true;
    }
}