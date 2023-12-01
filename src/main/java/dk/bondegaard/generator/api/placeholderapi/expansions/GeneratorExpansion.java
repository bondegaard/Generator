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
        if (params.equalsIgnoreCase("multiplier")) {
            GPlayer gPlayer = PlayerDataHandler.getGPlayer((Player) player);
            if (gPlayer == null) return Lang.NOT_LOADED;
            return String.valueOf(gPlayer.getMultiplier());
        }

        return null;
    }
}
