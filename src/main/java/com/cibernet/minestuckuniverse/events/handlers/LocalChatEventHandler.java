package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class LocalChatEventHandler
{
	@SubscribeEvent
	public static void onChat(ServerChatEvent event)
	{
		if(!MSUConfig.localizedChat)
			return;

		World world = event.getPlayer().world;
		EntityPlayer player = world.getPlayerEntityByUUID(event.getPlayer().getCommandSenderEntity().getUniqueID());
		boolean canSenderGab = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null) != null && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.GIFT_OF_GAB);

		for(EntityPlayer receiver : world.getMinecraftServer().getPlayerList().getPlayers())
		{
			boolean canReceiverGab = receiver.getCapability(MSUCapabilities.GOD_TIER_DATA, null) != null && receiver.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.GIFT_OF_GAB);

			if(receiver == player || (canSenderGab && canReceiverGab) || (!canSenderGab && player.getDistanceSq(receiver) < 128 && player.world.provider.getDimension() == receiver.world.provider.getDimension()))
				receiver.sendStatusMessage(event.getComponent(), false);
		}

		event.setCanceled(true);
	}


	@SubscribeEvent
	public static void onCommandSent(CommandEvent event)
	{
		if(!MSUConfig.localizedChat)
			return;

		World world = event.getSender().getEntityWorld();
		EntityPlayer player = world.getPlayerEntityByUUID(event.getSender().getCommandSenderEntity().getUniqueID());
		boolean canSenderGab = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null) != null && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.GIFT_OF_GAB);

		if(!canSenderGab && event.getCommand() instanceof CommandEmote)
		{
			TextComponentTranslation msg = new TextComponentTranslation("commands.generic.permission");
			msg.getStyle().setColor(TextFormatting.RED);
			event.getSender().sendMessage(msg);
			event.setCanceled(true);
		}
		else if(event.getCommand() instanceof CommandMessage)
		{
			EntityPlayer receiver;

			try { receiver = CommandBase.getPlayer(event.getSender().getServer(), event.getSender(), event.getParameters()[0]); }
			catch (Throwable throwable) { return; }


			boolean canReceiverGab = receiver.getCapability(MSUCapabilities.GOD_TIER_DATA, null) != null && receiver.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUSkills.GIFT_OF_GAB);

			if(!(receiver == player || (canSenderGab && canReceiverGab) || (!canSenderGab && player.getDistanceSq(receiver) < 128 && player.world.provider.getDimension() == receiver.world.provider.getDimension())))
			{
				TextComponentTranslation msg = new TextComponentTranslation("commands.localizedChat.outOfReach", player.getDisplayName());
				msg.getStyle().setColor(TextFormatting.RED);
				event.getSender().sendMessage(msg);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_CONFIG), event.player);
	}
}
