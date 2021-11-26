package com.cibernet.minestuckuniverse.events;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;

/**
 * This event is {@link Cancelable}.<br>
 * The effect will count as successful if the event is cancelled, lowering the player's experience.<br>
 **/
public class TimetableEffectEvent extends Event
{

	@Nullable
	private final EntityLivingBase player;
	@Nullable
	private final Entity target;

	public TimetableEffectEvent(EntityLivingBase player, Entity target)
	{
		this.player = player;
		this.target = target;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	public EntityLivingBase getPlayer()
	{
		return player;
	}

	public Entity getTarget()
	{
		return target;
	}
}
