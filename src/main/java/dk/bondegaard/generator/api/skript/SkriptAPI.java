package dk.bondegaard.generator.api.skript;

import ch.njol.skript.Skript;
import dk.bondegaard.generator.api.GeneratorAPI;
import dk.bondegaard.generator.api.skript.expressions.*;

public class SkriptAPI {

    private final GeneratorAPI api;

    public SkriptAPI(GeneratorAPI api) {
        this.api = api;
        load();
    }


    private void load() {
        Skript.registerEffect(SetMaxGens.class, "set maxgens for %players% to %number%");
        Skript.registerEffect(AddMaxGens.class, "add maxgens for %players% to %number%");
        Skript.registerEffect(RemoveMaxGens.class, "remove maxgens for %players% to %number%");
        Skript.registerEffect(SellInventory.class, "sellgenerator drops for %players%");
        Skript.registerEffect(SellInventoryWithMultiplier.class, "sellgenerator drops with multi %number% for %players%");
        Skript.registerEffect(GetGeneratorItem.class, "give %players% generator %string%");
        Skript.registerEffect(PickupGens.class, "pickup gens for %players%");
        Skript.registerEffect(RemoveOfflineplayerGen.class, "remove gen for %offline-players% at %location%");
        Skript.registerEffect(RemoveOfflineplayerGensPickup.class, "pickup gen for %offline-players% at %location% to %player%");

    }
}

