package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.util.math.RayTraceResult;

public class PropertyBreakableProjectile extends WeaponProperty implements IPropertyThrowable
{
	float chance;

	public PropertyBreakableProjectile(float chance)
	{
		this.chance = chance;
	}

	@Override
	public boolean dropOnImpact(EntityMSUThrowable projectile, RayTraceResult result) {
		return projectile.world.rand.nextFloat() <= chance;
	}
}
