package sakura.kooi.SakuraReport.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sakura.kooi.SakuraReport.SakuraReport;
import sakura.kooi.SakuraReport.MessageBuilder;
import sakura.kooi.SakuraReport.Report;
import sakura.kooi.SakuraReport.threads.StaffReportThread;
import sakura.kooi.Utils.Bukkit.command.CommandHandler;

public class StaffReportCommand extends CommandHandler {

	public StaffReportCommand() {
		super(SakuraReport.getInstance(), "§b§l举报");
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onCommand(final CommandSender sender, final String[] args) {
		if (checkPermission(sender, "lreport.lpt")) return;
		if (!(sender instanceof Player)) {
			sendMessage(sender, "§c仅玩家可以执行此命令.");
			return;
		}
		if (args.length==0) {
			sendMessage(sender,"&7/staff <玩家> <原因>   &b向管理员提交对玩家的处罚申请");
			return;
		}
		final OfflinePlayer offp = Bukkit.getOfflinePlayer(args[0]);
		if (offp==null) {
			sendMessage(sender, "§c该玩家不存在.");
			return;
		}
		if (sender.hasPermission("lreport.staff")) {
			// do fast staff
			String reason = null;
			if (args.length>1) {
				final StringBuilder builder = new StringBuilder();
				for (int i = 1;i<args.length;i++) {
					builder.append(args[i]);
					builder.append(' ');
				}
				reason = builder.toString();
			} else {
				sendMessage(sender, "§c请提供处罚原因.");
				return;
			}
			MessageBuilder.sendPunishSuggest((Player) sender, new Report("#NULL", offp.getName(), sender.getName(), reason, 0, "NULL", "NULL", offp.getName(), true));
		} else {
			String reason = null;
			if (args.length>1) {
				final StringBuilder builder = new StringBuilder();
				for (int i = 1;i<args.length;i++) {
					builder.append(args[i]);
					builder.append(' ');
				}
				reason = builder.toString();
			} else {
				sendMessage(sender, "§c请提供准确的处罚原因.");
				return;
			}
			new StaffReportThread(sender,offp,reason);
		}
	}

}
