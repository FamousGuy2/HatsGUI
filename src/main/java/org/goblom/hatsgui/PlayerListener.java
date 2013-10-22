package org.goblom.hatsgui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Goblom
 */
public class PlayerListener implements Listener {
    
    private HatsGUI plugin;
    public PlayerListener(HatsGUI instance) { plugin = instance; }
    
    @EventHandler
    private void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType().equals(Material.valueOf(plugin.getConfig().getString("Open-GUI-Block")))) {
                if (!player.hasPermission("Hats.Use")) return;
                if (player.hasPermission("Hats.Use")) {
                    plugin.hatsGUI().open(player);
                }
            }
        }
    }
}
