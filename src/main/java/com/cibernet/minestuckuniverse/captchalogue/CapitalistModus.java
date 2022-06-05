package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.CapitalistGuiHandler;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

public class CapitalistModus extends BaseModus
{
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(asCard)
			return super.getItem(id, asCard);
		
		MinestuckPlayerData.PlayerData playerData = MinestuckPlayerData.getData(player);
		
		int price = 0;
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ItemStack stack : list)
				price += getItemPrice(stack);
			
			price *= 0.8f;
			
			if(playerData.boondollars >= price)
			{
				playerData.boondollars -= price;
				if(!player.world.isRemote)
					MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, playerData.boondollars), player);
				player.sendStatusMessage(new TextComponentTranslation("status.capitalistBuyAll", price), false);
				return super.getItem(id, asCard);
			}
			player.sendStatusMessage(new TextComponentTranslation("status.capitalistDeniedAll", price-playerData.boondollars), false);
			return ItemStack.EMPTY;
		}
		
		if(id > list.size())
			return ItemStack.EMPTY;
		
		ItemStack stack = list.get(id);
		if(list.isEmpty() || stack.isEmpty())
			return ItemStack.EMPTY;
		
		price = getItemPrice(stack);
		
		if(playerData.boondollars >= price)
		{
			playerData.boondollars -= price;
			if(!player.world.isRemote)
				MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, playerData.boondollars), player);
			player.sendStatusMessage(new TextComponentTranslation("status.capitalistBuy", price, stack.getTextComponent()), false);
			return super.getItem(id, asCard);
		}
		player.sendStatusMessage(new TextComponentTranslation("status.capitalistDenied", stack.getTextComponent()), false);
		return ItemStack.EMPTY;
	}
	
	@Override
	protected boolean getSort()
	{
		return false;
	}
	
	public static int getItemPrice(ItemStack stack)
	{
		int cost = Math.max(1,(int) (((float)stack.getCount()/(float)Math.max(stack.getMaxStackSize(),16))*20));
		GristSet gristConversion = GristRegistry.getGristConversion(stack);
		
		int gristCost = 0;
		if(gristConversion != null)
		for(Map.Entry<GristType, Integer> grist : gristConversion.gristTypes.entrySet())
			gristCost += Math.abs(grist.getValue()*grist.getKey().getValue());
		
		return cost*gristCost;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new CapitalistGuiHandler(this);
		return gui;
	}
}
