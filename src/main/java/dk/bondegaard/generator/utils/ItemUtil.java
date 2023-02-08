package dk.bondegaard.generator.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    public static ItemStack getConfigItem(String path, ConfigurationSection config) {

        ConfigurationSection section = config.getConfigurationSection(path);

        //If the item is a custom skull
        String[] itemID = section.getString("type").split(":");
        try {

            ItemBuilder itemBuilder = new ItemBuilder(
                    /* Material */ Material.getMaterial(Integer.parseInt(itemID[0])),
                    /* Amount */ 1,
                    /* Data */ Short.parseShort(itemID[1])
            );

            itemBuilder.name(section.getString("name"));
            itemBuilder.addLore(section.getStringList("lore"));
            itemBuilder.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            if (section.getBoolean("glowing")) itemBuilder.makeGlowing();

            return itemBuilder.build();

        } catch (Exception err) {
        }
        return null;
    }
}
