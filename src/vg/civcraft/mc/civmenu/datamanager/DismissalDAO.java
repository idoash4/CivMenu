package vg.civcraft.mc.civmenu.datamanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import vg.civcraft.mc.civmenu.CivMenu;
import vg.civcraft.mc.civmenu.database.Database;
import vg.civcraft.mc.civmodcore.Config;
import vg.civcraft.mc.civmodcore.annotations.CivConfig;
import vg.civcraft.mc.civmodcore.annotations.CivConfigType;
import vg.civcraft.mc.civmodcore.annotations.CivConfigs;

public class DismissalDAO {

	private static DismissalDAO instance;
	
	private Database db;
	private String tableName;
	
	private DismissalDAO(String plugin) {
		tableName = plugin + "_dismissals";
	}
	
	@CivConfigs({
		@CivConfig(name = "mysql.username", def = "bukkit", type = CivConfigType.String),
		@CivConfig(name = "mysql.password", def = "", type = CivConfigType.String),
		@CivConfig(name = "mysql.host", def = "localhost", type = CivConfigType.String),
		@CivConfig(name = "mysql.dbname", def = "bukkit", type = CivConfigType.String),
		@CivConfig(name = "mysql.port", def = "3306", type = CivConfigType.Int)
	})
	private void initializeDatabase() {
		CivMenu plugin = CivMenu.getInstance();
		Config config = plugin.GetConfig();
		String username = config.get("mysql.username").getString();
		String password = config.get("mysql.password").getString();
		String host = config.get("mysql.host").getString();
		String dbname = config.get("mysql.dbname").getString();
		int port = config.get("mysql.port").getInt();
		db = new Database(host, port, dbname, username, password, plugin.getLogger());
		if (!db.connect()) {
			plugin.getLogger().log(Level.INFO, "Mysql could not connect, shutting down.");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
		createTables();
	}
	
	private void createTables() {
		db.execute("create table if not exists " + tableName + " ("
					+ "event VARCHAR(40) not null,"
					+ "player VARCHAR(40) not null)");
	}
	
	public List<String> getDismissals(UUID id) {
		List<String> events = new ArrayList<String>();
		try {
			PreparedStatement getDismissals = db.prepareStatement("SELECT * FROM " + tableName + " WHERE player = ?");
			getDismissals.setString(1, id.toString());
			ResultSet result = getDismissals.executeQuery();
			while(result.next()) {
				events.add(result.getString("event"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return events;
	}
	
	public void dismissEvent(String event, UUID player) {
		try {
			PreparedStatement dismiss = db.prepareStatement("INSERT INTO " + tableName + " (event, player) VALUES (?,?)");
			dismiss.setString(1, event);
			dismiss.setString(2, player.toString());
			dismiss.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static DismissalDAO getInstance(String plugin) {
		if(instance == null) {
			instance = new DismissalDAO(plugin);
			instance.initializeDatabase();
		}
		return instance;
	}
}
