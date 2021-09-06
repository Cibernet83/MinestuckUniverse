package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;

public class PropertyLaserArrow extends WeaponProperty implements IPropertyArrow
{
	private static int arrowToRemove = -1;

	@Override
	public boolean onBlockImpact(EntityMSUArrow arrow, RayTraceResult result)
	{
		arrow.setDead();
		return true;
	}

	@Override
	public boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result)
	{
		if(!result.entityHit.world.isRemote && result.entityHit instanceof EntityLivingBase)
			arrowToRemove = ((EntityLivingBase) result.entityHit).getArrowCountInEntity();
		return true;
	}

	@Override
	public void onEntityImpactPost(EntityMSUArrow arrow, EntityLivingBase entityLivingBase)
	{
		if(entityLivingBase.getArrowCountInEntity()-arrowToRemove == 0)
			entityLivingBase.setArrowCountInEntity(entityLivingBase.getArrowCountInEntity()-1);
		arrowToRemove = -1;
		arrow.setDead();
	}
}
