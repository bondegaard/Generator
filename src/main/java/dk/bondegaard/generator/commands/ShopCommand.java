package dk.bondegaard.generator.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import dk.bondegaard.generator.features.shop.ShopHandler;
import org.bukkit.entity.Player;

public class ShopCommand extends BaseCommand {
    public ShopCommand() {
        super("shop");
    }


    @Default
    public void execute(Player player) {
        if (!ShopHandler.isEnabled()) return;
        ShopHandler.openShop(player);
    }
}
