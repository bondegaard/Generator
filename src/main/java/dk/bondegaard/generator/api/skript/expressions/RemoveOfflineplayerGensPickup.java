package dk.bondegaard.generator.api.skript.expressions;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.bondegaard.generator.features.Pickup;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class RemoveOfflineplayerGensPickup extends Effect {

    private Expression<Player> player;
    private Expression<OfflinePlayer> offlinePlayer;

    private Expression<Location> location;

    @Override
    protected void execute(Event e) {
        ItemStack gen = Pickup.pickupOfflineplayerGen(offlinePlayer.getSingle(e).getPlayer(), location.getSingle(e));
        if (gen == null) return;
        Pickup.giveItem(player.getSingle(e).getPlayer(), gen);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        try {
            this.player = (Expression) expressions[2];
            this.offlinePlayer = (Expression) expressions[0];
            this.location = (Expression) expressions[1];
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
