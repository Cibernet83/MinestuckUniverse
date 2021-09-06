package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.util.math.RayTraceResult;

public interface IPropertyThrowable
{
	default boolean dropOnImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default boolean onEntityImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default boolean onBlockImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default float getGravity(EntityMSUThrowable projectile, float gravity) { return gravity; }
	default void onProjectileUpdate(EntityMSUThrowable projectile) {}
	default void onStatusUpdate(EntityMSUThrowable projectile, byte id) {}
}
