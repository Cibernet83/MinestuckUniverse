package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class PorkhollowAtmPacket extends MSUPacket
{
	Type type;
	EntityPlayer reciever;
	int amount;
	int n;
	
	@Override
	public MSUPacket generatePacket(Object... dat)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode((EntityPlayer) dat[1]);
		int n = 1;
		this.data.writeInt(((Type)dat[0]).ordinal());
		this.data.writeInt(identifier.getId());
		this.data.writeInt((int)dat[2]);
		if(dat.length > 3)
			n = Math.max(0,(int)dat[3]);
		this.data.writeInt(n);
		return this;
	}
	
	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		type = Type.values()[data.readInt()];
		reciever = IdentifierHandler.getById(data.readInt()).getPlayer();
		amount = data.readInt();
		n = data.readInt();
		return this;
	}
	
	@Override
	public void execute(EntityPlayer sender)
	{
		if(MinestuckPlayerData.addBoondollars(sender, -amount))
		switch(type)
		{
			case SEND:
				MinestuckPlayerData.addBoondollars(reciever, amount);
				sender.sendMessage(new TextComponentTranslation("message.atm.sendSuccess", amount, reciever));
			break;
			case TAKE:
				int split = 0;
				if(n > 0)
					split = (int) Math.floor(amount/n);
				for(int i = 0; i < n; i++)
					MSUUtils.giveBoonItem(sender, split);
				if(split*n != amount)
					MSUUtils.giveBoonItem(sender, amount-split*n);
				sender.sendMessage(new TextComponentTranslation("message.atm.withdrawSuccess", amount));
			break;
		} else sender.sendMessage(new TextComponentTranslation("commands.porkhollow.notEnough"));
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
	
	
	
	public enum Type
	{
		SEND,
		TAKE
		;
	}
}
