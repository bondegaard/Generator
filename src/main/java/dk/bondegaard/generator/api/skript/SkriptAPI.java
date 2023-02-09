package dk.bondegaard.generator.api.skript;

import ch.njol.skript.Skript;
import dk.bondegaard.generator.api.GeneratorAPI;
import dk.bondegaard.generator.api.skript.expressions.SetMaxGens;

public class SkriptAPI {

    private final GeneratorAPI api;

    public SkriptAPI(GeneratorAPI api) {
        this.api = api;
        load();
    }


    private void load() {
        Skript.registerEffect(SetMaxGens.class, "set maxgens for %players% to %number%");
    }
}

