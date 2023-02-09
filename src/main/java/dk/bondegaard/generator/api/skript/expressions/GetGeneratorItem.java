package dk.bondegaard.generator.api.skript.expressions;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.bondegaard.generator.Main;
import dk.bondegaard.generator.generators.objects.GeneratorType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class GetGeneratorItem extends Effect {

    private Expression<Player> player;

    private Expression<String> name;

    @Override
    protected void execute(Event e) {
        GeneratorType generatorType = Main.getInstance().getGeneratorHandler().getGeneratorType(name.getSingle(e).toString());
        if (generatorType == null || generatorType.getGeneratorItem() == null) return;

        player.getSingle(e).getPlayer().getInventory().addItem(generatorType.getGeneratorItem());
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        try {
            this.player = (Expression) expressions[0];
            this.name = (Expression) expressions[1];
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
