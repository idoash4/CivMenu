package vg.civcraft.mc.civmenu;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;

public class TOSListener implements Listener {

	private CivMenu plugin = CivMenu.getInstance();
	private Map<UUID, Location> locations;

	public TOSListener() {
		locations = new ConcurrentHashMap<UUID, Location>();
	}
	
	@CivConfigs({
		@CivConfig(name = "terms.kickDelay", def = "6000", type = CivConfigType.Int),
		@CivConfig(name = "terms.kickMessage", def = "You must accept the terms in order to play", type = CivConfigType.String)
	})
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (!TOSManager.registeredPlayers.containsKey(p.getUniqueId())) {
			sendTOS(p);
			locations.put(p.getUniqueId(), p.getLocation());
			new BukkitRunnable() {
				
				@Override
				public void run() {
					locations.remove(p.getUniqueId());
					if(!TOSManager.registeredPlayers.containsKey(p.getUniqueId())){
						p.kickPlayer(plugin.GetConfig().get("terms.kickMessage").getString());
					}
					
				}
			}.runTaskLater(this.plugin, plugin.GetConfig().get("terms.kickDelay").getInt());
		}
	}
	
	@CivConfig(name = "terms.MovementRange", def = "15", type = CivConfigType.Int)
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (!TOSManager.registeredPlayers.containsKey(p.getUniqueId())) {
			if(event.getTo().distance(locations.get(p.getUniqueId())) > plugin.GetConfig().get("terms.MovementRange").getInt()){
				p.sendMessage(ChatColor.RED + "You must accept the terms in order to play.");
				sendTOS(p);
				event.setTo(locations.get(p.getUniqueId()));
			}
		}
	}

	@CivConfigs({
		@CivConfig(name = "terms.link", def = "http://www.google.com", type = CivConfigType.String),
		@CivConfig(name = "terms.message", def = "You can click this message to open up the terms of service.", type = CivConfigType.String) 
	})
	public void sendTOS(Player p) {

		Menu menu = new Menu();

		TextComponent welcome = new TextComponent("Welcome to Civcraft!");
		welcome.setColor(ChatColor.RED);
		welcome.setBold(true);
		menu.setTitle(welcome);

		TextComponent agree = new TextComponent(plugin.GetConfig()
				.get("terms.message").getString());

		agree.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, plugin
				.GetConfig().get("terms.link").getString()));
		menu.setSubTitle(agree);
		
		TextComponent confirm = new TextComponent(
				"Once you've read it, you can click this message to agree to the terms");
		confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
				"/sign"));
		
		menu.setParts(confirm);

		p.spigot().sendMessage(menu.create());

	}
}