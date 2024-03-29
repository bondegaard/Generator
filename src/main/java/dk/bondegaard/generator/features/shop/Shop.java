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

package dk.bondegaard.generator.features.shop;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.features.Pickup;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.NumUtils;
import dk.bondegaard.generator.utils.PlaceholderString;
import dk.bondegaard.generator.utils.PlayerUtils;
import dk.bondegaard.generator.utils.StringUtil;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Shop {


    public static void open(ShopHandler handler, Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(StringUtil.colorize(Lang.SHOP_GUI_TITLE)))
                .rows(6)
                .disableAllInteractions()
                .create();

        for (String key : handler.getShopConfig().getConfigurationSection("gui-layout").getKeys(false)) {
            // Basic Variables
            Integer slot = 0;
            try { slot = Integer.parseInt(key);} catch (Exception ignored) {}
            if (slot > 53) continue;
            ConfigurationSection section = handler.getShopConfig().getConfigurationSection("gui-layout."+key);

            // Get Shop item Info
            String name = section.contains("name") ? section.getString("name") : "";
            double price = section.contains("price") ? section.getDouble("price") : -1;
            if (price == -1 || name.equals("")) continue;

            // Make sure Generator exist
            GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(name);
            if (generatorType == null) continue;

            // Add Text To Item
            ItemStack item = generatorType.getGeneratorItem().clone();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            for (String s:Lang.SHOP_ITEM_LORE) {
                PlaceholderString loreMessage = new PlaceholderString(StringUtil.colorize(s), "%PRICE%", "%TYPE%")
                        .placeholderValues(NumUtils.formatNumber(price), generatorType.getName());
                lore.add(loreMessage.parse());
            }
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            // Insert Items
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem(event -> {
                if (player.getInventory().firstEmpty() == -1) {
                    PlayerUtils.sendMessage(player, Lang.PREFIX+ Lang.SHOP_FULL_INVENTORY);
                    return;
                }
                Economy econ = Main.getInstance().getEconomy();
                if (econ == null) {
                    PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                            .placeholderValues(Lang.NO_ECONOMY);
                    PlayerUtils.sendMessage(player, errorMessage);
                    return;
                }
                double playerBalance = econ.getBalance(player);
                if (playerBalance-price < 0) {
                    PlaceholderString shopFailMessage = new PlaceholderString(Lang.PREFIX + Lang.SHOP_BUY_FAIL, "%NEEDED%")
                            .placeholderValues(NumUtils.formatNumber((price- playerBalance)));
                    PlayerUtils.sendMessage(player, shopFailMessage);
                    return;
                }
                econ.withdrawPlayer(player, price);
                PlaceholderString shopSuccessMessage = new PlaceholderString(Lang.PREFIX + Lang.SHOP_BUY_SUCCESS, "%TYPE%", "%PRICE%")
                        .placeholderValues(generatorType.getName(), NumUtils.formatNumber(price));
                PlayerUtils.sendMessage(player, shopSuccessMessage);
                Pickup.giveItem(player, generatorType.getGeneratorItem());

            }));
        }

        gui.open(player);
    }
}
