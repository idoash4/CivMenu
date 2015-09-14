package vg.civcraft.mc.civmenu;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import vg.civcraft.mc.civmenu.database.TOSManager;
import vg.civcraft.mc.civmodcore.ACivMod;

public class CivMenu extends ACivMod {
	
	private TOSManager tosManager;
	private static CivMenu plugin;
	
	public void onEnable() {
		super.onEnable();
		new CivMenuAPI();
		plugin = this;
		tosManager = new TOSManager(this);
		getServer().getPluginManager().registerEvents(new TOSListener(), this);
		
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

    public void SendHelpMenu(Player player, JavaPlugin plugin){
    	Menu menu = new Menu();
    	
		menu.setTitle(new TextComponent(plugin.getName()));

		if(plugin.getDescription().getDescription()!=null){
			menu.setSubTitle(new TextComponent(plugin.getDescription().getDescription()));
		}
		
    	for (String commandName : plugin.getDescription().getCommands().keySet()) {
    		Command command = plugin.getCommand(commandName);
			if(command.getPermission()!=null && !player.hasPermission(command.getPermission())){
				continue;
			}
			TextComponent part = new TextComponent(command.getLabel());
			part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command.getDescription()).create()));
			
			//This simply doesn't work. Nice one Spigot.
			part.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + command.getLabel()));
			
			menu.addPart(part);
		}
    	
    	player.spigot().sendMessage(menu.create());
    	
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
