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
        Shop.open(instance, player);
    }

    public void load() {
        enabled = Main.getInstance().getConfig().contains("shop-enabled") && Main.getInstance().getConfig().getBoolean("shop-enabled");
        if (!enabled) return;
        //Load/Create config file
        File dataFile = new File(Main.getInstance().getDataFolder(), "shop.yml");
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
