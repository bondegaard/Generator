package dk.bondegaard.generator.utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerUtils {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(StringUtil.colorize(message));
    }

    public static void sendMessage(CommandSender sender, PlaceholderString message) {
        sender.sendMessage(StringUtil.colorize(message.parse()));
    }

    public static void sendTextComponent(Player player, TextComponent textComponent) {
        textComponent.setText(StringUtil.colorize(textComponent.getText()));
        player.spigot().sendMessage(textComponent);
    }
}