package dk.bondegaard.generator.features;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.GeneratorDropItem;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SellInventory {


    public static void sellInventory(Player player) {
        if (player == null) return;
        Economy econ = Main.getInstance().getEconomy();
        if (econ == null) return;

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
        player.updateInventory();
        econ.depositPlayer(player, amount);
        PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.SELL_SUCCESS.replace("%TOTAL%", amount + ""));
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
