package com.ortus.narco.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ortus.narco.Narco;

public class Drug {

	private Narco plugin;
	
	ItemStack item;
	
	NumberFormat numFormat = NumberFormat.getNumberInstance(Locale.US);
	
	public Drug(ItemStack item) {
		this.plugin = Narco.getPlugin();
		this.item = item;

		numFormat.setMinimumFractionDigits(2);
		
		if(plugin.getConfig().getConfigurationSection("items." + item.getType().name()) == null) {
			plugin.getLogger().severe(ExceptionTypes.EFFECT_NOT_FOUND.getException(item.getType().name()));
			return;
		}
	}
	
	public ConfigurationSection getSection() {
		return plugin.getConfig().getConfigurationSection("items." + item.getType().name());
	}
	
	public ConfigurationSection getEffectSection() {
		return plugin.getConfig().getConfigurationSection("items." + item.getType().name() + ".effects");
	}
	
	/*
	 * Drug Attributes
	 */
	
	public String getName() {
		return this.getSection().getString("name");
	}
	
	public double getPrice() {
		return this.getSection().getDouble("price");
	}
	
	public ChatColor getColor() {
		return ChatColor.valueOf(getSection().getString("color"));
	}
	
	public String getMessage() {
		return this.getSection().getString("message");
	}
	
	public void sendMessage(Player player) {
		player.sendMessage(this.getMessage());
	}
	
	public void applyDrug(Player player) {
//		this.removeDrug(player);
    	
		this.applyEffects(player);
		
		player.sendMessage(Lang.DRUG_APPLIED.toString().replace("%drug%", this.getColor() + this.getName()));
	}
	
	/*
	 * Utilities
	 */
	
    public int removeDrug(Player player)
    {
        ItemStack[] contents = player.getInventory().getContents();
        int removed = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack playerItem = contents[i];
            int amount = 1;
 
            if (playerItem == null || !playerItem.getType().equals(item.getType())) {
                continue;
            }
 
            if (item.getDurability() != (short) -1 && playerItem.getDurability() != item.getDurability()) {
                continue;
            }
 
            int remove = playerItem.getAmount() - amount - removed;
 
            if (removed > 0) {
                removed = 0;
            }
 
            if (remove <= 0) {
                removed += Math.abs(remove);
                contents[i] = null;
            } else {
            	playerItem.setAmount(remove);
            }
        }
        return removed;
    }
    
	public void applyEffects(Player player)
	{
		for(PotionEffect effect : getEffects()) {
			player.addPotionEffect(effect);
		}
	}
	
	/*
	 * Drug Effects
	 */
	
	public List<PotionEffect> getEffects() {
		List<PotionEffect> types = new ArrayList<PotionEffect>();
		
		for(String e : getEffectSection().getKeys(false)) {			
			int amplifier = getEffectSection().getInt("amplifier");
			int duration = getEffectSection().getInt("duration") * 20;
			PotionEffectType type = PotionEffectType.getByName(e);
			
			types.add(new PotionEffect(type, duration, amplifier));
		}
		
		return types;
	}
	
	public static List<ItemStack> getDrugs() {
		List<ItemStack> drugs = new ArrayList<ItemStack>();
		
		for(String str : Narco.getPlugin().getConfig().getConfigurationSection("items").getKeys(false)) {
			Material m = Material.matchMaterial(str);
			ItemStack is = new ItemStack(m);
			drugs.add(is);
		}
		
		return drugs;
	}
	
	/*
	 * Drug Material Shit
	 */
	
	public ItemStack getItem() {
		return new ItemStack(item.getType(), 1, item.getDurability());
	}	
	
	public ItemStack getItemWithAttributes() {
		ItemStack newItem = new ItemStack(item.getType(), 1, item.getDurability());
		ItemMeta meta = newItem.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();

		lore.add(ChatColor.translateAlternateColorCodes('&', "&f&lPrice: &2$" + numFormat.format(this.getSection().getDouble("price")) + "&r/each"));
		
		for(String s : this.getSection().getStringList("item-meta")) {
			lore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.getColor() + this.getName()));
		newItem.setItemMeta(meta);
		return newItem;
	}
	
	public short getDataId() {
		return item.getDurability();
	}

}
