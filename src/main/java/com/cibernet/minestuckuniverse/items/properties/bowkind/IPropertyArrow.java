package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public interface IPropertyArrow
{
	default boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result) { return true; }
	default boolean onBlockImpact(EntityMSUArrow arrow, RayTraceResult result) { return true; }
	default boolean hasGravity(EntityMSUArrow arrow) { return true; }
	default float getArrowVelocity(ItemStack bow, int charge, float vel) { return vel; }
	default int getDrawTime(EntityLivingBase player, ItemStack bow, int time) {return time;}
	default void onProjectileUpdate(EntityMSUArrow arrow) {}
	default void onStatusUpdate(EntityMSUArrow arrow, byte id) {}

	default EntityArrow customizeArrow(EntityArrow arrow, float chargeTime) {return arrow;}

	default void onEntityImpactPost(EntityMSUArrow arrow, EntityLivingBase result) {}
}
