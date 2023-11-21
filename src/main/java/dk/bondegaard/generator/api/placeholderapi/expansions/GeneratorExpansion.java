package dk.bondegaard.generator.api.placeholderapi.expansions;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.languages.Lang;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GeneratorExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "generator";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Generator";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("maxgens")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return Lang.NOT_LOADED;
            return String.valueOf(gPlayer.getMaxGens());
        }
        if (params.equalsIgnoreCase("totalgens")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return Lang.NOT_LOADED;
            return String.valueOf(gPlayer.getGenerators().size());
        }
        if (params.equalsIgnoreCase("prestige")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return Lang.NOT_LOADED;
            return String.valueOf(gPlayer.getPrestige());
        }
        if (params.equalsIgnoreCase("level")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return Lang.NOT_LOADED;
            return String.valueOf(gPlayer.getLevel());
        }
        if (params.equalsIgnoreCase("exp")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return Lang.NOT_LOADED;
            return String.valueOf(gPlayer.getExp());
        }

        return null;
    }
}
