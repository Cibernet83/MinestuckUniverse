package com.cibernet.minestuckuniverse.items.properties;

import com.cibernet.minestuckuniverse.events.handlers.CommonEventHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.cibernet.minestuckuniverse.items.properties.PropertySoundOnHit.Value;

public class PropertySoundOnClick extends WeaponProperty
{
	SoundEvent[] soundPool;
	Value pitch;
	Value volume;
	boolean onBlockClick;

	public PropertySoundOnClick(SoundEvent[] soundPool, float pitch, float volume)
	{
		this(soundPool, (stack, target, player) -> pitch, (stack, target, player) -> volume);
	}

	public PropertySoundOnClick(SoundEvent[] soundPool, Value pitch, Value volume, boolean onBlockClick)
	{
		this.soundPool = soundPool;
		this.pitch = pitch;
		this.volume = volume;
		this.onBlockClick = onBlockClick;
	}

	public PropertySoundOnClick(SoundEvent[] soundPool, Value pitch, Value volume)
	{
		this(soundPool, pitch, volume, false);
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
		if(onBlockClick)
			return super.onItemRightClick(worldIn, player, handIn);

		player.swingArm(handIn);
		ItemStack stack = player.getHeldItem(handIn);
		player.world.playSound(null, player.posX, player.posY, player.posZ, soundPool[player.getRNG().nextInt(soundPool.length)], SoundCategory.PLAYERS, volume.consume(stack, null, player), pitch.consume(stack, null, player));
		return EnumActionResult.SUCCESS;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(onBlockClick && !player.isSneaking())
		{
			player.swingArm(hand);
			ItemStack stack = player.getHeldItem(hand);
			player.world.playSound(null, player.posX, player.posY, player.posZ, soundPool[player.getRNG().nextInt(soundPool.length)], SoundCategory.PLAYERS, volume.consume(stack, null, player), pitch.consume(stack, null, player));
			return EnumActionResult.SUCCESS;

		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

}
