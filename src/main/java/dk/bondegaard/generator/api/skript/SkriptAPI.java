package dk.bondegaard.generator.api.skript;

import ch.njol.skript.Skript;
import dk.bondegaard.generator.api.GeneratorAPI;
import dk.bondegaard.generator.api.skript.expressions.*;
import dk.bondegaard.generator.api.skript.expressions.gplayer.*;

public class SkriptAPI {

    private final GeneratorAPI api;

    public SkriptAPI(GeneratorAPI api) {
        this.api = api;
        load();
    }


    private void load() {
        // gplayer
        Skript.registerEffect(ExpAdd.class, "add %number% generator exp to %player%");
        Skript.registerEffect(LevelAdd.class, "add %number% generator level to %player%");
        Skript.registerEffect(PrestigeAdd.class, "add %number% generator prestige to %player%");
        Skript.registerEffect(MultiplierAdd.class, "add %number% multiplier to %player%");

        Skript.registerEffect(ExpRemove.class, "remove %number% generator exp from %player%");
        Skript.registerEffect(LevelRemove.class, "remove %number% generator level from %player%");
        Skript.registerEffect(PrestigeRemove.class, "remove %number% generator prestige from %player%");
        Skript.registerEffect(MultiplierRemove.class, "remove %number% multiplier from %player%");

        Skript.registerEffect(ExpReset.class, "reset generator exp for %player%");
        Skript.registerEffect(LevelReset.class, "reset generator level for %player%");
        Skript.registerEffect(PrestigeReset.class, "reset generator prestige for %player%");
        Skript.registerEffect(MulitiplierReset.class, "reset multiplier for %player%");

        Skript.registerEffect(ExpSet.class, "set %player% generator exp to %number%");
        Skript.registerEffect(LevelSet.class, "set %player% generator level to %number%");
        Skript.registerEffect(PrestigeSet.class, "set %player% generator prestige to %number%");
        Skript.registerEffect(MultiplierSet.class, "set multiplier for %player% to %number%");

        // Other
        Skript.registerEffect(SetMaxGens.class, "set maxgens for %players% to %number%");
        Skript.registerEffect(AddMaxGens.class, "add maxgens for %players% to %number%");
        Skript.registerEffect(RemoveMaxGens.class, "remove maxgens for %players% to %number%");
        Skript.registerEffect(SellInventory.class, "sell generator drops for %players%");
        Skript.registerEffect(SellInventoryWithMultiplier.class, "sellgenerator drops with multi %number% for %players%");
        Skript.registerEffect(GetGeneratorItem.class, "give %players% generator %string%");
        Skript.registerEffect(PickupGens.class, "pickup gens for %players%");
        Skript.registerEffect(RemoveOfflineplayerGen.class, "remove gen for %offlineplayers% at %location%");
        Skript.registerEffect(RemoveOfflineplayerGensPickup.class, "pickup gen for %offlineplayers% at %location% to %player%");
        Skript.registerEffect(OpenShop.class, "open shop for %players%");
        Skript.registerEffect(GetSellstick.class, "give %players% sellstick whit multi %number%");
    }
}

