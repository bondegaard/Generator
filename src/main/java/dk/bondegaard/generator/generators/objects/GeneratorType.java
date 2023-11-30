package dk.bondegaard.generator.generators.objects;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.utils.ItemUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor @Getter
public class GeneratorType {

    private final String name;

    private final ItemStack generatorItem;

    private final List<GeneratorDropItem> generatorDrops;

    private final String nextGeneratorName;

    private final double upgradePrice;

    private final long defaultTicksBetweenDrop;

    public void addGeneratorDrop(double sellPrice, ItemStack dropItem) {
        if (!Main.getInstance().getConfig().contains("generators."+name)) return;
        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("generators."+name);

        if (!section.contains("generator-drops")) {
            section.set("generator-drops.1.sell-price", sellPrice);
            ItemUtil.setConfigItem("generator-drops.1", section, dropItem);
            Main.getInstance().saveConfig();

            generatorDrops.add(new GeneratorDropItem("1", dropItem, sellPrice));
            return;
        }
        int i = 1;
        while (i < 10000) {
            if (section.contains("generator-drops."+i)) {
                i++;
                continue;
            }
            section.set("generator-drops."+i+".sell-price", sellPrice);
            ItemUtil.setConfigItem("generator-drops."+i, section, dropItem);
            Main.getInstance().saveConfig();

            generatorDrops.add(new GeneratorDropItem(String.valueOf(i), dropItem, sellPrice));
            return;
        }
    }

    public void removeGeneratorDrop(GeneratorDropItem generatorDropItem) {
        if (!generatorDrops.contains(generatorDropItem)) return;
        if (!Main.getInstance().getConfig().contains("generators."+name+".generator-drops."+generatorDropItem.getId())) return;
        Main.getInstance().getConfig().set("generators."+name+".generator-drops."+generatorDropItem.getId(), null);
        Main.getInstance().saveConfig();
        generatorDrops.remove(generatorDropItem);
    }
}