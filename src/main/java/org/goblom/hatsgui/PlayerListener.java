package org.goblom.hatsgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Goblom
 */
public class PlayerListener implements Listener {
    
    private HatsGUI plugin;
    public PlayerListener(HatsGUI instance) { plugin = instance; }
    
    private Map<Player, Integer> dropAttempts = new HashMap();
    
    private ItemStack guiBlock() {
        List<String> lore = new ArrayList<String>();
        ItemStack guiBlock;
        if (plugin.getConfig().contains("Settings.Item.Data-Value") && plugin.getConfig().getString("Settings.Item.Data-Value") != null) {
             guiBlock = new ItemStack(Material.valueOf(plugin.getConfig().getString("Settings.Item.Material")), 1, (short) plugin.getConfig().getInt("Settings.Item.Data-Value"));
        } else guiBlock = new ItemStack(Material.valueOf(plugin.getConfig().getString("Settings.Item.Material")), 1);
        ItemMeta guiBlockMeta = guiBlock.getItemMeta();
        guiBlockMeta.setDisplayName("Settings.Item.Name");
        if (plugin.getConfig().contains("Settings.Item.Lore")) {
            if (plugin.getConfig().isList("Settings.Item.Lore")) {
                for (String string : plugin.getConfig().getStringList("Settings.Item.Lore")) {
                    lore.add(string);
                }
            } else if (!plugin.getConfig().getString("Settings.Item.Lore").equalsIgnoreCase("")) lore.add(plugin.getConfig().getString("Settings.Item.Lore"));
            guiBlockMeta.setLore(lore);
        }
        guiBlock.setItemMeta(guiBlockMeta);

        if (plugin.getConfig().getBoolean("Settings.Item.Glow")) guiBlock.addUnsafeEnchantment(plugin.getUtil().getKeys(), 1);
        return guiBlock;
    }
    
    @EventHandler
    private void onItemClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!player.hasPermission("Hats.Use")) return;
        if (player.hasPermission("Hats.Use")) {
            if (event.getCurrentItem().equals(guiBlock())) {
                player.closeInventory();
                player.setItemOnCursor(null);
                plugin.hatsGUI().open(player);
            }
        }
    }
    
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
       Player player = event.getPlayer();
       if (player.getInventory().contains(guiBlock())) player.getInventory().remove(guiBlock());
       if (dropAttempts.containsKey(player)) dropAttempts.remove(player);
    }
    
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("Settings.Give-On-Login")) {
            if (player.hasPermission("Hats.Use")) {
                dropAttempts.put(player, 0);
                if (!player.getInventory().contains(guiBlock())) player.getInventory().addItem(guiBlock());
                else player.sendMessage(
                        ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "HatsGUI" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.GOLD + "You already have the GUI block in your inventory."
                        );
            }
        } else {
            dropAttempts.put(player, 0);
            if (!player.getInventory().contains(guiBlock())) player.getInventory().addItem(guiBlock());
            else player.sendMessage(
                        ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "HatsGUI" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.GOLD + "You already have the GUI block in your inventory."
                        );
        }
    }
    
    @EventHandler
    private void onGUIBlockDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        
        if(!dropAttempts.containsKey(player)) return;
        if (event.getItemDrop().getItemStack().equals(guiBlock())) {
            if (dropAttempts.get(player) == 0) {
                event.setCancelled(true);
                player.sendMessage(
                        ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "HatsGUI" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.GOLD + "We have noted that you want to drop the Hats GUI block. Drop it again to remove it."
                        );
                {
                    dropAttempts.remove(player);
                    dropAttempts.put(player, 1);
                }
            } else if (dropAttempts.get(player) == 1) {
                event.setCancelled(true);
                player.sendMessage(
                        ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "HatsGUI" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.GOLD + "The Hats GUI has been removed. If you still wish to open the Hats GUI you can do '/hatsgui'."
                        );
                {
                    player.getInventory().remove(guiBlock());
                    dropAttempts.remove(player);
                }
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    private void onGUIBlockPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (dropAttempts.containsKey(player)) {
            if (event.getItem().getItemStack().equals(guiBlock())) {
                event.setCancelled(true);
                player.sendMessage(
                            ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "HatsGUI" + ChatColor.DARK_GRAY + "] " +
                            ChatColor.GOLD + "You still have your Hats GUI. Please remove it before you pickup another."
                            );
            }
        } else if (!dropAttempts.containsKey(player)) {
            if (event.getItem().getItemStack().equals(guiBlock())) {
                dropAttempts.put(player, 0);
                player.sendMessage(
                            ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "HatsGUI" + ChatColor.DARK_GRAY + "] " +
                            ChatColor.GOLD + "You have just picked up the Hats GUI block."
                            );
            }
        }
        
    }
}
