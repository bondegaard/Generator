package dk.bondegaard.generator.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemUtil {
    public static ItemStack getConfigItem(String path, ConfigurationSection config) {

        ConfigurationSection section = config.getConfigurationSection(path);

        String[] itemID = section.getString("type").split(":");
        try {

            ItemBuilder itemBuilder = new ItemBuilder(
                    /* Material */ Material.getMaterial(Integer.parseInt(itemID[0])),
                    /* Amount */ section.contains("amount") ? section.getInt("amount") : 1,
                    /* Data */ Short.parseShort(itemID[1])
            );

            itemBuilder.name(StringUtil.colorize((section.contains("name") ? section.getString("name") : "")));
            itemBuilder.addLore(StringUtil.colorize((section.contains("lore") ?section.getStringList("lore") : new ArrayList<>())));
            itemBuilder.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            if (section.contains("glowing") && section.getBoolean("glowing")) itemBuilder.makeGlowing();

            return itemBuilder.build();

        } catch (Exception err) {
        }
        return null;
    }

    public static void setConfigItem(String path, ConfigurationSection config, ItemStack itemStack) {

        String itemID = String.valueOf(itemStack.getType().getId());
        String duability = String.valueOf(itemStack.getDurability());

        config.set(path+".type", itemID + ":" + duability);
        config.set(path+".amount", itemStack.getAmount());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName()) config.set(path+".name", itemMeta.getDisplayName());
        if (itemMeta.hasLore()) config.set(path+".lore", itemMeta.getLore());
    }
}
