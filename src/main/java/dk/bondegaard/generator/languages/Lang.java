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
    public static String INVALID_GEN_NAME;

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

        // Default lang.yml
        PREFIX = getString(lang, "prefix", "&8[&b&lGenerator&8] &f");
        PERMISSION_DENY = getString(lang, "permission-denied", "&cDu har ikke adgang til dette!");
        ERROR = getString(lang, "error-message", "Der skete en fejl: %ERROR%");

        // Command messages
        CMD_UNKNOWN_SUB_COMMAND = getString(lang, "cmd-unknown-sub-command", "&7Kommandoen &c/%FULL_COMMAND% &7findes ikke.");
        CMD_INVALID_ARGUMENT = getString(lang, "cmd-invalid-argument", "&7Ugyldigt argument, prøv et andet.");
        CMD_NOT_ENOUGH_ARGS = getString(lang, "cmd-not-enough-args", "&7Kommandoen har for lidt argumenter.");
        CMD_TOO_MANY_ARGS = getString(lang, "cmd-too-many-args", "&7Kommandoen har for mange argumenter.");
        CMD_NO_PERMISSION = getString(lang, "cmd-no-permission", "&7Du har ikke adgang til at bruge Kommandoen.");
        CMD_CONSOLE_ONLY = getString(lang, "cmd-console-only", "&7Kommandoen kan kun blive brugt af console.");
        CMD_PLAYER_ONLY = getString(lang, "cmd-player-only", "&7Kommandoen kan kun blive brugt af spillere.");

        // Generator messages
        GENS_MAX = getString(lang, "generator.max-placed-message", "Du kan ikke placere flere generators. (%PLACED%/%MAX%)");
        GENS_PLACE = getString(lang, "generator.place-message", "Du placerede en %TYPE% generator. (%PLACED%/%MAX%)");
        GENS_UPGRADED_SUCCESS = getString(lang, "generator.upgrade-success-message", "Du har opgraderet din generator til %TYPE%");
        GENS_UPGRADED_INVALID_FOUNDS = getString(lang, "generator.upgrade-invalid-founds", "Du mangler %NEEDED%$ for at upgrade din generator");
        GENS_UPGRADED_MAX = getString(lang, "generator.upgrade-max", "Du kan ikke opgradere denne generator!");
        GENS_UPGRADED_NO_UPGRADE = getString(lang, "generator.no-upgrade", "Denne generator kan ikke opgrades!");

        GENS_REMOVED = getString(lang, "generator.remove-success", "Du har fjernet din generator");
        GENS_NOT_YOURS = getString(lang, "generator.wrong-owner", "Du ejer ikke denne generator");

        GENS_RECEIVE = getString(lang, "generator.reveive", "Du modtag en %TYPE% generator!");

        // Sell
        SELL_SUCCESS = getString(lang, "sell.sell-success", "Du solgte din inventory for %TOTAL%$ (%MULTIPLIER%x)");
        SELL_FAIL = getString(lang, "sell.sell-fail", "Du har ikke noget at sælge!");

        // Shop
        SHOP_GUI_TITLE = getString(lang, "shop.gui-title", "&b&lGenerator Shop");
        SHOP_BUY_SUCCESS = getString(lang, "shop.buy-success", "Du købte en %TYPE% generator for %PRICE%$");
        SHOP_BUY_FAIL = getString(lang, "shop.buy-fail", "Du mangler %NEEDED%$ købe denne generator!");
        SHOP_FULL_INVENTORY = getString(lang, "shop.full-inventory", "Du har ikke nok plads i din inventory til dette!");
        SHOP_ITEM_LORE = getStringList(lang, "shop.shop-item-lore", Arrays.asList("&bPris: &f%PRICE%$", "", "&b&nTryk for at købe!"));

        // Sell Stick
        SELLSTICK_SELL_MESSAGE = getString(lang, "sell-stick.sell-message", "Du solgte kisten for %amount%$");
        SELLSTICK_SELL_NOTHING = getString(lang, "sell-stick.empty-chest-message", "Der var ikke noget at sælge i kisten!");

        // Error
        NO_ECONOMY = getString(lang, "no-economy-found", "Økonomi ikke opsat!");
        INVALID_GEN_NAME = getString(lang, "invalid-gen-name", "&cUgyldig generator navn!");

        // Placeholders
        NOT_LOADED = getString(lang, "not-loaded", "Not Loaded...!");

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
