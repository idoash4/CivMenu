package vg.civcraft.mc.civmenu.guides;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;

public class UnloadDismissalsTask implements Runnable {

	private ResponseManager manager;
	private ConcurrentHashMap<UUID, Long> mru;
	private long delay = -1;
	
	public UnloadDismissalsTask(ResponseManager manager) {
		this.manager = manager;
		mru = new ConcurrentHashMap<UUID, Long>();
		delay = getUnloadDelay();
	}
	
	@Override
	public void run() {
		unloadCache();
	}
	
	@CivConfigs({
		@CivConfig(name = "unload_delay", def = "18000", type = CivConfigType.Int)
	})
	public long getUnloadDelay() {
		if(delay < 0) {
			delay = CivMenu.getInstance().GetConfig().get("unload_delay").getInt();
		}
		return delay;
	}
	
	public void unloadCache() {
		Enumeration<UUID> keys = mru.keys();
		UUID id = null;
		while((id = keys.nextElement()) != null) {
			if(mru.get(id) < System.currentTimeMillis() - CivMenu.getInstance().GetConfig().get("unload_delay").getInt()) {
				manager.unloadDismissals(id);
			}
		}
	}
	
	public void updateMRU(UUID id) {
		mru.put(id, System.currentTimeMillis());
	}
	
	public void removePlayer(UUID id) {
		mru.remove(id);
	}
}
