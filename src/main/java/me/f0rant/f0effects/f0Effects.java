package me.f0rant.f0effects;

import me.f0rant.f0effects.command.MainCommand;
import me.f0rant.f0effects.command.MainCommandCompleter;
import me.f0rant.f0effects.database.DatabaseManager;
import me.f0rant.f0effects.gui.EffectGUI;
import me.f0rant.f0effects.gui.UpgradeGUI;
import me.f0rant.f0effects.gui.VisualEffectGUI;
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
    private VisualEffectGUI visualEffectGUI; 
    
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
        this.visualEffectGUI = new VisualEffectGUI(this); 

        this.updateChecker = new UpdateChecker(
                this,
                "https://raw.githubusercontent.com/jaku49/f0Effects/refs/heads/main/src/main/version.txt"
        );
        updateChecker.check();

        getCommand("f0effects").setExecutor(new MainCommand(this));
        getCommand("f0effects").setTabCompleter(new MainCommandCompleter(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        if (effectManager != null && databaseManager != null) {
            for (org.bukkit.entity.Player player : getServer().getOnlinePlayers()) {
                me.f0rant.f0effects.model.PlayerData data = effectManager.getPlayerData(player.getUniqueId());
                databaseManager.savePlayerSync(data);
            }
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
    public VisualEffectGUI getVisualEffectGUI() { return visualEffectGUI; }
    
    public UpdateChecker getUpdateChecker() { return updateChecker; }
}