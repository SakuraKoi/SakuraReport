package sakura.kooi.SakuraReport.threads;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import sakura.kooi.SakuraReport.SakuraReport;
import sakura.kooi.Utils.exception.ExceptionUtils;

public class ConsoleReportThread implements Runnable {
	private final CommandSender reporter;
	private final OfflinePlayer player;
	private final String reason;
	public ConsoleReportThread(final CommandSender reporter,final OfflinePlayer player,final String reason) {
		this.reporter = reporter;
		this.player = player;
		String temp = reason;
		if (temp.length()>48) {
			temp = temp.substring(0, 48);
			temp = temp+"...[限制48字]";
		}
		this.reason = temp;
		Bukkit.getScheduler().runTaskAsynchronously(SakuraReport.getInstance(), this);
	}
	@Override
	public void run() {
		try {
			SakuraReport.getInstance().getReportManager().addReport(player.getName(), "Console", reason, player.isOnline()? player.getPlayer().getDisplayName() : "[Offline] "+player.getName());
			reporter.sendMessage("§b§l举报 §7>> §a已成功举报该玩家.");
			if (!player.isOnline()) {
				reporter.sendMessage("§b§l举报 §7>> §c注意: 该玩家不在线, 是否打错ID?");
			} else {
				if (isCheatReason(reason) && SakuraReport.getInstance().getAntiCheatHook()!=null) {
					SakuraReport.getInstance().getAntiCheatHook().active(player.getPlayer(),reporter);
				}
			}
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			reporter.sendMessage("§b§l举报 §7>> §c错误: 数据库操作出错, 请检查后台报错.");
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
			"autoclicker",
			"ac",
			"连点",
			"连点器",
			"speed",
			"bhop",
			"timer",
			"加速",
			"fly",
			"飞行",
			"scaffold",
			"自动搭路"
			));
}
