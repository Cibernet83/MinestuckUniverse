package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class PropertyPotion extends WeaponProperty
{
	public PotionEffect effect;
	public boolean onCrit;
	public float chance;

	public PropertyPotion(PotionEffect effect, boolean onCrit, float chance)
	{
		this.effect = effect;
		this.onCrit = onCrit;
		this.chance = chance;
	}


	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(!onCrit && (player.world.rand.nextFloat() <= chance) && (!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength(((EntityPlayer) player)) >= 1))
			target.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
	}

}
