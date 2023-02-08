package dk.bondegaard.generator.generators;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GeneratorHandler {

    private final List<Generator> activeGenerators = new ArrayList<>();

    private final List<GeneratorType> generatorTypes = new ArrayList<>();

    public GeneratorHandler() {
        loadGeneratorTypes();
        loop();

        new GeneratorListener(this);
    }

    public void loadGeneratorTypes() {
        generatorTypes.clear();

        FileConfiguration config = Main.getInstance().getConfig();
        if (!config.contains("generators")) config.set("generators", new ArrayList<>());
        for (String key : config.getConfigurationSection("generators").getKeys(false)) {
            // Check if already loaded
            if (hasGeneratorType(key)) {
                Main.getInstance().getLogger().log(Level.WARNING, "Could not load GeneratorType generators." + key + " : GeneratorType already loaded");
                continue;
            }

            // Load basic Information
            ConfigurationSection generatorType = config.getConfigurationSection("generators." + key);
            String nextGenerator = generatorType.contains("next-generator") ? generatorType.getString("next-generator") : "";

            // Load generator item
            ItemStack generatorItem = generatorType.contains("generator-item") ? ItemUtil.getConfigItem("generator-item", generatorType) : null;
            if (generatorItem == null) {
                Main.getInstance().getLogger().log(Level.WARNING, "Could not load GeneratorType generators." + key + " : Invalid generator-item");
                continue;
            }
            // Load generator drops
            List<ItemStack> itemDrops = new ArrayList<>();
            if (generatorType.contains("generator-drops")) {
                for (String dropkey : config.getConfigurationSection("generators." + key + ".generator-drops").getKeys(false)) {
                    ConfigurationSection dropType = config.getConfigurationSection("generators." + key + ".generator-drops");
                    ItemStack drop = ItemUtil.getConfigItem("generators." + key + ".generator-drops." + dropkey, config);
                    if (drop == null) {
                        Main.getInstance().getLogger().log(Level.WARNING, "Could not load GeneratorDrop Item generators." + key + ".generator-drops." + dropkey + ": Invalid item");
                        continue;
                    }
                    itemDrops.add(drop);
                }
            }
            double upgradePrice = generatorType.contains("upgrade-price") ? generatorType.getDouble("upgrade-price") : -1;

            // Save Generator Type
            generatorTypes.add(new GeneratorType(key, generatorItem, itemDrops, nextGenerator, upgradePrice));
        }
    }

    public GeneratorType getGeneratorType(String name) {
        for (GeneratorType generatorType : generatorTypes) {
            if (generatorType.getName().equalsIgnoreCase(name))
                return generatorType;
        }
        return null;
    }

    public boolean hasGeneratorType(String name) {
        return getGeneratorType(name) != null;
    }

    public void addActiveGenerator(Generator generator) {
        activeGenerators.add(generator);
    }

    public void removeActiveGenerator(Generator generator) {
        activeGenerators.remove(generator);
    }

    public void removeActiveGenerator(String uuid) {
        List<Generator> objectsToRemove = getActiveGenerator(uuid);
        for (Generator generator : objectsToRemove) {
            activeGenerators.remove(generator);
        }
    }

    private List<Generator> getActiveGenerator(String uuid) {
        List<Generator> objectsToRemove = new ArrayList<>();
        for (Generator generator : activeGenerators) {
            if (!generator.getOwnerUUID().equals(uuid)) continue;
            objectsToRemove.add(generator);
        }
        return objectsToRemove;
    }

    public Generator getActiveGenerator(Location location) {
        for (Generator generator : activeGenerators) {
            if (generator.getLocation().distance(location) < 0.51)
                return generator;
        }
        return null;
    }


    private void loop() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Generator generator : activeGenerators) {
                    if (System.currentTimeMillis() - generator.getLastDrop() < generator.getTicksBetweenDrop())
                        continue;
                    if (!generator.getLocation().getChunk().isLoaded()) continue;
                    for (ItemStack drop : generator.getGeneratorType().getGeneratorDrops()) {
                        generator.getLocation().getWorld().dropItemNaturally(generator.getLocation().clone().add(0, 0.5, 0), drop);
                    }
                    generator.setLastDrop(System.currentTimeMillis());
                }
            }
        }, 10L, 10L);
    }



    public List<Generator> getActiveGenerators() {
        return activeGenerators;
    }

    public List<GeneratorType> getGeneratorTypes() {
        return generatorTypes;
    }
}
