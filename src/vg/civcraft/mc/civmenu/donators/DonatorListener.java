package vg.civcraft.mc.civmenu.donators;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;

public class DonatorListener implements Listener {

	private IPlayerDonationNotifier notifier;
	private static final int firstJoinDelay = 600000;

	public DonatorListener(IPlayerDonationNotifier notifer) {
		this.notifier = notifer;
	}

	@EventHandler
	@CivConfigs({
		@CivConfig(name = "donationNotifcation", def ="false", type = CivConfigType.Bool),
	})
	public void playerLogin(PlayerJoinEvent e) {
	   
		if (!CivMenu.getInstance().GetConfig().get("donationNotifcation").getBool() || System.currentTimeMillis() - e.getPlayer().getFirstPlayed() < firstJoinDelay) {
			// dont show messages during first 10 min to not possibly override
			// tos title messages
			return;
		}
		notifier.sendMessage(e.getPlayer());
		
	}

}
