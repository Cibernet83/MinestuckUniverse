package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class PropertyProjectileDeflect extends WeaponProperty
{
	float deflectStrength;
	float deflectRadius;

	public PropertyProjectileDeflect(float strength, float radius)
	{
		this.deflectRadius = radius;
		this.deflectStrength = strength;
	}

	@Override
	public void onEmptyHit(ItemStack stack, EntityPlayer player)
	{
		if(CommonEventHandler.getCooledAttackStrength(player) == 1)
		{
			for(Entity target : player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(deflectRadius)))
			{
				Vec3d vec3d = new Vec3d(target.posX, target.posY, target.posZ);

				Vec3d vec3d1 = player.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(player.posX, player.posY, player.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

				if (vec3d2.dotProduct(vec3d1) < 0.0D)
				{
					Vec3d vec = new Vec3d(player.posX-target.posX, player.posY-target.posY, player.posZ-target.posZ).normalize()
							.scale(Math.sqrt(target.motionX*target.motionX + target.motionY*target.motionY + target.motionZ*target.motionZ) * -deflectStrength);
					target.isAirBorne = true;
					target.motionX = vec.x;
					target.motionY = vec.y;
					target.motionZ = vec.z ;

					if(target instanceof EntityFireball)
					{
						((EntityFireball) target).accelerationX = target.motionX * 0.1D;
						((EntityFireball) target).accelerationY = target.motionY * 0.1D;
						((EntityFireball) target).accelerationZ = target.motionZ * 0.1D;
					}
				}
			}


		}
	}
}
