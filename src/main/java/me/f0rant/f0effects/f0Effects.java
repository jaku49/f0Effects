package me.f0rant.f0effects;

import me.f0rant.f0effects.command.AdminCommand;
import me.f0rant.f0effects.command.AdminCommandCompleter;
import me.f0rant.f0effects.command.EffectsCommand;
import me.f0rant.f0effects.database.DatabaseManager;
import me.f0rant.f0effects.gui.EffectGUI;
import me.f0rant.f0effects.gui.UpgradeGUI;
import me.f0rant.f0effects.listener.InventoryClickListener;
import me.f0rant.f0effects.listener.PlayerDeathListener;
import me.f0rant.f0effects.listener.PlayerJoinQuitListener;
import me.f0rant.f0effects.manager.EffectManager;
import me.f0rant.f0effects.manager.BossBarManager;
import me.f0rant.f0effects.update.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class f0Effects extends JavaPlugin {

    private DatabaseManager databaseManager;
    private EffectManager effectManager;
    private BossBarManager bossBarManager;
    private EffectGUI effectGUI;
    private UpgradeGUI upgradeGUI;
    private UpdateChecker updateChecker;
    private Economy econ = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new me.f0rant.f0effects.utils.EffectsExpansion(this).register();
            getLogger().info("Found PlaceholderAPI, registered placeholders.");
        }
        if (!setupEconomy()) {
            getLogger().severe("Vault and an economy plugin are required for f0Effects to work! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.connect();

        this.effectManager = new EffectManager(this);
        this.bossBarManager = new BossBarManager(this);
        this.effectGUI = new EffectGUI(this);
        this.upgradeGUI = new UpgradeGUI(this);

        this.updateChecker = new UpdateChecker(
                this,
                "https://raw.githubusercontent.com/jaku49/f0Effects/refs/heads/main/src/main/version.txt"
        );
        updateChecker.check();

        getCommand("efekty").setExecutor(new EffectsCommand(this));
        getCommand("f0ef").setExecutor(new AdminCommand(this));
        getCommand("f0ef").setTabCompleter(new AdminCommandCompleter(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() { return econ; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public EffectManager getEffectManager() { return effectManager; }
    public BossBarManager getBossBarManager() { return bossBarManager; }
    public EffectGUI getEffectGUI() { return effectGUI; }
    public UpgradeGUI getUpgradeGUI() { return upgradeGUI; }
    public UpdateChecker getUpdateChecker() { return updateChecker; }
}