package dk.bondegaard.generator.playerdata;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GPlayer {

    // Variables
    private final Player player;
    private final List<Generator> generators = new ArrayList<>();
    private FileConfiguration data;
    private long lastSave = System.currentTimeMillis() - 10000;
    private int maxGens = 20;


    // Constructor
    public GPlayer(Player player) {
        this.player = player;
    }

    // Methods
    public GPlayer load() {
        //Creates the folder Generator/playerdata/ if it doesn't exist
        File folder = new File(Main.getInstance().getDataFolder(), "playerdata");
        folder.mkdirs();

        //Creates the unique data file if it doesn't exist
        File dataFile = new File(folder, player.getUniqueId() + ".yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ex) {
        }

        //Loads the file as a bukkit config
        this.data = YamlConfiguration.loadConfiguration(dataFile);
        setDefaultValues(player, data);
        loadPlayerStats();
        return this;
    }

    public void save() {
        save(false);
    }

    public void save(boolean force) {
        if (!force && System.currentTimeMillis() - lastSave < 10000) return; // Prevent oversaving (lag prevention)
        lastSave = System.currentTimeMillis();
        File folder = new File(Main.getInstance().getDataFolder(), "playerdata");
        File dataFile = new File(folder, player.getUniqueId() + ".yml");
        // Save values
        data.set("max-gens", maxGens);

        // save generators and clear old
        int i = 1;
        data.set("generators", null);
        for (Generator generator : generators) {
            data.set("generators." + i + ".location", Utils.locationToString(generator.getLocation()));
            data.set("generators." + i + ".gen-name", generator.getGeneratorType().getName());
            data.set("generators." + i + ".time-between-drop", generator.getTicksBetweenDrop());
            data.set("generators." + i + ".last-drop", generator.getLastDrop());
            i++;
        }


        try {
            data.save(dataFile);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    // Getters
    public Player getPlayer() {
        return player;
    }

    public FileConfiguration getData() {
        return data;
    }

    private void setDefaultValues(Player player, FileConfiguration data) {
        data.set("username", player.getName());
        if (!data.contains("generators")) data.set("generators", new ArrayList<>());
        if (data.contains("max-gens")) data.set("max-gens", Main.getInstance().getConfig().getInt("max-gens"));
    }

    private void loadPlayerStats() {
        generators.clear();
        maxGens = data.getInt("max-gens");
        for (String key : data.getConfigurationSection("generators").getKeys(false)) {
            try {
                ConfigurationSection gen = data.getConfigurationSection("generators." + key);

                // Load Generator stats
                Location loc = Utils.stringToLocation(gen.getString("location"));
                GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(gen.getString("gen-name"));
                long timeBetween = gen.contains("time-between-drop") ? gen.getLong("time-between-drop") : Main.getInstance().getConfig().getLong("time-inbetween");
                long lastDrop = gen.contains("last-drop") ? gen.getLong("last-drop") : -1;

                generators.add(new Generator(player.getUniqueId().toString(), loc, generatorType, timeBetween).setLastDrop(lastDrop != -1 ? lastDrop : System.currentTimeMillis()));
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                Main.getInstance().getLogger().log(Level.WARNING, "Could not load " + player.getName() + " generator as it is invalid : Deleting it");
                data.set("generators." + key, null);
            }
        }
    }

    public long getLastSave() {
        return lastSave;
    }

    public List<Generator> getGenerators() {
        return generators;
    }

    public int getMaxGens() {
        return maxGens;
    }
}
