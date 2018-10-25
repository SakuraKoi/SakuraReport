package ldcr.LReport.threads;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.Utils.exception.ExceptionUtils;

public class StaffReportThread implements Runnable {
	private final CommandSender reporter;
	private final OfflinePlayer player;
	private final String reason;
	public StaffReportThread(final CommandSender reporter,final OfflinePlayer player,final String reason) {
		this.reporter = reporter;
		this.player = player;
		this.reason = reason;
		Bukkit.getScheduler().runTaskAsynchronously(LReport.getInstance(), this);
	}
	@Override
	public void run() {
		try {
			LReport.getInstance().getReportManager().addStaffReport(player.getName(), reporter.getName(), reason, player.isOnline()? player.getPlayer().getDisplayName() : player.getName());
			reporter.sendMessage("§b§l举报 §7>> §a已提交对玩家 "+player.getName()+" 的处罚申请.");
			if (!player.isOnline()) {
				reporter.sendMessage("§b§l举报 §7>> §c注意: 该玩家不在线, 是否打错ID?");
			}
			try {
				LReport.getInstance().getMessageChannel().broadcastStaff(player.isOnline()? player.getPlayer().getDisplayName() : player.getName(), (Player) reporter, LReport.getDisplayServerName(), reason);
			} catch (final IOException e) {
				ExceptionUtils.printStacktrace(e);
				reporter.sendMessage("§b§l举报 §7>> §e警告: 广播时发生数据库错误");
			}
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			reporter.sendMessage("§b§l举报 §7>> §c举报失败: 数据库错误, 请联系管理员");
		}
	}
}
