package ldcr.LReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import ldcr.Utils.Bukkit.TitleUtils;

public class MessageChannel implements PluginMessageListener {
	public void onReceiveBroadcastOP(final String cheater,final String reporter,final String reason,final String server) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("lreport.lpt")) {
				player.sendMessage(new String [] {
						"§b§l举报 §7>> §e------------------------------",
						"§b§l举报 §7>> §e玩家 §a"+reporter+" §e在服务器 §d"+server+" §e举报了玩家 §c"+cheater,
						"§b§l举报 §7>> §e举报原因:  §a"+reason,
						"§b§l举报 §7>> §e------------------------------"
				});
			}
		}
	}
	public void onReceiveBroadcastStaff(final String cheater,final String reporter,final String reason,final String server) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("lreport.staff")) {
				player.sendMessage(new String [] {
						"§b§l举报 §7>> §a------------------------------",
						"§b§l举报 §7>> §b志愿者 §a"+reporter+" §b请求处罚玩家 §c"+cheater,
						"§b§l举报 §7>> §b处罚原因:  §a"+reason
				});
			}
		}
	}
	public void onReceiveBroadcastReporter(final String cheater,final String reporter,final String admin) {
		final OfflinePlayer offp = Bukkit.getOfflinePlayer(reporter);
		if (offp==null) return;
		if (offp.isOnline()) {
			TitleUtils.sendTitle(offp.getPlayer(), "", "§a你对玩家 §c"+cheater +" §a的举报已被 §b"+admin+ " §a处理", 10, 20, 10);
			offp.getPlayer().sendMessage("§b§l举报 §7>> §a你对玩家 §c"+cheater +" §a的举报已被 §b"+admin+ " §a处理");
		}
	}
	@Override
	public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
		if (!channel.equals("BungeeCord"))
			return;
		final ByteArrayDataInput in = ByteStreams.newDataInput(message);
		final String subchannel = in.readUTF();
		try {
			if (subchannel.equals("LReportToOP")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > (timestamp+2000)) return;
				final String cheater = msgin.readUTF();
				final String reporter = msgin.readUTF();
				final String reason = msgin.readUTF();
				final String server = msgin.readUTF();
				onReceiveBroadcastOP(cheater, reporter, reason, server);
			} else if (subchannel.equals("LReportToReporter")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > (timestamp+2000)) return;
				final String cheater = msgin.readUTF();
				final String reporter = msgin.readUTF();
				final String admin = msgin.readUTF();
				onReceiveBroadcastReporter(cheater, reporter, admin);
			} else if (subchannel.equals("LReportToStaff")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > (timestamp+2000)) return;
				final String cheater = msgin.readUTF();
				final String reporter = msgin.readUTF();
				final String reason = msgin.readUTF();
				final String server = msgin.readUTF();
				onReceiveBroadcastStaff(cheater, reporter, reason, server);
			}
		} catch (final IOException e) {}
	}
	public void forwardReportToOP(final String player,final Player reporter, final String reason, final String server) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReportToOP");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(player);
			msgout.writeUTF(reporter.getName());
			msgout.writeUTF(reason);
			msgout.writeUTF(server);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			reporter.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
			onReceiveBroadcastOP(player, reporter.getName(), reason, server);
		} catch (final Exception e) {}
	}
	public void forwardStaffReport(final String player,final Player reporter, final String reason, final String server) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReportToStaff");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(player);
			msgout.writeUTF(reporter.getDisplayName());
			msgout.writeUTF(reason);
			msgout.writeUTF(server);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			reporter.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
			onReceiveBroadcastStaff(player, reporter.getDisplayName(), reason, server);
		} catch (final Exception e) {}
	}
	public void forwardOKToReporter(final String player, final String reporter, final Player admin) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReportToReporter");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(player);
			msgout.writeUTF(reporter);
			msgout.writeUTF(admin.getName());

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			admin.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
			onReceiveBroadcastReporter(player, reporter, admin.getName());
		} catch (final Exception e) {}
	}

	public void jumpServer(final Player player,final String serverID) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(serverID);
			player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
		} catch (final Exception e) {}
	}

	public void forwardConsoleReportToOP(final String name, final Player player, final String reason, final String server) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReportToOP");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(name);
			msgout.writeUTF("Console");
			msgout.writeUTF(reason);
			msgout.writeUTF(server);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
			onReceiveBroadcastOP(name, "Console", reason, server);
		} catch (final Exception e) {}
	}
}
