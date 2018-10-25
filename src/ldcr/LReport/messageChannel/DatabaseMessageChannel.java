package ldcr.LReport.messageChannel;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.Utils.database.mysql.MysqlDataSource;
import ldcr.Utils.database.mysql.MysqlMessageChannel;
import ldcr.lib.com.google.gson.JsonElement;
import ldcr.lib.com.google.gson.JsonObject;
import ldcr.lib.com.google.gson.JsonPrimitive;

public class DatabaseMessageChannel extends MysqlMessageChannel implements IMessageChannel {
	private final MessageProcessor processor = new MessageProcessor();
	public DatabaseMessageChannel(final MysqlDataSource mysqlDataSource) {
		super(LReport.getInstance(), mysqlDataSource, "lreport_channel", 100, true);
	}
	@Override
	public void onMessageReceived(final JsonElement data) {
		if (data.isJsonObject()) {
			final JsonObject json = data.getAsJsonObject();
			final JsonElement type = json.get("type");
			if (type.isJsonPrimitive()) {
				switch (type.getAsInt()) {
				case 1: { // report
					processor.onReceiveBroadcastReport(
							json.get("player").getAsString(),
							json.get("reporter").getAsString(),
							json.get("reason").getAsString(),
							json.get("server").getAsString()
							);
					return;
				}
				case 2: { // staff
					processor.onReceiveBroadcastStaff(
							json.get("player").getAsString(),
							json.get("reporter").getAsString(),
							json.get("reason").getAsString(),
							json.get("server").getAsString()
							);
					return;
				}
				case 3: { // process
					processor.onReceiveBroadcastProcessed(
							json.get("player").getAsString(),
							json.get("reporter").getAsString(),
							json.get("processor").getAsString()
							);
					return;
				}
				default:
				}
			}
		}
		LReport.sendConsoleMessage("&e警告: 广播信道收到异常数据");
	}
	@Override
	public void jumpServer(final Player player, final String server) {
		processor.jumpServer(player, server);
	}
	@Override
	public void broadcastReport(final String player, final Player reporter, final String server, final String reason) throws IOException {
		final JsonObject json = new JsonObject();
		json.add("type", new JsonPrimitive(1));
		json.add("player", new JsonPrimitive(player));
		json.add("reporter", new JsonPrimitive(reporter.getName()));
		json.add("server", new JsonPrimitive(server));
		json.add("reason", new JsonPrimitive(reason));
		try {
			sendMessage(json);
		} catch (final SQLException e) {
			throw new IOException(e);
		}
	}
	@Override
	public void broadcastConsoleReport(final String player, final String server, final String reason) throws IOException {
		final JsonObject json = new JsonObject();
		json.add("type", new JsonPrimitive(1));
		json.add("player", new JsonPrimitive(player));
		json.add("reporter", new JsonPrimitive("Console"));
		json.add("server", new JsonPrimitive(server));
		json.add("reason", new JsonPrimitive(reason));
		try {
			sendMessage(json);
		} catch (final SQLException e) {
			throw new IOException(e);
		}
	}
	@Override
	public void broadcastStaff(final String player, final Player reporter, final String server, final String reason) throws IOException {
		final JsonObject json = new JsonObject();
		json.add("type", new JsonPrimitive(2));
		json.add("player", new JsonPrimitive(player));
		json.add("reporter", new JsonPrimitive(reporter.getName()));
		json.add("server", new JsonPrimitive(server));
		json.add("reason", new JsonPrimitive(reason));
		try {
			sendMessage(json);
		} catch (final SQLException e) {
			throw new IOException(e);
		}
	}
	@Override
	public void broadcastProcessed(final String player, final String reporter, final Player processor) throws IOException {
		final JsonObject json = new JsonObject();
		json.add("type", new JsonPrimitive(3));
		json.add("player", new JsonPrimitive(player));
		json.add("reporter", new JsonPrimitive(reporter));
		json.add("processor", new JsonPrimitive(processor.getName()));
		try {
			sendMessage(json);
		} catch (final SQLException e) {
			throw new IOException(e);
		}
	}
	@Override
	public void connectChannel() throws IOException {
		try {
			connect();
		} catch (final SQLException e) {
			throw new IOException(e);
		}
	}
	@Override
	public void disconnectChannel() {
		disconnect();
	}
}
