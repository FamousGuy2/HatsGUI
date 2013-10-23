package org.goblom.hatsgui.util;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public abstract class EnchantmentCustom extends Enchantment {
 
    public EnchantmentCustom(int id) {
        super(id);
        if (id >= 256) throw new IllegalArgumentException("A enchantment id has to be lower then 256!");
 
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
               
            boolean b = f.getBoolean(null);
            f.set(null, Boolean.valueOf(true));
   
            Enchantment.registerEnchantment(this);
            f.set(null, b);
        } catch (Exception e) { e.printStackTrace(); }
    }
 
    public abstract boolean canEnchantItem(ItemStack itemstack);
    public abstract boolean conflictsWith(Enchantment enchantment);
    public abstract EnchantmentTarget getItemTarget();
    public abstract int getMaxLevel();
    public abstract int getStartLevel();
    public abstract int getWeight();
 
    @Override
    public String getName() { return "Usages" + this.getId(); }
}