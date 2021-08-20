package com.cibernet.minestuckuniverse.items.properties.clawkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PropertyActionBuff extends WeaponProperty implements IPropertyClaw
{
	int actionTime;
	double actionMultiplier;

	public PropertyActionBuff(int actionTime, double actionMultiplier)
	{
		this.actionTime = actionTime;
		this.actionMultiplier = actionMultiplier;
	}

	@Override
	public double getAttackDamage(ItemStack stack, double parent)
	{
		if(stack.hasTagCompound())
			return parent* Math.max(1,actionMultiplier*(double)stack.getTagCompound().getInteger("ActionTime")/(double)actionTime);
		return parent;
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


	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		ItemStack stackA = oldStack.copy();
		ItemStack stackB = newStack.copy();

		if(!stackA.hasTagCompound())
			stackA.setTagCompound(new NBTTagCompound());
		if(!stackB.hasTagCompound())
			stackB.setTagCompound(new NBTTagCompound());

		stackA.getTagCompound().setInteger("ActionTime", 0);
		stackB.getTagCompound().setInteger("ActionTime", 0);

		return !ItemStack.areItemStacksEqual(stackA, stackB);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		ItemStack stackA = oldStack.copy();
		ItemStack stackB = newStack.copy();

		if(!stackA.hasTagCompound())
			stackA.setTagCompound(new NBTTagCompound());
		if(!stackB.hasTagCompound())
			stackB.setTagCompound(new NBTTagCompound());

		stackA.getTagCompound().setInteger("ActionTime", 0);
		stackB.getTagCompound().setInteger("ActionTime", 0);

		return super.shouldCauseBlockBreakReset(stackA, stackB);
	}
}
