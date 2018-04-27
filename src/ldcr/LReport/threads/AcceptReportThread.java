package ldcr.LReport.threads;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.Report;

public class AcceptReportThread implements Runnable {
    private final CommandSender sender;
    private final String id;
    public AcceptReportThread(final CommandSender sender, final String id) {
	this.sender = sender;
	this.id = id;
	Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this);
    }
    @Override
    public void run() {
	final Report rpt = Main.instance.manager.getReport("#"+id);
	if (rpt==null) {
	    sender.sendMessage("§b§l举报 §7>> §c举报不存在");
	    return;
	}
	if (Main.instance.manager.deleteReport(rpt.getID())) {
	    Main.instance.messageChannel.forwardOKToReporter(rpt.getPlayer(), rpt.getReporter(), (Player) sender);
	    sender.sendMessage("§b§l举报 §7>> §a处理举报成功.");
	} else {
	    sender.sendMessage("§b§l举报 §7>> §a处理举报失败. 数据库错误.");
	}
    }
}
