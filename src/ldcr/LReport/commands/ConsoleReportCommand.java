package ldcr.LReport.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.LReport.threads.ConsoleReportThread;
import ldcr.Utils.Bukkit.command.CommandHandler;

public class ConsoleReportCommand extends CommandHandler {

	public ConsoleReportCommand() {
		super(LReport.getInstance(), "§b§l举报");
	}

	@Override
	public void onCommand(final CommandSender sender, final String[] args) {
		if (sender instanceof Player) {
			sendMessage(sender, "§c此命令仅供反作弊调用, 玩家请使用 /report 举报.");
			return;
		}
		if (args.length==0) {
			sendMessage(sender, "&7/report <玩家> <原因>");
			return;
		}
		@SuppressWarnings("deprecation")
		final OfflinePlayer offp = Bukkit.getOfflinePlayer(args[0]);
		if (offp==null) {
			sendMessage(sender, "§c该玩家不存在.");
			return;
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
			sendMessage(sender, "§c请提供举报原因.");
			return;
		}
		new ConsoleReportThread(sender,offp,reason);
	}
}
