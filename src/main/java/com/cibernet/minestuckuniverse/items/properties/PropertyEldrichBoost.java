package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PropertyEldrichBoost extends WeaponProperty
{
	@Override
	public void onEntityItemUpdate(EntityItem entityItem)
	{
		ItemStack stack = entityItem.getItem();
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("HeldTime"))
			stack.getTagCompound().setInteger("HeldTime", 0);
	}

	protected static final Potion[] EFFECTS = new Potion[]{MobEffects.NAUSEA, MobEffects.HUNGER, MobEffects.POISON, MobEffects.BLINDNESS, MobEffects.WITHER};

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		if(!worldIn.isRemote)
		{
			if (entityIn instanceof EntityLivingBase && isSelected) {
				if (!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				int heldTime = stack.getTagCompound().getInteger("HeldTime");

				System.out.println(heldTime);

				stack.getTagCompound().setInteger("HeldTime", Math.min(6000, heldTime + 1));

				if (heldTime % 40 == 0 && worldIn.rand.nextInt(12000) < heldTime)
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(EFFECTS[(int) Math.min(EFFECTS.length - 1, Math.round(worldIn.rand.nextFloat()) + heldTime / 5000f * EFFECTS.length)], heldTime / 5 + 100, heldTime / 10));

			} else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("HeldTime"))
				stack.getTagCompound().setInteger("HeldTime", 0);
		}

		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public double getAttackDamage(ItemStack stack, double parent)
	{
		if(stack.hasTagCompound())
			return (stack.getTagCompound().getInteger("HeldTime")/2000f +1) * parent;
		return parent;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		if(!oldStack.hasTagCompound())
			oldStack.setTagCompound(new NBTTagCompound());
		if(!newStack.hasTagCompound())
			newStack.setTagCompound(new NBTTagCompound());

		oldStack.getTagCompound().setInteger("HeldTime", 0);
		newStack.getTagCompound().setInteger("HeldTime", 0);

		return !ItemStack.areItemStacksEqual(oldStack, newStack);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		if(!oldStack.hasTagCompound())
			oldStack.setTagCompound(new NBTTagCompound());
		if(!newStack.hasTagCompound())
			newStack.setTagCompound(new NBTTagCompound());

		oldStack.getTagCompound().setInteger("HeldTime", 0);
		newStack.getTagCompound().setInteger("HeldTime", 0);

		return super.shouldCauseBlockBreakReset(oldStack, newStack);
	}
}
