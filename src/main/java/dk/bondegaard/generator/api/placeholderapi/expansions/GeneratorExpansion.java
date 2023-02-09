package dk.bondegaard.generator.api.placeholderapi.expansions;

import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GeneratorExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "generator";
    }

    @Override
    public String getAuthor() {
        return "Generator";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("maxgens")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return "Not Loaded...!";
            return gPlayer.getMaxGens() + "";
        }
        if (params.equalsIgnoreCase("totalgens")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return "Not Loaded...!";
            return gPlayer.getGenerators().size() + "";
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
