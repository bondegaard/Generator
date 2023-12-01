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

package dk.bondegaard.generator.features;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.GeneratorDropItem;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.NumUtils;
import dk.bondegaard.generator.utils.PlaceholderString;
import dk.bondegaard.generator.utils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellInventory {


    /**
     * Sell a players inventory
     * @param player Player which inventory will be sold
     * @param bonusMultiplier Bonus multiplierr to be added on top of players multiplier
     */
    public static void sellInventory(Player player, double bonusMultiplier) {
        if (player == null) return;
        GPlayer gPlayer = PlayerDataHandler.getOrCreateGPlayer(player);
        double multiplier = gPlayer.getMultiplier(bonusMultiplier);

        // Check if Economy is set.
        Economy econ = Main.getInstance().getEconomy();
        if (econ == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.NO_ECONOMY);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        double amount = 0.0;
        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;
            if (item.getAmount() < 1 || item.getAmount() > 64) continue;
            GeneratorDropItem generatorDropItem = getDropItem(item);
            if (generatorDropItem == null || generatorDropItem.getSellPrice() <= 0) continue;
            amount += item.getAmount() * generatorDropItem.getSellPrice();
            player.getInventory().clear(slot);
        }
        if (amount <= 0) {
            PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.SELL_FAIL);
            return;
        }
        amount *= multiplier;

        player.updateInventory();
        econ.depositPlayer(player, amount);
        PlaceholderString sellMessage = new PlaceholderString(Lang.PREFIX + Lang.SELL_SUCCESS, "%TOTAL%", "%MULTIPLIER%")
                .placeholderValues(NumUtils.formatNumber(amount), NumUtils.formatNumber(multiplier));
        PlayerUtils.sendMessage(player, sellMessage);
    }

    public static double sellInventory(Inventory inventory, double multiplier) {

        double amount = 0.0;
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;
            if (item.getAmount() < 1 || item.getAmount() > 64) continue;
            GeneratorDropItem generatorDropItem = getDropItem(item);
            if (generatorDropItem == null || generatorDropItem.getSellPrice() <= 0) continue;
            amount += item.getAmount() * generatorDropItem.getSellPrice();
            inventory.clear(slot);
        }
        amount *= multiplier;
        return amount;
    }

    private static GeneratorDropItem getDropItem(ItemStack item) {
        for (GeneratorType generatorType : Main.getInstance().getGeneratorHandler().getGeneratorTypes()) {
            for (GeneratorDropItem generatorDropItem : generatorType.getGeneratorDrops()) {
                if (!generatorDropItem.getItem().isSimilar(item)) continue;
                return generatorDropItem;
            }
        }
        return null;
    }
}
