package dk.bondegaard.generator.languages;

import com.google.gson.JsonArray;
import dk.bondegaard.generator.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Lang {
    // Default Messages
    public static String PREFIX;
    public static String ERROR;
    // Generator messages
    public static String GENS_MAX;
    public static String GENS_PLACE;
    private final Main instance;


    public Lang(Main instance) {
        this.instance = instance;
        loadLangConfig();
    }

    private void loadLangConfig() {
        boolean pathChanges = false;

        //Creates the unique data file if it doesn't exist
        File dataFile = new File("lang.yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ex) {
        }

        FileConfiguration lang = YamlConfiguration.loadConfiguration(dataFile);

        // Default lang.yml
        PREFIX = getString(lang, "prefix", "&8[&6&lGenerator&8] &f");
        ERROR = getString(lang, "error-message", "Der skete en fejl.");

        // Generator messages
        GENS_MAX = getString(lang, "generator.max-placed-message", "Du kan ikke placere flere generators. (%PLACED%/%MAX%)");
        GENS_PLACE = getString(lang, "generator.place-message", "Du placerede en %TYPE% generator. (%PLACED%/%MAX%)");

        try {
            lang.save(dataFile);
        } catch (IOException ex) {
            instance.getLogger().log(Level.WARNING, "Couldn't save default lang.yml");
        }
    }

    private String getString(FileConfiguration lang, String path, String def, int maxLenth) {
        if (!lang.contains(path)) lang.set(path, def);
        String string = ChatColor.translateAlternateColorCodes('&', lang.getString(path, def));

        if (maxLenth != -1 && string.length() > maxLenth) {
            Bukkit.getLogger().severe("The message " + path + " is too long, max length is " + maxLenth + " characters!");
            string = string.substring(0, maxLenth);
        }

        return string;
    }

    private String getString(FileConfiguration lang, String path, String def) {
        return getString(lang, path, def, -1);
    }

    private List<String> getStringList(FileConfiguration lang, String path, List<String> def) {
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

    private List<String> getStringList(FileConfiguration lang, String path, JsonArray def) {
        List<String> defList = new ArrayList<>();
        def.forEach(e -> defList.add(e.getAsString()));
        return getStringList(lang, path, defList);
    }
}
