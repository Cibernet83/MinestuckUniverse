package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;

public class PropertyDamagePrjctle implements IPropertyProjectile
{
	float damage;

	public PropertyDamagePrjctle(float damage)
	{
		this.damage = damage;
	}

	@Override
	public boolean onEntityImpact(EntityMSUThrowable throwable, RayTraceResult result)
	{
		result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(throwable, throwable.getThrower()), damage);
		return true;
	}
}
