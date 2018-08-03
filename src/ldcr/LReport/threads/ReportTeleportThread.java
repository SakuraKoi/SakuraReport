package ldcr.LReport.threads;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.LReport.MessageBuilder;
import ldcr.LReport.Report;
import ldcr.Utils.exception.ExceptionUtils;

public class ReportTeleportThread implements Runnable {
	private final Player player;
	private final String id;
	public ReportTeleportThread(final Player sender, final String id) {
		player = sender;
		this.id = id;
		Bukkit.getScheduler().runTaskAsynchronously(LReport.getInstance(), this);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			final Report rpt = LReport.getInstance().getReportManager().getReport("#"+id);
			if (rpt==null) {
				player.sendMessage("§b§l举报 §7>> §c举报不存在");
				return;
			}
			if (rpt.isStaffReport()) {
				if (!player.hasPermission("lreport.staff")) {
					player.sendMessage("§b§l举报 §7>> §c你没有权限处理处罚申请.");
					return;
				} else {
					MessageBuilder.sendPunishSuggest(player, rpt);
					return;
				}
			}
			final String server = LReport.getInstance().getReportManager().getPlayerServer(rpt.getPlayer());
			if(server==null) {
				player.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
				player.closeInventory();
				return;
			}
			LReport.getInstance();
			if (LReport.serverID.equals(server)) {
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
				LReport.getInstance().specListener.spec(player, offp.getPlayer());
				player.sendMessage("§b§l举报 §7>> §a已将您传送到被举报玩家 §6"+rpt.getPlayer()+" §a所在位置.");
				player.closeInventory();
				return;
			} else {
				LReport.getInstance().getMessageChannel().jumpServer(player, server);
				player.sendMessage("§b§l举报 §7>> §a正在将您传送到被举报玩家所在的 §6"+server+" §a服务器.");
				player.closeInventory();
				return;
			}
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			player.sendMessage("§b§l举报 §7>> §c错误: 发生数据库错误");
		}
	}
}
