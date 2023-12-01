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
