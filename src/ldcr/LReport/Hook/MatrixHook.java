package ldcr.LReport.Hook;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ldcr.MatrixAntiKillaura.MatrixAntiKillauraMain;

public class MatrixHook implements IHook {

	@Override
	public void active(final Object... args) {
		MatrixAntiKillauraMain.checkPlayer((Player) args[0]);
		((CommandSender)args[1]).sendMessage("§b§lMatrix §3>> §a开始对玩家 "+((Player) args[0]).getName() + "进行检测...");
	}

}
