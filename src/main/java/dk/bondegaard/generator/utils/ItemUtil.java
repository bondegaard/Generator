/*
 * Copyright (c) 2023 bondegaard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
