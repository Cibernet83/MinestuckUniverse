package com.cibernet.minestuckuniverse.modSupport;

import com.cibernet.minestuckuniverse.badges.MSUBadges;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.capabilities.slots.ISlotInfo;
import net.lomeli.trophyslots.capabilities.slots.SlotManager;
import net.lomeli.trophyslots.core.triggers.AllTriggers;
import net.lomeli.trophyslots.repack.kotlin.jvm.internal.Intrinsics;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TrophySlotsSupport
{
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		updateSlots(event.player, true);

	}

	@SubscribeEvent
	public static void onPlayerJoinDim(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		EntityPlayer player = event.player;
		ISlotInfo slotInfo = SlotManager.INSTANCE.getPlayerSlotInfo(player);

		if(!player.world.isRemote && FMLCommonHandler.instance() != null)
			SlotManager.INSTANCE.updateClient(player, slotInfo);
	}


	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.PlayerRespawnEvent event)
	{
		EntityPlayer player = event.player;
		ISlotInfo slotInfo = SlotManager.INSTANCE.getPlayerSlotInfo(player);

		if(!player.world.isRemote && FMLCommonHandler.instance() != null)
			SlotManager.INSTANCE.updateClient(player, slotInfo);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerDeath(LivingDeathEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer))
			return;

		updateSlots((EntityPlayer) event.getEntity(), false);
	}

	protected static void updateSlots(EntityPlayer player, boolean sendMessage)
	{
		ISlotInfo slotInfo = SlotManager.INSTANCE.getPlayerSlotInfo(player);
		if (slotInfo == null)
			Intrinsics.throwNpe();

		Echeladder echeladder = MinestuckPlayerData.getData(player).echeladder;
		int slots = (int) (Math.min(20, echeladder.getRung())*9/20f);

		if(player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.PATCH_OF_THE_HOARDER))
			slots = slotInfo.getMaxSlots();

		int slotsToUnlock = slots-slotInfo.getSlotsUnlocked();
		//slots += TrophySlots.INSTANCE.getProxy().getStartingSlots();

		if(!player.isDead && slotInfo.getSlotsUnlocked() < slots)
		{
			slotInfo.unlockSlot(slotsToUnlock);
			if(sendMessage)
				player.sendStatusMessage(new TextComponentTranslation("status.trophySlotsRung", slots+ TrophySlots.INSTANCE.getProxy().getStartingSlots()), true);

			if(!player.world.isRemote && FMLCommonHandler.instance() != null)
				SlotManager.INSTANCE.updateClient(player, slotInfo);
			if (player instanceof EntityPlayerMP)
				AllTriggers.getUNLOCK_SLOT().trigger((EntityPlayerMP)player);
		}
	}
}
