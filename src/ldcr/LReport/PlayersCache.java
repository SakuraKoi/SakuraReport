package ldcr.LReport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class PlayersCache {
    private final HashMap<String,ServerCache> cache = new HashMap<String,ServerCache>();
    public void update(final String server,final String[] players) {
	if (cache.containsKey(server)) {
	    cache.get(server).update(players);;
	} else {
	    cache.put(server, new ServerCache(players));
	}
    }
    public boolean isOnline(final String server,final String player) {
	if (!cache.containsKey(server)) return false;
	return cache.get(server).hasPlayer(player);
    }
    public boolean isOnline(final String player) {
	for (final Entry<String,ServerCache> list : cache.entrySet()) {
	    if (list.getValue().hasPlayer(player)) return true;
	}
	return false;
    }
    public String getServer(final String player) {
	for (final Entry<String,ServerCache> list : cache.entrySet()) {
	    if (list.getValue().hasPlayer(player)) return list.getKey();
	}
	return null;
    }
    class ServerCache {
	private final ArrayList<String> players;
	private long expire;
	public ServerCache(final String[] player) {
	    players = new ArrayList<String>(Arrays.asList(player));
	    expire = System.currentTimeMillis() + 70000;
	}
	public void update(final String[] player) {
	    players.clear();
	    players.addAll(Arrays.asList(player));
	    expire = System.currentTimeMillis() + 70000;
	}
	public boolean hasPlayer(final String player) {
	    if (System.currentTimeMillis() > expire) {
		players.clear();
		return false;
	    }
	    return players.contains(player);
	}
    }
}
