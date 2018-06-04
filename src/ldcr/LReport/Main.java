package ldcr.LReport;

import java.io.File;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ldcr.LReport.Hook.BattlEyeHook;
import ldcr.LReport.Hook.EmptyHook;
import ldcr.LReport.Hook.IHook;
import ldcr.LReport.commands.ConsoleReportCommand;
import ldcr.LReport.commands.ReportCommand;
import ldcr.LReport.commands.ReportManagerCommand;
import ldcr.LReport.commands.StaffReportCommand;
import ldcr.Utils.ExceptionUtils;
import ldcr.Utils.Bukkit.TitleUtils;

public class Main extends JavaPlugin implements Listener {
	public static Main instance;
	private static CommandSender logger;
	private String mysqlServer;
	private String mysqlPort;
	private String mysqlDatabase;
	private String mysqlUser;
	private String mysqlPassword;
	public String displayServer;
	public String serverID;
	public ReportManager manager;
	public MessageChannel messageChannel;
	public ManageGUICreator guiCreator;
	public IHook battlEyeHook;
	public SpecListener specListener;
	@Override
	public void onEnable() {
		instance = this;
		logger = Bukkit.getConsoleSender();
		loadConfig();
		specListener = new SpecListener();
		manager = new ReportManager();
		try {
			manager.connect(mysqlServer,mysqlPort,mysqlDatabase,mysqlUser,mysqlPassword);
		} catch (final SQLException e) {
			ExceptionUtils.printStacetrace(e);
			Main.sendConsoleMessage("&c数据库连接失败, 请检查配置文件.");
			setEnabled(false);
			return;
		}
		messageChannel = new MessageChannel();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", messageChannel);
		hookBattlEye();
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
		manager.stopConnection();
		specListener.backAllSpectator();
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

		displayServer = config.getString("DisplayServerName","Undefined");
		serverID = config.getString("BungeeServerID","undefined");
		sendConsoleMessage("&a此子服被命名为 "+displayServer+" ["+serverID+"]");
	}
	private void hookBattlEye() {
		if (Bukkit.getPluginManager().isPluginEnabled("BattlEye")) {
			sendConsoleMessage("&bBattlEye 反作弊系统已挂钩");
			battlEyeHook = new BattlEyeHook();
		} else {
			battlEyeHook = new EmptyHook();
		}
	}
	public static void boardcastOP() {
		try {
			if (!Main.instance.manager.hasReport()) return;
		} catch (final SQLException ex) {
			ExceptionUtils.printStacetrace(ex);
			sendConsoleMessage("&c错误: 数据库操作出错, 请检查后台报错.");
			return;
		}
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("lreport.lpt")) {
				TitleUtils.sendTitle(p, "", "§c你有未处理举报", 1, 20, 1);
			}
		}
	}
	public static void sendConsoleMessage(final String... messages) {
		for (final String str : messages) {
			logger.sendMessage("§a§lLReport §7>> §e"+str.replace('&', '§').replace("§§", "&"));
		}
	}

	@EventHandler
	public void onJoin(final PlayerLoginEvent e) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				try {
					Main.instance.manager.playerOnline(e.getPlayer().getName());
				} catch (final SQLException e) {
					ExceptionUtils.printStacetrace(e);
					sendConsoleMessage("&c错误: 发生数据库错误, 请检查后台报错.");
				}
			}

		}, 10);

		if (e.getPlayer().hasPermission("lreport.lpt")) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
				@Override
				public void run() {
					try {
						if (manager.hasReport()) {
							TitleUtils.sendTitle(e.getPlayer(), "", "§c你有未处理举报", 1, 20, 1);
						}
					}catch (final SQLException ex) {
						ExceptionUtils.printStacetrace(ex);
						sendConsoleMessage("&c错误: 发生数据库错误, 请检查后台报错.");
						return;
					}
				}
			}
			, 100);
		}
	}
	@EventHandler
	public void onLogout(final PlayerQuitEvent e) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				try {
					Main.instance.manager.playerOffline(e.getPlayer().getName());
				} catch (final SQLException e) {
					ExceptionUtils.printStacetrace(e);
					sendConsoleMessage("&c错误: 发生数据库错误, 请检查后台报错.");
				}
			}
		}, 10);
	}
	class PlayersUpdateTask implements Runnable {
		@Override
		public void run() {
			try {
				Main.instance.manager.updatePlayerlist();
			} catch (final SQLException e) {
				ExceptionUtils.printStacetrace(e);
				Main.sendConsoleMessage("&c更新玩家列表时发生了数据库错误.");
			}
		}

	}
}
