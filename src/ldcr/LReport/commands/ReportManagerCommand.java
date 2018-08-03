package ldcr.LReport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.LReport;
import ldcr.LReport.threads.AcceptReportThread;
import ldcr.LReport.threads.ListReportThread;
import ldcr.LReport.threads.ReportTeleportThread;
import ldcr.Utils.Bukkit.command.CommandHandler;

public class ReportManagerCommand extends CommandHandler {

	public ReportManagerCommand() {
		super(LReport.getInstance(), "§b§l举报");
	}

	@Override
	public void onCommand(final CommandSender sender, final String[] args) {
		if (checkPermission(sender, "lreport.lpt")) return;
		if (!(sender instanceof Player)) {
			sendMessage(sender, "§c仅玩家可以执行此命令.");
			return;
		}
		if (args.length==0) {
			LReport.getInstance().guiCreator.openManageGUI((Player) sender);
			return;
		}
		switch (args[0].toLowerCase()) {
		case "list": {
			new ListReportThread(sender);
			return;
		}
		case "tp" : {
			if (args.length<2) {
				sendMessage(sender, "§a/lpt tp <举报ID> 传送到被举报玩家");
				return;
			}
			new ReportTeleportThread((Player) sender,args[1]);
			return;
		}
		case "ok": {
			if (args.length<2) {
				sendMessage(sender, "§a/lpt ok <举报ID> 将举报标记为已处理");
				return;
			}
			new AcceptReportThread(sender,args[1]);
			return;
		}
		default: {
			sendMessage(sender,
			            "§a/lpt list       列出所有未处理举报",
			            "§a/lpt tp <举报ID> 传送到被举报玩家",
					"§a/lpt ok <举报ID> 将举报标记为已处理");
			return;
		}
		}
	}

}
