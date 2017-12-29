package com.ortus.narco.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
* An enum for requesting strings from the language file.
* @author gomeow
*/
public enum Lang {
    PREFIX("prefix", "&7[&cNarco&7] &r"),
    DRUG_APPLIED("drug-applied", "&7You've just done some %drug%&r&7!"),
    CMD_PLAYER_ONLY("cmd-player-only", "Sorry, but that command can only be run by a player!"),
    NO_PERMS("no-permissions", "&cYou don''t have permission for that!"),
    INV_MISSING("", "");
 
    private String path;
    private String def;
    private static YamlConfiguration LANG;
 
    /**
    * Lang enum constructor.
    * @param path The string path.
    * @param start The default string.
    */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }
 
    /**
    * Set the {@code YamlConfiguration} to use.
    * @param config The config to set.
    */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }
 
    @Override
    public String toString() {
    	String value = LANG.getString(this.path, def);
    	
        if (this == PREFIX)
            return ChatColor.translateAlternateColorCodes('&', value) + " ";
        return ChatColor.translateAlternateColorCodes('&', value);
    }
 
    /**
    * Get the default value of the path.
    * @return The default value of the path.
    */
    public String getDefault() {
        return this.def;
    }
 
    /**
    * Get the path to the string.
    * @return The path to the string.
    */
    public String getPath() {
        return this.path;
    }
}
 