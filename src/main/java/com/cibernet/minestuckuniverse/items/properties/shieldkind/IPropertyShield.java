package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public interface IPropertyShield
{
	default void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{

	}

	default boolean onShieldParry(ItemStack stack, EntityLivingBase player, DamageSource source, float damage)
	{
		return true;
	}

	default boolean isShielding(ItemStack stack, EntityLivingBase player)
	{
		return true;
	}
}
