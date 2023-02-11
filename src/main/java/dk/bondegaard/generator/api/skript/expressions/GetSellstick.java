package dk.bondegaard.generator.api.skript.expressions;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.bondegaard.generator.features.Pickup;
import dk.bondegaard.generator.features.sellstick.SellStickHandler;
import dk.bondegaard.generator.features.shop.ShopHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class GetSellstick extends Effect {

    private Expression<Player> player;

    private Expression<Number> value;

    @Override
    protected void execute(Event e) {
        ItemStack item = SellStickHandler.getInstance().getSellStick(value.getSingle(e).doubleValue());
        if (item == null) return;
        Pickup.giveItem(player.getSingle(e).getPlayer(), item);
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
