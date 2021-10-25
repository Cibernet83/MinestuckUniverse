package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public interface IPropertyThrowable
{
	default boolean onProjectileThrow(EntityMSUThrowable projectile, EntityLivingBase thrower, ItemStack stack) {return true;}
	default boolean dropOnImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default boolean onEntityImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default boolean onBlockImpact(EntityMSUThrowable projectile, RayTraceResult result) { return true; }
	default float getGravity(EntityMSUThrowable projectile, float gravity) { return gravity; }
	default void onProjectileUpdate(EntityMSUThrowable projectile) {}
	default void onStatusUpdate(EntityMSUThrowable projectile, byte id) {}
}
