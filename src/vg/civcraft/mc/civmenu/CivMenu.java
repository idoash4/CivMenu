package vg.civcraft.mc.civmenu;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import vg.civcraft.mc.civmenu.database.TOSManager;
import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;
import vg.civcraft.mc.mercury.MercuryAPI;

public class CivMenu extends ACivMod {
	
	private TOSManager tosManager;
	private static CivMenu plugin;
	
	public void onEnable() {
		super.onEnable();
		new CivMenuAPI();
		plugin = this;
		tosManager = new TOSManager(this);
		getServer().getPluginManager().registerEvents(new TOSListener(), this);
		if (getServer().getPluginManager().isPluginEnabled("Mercury")){
			getServer().getPluginManager().registerEvents(new MercuryListener(), plugin);
			MercuryAPI.instance.registerPluginMessageChannel("civmenu");
		}
		CommandHandler commandHandler = new CommandHandler(this);
		for (String command : getDescription().getCommands().keySet()) {
			getCommand(command).setExecutor(commandHandler);
		}
		
	}
	
	public void onLoad() {
		super.onLoad();
	}
	
    public void onDisable() { 
    	tosManager.save();
    }

    
	@CivConfigs({
		@CivConfig(name = "helpMenu.message", def = "Civcraft is a unique Minecraft server where the players make the rules. It's an experiment for communities, political ideologies, debate and discussion. Most Minecraft servers have rules, like “no griefing”, “no stealing”, or “be nice”. Civcraft doesn't. However, that doesn't mean you can run wild throughout the world, pillaging and plundering all that you come across, because Civcraft isn't an ordinary vanilla Minecraft server. We have a collection of plugins that encourages co-operation between players by increasing Minecraft's base difficulty tenfold and puts players in control of justice.", type = CivConfigType.String),
		@CivConfig(name = "helpMenu.plugins", def= "NameLayer, Citadel, JukeAlert, RealisticBiomes, ItemExchange, CivChat2", type = CivConfigType.String)
	})
    public void SendHelpMenu(Player player, JavaPlugin plugin){
		Menu menu = new Menu();

		if (plugin == null) {
			TextComponent title = new TextComponent("Civcraft Help Menu");
			title.setColor(ChatColor.RED);
			menu.setTitle(title);
			menu.setSubTitle(new TextComponent(this.GetConfig().get("helpMenu.message").getString()));
			String[] plugins = this.GetConfig().get("helpMenu.plugins").getString().split(", ");
			for(String pluginName:plugins){
				TextComponent part = new TextComponent(pluginName);
				part.setColor(ChatColor.YELLOW);
				part.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + pluginName));
				menu.addPart(part);
			}
			menu.sendPlayer(player);
			return;
		}
		
		menu.setTitle(new TextComponent(plugin.getName()));
		
		if (plugin.getDescription().getDescription() != null) {
			menu.setSubTitle(new TextComponent(plugin.getDescription()
					.getDescription()));
		}
		
		for (String commandName : plugin.getDescription().getCommands()
				.keySet()) {
			Command command = plugin.getCommand(commandName);
			if (command.getPermission() != null
					&& !player.hasPermission(command.getPermission())) {
				continue;
			}
			TextComponent part = new TextComponent(command.getLabel());
			part.setColor(ChatColor.YELLOW);
			part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(command.getUsage() + " \n"
							+ command.getDescription()).create()));
			part.setClickEvent(new ClickEvent(
					ClickEvent.Action.SUGGEST_COMMAND, "/" + command.getLabel()));

			menu.addPart(part);
		}

		menu.sendPlayer(player);
    	
    }
    
	public TOSManager getTosManager() {
		return tosManager;
	}

	@Override
	protected String getPluginName() {
		return "CivMenu";
	}
	
	public static CivMenu getInstance() {
		return plugin;
	}
    
}
