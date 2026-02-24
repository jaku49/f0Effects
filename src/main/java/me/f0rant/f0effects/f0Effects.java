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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class f0Effects extends JavaPlugin {

    private DatabaseManager databaseManager;
    private EffectManager effectManager;
    private EffectGUI effectGUI;
    private UpgradeGUI upgradeGUI;
    private Economy econ = null;

    @Override
    public void onEnable() {
        // Zapisywanie domyślnego config.yml
        saveDefaultConfig();

        // Ładowanie ekonomii Vault
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Wylaczono ze wzgledu na brak zaleznosci Vault (lub brak pluginu na ekonomie)!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Inicjalizacja modułów bazy danych (HikariCP)
        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.connect(); 

        this.effectManager = new EffectManager(this);
        this.effectGUI = new EffectGUI(this);
        this.upgradeGUI = new UpgradeGUI(this);

        // Rejestracja komend
        getCommand("efekty").setExecutor(new EffectsCommand(this));
        getCommand("f0ef").setExecutor(new AdminCommand(this));
        getCommand("f0ef").setTabCompleter(new AdminCommandCompleter(this));

        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);

        getLogger().info("Plugin f0Effects wlaczony pomyslnie!");
    }

    @Override
    public void onDisable() {
        // Poprawne zamknięcie puli HikariCP przed wyłączeniem serwera
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        getLogger().info("Plugin f0Effects wylaczony!");
    }

    // Metoda z dokumentacji Vault API
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    // Gettery
    public Economy getEconomy() { return econ; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public EffectManager getEffectManager() { return effectManager; }
    public EffectGUI getEffectGUI() { return effectGUI; }
    public UpgradeGUI getUpgradeGUI() { return upgradeGUI; }
}