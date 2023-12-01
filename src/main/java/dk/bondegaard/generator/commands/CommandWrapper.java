/*
 * Copyright (c) 2023 bondegaard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dk.bondegaard.generator.commands;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.message.MessageKey;
import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.utils.PlaceholderString;
import dk.bondegaard.generator.utils.PlayerUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandWrapper {
    private static Field bukkitCommandsField;

    private static Field commandMapField;

    static {
        try {
            bukkitCommandsField = BukkitCommandManager.class.getDeclaredField("bukkitCommands");
            bukkitCommandsField.setAccessible(true);

            commandMapField = BukkitCommandManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Getter
    private final BukkitCommandManager<CommandSender> triumphCommandManager;

    private final Map<String, Command> allBukkitCommands;

    private final CommandMap commandMap;

    @SneakyThrows
    public CommandWrapper(Main plugin) {
        this.triumphCommandManager = BukkitCommandManager.create(plugin);

        this.allBukkitCommands = (Map<String, org.bukkit.command.Command>) bukkitCommandsField.get(this.triumphCommandManager);
        this.commandMap = (CommandMap) commandMapField.get(this.triumphCommandManager);
    }

    public void register(final @NotNull BaseCommand baseCommand) {
        this.triumphCommandManager.registerCommand(baseCommand);
    }

    public void unregister(final @NotNull BaseCommand baseCommand) {
        this.unregisterSingle(this.getCommandName(baseCommand));
        this.getCommandAliases(baseCommand).forEach(this::unregisterSingle);
    }

    private void unregisterSingle(final String commandName) {
        if (commandName == null) return;

        org.bukkit.command.Command bukkitCommand = this.commandMap.getCommand(commandName);
        if (bukkitCommand == null) return;

        bukkitCommand.unregister(this.commandMap);
        this.allBukkitCommands.remove(commandName);
    }

    private String getCommandName(final @NotNull BaseCommand baseCommand) {
        if (baseCommand.getCommand() != null) return baseCommand.getCommand();

        dev.triumphteam.cmd.core.annotation.Command commandInfo = this.getCommandAnnotation(baseCommand);
        if (commandInfo == null) return null;

        return commandInfo.value();
    }

    private List<String> getCommandAliases(final @NotNull BaseCommand baseCommand) {
        if (!baseCommand.getAlias().isEmpty()) return baseCommand.getAlias();

        dev.triumphteam.cmd.core.annotation.Command commandInfo = this.getCommandAnnotation(baseCommand);
        if (commandInfo == null) return Collections.emptyList();

        return Arrays.asList(commandInfo.alias());
    }

    private dev.triumphteam.cmd.core.annotation.Command getCommandAnnotation(final @NotNull BaseCommand baseCommand) {
        if (baseCommand.getClass().isAnnotationPresent(dev.triumphteam.cmd.core.annotation.Command.class))
            return baseCommand.getClass().getAnnotation(dev.triumphteam.cmd.core.annotation.Command.class);

        return null;
    }

    public void loadMessages() {
        this.triumphCommandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> {
            PlaceholderString unknownCommandMsg = new PlaceholderString(Lang.CMD_UNKNOWN_SUB_COMMAND, "%FULL_COMMAND%")
                    .placeholderValues(context.getCommand() + " " + context.getSubCommand());

            sender.sendMessage(unknownCommandMsg.parse());
        });

        this.triumphCommandManager.registerMessage(MessageKey.INVALID_ARGUMENT,
                (sender, context) -> this.resolveDefaultMessage(sender, Lang.CMD_INVALID_ARGUMENT));

        this.triumphCommandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS,
                (sender, context) -> this.resolveDefaultMessage(sender, Lang.CMD_NOT_ENOUGH_ARGS));

        this.triumphCommandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS,
                (sender, context) -> this.resolveDefaultMessage(sender, Lang.CMD_TOO_MANY_ARGS));

        this.triumphCommandManager.registerMessage(BukkitMessageKey.NO_PERMISSION,
                (sender, context) -> this.resolveDefaultMessage(sender, Lang.CMD_NO_PERMISSION));

        this.triumphCommandManager.registerMessage(BukkitMessageKey.CONSOLE_ONLY,
                (sender, context) -> this.resolveDefaultMessage(sender, Lang.CMD_CONSOLE_ONLY));

        this.triumphCommandManager.registerMessage(BukkitMessageKey.PLAYER_ONLY,
                (sender, context) -> this.resolveDefaultMessage(sender, Lang.CMD_PLAYER_ONLY));
    }

    private void resolveDefaultMessage(CommandSender sender, String message) {
        PlayerUtils.sendMessage(sender, message);
    }
}
