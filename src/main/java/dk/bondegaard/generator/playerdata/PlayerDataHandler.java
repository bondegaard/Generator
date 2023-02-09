package dk.bondegaard.generator.playerdata;

import dk.bondegaard.generator.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataHandler implements Listener {

    private static final List<GPlayer> players = new ArrayList<>();

    public PlayerDataHandler(Main instance) {
        Bukkit.getPluginManager().registerEvents(this, instance);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (loadPlayer(player)) return;
        }
    }

    public static void saveAll(boolean force) {
        for (GPlayer gPlayer : players) {
            gPlayer.save(force);
        }
    }


    public static List<GPlayer> getPlayers() {
        return players;
    }

    public static GPlayer getGPlayer(Player player) {
        return getGPlayer(player.getUniqueId().toString());
    }

    public static GPlayer getOrCreateGPlayer(OfflinePlayer player) {
        for (GPlayer gPlayer : players) {
            if (gPlayer.getPlayer() == null) continue;
            if (!gPlayer.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString())) continue;
            return gPlayer;
        }
        GPlayer gPlayer = new GPlayer(player);
        players.add(gPlayer);
        return gPlayer;
    }

    public static GPlayer getGPlayer(UUID uuid) {
        return getGPlayer(uuid.toString());
    }


    public static GPlayer getGPlayer(String uuid) {
        for (GPlayer gPlayer : players) {
            if (gPlayer.getPlayer() == null) continue;
            if (!gPlayer.getPlayer().getUniqueId().toString().equals(uuid)) continue;
            return gPlayer;
        }
        return null;
    }

    public static GPlayer getGPlayerByName(Player player) {
        return getGPlayerByName(player.getName());
    }

    public static GPlayer getGPlayerByName(String username) {
        for (GPlayer gPlayer : players) {
            if (gPlayer.getPlayer() == null) continue;
            if (!gPlayer.getPlayer().getName().equals(username)) continue;
            return gPlayer;
        }
        return null;
    }

    public static boolean hasGPlayerByName(String username) {
        return getGPlayerByName(username) != null;
    }

    public static boolean hasGPlayerByName(Player player) {
        return getGPlayerByName(player.getName()) != null;
    }

    public static boolean hasPlayer(String uuid) {
        return getGPlayer(uuid) != null;
    }

    public static boolean hasPlayer(UUID uuid) {
        return getGPlayer(uuid.toString()) != null;
    }

    public static boolean hasPlayer(OfflinePlayer player) {
        return getGPlayer(player.getUniqueId().toString()) != null;
    }

    private static boolean loadPlayer(OfflinePlayer offlinePlayer) {
        if (hasPlayer(offlinePlayer)) return false;
        players.add(new GPlayer(offlinePlayer).load());
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        GPlayer gPlayer = getGPlayer(event.getPlayer());
        if (gPlayer == null) return;
        gPlayer.save(true);
        players.remove(gPlayer);
        Main.getInstance().getGeneratorHandler().removeActiveGenerator(event.getPlayer().getUniqueId().toString());
    }
}
