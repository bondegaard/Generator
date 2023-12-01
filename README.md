<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
    <h3 align="center">Generator</h3>
    <a href="https://github.com/bondegaard/Generator">Github</a>
    <a href="https://www.spigotmc.org/resources/generator.113773/">Spigot</a>
</div>

## Info

Generator is a plugin made to make it easier for server owners to make their own 'generator servers' which is a very specific type of minecraft server, mostly seen on a server with the name of SuperAwesome.

This plugin is not meant to be used alone and another spigot plugin or Skript-Lang has to be used to support this project. This is because this project has a lot of small features that is meant to be run by the server owners own code.

#### How to include the API with Maven:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.github.bondegaard</groupId>
        <artifactId>Generator</artifactId>
        <version>1.1.13</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

#### How to include the API with Gradle:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    compileOnly 'com.github.bondegaard:Generator:1.1.13'
}
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br />

## License

```txt
Copyright (c) 2023 bondegaard

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br />

## Building

To build Geneator, you need JDK 8 or higher installed on your system.

Clone this repository, then run the following command:

- On Linux or macOS: `./gradlew build`
- On Windows: `gradlew build`

<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br />

## Implementing Generator

See example here of how to use the Generator plugins API

<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br />

### Using a plugin:

```java
package com.example.plugin;

import java.util.logging.Logger;

import dk.bondegaard.generator.api.GeneratorAPI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private static GeneratorAPI generatorAPI = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        if (!setupGenerator() ) {
            getLogger().log(Level.SEVERE, "Disabled due to no Generator dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupGenerator() {
        if (getServer().getPluginManager().getPlugin("Generator") == null) {
            return false;
        }
        generatorAPI = GeneratorAPI.getInstance();
    }

    public static GeneratorAPI getGeneratorAPI() {
        return generatorAPI;
    }

}
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br />

### Using a Skript-Lang:

```skript-lang
# Change players exp amount
command /test1 <number>:
    trigger:
        add arg-1 generator exp to player
        remove arg-1 generator exp from player
        reset generator exp for player
        set player generator exp to arg-1

# Change players level amount
command /test2 <number>:
    trigger:
        add arg-1 generator level to player
        remove arg-1 generator level from player
        reset generator level for player
        set player generator level to arg-1

# Change players prestige amount
command /test3 <number>:
    trigger:
        add arg-1 generator prestige to player
        remove arg-1 generator prestige from player
        reset generator prestige for player
        set player generator prestige to arg-1

# Change players multiplier amount
command /test4 <number>:
    trigger:
        add arg-1 multiplier to player
        remove arg-1 multiplier from player
        reset multiplier for player
        set player multiplier to arg-1

# Change players max gen amount
command /test5 <number>:
    trigger:
        set maxgens for player to arg-1
        add maxgens for player to arg-1
        remove maxgens for player to arg-1

# Sell Generators
command /test6 <number>:
    trigger:
        sell generator drops for player
        sellgenerator drops with multi arg-1 for player

# Get a generator
command /test7 <string>:
    trigger:
        give player generator arg-1

# Pickup a players gens
command /test8 <player>:
    trigger:
        pickup gens for arg-1

# Open the shop GUI
command /test9:
    trigger:
        open shop for player

# Get a sell stick defined in the config
command /test10 <number>:
    trigger:
        give player sellstick whit multi arg-1

# Other things you can do:
# - pickup gen for %offlineplayer% at %location% to %player%
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br />
