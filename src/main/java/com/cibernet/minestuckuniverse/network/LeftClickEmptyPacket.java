package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.events.WeaponAssignedEvent;
import com.cibernet.minestuckuniverse.events.handlers.StrifeEventHandler;
import com.cibernet.minestuckuniverse.items.IPropertyWeapon;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.List;

public class LeftClickEmptyPacket extends MSUPacket
{
	@Override
	public MSUPacket generatePacket(Object... args)
	{
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		boolean checkAssigned = MSUConfig.combatOverhaul && MSUConfig.restrictedStrife;

		if(checkAssigned)
		{
			WeaponAssignedEvent event = new WeaponAssignedEvent(player, stack, StrifeEventHandler.isStackAssigned(stack));
			MinecraftForge.EVENT_BUS.post(event);
			checkAssigned = !event.getCheckResult();
		}

		if(stack.getItem() instanceof IPropertyWeapon && !checkAssigned)
		{
			List<WeaponProperty> propertyList = ((IPropertyWeapon) stack.getItem()).getProperties(stack);
			for(WeaponProperty p : propertyList)
				p.onEmptyHit(stack, player);
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}
}
