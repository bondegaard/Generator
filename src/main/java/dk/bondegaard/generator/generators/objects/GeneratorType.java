package dk.bondegaard.generator.generators.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorType {

    private final String name;

    private final ItemStack generatorItem;

    private final List<GeneratorDropItem> generatorDrops;

    private final String nextGeneratorName;

    private final double upgradePrice;

    public GeneratorType(String name, ItemStack generatorItem, List<GeneratorDropItem> generatorDrops, String nextGeneratorName, double upgradePrice) {
        this.name = name;
        this.generatorItem = generatorItem;
        this.generatorDrops = generatorDrops;
        this.nextGeneratorName = nextGeneratorName;
        this.upgradePrice = upgradePrice;
    }

    public String getName() {
        return name;
    }

    public ItemStack getGeneratorItem() {
        return generatorItem;
    }

    public List<GeneratorDropItem> getGeneratorDrops() {
        return generatorDrops;
    }

    public String getNextGeneratorName() {
        return nextGeneratorName;
    }

    public double getUpgradePrice() {
        return upgradePrice;
    }
}