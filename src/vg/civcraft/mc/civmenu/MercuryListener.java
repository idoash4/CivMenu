package vg.civcraft.mc.civmenu;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import vg.civcraft.mc.civmenu.database.TOSManager;
import vg.civcraft.mc.mercury.events.AsyncPluginBroadcastMessageEvent;

public class MercuryListener implements Listener {
	
	@EventHandler
	public void onMercuryMessage(AsyncPluginBroadcastMessageEvent event){
		if (!event.getChannel().equalsIgnoreCase("civmenu"))
			return;
		String[] splitmsg = event.getMessage().split("\\|");
		if (splitmsg[0].equals("sign")){
			UUID uuid = UUID.fromString(splitmsg[1]);
			TOSManager.setUUID(uuid, "CivMenu Agreement");
		}
	}
}
