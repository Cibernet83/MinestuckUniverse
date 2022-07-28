package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.HashchatGuiHandler;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HashchatModus extends ChatModus
{
	public ModusHashFunction hashFunc = new Vowel1Const2Hash();

	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new HashchatGuiHandler(this);
		return gui;
	}

	@Override
	public boolean putItemStack(ItemStack item)
	{
		if(item.isEmpty())
			return false;

		int cardIndex = hashFunc.hash(item.getDisplayName()) % getSize();
		while (list.size() <= cardIndex) list.add(ItemStack.EMPTY);

		if(!list.get(cardIndex).isEmpty())
		{
			ItemStack otherItem = list.get(cardIndex);
			if(otherItem.getItem() == item.getItem() && otherItem.getItemDamage() == item.getItemDamage() && ItemStack.areItemStackTagsEqual(otherItem, item)
			   && otherItem.getCount() + item.getCount() <= otherItem.getMaxStackSize())
			{
				otherItem.grow(item.getCount());
				return true;
			} else CaptchaDeckHandler.launchItem(player, list.get(cardIndex));
		}

		list.set(cardIndex, item);

		if(MinestuckConfig.hashmapChatModusSetting != 2 || MinestuckConfig.hashmapChatModusSetting == 1)
			this.player.sendMessage(new TextComponentTranslation("message.hashmap", item.getTextComponent(), getSize(), cardIndex));

		return true;
	}

	@Override
	public void handleChatSend(String msg)
	{
		NonNullList<ItemStack> items = getItems();

		String asCardCheck = msg;
		for(ItemStack stack : items)
			asCardCheck = asCardCheck.toLowerCase().replace(stack.getDisplayName().toLowerCase(), "");
		boolean asCard = asCardCheck.contains("card");

		boolean ejected = false;
		String noWhitespace = msg.replaceAll("[^a-zA-Z]+", "");
		int lastStartIndex = -1;
		for (int i = 0; i <= noWhitespace.length(); i++)
		{
			if (i < noWhitespace.length() && Character.isUpperCase(noWhitespace.charAt(i)))
			{
				if (lastStartIndex == -1)
				{
					lastStartIndex = i;
				}
			}
			else
			{
				if (lastStartIndex != -1)
				{
					if (i >= lastStartIndex + 3)
					{
						String hashText = noWhitespace.substring(lastStartIndex, i);
						lastStartIndex = -1;
						int cardIndex = hashFunc.hash(hashText) % getSize();
						while (list.size() <= cardIndex) list.add(ItemStack.EMPTY);
						if (!list.get(cardIndex).isEmpty())
						{
							CaptchaDeckHandler.getItem((EntityPlayerMP) player, cardIndex, asCard);
							ejected = true;
						}
					}
					else
					{
						lastStartIndex = -1;
					}
				}
			}
		}

		if(ejected)
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(this)), player);
	}

	@Override
	public void handleChatReceived(String msg) { }

	public static abstract class ModusHashFunction
	{
		protected abstract int[] getHashValues();
		public int hash(String text)
		{
			text = text.toLowerCase().replaceAll("[^a-z]+", "");
			int[] hashValues = getHashValues();
			int hash = 0;
			for (int i = 0; i < text.length(); i++)
				hash += hashValues[text.charAt(i) - 97];
			return hash;
		}
	}

	public static class Vowel1Const2Hash extends ModusHashFunction
	{
		//											 a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z
		private static int[] hashValues = new int[] {1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2};
		@Override
		protected int[] getHashValues()
		{
			return hashValues;
		}
	}
}
