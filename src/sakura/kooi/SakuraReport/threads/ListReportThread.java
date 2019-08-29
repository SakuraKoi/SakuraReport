package sakura.kooi.SakuraReport.threads;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import sakura.kooi.SakuraReport.SakuraReport;
import sakura.kooi.SakuraReport.Report;
import sakura.kooi.Utils.exception.ExceptionUtils;

public class ListReportThread implements Runnable {
	private final CommandSender sender;
	public ListReportThread(final CommandSender sender) {
		this.sender = sender;
		Bukkit.getScheduler().runTaskAsynchronously(SakuraReport.getInstance(), this);
	}
	@Override
	public void run() {
		try {
			for (final Report rpt : SakuraReport.getInstance().getReportManager().getAllReports()) {
				sender.sendMessage("§a- §bID "+rpt.getID()+"§a ------- 于服务器 §e"+rpt.getDisplayServer());
				sender.sendMessage("§a 举报者: "+rpt.getReporter()+" 于 "+formatTime(rpt.getTime()));
				sender.sendMessage("§c 被举报玩家 "+rpt.getPlayer()+" 原因 "+rpt.getReason());
			}
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			sender.sendMessage("§b§l举报 §7>> §c错误: 数据库操作出错, 请检查后台报错.");
		}
	}
	private static String formatTime(final long time)
	{
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(time);
	}
}
