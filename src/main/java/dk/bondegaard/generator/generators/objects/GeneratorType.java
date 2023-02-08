package dk.bondegaard.generator.generators.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorType {

    private final String name;

    private final ItemStack generatorItem;

    private final List<ItemStack> generatorDrops;

    private final String nextGeneratorName;

    public GeneratorType(String name, ItemStack generatorItem, List<ItemStack> generatorDrops, String nextGeneratorName) {
        this.name = name;
        this.generatorItem = generatorItem;
        this.generatorDrops = generatorDrops;
        this.nextGeneratorName = nextGeneratorName;
    }

    public String getName() {return name;}

    public ItemStack getGeneratorItem() {return generatorItem;}

    public List<ItemStack> getGeneratorDrops() {return generatorDrops;}

    public String getNextGeneratorName() {return nextGeneratorName;}


}