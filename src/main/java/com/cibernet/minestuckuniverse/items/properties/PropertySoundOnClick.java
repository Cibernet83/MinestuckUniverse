package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class PropertySoundOnClick extends WeaponProperty
{
	SoundEvent[] soundPool;
	Value pitch;
	Value volume;

	public PropertySoundOnClick(SoundEvent[] soundPool, float pitch, float volume)
	{
		this(soundPool, ((stack, player) -> pitch), ((stack, player) -> volume));
	}

	public PropertySoundOnClick(SoundEvent[] soundPool, Value pitch, Value volume)
	{
		this.soundPool = soundPool;
		this.pitch = pitch;
		this.volume = volume;
	}

	public PropertySoundOnClick(SoundEvent sound, float pitch, float volume)
	{
		this(new SoundEvent[]{sound}, pitch, volume);
	}

	public PropertySoundOnClick(SoundEvent sound, Value pitch, Value volume)
	{
		this(new SoundEvent[]{sound}, pitch, volume);
	}

	@Override
	public EnumActionResult onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
	{
		player.swingArm(handIn);
		ItemStack stack = player.getHeldItem(handIn);
		player.world.playSound(null, player.posX, player.posY, player.posZ, soundPool[player.getRNG().nextInt(soundPool.length)], SoundCategory.PLAYERS, volume.consume(stack, player), pitch.consume(stack, player));
		return EnumActionResult.SUCCESS;
	}

	public interface Value
	{
		float consume(ItemStack stack, EntityLivingBase player);
	}
}
