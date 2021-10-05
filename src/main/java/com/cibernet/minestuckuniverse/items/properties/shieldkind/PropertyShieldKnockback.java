package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public class PropertyShieldKnockback extends WeaponProperty implements IPropertyShield
{
	float strength;
	boolean onParry;

	public PropertyShieldKnockback(float strength, boolean onParry)
	{
		this.strength = strength;
		this.onParry = onParry;
	}

	@Override
	public void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{
		if(blocked && onParry && source.getImmediateSource() instanceof EntityLivingBase)
		{
			EntityLivingBase target = (EntityLivingBase) source.getImmediateSource();
			if(player instanceof EntityPlayer && CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1)
			{
				Vec3d vec = new Vec3d(player.posX-target.posX, player.posY-target.posY, player.posZ-target.posZ).normalize();

				target.knockBack(player, strength, vec.x, vec.z);
				target.velocityChanged = true;
			}
		}
	}

	@Override
	public boolean onShieldParry(ItemStack stack, EntityLivingBase player, DamageSource source, float damage)
	{
		if(onParry && source.getImmediateSource() instanceof EntityLivingBase)
		{
			EntityLivingBase target = (EntityLivingBase) source.getImmediateSource();
			if(player instanceof EntityPlayer && CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1)
			{
				Vec3d vec = new Vec3d(player.posX-target.posX, player.posY-target.posY, player.posZ-target.posZ).normalize();

				target.knockBack(player, strength, vec.x, vec.z);
				target.velocityChanged = true;
			}
		}
		return true;
	}
}
