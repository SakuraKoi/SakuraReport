package ldcr.LReport;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import ldcr.Utils.ExceptionUtils;

public class ManageGUIListener implements Listener {
	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		if (e.getInventory().getName()==null) return;
		if (!e.getInventory().getName().startsWith("§b§l举报 §7>> ")) return;
		e.setCancelled(true);
		if (e.getInventory().getName().startsWith("§b§l举报 §7>> §6§l举报箱")) {
			final Player clicker = (Player) e.getView().getPlayer();
			if (e.getCurrentItem()==null) return;
			if (e.getCurrentItem().getType()==Material.AIR) return;
			if (e.getCurrentItem().getType()==Material.WOOL) { // 举报
				final String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
				if (itemName==null) return;
				final String id = itemName.substring(7, itemName.length());
				Report rpt;
				try {
					rpt = Main.instance.manager.getReport(id);
				} catch (final SQLException ex) {
					ExceptionUtils.printStacetrace(ex);
					clicker.sendMessage("§b§l举报 §7>> §c错误: 发生数据库错误, 请检查后台报错.");
					clicker.closeInventory();
					return;
				}
				if (rpt==null) {
					clicker.sendMessage("§b§l举报 §7>> §c举报 §6"+id+" §c不存在, 是否已被其他人处理?");
					clicker.closeInventory();
					return;
				}
				if (e.getAction()==InventoryAction.PICKUP_ALL) { // 左键
					if (rpt.isStaffReport()) {
						if(clicker.hasPermission("lreport.staff")) {
							MessageBuilder.sendPunishSuggest(clicker, rpt);
						} else {
							clicker.sendMessage("§b§l举报 §7>> §c你没有权限处理处罚申请.");
						}
						clicker.closeInventory();
						return;
					} else {
						String server;
						try {
							server = Main.instance.manager.getPlayerServer(rpt.getPlayer());
						} catch (final SQLException e1) {
							clicker.sendMessage("§b§l举报 §7>> §c错误: 发生数据库错误, 请检查后台报错.");
							clicker.closeInventory();
							ExceptionUtils.printStacetrace(e1);
							return;
						}
						if(server==null) {
							clicker.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
							clicker.closeInventory();
							return;
						}
						if (Main.instance.serverID.equals(server)) {
							final OfflinePlayer offp = Bukkit.getOfflinePlayer(rpt.getPlayer());
							if (offp==null) {
								clicker.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
								clicker.closeInventory();
								return;
							}
							if (!offp.isOnline()) {
								clicker.sendMessage("§b§l举报 §7>> §c被举报玩家 §6"+rpt.getPlayer()+" §c不在线.");
								clicker.closeInventory();
								return;
							}
							Main.instance.specListener.spec(clicker, offp.getPlayer());
							clicker.sendMessage("§b§l举报 §7>> §a已将您传送到被举报玩家 §6"+rpt.getPlayer()+" §a所在位置.");
							clicker.closeInventory();
							return;
						} else {
							Main.instance.messageChannel.jumpServer(clicker, server);
							clicker.sendMessage("§b§l举报 §7>> §a正在将您传送到被举报玩家所在的 §6"+server+" §a服务器.");
							clicker.closeInventory();
							return;
						}
					}
				} else if (e.getAction() == InventoryAction.PICKUP_HALF) { // 右键
					if (rpt.isStaffReport()) {
						if(!clicker.hasPermission("lreport.staff")) {
							clicker.sendMessage("§b§l举报 §7>> §c你没有权限处理处罚申请.");
							clicker.closeInventory();
							return;
						}
					}
					try {
						Main.instance.manager.deleteReport(id);
						Main.instance.messageChannel.forwardOKToReporter(rpt.getPlayer(), rpt.getReporter(), clicker);
						Main.instance.guiCreator.openManageGUI(clicker, Main.instance.guiCreator.getPage(e.getInventory()));
						clicker.sendMessage("§b§l举报 §7>> §a举报 §6"+id+" §a已处理.");
					} catch (final SQLException ex) {
						ExceptionUtils.printStacetrace(ex);
						clicker.sendMessage("§b§l举报 §7>> §c错误: 发生数据库错误, 请检查后台报错.");
						clicker.closeInventory();
						return;
					}
				}
			} else if (e.getCurrentItem().getType()==Material.GLOWSTONE) { // next or pre page
				final int page = e.getInventory().getItem(49).getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
				if (e.getSlot()==46) { // pre
					Main.instance.guiCreator.openManageGUI(clicker, page-1);
				} else if (e.getSlot()==52) { //next
					Main.instance.guiCreator.openManageGUI(clicker, page+1);
				}
			}

		}
	}
}
