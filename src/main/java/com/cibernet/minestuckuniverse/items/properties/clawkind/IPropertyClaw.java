package com.cibernet.minestuckuniverse.items.properties.clawkind;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPropertyClaw
{
	default void onStateChange(EntityPlayer player, ItemStack stack, boolean draw)
	{

	}
}
