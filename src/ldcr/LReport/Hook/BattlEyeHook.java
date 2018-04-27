package ldcr.LReport.Hook;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.BattlEye.BattlEyeMain;

public class BattlEyeHook implements IHook {

    @Override
    public void active(final Object... args) {
	BattlEyeMain.checkPlayer((Player) args[0]);
	((CommandSender)args[1]).sendMessage("§b§lBattlEye §3>> §bBattlEye反作弊系统 开始检查玩家 "+((Player) args[0]).getName());
    }

}
