package com.cibernet.minestuckuniverse.damage;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import net.minecraft.util.DamageSource;

public class CritDamageSource extends DamageSource implements IGodTierDamage
{
	boolean isCrit = false;
	boolean godproof = false;

	public CritDamageSource(String damageTypeIn)
	{
		super(MinestuckUniverse.MODID+"."+damageTypeIn);
	}

	@Override
	public CritDamageSource setCrit()
	{
		isCrit = true;
		return this;
	}

	@Override
	public boolean isCrit() {
		return isCrit;
	}

	@Override
	public CritDamageSource setGodproof()
	{
		godproof = true;
		return this;
	}

	@Override
	public boolean isGodproof() {
		return godproof;
	}
}
