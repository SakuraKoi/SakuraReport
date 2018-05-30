package ldcr.LReport;

import java.util.Random;

public class Report {
	private final String id;
	private final String player;
	private final String reporter;
	private final String reason;
	private final long time;
	private final String displayServer;
	private final String serverID;
	private final String displayPlayerName;
	public Report(final String id, final String player, final String reporter, final String reason, final long time,final String displayServer,final String serverID, final String displayPlayerName) {
		this.id = id;
		this.player = player;
		this.reporter = reporter;
		this.reason = reason;
		this.time = time;
		this.displayServer = displayServer;
		this.serverID = serverID;
		this.displayPlayerName = displayPlayerName;
	}
	public String getID() {
		return id;
	}
	public String getPlayer() {
		return player;
	}
	public String getReporter() {
		return reporter;
	}
	public String getReason() {
		return reason;
	}
	public long getTime() {
		return time;
	}
	public String getDisplayServer() {
		return displayServer;
	}
	public String getServerID() {
		return serverID;
	}
	private static char[] keyword = "0123456789abcdef".toCharArray();
	public static String generateID() {
		final StringBuilder builder = new StringBuilder("#");
		final Random ran = new Random();
		for (int i = 0 ;i<8;i++) {
			builder.append(keyword[ran.nextInt(keyword.length)]);
		}
		return builder.toString();
	}
	public String getDisplayPlayerName() {
		return displayPlayerName;
	}
}
