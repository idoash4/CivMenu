package vg.civcraft.mc.civmenu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmenu.database.TOSManager;
import vg.civcraft.mc.civmenu.guides.GuideBook;
import vg.civcraft.mc.civmenu.guides.ResponseManager;
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
		
		if(caption.equals("dismiss")) {
			return commandDismiss(sender, argv);
		}
		
		if(caption.equals("guide")) {
			return commandGuide(sender, argv);
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
					MercuryAPI.sendGlobalMessage("sign|" + player.getUniqueId(), "civmenu");
				}
				return true;
			}
		} else {
			player.sendMessage("You have already signed the terms of service.");
			return true;
		}
		
		return false;
	}

	private boolean commandDismiss(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Sorry, only players can dismiss events.");
			return true;
		}
		
		if(args.length == 1 && args[0].equals("*")) {
			ResponseManager.dismissAll((Player) sender);
			sender.sendMessage(ChatColor.GREEN + "You have dismissed all future events from CivMenu.");
			return true;
		}
		
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments, do /dismiss <plugin> <event>.");
			return true;
		}
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin(args[0]);
		if(plugin == null) {
			sender.sendMessage(ChatColor.RED + "That plugin isn't installed and thus cannot have it's events dismissed.");
			return true;
		}
		
		ResponseManager rm;
		if((rm = ResponseManager.getResponseManager(plugin)) != null) {
			rm.dismissEvent(args[1], (Player) sender);
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + args[0] + " does not have any events configured.");
			return true;
		}
	}
	
	private boolean commandGuide(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can have books you dodo!");
			return true;
		}
		
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments, do /guide <bookname>.");
			return true;
		}
		
		GuideBook.giveBook(args[0], (Player) sender);
		return true;
	}
}
