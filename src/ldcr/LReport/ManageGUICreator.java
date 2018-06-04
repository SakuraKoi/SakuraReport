package ldcr.LReport;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import ldcr.LReport.threads.ManageGUIThread;
import ldcr.Utils.ExceptionUtils;

public class ManageGUICreator {
	public void openManageGUI(final Player player) {
		openManageGUI(player,1);
	}
	public void openManageGUI(final Player player, final int page) {
		new ManageGUIThread(this, player,page);
	}
	public ItemStack getPrePageItem(final int page) {
		final ItemStack item = new ItemStack(Material.GLOWSTONE,1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§b§l<< 上一页 "+(page-1));
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getNextPageItem(final int page) {
		final ItemStack item = new ItemStack(Material.GLOWSTONE,1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§b§l"+(page+1)+" 下一页 >>");
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getPageItem(final int page) {
		final ItemStack item = new ItemStack(Material.PAPER,1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§b§l第 "+page+" 页");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, page, true);
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getReportItem(final Report rpt) {
		if (rpt.isStaffReport()) {
			final ItemStack item = new Wool(DyeColor.LIGHT_BLUE).toItemStack(1);
			final ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§6§l举报 "+rpt.getID());
			final LinkedList<String> lore = new LinkedList<String>();
			lore.add("");
			lore.add("§b待处理玩家: §c"+rpt.getDisplayPlayerName());
			lore.add("§b提交志愿者: §e"+rpt.getReporter());
			lore.add("§b原因: §e"+rpt.getReason());
			lore.add("§b所在服务器: §e"+rpt.getDisplayServer());
			lore.add("§b举报时间: §e"+formatTime(rpt.getTime()));
			lore.add("");
			lore.add("§b这是一条志愿者提交的处理申请");
			lore.add("");
			lore.add("§a左键处罚 §b§l| §d右键清除");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		} else {
			String server;
			boolean error = false;
			try {
				server = Main.instance.manager.getPlayerServer(rpt.getPlayer());
			} catch (final SQLException e) {
				server=null;
				error = true;
				ExceptionUtils.printStacetrace(e);
			}
			boolean isOnReportServer = false;
			final boolean isOnline = server!=null;
			if (isOnline) {
				isOnReportServer = rpt.getServerID().equals(server);
			}
			final ItemStack item = new Wool(error ? DyeColor.RED : isOnline ? (isOnReportServer ? DyeColor.LIME : DyeColor.YELLOW) : DyeColor.SILVER).toItemStack(1);
			final ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§6§l举报 "+rpt.getID());
			final LinkedList<String> lore = new LinkedList<String>();
			lore.add("");
			lore.add("§b被举报玩家: §c"+rpt.getDisplayPlayerName());
			lore.add("§b举报者: §e"+rpt.getReporter());
			lore.add("§b原因: §e"+rpt.getReason());
			lore.add("§b所在服务器: §e"+rpt.getDisplayServer());
			lore.add("§b举报时间: §e"+formatTime(rpt.getTime()));
			lore.add("");
			lore.add(error ? "§c数据库错误" : isOnReportServer ? "§a被举报玩家当前在线" : (isOnline ? "§e被举报玩家不在当时的服务器" : "§7被举报玩家当前不在线"));
			lore.add("");
			lore.add("§a左键传送 §b§l| §d右键处理");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
	}
	public int getPage(final Inventory inv) {
		if ((inv.getItem(49)!=null) && (inv.getItem(49).getType()==Material.PAPER))
			return inv.getItem(49).getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
		else return 1;
	}
	private String formatTime(final long time)
	{
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(time);
	}
}
