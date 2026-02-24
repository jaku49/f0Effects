package me.f0rant.f0effects.command;

import me.f0rant.f0effects.f0Effects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectsCommand implements CommandExecutor {
    private final f0Effects plugin;

    public EffectsCommand(f0Effects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("To menu mozna otworzyc tylko w grze!");
            return true;
        }

        Player player = (Player) sender;
        plugin.getEffectGUI().open(player);
        return true;
    }
}