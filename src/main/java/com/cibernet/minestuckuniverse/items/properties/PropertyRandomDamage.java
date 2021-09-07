package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.items.properties.bowkind.IPropertyArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.Random;

public class PropertyRandomDamage extends WeaponProperty implements IPropertyArrow
{
	protected float min;
	protected float max;
	protected float mulitiplier;

	@Override
	public double getAttackDamage(ItemStack stack, double dmg) {
		return super.getAttackDamage(stack, dmg);
	}

	public PropertyRandomDamage()
	{
		this(0, 7, 1);
	}

	public PropertyRandomDamage(float min, float max, float multiplier)
	{
		this.min = min;
		this.max = max;
		this.mulitiplier = multiplier;
	}

	public float getMin()
	{
		return min;
	}

	public float getMax() {
		return max;
	}

	public float getMulitiplier() {
		return mulitiplier;
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		DamageSource source = DamageSource.causeMobDamage(player);
		if(player instanceof EntityPlayer)
			source = DamageSource.causePlayerDamage((EntityPlayer) player);

		Random rand = new Random();
		int dmg = Math.round((rand.nextFloat()*max+min)*mulitiplier);
		target.hurtResistantTime = 0;
		target.attackEntityFrom(source, dmg);
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow, float chargeTime)
	{
		arrow.setDamage(arrow.getDamage() + Math.round((arrow.world.rand.nextFloat()*max+min)*mulitiplier));
		return arrow;
	}
}
