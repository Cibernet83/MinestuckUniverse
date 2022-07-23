package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.ChatGuiHandler;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChatModus extends BaseModus
{
	@Override
	protected boolean getSort()
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new ChatGuiHandler(this);
		return gui;
	}

	public void ejectByChat(int index, boolean asCard)
	{
		if(side.isServer())
		{
			if(index == CaptchaDeckHandler.EMPTY_SYLLADEX)
			{
				getItem(CaptchaDeckHandler.EMPTY_SYLLADEX, false);
				return;
			}
			
			if(index >= 0 && index < list.size())
				CaptchaDeckHandler.launchAnyItem(player, getItem(index, asCard));
		}
		else
		{
			getItem(index, false);
			gui.updateContent();
		}
	}

	public void handleChatSend(String msg)
	{
		if(msg.toLowerCase().contains("@everyone"))
		{
			player.world.playSound(null, player.getPosition(), MSUSoundHandler.chatModusPing, SoundCategory.PLAYERS, 0.8f, 1);
			ejectByChat(CaptchaDeckHandler.EMPTY_SYLLADEX, false);
		}
		if(msg.toLowerCase().contains("@card"))
		{
			player.world.playSound(null, player.getPosition(), MSUSoundHandler.chatModusPing, SoundCategory.PLAYERS, 0.8f, 1);
			ejectByChat(CaptchaDeckHandler.EMPTY_CARD, false);
		}
	}

	public void handleChatReceived(String msg)
	{
		NonNullList<ItemStack> items = getItems();

		String asCardCheck = msg;
		for(ItemStack stack : items)
			asCardCheck = asCardCheck.toLowerCase().replace(stack.getDisplayName().toLowerCase(), "");
		boolean asCard = asCardCheck.contains("card");

		boolean playPing = false;
		for(ItemStack stack : items)
		{
			if(stack.isEmpty())
				continue;

			if(msg.toLowerCase().contains(stack.getDisplayName().toLowerCase()))
			{
				playPing = true;
				ejectByChat(list.indexOf(stack), asCard);
			}
		}

		if(playPing)
		{
			player.world.playSound(null, player.getPosition(), MSUSoundHandler.chatModusPing, SoundCategory.PLAYERS, 0.8f, 1);
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
		}
	}
}
