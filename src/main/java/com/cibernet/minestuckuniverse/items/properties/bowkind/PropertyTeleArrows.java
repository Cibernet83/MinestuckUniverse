package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUArrow;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;

public class PropertyTeleArrows extends WeaponProperty implements IPropertyArrow
{
	@Override
	public boolean onBlockImpact(EntityMSUArrow arrow, RayTraceResult result) 
	{
		if (!arrow.world.isRemote)
		{
			if (arrow.shootingEntity instanceof EntityPlayerMP)
			{
				EntityPlayerMP entityplayermp = (EntityPlayerMP)arrow.shootingEntity;

				if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == arrow.world && !entityplayermp.isPlayerSleeping())
				{
					net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entityplayermp, arrow.posX, arrow.posY, arrow.posZ, 5.0F);
					if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
					{
						if (entityplayermp.isRiding())
							entityplayermp.dismountRidingEntity();

						entityplayermp.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
						entityplayermp.fallDistance = 0.0F;
						entityplayermp.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
					}
				}
			}
			else if (arrow.shootingEntity != null)
			{
				arrow.shootingEntity.setPositionAndUpdate(arrow.posX, arrow.posY, arrow.posZ);
				arrow.shootingEntity.fallDistance = 0.0F;
			}
		}
		
		return true;
	}
}
