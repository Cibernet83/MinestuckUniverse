package com.cibernet.minestuckuniverse.items.properties.beams;

import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class PropertyMagicBeam extends WeaponProperty implements IPropertyBeam
{
	@Override
	public DamageSource onEntityImpact(ItemStack stack, Beam beam, Entity entity, DamageSource damageSource)
	{
		return damageSource.setMagicDamage();
	}
}
