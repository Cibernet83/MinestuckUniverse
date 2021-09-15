package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;

public class PropertyThrowGravity extends WeaponProperty implements IPropertyThrowable
{
	float gravity;

	public PropertyThrowGravity(float gravity)
	{
		this.gravity = gravity;
	}

	@Override
	public float getGravity(EntityMSUThrowable projectile, float gravity)
	{
		return this.gravity*gravity;
	}
}
