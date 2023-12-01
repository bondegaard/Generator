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
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Pickup {

    public static void pickupAll(Player player) {
        if (player == null || !player.isOnline()) return;
        GPlayer gPlayer = PlayerDataHandler.getGPlayer(player);
        if (gPlayer == null) return;
        if (gPlayer.getGenerators().size() < 1) return;
        for (Generator generator : gPlayer.getGenerators()) {
            generator.getLocation().getBlock().setType(Material.AIR);
            giveItem(player, generator.getGeneratorType().getGeneratorItem());
        }
        gPlayer.getGenerators().clear();
        Main.getInstance().getGeneratorHandler().removeActiveGenerator(player.getUniqueId().toString());
    }

    public static ItemStack pickupOfflineplayerGen(OfflinePlayer offlinePlayer, Location location) {
        if (offlinePlayer == null) return null;
        // Get GPlayer
        GPlayer gPlayer = PlayerDataHandler.getOrCreateGPlayer(offlinePlayer);

        // Get Generator at location
        Generator generator = gPlayer.getGeneratorAtLoc(location);
        if (generator == null) return null;

        // Remove Generator
        generator.getLocation().getBlock().setType(Material.AIR);
        gPlayer.removeGenerator(generator);
        Main.getInstance().getGeneratorHandler().removeActiveGenerator(generator);

        // Return Generator item
        return generator.getGeneratorType().getGeneratorItem();
    }


    public static void giveItem(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() != -1)
            player.getInventory().addItem(item);
        else
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
    }
}
