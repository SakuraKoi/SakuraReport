package ldcr.LReport.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.LReport.Main;
import ldcr.LReport.Hook.BattlEyeHook;
import ldcr.LReport.threads.ReportThread;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
	if (!(sender instanceof Player)) {
	    sender.sendMessage("§b§l举报 §7>> §c仅玩家可以执行此命令.");
	    return true;
	}
	if (args.length==0) {
	    sender.sendMessage("§b§b§l举报 §7>> /report <玩家> <原因>");
	    sender.sendMessage("§a      常见作弊: Killaura/杀戮光环 Scaffold/自动搭路 AutoClicker/连点器 Speed/BHop/加速 Fly/飞行");
	    if (Main.instance.battlEyeHook instanceof BattlEyeHook) {
		sender.sendMessage("§c      如果你举报原因为以上原因, 被举报玩家将自动被 §3§lBattlEye反作弊 &c检测.");
	    }
	    return true;
	}
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
	new ReportThread(sender,offp,reason);
	return true;
	/*
	if (Main.instance.manager.addReport(offp.getName(), sender.getName(), reason, Main.instance.displayServer, Main.instance.serverID)) {
	    sender.sendMessage("§b§l举报 §7>> §a已成功举报该玩家.");
	    if (!offp.isOnline()) {
		sender.sendMessage("§b§l举报 §7>> §c注意: 该玩家不在线, 是否打错ID?");
	    } else {
		if (isCheatReason(reason)) {
		    Main.instance.battlEyeHook.active(offp.getPlayer(),sender);
		}
	    }
	    Main.boardcastOP();
	    Main.instance.messageChannel.forwardReportToOP(offp.getName(), (Player) sender, reason, Main.instance.displayServer);
	    return true;
	} else {
	    sender.sendMessage("§b§l举报 §7>> §c举报失败. 举报时出错, 请联系管理员.");
	    return true;
	}*/
    }

}
