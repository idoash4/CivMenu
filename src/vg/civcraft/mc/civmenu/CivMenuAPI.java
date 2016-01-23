package vg.civcraft.mc.civmenu;

import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CivMenuAPI {

	private static CivMenuAPI api;
	
	private CivMenuAPI() {}
	
	public static CivMenuAPI getInstance() {
		if(api == null) {
			api = new CivMenuAPI();
		}
		return api;
	}
	
	public void performAction(Player p, Menu menu) {
		menu.sendPlayer(p);
	}
	
	/**
	 * 
	 * @param title the title component
	 * @param message the main message component
	 * @param other any other components such as a clickable link or command
	 * @return a menu with those components
	 */
	public static Menu makeMenu(TextComponent title, TextComponent message, List<TextComponent> other) {
		Menu menu = new Menu();
		menu.setTitle(title);
		menu.setSubTitle(message);
		for(TextComponent component : other) {
			menu.addPart(component);
		}
		return menu;
	}
	
	/**
	 * @param text the text for the component
	 * @param color the color of the text
	 * @return a text component to be used as either a title or subtitle
	 */
	public static TextComponent makeTextComponent(String text, ChatColor color, boolean italic, boolean bold, boolean obfuscated, boolean strikethrough) {
		TextComponent component = new TextComponent(text);
		component.setItalic(italic);
		component.setBold(bold);
		component.setObfuscated(obfuscated);
		component.setStrikethrough(strikethrough);
		component.setColor(color);
		return component;
	}
	
	/**
	 * @param text the actual text for the message
	 * @param link the link to send people to (ie. "http://civcraft.vg")
	 * @param hoverText text to show when you hover over this component, almost always should be the same as command
	 * @return a text component that when clicked will open the link in the default browser
	 */
	public static TextComponent makeLinkComponent(String text, String link, String hoverText, boolean italic, boolean bold, boolean obfuscated, boolean strikethrough, ChatColor color) {
		TextComponent component = new TextComponent(text);
		component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
		component.setItalic(italic);
		component.setBold(bold);
		component.setObfuscated(obfuscated);
		component.setStrikethrough(strikethrough);
		component.setColor(color);
		return component;
	}
	
	/**
	 * @param text the actual text for the message
	 * @param command the text representation of the command to run (ie. "/command args")
	 * @param hoverText text to show when you hover over this component, almost always should be the same as command
	 * @return a text component that when clicked will run a command
	 */
	public static TextComponent makeCommandComponent(String text, String command, String hoverText, boolean italic, boolean bold, boolean obfuscated, boolean strikethrough, ChatColor color) {
		TextComponent component = new TextComponent(text);
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
		component.setItalic(italic);
		component.setBold(bold);
		component.setObfuscated(obfuscated);
		component.setStrikethrough(strikethrough);
		component.setColor(color);
		return component;
	}
	
	/**
	 * @param text the actual text for the message
	 * @param command the text representation of the command to run (ie. "/command args")
	 * @param hoverText text to show when you hover over this component, almost always should be the same as command
	 * @return a text component that when clicked will suggest a command
	 */
	public static TextComponent makeCommandSuggestComponent(String text, String command, String hoverText, boolean italic, boolean bold, boolean obfuscated, boolean strikethrough, ChatColor color) {
		TextComponent component = new TextComponent(text);
		component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
		component.setItalic(italic);
		component.setBold(bold);
		component.setObfuscated(obfuscated);
		component.setStrikethrough(strikethrough);
		component.setColor(color);
		return component;
	}
}