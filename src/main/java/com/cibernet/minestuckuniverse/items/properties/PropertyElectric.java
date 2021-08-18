package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PropertyElectric extends PropertyShock
{
	public PropertyElectric(int stunTime, float stunDamage, float chance, boolean onCrit) {
		super(stunTime, stunDamage, chance, onCrit);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(entityIn instanceof EntityLivingBase && (isSelected || ((EntityLivingBase) entityIn).getHeldItemOffhand().equals(stack)) && entityIn.isInWater() && ((entityIn instanceof EntityPlayer &&
				!((EntityPlayer) entityIn).getCooldownTracker().hasCooldown(stack.getItem()) || ((EntityPlayer) entityIn).getCooldownTracker().getCooldown(stack.getItem(), 0) < 1f)
				|| !(entityIn instanceof EntityPlayer)))
			shockTarget((EntityLivingBase) entityIn, (EntityLivingBase) entityIn, stunTime, stunDamage);
	}
}
