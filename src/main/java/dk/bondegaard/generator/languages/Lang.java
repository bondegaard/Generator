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

package dk.bondegaard.generator.languages;

import dk.bondegaard.generator.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Lang {
    private static final Main instance = Main.getInstance();


    // Default Messages
    public static String PREFIX;
    public static String PERMISSION_DENY;
    public static String ERROR;

    // Command messages
    public static String CMD_UNKNOWN_SUB_COMMAND;
    public static String CMD_INVALID_ARGUMENT;
    public static String CMD_NOT_ENOUGH_ARGS;
    public static String CMD_TOO_MANY_ARGS;
    public static String CMD_NO_PERMISSION;
    public static String CMD_CONSOLE_ONLY;
    public static String CMD_PLAYER_ONLY;
    public static String ADMIN_CMD_ADD_GEN;
    public static String ADMIN_CMD_ADD_GENDROP;
    public static String ADMIN_CMD_REMOVE_GEN;

    public static String ADMIN_CMD_REMOVE_GENDROP;
    public static String ADMIN_CMD_ADD_GEN_MISSING_ARGS;
    public static String ADMIN_CMD_ADD_GEN_HOLD_GEN;
    public static String ADMIN_CMD_ADD_GENDROP_MISSING_ARGS;
    public static String ADMIN_CMD_ADD_GENDROP_HOLD_GENDROP;

    // Generator messages
    public static String GENS_MAX;
    public static String GENS_PLACE;
    public static String GENS_UPGRADED_SUCCESS;
    public static String GENS_UPGRADED_INVALID_FOUNDS;
    public static String GENS_UPGRADED_MAX;
    public static String GENS_UPGRADED_NO_UPGRADE;
    public static String GENS_REMOVED;
    public static String GENS_NOT_YOURS;
    public static String GENS_RECEIVE;

    // SELL
    public static String SELL_SUCCESS;
    public static String SELL_FAIL;

    // SHOP
    public static String SHOP_GUI_TITLE;
    public static String SHOP_BUY_SUCCESS;
    public static String SHOP_BUY_FAIL;
    public static String SHOP_FULL_INVENTORY;
    public static List<String> SHOP_ITEM_LORE;

    // SELL STICK
    public static String SELLSTICK_SELL_MESSAGE;
    public static String SELLSTICK_SELL_NOTHING;

    // Error
    public static String NO_ECONOMY;
    public static String STRING_IS_NOT_NUMBER;
    public static String INVALID_GEN_NAME;
    public static String INVALID_NEXT_GEN_NAME;
    public static String GEN_ALREADY_EXIST;
    public static String GEN_DOES_NOT_EXIST;
    public static String GENDROP_DOES_NOT_EXIST;
    public static String GEN_BUGGED_RETURNED;

    // Placeholders
    public static String NOT_LOADED;

    public Lang() {
        loadLangConfig();
    }

    public static void reload() {
        loadLangConfig();
    }

    private static void loadLangConfig() {

        //Creates the unique data file if it doesn't exist
        File dataFile = new File(Main.getInstance().getDataFolder(), "lang.yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ignored) {
        }

        FileConfiguration lang = YamlConfiguration.loadConfiguration(dataFile);
        lang.options()
                .header("Use this file to change the lang of Generator.\n\n" +
                        "Changes in this file requires a restart of the server\n" +
                        "or the use of the command /gadmin reload for the changes to be shown on your server.\n")
                .copyDefaults(true);

        // Default lang.yml
        PREFIX = getString(lang, "prefix", "&8[&b&lGenerator&8] &f");
        PERMISSION_DENY = getString(lang, "permission-denied", "&cDu har ikke adgang til dette!");
        ERROR = getString(lang, "error-message", "Der skete en fejl: %ERROR%");

        // Command messages
        CMD_UNKNOWN_SUB_COMMAND = getString(lang, "cmd-unknown-sub-command", "&7Kommandoen &c/%FULL_COMMAND% &7findes ikke.");
        CMD_INVALID_ARGUMENT = getString(lang, "cmd-invalid-argument", "&7Ugyldigt argument, prøv et andet.");
        CMD_NOT_ENOUGH_ARGS = getString(lang, "cmd-not-enough-args", "&7Kommandoen har for få argumenter.");
        CMD_TOO_MANY_ARGS = getString(lang, "cmd-too-many-args", "&7Kommandoen har for mange argumenter.");
        CMD_NO_PERMISSION = getString(lang, "cmd-no-permission", "&7Du har ikke tilladelse til at bruge kommandoen.");
        CMD_CONSOLE_ONLY = getString(lang, "cmd-console-only", "&7Kommandoen kan kun bruges af konsollen.");
        CMD_PLAYER_ONLY = getString(lang, "cmd-player-only", "&7Kommandoen kan kun bruges af spillere.");
        ADMIN_CMD_ADD_GEN = getString(lang, "admin-cmd-add-gen", "&7Du har tilføjet generatoren %TYPE%.");
        ADMIN_CMD_ADD_GENDROP = getString(lang, "admin-cmd-add-gendrop", "&7Du har tilføjet et generatordrop til generatoren: %TYPE%.");
        ADMIN_CMD_REMOVE_GEN = getString(lang, "admin-cmd-remove-gen", "&7Du har fjernet generatoren %TYPE%.");
        ADMIN_CMD_REMOVE_GENDROP = getString(lang, "admin-cmd-remove-gendrop", "&7Du har fjernet et generator drop fra generatoren %TYPE%.");
        ADMIN_CMD_ADD_GEN_MISSING_ARGS = getString(lang, "admin-cmd-add-gen-missing-args", "&7Brug &c/gadmin addgen <navn> <opgraderingspris> [næste gen]");
        ADMIN_CMD_ADD_GEN_HOLD_GEN = getString(lang, "admin-cmd-add-gen-hold_gen", "&7Du skal holde en blok i hånden!");
        ADMIN_CMD_ADD_GENDROP_MISSING_ARGS = getString(lang, "admin-cmd-add-gendrop-missing-args", "&7Brug &c/gadmin addgendrop <generator navn> <sælgpris>");
        ADMIN_CMD_ADD_GENDROP_HOLD_GENDROP = getString(lang, "admin-cmd-add-gendrop-hold_gendrop", "&7Du skal holde et item i hånden!");

        // Generator messages
        GENS_MAX = getString(lang, "generator.max-placed-message", "Du kan ikke placere flere generatorer. (%PLACED%/%MAX%)");
        GENS_PLACE = getString(lang, "generator.place-message", "Du placerede en %TYPE% generator. (%PLACED%/%MAX%)");
        GENS_UPGRADED_SUCCESS = getString(lang, "generator.upgrade-success-message", "Du har opgraderet din generator til %TYPE%");
        GENS_UPGRADED_INVALID_FOUNDS = getString(lang, "generator.upgrade-invalid-founds", "Du mangler %NEEDED%$ for at opgradere din generator");
        GENS_UPGRADED_MAX = getString(lang, "generator.upgrade-max", "Du kan ikke opgradere denne generator!");
        GENS_UPGRADED_NO_UPGRADE = getString(lang, "generator.no-upgrade", "Denne generator kan ikke opgraderes!");

        GENS_REMOVED = getString(lang, "generator.remove-success", "Du har fjernet din generator");
        GENS_NOT_YOURS = getString(lang, "generator.wrong-owner", "Du ejer ikke denne generator");

        GENS_RECEIVE = getString(lang, "generator.receive", "Du modtog en %TYPE% generator!");

        // Sell
        SELL_SUCCESS = getString(lang, "sell.sell-success", "Du solgte din beholdning for %TOTAL%$ (%MULTIPLIER%x)");
        SELL_FAIL = getString(lang, "sell.sell-fail", "Du har ikke noget at sælge!");

        // Shop
        SHOP_GUI_TITLE = getString(lang, "shop.gui-title", "&b&lGenerator Butik");
        SHOP_BUY_SUCCESS = getString(lang, "shop.buy-success", "Du købte en %TYPE% generator for %PRICE%$");
        SHOP_BUY_FAIL = getString(lang, "shop.buy-fail", "Du mangler %NEEDED%$ for at købe denne generator!");
        SHOP_FULL_INVENTORY = getString(lang, "shop.full-inventory", "Du har ikke nok plads i din beholdning til dette!");
        SHOP_ITEM_LORE = getStringList(lang, "shop.shop-item-lore", Arrays.asList("&bPris: &f%PRICE%$", "", "&b&nTryk for at købe!"));

        // Sell Stick
        SELLSTICK_SELL_MESSAGE = getString(lang, "sell-stick.sell-message", "Du solgte kisten for %amount%$");
        SELLSTICK_SELL_NOTHING = getString(lang, "sell-stick.empty-chest-message", "Der var ikke noget at sælge i kisten!");

        // Error
        NO_ECONOMY = getString(lang, "no-economy-found", "Økonomi ikke konfigureret!");
        STRING_IS_NOT_NUMBER = getString(lang, "string_is_not_number", "Ugyldigt tal.");
        INVALID_GEN_NAME = getString(lang, "invalid-gen-name", "&cUgyldigt generatornavn!");
        INVALID_NEXT_GEN_NAME = getString(lang, "invalid-next-gen-name", "&cUgyldigt generatornavn valgt til næste generator!");
        GEN_ALREADY_EXIST = getString(lang, "gen_already_exist", "&cGeneratorens navn er allerede i brug!");
        GEN_DOES_NOT_EXIST = getString(lang, "gen_does_not_exist", "&cGeneratorens navn findes ikke!");
        GENDROP_DOES_NOT_EXIST = getString(lang, "gendrop_does_not_exist", "&cGeneratordrop findes ikke!");
        GEN_BUGGED_RETURNED = getString(lang, "gen-bugged-returned", "&cDu havde en eller flere buggede generators som du har fået tilbage!");

        // Placeholders
        NOT_LOADED = getString(lang, "not-loaded", "Ikke indlæst...!");


        try {
            lang.save(dataFile);
        } catch (IOException ex) {
            instance.getLogger().log(Level.WARNING, "Couldn't save default lang.yml");
        }
    }

    private static String getString(FileConfiguration lang, String path, String def, int maxLenth) {
        if (!lang.contains(path)) lang.set(path, def);
        String string = ChatColor.translateAlternateColorCodes('&', lang.getString(path, def));

        if (maxLenth != -1 && string.length() > maxLenth) {
            Bukkit.getLogger().severe("The message " + path + " is too long, max length is " + maxLenth + " characters!");
            string = string.substring(0, maxLenth);
        }

        return string;
    }

    private static String getString(FileConfiguration lang, String path, String def) {
        return getString(lang, path, def, -1);
    }

    private static List<String> getStringList(FileConfiguration lang, String path, List<String> def) {
        if (!lang.contains(path)) lang.set(path, def);
        List<String> list = lang.getStringList(path);
        if (list.isEmpty()) {
            list = def;
            lang.set(path, def);
        }

        // Translate color codes.
        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }

        return list;
    }
}
