package com.cibernet.minestuckuniverse.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionCounter extends MSUPotionBase
{


	private final ArrayList<Potion> counteredPotions = new ArrayList<>();

	protected PotionCounter(boolean isBadEffectIn, int liquidColorIn, String name, Potion... counters)
	{
		super(isBadEffectIn, liquidColorIn, name);

		counteredPotions.addAll(Arrays.asList(counters));
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier)
	{
		for(Potion key : counteredPotions)
			if(entity.getActivePotionEffect(key) != null)
				entity.removePotionEffect(key);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
}
