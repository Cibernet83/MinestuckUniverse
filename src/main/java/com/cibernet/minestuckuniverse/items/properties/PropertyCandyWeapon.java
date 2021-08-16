package com.cibernet.minestuckuniverse.items.properties;

import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class PropertyCandyWeapon extends WeaponProperty
{
	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player) {
		if(target instanceof EntityUnderling)
			((EntityUnderling) target).dropCandy = true;
	}
}
