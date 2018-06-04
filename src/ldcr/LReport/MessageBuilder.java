package ldcr.LReport;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageBuilder {
	public static void sendPunishSuggest(final Player player, final Report report) {
		final BaseComponent line = new TextComponent("§c§l处罚 §7>> §b§l-----------------------------");
		final BaseComponent line1;
		final BaseComponent suggest = new TextComponent("§c§l处罚 §7>> §b处罚原因: §c"+report.getReason());
		final BaseComponent line2; // autoclicker
		final BaseComponent line3; // cheating
		final BaseComponent line4; // spam
		final BaseComponent line5; // ads
		{
			final TextComponent reason = new TextComponent("§c§l处罚 §7>> §b志愿者 §a"+report.getReporter()+" §b对玩家 §c"+report.getDisplayPlayerName()+" §b的处罚申请  ");
			final TextComponent viewHistory = new TextComponent("§6§l『查看处罚记录』");
			viewHistory.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a点击查看处罚记录")}));
			viewHistory.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/history "+report.getPlayer()));
			line1 =  new TextComponent(reason, viewHistory);
		}
		{
			final TextComponent autoClicker = new TextComponent(" §c§l--> 连点 ");
			final TextComponent time1 = new TextComponent(" §e第一次");
			time1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁三天: 连点")}));
			time1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 3days AutoClicker"));
			final TextComponent time2 = new TextComponent(" §e第二次");
			time2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁七天: 连点")}));
			time2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 7days AutoClicker"));
			final TextComponent time3 = new TextComponent(" §e第三次");
			time3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁一月: 连点")}));
			time3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 30days AutoClicker"));
			line2 = new TextComponent(autoClicker, time1, time2, time3);
		}
		{
			final TextComponent cheat = new TextComponent(" §c§l--> 作弊 ");
			final TextComponent time1 = new TextComponent(" §e第一次");
			time1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁一月: 作弊")}));
			time1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 30days Hacking"));
			final TextComponent time2 = new TextComponent(" §e第二次");
			time2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c永久封禁: 作弊")}));
			time2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ipban "+report.getPlayer()+" Hacking"));
			line3 = new TextComponent(cheat, time1, time2);
		}
		{
			final TextComponent spam = new TextComponent(" §c§l--> 刷屏 ");
			final TextComponent time1 = new TextComponent(" §e第一次");
			time1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁一天: 刷屏")}));
			time1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 1days 请勿刷屏"));
			final TextComponent time2 = new TextComponent(" §e第二次");
			time2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁三天: 刷屏")}));
			time2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 3days 请勿刷屏"));
			final TextComponent time3 = new TextComponent(" §e第三次");
			time3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁七天: 刷屏")}));
			time3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 7days 请勿刷屏"));
			line4 = new TextComponent(spam, time1, time2, time3);
		}
		{
			final TextComponent ads = new TextComponent(" §c§l--> 广告 ");
			final TextComponent time1 = new TextComponent(" §e第一次");
			time1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁三天: 广告")}));
			time1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 3days 广告宣传"));
			final TextComponent time2 = new TextComponent(" §e第二次");
			time2.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁十五天: 广告")}));
			time2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 15days 广告宣传"));
			final TextComponent time3 = new TextComponent(" §e第三次");
			time3.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] {
					new TextComponent("§a玩家 §e"+report.getPlayer()+" §a将被 §c封禁一月: 广告")}));
			time3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tempipban "+report.getPlayer()+" 30days 广告宣传"));
			line5 = new TextComponent(ads, time1, time2, time3);
		}
		player.spigot().sendMessage(line);
		player.spigot().sendMessage(line1);
		player.spigot().sendMessage(suggest);
		player.spigot().sendMessage(line);
		player.spigot().sendMessage(line2);
		player.spigot().sendMessage(line3);
		player.spigot().sendMessage(line4);
		player.spigot().sendMessage(line5);
		player.spigot().sendMessage(line);
	}
}
