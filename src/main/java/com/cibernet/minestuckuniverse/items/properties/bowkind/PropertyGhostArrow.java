package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.util.math.RayTraceResult;

public class PropertyGhostArrow extends WeaponProperty implements IPropertyArrow
{
	@Override
	public boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result) {
		return false;
	}
}
