package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class PropertySneaky extends WeaponProperty
{
	float sneakDmg;
	float invisDmg;
	float backstabDmg;

	public PropertySneaky(float sneakMultiplier, float invisMultiplier, float backstabMultiplier)
	{
		this.sneakDmg = sneakMultiplier;
		this.invisDmg = invisMultiplier;
		this.backstabDmg = backstabMultiplier;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		Vec3d vec3d2 = new Vec3d(player.posX, player.posY, player.posZ).subtractReverse(new Vec3d(target.posX, target.posY, target.posZ)).normalize();
		vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

		if (vec3d2.dotProduct(target.getLook(1.0F)) < 0.0D)
			amount *= backstabDmg;
		else if(player.isInvisible())
			amount *= invisDmg;

		if(player.isSneaking())
			amount *= sneakDmg;

		return amount;
	}
}
