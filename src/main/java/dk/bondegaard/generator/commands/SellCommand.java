package dk.bondegaard.generator.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import dk.bondegaard.generator.features.SellInventory;
import org.bukkit.entity.Player;

public class SellCommand extends BaseCommand {
    public SellCommand() {
        super("sell");
    }

    @Default
    public void execute(Player player) {
        SellInventory.sellInventory(player, 0);
    }
}
