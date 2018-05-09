package ldcr.LReport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import ldcr.Utils.database.mysql.MysqlConnection;

public class ReportManager {
	private MysqlConnection conn = null;
	private final String TABLE_NAME = "LReportNew";
	public void connect(final String mysqlServer, final String mysqlPort, final String mysqlDatabase, final String mysqlUser,
			final String mysqlPassword) throws SQLException {
		if (conn!=null) {
			stopConnection();
		}
		Main.sendConsoleMessage("&a正在连接Mysql数据库 "+mysqlServer+":"+mysqlPort+" ...");
		conn = new MysqlConnection(mysqlServer, mysqlUser, mysqlPort, mysqlPassword, mysqlDatabase, 10, Main.instance);
		if (conn.isConnection()) {
			conn.createTable(TABLE_NAME, "reportID", "player", "reporter", "reason", "reportTime", "displayServer", "serverID","displayPlayerName");
		} else throw new SQLException("Failed connect Database");
	}

	public void stopConnection() {
		if (conn!=null) {
			if (conn.isConnection()) {
				Main.sendConsoleMessage("&e正在关闭数据库连接...");
				conn.closeConnection();
				Main.sendConsoleMessage("&a举报系统已与数据库断线.");
			}
		}
	}

	public boolean hasReport() {
		return !conn.getValues(TABLE_NAME, "reportID", 1).isEmpty();
	}

	public boolean addReport(final String player,final String reporter,final String reason,final String displayServer,final String serverID, final String displayPlayerName) {
		final HashMap<String, Object> data = conn.getValueLast(TABLE_NAME, "reporter", reporter, "reportID", "player","serverID","displayPlayerName");
		if (data!=null) {
			if (player.equals(data.get("player")))
				if (serverID.equals(data.get("serverID")))
					return conn.setValue(TABLE_NAME, "id", data.get("reportID"), "reason", reason);
		}
		return conn.intoValue(TABLE_NAME, Report.generateID(),player,reporter,reason,String.valueOf(System.currentTimeMillis()),Main.instance.displayServer,Main.instance.serverID,displayPlayerName);
	}

	public Report getReport(final String id) {
		if (!conn.isExists(TABLE_NAME, "reportID", id)) return null;
		final HashMap<String, Object> data = conn.getValue(TABLE_NAME, "reportID", id, "player", "reporter", "reason", "reportTime", "displayServer", "serverID", "displayPlayerName");
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
	public boolean deleteReport(final String id) {
		return conn.deleteValue(TABLE_NAME, "reportID", id);
	}
	public LinkedList<Report> getAllReports() {
		final LinkedList<Report> reports = new LinkedList<Report>();
		final LinkedList<HashMap<String, Object>> datas = conn.getValues(TABLE_NAME, -1, "reportID", "player", "reporter", "reason", "reportTime", "displayServer", "serverID", "displayPlayerName");
		for (final HashMap<String, Object> data : datas) {
			reports.add(new Report(data.get("reportID").toString(),data.get("player").toString(),data.get("reporter").toString(),data.get("reason").toString(),Long.valueOf(data.get("reportTime").toString()), data.get("displayServer").toString(), data.get("serverID").toString(), data.get("displayPlayerName").toString()));
		}
		return reports;
	}
}
