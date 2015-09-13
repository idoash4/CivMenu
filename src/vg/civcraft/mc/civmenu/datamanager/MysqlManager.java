package vg.civcraft.mc.civmenu.datamanager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.TermObject;
import vg.civcraft.mc.civmenu.database.Database;

public class MysqlManager implements ISaveLoad{

	private CivMenu plugin;
	private Database db;
	
	private Map<UUID, TermObject> registeredPlayers = new HashMap<UUID, TermObject>();
	
	public MysqlManager(CivMenu plugin) {
		this.plugin = plugin;
		
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPlayer(Player p, String term) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAddedPlayer(Player p, String term) {
		// TODO Auto-generated method stub
		return false;
	}
}
