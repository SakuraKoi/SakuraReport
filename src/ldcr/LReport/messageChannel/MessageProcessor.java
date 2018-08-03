package ldcr.LReport.messageChannel;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import ldcr.LReport.LReport;
import ldcr.Utils.Bukkit.TitleUtils;

public class MessageProcessor {
	public void jumpServer(final Player player,final String serverID) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(serverID);
			player.sendPluginMessage(LReport.getInstance(), "BungeeCord", out.toByteArray());
		} catch (final Exception e) {}
	}
	public void onReceiveBroadcastReport(final String cheater,final String reporter,final String reason,final String server) {
		final String[] message = new String [] {
				"§b§l 举报 §7>> §e------------------------------",
				"§b§l 举报 §7>> §e玩家 §a"+reporter+" §e在服务器 §d"+server+" §e举报了玩家 §c"+cheater,
				"§b§l 举报 §7>> §e举报原因:  §a"+reason,
				"§b§l 举报 §7>> §e------------------------------"
		};
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("lreport.lpt")) {
				player.sendMessage(message);
			}
		}
	}
	public void onReceiveBroadcastStaff(final String cheater, final String reporter, final String reason, final String server) {
		final String[] message = new String [] {
				"§b§l 举报 §7>> §a------------------------------",
				"§b§l 举报 §7>> §b志愿者 §a"+reporter+" §b请求处罚玩家 §c"+cheater,
				"§b§l 举报 §7>> §b处罚原因:  §a"+reason
		};
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("lreport.staff")) {
				player.sendMessage(message);
			}
		}
	}
	public void onReceiveBroadcastProcessed(final String cheater, final String reporter, final String admin) {
		@SuppressWarnings("deprecation")
		final OfflinePlayer offp = Bukkit.getOfflinePlayer(reporter);
		if (offp==null) return;
		if (offp.isOnline()) {
			TitleUtils.sendTitle(offp.getPlayer(), "", "§a你对玩家 §c"+cheater +" §a的举报已被 §b"+admin+ " §a处理", 10, 20, 10);
			offp.getPlayer().sendMessage("§b§l 举报 §7>> §a你对玩家 §c"+cheater +" §a的举报已被 §b"+admin+ " §a处理");
		}
	}
}
