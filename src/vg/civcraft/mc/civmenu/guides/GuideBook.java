package vg.civcraft.mc.civmenu.guides;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class GuideBook {
	
	private String guideName;
	private ArrayList<String> pages;
	private static HashMap<String, GuideBook> books = new HashMap<String, GuideBook>();

	public GuideBook(String guideName) {
		this.guideName = guideName;
		pages = new ArrayList<String>();
	}
	
	public String getName() {
		return guideName;
	}
	
	public boolean addPage(String page) {
		if(page == null) {
			return false;
		} else if(page.isEmpty()) {
			return false;
		} else {
			pages.add(parsePage(page));
			return true;
		}
	}
	
	public void addPages(List<String> pages){
		if (pages == null){return;}
		
		for (String page: pages){
			this.addPage(parsePage(page));
		}
	}
	
	public ArrayList<String> getPages(){
		return pages;
	}
	
	public void clearPages(){
		pages.clear();
	}
	
	public ItemStack makeBookItem(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookdat = (BookMeta)book.getItemMeta();
		
		bookdat.setDisplayName(ChatColor.AQUA + guideName);
		bookdat.setAuthor("CivGuide");
		if (pages.isEmpty()){
			bookdat.addPage(ChatColor.DARK_RED+"     *Book is Empty*");
		} else { 
			for (String page : pages){
				bookdat.addPage(page);
			}
		}
		book.setItemMeta(bookdat);
		return book;
	}
	
	private String parsePage(String page){
		String ret = page;
		ret = ret.replaceAll("~n", "\n");
		ret = ret.replaceAll("~q", "\"");
		ret = ret.replaceAll("~s", ChatColor.STRIKETHROUGH.toString());
		ret = ret.replaceAll("~b", ChatColor.BOLD.toString());
		ret = ret.replaceAll("~i", ChatColor.ITALIC.toString());
		ret = ret.replaceAll("~u", ChatColor.UNDERLINE.toString());
		ret = ret.replaceAll("~r", ChatColor.RESET.toString());
		ret = ret.replaceAll("~c:a", ChatColor.AQUA.toString());
		ret = ret.replaceAll("~c:bl", ChatColor.BLUE.toString());
		ret = ret.replaceAll("~c:b", ChatColor.BLACK.toString());
		ret = ret.replaceAll("~c:m", ChatColor.MAGIC.toString());
		ret = ret.replaceAll("~c:da", ChatColor.DARK_AQUA.toString());
		ret = ret.replaceAll("~c:db", ChatColor.DARK_BLUE.toString());
		ret = ret.replaceAll("~c:dgr", ChatColor.DARK_GREEN.toString());
		ret = ret.replaceAll("~c:dg", ChatColor.DARK_GRAY.toString());
		ret = ret.replaceAll("~c:dp", ChatColor.DARK_PURPLE.toString());
		ret = ret.replaceAll("~c:dr", ChatColor.DARK_RED.toString());
		ret = ret.replaceAll("~c:go", ChatColor.GOLD.toString());
		ret = ret.replaceAll("~c:gr", ChatColor.GREEN.toString());
		ret = ret.replaceAll("~c:g", ChatColor.GRAY.toString());
		ret = ret.replaceAll("~c:lp", ChatColor.LIGHT_PURPLE.toString());
		ret = ret.replaceAll("~c:r", ChatColor.RED.toString());
		ret = ret.replaceAll("~c:w", ChatColor.WHITE.toString());
		ret = ret.replaceAll("~c:y", ChatColor.YELLOW.toString());
		return ret;
	}
	
	public static void addBook(GuideBook book) {
		books.put(book.getName(), book);
	}
	
	public static GuideBook getBook(String bookName) {
		return books.get(bookName);
	}
	
	public static boolean hasBook(String bookName, Player player) {
		for(ItemStack is : player.getInventory().getContents()) {
			if(is != null  && is.getType() == Material.WRITTEN_BOOK) {
				if(is.getItemMeta().getDisplayName().equalsIgnoreCase(bookName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void giveBook(String bookName, Player player) {
		if(!books.keySet().contains(bookName)) {
			player.sendMessage(ChatColor.RED + bookName + " is not a GuideBook");
			return;
		}
		
		ItemStack inHand = player.getItemInHand();
		if(!(inHand.getType() == Material.AIR)) {
			player.sendMessage(ChatColor.RED + "Your hand needs to be empty to receive GuidBooks");
		} else {
			player.setItemInHand(books.get(bookName).makeBookItem());
		}
	}
	
	public static void clearBooks() {
		books.clear();
	}
}
