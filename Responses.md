#Adding responses to your plugin using CivMenu's ResponseManager

Basically just implement as the example below shows. Make sure each event is configured in your config.yml. 

ExamplePlugin main class:
``` java
public class ExamplePlugin extends JavaPlugin implements Listener {
  private ResponseManager rm;
  
  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    saveDefaultConfig();
    reloadConfig();
    //DO THIS AFTER YOU INITIALIZE YOUR CONFIG!!!
    if(getServer().getPluginManager().isPluginEnabled("CivMenu")) {
      rm = ResponseManager.getResponseManager(this);
    }
  }
  
  @EventHandler
  public void onSomeBukkitEvent(SomeBukkitEvent event) {
    if(rm != null) {
      rm.sendMessageForEvent(event.getEventName(), event.getPlayer());
    }
  }
}
```
When configuring events, you can specify text to show the player (the basic description such as "you can use pearls to imprison people"), an event specific documentation url which will override the documentation url if it was set for the plugin, and a book to prompt the users with. If you include a book with the plugin's name in your config it will load that book by default

Config for ExamplePlugin:
``` yml
CivMenu:
  events:
    SomeBukkitEvent:
      text: Text to send to the user on the SomeBukkitEvent
      url: http://documentationurl.com/SomeBukkitEvent
      book: ham
  url: http://documentationurl.com
  books:
    ham:
      fullname: "Green Eggs & Ham"
      pages:
        - "~iI do not like ~c:grgreen ~c:beggs and ham..."
        - "~iI do not like them.."
        - "~i~u~c:rSam~c:b-~c:dgI~c:b-~c:gAm."
# Page code reference for books:
#-----------------------
# ~n - New Line
# ~q - Insert Quotation "
# ~s - Strikethrough
# ~b - Bold
# ~i - Italic
# ~u - Underline
# ~r - Reset/remove formatting
# -- Coloring:
# ~c:m - Magic
# ~c:a - Aqua
# ~c:b - Black
# ~c:bl - Blue
# ~c:da - Dark Aqua
# ~c:db - Dark Blue
# ~c:dg - Dark Grey
# ~c:dgr - Dark Green
# ~c:dp - Dark Purple
# ~c:dr - Dark Red
# ~c:go - Gold
# ~c:g - Gray
# ~c:gr - Green
# ~c:lp - Light Purple
# ~c:r - Red
# ~c:w - White
# ~c:y - Yellow
#-----------------------
```
ExamplePlugin plugin.yml:
``` yml
name: ExamplePlugin
main: com.example.ExamplePlugin
version: 1.0
softdepend: [CivMenu]
```

Event names can also be a cusom event name rather than using the bukkit event. So if you want the same response to be triggered by multipl events, for example picking up an item being triggered by PlayerPickupItemEvent and InventoryClickEvent, you could configure it as:
```yml
CivMenu:
  events:
    SomeItemPickupEvent:
      text: you picked up some item or something
```

```java
@EventHandler
public void onPlayerPickupItem(PlayerPickupItemEvent event) {
  rm.sendMessageForEvent("SomeItemPickupEvent", event.getPlayer());
}

@EventHandler
public void onInventoryClick(InventoryClickEvent event) {
  rm.sendMessageForEvent("SomeItemPickupEvent", Bukkit.getPlayer(event.getWhoClicked().getUniqueId()));
}
```
