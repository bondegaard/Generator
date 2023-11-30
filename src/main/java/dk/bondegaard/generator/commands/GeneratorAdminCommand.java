package dk.bondegaard.generator.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Description;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.features.sellstick.SellStickHandler;
import dk.bondegaard.generator.features.shop.ShopHandler;
import dk.bondegaard.generator.generators.GeneratorHandler;
import dk.bondegaard.generator.generators.objects.GeneratorDropItem;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.NumUtils;
import dk.bondegaard.generator.utils.PlaceholderString;
import dk.bondegaard.generator.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Description(value = "Generator admin command")
@Permission(value = "generator.admin")
public class GeneratorAdminCommand extends BaseCommand {

    public GeneratorAdminCommand() {
        super("generatoradmin", Arrays.asList( "generatora", "gadmin"));

        Main.getInstance().getCommandWrapper().getTriumphCommandManager().registerSuggestion(SuggestionKey.of("gens"), (sender, context) -> {
            if (context.getArgs().size() < 1) {
                return Main.getInstance().getGeneratorHandler().getGeneratorTypes().stream().map(GeneratorType::getName).map(String::toLowerCase).collect(Collectors.toList());
            }
            return Main.getInstance().getGeneratorHandler().getGeneratorTypes().stream().map(GeneratorType::getName).map(String::toLowerCase).filter(s -> s.startsWith(context.getArgs().get(0).toLowerCase())).collect(Collectors.toList());
        });
    }

    @Default
    public void execute(CommandSender sender) {
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin reload &fReload the plugin");
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin info &fGet information about the plugin.");
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin getgenerator <name> &fGet a generator!");
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin removegenerator <name> &fDelete/Remove a generator!");
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin addgenerator <name> <upgrade price> [next gen] &fAdd/Create a generator!");
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin addgendrop <generator name> <sell price> &fAdd a drop to a generator!");
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin removegendrop <generator name> <generator drop id> &fRemove a drop from a generator!");
    }

    @SubCommand(value = "info", alias = {"information", "credit", "kredit"})
    public void info(CommandSender sender) {
        PlayerUtils.sendMessage(sender, "");
        PlayerUtils.sendMessage(sender, "&a&lGenerator &r&a(" + Main.getInstance().getDescription().getVersion() + "):");
        PlayerUtils.sendMessage(sender, " &a* Credit: &bBondegaard");
        PlayerUtils.sendMessage(sender, " &a* Version: &b" + Main.getInstance().getDescription().getVersion());
        PlayerUtils.sendMessage(sender, " &a* Spigot: &bhttps://www.spigotmc.org/resources/generator.113773/");
        PlayerUtils.sendMessage(sender, " &a* Github: &bhttps://github.com/bondegaard/Generator");
    }

    @SubCommand(value = "reload")
    public void reload(CommandSender sender) {
        Main.getInstance().reloadConfig();
        Main.getInstance().getGeneratorHandler().reload();
        SellStickHandler.getInstance().load();
        ShopHandler.getInstance().load();
        Lang.reload();
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&eGenerator plugin reloaded!");
    }

    @SubCommand(value = "playerinfo")
    public void playerInfo(Player player, Player target) {
        if (!target.isOnline()) return;
        GPlayer gPlayer = PlayerDataHandler.getOrCreateGPlayer(target);
        PlayerUtils.sendMessage(player, " §c§lPlayer Information:");
        PlayerUtils.sendMessage(player, " §4* §cName §8- §e" + target.getName());
        PlayerUtils.sendMessage(player, " §4* §cMulti §8- §e" + gPlayer.getMultiplier());
        PlayerUtils.sendMessage(player, " §4* §cPrestige §8- §e" + gPlayer.getPrestige());
        PlayerUtils.sendMessage(player, " §4* §cLevel §8- §e" + gPlayer.getLevel());
        PlayerUtils.sendMessage(player, " §4* §cExp §8- §e" + gPlayer.getExp());
    }

