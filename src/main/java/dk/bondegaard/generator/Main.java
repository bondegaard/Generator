package dk.bondegaard.generator;

import dk.bondegaard.generator.api.GeneratorAPI;
import dk.bondegaard.generator.commands.GeneratorAdminCommand;
import dk.bondegaard.generator.commands.CommandWrapper;
import dk.bondegaard.generator.commands.SellCommand;
import dk.bondegaard.generator.commands.ShopCommand;
import dk.bondegaard.generator.features.sellstick.SellStickHandler;
import dk.bondegaard.generator.features.shop.ShopHandler;
import dk.bondegaard.generator.generators.GeneratorHandler;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;


    private GeneratorHandler generatorHandler;

    private Economy economy;

    private CommandWrapper commandWrapper;

    @Override
    public void onEnable() {
        handleConfigVersion();
        instance = this;

        // Setup Economy
        if (!setupEconomy()) {
            getLogger().log(Level.WARNING, "Couldn't load Vault dependency. shutting down...!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Load Lang
        Lang.reload();

        // Load CommandWrapper
        this.commandWrapper = new CommandWrapper(this);
        this.commandWrapper.loadMessages();

        // Load handlers
        new PlayerDataHandler(this);
        this.generatorHandler = new GeneratorHandler();

        // Register Commands
        this.commandWrapper.register(new GeneratorAdminCommand());

        // API
        new GeneratorAPI(this);
        loadFeatures();

        loadCommands();
    }

    private void loadFeatures() {
        new ShopHandler();
        new SellStickHandler();
    }

    @Override
    public void onDisable() {
        PlayerDataHandler.saveAll(true);
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
        getLogger().log(Level.SEVERE, " Switching to default newer version!");
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

    private void loadCommands() {
        if (getConfig().contains("sell-command-enabled") && getConfig().getBoolean("sell-command-enabled"))
            this.commandWrapper.register(new SellCommand());
        if (getConfig().contains("shop-command-enabled") && getConfig().getBoolean("shop-command-enabled"))
            this.commandWrapper.register(new ShopCommand());
    }
}
