
package org.goblom.hatsgui.util;

import java.text.SimpleDateFormat;
import org.bukkit.Location;
import org.goblom.hatsgui.HatsGUI;
/**
 *
 * @author Goblom
 */
public class Util {

    private HatsGUI plugin;
    private final EnchantmentCustomData KEYS = new EnchantmentCustomData(100);
    
    public Util(HatsGUI instance) { plugin = instance; }
    public EnchantmentCustomData getKeys() { return KEYS; }
    public SimpleDateFormat getSDF() { return new SimpleDateFormat("MM/dd/yyyy"); }
    
    public String timeUntil(Long sec) {
        Integer buf;
        if (sec < 60*2) {
            buf = Math.round(sec);
            return buf + " second" + (buf==1?"":"s"); 
        }
        if (sec < 3600*2) { 
            buf = Math.round(sec/60);
            return buf + " minute" + (buf==1?"":"s"); 
        }
        if (sec < 86400*2) { 
            buf = Math.round(sec/3600);
            return buf + " hour" + (buf==1?"":"s"); 
        }
        buf = Math.round(sec/86400);
        return buf + " day" + (buf==1?"":"s"); 
    }
    
}
