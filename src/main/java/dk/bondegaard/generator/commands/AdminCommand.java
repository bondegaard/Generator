package dk.bondegaard.generator.commands;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            PlayerUtils.sendMessage(sender, Lang.PREFIX+"&e/generatoradmin reload &fReload plugin'et");
            PlayerUtils.sendMessage(sender, Lang.PREFIX+"&e/generatoradmin getgenerator <Name> &fFå en generator!");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            Main.getInstance().reloadConfig();
            Main.getInstance().getGeneratorHandler().loadGeneratorTypes();
            Lang.reload();
            PlayerUtils.sendMessage(sender, Lang.PREFIX+"&eGenerator plugin reloaded!");
            return true;
        }
        if (args[0].equalsIgnoreCase("getgenerator")) {
            if (!(sender instanceof Player)) {
                PlayerUtils.sendMessage(sender, Lang.PREFIX+Lang.ERROR.replace("%ERROR%", "&cDu er ikke en spiller!"));
                return true;
            }
            if (args.length < 2) {
                PlayerUtils.sendMessage(sender, Lang.PREFIX+"Brug &e/generatoradmin getgenerator <Name> &fFå en generator!");
                return true;
            }
            GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(args[1]);
            if (generatorType == null) {
                PlayerUtils.sendMessage(sender, Lang.PREFIX+Lang.ERROR.replace("%ERROR%", "&cUgyldig generator navn!"));
                return true;
            }
            PlayerUtils.sendMessage(sender, Lang.PREFIX+"Du modtag en "+generatorType.getName()+" generator!");
            ((Player) sender).getInventory().addItem(generatorType.getGeneratorItem());
        }

        return true;
    }
}
