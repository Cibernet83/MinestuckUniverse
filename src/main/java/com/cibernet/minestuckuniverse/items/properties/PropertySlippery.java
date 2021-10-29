package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class PropertySlippery extends WeaponProperty
{
	@Override
	public void onEntityHit(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if(attacker.world.isRemote)
		{
			EntityItem sord = new EntityItem(attacker.world, attacker.posX, attacker.posY, attacker.posZ, itemStack.copy());
			sord.getItem().setCount(1);
			sord.setPickupDelay(40);
			attacker.world.spawnEntity(sord);
		}

		itemStack.shrink(1);
	}
}
