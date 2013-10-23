package org.goblom.hatsgui.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EnchantmentCustomData extends EnchantmentCustom {
	 
    public EnchantmentCustomData(int id) { super(id); }
    @Override
    public boolean canEnchantItem(ItemStack itemstack) { return false; }
    @Override
    public boolean conflictsWith(Enchantment enchantment) { return false; }
    @Override
    public EnchantmentTarget getItemTarget() { return EnchantmentTarget.ALL; }
    @Override
    public int getMaxLevel() { return 1; }
    @Override
    public int getStartLevel() { return 1; }
    @Override
    public int getWeight() { return 1000; }
}