package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.gui.GuiEditMode;
import com.mraof.minestuck.client.gui.playerStats.GuiInventoryEditmode;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EditModeEventHandler
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiInventoryEditmode)
			event.setGui(new GuiEditMode());
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(isInEditMode(event.player))
		{
		}
	}

	public static boolean isInEditMode(EntityPlayer player)
	{
		return FMLCommonHandler.instance().getSide() == Side.CLIENT ? ClientEditHandler.isActive() : ServerEditHandler.getData(player) != null;
	}
}
