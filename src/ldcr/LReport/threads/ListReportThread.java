package ldcr.LReport.threads;

import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import ldcr.LReport.Main;
import ldcr.LReport.Report;

public class ListReportThread implements Runnable {
    private final CommandSender sender;
    public ListReportThread(final CommandSender sender) {
	this.sender = sender;
	Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this);
    }
    @Override
    public void run() {
	for (final Report rpt : Main.instance.manager.getAllReports()) {
	    sender.sendMessage("§a- §bID "+rpt.getID()+"§a ------- 于服务器 §e"+rpt.getDisplayServer());
	    sender.sendMessage("§a 举报者: "+rpt.getReporter()+" 于 "+formatTime(rpt.getTime()));
	    sender.sendMessage("§c 被举报玩家 "+rpt.getPlayer()+" 原因 "+rpt.getReason());
	}
    }
    private static String formatTime(final long time)
    {
	final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return format.format(time);
    }
}
