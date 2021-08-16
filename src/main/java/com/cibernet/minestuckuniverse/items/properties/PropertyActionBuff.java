package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PropertyActionBuff extends PropertyAbstractClaw
{
	int actionTime;
	double actionMultiplier;

	public PropertyActionBuff(int actionTime, double actionMultiplier)
	{
		this.actionTime = actionTime;
		this.actionMultiplier = actionMultiplier;
	}

	@Override
	public double getAttackDamage(ItemStack stack, double dmg)
	{
		if(stack.hasTagCompound())
			dmg *= actionMultiplier*stack.getTagCompound().getInteger("ActionTime")/(double)actionTime;

		return dmg;
	}

	@Override
	public void onEntityItemUpdate(EntityItem entityItem) {
		ItemStack stack = entityItem.getItem();

		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("ActionTime"))
			stack.getTagCompound().removeTag("ActionTime");

	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(stack.hasTagCompound())
			stack.getTagCompound().setInteger("ActionTime", Math.max(0, stack.getTagCompound().getInteger("ActionTime")-1));
	}

	@Override
	public void onStateChange(EntityPlayer player, ItemStack stack, boolean draw)
	{
		if(draw)
		{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("ActionTime", actionTime);
		}
	}
}
