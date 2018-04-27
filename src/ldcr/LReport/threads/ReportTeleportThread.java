package ldcr.LReport.threads;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.Report;

public class ReportTeleportThread implements Runnable {
    private final CommandSender sender;
    private final String id;
    public ReportTeleportThread(final CommandSender sender, final String id) {
	this.sender = sender;
	this.id = id;
	Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void run() {
	final Report rpt = Main.instance.manager.getReport("#"+id);
	if (rpt==null) {
	    sender.sendMessage("§b§l举报 §7>> §c举报不存在");
	    return;
	}
	final OfflinePlayer offp = Bukkit.getOfflinePlayer(rpt.getPlayer());
	if (!offp.isOnline()) {
	    sender.sendMessage("§b§l举报 §7>> §c被举报玩家已离线.");
	    return;
	}
	((Player)sender).teleport(offp.getPlayer().getLocation());
	sender.sendMessage("§b§l举报 §7>> §a已传送至被举报玩家 "+rpt.getPlayer());
    }
}
