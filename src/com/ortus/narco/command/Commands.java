package com.ortus.narco.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ortus.narco.Narco;
import com.ortus.narco.utils.Drug;
import com.ortus.narco.utils.Lang;
import com.ortus.narco.utils.Wrapper;

public class Commands implements CommandExecutor {

	private FileConfiguration config = Narco.getPlugin().getConfig();

	public Commands() {
		for (String command : Narco.getPlugin().getDescription().getCommands().keySet()) {
			Narco.getPlugin().getCommand(command).setExecutor(this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.CMD_PLAYER_ONLY.toString());
			return false;
		}

		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("drugs")) {
			if (args.length == 0) {
				if (!player.hasPermission("narco.cmd.drugs")) {
					player.sendMessage(Lang.NO_PERMS.toString()); // TODO: Replace with a custom no-permission message
				}

				String invName = config.getString("inventory-name");
				int invSize = config.getInt("inventory-rows") * 9;
				
				int i = 0;
				
				Inventory drugInventory = Bukkit.createInventory(null, invSize, ChatColor.translateAlternateColorCodes('&', invName));

				for(String key : config.getConfigurationSection("items").getKeys(false)) {
					Drug drug = new Drug(new ItemStack(Material.matchMaterial(key)));
					drugInventory.setItem(i, drug.getItemWithAttributes());
					i++;
				}

				player.openInventory(drugInventory);
				
			} else if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("help")) {
					if (!player.hasPermission("narco.cmd.drugs.help")) {
						player.sendMessage("§cYou do not have permission.");
						return false;
					}
					
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (!player.hasPermission("narco.cmd.drugs.reload")) {
						player.sendMessage("§cYou do not have permission.");
						return false;
					}
					
					Narco.getPlugin().reloadConfig();
					player.sendMessage(Wrapper.getPrefix() + " §aConfig reloaded.");
				}
			}
		}
		return false;
	}

}
