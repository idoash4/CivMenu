package vg.civcraft.mc.civmenu.donators;

import java.util.Collection;

import org.bukkit.entity.Player;

public interface IPlayerDonationNotifier {
	
	public void sendMessage(Player ... p);

	public void sendMessage(Collection <Player> players);
}
