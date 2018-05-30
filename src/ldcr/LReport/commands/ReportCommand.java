package ldcr.LReport.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.Hook.BattlEyeHook;
import ldcr.LReport.threads.ReportThread;
import ldcr.Utils.Bukkit.command.CommandHandler;

public class ReportCommand extends CommandHandler {

	public ReportCommand() {
		super(Main.instance, "§b§l举报");
	}

	@Override
	public void onCommand(final CommandSender sender, final String[] args) {
		if (!(sender instanceof Player)) {
			sendMessage(sender, "§c仅玩家可以执行此命令.");
			return;
		}
		if (args.length==0) {
			sendMessage(sender,"&7/report <玩家> <原因>");
			if (Main.instance.battlEyeHook instanceof BattlEyeHook) {
				sendMessage(sender,
				            "§a      常见战斗作弊: Killaura/杀戮光环 Aimbot/自瞄 Hitbox/碰撞箱 Reach/攻击距离",
				            "§c      如果你举报原因为以上原因, 被举报玩家将自动被 §3§lBattlEye反作弊 &c检测."
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
