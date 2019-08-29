package sakura.kooi.SakuraReport.messageChannel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import sakura.kooi.SakuraReport.SakuraReport;

public class BungeecordMessageChannel implements PluginMessageListener, IMessageChannel {
	private final MessageProcessor processor = new MessageProcessor();
	@Override
	public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
		if (!channel.equals("BungeeCord"))
			return;
		final ByteArrayDataInput in = ByteStreams.newDataInput(message);
		final String subchannel = in.readUTF();
		try {
			if (subchannel.equals("LReport_report")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > timestamp+2000) return;
				final String cheater = msgin.readUTF();
				final String reporter = msgin.readUTF();
				final String reason = msgin.readUTF();
				final String server = msgin.readUTF();
				processor.onReceiveBroadcastReport(cheater, reporter, reason, server);
			} else if (subchannel.equals("LReport_processed")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > timestamp+2000) return;
				final String cheater = msgin.readUTF();
				final String reporter = msgin.readUTF();
				final String admin = msgin.readUTF();
				processor.onReceiveBroadcastProcessed(cheater, reporter, admin);
			} else if (subchannel.equals("LReport_staff")) {
				final short len = in.readShort();
				final byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				final long timestamp = msgin.readLong();
				if (System.currentTimeMillis() > timestamp+2000) return;
				final String cheater = msgin.readUTF();
				final String reporter = msgin.readUTF();
				final String reason = msgin.readUTF();
				final String server = msgin.readUTF();
				processor.onReceiveBroadcastStaff(cheater, reporter, reason, server);
			}
		} catch (final IOException ignored) {}
	}

	@Override
	public void broadcastReport(final String player, final Player reporter, final String server, final String reason) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReport_report");
			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());
			msgout.writeUTF(player);
			msgout.writeUTF(reporter.getName());
			msgout.writeUTF(reason);
			msgout.writeUTF(server);
			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			reporter.sendPluginMessage(SakuraReport.getInstance(), "BungeeCord", out.toByteArray());
			processor.onReceiveBroadcastReport(player, reporter.getName(), reason, server);
		} catch (final Exception ignored) {}
	}
	@Override
	public void broadcastConsoleReport(final String player, final String server, final String reason) {
		Player sender;
		{
			final Iterator<? extends Player> iter = Bukkit.getOnlinePlayers().iterator();
			if (!iter.hasNext()) return;
			sender = iter.next();
		}
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReport_report");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());
			msgout.writeUTF(player);
			msgout.writeUTF("Console");
			msgout.writeUTF(reason);
			msgout.writeUTF(server);
			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			sender.sendPluginMessage(SakuraReport.getInstance(), "BungeeCord", out.toByteArray());
			processor.onReceiveBroadcastReport(player, "Console", reason, server);
		} catch (final Exception ignored) {}
	}
	@Override
	public void broadcastStaff(final String player, final Player reporter, final String server, final String reason) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReport_staff");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(player);
			msgout.writeUTF(reporter.getDisplayName());
			msgout.writeUTF(reason);
			msgout.writeUTF(server);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			reporter.sendPluginMessage(SakuraReport.getInstance(), "BungeeCord", out.toByteArray());
			processor.onReceiveBroadcastStaff(player, reporter.getDisplayName(), reason, server);
		} catch (final Exception ignored) {}
	}
	@Override
	public void broadcastProcessed(final String player, final String reporter, final Player executor) {
		try {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("LReport_processed");

			final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			final DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeLong(System.currentTimeMillis());

			msgout.writeUTF(player);
			msgout.writeUTF(reporter);
			msgout.writeUTF(executor.getName());

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			executor.sendPluginMessage(SakuraReport.getInstance(), "BungeeCord", out.toByteArray());
			processor.onReceiveBroadcastProcessed(player, reporter, executor.getName());
		} catch (final Exception ignored) {}
	}

	@Override
	public void connectChannel() {
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(SakuraReport.getInstance(), "BungeeCord", this);
	}

	@Override
	public void disconnectChannel() {
		Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(SakuraReport.getInstance(), "BungeeCord", this);
	}

	@Override
	public void jumpServer(final Player player, final String server) {
		processor.jumpServer(player, server);
	}
}
