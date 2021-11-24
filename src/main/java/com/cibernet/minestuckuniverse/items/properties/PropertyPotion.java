package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class PropertyPotion extends WeaponProperty
{
	public PotionEffect[] effects;
	public boolean onCrit;
	public float chance;

	public PropertyPotion(PotionEffect effect, boolean onCrit, float chance)
	{
		this(onCrit, chance, effect);
	}

	public PropertyPotion(boolean onCrit, float chance, PotionEffect... effects)
	{
		this.effects = effects;
		this.onCrit = onCrit;
		this.chance = chance;
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(!player.world.isRemote && !onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
		{
			PotionEffect effect = effects[player.world.rand.nextInt(effects.length)];
			target.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
		}
	}

	@Override
	public float onCrit(ItemStack stack, EntityPlayer player, EntityLivingBase target, float damageModifier)
	{
		if(!player.world.isRemote && onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(player) >= 1))
		{
			PotionEffect effect = effects[player.world.rand.nextInt(effects.length)];
			target.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
		}
		return super.onCrit(stack, player, target, damageModifier);
	}
}
