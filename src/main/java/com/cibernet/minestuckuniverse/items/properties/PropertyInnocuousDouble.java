package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class PropertyInnocuousDouble extends WeaponProperty
{
	Item innocuousDouble;
	boolean onSneak;
	boolean dualWielded;
	boolean dualDouble;

	public PropertyInnocuousDouble(Item iDouble, boolean onSneak, boolean dualWielded, boolean dualDouble)
	{
		this.innocuousDouble = iDouble;
		this.onSneak = onSneak;
		this.dualWielded = dualWielded;
		this.dualDouble = dualDouble;
	}

	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		EnumHand oppositeHand = EnumHand.values()[(handIn.ordinal()+1)%EnumHand.values().length];
		if((onSneak && !playerIn.isSneaking()) || (!dualWielded && dualDouble && !playerIn.getHeldItem(oppositeHand).isEmpty())
		|| (dualWielded && !playerIn.getHeldItem(oppositeHand).getItem().equals(playerIn.getHeldItem(handIn).getItem())))
			return super.onItemRightClick(worldIn, playerIn, handIn);

		ItemStack stack = playerIn.getHeldItem(handIn);
		ItemStack newStack = new ItemStack(innocuousDouble);

		if(stack.hasTagCompound())
			newStack.setTagCompound(stack.getTagCompound().copy());

		if(innocuousDouble.isDamageable())
		{
			if(stack.hasTagCompound() && stack.getTagCompound().hasKey("InnocuousDamage"))
				newStack.setItemDamage((int) (stack.getTagCompound().getFloat("InnocuousDamage")*newStack.getMaxDamage()));
			else newStack.setItemDamage(stack.getItemDamage()/stack.getMaxDamage() * newStack.getMaxDamage());
		}
		else if (stack.getItem().isDamageable())
			newStack.getTagCompound().setFloat("InnocuousDamage", stack.getItemDamage()/stack.getMaxDamage());

		playerIn.setHeldItem(handIn, newStack);

		if(dualDouble)
			playerIn.setHeldItem(oppositeHand, newStack.copy());
		else if(dualWielded)
			playerIn.setHeldItem(oppositeHand, ItemStack.EMPTY);

		return EnumActionResult.SUCCESS;
	}
}
