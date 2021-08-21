package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class PropertyDaytimeBasedDamage extends WeaponProperty
{
	float multiplier;
	boolean onDaytime;

	public PropertyDaytimeBasedDamage(float multiplier, boolean onDaytime)
	{
		this.multiplier = multiplier;
		this.onDaytime = onDaytime;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		return amount * (player.world.isDaytime() == onDaytime ? multiplier : 1);
	}
}
