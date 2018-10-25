package ldcr.LReport;

import org.bukkit.entity.Player;

import ldcr.Utils.Bukkit.TextComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageBuilder {
	private MessageBuilder() {}
	public static void sendPunishSuggest(final Player player, final Report report) {
		final String reason = report.getReason();
		final TextComponent prefix = new TextComponent("§c§l处罚 §7>> §b§l-----------------------------");
		final TextComponent staffInfo;
		final TextComponent staffReason = new TextComponent("§c§l处罚 §7>> §b处罚原因: §c"+reason);
		final TextComponent templateKick;
		final TextComponent templateMute;
		final TextComponent templateBan;
		final TextComponent templateIpban;
		{
			final TextComponent info = new TextComponent(TextComponent.fromLegacyText("§c§l处罚 §7>> §b志愿者 §a"+report.getReporter()+" §b对玩家 §c"+report.getDisplayPlayerName()+" §b的处罚申请  "));
			final TextComponent viewHistory = new TextComponent(TextComponent.fromLegacyText("§6『§l查看处罚记录§6』"));
			viewHistory.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a点击查看处罚记录")));
			viewHistory.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history "+report.getPlayer()));
			staffInfo =  new TextComponent(info, viewHistory);
		}
		{
			final TextComponent kick = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 踢出 "));
			final TextComponent button1 = new TextComponent(TextComponent.fromLegacyText(" §e踢出该玩家"));
			button1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c踢出游戏")));
			button1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kick "+report.getPlayer()+" "+reason));
			templateKick = new TextComponent(kick, button1);
		}
		{
			final TextComponent mute = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 禁言 "));
			final TextComponent button1 = new TextComponent(TextComponent.fromLegacyText(" §e一分钟"));
			button1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 一分钟")));
			button1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 1min "+reason));
			final TextComponent button2 = new TextComponent(TextComponent.fromLegacyText(" §e一小时"));
			button2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 一小时")));
			button2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 1h "+reason));
			final TextComponent button3 = new TextComponent(TextComponent.fromLegacyText(" §e一天"));
			button3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 一天")));
			button3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 1day "+reason));
			final TextComponent button4 = new TextComponent(TextComponent.fromLegacyText(" §e七天"));
			button4.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c禁言 七天")));
			button4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempmute "+report.getPlayer()+" 7day "+reason));
			templateMute = new TextComponent(mute, button1, button2, button3, button4);
		}
		{
			final TextComponent ban = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 封禁 "));
			final TextComponent button1 = new TextComponent(TextComponent.fromLegacyText(" §e一天"));
			button1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁 一天")));
			button1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempban "+report.getPlayer()+" 1day "+reason));
			final TextComponent button2 = new TextComponent(TextComponent.fromLegacyText(" §e七天"));
			button2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁 七天")));
			button2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempban "+report.getPlayer()+" 7day "+reason));
			final TextComponent button3 = new TextComponent(TextComponent.fromLegacyText(" §e三十天"));
			button3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁 三十天")));
			button3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempban "+report.getPlayer()+" 30day "+reason));
			final TextComponent button4 = new TextComponent(TextComponent.fromLegacyText(" §e永久"));
			button4.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c永久封禁")));
			button4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban "+report.getPlayer()+" "+reason));
			templateBan = new TextComponent(ban, button1, button2, button3, button4);
		}
		{
			final TextComponent ipban = new TextComponent(TextComponent.fromLegacyText(" §c§l--> 封禁 "));
			final TextComponent button1 = new TextComponent(TextComponent.fromLegacyText(" §e一天"));
			button1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §cIP封禁 一天")));
			button1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 1day "+reason));
			final TextComponent button2 = new TextComponent(TextComponent.fromLegacyText(" §e七天"));
			button2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §cIP封禁 七天")));
			button2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 7day "+reason));
			final TextComponent button3 = new TextComponent(TextComponent.fromLegacyText(" §e三十天"));
			button3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §cIP封禁 三十天")));
			button3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 30day "+reason));
			final TextComponent button4 = new TextComponent(TextComponent.fromLegacyText(" §e永久"));
			button4.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText("§a玩家 §e"+report.getPlayer()+" §a将被 §c永久封禁IP")));
			button4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ipban "+report.getPlayer()+" "+reason));
			templateIpban = new TextComponent(ipban, button1, button2, button3, button4);
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
				staffInfo,
				staffReason,
				prefix,
				templateKick,
				templateMute,
				templateBan,
				templateIpban,
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
