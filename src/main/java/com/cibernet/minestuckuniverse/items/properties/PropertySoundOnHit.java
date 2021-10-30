package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Consumer;

public class PropertySoundOnHit extends WeaponProperty
{
	SoundEvent[] soundPool;
	Value pitch;
	Value volume;

	public PropertySoundOnHit(SoundEvent[] soundPool, float pitch, float volume)
	{
		this(soundPool, ((stack, target, player) -> pitch), ((stack, target, player) -> volume));
	}

	public PropertySoundOnHit(SoundEvent[] soundPool, Value pitch, Value volume)
	{
		this.soundPool = soundPool;
		this.pitch = pitch;
		this.volume = volume;
	}

	public PropertySoundOnHit(SoundEvent sound, float pitch, float volume)
	{
		this(new SoundEvent[]{sound}, pitch, volume);
	}

	public PropertySoundOnHit(SoundEvent sound, Value pitch, Value volume)
	{
		this(new SoundEvent[]{sound}, pitch, volume);
	}

	@Override
	public void onEntityHit(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		super.onEntityHit(stack, target, player);
		if(!(player instanceof EntityPlayer) || CommonEventHandler.getCooledAttackStrength((EntityPlayer) player) >= 0.9f )
			player.world.playSound(null, player.posX, player.posY, player.posZ, soundPool[player.getRNG().nextInt(soundPool.length)], SoundCategory.PLAYERS, volume.consume(stack, target, player), pitch.consume(stack, target, player));
	}

	public interface Value
	{
		float consume(ItemStack stack, EntityLivingBase target, EntityLivingBase player);
	}
}
