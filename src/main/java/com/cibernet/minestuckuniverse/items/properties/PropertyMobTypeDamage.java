package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;

public class PropertyMobTypeDamage extends WeaponProperty
{
	EnumCreatureAttribute mobType;
	float multiplier;

	public PropertyMobTypeDamage(EnumCreatureAttribute mobType, float multiplier)
	{
		this.mobType = mobType;
		this.multiplier = multiplier;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount) {
		return amount * (target.getCreatureAttribute() == mobType ? multiplier : 1);
	}
}
