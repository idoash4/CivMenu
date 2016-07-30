package vg.civcraft.mc.civmenu.guides;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DismissalCacheListener implements Listener {

	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		ResponseManager.handlePlayerLogout(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		ResponseManager.handlePlayerLogin(event.getPlayer());
	}
}