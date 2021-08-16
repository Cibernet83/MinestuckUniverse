package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class PropertyPlantMend extends WeaponProperty
{
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(!worldIn.isRemote && worldIn.getTotalWorldTime() % 80 == 0 && worldIn.getLightFor(EnumSkyBlock.SKY, entityIn.getPosition()) > 7)
			stack.setItemDamage(stack.getItemDamage()-1);
	}
}
