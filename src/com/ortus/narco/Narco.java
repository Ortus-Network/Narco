package com.ortus.narco;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.ortus.narco.command.Commands;
import com.ortus.narco.events.EventInventoryClick;
import com.ortus.narco.events.EventPlayerInteract;
import com.ortus.narco.utils.Lang;

import net.minelink.ctplus.CombatTagPlus;

public class Narco extends JavaPlugin {

	private static Narco plugin;

	/*
	 * Configuration Files
	 */

	// Config.yml
	FileConfiguration config = getConfig();

	// Language.yml
	public static YamlConfiguration LANG;
	public static File LANG_FILE;

	// Dependencies
	public static CombatTagPlus ct;

	public void onEnable() {
		plugin = this;

		getServer().getPluginManager().registerEvents(new EventPlayerInteract(), this);
		getServer().getPluginManager().registerEvents(new EventInventoryClick(), this);

		loadConfig();
		saveConfig();

		loadLang();

		new Commands();

		if (this.getServer().getPluginManager().getPlugin("CombatTagPlus") == null) {
			getLogger().warning("CombatTagPlus was not detected and remains inactive.");
		}

		getLogger().info("Enabled");
	}

	public void onDisable() {
		plugin = null;
		getLogger().info("Disabled");
	}

	public void loadConfig() {
		if (getDataFolder().exists())
			return;

		List<String> meta = new ArrayList<>();
		meta.add("&a&lShift & Right-Click");

		config.addDefault("enabled", true);
		config.addDefault("inventory-name", "Narco");
		config.addDefault("inventory-rows", 1);
		config.addDefault("item-meta", meta);
		config.addDefault("items.WHEAT.name", "Weed");
		config.addDefault("items.WHEAT.price", 500.0);
		config.addDefault("items.WHEAT.data-id", 0);
		config.addDefault("items.WHEAT.color", "DARK_GREEN");
		config.addDefault("items.WHEAT.message",
				"You have just smoked some weed. Shit is about to get trippy, {PLAYER}!");
		config.addDefault("items.WHEAT.effects.JUMP.duration", 10);
		config.addDefault("items.WHEAT.effects.JUMP.amplifier", 1);

		config.options().copyDefaults(true);
	}
	
	/**
	 * Load the language.yml file.
	 * 
	 * @return The language.yml config.
	 */
	public void loadLang() {
		File lang = new File(getDataFolder(), "language.yml");

		if (!lang.exists()) {
			try {
				getDataFolder().mkdir();
				lang.createNewFile();

				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
				defConfig.save(lang);
				Lang.setFile(defConfig);
				
			} catch (IOException e) {
				e.printStackTrace(); // So they notice
				getLogger().severe("Couldn't create language file.");
				getLogger().severe("This is a fatal error. Now disabling");
				this.setEnabled(false); // Without it loaded, we can't send them messages
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);

		for (Lang item : Lang.values()) {
			if (conf.getString(item.getPath()) == null) {
				conf.set(item.getPath(), item.getDefault());
			}
		}
		
		Lang.setFile(conf);
		LANG = conf;
		LANG_FILE = lang;

		try {
			conf.save(getLangFile());
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "Failed to save language.yml.");
			getLogger().log(Level.WARNING, "Report this stack trace to the developer.");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the language.yml config.
	 * 
	 * @return The language.yml config.
	 */
	public YamlConfiguration getLang() {
		return LANG;
	}

	/**
	 * Get the language.yml file.
	 * 
	 * @return The language.yml file.
	 */
	public File getLangFile() {
		return LANG_FILE;
	}

	/*
	 * Provide a way to get methods without init
	 * 
	 * @return instance of this class
	 */
	public static Narco getPlugin() {
		return plugin;
	}

}
