package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.util.math.RayTraceResult;

public interface IPropertyProjectile
{
	default boolean dropOnImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default boolean onEntityImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default boolean onBlockImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default void onProjectileUpdate(EntityMSUThrowable projectile) {}
}
