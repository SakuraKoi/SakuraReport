package ldcr.LReport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import ldcr.Utils.database.mysql.MysqlConnection;

public class ReportManager {
    private MysqlConnection conn = null;
    public void connect(final String mysqlServer, final String mysqlPort, final String mysqlDatabase, final String mysqlUser,
	    final String mysqlPassword) throws SQLException {
	if (conn!=null) {
	    stopConnection();
	}
	Main.sendConsoleMessage("&a正在连接Mysql数据库 "+mysqlServer+":"+mysqlPort+" ...");
	conn = new MysqlConnection(mysqlServer, mysqlUser, mysqlPort, mysqlPassword, mysqlDatabase, 10, Main.instance);
	if (conn.isConnection()) {
	    conn.createTable("LReport", "reportID", "player", "reporter", "reason", "reportTime", "displayServer", "serverID");
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
	return !conn.getValues("LReport", "reportID", 1).isEmpty();
    }

    public boolean addReport(final String player,final String reporter,final String reason,final String displayServer,final String serverID) {
	final HashMap<String, Object> data = conn.getValueLast("LReport", "reporter", reporter, "reportID", "player","serverID");
	if (data!=null) {
	    if (player.equals(data.get("player")))
		if (serverID.equals(data.get("serverID")))
		    return conn.setValue("LReport", "id", data.get("reportID"), "reason", reason);
	}
	return conn.intoValue("LReport", Report.generateID(),player,reporter,reason,String.valueOf(System.currentTimeMillis()),Main.instance.displayServer,Main.instance.serverID);
    }

    public Report getReport(final String id) {
	if (!conn.isExists("LReport", "reportID", id)) return null;
	final HashMap<String, Object> data = conn.getValue("LReport", "reportID", id, "player", "reporter", "reason", "reportTime", "displayServer", "serverID");
	if (data==null) return null;
	try {
	    return new Report(id,data.get("player").toString(),data.get("reporter").toString(),data.get("reason").toString(),Long.valueOf(data.get("reportTime").toString()), data.get("displayServer").toString(), data.get("serverID").toString());
	} catch (final Exception e) {
	    return null;
	}
    }
    public boolean deleteReport(final String id) {
	return conn.deleteValue("LReport", "reportID", id);
    }
    public LinkedList<Report> getAllReports() {
	final LinkedList<Report> reports = new LinkedList<Report>();
	final LinkedList<HashMap<String, Object>> datas = conn.getValues("LReport", -1, "reportID", "player", "reporter", "reason", "reportTime", "displayServer", "serverID");
	for (final HashMap<String, Object> data : datas) {
	    reports.add(new Report(data.get("reportID").toString(),data.get("player").toString(),data.get("reporter").toString(),data.get("reason").toString(),Long.valueOf(data.get("reportTime").toString()), data.get("displayServer").toString(), data.get("serverID").toString()));
	}
	return reports;
    }
}
