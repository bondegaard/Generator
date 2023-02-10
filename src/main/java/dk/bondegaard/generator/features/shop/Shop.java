package dk.bondegaard.generator.features.shop;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.features.Pickup;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.GUI;
import dk.bondegaard.generator.utils.Pair;
import dk.bondegaard.generator.utils.PlayerUtils;
import dk.bondegaard.generator.utils.StringUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends GUI {

    private Map<Integer, Pair<GeneratorType, Double>> slotToItem = new HashMap<>();
    public Shop(Player player, ShopHandler handler) {
        super("", 6, player);
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
                lore.add(StringUtil.colorize(s).replace("%PRICE%", price+"").replace("%TYPE%", generatorType.getName()));
            }
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            // Save Items
            this.layout.put(slot, item);
            slotToItem.put(slot, new Pair<>(generatorType, price));
        }
    }

    @Override
    public void click(int slot, ItemStack clickedItem, boolean shift) {
        if (!slotToItem.containsKey(slot)) return;
        Pair<GeneratorType, Double> shopItem = slotToItem.get(slot);

        if (player.getInventory().firstEmpty() == -1) {
            PlayerUtils.sendMessage(player, Lang.PREFIX+ Lang.SHOP_FULL_INVENTORY);
            return;
        }

        Economy econ = Main.getInstance().getEconomy();
        if (econ == null) {
            PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.ERROR.replace("%ERROR%", "Økonomi ikke opsat!"));
            return;
        }
        double playerBalance = econ.getBalance(player);
        if (shopItem.getRight()-playerBalance < 0) {
            PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.SHOP_BUY_FAIL.replace("%NEEDED%", (shopItem.getRight()- playerBalance) + ""));
            return;
        }
        econ.withdrawPlayer(player, shopItem.getRight());
        PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.SHOP_BUY_SUCCESS.replace("%%TYPE%%", shopItem.getLeft().getName()).replace("%PRICE%", shopItem.getRight()+""));
        Pickup.giveItem(player, shopItem.getLeft().getGeneratorItem());
    }
}
