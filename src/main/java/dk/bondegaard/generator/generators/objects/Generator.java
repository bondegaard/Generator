package dk.bondegaard.generator.generators.objects;

import org.bukkit.Location;

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

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    public void setGeneratorType(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    public long getLastDrop() {
        return lastDrop;
    }

    public Generator setLastDrop(long lastDrop) {
        this.lastDrop = lastDrop;
        return this;
    }

    public long getTicksBetweenDrop() {
        return ticksBetweenDrop;
    }

    public Generator setTicksBetweenDrop(long ticksBetweenDrop) {
        this.ticksBetweenDrop = ticksBetweenDrop;
        return this;
    }

    public Location getLocation() {
        return location;
    }
}
