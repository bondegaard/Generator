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

package dk.bondegaard.generator.generators;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.NumUtils;
import dk.bondegaard.generator.utils.PlaceholderString;
import dk.bondegaard.generator.utils.PlayerUtils;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

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
                PlaceholderString genMaxMessage = new PlaceholderString(Lang.PREFIX + Lang.GENS_MAX, "%PLACED%","%MAX%", "%GENS_MAX%")
                        .placeholderValues(String.valueOf(gPlayer.getGenerators().size()), String.valueOf(gPlayer.getMaxGens()), String.valueOf(gPlayer.getMaxGens()));
                PlayerUtils.sendMessage(player, genMaxMessage);
                event.setCancelled(true);
                return;
            }
            Generator generator = new Generator(player.getUniqueId().toString(), event.getBlockPlaced().getLocation(), generatorType, Main.getInstance().getConfig().getLong("time-inbetween"));
            gPlayer.getGenerators().add(generator);
            Main.getInstance().getGeneratorHandler().addActiveGenerator(generator);

            PlaceholderString genPlaceMessage = new PlaceholderString(Lang.PREFIX + Lang.GENS_PLACE, "%TYPE%", "%PLACED%", "%MAX%")
                    .placeholderValues(generatorType.getName(), String.valueOf(gPlayer.getGenerators().size()), String.valueOf(gPlayer.getMaxGens()));
            PlayerUtils.sendMessage(player, genPlaceMessage);


            gPlayer.save();
            event.getBlockPlaced().setMetadata("generator", new FixedMetadataValue(Main.getInstance(), player.getUniqueId().toString()));
            return;
        }
    }

    @EventHandler
    public void onGeneratorBreak(BlockBreakEvent event) {
        if (!event.getBlock().hasMetadata("generator")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onGeneratorInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        if (!event.getClickedBlock().hasMetadata("generator")) return;

        if (!event.getClickedBlock().getMetadata("generator").get(0).asString().equals(event.getPlayer().getUniqueId().toString())) {
            PlayerUtils.sendMessage(event.getPlayer(), Lang.PREFIX + Lang.GENS_NOT_YOURS);
            return;
        }

        Generator generator = handler.getActiveGenerator(event.getClickedBlock().getLocation());
        if (generator == null) return;
        Player player = event.getPlayer();

        // UPGRADE GENERATOR
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getInventory().firstEmpty() != -1) {

            // CAN'T UPGRADE
            if (generator.getGeneratorType().getUpgradePrice() < 0) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.GENS_UPGRADED_NO_UPGRADE);
                return;
            }

            // MAX GENERATOR
            if (generator.getGeneratorType().getNextGeneratorName() == null || generator.getGeneratorType().getNextGeneratorName().isEmpty() || !Main.getInstance().getGeneratorHandler().hasGeneratorType(generator.getGeneratorType().getNextGeneratorName())) {
                PlayerUtils.sendMessage(player, Lang.PREFIX + Lang.GENS_UPGRADED_MAX);
                return;
            }

            // Get Economy plugin and make sure it is valid
            Economy economy = Main.getInstance().getEconomy();
            if (economy == null) {
                PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                        .placeholderValues(Lang.NO_ECONOMY);
                PlayerUtils.sendMessage(event.getPlayer(), errorMessage);
            }

            // Check player Balance
            double playerBalance = economy.getBalance(player);
            if (playerBalance < generator.getGeneratorType().getUpgradePrice()) {
                PlaceholderString invalidMessage = new PlaceholderString(Lang.PREFIX + Lang.GENS_UPGRADED_INVALID_FOUNDS, "%NEEDED%")
                        .placeholderValues(NumUtils.formatNumber((generator.getGeneratorType().getUpgradePrice() - playerBalance)));
                PlayerUtils.sendMessage(player, invalidMessage);
                return;
            }

            // Upgrade Generator
            economy.withdrawPlayer(player, generator.getGeneratorType().getUpgradePrice());

            GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(generator.getGeneratorType().getNextGeneratorName());

            event.getClickedBlock().setType(generatorType.getGeneratorItem().getType());
            event.getClickedBlock().setData((byte) generatorType.getGeneratorItem().getDurability());


            generator.setGeneratorType(generatorType);

            if (!event.getClickedBlock().hasMetadata("generator")) event.getClickedBlock().setMetadata("generator", new FixedMetadataValue(Main.getInstance(), player.getUniqueId().toString()));

            PlaceholderString genUpgradeMessage = new PlaceholderString(Lang.PREFIX + Lang.GENS_UPGRADED_SUCCESS, "%TYPE%")
                    .placeholderValues(generator.getGeneratorType().getName());
            PlayerUtils.sendMessage(player, genUpgradeMessage);
            return;
        }
        // PICKUP GENERATOR
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.getClickedBlock().removeMetadata("generator", Main.getInstance());

            event.getClickedBlock().setType(Material.AIR);
            handler.removeActiveGenerator(generator);
            PlayerDataHandler.getGPlayer(player).removeGenerator(generator);
            player.getInventory().addItem(generator.getGeneratorType().getGeneratorItem());
        }
    }
}
