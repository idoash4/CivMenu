package vg.civcraft.mc.civmenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;

public class TOSListener implements Listener {
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			if (!TOSManager.registeredPlayers
					.containsKey(p.getUniqueId())) {
				sendTOS(p);
			}
		}
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if (p != null) {
			if (!TOSManager.registeredPlayers
					.containsKey(p.getUniqueId())) {
				if (((event.getTo().getX() != event.getFrom().getX()) || (event.getTo().getZ() != event.getFrom().getZ()))) {
					event.setTo(event.getFrom());
		        }
				sendTOS(p);
				return;
			}
		}
	}
	
	public void sendTOS(Player p) {
		if (p == null) {
			return;
		}
		
		Menu menu = new Menu();
		
		TextComponent welcome = new TextComponent("Welcome to Civcraft!");
		welcome.setColor(ChatColor.RED);
		welcome.setBold(true);
		menu.setTitle(welcome);

		TextComponent agree = new TextComponent(
				"To be able to play, you need to read our terms of service first and agree to those");
		menu.setSubTitle(agree);
		
		TextComponent click = new TextComponent(
				"You can click this message to open up the terms of service");
		click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
				"http://google.com")); // TODO TODO TODO change to wherever the
										// tos are
		TextComponent confirm = new TextComponent("Once you've read it, you can click this message to agree to the terms");
		confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sign"));
		menu.setParts(click, confirm);
		
		p.spigot().sendMessage(menu.create());

	}
}