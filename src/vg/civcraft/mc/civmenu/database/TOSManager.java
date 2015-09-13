package vg.civcraft.mc.civmenu.database;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.datamanager.FlatFileManager;
import vg.civcraft.mc.civmenu.datamanager.ISaveLoad;
import vg.civcraft.mc.civmenu.datamanager.MysqlManager;
import vg.civcraft.mc.civmodcore.Config;
import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;


public class TOSManager {
	private CivMenu plugin;
	private static Config config;
	
	private static ISaveLoad manager;
	
	public TOSManager(CivMenu plugin) {
		this.plugin = plugin;
		config = plugin.GetConfig();
		handleWriteManagerment();
	}
	
	@CivConfigs({
		@CivConfig(name = "save.manager", def = "0 # Use 0 for flatfile, 1 for mysql.", type = CivConfigType.String)
	})
	private void handleWriteManagerment() {
		switch (config.get("save.manager").getInt()) {
		case 0:
			manager = new FlatFileManager(plugin);
			break;
		case 1:
			manager = new MysqlManager(plugin);
			break;
		default:
			plugin.getLogger().log(Level.INFO, "No database manager specified, shutting down.");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
		manager.load();
	}
	
	/**
	 * Adds an accepted term to a players track record.
	 */
	public static boolean addPlayer(Player p, String term) {
		if (manager != null) {
			manager.addPlayer(p, term);
		} else {
			return false;
		}
		
		return true;
	}

	/**
	 * Checks if a player has a specific term.
	 */
	public static boolean isTermPlayer(Player p, String term) {
		return manager != null && manager.isAddedPlayer(p, term);
	}
	
	public void save() {
		manager.save();
	}
}
