package dk.bondegaard.generator.generators;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GeneratorListener implements Listener {

    private final GeneratorHandler handler;

    public GeneratorListener(GeneratorHandler handler) {
        this.handler = handler;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onGeneratorPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();

        ItemStack item = event.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;

        for (GeneratorType generatorType : handler.getGeneratorTypes()) {
            if (!generatorType.getGeneratorItem().isSimilar(item)) continue;
            GPlayer gPlayer = PlayerDataHandler.getGPlayer(player);
            if (gPlayer.getMaxGens() <= gPlayer.getGenerators().size()) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + " " + Lang.GENS_MAX.replace("GENS_MAX", gPlayer.getMaxGens() + "").replace("%PLACED%", gPlayer.getGenerators().size() + ""));
                return;
            }
            Generator generator = new Generator(player.getUniqueId().toString(), event.getBlockPlaced().getLocation(), generatorType, Main.getInstance().getConfig().getLong("time-inbetween"));
            gPlayer.getGenerators().add(generator);
            Main.getInstance().getGeneratorHandler().addActiveGenerator(generator);
            PlayerUtils.sendMessage(player, Lang.PREFIX + " " + Lang.GENS_PLACE
                    .replace("%TYPE%", generatorType.getName())
                    .replace("%PLACED%", gPlayer.getGenerators().size() + "")
                    .replace("%MAX%", gPlayer.getMaxGens() + ""));

            gPlayer.save();

            //todo set metadata of block
            return;
        }
    }

    @EventHandler
    public void onGeneratorBreak(BlockBreakEvent event) {
        //todo check metadata if player can break block
    }

    @EventHandler
    public void onGeneratorInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        // todo check metadata of block

        Generator generator = handler.getActiveGenerator(event.getClickedBlock().getLocation());
        if (generator == null) return;
        Player player = event.getPlayer();

        // UPGRADE GENERATOR
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getInventory().firstEmpty() != -1) {

            // CAN'T UPGRADE
            if (generator.getGeneratorType().getUpgradePrice() < 0) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + " " + " " +Lang.GENS_UPGRADED_NO_UPGRADE);
                return;
            }

            // MAX GENERATOR
            if (generator.getGeneratorType().getNextGeneratorName().equals("")) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + " " + " " +Lang.GENS_UPGRADED_MAX);
                return;
            }

            // Get Economy plugin and make sure it is valid
            Economy economy = Main.getInstance().getEconomy();
            if (economy == null) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + " " + " " +Lang.ERROR.replace("%ERROR%", "Kunne ikke bruge økonomi plugin."));
                return;
            }

            // Check player Balance
            double playerBalance = economy.getBalance(player);
            if (playerBalance < generator.getGeneratorType().getUpgradePrice()) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + " " + " " +Lang.GENS_UPGRADED_INVALID_FOUNDS.replace("%NEEDED%", ""+(generator.getGeneratorType().getUpgradePrice()-playerBalance)));
                return;
            }
            
            // Upgrade Generator
            economy.withdrawPlayer(player, generator.getGeneratorType().getUpgradePrice());
            PlayerUtils.sendMessage(player, Lang.PREFIX + " " + Lang.GENS_UPGRADED_SUCCESS.replace("%TYPE%", generator.getGeneratorType().getNextGeneratorName()));
            //todo Change block and set settings after player upgraded the generator
            return;
        }
        // PICKUP GENERATOR
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            //todo remove metadata of block

            event.getClickedBlock().setType(Material.AIR);
            handler.removeActiveGenerator(generator);
            PlayerDataHandler.getGPlayer(player).removeGenerator(generator);
            player.getInventory().addItem(generator.getGeneratorType().getGeneratorItem());
            return;
        }
    }
}
