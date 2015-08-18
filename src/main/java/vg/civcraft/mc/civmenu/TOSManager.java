package vg.civcraft.mc.civmenu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;


public class TOSManager {
	private CivMenu plugin;
	public static Map <UUID,Long> registeredPlayers;
	private static File logFile;
	
	
	public TOSManager(CivMenu plugin) {
		this.plugin = plugin;
		registeredPlayers = new TreeMap<UUID, Long>();
		new File("plugins/CivMenu").mkdirs(); //this should already exist, but we do this anyway to make sure
		logFile = new File("plugins/CivMenu/registeredPlayers.txt");
		parseLogFile();
	}
	
	public void parseLogFile() {
	        String line = null;
	        try {
	            FileReader fileReader = 
	                new FileReader(logFile.getName());
	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	                parseLine(line);
	            }    
	            bufferedReader.close();            
	        }
	        catch(FileNotFoundException ex) {
	            //TODO fix this so it works with main plugin
	        	plugin.getLogger().log(Level.WARNING, "File not found, so no data about people who accepted the TOS before was created");
	        }
	        catch(IOException ex) {
	        	plugin.getLogger().log(Level.WARNING, "Error reading file '" + logFile.getName() + "'");
	            ex.printStackTrace();
	        }
	}
	
	public void parseLine(String line) {
		String [] values = line.split(":");
		if (values.length!=2) {
			plugin.getLogger().log(Level.WARNING, "Found invalid line while loading registered players: "+line); //TODO
			return;
		}
		UUID playerUUID = UUID.fromString(values[0]);
		Long time = Long.parseLong(values[1]);
		if (registeredPlayers!=null) {
			registeredPlayers.put(playerUUID,time);
		}
	}
	
	public static boolean addPlayer(Player p) {
		if (p==null) {
			return false;
		}
		UUID playerUUID= p.getUniqueId();
		Long time = System.currentTimeMillis();
		if (registeredPlayers != null) {
			registeredPlayers.put(playerUUID,time);
		} else {
			return false;
		}
		
		//write the name to logfile right away
		writeToFile(playerUUID.toString()+":"+time.toString(),logFile);
		
		return true;
	}

	public static void writeToFile(String content, File file) {
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter(file));
		    writer.write(content+"\n");

		}
		catch ( IOException e)
		{
			//TODO log something
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    	//TODO log something
		    }
		}
	}
}
