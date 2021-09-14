package com.cibernet.minestuckuniverse.items.properties.beams;

import com.cibernet.minestuckuniverse.capabilities.beam.Beam;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class PropertyPotionBeam extends WeaponProperty implements IPropertyBeam
{
	PotionEffect potionEffect;

	public PropertyPotionBeam(PotionEffect effect)
	{
		this.potionEffect = effect;
	}

	@Override
	public DamageSource onEntityImpact(ItemStack stack, Beam beam, Entity entity, DamageSource damageSource)
	{
		if(entity instanceof EntityLivingBase)
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(potionEffect.getPotion(), potionEffect.getDuration(), potionEffect.getAmplifier(), potionEffect.getIsAmbient(), potionEffect.doesShowParticles()));
		return damageSource;
	}
}
