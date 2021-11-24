package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class PropertyVMotionDamage extends WeaponProperty
{
	float multiplier;
	float max;

	public PropertyVMotionDamage(float multiplier, float max)
	{
		this.multiplier = multiplier;
		this.max = max;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		return super.damageAgainstEntity(stack, player, target, amount) * Math.min(max, (float)-Math.min(-1, (player.motionY-0.5)*multiplier));
	}
}
