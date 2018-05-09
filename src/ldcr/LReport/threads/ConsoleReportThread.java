package ldcr.LReport.threads;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import ldcr.LReport.Main;

public class ConsoleReportThread implements Runnable {
	private final CommandSender reporter;
	private final OfflinePlayer player;
	private final String reason;
	public ConsoleReportThread(final CommandSender reporter,final OfflinePlayer player,final String reason) {
		this.reporter = reporter;
		this.player = player;
		String temp = reason;
		if (temp.length()>24) {
			temp = temp.substring(0, 24);
			temp = temp+"...[限制24字]";
		}
		this.reason = temp;
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this);
	}
	@Override
	public void run() {
		if (Main.instance.manager.addReport(player.getName(), "Console", reason, Main.instance.displayServer, Main.instance.serverID, player.isOnline()? player.getPlayer().getDisplayName() : "[Offline] "+player.getName())) {
			reporter.sendMessage("§b§l举报 §7>> §a已成功举报该玩家.");
			if (!player.isOnline()) {
				reporter.sendMessage("§b§l举报 §7>> §c注意: 该玩家不在线, 是否打错ID?");
			} else {
				if (isCheatReason(reason)) {
					Main.instance.battlEyeHook.active(player.getPlayer(),reporter);
				}
			}
			return;
		} else {
			reporter.sendMessage("§b§l举报 §7>> §c举报失败. 举报时出错, 请联系管理员.");
			return;
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
	}));
}
