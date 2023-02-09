package dk.bondegaard.generator.api.skript.expressions;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.bondegaard.generator.features.Pickup;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

public class RemoveOfflineplayerGen extends Effect {

    private Expression<OfflinePlayer> offlinePlayer;

    private Expression<Location> location;

    @Override
    protected void execute(Event e) {
        Pickup.pickupOfflineplayerGen(offlinePlayer.getSingle(e).getPlayer(), location.getSingle(e));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        try {
            this.offlinePlayer = (Expression) expressions[0];
            this.location = (Expression) expressions[1];
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
