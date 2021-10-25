package com.cibernet.minestuckuniverse.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is not {@link Cancelable}.<br>
 * this event is called to perform a check on whether you can use an item or not while restrictedStrife is set to true
 */
public class WeaponAssignedEvent extends Event
{
	final EntityLivingBase player;
	final ItemStack stack;
	boolean result = true;

	public WeaponAssignedEvent(EntityLivingBase player, ItemStack stack)
	{
		this.player = player;
		this.stack = stack;
	}

	public EntityLivingBase getPlayer() {
		return player;
	}

	public ItemStack getStack() {
		return stack;
	}

	public boolean getCheckResult() {
		return result;
	}

	public void setCheckResult(boolean result) {
		this.result = result;
	}
}
