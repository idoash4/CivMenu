package vg.civcraft.mc.civmenu.guides;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UnloadDismissalsTask extends BukkitRunnable {

	private Player player;
	private ResponseManager manager;
	
	public UnloadDismissalsTask(Player player, ResponseManager manager) {
		this.player = player;
		this.manager = manager;
	}
	
	@Override
	public void run() {
		manager.unloadDismissals(player);
	}

}
