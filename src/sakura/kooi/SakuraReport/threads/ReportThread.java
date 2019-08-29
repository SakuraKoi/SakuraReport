package sakura.kooi.SakuraReport.threads;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sakura.kooi.SakuraReport.SakuraReport;
import sakura.kooi.Utils.exception.ExceptionUtils;

public class ReportThread implements Runnable {
	private final CommandSender reporter;
	private final OfflinePlayer player;
	private final String reason;
	public ReportThread(final CommandSender reporter,final OfflinePlayer player,final String reason) {
		this.reporter = reporter;
		this.player = player;
		this.reason = reason;
		Bukkit.getScheduler().runTaskAsynchronously(SakuraReport.getInstance(), this);
	}
	@Override
	public void run() {
		if (reason.length()>72) {
			reporter.sendMessage("§b§l举报 §7>> §c举报原因过长, 请重新填写.");
			return;
		}
		try {
			if (!SakuraReport.getInstance().getReportManager().addReport(player.getName(), reporter.getName(), reason, player.isOnline()? player.getPlayer().getDisplayName() : "[Offline] "+player.getName())) {
				reporter.sendMessage("§b§l举报 §7>> §a您已举报过玩家 "+player.getName()+", 请等待管理员处理...");
				return;
			}
			reporter.sendMessage("§b§l举报 §7>> §a已成功举报该玩家.");
			if (!player.isOnline()) {
				reporter.sendMessage("§b§l举报 §7>> §c注意: 该玩家不在线, 是否打错ID?");
			} else {
				if (isCheatReason(reason) && SakuraReport.getInstance().getAntiCheatHook()!=null) {
					SakuraReport.getInstance().getAntiCheatHook().active(player.getPlayer(),reporter);
				}
			}
			try {
				SakuraReport.getInstance().getMessageChannel().broadcastReport(player.getName(), (Player) reporter, SakuraReport.getDisplayServerName(), reason);
			} catch (final IOException e) {
				ExceptionUtils.printStacktrace(e);
				reporter.sendMessage("§b§l举报 §7>> §e发生数据库错误, 请联系管理员");
			}
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			reporter.sendMessage("§b§l举报 §7>> §c举报失败: 数据库错误, 请联系管理员");
		}
	}
	private static boolean isCheatReason(final String reasons) {
		for (final String reason : reasons.split(" ")) {
			if (cheatReason.contains(reason.toLowerCase())) return true;
		}
		return false;
	}
	private static ArrayList<String> cheatReason = new ArrayList<>(Arrays.asList(
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
			));
}
