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
