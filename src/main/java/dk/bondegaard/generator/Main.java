package dk.bondegaard.generator;

import dk.bondegaard.generator.generators.GeneratorHandler;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    private PlayerDataHandler playerDataHandler;

    private GeneratorHandler generatorHandler;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        new Lang(this);

        playerDataHandler = new PlayerDataHandler(this);
        generatorHandler = new GeneratorHandler();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerDataHandler;
    }

    public GeneratorHandler getGeneratorHandler() {
        return generatorHandler;
    }
}
