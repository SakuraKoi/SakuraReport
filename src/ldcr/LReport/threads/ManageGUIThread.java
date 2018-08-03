package ldcr.LReport.threads;

import java.sql.SQLException;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import ldcr.LReport.LReport;
import ldcr.LReport.ManageGUICreator;
import ldcr.LReport.Report;
import ldcr.Utils.exception.ExceptionUtils;

public class ManageGUIThread implements Runnable {
	private final ManageGUICreator creator;
	private final Player player;
	private int page;
	public ManageGUIThread(final ManageGUICreator creator, final Player player, final int page) {
		this.creator = creator;
		this.player = player;
		this.page = page;
		Bukkit.getScheduler().runTaskAsynchronously(LReport.getInstance(), this);
	}
	@Override
	public void run() {
		try {
			LReport.getInstance().getReportManager().updatePlayerCache();
			final LinkedList<Report> reports = LReport.getInstance().getReportManager().getAllReports();
			boolean update = true;
			if (reports.size()<=54) {
				page=1;
			}
			Inventory mainGUI = Bukkit.createInventory(null, 54, "§b§l举报 §7>> §6§l举报箱 §a第 "+ page +" 页");
			if (player.getOpenInventory() != null) {
				if (player.getOpenInventory().getTopInventory() != null) {
					if (player.getOpenInventory().getTopInventory().getName()!=null) {
						if (player.getOpenInventory().getTopInventory().getName().startsWith("§b§l举报 §7>> §6§l举报箱")) {
							final Inventory current = player.getOpenInventory().getTopInventory();
							if (creator.getPage(current)==page) {
								current.clear();
								mainGUI = current;
								update = false;
							}
						}
					}
				}
			}
			if (reports.size() > 54) {
				final int start = 45*(page-1);
				final int size = reports.size()-1;
				final boolean nextPage = !((start+45)>size);
				for (int i = start,max = start+45;i<max;i++) {
					if (i>size) {
						break;
					}
					mainGUI.addItem(creator.getReportItem(reports.get(i)));
				}

				if (page>1) {
					mainGUI.setItem(46, creator.getPrePageItem(page));
				}
				mainGUI.setItem(49, creator.getPageItem(page));
				if (nextPage) {
					mainGUI.setItem(52, creator.getNextPageItem(page));
				}
			} else {
				for (final Report rpt : reports) {
					mainGUI.addItem(creator.getReportItem(rpt));
				}
			}
			if (update) {
				player.closeInventory();
				player.openInventory(mainGUI);
			}
			LReport.getInstance().getReportManager().clearCache();
		} catch (final SQLException ex) {
			ExceptionUtils.printStacktrace(ex);
			player.sendMessage("§b§l举报 §7>> §c错误: 数据库操作出错, 请检查后台报错.");
		}
	}
}
