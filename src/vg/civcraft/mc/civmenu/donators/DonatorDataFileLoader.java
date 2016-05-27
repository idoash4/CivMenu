package vg.civcraft.mc.civmenu.donators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import vg.civcraft.mc.civmenu.CivMenu;

public class DonatorDataFileLoader {

	public static void loadDataAndInitEverything() {
		String filePath = CivMenu.getInstance().getDataFolder()
				.getAbsolutePath()
				+ File.separatorChar + "donators.txt";
		List<String> fileContent = loadFromFile(filePath);
		if (fileContent == null) {
			CivMenu.getInstance().severe("Failed to load donator data");
			return;
		}
		Map <String, Integer> donator = new HashMap<String, Integer>();
		for (int i = 0; i < fileContent.size(); i++) {
			String name = fileContent.get(i);
			if (name.equals("")) {
				continue;
			}
			int value;
			try {
				value = Integer.valueOf(fileContent.get(i + 1));
			} catch (NumberFormatException e) {
				CivMenu.getInstance().warning("Failed to parse " + fileContent.get(i + 1) + "as donator value, skipping it");
				continue;
			}
			catch (IndexOutOfBoundsException e) {
				CivMenu.getInstance().warning("Found no value specified for " + name + "at end of file, skipping it");
				continue;
			}
			donator.put(name, value);
		}
		Bukkit.getPluginManager().registerEvents(new DonatorListener(new TitleNotifier(new DonatorManager(donator))), CivMenu.getInstance());
	}

	public static List<String> loadFromFile(String path) {
		BufferedReader br = null;
		List<String> result = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(path));
			String line = br.readLine();

			while (line != null) {
				result.add(line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			CivMenu.getInstance()
					.severe("Could not find file containing donator information. Failed to init donator displaying");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			CivMenu.getInstance().severe("Failed to init donator displaying");
			e.printStackTrace();
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				CivMenu.getInstance().severe(
						"Failed to close stream loading donator data?");
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}
}
