package vg.civcraft.mc.civmenu.donators;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmenu.Title;

public class TitleNotifier implements IPlayerDonationNotifier {
	
	private DonatorManager dm;
	private final static int fadeInTicks = 20;
	private final static int stayTicks = 60;
	private final static int fadeOutTicks = 20;
	

	public TitleNotifier(DonatorManager dm) {
		this.dm = dm;
	}
	
	@Override
	public void sendMessage(Player... players) {
		Title tit = new Title(ChatColor.GOLD + dm.getRandomPlayerName(), ChatColor.GOLD + "Thanks for supporting us", fadeInTicks, stayTicks, fadeOutTicks);
		for(Player p : players) {
			tit.sendTitle(p);
		}
	}

	@Override
	public void sendMessage(Collection<Player> players) {
		Title tit = new Title(ChatColor.GOLD + dm.getRandomPlayerName(), ChatColor.GOLD + "Thanks for supporting us", fadeInTicks, stayTicks, fadeOutTicks);
		for(Player p : players) {
			tit.sendTitle(p);
		}	
	}
	
	

}
