package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public interface IEnchantableProperty
{
	boolean canEnchantWith(ItemStack stack, Enchantment enchantment);
}
