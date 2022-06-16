package com.cibernet.minestuckuniverse.potions;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class PotionBleeding extends MSUPotionBase
{
	public static final DamageSource DAMAGE_SOURCE = new DamageSource(MinestuckUniverse.MODID+".bleeding").setDamageBypassesArmor();

	protected PotionBleeding(boolean isBadEffectIn, int liquidColorIn, String name)
	{
		super(isBadEffectIn, liquidColorIn, name);
	}

	@Override
	public boolean isReady(int duration, int amplifier)
	{
		int timeBetweenHits = 40;

		return duration % timeBetweenHits == 0;
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		entityLivingBaseIn.attackEntityFrom(DAMAGE_SOURCE, amplifier+1);
	}
}
