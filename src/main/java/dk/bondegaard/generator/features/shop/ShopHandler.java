package dk.bondegaard.generator.features.shop;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ShopHandler {

    private static ShopHandler instance;

    private static boolean enabled = false;

    private FileConfiguration shopConfig = null;

    public ShopHandler() {
        instance = this;
        load();
    }

    public static void openShop(Player player) {
        if (!enabled) return;
        if (player == null) return;
        new Shop(player, instance).open(player);
    }

    public void load() {
        enabled = Main.getInstance().getConfig().contains("shop-enabled") && Main.getInstance().getConfig().getBoolean("shop-enabled");
        if (!enabled) return;
        //Load/Create config file
        File dataFile = new File("shop.yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ex) {
        }

        // Loads the file as a bukkit config
        this.shopConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (shopConfig.contains("gui-layout")) return;
        int i = 0;
        for (GeneratorType generatorType:Main.getInstance().getGeneratorHandler().getGeneratorTypes()) {
            shopConfig.set("gui-layout."+i+".name", generatorType.getName());
            shopConfig.set("gui-layout."+i+".price", 500 * (i+1));
            i++;
        }
        try {shopConfig.save(dataFile); } catch (IOException ignored) {}
    }


    public static ShopHandler getInstance() {return instance;}
    public FileConfiguration getShopConfig() {return shopConfig;}
    public static boolean isEnabled() {return enabled;}

    public static void setEnabled(boolean enabled) {ShopHandler.enabled = enabled;}
}
