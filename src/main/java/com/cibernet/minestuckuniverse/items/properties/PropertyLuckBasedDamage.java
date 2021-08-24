package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PropertyLuckBasedDamage extends WeaponProperty
{
	float multiplier;

	public PropertyLuckBasedDamage(float multiplier)
	{
		this.multiplier = multiplier;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount) {
		return player instanceof EntityPlayer ? amount + amount*((EntityPlayer) player).getLuck()*multiplier : amount;
	}
}
