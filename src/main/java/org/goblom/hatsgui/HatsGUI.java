
package org.goblom.hatsgui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Goblom
 */
public class HatsGUI extends JavaPlugin {

    private HatsGUI instance;
    private JavaPlugin plugin;
    private IconMenu iconMenu;
    
    public void onEnable() {
        instance = this;
        plugin = this;
        setupGUI();
        
        getCommand("hatsgui").setExecutor(new HatsGUICommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), plugin);
    }
    
    public void onDisable() {
        
    }
    
    public HatsGUI getInstance() {
        return instance;
    }
    
    public JavaPlugin getPlugin() {
        return plugin;
    }
    
    private String getGUITitle() {
        String title = null;
        
        if (
                !(getConfig().contains("GUI.Name")) || 
                (getConfig().getString("GUI.Name").equals("")) || 
                (getConfig().getString("GUI.Name") == null)) 
            {
                title = "Hats GUI";
            } else title = getConfig().getString("GUI.Name");
        
        return title;
    }
    
    private int getSlots() {
        if (getConfig().contains("GUI.Rows")) {
            if (getConfig().getInt("GUI.Rows") >= 1 && getConfig().getInt("GUI.Rows") <= 7) { 
                return getConfig().getInt("GUI.Rows") * 9;
            } else {
                plugin.getLogger().severe("Rows MUST be greater then 1 & less then 7.");
                plugin.getLogger().severe("Using default chest size for GUI. (I sure hope you dont have any items above slot # 27");
                return 27;
            }
        }
        return 9;
    }
    public void setupGUI() {
        iconMenu = new IconMenu(
                getGUITitle(),
                getSlots(),
                new IconMenu.OptionClickEventHandler() {
                    @Override
                    public void onOptionClick(IconMenu.OptionClickEvent event) {
                        event.getPlayer().getInventory().setHelmet(getHat(event.getName()));
                        event.setWillClose(true);
                    }
                }, plugin);
        
        for (String item : getConfig().getConfigurationSection("Items").getKeys(false)) {
            if (getConfig().contains("Items." + item + ".Lore")) {
                List<String> loreConfig = getConfig().getStringList("Items." + item + ".Lore");
                String[] lore = loreConfig.toArray(new String[loreConfig.size()]);
                
                iconMenu.setOption(
                        Integer.valueOf(getConfig().getString("Items." + item + ".Slot")) - 1, // -1 to make it align correctly
                        new ItemStack(Material.valueOf(getConfig().getString("Items." + item + ".Material")), 1), 
                        getConfig().getString("Items." + item + ".Name"),
                        lore);
            } else {
                iconMenu.setOption(
                        Integer.valueOf(getConfig().getString("Items." + item + ".Slot")) - 1, // -1 to make it align correctly
                        new ItemStack(Material.valueOf(getConfig().getString("Items." + item + ".Material")), 1), 
                        getConfig().getString("Items." + item + ".Name"));
            }
        }
    }
    
    public IconMenu hatsGUI() { return iconMenu; }
    public void reloadGUI() { destroyGUI(); setupGUI(); }
    public void destroyGUI() { hatsGUI().destroy(); }
    

    private ItemStack getHat(String name) {
        ItemStack hat;
        ItemMeta hatMeta;
        List<String> lore = new ArrayList<String>();
        
        for (String item : getConfig().getConfigurationSection("Items").getKeys(false)) {
            if (getConfig().getString("Items." + item + ".Name").equalsIgnoreCase(name)) {
                hat = new ItemStack(Material.valueOf("Items." + item + ".Material"), 1);
                hatMeta = hat.getItemMeta();
                
                hatMeta.setDisplayName("Items." + item + ".Name");
                if (getConfig().contains("Items." + item + ".Lore")) {
                    for (String string : getConfig().getStringList("Items." + item + ".Lore")) lore.add(string);
                }
                
                hatMeta.setLore(lore);
                hat.setItemMeta(hatMeta);
                
                return hat;
            }
        }
        
        return new ItemStack(Material.AIR, 1);
    }
}