    @SubCommand(value = "getgenerator")
    public void getGenerator(Player player, @Suggestion(value = "gens") String generatorName) {
        GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(generatorName);
        if (generatorType == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.INVALID_GEN_NAME);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }
        PlaceholderString recieveMessage = new PlaceholderString(Lang.PREFIX+Lang.GENS_RECEIVE, "%TYPE%")
                .placeholderValues(generatorType.getName());

        PlayerUtils.sendMessage(player, recieveMessage);
        player.getInventory().addItem(generatorType.getGeneratorItem());
    }

    @SubCommand(value = "generatoreinfo", alias = {"geninfo", "generatorinformation", "geninformation"})
    public void generatoreInformation(Player player, @Suggestion(value = "gens")  String genName) {
        GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(genName);
        // Check name of choosen generator
        if (generatorType == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.GEN_DOES_NOT_EXIST);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        PlayerUtils.sendMessage(player, " §c§lGenerator Information:");
        PlayerUtils.sendMessage(player, " §4* §cName §8- §e" + generatorType.getName());
        if (!(generatorType.getNextGeneratorName() == null || generatorType.getNextGeneratorName().isEmpty())) PlayerUtils.sendMessage(player, " §4* §cNext Gen §8- §e" + generatorType.getNextGeneratorName());
        PlayerUtils.sendMessage(player, " §4* §cUpgrade Price §8- §e" + NumUtils.formatNumber(generatorType.getUpgradePrice())+"$");

        if (generatorType.getGeneratorDrops().isEmpty()) return;
        PlayerUtils.sendMessage(player, " §4* §cGenerator Drops:");
        for (GeneratorDropItem generatorDropItem : generatorType.getGeneratorDrops()) {
            PlayerUtils.sendMessage(player, " §4* | §cID §8- §e"+generatorDropItem.getId());
            PlayerUtils.sendMessage(player, " §4* | §cSELL PRICE §8- §e"+generatorDropItem.getSellPrice());
            PlayerUtils.sendMessage(player, " §4* | §cITEM TYPE §8- §e"+generatorDropItem.getItem().getType().name());
            PlayerUtils.sendMessage(player, " §4* |");
        }
    }

    @SubCommand(value = "removegen", alias = {"removegenerator"})
    public void removeGen(Player player, @Suggestion(value = "gens")  String genName) {

        GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(genName);
        // Check name of choosen generator
        if (generatorType == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.GEN_DOES_NOT_EXIST);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        Main.getInstance().getGeneratorHandler().removeGeneratorType(generatorType);

        // Send Generator removed message
        PlaceholderString addedMessage = new PlaceholderString(Lang.PREFIX + Lang.ADMIN_CMD_REMOVE_GEN, "%TYPE%")
                .placeholderValues(generatorType.getName());
        PlayerUtils.sendMessage(player, addedMessage);
        return;
    }

    /**
     * Subcommand used to add a new type of generator
     * @param player
     * @param args
     */
    @SubCommand(value = "addgen", alias = {"addgenerator"})
    public void addGen(Player player, List<String> args) {
        // Check if player is using correct args
        if (args.size() < 2) {
            PlaceholderString missingArgsMessage = new PlaceholderString(Lang.PREFIX+Lang.ADMIN_CMD_ADD_GEN_MISSING_ARGS);
            PlayerUtils.sendMessage(player, missingArgsMessage);
            return;
        }

        // Get player inputs
        String name = args.get(0);
        double upgradePrice = 0;
        String nextGen = args.size() > 2 ? args.get(2) : "";

        // Parse upgradePrice to double
        try {
            upgradePrice = Double.parseDouble(args.get(1));
        } catch (NumberFormatException ex) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.STRING_IS_NOT_NUMBER);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        // Check name of next choosen generator
        GeneratorType nextType;
        if (!nextGen.equals("")) {
            nextType = Main.getInstance().getGeneratorHandler().getGeneratorType(nextGen);
            if (nextType == null) {
                PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                        .placeholderValues(Lang.INVALID_NEXT_GEN_NAME);
                PlayerUtils.sendMessage(player, errorMessage);
              return;
            }
        }

        // Check name of choosen generator
        if (Main.getInstance().getGeneratorHandler().getGeneratorType(name) != null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.GEN_ALREADY_EXIST);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        ItemStack playerHeldItem = player.getItemInHand();
        //Check player heldItem
        if (playerHeldItem == null || playerHeldItem.getType() == Material.AIR) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.ADMIN_CMD_ADD_GEN_HOLD_GEN);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        long timeBetween = Main.getInstance().getConfig().contains("time-inbetween") ? Main.getInstance().getConfig().getLong("time-inbetween") : 5000;

        GeneratorType generatorType = new GeneratorType(name, playerHeldItem, new ArrayList<>(), nextGen, upgradePrice, timeBetween);
        Main.getInstance().getGeneratorHandler().addGeneratorType(generatorType);

        // Send Generator added message
        PlaceholderString addedMessage = new PlaceholderString(Lang.PREFIX + Lang.ADMIN_CMD_ADD_GEN, "%TYPE%")
                .placeholderValues(generatorType.getName());
        PlayerUtils.sendMessage(player, addedMessage);
    }

    /**
     * Add a Generator drop to a Generator
     * @param player
     * @param args
     */
    @SubCommand(value = "addgendrop", alias = {"addgendrops", "addgeneratordrops"})
    public void addGeneratorDrops(Player player, List<String> args) {
        // Check if player is using correct args
        if (args.size() < 2) {
            PlaceholderString missingArgsMessage = new PlaceholderString(Lang.PREFIX+Lang.ADMIN_CMD_ADD_GENDROP_MISSING_ARGS);
            PlayerUtils.sendMessage(player, missingArgsMessage);
            return;
        }

        String genName = args.get(0);
        double sellPrice;
        ItemStack playerHeldItem = player.getItemInHand();

        // Parse sellPrice to double
        try {
            sellPrice = Double.parseDouble(args.get(1));
        } catch (NumberFormatException ex) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.STRING_IS_NOT_NUMBER);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        //Check player heldItem
        if (playerHeldItem == null || playerHeldItem.getType() == Material.AIR) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.ADMIN_CMD_ADD_GENDROP_HOLD_GENDROP);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        // Check name of choosen generator
        GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(genName);
        if (generatorType == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.GEN_DOES_NOT_EXIST);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        generatorType.addGeneratorDrop(sellPrice, playerHeldItem);

        // Send Generator added message
        PlaceholderString addedMessage = new PlaceholderString(Lang.PREFIX + Lang.ADMIN_CMD_ADD_GENDROP, "%TYPE%")
                .placeholderValues(generatorType.getName());
        PlayerUtils.sendMessage(player, addedMessage);
    }

    @SubCommand(value = "removegendrop", alias = {"removegendrops", "removegeneratordrops"})
    public void removeGeneratorDrops(Player player, @Suggestion(value = "gens")  String genName, String genDropID) {
        GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(genName);
        // Check name of choosen generator
        if (generatorType == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.GEN_DOES_NOT_EXIST);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        GeneratorDropItem generatorDropItem = null;
        for (GeneratorDropItem gd : generatorType.getGeneratorDrops()) {
            if (!gd.getId().equalsIgnoreCase(genDropID)) continue;
            generatorDropItem = gd;
            break;
        }

        // Check name of choosen generator
        if (generatorDropItem == null) {
            PlaceholderString errorMessage = new PlaceholderString(Lang.PREFIX + Lang.ERROR, "%ERROR%")
                    .placeholderValues(Lang.GENDROP_DOES_NOT_EXIST);
            PlayerUtils.sendMessage(player, errorMessage);
            return;
        }

        // Send Generator added message
        PlaceholderString removeMessage = new PlaceholderString(Lang.PREFIX + Lang.ADMIN_CMD_REMOVE_GENDROP, "%TYPE%")
                .placeholderValues(generatorType.getName());
        PlayerUtils.sendMessage(player, removeMessage);
    }
}
