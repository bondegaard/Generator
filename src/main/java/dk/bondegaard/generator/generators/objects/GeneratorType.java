package dk.bondegaard.generator.generators.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor @Getter
public class GeneratorType {

    private final String name;

    private final ItemStack generatorItem;

    private final List<GeneratorDropItem> generatorDrops;

    private final String nextGeneratorName;

    private final double upgradePrice;
}