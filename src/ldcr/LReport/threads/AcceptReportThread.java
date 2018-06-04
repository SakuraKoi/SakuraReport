package ldcr.LReport.threads;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.Report;
import ldcr.Utils.ExceptionUtils;

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
		try {
			final Report rpt = Main.instance.manager.getReport("#"+id);
			if (rpt==null) {
				sender.sendMessage("§b§l举报 §7>> §c举报不存在");
				return;
			}
			if (rpt.isStaffReport()) {
				if (!sender.hasPermission("lreport.staff")) {
					sender.sendMessage("§b§l举报 §7>> §c你没有权限处理处罚申请.");
					return;
				}
			}
			Main.instance.manager.deleteReport(rpt.getID());
			Main.instance.messageChannel.forwardOKToReporter(rpt.getPlayer(), rpt.getReporter(), (Player) sender);
			sender.sendMessage("§b§l举报 §7>> §a处理举报成功.");
		} catch (final SQLException ex) {
			ExceptionUtils.printStacetrace(ex);
			sender.sendMessage("§b§l举报 §7>> §c错误: 数据库操作出错, 请检查后台报错.");
		}
	}
}
