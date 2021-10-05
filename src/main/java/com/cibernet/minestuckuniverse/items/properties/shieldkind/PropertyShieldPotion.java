package com.cibernet.minestuckuniverse.items.properties.shieldkind;

import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class PropertyShieldPotion extends WeaponProperty implements IPropertyShield
{
	public PotionEffect[] effects;
	public boolean onParry;
	public float chance;

	public PropertyShieldPotion(boolean onParry, float chance, PotionEffect... effects)
	{
		this.effects = effects;
		this.onParry = onParry;
		this.chance = chance;
	}

	@Override
	public void onHitWhileShielding(ItemStack stack, EntityLivingBase player, DamageSource source, float damage, boolean blocked)
	{

		if(blocked && !onParry && (player.world.rand.nextFloat() <= chance))
		{
			PotionEffect effect = effects[player.world.rand.nextInt(effects.length)];
			player.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
		}
	}

	@Override
	public boolean onShieldParry(ItemStack stack, EntityLivingBase player, DamageSource source, float damage)
	{
		if(onParry && (player.world.rand.nextFloat() <= chance))
		{
			PotionEffect effect = effects[player.world.rand.nextInt(effects.length)];
			player.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
		}
		return true;
	}
}
