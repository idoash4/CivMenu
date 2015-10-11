package vg.civcraft.mc.civmenu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import vg.civcraft.mc.civmenu.database.TOSManager;
import vg.civcraft.mc.mercury.MercuryAPI;

public class CommandHandler implements CommandExecutor{

	private CivMenu pluginInstance = null;
	private boolean isMercuryEnabled = false;
	
	public CommandHandler(CivMenu pluginInstance) {
		this.pluginInstance = pluginInstance;
		this.isMercuryEnabled = pluginInstance.getServer().getPluginManager().isPluginEnabled("Mercury");
	}	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String caption, String[] argv) {
		
		if (caption.length() <= 0) {
			return false;
		}
		
		if (caption.equals("help")) {
			return commandHelp(sender, argv);
		}
		
		if (caption.equals("sign")) {
			return commandSign(sender, argv);
		}
		
		return false;
	}

	private boolean commandHelp(CommandSender sender, String[] argv) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage("This command can only be executed by a player");
			return true;
		}
		
		if (argv.length < 1) {
			pluginInstance.SendHelpMenu(((Player)sender), null);
			return true;
		}
		
		for(Plugin plugin : Bukkit.getPluginManager().getPlugins()){
			if(plugin.getName().equalsIgnoreCase(argv[0])){
				pluginInstance.SendHelpMenu(((Player)sender), (JavaPlugin)plugin);
				return true;
			}
		}
		
		pluginInstance.SendHelpMenu(((Player)sender), null);
		return true;
	}

	private boolean commandSign(CommandSender sender, String[] argv) {
		
		if(!(sender instanceof Player)){
			return false;
		}
		
		Player player = (Player)sender;
		
		if (!TOSManager.isTermPlayer(player, "CivMenu Agreement")){
			if(TOSManager.addPlayer(player, "CivMenu Agreement")){
				player.sendMessage("Thank you for signing the terms of service");
				if (isMercuryEnabled){
					MercuryAPI.instance.sendMessage("all",
							"sign~"+player.getUniqueId().toString(), 
							"civmenu");
				}
				return true;
			}
		} else {
			player.sendMessage("You have already signed the terms of service");
			return true;
		}
		
		return false;
	}

}
