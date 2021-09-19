package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PropertyTipperDamage extends WeaponProperty
{
	float minPctg;
	float maxPctg;
	float optimalDistance;

	public PropertyTipperDamage(float min, float max, float tip)
	{
		minPctg = min;
		maxPctg = max;
		optimalDistance = tip;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE) == null ? EntityPlayer.REACH_DISTANCE.getDefaultValue() : player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		return amount*(minPctg + (maxPctg-minPctg) * (float)(1 - Math.abs(player.getDistance(target)/reach - optimalDistance)));
	}
}
