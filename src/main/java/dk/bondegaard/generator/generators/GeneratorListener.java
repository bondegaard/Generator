package dk.bondegaard.generator.generators;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.Generator;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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

            //todo set metadata of block
            return;
        }
    }
}
