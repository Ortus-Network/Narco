package com.ortus.narco.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.ortus.narco.Narco;
import com.ortus.narco.utils.Drug;
import com.ortus.narco.utils.Lang;
import com.ortus.narco.utils.Wrapper;

public class EventPlayerInteract implements Listener {

	FileConfiguration config = Narco.getPlugin().getConfig();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK || !player.isSneaking()) {
			return;
		}

		if (!config.getBoolean("enabled") || config.getConfigurationSection("items." + item.getType().name()) == null) {
			return;
		}
    	
		Drug drug = new Drug(event.getItem());
		
    	if(Wrapper.remove(player.getInventory(), item.getType(), 1, item.getDurability()) != 1) {
    		Narco.getPlugin().getLogger().severe(Lang.INV_MISSING.toString());
    		return;
    	}
    	
    	player.updateInventory();
		
		drug.applyDrug(player);
		
		event.setCancelled(true);
	}
}
