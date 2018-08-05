package ldcr.LReport;

import org.bukkit.entity.Player;

import ldcr.Utils.Bukkit.TextComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageBuilder {
	public static void sendPunishSuggest(final Player player, final Report report) {
		final String reason = report.getReason();
		final TextComponent prefix = new TextComponent("§c§l处罚 §7>> §b§l-----------------------------");
		final TextComponent staff_info;
		final TextComponent staff_reason = new TextComponent("§c§l处罚 §7>> §b处罚原因: §c"+reason);
		final TextComponent template_kick;
		final TextComponent template_mute;
		final TextComponent template_ban;
		final TextComponent template_ipban;
		{
			final TextComponent info = new TextComponent(TextComponent.fromLegacyText("§c§l处罚 §7>> §b志愿者 §a"+report.getReporter()+" §b对玩家 §c"+report.getDisplayPlayerName()+" §b的处罚申请  "));
			final TextComponent viewHistory = new TextComponent(TextComponent.fromLegacyText("§6『§l查看处罚记录§6』"));
			viewHistory.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a点击查看处罚记录")));
			viewHistory.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history "+report.getPlayer()));
			staff_info =  new TextComponent(info, viewHistory);
		}
		{
			final TextComponent kick = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 踢出 "));
			final TextComponent button_1 = new TextComponent(TextComponent.fromLegacyText(" §e踢出该玩家"));
			button_1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c踢出游戏")));
			button_1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kick "+report.getPlayer()+" "+reason));
			template_kick = new TextComponent(kick, button_1);
		}
		{
			final TextComponent mute = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 禁言 "));
			final TextComponent button_1 = new TextComponent(TextComponent.fromLegacyText(" §e一分钟"));
			button_1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 一分钟")));
			button_1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 1min "+reason));
			final TextComponent button_2 = new TextComponent(TextComponent.fromLegacyText(" §e一小时"));
			button_2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 一小时")));
			button_2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 1h "+reason));
			final TextComponent button_3 = new TextComponent(TextComponent.fromLegacyText(" §e一天"));
			button_3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 一天")));
			button_3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 1day "+reason));
			final TextComponent button_4 = new TextComponent(TextComponent.fromLegacyText(" §e七天"));
			button_4.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 七天")));
			button_4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 7day "+reason));
			template_mute = new TextComponent(mute, button_1, button_2, button_3, button_4);
		}
		{
			final TextComponent ban = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 封禁 "));
			final TextComponent button_1 = new TextComponent(TextComponent.fromLegacyText(" §e一天"));
			button_1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁 一天")));
			button_1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempban "+report.getPlayer()+" 1day "+reason));
			final TextComponent button_2 = new TextComponent(TextComponent.fromLegacyText(" §e七天"));
			button_2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁 七天")));
			button_2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempban "+report.getPlayer()+" 7day "+reason));
			final TextComponent button_3 = new TextComponent(TextComponent.fromLegacyText(" §e三十天"));
			button_3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁 三十天")));
			button_3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempban "+report.getPlayer()+" 30day "+reason));
			final TextComponent button_4 = new TextComponent(TextComponent.fromLegacyText(" §e永久"));
			button_4.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c永久封禁")));
			button_4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban "+report.getPlayer()+" "+reason));
			template_ban = new TextComponent(ban, button_1, button_2, button_3, button_4);
		}
		{
			final TextComponent ipban = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 封禁 "));
			final TextComponent button_1 = new TextComponent(TextComponent.fromLegacyText(" §e一天"));
			button_1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §cIP封禁 一天")));
			button_1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 1day "+reason));
			final TextComponent button_2 = new TextComponent(TextComponent.fromLegacyText(" §e七天"));
			button_2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §cIP封禁 七天")));
			button_2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 7day "+reason));
			final TextComponent button_3 = new TextComponent(TextComponent.fromLegacyText(" §e三十天"));
			button_3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §cIP封禁 三十天")));
			button_3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 30day "+reason));
			final TextComponent button_4 = new TextComponent(TextComponent.fromLegacyText(" §e永久"));
			button_4.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c永久封禁IP")));
			button_4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ipban "+report.getPlayer()+" "+reason));
			template_ipban = new TextComponent(ipban, button_1, button_2, button_3, button_4);
		}
		BaseComponent suffix;
		if (report.getID().equals("#NULL")) {
			suffix = prefix.duplicate();
		} else {
			final TextComponent processReport = new TextComponent(TextComponent.fromLegacyText("§a『§l标记处罚已处理§a』"));
			processReport.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a点击标记此处罚已处理")));
			processReport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lpt ok "+report.getID()));
			suffix = new TextComponent(prefix, processReport);
		}
		TextComponentUtils.sendMessage(player,
		                               prefix,
		                               staff_info,
		                               staff_reason,
		                               prefix,
		                               template_kick,
		                               template_mute,
		                               template_ban,
		                               template_ipban,
		                               suffix
				);
		/*player.spigot().sendMessage(prefix);
		player.spigot().sendMessage(staff_info);
		player.spigot().sendMessage(staff_reason);
		player.spigot().sendMessage(prefix);
		player.spigot().sendMessage(template_kick);
		player.spigot().sendMessage(template_mute);
		player.spigot().sendMessage(template_ban);
		player.spigot().sendMessage(template_ipban);
		player.spigot().sendMessage(prefix);*/
	}
}
