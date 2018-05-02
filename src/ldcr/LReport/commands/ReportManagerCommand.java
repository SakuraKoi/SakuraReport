package ldcr.LReport.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.threads.AcceptReportThread;
import ldcr.LReport.threads.ListReportThread;
import ldcr.LReport.threads.ReportTeleportThread;

public class ReportManagerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
	if (!sender.hasPermission("lreport.lpt")) {
	    sender.sendMessage("§b§l举报 §7>> §c你没有权限执行此命令.");
	    return true;
	}
	if (!(sender instanceof Player)) {
	    sender.sendMessage("§b§l举报 §7>> §c仅玩家可以执行此命令.");
	    return true;
	}
	if (args.length==0) {
	    Main.instance.guiCreator.openManageGUI((Player) sender);
	    return true;
	}
	switch (args[0].toLowerCase()) {
	case "list": {
	    new ListReportThread(sender);
	    return true;
	}
	case "tp" : {
	    if (!(sender instanceof Player)) {
		sender.sendMessage("§b§l举报 §7>> §c别闹, 后台不能传送.");
		return true;
	    }
	    if (args.length<2) {
		sender.sendMessage("§b§l举报 §7>> §a/lpt tp <举报ID> 传送到被举报玩家");
		return true;
	    }
	    new ReportTeleportThread((Player) sender,args[1]);
	    return true;
	}
	case "ok": {
	    if (args.length<2) {
		sender.sendMessage("§b§l举报 §7>> §a/lpt ok <举报ID> 将举报标记为已处理");
		return true;
	    }
	    new AcceptReportThread(sender,args[1]);
	    return true;
	}
	default: {
	    sender.sendMessage("§b§l举报 §7>> §a/lpt list       列出所有未处理举报");
	    sender.sendMessage("§b§l举报 §7>> §a/lpt tp <举报ID> 传送到被举报玩家");
	    sender.sendMessage("§b§l举报 §7>> §a/lpt ok <举报ID> 将举报标记为已处理");
	    return true;
	}
	}
    }

}
