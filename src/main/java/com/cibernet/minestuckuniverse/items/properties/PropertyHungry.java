package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;

public class PropertyHungry extends WeaponProperty
{
	boolean onCrit;
	int amount;
	float damageMultiplier;

	public PropertyHungry(float damageMultiplier, int hungerUsed, boolean onCrit)
	{
		this.amount = hungerUsed;
		this.damageMultiplier = damageMultiplier;
		this.onCrit = onCrit;
	}

	@Override
	public float damageAgainstEntity(ItemStack stack, EntityLivingBase player, EntityLivingBase target, float amount)
	{
		float scale = 1;
		if(!onCrit && (player instanceof EntityPlayer) && CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1)
		{
			FoodStats foodStats = ((EntityPlayer) player).getFoodStats();
			if(foodStats.getFoodLevel() >= this.amount)
			{
				scale = damageMultiplier;
				foodStats.setFoodLevel(foodStats.getFoodLevel()-this.amount);
			}
		}
		return super.damageAgainstEntity(stack, player, target, amount) * scale;
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{
		float scale = onCrit ? 1 : super.onCrit(stack, player, target, damageModifier);
		if(onCrit && CommonEventHandler.getCooledAttackStrength(player) >= 1)
		{
			FoodStats foodStats = player.getFoodStats();
			if(foodStats.getFoodLevel() >= this.amount)
			{
				scale = damageMultiplier;
				foodStats.setFoodLevel(foodStats.getFoodLevel()-this.amount);
			}
		}
		return scale;
	}
}
