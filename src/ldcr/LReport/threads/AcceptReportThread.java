package ldcr.LReport.threads;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.LReport.Report;
import ldcr.Utils.exception.ExceptionUtils;

public class AcceptReportThread implements Runnable {
	private final CommandSender sender;
	private final String id;
	public AcceptReportThread(final CommandSender sender, final String id) {
		this.sender = sender;
		this.id = id;
		Bukkit.getScheduler().runTaskAsynchronously(LReport.getInstance(), this);
	}
	@Override
	public void run() {
		try {
			final Report rpt = LReport.getInstance().getReportManager().getReport("#"+id);
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
			LReport.getInstance().getReportManager().deleteReport(rpt.getID());
			if (!"Console".equals(rpt.getReporter())) {
				try {
					LReport.getInstance().getMessageChannel().broadcastProcessed(rpt.getPlayer(), rpt.getReporter(), (Player) sender);
				} catch (final IOException e) {
					ExceptionUtils.printStacktrace(e);
					sender.sendMessage("§b§l举报 §7>> §e警告: 广播时发生数据库错误");
				}
			}
			sender.sendMessage("§b§l举报 §7>> §a处理举报成功.");
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			sender.sendMessage("§b§l举报 §7>> §c错误: 数据库操作出错, 请检查后台报错.");
		}
	}
}
