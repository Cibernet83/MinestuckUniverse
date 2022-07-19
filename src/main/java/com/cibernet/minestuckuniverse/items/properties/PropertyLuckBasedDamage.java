package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PropertyLuckBasedDamage extends WeaponProperty
{
	float multiplier;
	float multMax;

	public PropertyLuckBasedDamage(float multiplier, float multMax)
	{
		this.multiplier = multiplier;
		this.multMax = multMax;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount) {
		return player instanceof EntityPlayer ? Math.max(amount + amount*((EntityPlayer) player).getLuck()*multiplier, multMax) : amount;
	}
}
