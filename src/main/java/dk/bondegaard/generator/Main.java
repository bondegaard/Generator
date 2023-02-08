package dk.bondegaard.generator;

import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    private PlayerDataHandler playerDataHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        playerDataHandler = new PlayerDataHandler(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {return instance;}

    public PlayerDataHandler getPlayerDataHandler() {return playerDataHandler;}
}
