package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;

public class PropertyDamagePrjctle extends WeaponProperty implements IPropertyThrowable
{
	float damage;
	boolean multiTarget;

	public PropertyDamagePrjctle(float damage, boolean multiTarget)
	{
		this.damage = damage;
		this.multiTarget = multiTarget;
	}

	public PropertyDamagePrjctle(float damage)
	{
		this(damage, false);
	}

	@Override
	public boolean onBlockImpact(EntityMSUThrowable projectile, RayTraceResult result)
	{
		if(multiTarget)
		{
			for(Entity target : projectile.world.getEntitiesWithinAABB(Entity.class, projectile.getEntityBoundingBox().grow(1)))
				if(!target.equals(projectile.getThrower()))
					target.attackEntityFrom(DamageSource.causeIndirectDamage(projectile, projectile.getThrower()), damage);
		}
		return true;
	}

	@Override
	public boolean onEntityImpact(EntityMSUThrowable throwable, RayTraceResult result)
	{
		if(multiTarget)
		{
			for(Entity target : throwable.world.getEntitiesWithinAABB(Entity.class, throwable.getEntityBoundingBox().grow(1)))
				if(!target.equals(throwable.getThrower()))
					target.attackEntityFrom(DamageSource.causeIndirectDamage(throwable, throwable.getThrower()), damage);
		} else result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(throwable, throwable.getThrower()), damage);
		return true;
	}
}
