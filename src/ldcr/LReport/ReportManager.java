package ldcr.LReport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ldcr.Utils.database.MysqlDataSource;

public class ReportManager {
	private MysqlDataSource conn = null;
	private final String REPORT_TABLE_NAME = "lreport_reports";
	private final String PLAYER_TABLE_NAME = "lreport_player";
	public void connect(final String mysqlServer, final String mysqlPort, final String mysqlDatabase, final String mysqlUser,
			final String mysqlPassword) throws SQLException {
		if (conn!=null) {
			stopConnection();
		}
		Main.sendConsoleMessage("&a正在连接Mysql数据库 "+mysqlServer+":"+mysqlPort+" ...");
		conn = new MysqlDataSource(mysqlServer, mysqlUser, mysqlPort, mysqlPassword, mysqlDatabase, Main.instance);
		try {
			conn.connectDatabase();
		} catch (final SQLException e) {
			throw new SQLException("Failed connect Database", e);
		}
		if (conn.isConnected()) {
			try {
				conn.createTable(REPORT_TABLE_NAME, "reportID", "player", "reporter", "reason", "reportTime", "displayServer", "serverID","displayPlayerName");
				conn.createTable(PLAYER_TABLE_NAME, "player", "server","indexc");
			} catch (final SQLException e) {
				throw new SQLException("Failed create Table", e);
			}
		} else throw new SQLException("Failed connect Database");
	}

	public void stopConnection() {
		if (conn!=null) {
			if (conn.isConnected()) {
				Main.sendConsoleMessage("&e正在关闭数据库连接...");
				conn.disconnectDatabase();
				Main.sendConsoleMessage("&a举报系统已与数据库断线.");
			}
		}
	}

	public boolean hasReport() throws SQLException {
		return !conn.getValues(REPORT_TABLE_NAME, "reportID", 1).isEmpty();
	}

	public void addReport(final String player,final String reporter,final String reason,final String displayServer,final String serverID, final String displayPlayerName) throws SQLException {
		final HashMap<String, Object> data = conn.getValueLast(REPORT_TABLE_NAME, "reporter", reporter, "reportID", "player","serverID","displayPlayerName");
		if (data!=null) {
			if (player.equals(data.get("player")))
				if (serverID.equals(data.get("serverID"))) {
					conn.setValue(REPORT_TABLE_NAME, "id", data.get("reportID"), "reason", reason);
				}
		}
		conn.intoValue(REPORT_TABLE_NAME, Report.generateID(),player,reporter,reason,String.valueOf(System.currentTimeMillis()),Main.instance.displayServer,Main.instance.serverID,displayPlayerName);
	}

	public Report getReport(final String id) throws SQLException {
		if (!conn.isExists(REPORT_TABLE_NAME, "reportID", id)) return null;
		final HashMap<String, Object> data = conn.getValue(REPORT_TABLE_NAME, "reportID", id, "player", "reporter", "reason", "reportTime", "displayServer", "serverID", "displayPlayerName");
		if (data==null) return null;
		try {
			return new Report(id,
			                  data.get("player").toString(),
			                  data.get("reporter").toString(),
			                  data.get("reason").toString(),
			                  Long.valueOf(data.get("reportTime").toString()),
			                  data.get("displayServer").toString(),
			                  data.get("serverID").toString(),
			                  data.get("displayPlayerName").toString());
		} catch (final Exception e) {
			return null;
		}
	}
	public void deleteReport(final String id) throws SQLException {
		conn.deleteValue(REPORT_TABLE_NAME, "reportID", id);
	}
	public LinkedList<Report> getAllReports() throws SQLException {
		final LinkedList<Report> reports = new LinkedList<Report>();
		final LinkedList<HashMap<String, Object>> datas = conn.getValues(REPORT_TABLE_NAME, -1, "reportID", "player", "reporter", "reason", "reportTime", "displayServer", "serverID", "displayPlayerName");
		for (final HashMap<String, Object> data : datas) {
			reports.add(new Report(data.get("reportID").toString(),data.get("player").toString(),data.get("reporter").toString(),data.get("reason").toString(),Long.valueOf(data.get("reportTime").toString()), data.get("displayServer").toString(), data.get("serverID").toString(), data.get("displayPlayerName").toString()));
		}
		return reports;
	}
	public void playerOnline(String player) throws SQLException {
		player = player.toLowerCase();
		conn.intoValue(PLAYER_TABLE_NAME, player, Main.instance.serverID, Main.instance.serverID+"|"+player);
	}
	public void playerOffline(final String player) throws SQLException {
		conn.deleteValue(PLAYER_TABLE_NAME, "indexc", Main.instance.serverID+"|"+player.toLowerCase());
	}
	public String getPlayerServer(final String player) throws SQLException {
		final Object result = conn.getValue(PLAYER_TABLE_NAME, "player", player.toLowerCase(), "server");
		if (result == null) return null;
		return result.toString();
	}

	public void updatePlayerlist() throws SQLException {
		conn.deleteValue(PLAYER_TABLE_NAME, "server", Main.instance.serverID);
		for (final Player player : Bukkit.getOnlinePlayers()) {
			playerOnline(player.getName());
		}
	}
}
