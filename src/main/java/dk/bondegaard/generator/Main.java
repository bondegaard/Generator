package dk.bondegaard.generator;

import dk.bondegaard.generator.generators.GeneratorHandler;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

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
        saveDefaultConfig();
        instance = this;

        if (!setupEconomy()) {
            getLogger().log(Level.WARNING, "Couldn't load Vault dependency. shutting down...!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        new Lang(this);

        new PlayerDataHandler(this);
        generatorHandler = new GeneratorHandler();

    }

    @Override
    public void onDisable() {
        PlayerDataHandler.saveAll(true);
    }

    public GeneratorHandler getGeneratorHandler() {
        return generatorHandler;
    }

    public Economy getEconomy() {return economy;}

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        this.economy = rsp.getProvider();
        return this.economy != null;
    }
}
