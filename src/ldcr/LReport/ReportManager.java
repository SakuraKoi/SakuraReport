package ldcr.LReport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ldcr.Utils.database.mysql.MysqlDataSource;
import ldcr.Utils.database.mysql.Processor.HasResultProcessor;
import lombok.Getter;

public class ReportManager {
	@Getter protected MysqlDataSource dataSource = null;
	private static final String REPORT_TABLE_NAME = "lreport_report";
	private static final String PLAYER_TABLE_NAME = "lreport_player";
	public void connect(final String mysqlServer, final String mysqlPort, final String mysqlDatabase, final String mysqlUser,
			final String mysqlPassword) throws SQLException {
		if (dataSource!=null) {
			stopConnection();
		}
		LReport.sendConsoleMessage("&a正在连接Mysql数据库 "+mysqlServer+":"+mysqlPort+" ...");
		dataSource = new MysqlDataSource(mysqlServer, mysqlPort, mysqlUser, mysqlPassword, mysqlDatabase, LReport.getInstance());
		try {
			dataSource.connectDatabase();
		} catch (final SQLException e) {
			throw new SQLException("Failed connect Database", e);
		}
		if (dataSource.isConnected()) {
			try {
				dataSource.update("CREATE TABLE IF NOT EXISTS "+REPORT_TABLE_NAME+" (id int(1) not null primary key auto_increment,  reportID text, player text, reporter text, reason text, reportTime long, getDisplayServerName() text, getServerID() text, displayPlayerName text, isStaff boolean)", null);
				dataSource.update("CREATE TABLE IF NOT EXISTS "+PLAYER_TABLE_NAME+" (id int(1) not null primary key auto_increment,  player text, server text)", null);
			} catch (final SQLException e) {
				throw new SQLException("Failed create Table", e);
			}
		} else throw new SQLException("Failed connect Database");
	}

	public void stopConnection() {
		if (dataSource!=null && dataSource.isConnected()) {
			LReport.sendConsoleMessage("&e正在关闭数据库连接...");
			dataSource.disconnectDatabase();
			LReport.sendConsoleMessage("&a举报系统已与数据库断线.");
		}
	}

	public boolean hasReport() throws SQLException {
		return dataSource.query("SELECT `reportID` FROM `"+REPORT_TABLE_NAME+"` LIMIT 1", null, new HasResultProcessor());
		//return !dataSource.getValues(REPORT_TABLE_NAME, "reportID", 1).isEmpty();
	}

	public boolean addReport(final String player,final String reporter,final String reason, final String displayPlayerName) throws SQLException {
		if (dataSource.query("SELECT `reportID` FROM `"+REPORT_TABLE_NAME+"` WHERE `player` = ? AND `reporter` = ? LIMIT 1", query -> {
			query.setString(1, player);
			query.setString(2, reporter);
		}, new HasResultProcessor())) return false;
		//if (dataSource.isExists(REPORT_TABLE_NAME, "reportIndex", reporter+":"+player)) return false;
		dataSource.update("INSERT INTO `"+REPORT_TABLE_NAME+"` VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?);", query -> {
			query.setString(1, Report.generateID());
			query.setString(2, player);
			query.setString(3, reporter);
			query.setString(4, reason);
			query.setLong(5, System.currentTimeMillis());
			LReport.getInstance();
			query.setString(6, LReport.getDisplayServerName());
			LReport.getInstance();
			query.setString(7, LReport.getServerID());
			query.setString(8, displayPlayerName);
			query.setBoolean(9, false);
		});
		//dataSource.intoValue(REPORT_TABLE_NAME, Report.generateID(),player,reporter,reason,String.valueOf(System.currentTimeMillis()),LReport.instance.getDisplayServerName(),LReport.instance.getServerID(),displayPlayerName, "false", reporter+":"+player);
		return true;
	}

	public void addStaffReport(final String player,final String reporter,final String reason, final String displayPlayerName) throws SQLException {
		dataSource.update("INSERT INTO `"+REPORT_TABLE_NAME+"` VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?);", query -> {
			query.setString(1, Report.generateID());
			query.setString(2, player);
			query.setString(3, reporter);
			query.setString(4, reason);
			query.setLong(5, System.currentTimeMillis());
			LReport.getInstance();
			query.setString(6, LReport.getDisplayServerName());
			query.setString(7, "#STAFF");
			query.setString(8, displayPlayerName);
			query.setBoolean(9, true);
		});
		//dataSource.intoValue(REPORT_TABLE_NAME, Report.generateID(),player,reporter,reason,String.valueOf(System.currentTimeMillis()),LReport.instance.getDisplayServerName(),"#STAFF",displayPlayerName, "true", reporter+":"+player);
	}

