package dk.bondegaard.generator.api.skript.expressions.gplayer;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.bondegaard.generator.playerdata.GPlayer;
import dk.bondegaard.generator.playerdata.PlayerDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class LevelReset extends Effect {

    private Expression<Player> player;

    @Override
    protected void execute(Event e) {
        GPlayer gPlayer = PlayerDataHandler.getOrCreateGPlayer(player.getSingle(e).getPlayer());
        gPlayer.setLevel(0);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        try {
            this.player = (Expression) expressions[0];
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
