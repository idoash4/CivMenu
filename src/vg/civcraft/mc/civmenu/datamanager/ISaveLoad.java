package vg.civcraft.mc.civmenu.datamanager;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface ISaveLoad {

	public void load();
	public void save();
	public void addPlayer(Player p, String term);
	public void setUUID(UUID uuid, String term);
	public boolean isAddedPlayer(Player p, String term);
}
