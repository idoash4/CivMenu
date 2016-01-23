package vg.civcraft.mc.civmenu.guides;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.Menu;
import vg.civcraft.mc.civmenu.datamanager.DismissalDAO;

public class ResponseManager {

	private static HashMap<String, ResponseManager> managers = new HashMap<String, ResponseManager>();
	
	private static DismissalDAO wildcardDAO;
	private static ArrayList<UUID> wildcardDismissals;
	
	private final String plugin;
	private DismissalDAO dao;
	private HashMap<String, EventResponse> responses;
	private ConcurrentHashMap<UUID, List<String>> dismissals;
	private String documentationUrl;
	private Logger logger;
	private UnloadDismissalsTask unloadTask;
	
	private ResponseManager(Plugin plugin) {
		this.plugin = plugin.getName();
		logger = CivMenu.getInstance().getLogger();
		dao = DismissalDAO.getInstance(plugin.getName());
		responses = new HashMap<String, EventResponse>();
		dismissals = new ConcurrentHashMap<UUID, List<String>>();
		loadEventResponses(plugin.getConfig());
		unloadTask = new UnloadDismissalsTask(this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(CivMenu.getInstance(), unloadTask, 0L, unloadTask.getUnloadDelay());
	}
	
	private void loadEventResponses(FileConfiguration config) {
		if(!config.contains("CivMenu")) {
			return;
		}
		if(config.contains("CivMenu.events")) {
			ConfigurationSection responses = config.getConfigurationSection("CivMenu.events");
			logger.info("Loading events from config for " + plugin);
			for(String event : responses.getKeys(false)) {
				if(!responses.contains(event + ".text")) {
					continue;
				}
				String url = null;
				String book = null;
				if(responses.contains(event + ".url")) url = responses.getString(event + ".url");
				if(responses.contains(event + ".book")) book = responses.getString(event + ".book");
				String text = responses.getString(event + ".text");
				if(url == null && book == null) {
					this.responses.put(event, new EventResponse(text));
				} else {
					this.responses.put(event, new EventResponse(text, url, book));
				}
			}
		}
		if(config.contains("CivMenu.url")) {
			documentationUrl = config.getString("CivMenu.url");
		}
		if(config.contains("CivMenu.books")) {
			ConfigurationSection books = config.getConfigurationSection("CivMenu.books");
			GuideBook book;
			logger.info("Loading books for " + plugin);
			for(String bookName : books.getKeys(false)) {
				book = new GuideBook(bookName);
				book.addPages(books.getStringList(bookName + ".pages"));
				GuideBook.addBook(book);
			}
		}
	}
	
	public void loadDismissals(UUID id) {
    List<String> events = dao.getDismissals(id);
    events.retainAll(responses.keySet());
		dismissals.put(id, events);
		unloadTask.updateMRU(id);
	}
	
	public void unloadDismissals(UUID id) {
		dismissals.remove(id);
		unloadTask.removePlayer(id);
	}
	
	public void sendMessageForEvent(String event, Player player) {
		if(wildcardDismissals.contains(player.getUniqueId())) {
			return;
		}
		if(!dismissals.containsKey(player.getUniqueId())) {
			loadDismissals(player.getUniqueId());
		}
		if(dismissals.get(player.getUniqueId()).contains(event)) {
			logger.info(player.getName() + " has dismissed " + plugin + " " + event);
			return;
		}
		
		if(responses.get(event) == null) {
			logger.severe(plugin + " does not have an event configured called \"" + event + "\"");
			return;
		}
		
		if(player.hasPermission("CivMenu.bypass")) {
			logger.info("Not sending " + plugin + " " + event + " to " + player.getName() + ", they have the bypass permission");
		}
		
		Menu menu = new Menu();
		
		TextComponent title = new TextComponent(plugin + " " + event);
		title.setColor(ChatColor.YELLOW);
		menu.setTitle(title);
		
		EventResponse response = responses.get(event);
		
		TextComponent message = new TextComponent(response.getText());
		message.setColor(ChatColor.AQUA);
		menu.setSubTitle(message);
		
		TextComponent command = new TextComponent("Click to permanently dismiss");
		command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/dismiss " + plugin + " " + event).create()));
		command.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dismiss " + plugin + " " + event));
		command.setItalic(true);
		menu.addPart(command);
		
		if(GuideBook.getBook(plugin) != null || (response.getBook() != null && GuideBook.getBook(response.getBook()) != null)) {
			TextComponent book = new TextComponent("Click for book");
			String bookName = response.getBook() != null ? response.getBook() : plugin;
			book.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/guide " + bookName).create()));
			book.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guide " + bookName));
			book.setItalic(true);
			menu.addPart(book);
		}
		
		if(response.getUrl() != null || documentationUrl != null) {
			TextComponent url = new TextComponent("Click to view documentation");
			String urlString = response.getUrl() != null ? response.getUrl() : documentationUrl;
			url.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(urlString).create()));
			url.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlString));
			url.setItalic(true);
			menu.addPart(url);
		}
		
		menu.sendPlayer(player);
	}
	
	public void dismissEvent(String event, Player player) {
		dao.dismissEvent(event, player.getUniqueId());
		dismissals.get(player.getUniqueId()).add(event);
	}
	
	public static ResponseManager getResponseManager(Plugin plugin) {
		if(managers.get(plugin.getName()) == null) {
			managers.put(plugin.getName(), new ResponseManager(plugin));
		}
		return managers.get(plugin.getName());
	}
	
	public static void handlePlayerLogin(Player player) {
		synchronized(wildcardDismissals) {
			if(wildcardDAO.getDismissals(player.getUniqueId()) != null) {
				wildcardDismissals.add(player.getUniqueId());
				return;
			}
		}
		for(ResponseManager manager : managers.values()) {
			manager.loadDismissals(player.getUniqueId());
		}
	}
	
	public static void handlePlayerLogout(Player player) {
		synchronized(wildcardDismissals) {
			if(wildcardDismissals.contains(player.getUniqueId())) {
				wildcardDismissals.remove(player.getUniqueId());
				return;
			}
		}
		for(ResponseManager manager : managers.values()) {
			manager.unloadDismissals(player.getUniqueId());
		}
	}
	
	public static void dismissAll(Player player) {
		synchronized(wildcardDismissals) {
			wildcardDismissals.add(player.getUniqueId());
		}
		wildcardDAO.dismissEvent("*", player.getUniqueId());
	}
	
	public static void initWildcardDismissals() {
		wildcardDAO = DismissalDAO.getInstance("*");
	}
}
