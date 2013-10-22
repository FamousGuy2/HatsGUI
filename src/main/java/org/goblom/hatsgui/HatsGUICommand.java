package org.goblom.hatsgui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Goblom
 */
public class HatsGUICommand implements CommandExecutor {

    private HatsGUI plugin;
    public HatsGUICommand(HatsGUI instance) { plugin = instance; }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("hatsgui")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("Hats.Use")) {
                    plugin.hatsGUI().open(player);
                    return true;
                }
            } else sender.sendMessage("Only players can do this!");
            return false;
        }
        return false;
    }
}
