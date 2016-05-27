package vg.civcraft.mc.civmenu.donators;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatNotifier implements IPlayerDonationNotifier {

	private DonatorManager dm;

	public ChatNotifier(DonatorManager dm) {
		this.dm = dm;
	}

	@Override
	public void sendMessage(Player... players) {
		for (Player p : players) {
			p.sendMessage(ChatColor.AQUA + "Thanks to "
					+ dm.getRandomPlayerName() + " for supporting the server");
		}
	}

	@Override
	public void sendMessage(Collection<Player> players) {
		for (Player p : players) {
			p.sendMessage(ChatColor.AQUA + "Thanks to "
					+ dm.getRandomPlayerName() + " for supporting the server");
		}
	}
}
