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
import dk.bondegaard.generator.generators.objects.GeneratorType;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import dk.bondegaard.generator.utils.PlaceholderString;
import dk.bondegaard.generator.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
        PlayerUtils.sendMessage(sender, Lang.PREFIX + "&e/generatoradmin getgenerator <navn> &fGet a generator!");
    }

    @SubCommand(value = "info", alias = {"information", "credit", "kredit"})
    public void info(CommandSender sender) {
        PlayerUtils.sendMessage(sender, "");
        PlayerUtils.sendMessage(sender, "&aGenerator (" + Main.getInstance().getDescription().getVersion() + "):");
        PlayerUtils.sendMessage(sender, "&aCredit: &bBondegaard");
        PlayerUtils.sendMessage(sender, "&aVersion: &b" + Main.getInstance().getDescription().getVersion());
        PlayerUtils.sendMessage(sender, "&aGithub: &bhttps://github.com/bondegaard/Generator");
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

    @SubCommand(value = "debug")
    public void debug(Player player) {
        GPlayer gPlayer = PlayerDataHandler.getOrCreateGPlayer(player);

        PlayerUtils.sendMessage(player, "EXP: " + gPlayer.getExp());
        PlayerUtils.sendMessage(player, "LEVEL: " + gPlayer.getLevel());
        PlayerUtils.sendMessage(player, "PRESTIGE: " + gPlayer.getPrestige());
        PlayerUtils.sendMessage(player, "MAX GENS: " + gPlayer.getMaxGens());
        PlayerUtils.sendMessage(player, "DATA: " + gPlayer.getData().toString());
    }
}
