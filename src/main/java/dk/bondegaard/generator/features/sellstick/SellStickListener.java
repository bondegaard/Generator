package dk.bondegaard.generator.features.sellstick;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.features.SellInventory;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellStickListener implements Listener {

    private final SellStickHandler handler;

    public SellStickListener(SellStickHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack heldItem = event.getPlayer().getItemInHand();
        if (heldItem == null || heldItem.getType() == Material.AIR) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !(clickedBlock.getType() == Material.CHEST || clickedBlock.getType() == Material.TRAPPED_CHEST)) return;

        Inventory inv = ((Chest) clickedBlock.getState()).getInventory();
        double sellMulti = handler.getMulti(heldItem);
        if (sellMulti <=0) return;

        event.setCancelled(true);
        Economy econ = Main.getInstance().getEconomy();
        if (econ == null) return;

        double amount = SellInventory.sellInventory(inv, sellMulti);
        if (amount > 0) {
            econ.depositPlayer(event.getPlayer(), amount);
            PlayerUtils.sendMessage(event.getPlayer(), Lang.PREFIX+Lang.SELLSTICK_SELL_MESSAGE.replace("%amount%", amount+""));
        } else {
            PlayerUtils.sendMessage(event.getPlayer(), Lang.PREFIX+Lang.SELLSTICK_SELL_NOTHING);
        }
    }
}
