package sakura.kooi.SakuraReport.messageChannel;

import java.io.IOException;

import org.bukkit.entity.Player;

public interface IMessageChannel {
	public void connectChannel() throws IOException;
	public void disconnectChannel();
	public void jumpServer(Player player, String server);
	public void broadcastReport(final String player, final Player reporter, final String server, final String reason) throws IOException;
	public void broadcastConsoleReport(final String player, final String server, final String reason) throws IOException;
	public void broadcastStaff(final String player,final Player reporter, final String server, final String reason) throws IOException;
	public void broadcastProcessed(String player, String reporter, Player executor) throws IOException;
}
