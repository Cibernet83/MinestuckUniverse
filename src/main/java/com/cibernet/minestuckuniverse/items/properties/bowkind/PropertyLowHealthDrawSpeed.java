package com.cibernet.minestuckuniverse.items.properties.bowkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class PropertyLowHealthDrawSpeed extends WeaponProperty implements IPropertyArrow
{
	float health;
	float maxModifier;

	public PropertyLowHealthDrawSpeed(float health, float maxModifier)
	{
		this.health = health;
		this.maxModifier = maxModifier;
	}

	@Override
	public int getDrawTime(EntityLivingBase player, ItemStack bow, int time)
	{
		if(player.getHealth() > player.getMaxHealth()*health)
			return time;
		return (int) (lerp(1, maxModifier, (1-player.getHealth()/(player.getMaxHealth()*health)))*time);
	}

	static float lerp(float a, float b, float n)
	{
		if(a > b)
			return b + (a-b)*(1-n);
		return a + (b-a)*n;
	}
}
