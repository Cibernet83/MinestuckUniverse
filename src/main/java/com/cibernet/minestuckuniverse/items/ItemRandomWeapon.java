package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.Random;

public class ItemRandomWeapon extends MSUWeaponBase
{
	protected float min = 0;
	protected float max = 7;
	protected float mulitiplier = 1;

	@Override
	public double getAttackDamage(ItemStack stack) {
		return super.getAttackDamage(stack);
	}

	public ItemRandomWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
	}
	
	public ItemRandomWeapon setRandoms(float min, float max, float multiplier)
	{
		this.min = min;
		this.max = max;
		this.mulitiplier = multiplier;
		return this;
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
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		DamageSource source = DamageSource.causeMobDamage(player);
		if(player instanceof EntityPlayer)
			source = DamageSource.causePlayerDamage((EntityPlayer) player);
		
		Random rand = new Random();
		int dmg = Math.round((rand.nextFloat()*max+min)*mulitiplier);
		target.attackEntityFrom(source, dmg);
		
		return super.hitEntity(stack, target, player);
	}
}
