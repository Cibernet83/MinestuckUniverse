package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class PropertyCrowdDamage extends WeaponProperty
{
	float buffPerEntity;
	float maxBuff;
	double crowdRadius;

	public PropertyCrowdDamage(float buffPerEntity, float maxBuff, double crowdRadius)
	{
		this.buffPerEntity = buffPerEntity;
		this.maxBuff = maxBuff;
		this.crowdRadius = crowdRadius;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		float buff = 1;

		for(EntityLivingBase entity : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(crowdRadius)))
		{
			if(entity == player)
				continue;
			if((buffPerEntity < 1 && buff <= maxBuff) || (buffPerEntity >= 1 && buff >= maxBuff))
			{
				buff = maxBuff;
				break;
			}
			buff += buffPerEntity;
		}

		return amount * (buff+1);
	}
}
