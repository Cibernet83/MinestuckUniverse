package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;

public class PropertyArrowNoGravity extends WeaponProperty implements IPropertyArrow
{
	@Override
	public void onProjectileUpdate(EntityMSUArrow arrow)
	{
		if(Math.abs(arrow.motionX)+Math.abs(arrow.motionY)+Math.abs(arrow.motionZ) < 0.01f)
			arrow.setDead();
	}

	@Override
	public boolean hasGravity(EntityMSUArrow arrow) {
		return false;
	}
}
