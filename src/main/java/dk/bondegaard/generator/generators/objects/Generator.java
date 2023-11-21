package dk.bondegaard.generator.generators.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter @Setter
public class Generator {
    private final String ownerUUID;

    private final Location location;

    private GeneratorType generatorType;

    private long lastDrop = System.currentTimeMillis();

    private long ticksBetweenDrop;


    public Generator(String ownerUUID, Location location, GeneratorType generatorType, long ticksBetweenDrop) {
        this.ownerUUID = ownerUUID;
        this.location = location;
        this.generatorType = generatorType;
        this.ticksBetweenDrop = ticksBetweenDrop;
    }

    public Generator setLastDrop(long lastDrop) {
        this.lastDrop = lastDrop;
        return this;
    }

    public Generator setTicksBetweenDrop(long ticksBetweenDrop) {
        this.ticksBetweenDrop = ticksBetweenDrop;
        return this;
    }
}
