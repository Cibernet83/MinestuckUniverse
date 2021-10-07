package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PropertyDaytimeDamage extends WeaponProperty
{
	boolean daytime;
	float multiplier;

	public PropertyDaytimeDamage(boolean daytime, float multiplier)
	{
		this.daytime = daytime;
		this.multiplier = multiplier;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setBoolean("DaytimeBoosted", daytime == worldIn.isDaytime());
	}

	@Override
	public double getAttackDamage(ItemStack stack, double dmg)
	{
		return dmg * (stack.hasTagCompound() && stack.getTagCompound().getBoolean("DaytimeBoosted") ? multiplier : 1);
	}
}
