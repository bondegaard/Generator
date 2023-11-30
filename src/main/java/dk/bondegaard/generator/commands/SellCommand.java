package dk.bondegaard.generator.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Description;
import dk.bondegaard.generator.features.SellInventory;
import org.bukkit.entity.Player;

@Description(value = "Generator sell command")
@Permission(value = "generator.sell")
public class SellCommand extends BaseCommand {
    public SellCommand() {
        super("sell");
    }

    @Default
    public void execute(Player player) {
        SellInventory.sellInventory(player, 0);
    }
}
