package ldcr.LReport.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.LReport.Hook.MatrixHook;
import ldcr.LReport.threads.ReportThread;
import ldcr.Utils.Bukkit.command.CommandHandler;

public class ReportCommand extends CommandHandler {

	public ReportCommand() {
		super(LReport.getInstance(), "§b§l举报");
	}

	@Override
	public void onCommand(final CommandSender sender, final String[] args) {
		if (!(sender instanceof Player)) {
			sendMessage(sender, "§c仅玩家可以执行此命令.");
			return;
		}
		if (args.length==0) {
			sendMessage(sender,"&7/report <玩家> <原因>");
			if (LReport.getInstance().matrixHook instanceof MatrixHook) {
				sendMessage(sender,
				            "§a  常见作弊: Killaura Aimbot Hitbox Reach",
				            "§c  如果您举报为以上原因, 被举报玩家将被 §b§lBattlEye反作弊 &c检测."
						);
			}
			return;
		}
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
		new ReportThread(sender,offp,reason);
	}

}
