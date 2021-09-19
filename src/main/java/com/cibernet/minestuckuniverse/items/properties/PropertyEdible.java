package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class PropertyEdible extends WeaponProperty
{
	int amount;
	float saturation;
	int damageTaken;
	IConsumableEffect effect;

	public PropertyEdible(int amount, float saturation, int damageTaken, IConsumableEffect effect)
	{
		this.amount = amount;
		this.saturation = saturation;
		this.damageTaken = damageTaken;
		this.effect = effect;
	}
	public PropertyEdible(int amount, float saturation, int damageTaken)
	{
		this(amount, saturation, damageTaken, ((stack, player) -> {}));
	}

	public PropertyEdible setPotionEffect(float chance, PotionEffect... effects)
	{
		effect = ((stack, player) ->
		{
			effect.consume(stack, player);

			if(player.world.rand.nextFloat() <= chance)
			{
				PotionEffect potion = effects[player.world.rand.nextInt(effects.length)];
				player.addPotionEffect(new PotionEffect(potion.getPotion(), potion.getDuration(), potion.getAmplifier(), potion.getIsAmbient(), potion.doesShowParticles()));
			}
		});
		return this;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack, int duration) {
		return Math.max(duration, 32);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		stack.damageItem(this.damageTaken, entityLiving);
		effect.consume(stack, entityLiving);
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			entityplayer.getFoodStats().addStats(this.amount, this.saturation);
			worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
		}

		return stack;
	}

	interface IConsumableEffect
	{
		void consume(ItemStack stack, EntityLivingBase player);
	}
}
