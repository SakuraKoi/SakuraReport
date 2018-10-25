package ldcr.LReport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ldcr.LReport.AnticheatSupport.IAnticheckHook;
import ldcr.LReport.commands.ConsoleReportCommand;
import ldcr.LReport.commands.ReportCommand;
import ldcr.LReport.commands.ReportManagerCommand;
import ldcr.LReport.commands.StaffReportCommand;
import ldcr.LReport.messageChannel.DatabaseMessageChannel;
import ldcr.LReport.messageChannel.IMessageChannel;
import ldcr.LdcrUtils.plugin.LdcrUtils;
import ldcr.Utils.exception.ExceptionUtils;
import lombok.Getter;

public class LReport extends JavaPlugin implements Listener {
	@Getter private static LReport instance;

	@Getter private ReportManager reportManager;
	@Getter private IMessageChannel messageChannel;
	private static CommandSender logger;
	private String mysqlServer;
	private String mysqlPort;
	private String mysqlDatabase;
	private String mysqlUser;
	private String mysqlPassword;
	@Getter private static String displayServerName;
	@Getter private static String serverID;
	@Getter private ManageGUICreator guiCreator;
	@Getter private IAnticheckHook antiCheatHook = null;
	@Getter private SpecListener specListener;
	@Override
	public void onEnable() {
		instance = this;
		LdcrUtils.requireVersion(this, 33);
		logger = Bukkit.getConsoleSender();
		loadConfig();
		specListener = new SpecListener();
		reportManager = new ReportManager();
		try {
			reportManager.connect(mysqlServer,mysqlPort,mysqlDatabase,mysqlUser,mysqlPassword);
		} catch (final SQLException e) {
			ExceptionUtils.printStacktrace(e);
			LReport.sendConsoleMessage("&c数据库连接失败, 请检查配置文件.");
			setEnabled(false);
			return;
		}
		messageChannel = new DatabaseMessageChannel(reportManager.getDataSource());
		try {
			messageChannel.connectChannel();
		} catch (final IOException e) {
			ExceptionUtils.printStacktrace(e);
			LReport.sendConsoleMessage("&c广播信道连接失败");
		}
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		guiCreator = new ManageGUICreator();
		Bukkit.getPluginManager().registerEvents(new ManageGUIListener(), this);
		Bukkit.getPluginManager().registerEvents(specListener, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("report").setExecutor(new ReportCommand());
		getCommand("staff").setExecutor(new StaffReportCommand());
		getCommand("crpt").setExecutor(new ConsoleReportCommand());
		getCommand("lpt").setExecutor(new ReportManagerCommand());
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new PlayersUpdateTask(), 20, 36000);
		sendConsoleMessage("&a举报系统已加载完毕.");
	}
	@Override
	public void onDisable() {
		if (reportManager != null) {
			reportManager.stopConnection();
		}
		if (messageChannel != null) {
			messageChannel.disconnectChannel();
		}
		if (specListener != null) {
			specListener.backAllSpectator();
		}
	}
	public void hookAnticheat(final Plugin plugin, final IAnticheckHook hook) {
		antiCheatHook = hook;
		sendConsoleMessage("&a成功与反作弊插件 "+plugin.getName()+" 挂钩.");
	}
	public void loadConfig() {
		final File configFile = new File(getDataFolder(),"config.yml");
		if (!configFile.exists()) {
			saveDefaultConfig();
		}
		final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		mysqlServer = config.getString("mysql.server","localhost");
		mysqlPort = config.getString("mysql.port","3306");
		mysqlDatabase = config.getString("mysql.database","lreport");
		mysqlUser = config.getString("mysql.user","root");
		mysqlPassword = config.getString("mysql.password","password");

		displayServerName = config.getString("DisplayServerName","Undefined");
		serverID = config.getString("BungeeServerID","undefined");
		sendConsoleMessage("&a此子服被命名为 "+displayServerName+" ["+serverID+"]");
	}

	public static void sendConsoleMessage(final String... messages) {
		for (final String str : messages) {
			logger.sendMessage("§a§lLReport §7>> §e"+str.replace('&', '§').replace("§§", "&"));
		}
	}

	@EventHandler
	public void onJoin(final PlayerLoginEvent e) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			try {
				LReport.instance.reportManager.playerOnline(e.getPlayer().getName());
			} catch (final SQLException e1) {
				ExceptionUtils.printStacktrace(e1);
				sendConsoleMessage("&c错误: 发生数据库错误, 请检查后台报错.");
			}
		}, 10);
	}
	@EventHandler
	public void onLogout(final PlayerQuitEvent e) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			try {
				LReport.instance.reportManager.playerOffline(e.getPlayer().getName());
			} catch (final SQLException e1) {
				ExceptionUtils.printStacktrace(e1);
				sendConsoleMessage("&c错误: 发生数据库错误, 请检查后台报错.");
			}
		}, 10);
	}

	class PlayersUpdateTask implements Runnable {
		@Override
		public void run() {
			try {
				LReport.instance.reportManager.updatePlayerlist();
			} catch (final SQLException e) {
				ExceptionUtils.printStacktrace(e);
				LReport.sendConsoleMessage("&c更新玩家列表时发生了数据库错误.");
			}
		}

	}
}
