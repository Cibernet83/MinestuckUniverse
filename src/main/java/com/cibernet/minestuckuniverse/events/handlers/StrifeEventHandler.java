package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.gui.GuiStrifePortfolio;
import com.mraof.minestuck.client.gui.playerStats.GuiStrifeSpecibus;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StrifeEventHandler
{

	//TODO config

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiStrifeSpecibus)
			event.setGui(new GuiStrifePortfolio());
	}

	@SubscribeEvent
	public static void onPlayerAttack(LivingAttackEvent event)
	{
		ItemStack stack = event.getEntityLiving().getHeldItemMainhand();
		//TODO assigned check
	}
}
