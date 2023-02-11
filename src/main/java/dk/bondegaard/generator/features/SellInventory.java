package dk.bondegaard.generator.features;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.GeneratorDropItem;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellInventory {

    public static void sellInventory(Player player) {
        sellInventory(player, 1.0);
    }

    public static void sellInventory(Player player, double multiplier) {
        if (player == null) return;
        Economy econ = Main.getInstance().getEconomy();
        if (econ == null) {
            PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.ERROR.replace("%ERROR%", "Økonomi ikke opsat!"));
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
        PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.SELL_SUCCESS.replace("%TOTAL%", amount + "").replace("%MULTIPLIER%", multiplier + ""));
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
