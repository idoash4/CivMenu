package vg.civcraft.mc.civmenu.guides;

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
	
	private final String plugin;
	private DismissalDAO dao;
	private HashMap<String, String> responses;
	private ConcurrentHashMap<UUID, List<String>> dismissals;
	private String documentationUrl;
	private Logger logger;
	private UnloadDismissalsTask unloadTask;
	
	private ResponseManager(Plugin plugin) {
		this.plugin = plugin.getName();
		logger = CivMenu.getInstance().getLogger();
		dao = DismissalDAO.getInstance(plugin.getName());
		responses = new HashMap<String, String>();
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
				this.responses.put(event, responses.getString(event + ".text"));
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
		
		TextComponent message = new TextComponent(responses.get(event));
		message.setColor(ChatColor.AQUA);
		menu.setSubTitle(message);
		
		TextComponent link = new TextComponent("Click to permanently dismiss");
		link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/dismiss " + plugin + " " + event).create()));
		link.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dismiss " + plugin + " " + event));
		link.setItalic(true);
		menu.addPart(link);
		
		if(GuideBook.getBook(plugin) != null) {
			TextComponent book = new TextComponent("Click for plugin guide");
			book.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/guide " + plugin).create()));
			book.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guide " + plugin));
			book.setItalic(true);
			menu.addPart(book);
		}
		
		if(documentationUrl != null) {
			TextComponent docu = new TextComponent("Click to view documentation");
			docu.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(documentationUrl).create()));
			docu.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, documentationUrl));
			docu.setItalic(true);
			menu.addPart(docu);
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
		for(ResponseManager manager : managers.values()) {
			manager.loadDismissals(player.getUniqueId());
		}
	}
	
	public static void handlePlayerLogout(Player player) {
		for(ResponseManager manager : managers.values()) {
			manager.unloadDismissals(player.getUniqueId());
		}
	}
}
