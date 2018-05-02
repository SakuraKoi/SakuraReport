package ldcr.LReport.threads;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.Report;

public class ReportTeleportThread implements Runnable {
    private final Player player;
    private final String id;
    public ReportTeleportThread(final Player sender, final String id) {
	player = sender;
	this.id = id;
	Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void run() {
	final Report rpt = Main.instance.manager.getReport("#"+id);
	if (rpt==null) {
	    player.sendMessage("§b§l举报 §7>> §c举报不存在");
	    return;
	}
	final String server = Main.instance.playersCache.getServer(rpt.getPlayer());
	if(server==null) {
	    player.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
	    player.closeInventory();
	    return;
	}
	if (Main.instance.serverID.equals(server)) {
	    final OfflinePlayer offp = Bukkit.getOfflinePlayer(rpt.getPlayer());
	    if (offp==null) {
		player.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
		player.closeInventory();
		return;
	    }
	    if (!offp.isOnline()) {
		player.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
		player.closeInventory();
		return;
	    }
	    Main.instance.specListener.spec(player, offp.getPlayer());
	    player.sendMessage("§b§l举报 §7>> §a已将您传送到被举报玩家 §6"+rpt.getPlayer()+" §a所在位置.");
	    player.closeInventory();
	    return;
	} else {
	    Main.instance.messageChannel.jumpServer(player, server);
	    player.sendMessage("§b§l举报 §7>> §a正在将您传送到被举报玩家所在的 §6"+server+" §a服务器.");
	    player.closeInventory();
	    return;
	}

	/*
	final OfflinePlayer offp = Bukkit.getOfflinePlayer(rpt.getPlayer());
	if (!offp.isOnline()) {
	    sender.sendMessage("§b§l举报 §7>> §c被举报玩家已离线.");
	    return;
	}
	Main.instance.specListener.spec(clicker, offp.getPlayer());
	sender.sendMessage("§b§l举报 §7>> §a已传送至被举报玩家 "+rpt.getPlayer());
	 */
    }
}
