package dk.bondegaard.generator;

import dk.bondegaard.generator.api.GeneratorAPI;
import dk.bondegaard.generator.commands.AdminCommand;
import dk.bondegaard.generator.features.sellstick.SellStickHandler;
import dk.bondegaard.generator.features.shop.ShopHandler;
import dk.bondegaard.generator.generators.GeneratorHandler;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static Main instance;

    private GeneratorHandler generatorHandler;

    private Economy economy;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        handleConfigVersion();
        instance = this;

        if (!setupEconomy()) {
            getLogger().log(Level.WARNING, "Couldn't load Vault dependency. shutting down...!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Lang.reload();

        new PlayerDataHandler(this);
        generatorHandler = new GeneratorHandler();

        // Commands
        getCommand("generatoradmin").setExecutor(new AdminCommand());

        // API
        new GeneratorAPI(this);
        loadFeatures();
    }

    private void loadFeatures() {
        new ShopHandler();
        new SellStickHandler();
    }

    @Override
    public void onDisable() {
        PlayerDataHandler.saveAll(true);
    }

    public GeneratorHandler getGeneratorHandler() {
        return generatorHandler;
    }

    public Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        this.economy = rsp.getProvider();
        return this.economy != null;
    }

    private void handleConfigVersion() {
        // Check config versions
        saveDefaultConfig();
        if (!getConfig().contains("version")) {
            getConfig().set("version", getDescription().getVersion());
            saveConfig();
            return;
        }
        String configVersion = getConfig().getString("version");
        if (configVersion.equalsIgnoreCase(getDescription().getVersion())) return;

        // # # # # # # # # # # # # # # # # # # # #
        // # CONFIG IS OLD AND NEEDS TO BE FIXED #
        // # # # # # # # # # # # # # # # # # # # #
        getLogger().log(Level.SEVERE, "---------) GENERATOR WARNING (---------");
        getLogger().log(Level.SEVERE, " Old config version detected...");
        getLogger().log(Level.SEVERE, " Switching too default newer version!");
        getLogger().log(Level.SEVERE, "...");
        try {
            // Rename old config file and put in new
            File dataFile = new File(getDataFolder(), "config.yml");
            //    dataFile.renameTo(new File(getDataFolder(), "config-old.yml"));
            Files.move(dataFile.toPath(), new File(getDataFolder(), "config-old.yml").toPath());
            getLogger().log(Level.SEVERE, "Config successfully updated!");
            saveResource("config.yml", true);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Config couldn't be updated... using old config!");
            saveDefaultConfig();
        }
        getLogger().log(Level.SEVERE, "---------) GENERATOR WARNING (---------");
        // # # # # # # # # # # # # # # # # # # # #
        // # CONFIG IS OLD AND NEEDS TO BE FIXED #
        // # # # # # # # # # # # # # # # # # # # #
    }
}
