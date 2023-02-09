package dk.bondegaard.generator.playerdata;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.utils.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GPlayer {

    // Variables
    private final OfflinePlayer player;
    private final List<Generator> generators = new ArrayList<>();
    private FileConfiguration data;
    private long lastSave = System.currentTimeMillis() - 10000;
    private int maxGens = 20;

    private long prestige = 0;

    private long level = 1;

    private long exp = 0;


    // Constructor
    public GPlayer(OfflinePlayer player) {
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

        // Loads the file as a bukkit config
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
        data.set("prestige", prestige);
        data.set("level", level);
        data.set("exp", exp);

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
    public OfflinePlayer getPlayer() {
        return player;
    }

    public FileConfiguration getData() {
        return data;
    }

    private void setDefaultValues(OfflinePlayer player, FileConfiguration data) {
        data.set("username", player.getName());
        if (!data.contains("generators")) data.set("generators", new ArrayList<>());
        if (data.contains("max-gens")) data.set("max-gens", Main.getInstance().getConfig().getInt("max-gens"));
        if (data.contains("prestige")) data.set("prestige", 0L);
        if (data.contains("level")) data.set("level", 1L);
        if (data.contains("exp")) data.set("exp", 0L);
    }

    private void loadPlayerStats() {
        generators.clear();
        maxGens = data.getInt("max-gens");
        prestige = data.getLong("prestige");
        level = data.getLong("level");
        exp = data.getLong("exp");
        // Load gens from player
        try {
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
            Main.getInstance().getGeneratorHandler().removeActiveGenerator(player.getUniqueId().toString());
        } catch (NullPointerException ignored) {
        }//if player has no gens
        // Add gens as active gens
        if (player.isOnline()) {
            for (Generator gen : generators) {
                Main.getInstance().getGeneratorHandler().getActiveGenerators().add(gen);
            }
        }
    }

    public void removeGenerator(Generator generator) {
        generators.remove(generator);
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

    public void setMaxGens(int maxGens) {
        this.maxGens = maxGens;
    }

    public Generator getGeneratorAtLoc(Location loc) {
        for (Generator generator : generators) {
            if (generator.getLocation().distance(loc) < 0.51)
                return generator;
        }
        return null;
    }

    public long getPrestige() {return prestige;}

    public long getLevel() {return level;}

    public long getExp() {return exp;}

    public void setPrestige(long prestige) {this.prestige = prestige;}
    public void addPrestige() {this.prestige++;}
    public void addPrestige(long prestige) {this.prestige+=prestige;}
    public void removePrestige() {this.prestige--;}
    public void removePrestige(long prestige) {this.prestige-=prestige;}
    public void setLevel(long level) {this.level = level;}
    public void addLevel() {this.level++;}
    public void addLevel(long level) {this.level+=level;}
    public void removeLevel() {this.level--;}
    public void removeLevel(long level) {this.level-=level;}
    public void setExp(long exp) {this.exp = exp;}

    public void addExp() {this.exp++;}
    public void addExp(long exp) {this.exp+=exp;}
    public void removeExp() {this.exp--;}
    public void removeExp(long exp) {this.exp-=exp;}
}
