package vg.civcraft.mc.civmenu;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;

public class TOSListener implements Listener {

	private CivMenu plugin = CivMenu.getInstance();

	public TOSListener() {

	}

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (!TOSManager.registeredPlayers.containsKey(p.getUniqueId())) {
			sendTOS(p);
		}
	}

	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX()
                && from.getBlockZ() == to.getBlockZ()
                && from.getWorld().equals(to.getWorld())) {
            // Player didn't move by at least one block.
            return;
        }
		
		Player p = event.getPlayer();
		if (!TOSManager.registeredPlayers.containsKey(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You must accept the terms in order to play.");
			sendTOS(p);
			event.setTo(event.getFrom());
		}
	}

	@CivConfigs({
			@CivConfig(name = "terms.link", def = "http://www.google.com", type = CivConfigType.String),
			@CivConfig(name = "terms.message", def = "You can click this message to open up the terms of service.", type = CivConfigType.String) })
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