package vg.civcraft.mc.civmenu.donators;

import java.util.Collection;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.Title;

public class TitleNotifier implements IPlayerDonationNotifier {

	private DonatorManager dm;
	private final static int fadeInTicks = 20;
	private final static int stayTicks = 60;
	private final static int fadeOutTicks = 20;
	private final static String patreonURL = "https://www.patreon.com/user?ty=h&u=2438745";

	public TitleNotifier(DonatorManager dm) {
		this.dm = dm;
	}

	@Override
	public void sendMessage(final Player... players) {
		String name = dm.getRandomPlayerName();
		Title tit = new Title(ChatColor.GOLD + name, ChatColor.GOLD
				+ "Thanks for supporting us", fadeInTicks, stayTicks,
				fadeOutTicks);
		final TextComponent text = new TextComponent(
				"Thanks to people like "
						+ name
						+ " this server can keep running. Want to donate and have your name displayed on login as well? Consider becoming a Civcraft Patron (Click here to open)");
		text.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
				patreonURL));
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("Open the Patreon page").create()));
		for (Player p : players) {
			tit.sendTitle(p);
		}
		Bukkit.getScheduler().runTaskLater(CivMenu.getInstance(),
				new Runnable() {

					@Override
					public void run() {
						for (Player p : players) {
							p.spigot().sendMessage(text);
						}

					}
				}, 10L);
	}

	@Override
	public void sendMessage(final Collection<Player> players) {
		String name = dm.getRandomPlayerName();
		Title tit = new Title(ChatColor.GOLD + name, ChatColor.GOLD
				+ "Thanks for supporting us", fadeInTicks, stayTicks,
				fadeOutTicks);
		final TextComponent text = new TextComponent(
				"Thanks to people like "
						+ name
						+ " this server can keep running. Want to donate and have your name displayed on login as well? Consider becoming a Civcraft Patron (Click here to open)");
		text.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
				patreonURL));
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("Open the Patreon page").create()));
		for (Player p : players) {
			tit.sendTitle(p);
		}
		Bukkit.getScheduler().runTaskLater(CivMenu.getInstance(),
				new Runnable() {

					@Override
					public void run() {
						for (Player p : players) {
							p.spigot().sendMessage(text);
						}

					}
				}, 10L);
	}

}
