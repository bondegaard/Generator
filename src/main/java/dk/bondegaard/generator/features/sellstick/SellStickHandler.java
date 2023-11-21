package dk.bondegaard.generator.features.sellstick;

import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SellStickHandler {
    private static SellStickHandler instance;

    private static boolean enabled = false;

    private FileConfiguration shopConfig = null;

    private Map<ItemStack, Double> sellStick = new HashMap<>();

    public SellStickHandler() {
        instance = this;
        load();
    }


    public void load() {
        enabled = Main.getInstance().getConfig().contains("sellstick-enabled") && Main.getInstance().getConfig().getBoolean("sellstick-enabled");
        if (!enabled) return;
        sellStick.clear();
        //Load/Create config file
        File dataFile = new File(Main.getInstance().getDataFolder(), "sellstick.yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ex) {
        }

        // Loads the file as a bukkit config
        this.shopConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (!shopConfig.contains("sticks")) {
            // 1x Boost
            shopConfig.set("sticks.1.sell-boost", 1);
            shopConfig.set("sticks.1.item.type", "280:0");
            shopConfig.set("sticks.1.item.name", "&b&lSellStick &f&l1X");
            // 2x Boost
            shopConfig.set("sticks.2.sell-boost", 1.5);
            shopConfig.set("sticks.2.item.type", "280:0");
            shopConfig.set("sticks.2.item.name", "&b&lSellStick &f&l1.5X");
            try {
                shopConfig.save(dataFile);
            } catch (IOException ignored) {
            }
        }
        for (String key : shopConfig.getConfigurationSection("sticks").getKeys(false)) {
            ConfigurationSection section = shopConfig.getConfigurationSection("sticks."+key);
            double boost = section.contains("sell-boost") ? section.getDouble("sell-boost") : 1;
            ItemStack itemStack = ItemUtil.getConfigItem("item", section);
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            sellStick.put(itemStack, boost);
        }
        new SellStickListener(this);
    }
    public double getSellStickMulti(ItemStack itemStack) {
        for (Map.Entry<ItemStack, Double> stick: sellStick.entrySet()) {
            if (stick.getKey().isSimilar(itemStack))
                return stick.getValue();
        }
        return -1;
    }

    public ItemStack getSellStick(double multi) {
        for (Map.Entry<ItemStack, Double> stick: sellStick.entrySet()) {
            if (stick.getValue() == multi)
                return stick.getKey();
        }
        return null;
    }

    public static SellStickHandler getInstance() {return instance;}
    public FileConfiguration getShopConfig() {return shopConfig;}
    public static boolean isEnabled() {return enabled;}

    public static void setEnabled(boolean enabled) {SellStickHandler.enabled = enabled;}
}
