package dk.bondegaard.generator.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Description;
import dk.bondegaard.generator.features.shop.ShopHandler;
import org.bukkit.entity.Player;

@Description(value = "Generator shop command")
@Permission(value = "generator.shop")
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
