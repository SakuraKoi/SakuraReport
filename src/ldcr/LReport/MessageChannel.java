package ldcr.LReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
				player.sendMessage("§b§l举报 §7>> §e------------------------------");
				player.sendMessage("§b§l举报 §7>> §e玩家 §a"+reporter+" §e在服务器 §d"+server+" §e举报了玩家 §c"+cheater);
				player.sendMessage("§b§l举报 §7>> §e举报原因:  §a"+reason);
				player.sendMessage("§b§l举报 §7>> §e------------------------------");
			}
		}
	}
	public void onReceiveBroadcastReporter(final String cheater,final String reporter,final String admin) {
		final OfflinePlayer offp = Bukkit.getOfflinePlayer(reporter);
		if (offp==null) return;
		if (offp.isOnline()) {
			TitleUtils.sendTitle(offp.getPlayer(), "", "§a你对玩家 §c"+cheater +" §a的举报已被 §b"+admin+ " §a处理", 10, 20, 10);
		}
	}
	public void onReceivePlayerList(final String[] playerList,final String serverID) {
		Main.instance.playersCache.update(serverID, playerList);
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
			} else if (subchannel.equals("LReportPlayerList")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > (timestamp+2000)) return;
				final String serverID = msgin.readUTF();
				final String[] playerList = msgin.readUTF().split("\\|");
				onReceivePlayerList(playerList, serverID);
			} else if (subchannel.equals("LReportListRequest")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > (timestamp+2000)) return;
				final String server = msgin.readUTF();
				forwardOnlineListToServer(server);
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
	private long forwardOnline = 0L;
	public void forwardOnlineList() {
		final Collection<? extends Player> onlines = Bukkit.getOnlinePlayers();
		if (onlines.isEmpty()) return;
		final Player player = onlines.iterator().next();
		final StringBuilder builder = new StringBuilder();
		final ArrayList<String> players = new ArrayList<String>();
		for (final Player p : onlines) {
			builder.append("|");
			builder.append(p.getName());
			players.add(p.getName());
		}
		String list = builder.toString();
		list = list.substring(1, list.length());
		Main.instance.playersCache.update(Main.instance.serverID, players.toArray(new String[]{}));
		if (System.currentTimeMillis() < forwardOnline) return;
		forwardOnline = System.currentTimeMillis()+5000;
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReportPlayerList");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(Main.instance.serverID);
			msgout.writeUTF(list);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	public void forwardOnlineListRequest() {
		final Collection<? extends Player> onlines = Bukkit.getOnlinePlayers();
		if (onlines.isEmpty()) return;
		final Player player = onlines.iterator().next();
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReportListRequest");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(Main.instance.serverID);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
		} catch (final Exception e) {}
	}
	public void forwardOnlineListToServer(final String server) {
		final Collection<? extends Player> onlines = Bukkit.getOnlinePlayers();
		if (onlines.isEmpty()) return;
		final Player player = onlines.iterator().next();
		final StringBuilder builder = new StringBuilder();
		for (final Player p : onlines) {
			builder.append("|");
			builder.append(p.getName());
		}
		String list = builder.toString();
		list = list.substring(1, list.length());
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF(server);
			out.writeUTF("LReportPlayerList");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());
			msgout.writeUTF(Main.instance.serverID);
			msgout.writeUTF(list);
			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	public void jumpServer(final Player player,final String serverID) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(serverID);
			player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
		} catch (final Exception e) {}
	}
	public void getPlayers() {

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
