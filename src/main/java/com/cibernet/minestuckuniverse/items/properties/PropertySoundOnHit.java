package com.cibernet.minestuckuniverse.items.properties;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Consumer;

public class PropertySoundOnHit extends WeaponProperty
{
	SoundEvent sound;
	Value pitch;
	Value volume;

	public PropertySoundOnHit(SoundEvent sound, float pitch, float volume)
	{
		this(sound, ((stack, target, player) -> pitch), ((stack, target, player) -> volume));
	}

	public PropertySoundOnHit(SoundEvent sound, Value pitch, Value volume)
	{
		this.sound = sound;
		this.pitch = pitch;
		this.volume = volume;
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		super.onEntityHit(stack, target, player);
		player.world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, volume.consume(stack, target, player), pitch.consume(stack, target, player));
	}

	public interface Value
	{
		float consume(ItemStack stack, EntityLivingBase target, EntityLivingBase player);
	}
}
