package dk.bondegaard.generator.api.skript.expressions;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Objects;

public class RemoveMaxGens extends Effect {

    private Expression<Player> player;

    private Expression<Number> value;

    @Override
    protected void execute(Event e) {
        int intValue = Objects.requireNonNull(value.getSingle(e)).intValue();
        GPlayer gPlayer = PlayerDataHandler.getGPlayer(player.getSingle(e).getPlayer());
        if (gPlayer == null) return;
        int newValue = gPlayer.getMaxGens() - intValue;
        if (newValue < 0) newValue = 0;
        gPlayer.setMaxGens(newValue);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        try {
            this.player = (Expression) expressions[0];
            this.value = (Expression) expressions[1];
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}

