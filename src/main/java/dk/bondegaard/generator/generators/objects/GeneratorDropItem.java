package dk.bondegaard.generator.generators.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor @Getter
public class GeneratorDropItem {

    private final ItemStack item;

    private final double sellPrice;
}
