package com.ortus.narco.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.ortus.narco.Narco;

import net.md_5.bungee.api.ChatColor;

public class EventInventoryClick implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		String invName = Narco.getPlugin().getConfig().getString("inventory-name");
		
		if(!(event.getWhoClicked() instanceof Player)) return;
		
		if(event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', invName))) {
			event.setCancelled(true);
		}
	}
	
}
