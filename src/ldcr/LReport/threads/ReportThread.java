package ldcr.LReport.threads;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.Utils.ExceptionUtils;

public class ReportThread implements Runnable {
	private final CommandSender reporter;
	private final OfflinePlayer player;
	private final String reason;
	public ReportThread(final CommandSender reporter,final OfflinePlayer player,final String reason) {
		this.reporter = reporter;
		this.player = player;
		this.reason = reason;
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this);
	}
	@Override
	public void run() {
		if (reason.length()>72) {
			reporter.sendMessage("§b§l举报 §7>> §c举报原因过长, 请重新填写.");
			return;
		}
		try {
			Main.instance.manager.addReport(player.getName(), reporter.getName(), reason, player.isOnline()? player.getPlayer().getDisplayName() : "[Offline] "+player.getName());
			reporter.sendMessage("§b§l举报 §7>> §a已成功举报该玩家.");
			if (!player.isOnline()) {
				reporter.sendMessage("§b§l举报 §7>> §c注意: 该玩家不在线, 是否打错ID?");
			} else {
				if (isCheatReason(reason)) {
					Main.instance.battlEyeHook.active(player.getPlayer(),reporter);
				}
			}
			Main.boardcastOP();
			Main.instance.messageChannel.forwardReportToOP(player.getName(), (Player) reporter, reason, Main.instance.displayServer);
			return;
		} catch (final SQLException ex) {
			ExceptionUtils.printStacetrace(ex);
			reporter.sendMessage("§b§l举报 §7>> §c举报失败: 数据库错误, 请联系管理员");
		}
	}
	private static boolean isCheatReason(final String reasons) {
		for (final String reason : reasons.split(" ")) {
			if (cheatReason.contains(reason.toLowerCase())) return true;
		}
		return false;
	}
	private static ArrayList<String> cheatReason = new ArrayList<String>(Arrays.asList(new String[] {
			"killaura",
			"ka",
			"杀戮",
			"杀戮光环",
			"aimbot",
			"aimassist",
			"自瞄",
			"自动瞄准",
			"hitbox",
			"碰撞箱",
			"reach",
			"攻击距离",
			"距离"
	}));
}
