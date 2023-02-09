package dk.bondegaard.generator.api;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.api.placeholderapi.PlaceholderAPI;
import dk.bondegaard.generator.api.skript.SkriptAPI;
import dk.bondegaard.generator.features.SellInventory;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorAPI {

    private final GeneratorAPI api;

    private final Main plugin;


    public GeneratorAPI(Main plugin) {
        this.api = this;
        this.plugin = plugin;

        if (Bukkit.getPluginManager().getPlugin("Skript") != null) new SkriptAPI(this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceholderAPI(this);
    }

    /**
     * @return Generator API instance to use the API
     */
    public GeneratorAPI getApi() {
        return api;
    }

    /**
     * @return true if plugin is enabled, otherwise false
     */
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    /**
     * Get a GPlayer if the player is online
     *
     * @param player The player to get
     * @return the GPlayer or null
     */
    public GPlayer getGPlayer(Player player) {
        return PlayerDataHandler.getGPlayer(player);
    }

    /**
     * Get a list of all active generators
     *
     * @return list of all active generators
     */
    public List<Generator> getActiveGenerators() {
        return plugin.getGeneratorHandler().getActiveGenerators();
    }

    /**
     * Returns a specific type of generator     *  types are defined in the servers config
     *
     * @param name Name of the Generator
     * @return The Generatortype or null
     */
    public GeneratorType getGeneratorType(String name) {
        return plugin.getGeneratorHandler().getGeneratorType(name);
    }

    /**
     * Gets a list of all GeneratorTypes
     *
     * @return a List of all GeneratorType
     */
    public List<GeneratorType> getGeneratorTypes() {
        return plugin.getGeneratorHandler().getGeneratorTypes();
    }

    /**
     * Get Generator block
     *
     * @param generatorTypeName Name of Generator type
     * @return The itemstack of the generator or null
     */
    public ItemStack getGeneratorBlock(String generatorTypeName) {
        GeneratorType generatorType = getGeneratorType(generatorTypeName);
        return generatorType != null ? generatorType.getGeneratorItem() : null;
    }

    /**
     * Sell a players inventory
     * (Only selling generator drops after price defined in config)
     *
     * @param player The player that will be selling their inventory
     */
    public void sellInventory(Player player) {
        SellInventory.sellInventory(player);
    }
}
