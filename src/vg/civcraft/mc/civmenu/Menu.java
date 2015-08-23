package vg.civcraft.mc.civmenu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Menu {
	TextComponent title;
	TextComponent subTitle;
	List<TextComponent> parts;
	
	public Menu(){
		title = new TextComponent();
		subTitle = new TextComponent();
		parts = new ArrayList<TextComponent>();
	}
	
	public TextComponent getTitle() {
		return title;
	}

	public void setTitle(TextComponent title) {
		this.title = title;
	}

	public TextComponent getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(TextComponent subTitle) {
		this.subTitle = subTitle;
	}

	public List<TextComponent> getParts() {
		return parts;
	}

	public void setParts(TextComponent... parts){
		for(TextComponent part:parts){
			this.parts.add(part);
		}
	}
	
	
	public void setParts(List<HelpTopic> parts) {
		for (HelpTopic ht : parts) {
			TextComponent text = new TextComponent(ht.getName());
			TextComponent[] tc = { new TextComponent(ht.getShortText()) };
			text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tc));
			text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ht.getName()));
			this.parts.add(text);
		}
	}


	
	public TextComponent create(){
		TextComponent menu = new TextComponent();
		menu.addExtra(title);
		menu.addExtra(subTitle);
		for(TextComponent text: parts){
			menu.addExtra(text);
		}
		return menu;
	}
}
