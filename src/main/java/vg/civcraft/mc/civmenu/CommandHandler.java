package vg.civcraft.mc.civmenu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor{

	private CivMenu pluginInstance = null;
	
	public CommandHandler(CivMenu pluginInstance) {
		this.pluginInstance = pluginInstance;
	}	
	
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
		
		if (argv.length < 1) {
			return false;
		}
		
		Player player = (Player) sender;
		player.sendMessage("hey");
		System.out.println("s");
		
		return true;
	}

	private boolean commandSign(CommandSender sender, String[] argv) {
		
		if(!(sender instanceof Player)){
			return false;
		}
		((Player)sender).sendMessage("d");
		if(TOSManager.addPlayer((Player)sender)){
			((Player)sender).sendMessage("Thank you for signing the terms of service");
			return true;
		}
		
		return false;
	}

}
