package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class PropertyGuidedArrow extends WeaponProperty implements IPropertyArrow
{
	@Override
	public boolean onEntityImpact(EntityMSUArrow arrow, RayTraceResult result)
	{
		return result.entityHit != arrow.shootingEntity;
	}

	@Override
	public void onProjectileUpdate(EntityMSUArrow arrow)
	{
		if(arrow.shootingEntity instanceof EntityLivingBase)
		{
			EntityLivingBase shooter = (EntityLivingBase) arrow.shootingEntity;

			if(ItemStack.areItemsEqualIgnoreDurability(arrow.getBowStack(), shooter.getHeldItemMainhand()) ||
					(ItemStack.areItemsEqualIgnoreDurability(arrow.getBowStack(), shooter.getHeldItemOffhand())))
			{
				arrow.rotationPitch = -shooter.rotationPitch;
				arrow.rotationYaw = -(shooter.rotationYaw % 360);

				if(Math.abs(arrow.rotationYaw) > 179)
					arrow.rotationYaw -= Math.signum(arrow.rotationYaw)*360;

				Vec3d lookVec = arrow.getLookVec();
				PropertyHookshot.moveTowards(arrow, 0.2f, -lookVec.x, -lookVec.y, lookVec.z);
			} else if(!arrow.world.isRemote) arrow.setDead();
		}
		else if(!arrow.world.isRemote) arrow.setDead();
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow, float chargeTime)
	{
		if(!arrow.getIsCritical())
			return null;
		arrow.setIsCritical(false);
		return arrow;
	}

	@Override
	public boolean hasGravity(EntityMSUArrow arrow) {
		return false;
	}
}
