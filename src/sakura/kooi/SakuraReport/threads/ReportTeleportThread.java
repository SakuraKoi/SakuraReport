package sakura.kooi.SakuraReport.threads;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import sakura.kooi.SakuraReport.SakuraReport;
import sakura.kooi.SakuraReport.MessageBuilder;
import sakura.kooi.SakuraReport.Report;
import sakura.kooi.Utils.exception.ExceptionUtils;

public class ReportTeleportThread implements Runnable {
	private final Player player;
	private final String id;
	public ReportTeleportThread(final Player sender, final String id) {
		player = sender;
		this.id = id;
		Bukkit.getScheduler().runTaskAsynchronously(SakuraReport.getInstance(), this);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			final Report rpt = SakuraReport.getInstance().getReportManager().getReport("#"+id);
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
			final String server = SakuraReport.getInstance().getReportManager().getPlayerServer(rpt.getPlayer());
			if(server==null) {
				player.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
				player.closeInventory();
				return;
			}
			SakuraReport.getInstance();
			if (SakuraReport.getServerID().equals(server)) {
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
				SakuraReport.getInstance().getSpecListener().spec(player, offp.getPlayer());
				player.sendMessage("§b§l举报 §7>> §a已将您传送到被举报玩家 §6"+rpt.getPlayer()+" §a所在位置.");
				player.closeInventory();
			} else {
				SakuraReport.getInstance().getMessageChannel().jumpServer(player, server);
				player.sendMessage("§b§l举报 §7>> §a正在将您传送到被举报玩家所在的 §6"+server+" §a服务器.");
				player.closeInventory();
			}
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			player.sendMessage("§b§l举报 §7>> §c错误: 发生数据库错误");
		}
	}
}
