package dk.bondegaard.generator.api.placeholderapi;

import dk.bondegaard.generator.api.GeneratorAPI;
import dk.bondegaard.generator.api.placeholderapi.expansions.GeneratorExpansion;
import lombok.Getter;

public class PlaceholderAPI {

    @Getter
    private final GeneratorAPI api;

    public PlaceholderAPI(GeneratorAPI api) {
        this.api = api;
        load();
    }

    private void load() {
        new GeneratorExpansion().register();
    }
}
