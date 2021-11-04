package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PropertyHungerSpeed extends WeaponProperty
{
	float maxMultiplier;

	public PropertyHungerSpeed(float maxMultiplier)
	{
		this.maxMultiplier = maxMultiplier;
	}

	@Override
	public void onEntityItemUpdate(EntityItem entityItem)
	{
		ItemStack stack = entityItem.getItem();
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("Hunger"))
				stack.getTagCompound().removeTag("Hunger");
			if(stack.getTagCompound().hasNoTags())
				stack.setTagCompound(null);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		if(!worldIn.isRemote)
		{
			if (entityIn instanceof EntityPlayer) {
				if (!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());

				int hunger = ((EntityPlayer) entityIn).getFoodStats().getFoodLevel();

				if(stack.getTagCompound().getInteger("Hunger") != hunger)
					stack.getTagCompound().setInteger("Hunger", hunger);

			}
			else if (stack.hasTagCompound())
			{
				if(stack.getTagCompound().hasKey("Hunger"))
					stack.getTagCompound().removeTag("Hunger");
				if(stack.getTagCompound().hasNoTags())
					stack.setTagCompound(null);
			}
		}

		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public double getAttackSpeed(ItemStack stack, double parent)
	{
		if(stack.hasTagCompound())
		{
			double add = ((stack.getTagCompound().getInteger("Hunger")/2f)*parent)/10f*(maxMultiplier-1);
			return parent + add - parent*(maxMultiplier-1);
		}
		return parent;
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

		stackA.getTagCompound().setInteger("Hunger", 0);
		stackB.getTagCompound().setInteger("Hunger", 0);

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

		stackA.getTagCompound().setInteger("Hunger", 0);
		stackB.getTagCompound().setInteger("Hunger", 0);

		return super.shouldCauseBlockBreakReset(stackA, stackB);
	}
}
