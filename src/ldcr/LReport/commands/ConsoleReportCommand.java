package ldcr.LReport.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.threads.ConsoleReportThread;

public class ConsoleReportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage("§b§l举报 §7>> §c此命令仅供反作弊调用, 玩家请使用 /report 举报.");
			return true;
		}
		if (args.length==0) {
			sender.sendMessage("§b§b§l举报 §7>> /report <玩家> <原因>");
			return true;
		}
		@SuppressWarnings("deprecation")
		final OfflinePlayer offp = Bukkit.getOfflinePlayer(args[0]);
		if (offp==null) {
			sender.sendMessage("§b§l举报 §7>> §c该玩家不存在.");
			return true;
		}
		String reason = null;
		if (args.length>1) {
			final StringBuilder builder = new StringBuilder();
			for (int i = 1;i<args.length;i++) {
				builder.append(args[i]);
				builder.append(' ');
			}
			reason = builder.toString();
		} else {
			sender.sendMessage("§b§l举报 §7>> §c请提供举报原因.");
			return true;
		}
		new ConsoleReportThread(sender,offp,reason);
		return true;
	}
}