	public Report getReport(final String id) throws SQLException {
		if (!dataSource.isExists(REPORT_TABLE_NAME, "reportID", id)) return null;
		return dataSource.query("SELECT * FROM `"+REPORT_TABLE_NAME+"` WHERE `reportID` = ? LIMIT 1", query -> query.setString(1, id), result -> {
			while (result.next())
				return new Report(
						id,
						result.getString("player"),
						result.getString("reporter"),
						result.getString("reason"),
						result.getLong("reportTime"),
						result.getString("getDisplayServerName()"),
						result.getString("getServerID()"),
						result.getString("displayPlayerName"),
						result.getBoolean("isStaff")
						);
			return null;
		});
	}
	public void deleteReport(final String id) throws SQLException {
		dataSource.update("DELETE FROM `"+REPORT_TABLE_NAME+"` WHERE `reportID` = ?", query -> query.setString(1, id));
		//dataSource.deleteValue(REPORT_TABLE_NAME, "reportID", id);
	}
	public List<Report> getAllReports() throws SQLException {
		return dataSource.query("SELECT * FROM `"+REPORT_TABLE_NAME+"`", null, result -> {
			final LinkedList<Report> results = new LinkedList<>();
			while (result.next()) {
				results.add(new Report(
						result.getString("reportID"),
						result.getString("player"),
						result.getString("reporter"),
						result.getString("reason"),
						result.getLong("reportTime"),
						result.getString("getDisplayServerName()"),
						result.getString("getServerID()"),
						result.getString("displayPlayerName"),
						result.getBoolean("isStaff")
						));
			}
			return results;
		});
	}
	public void playerOnline(final String player) throws SQLException {
		dataSource.update("INSERT INTO `"+PLAYER_TABLE_NAME+"` values(null, ?, ?)", query -> {
			query.setString(1, player.toLowerCase());
			LReport.getInstance();
			query.setString(2, LReport.getServerID());
		});
		//dataSource.intoValue(PLAYER_TABLE_NAME, player, LReport.instance.getServerID(), LReport.instance.getServerID()+"|"+player);
	}
	public void playerOffline(final String player) throws SQLException {
		dataSource.update("DELETE FROM `"+PLAYER_TABLE_NAME+"` WHERE ( `player` = ? AND `server` = ? )", query -> {
			query.setString(1, player.toLowerCase());
			LReport.getInstance();
			query.setString(2, LReport.getServerID());
		});
		//dataSource.deleteValue(PLAYER_TABLE_NAME, "indexc", LReport.instance.getServerID()+"|"+player.toLowerCase());
	}
	public String getPlayerServer(final String player) throws SQLException {
		return dataSource.query("SELECT * FROM `"+PLAYER_TABLE_NAME+"` WHERE `player` = ?", query -> query.setString(1, player.toLowerCase()), result -> {
			while (result.next())
				return result.getString("server");
			return null;
		});
	}

	public void updatePlayerlist() throws SQLException {
		dataSource.update("DELETE FROM `"+PLAYER_TABLE_NAME+"` WHERE `server` = ?", query -> {
			LReport.getInstance();
			query.setString(1, LReport.getServerID());
		});
		//dataSource.deleteValue(PLAYER_TABLE_NAME, "server", LReport.instance.getServerID());
		dataSource.updateBatch("INSERT INTO `"+PLAYER_TABLE_NAME+"` values(null, ?, ?)", query -> {
			for (final Player player : Bukkit.getOnlinePlayers()) {
				query.setString(1, player.getName().toLowerCase());
				LReport.getInstance();
				query.setString(2, LReport.getServerID());
				query.addBatch();
			}
		});
		//for (final Player player : Bukkit.getOnlinePlayers()) {
		//playerOnline(player.getName());
	}

	public String getCachedPlayerServer(final String player) {
		return serverCache.get(player);
	}
	private final HashMap<String, String> serverCache = new HashMap<>();
	public void updatePlayerCache() throws SQLException {
		dataSource.query("SELECT * FROM `"+PLAYER_TABLE_NAME+"`", null, result -> {
			while(result.next()) {
				serverCache.put(result.getString("player"), result.getString("server"));
			}
			return null;
		});
	}

	public void clearCache() {
		serverCache.clear();
	}
}
