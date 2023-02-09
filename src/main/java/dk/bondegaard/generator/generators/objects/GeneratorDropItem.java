package dk.bondegaard.generator.generators.objects;

import org.bukkit.inventory.ItemStack;

public class GeneratorDropItem {

    private final ItemStack item;

    private final double sellPrice;

    public GeneratorDropItem(ItemStack item, double sellPrice) {
        this.item = item;
        this.sellPrice = sellPrice;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
