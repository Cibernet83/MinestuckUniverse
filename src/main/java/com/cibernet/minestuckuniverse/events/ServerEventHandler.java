package com.cibernet.minestuckuniverse.events;

import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ServerEventHandler
{
	@SubscribeEvent
	public static void onTick(TickEvent.PlayerTickEvent event)
	{
		if(event.player.isPotionActive(MSUPotions.SKYHBOUND) || (event.player.isCreative() && event.player.isPotionActive(MSUPotions.SKYHBOUND) && event.player.getActivePotionEffect(MSUPotions.EARTHBOUND).getDuration() < 5))
			event.player.capabilities.allowFlying = true;
		if(event.player.isPotionActive(MSUPotions.EARTHBOUND) || (!event.player.isCreative() && event.player.isPotionActive(MSUPotions.SKYHBOUND) && event.player.getActivePotionEffect(MSUPotions.SKYHBOUND).getDuration() < 5))
		{
			event.player.capabilities.allowFlying = false;
			event.player.capabilities.isFlying = false;
		}

		if(event.player.isSpectator())
		{
			event.player.capabilities.isFlying = true;
			event.player.capabilities.allowFlying = true;
		}
	}

	@SideOnly(Side.CLIENT)
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(Minecraft.getMinecraft().player == null)
			return;

		EntityPlayer player = Minecraft.getMinecraft().player;

		if(player.isPotionActive(MSUPotions.SKYHBOUND) || (player.isCreative() && player.isPotionActive(MSUPotions.SKYHBOUND) && player.getActivePotionEffect(MSUPotions.EARTHBOUND).getDuration() < 5))
			player.capabilities.allowFlying = true;
		if(player.isPotionActive(MSUPotions.EARTHBOUND) || (!player.isCreative() && player.isPotionActive(MSUPotions.SKYHBOUND) && player.getActivePotionEffect(MSUPotions.SKYHBOUND).getDuration() < 5))
		{
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
		}

		if(player.isSpectator())
		{
			player.capabilities.isFlying = true;
			player.capabilities.allowFlying = true;
		}
	}

	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if(event.getEntityPlayer().isPotionActive(MSUPotions.CREATIVE_SHOCK))
			event.setNewSpeed(0);
	}

	@SubscribeEvent
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		//TODO find better alternative, maybe look into FTB utils code?
		if(event.getEntity() instanceof EntityLivingBase && ((EntityLivingBase) event.getEntity()).isPotionActive(MSUPotions.CREATIVE_SHOCK))
			event.setCanceled(true);
	}
}
