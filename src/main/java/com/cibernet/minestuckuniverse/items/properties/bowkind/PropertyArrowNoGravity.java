package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;

public class PropertyArrowNoGravity extends WeaponProperty implements IPropertyArrow
{
	@Override
	public boolean hasGravity(EntityMSUArrow arrow) {
		return false;
	}
}
