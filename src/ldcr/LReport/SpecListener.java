package ldcr.LReport;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SpecListener implements Listener {
    private final HashMap<Player, SpecInfo> specLocation = new HashMap<Player, SpecInfo>();

    @EventHandler
    public void onSneak(final PlayerToggleSneakEvent e) {
	if (e.getPlayer().getGameMode()==GameMode.SPECTATOR) {
	    if (e.isSneaking() == true) {
		if (specLocation.containsKey(e.getPlayer())) {
		    specLocation.get(e.getPlayer()).back();
		    specLocation.remove(e.getPlayer());
		}
	    }
	}
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
	if (e.getPlayer().getGameMode()==GameMode.SPECTATOR) {
	    if (specLocation.containsKey(e.getPlayer())) {
		specLocation.get(e.getPlayer()).back();
		specLocation.remove(e.getPlayer());
	    }
	}
    }

    public void spec(final Player player, final Entity entity) {
	if (!specLocation.containsKey(player)) {
	    specLocation.put(player, new SpecInfo(player));
	}
	player.setGameMode(GameMode.SPECTATOR);
	player.setSpectatorTarget(entity);
    }
    public void backAllSpectator() {
	for (final SpecInfo info : specLocation.values()) {
	    info.back();
	}
	specLocation.clear();
    }
    private class SpecInfo {
	private final Player player;
	private final GameMode mode;
	private final Location loc;
	public SpecInfo(final Player player) {
	    this.player = player;
	    mode = player.getGameMode();
	    loc = player.getLocation();
	}
	public void back() {
	    player.setGameMode(mode);
	    player.teleport(loc);
	}
    }
}