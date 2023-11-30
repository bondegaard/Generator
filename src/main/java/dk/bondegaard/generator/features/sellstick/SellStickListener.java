package dk.bondegaard.generator.features.sellstick;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.features.SellInventory;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.NumUtils;
import dk.bondegaard.generator.utils.PlaceholderString;
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
        if (!handler.isSellStick(heldItem)) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !(clickedBlock.getType() == Material.CHEST || clickedBlock.getType() == Material.TRAPPED_CHEST)) return;

        GPlayer gPlayer = PlayerDataHandler.getOrCreateGPlayer(event.getPlayer());

        Inventory inv = ((Chest) clickedBlock.getState()).getInventory();
        double sellStickMultiMulti = handler.getSellStickMulti(heldItem);
        double sellMulti = gPlayer.getMultiplier(sellStickMultiMulti);


        if (sellMulti <=0 || sellMulti-1 > Double.MAX_VALUE) return;

        event.setCancelled(true);
        Economy econ = Main.getInstance().getEconomy();
        if (econ == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.NO_ECONOMY);
            PlayerUtils.sendMessage(event.getPlayer(), errorMessage);
            return;
        }

        double amount = SellInventory.sellInventory(inv, sellMulti);
        if (amount > 0) {
            econ.depositPlayer(event.getPlayer(), amount);
            PlaceholderString sellMessage = new PlaceholderString(Lang.PREFIX+Lang.SELLSTICK_SELL_MESSAGE, "%amount%", "%multiplier%")
                    .placeholderValues(NumUtils.formatNumber(amount), NumUtils.formatNumber(sellMulti));

            PlayerUtils.sendMessage(event.getPlayer(), sellMessage);
        } else {
            PlayerUtils.sendMessage(event.getPlayer(), Lang.PREFIX+Lang.SELLSTICK_SELL_NOTHING);
        }
    }
}
