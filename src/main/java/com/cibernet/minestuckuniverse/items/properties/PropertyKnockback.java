package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class PropertyKnockback extends WeaponProperty
{
	float strength;

	public PropertyKnockback(float strength)
	{
		this.strength = strength;
	}



	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(player instanceof EntityPlayer && CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1)
		{
			Vec3d vec = new Vec3d(player.posX-target.posX, player.posY-target.posY, player.posZ-target.posZ).normalize();

			target.knockBack(player, strength, vec.x, vec.z);
			target.velocityChanged = true;
		}
	}
}
