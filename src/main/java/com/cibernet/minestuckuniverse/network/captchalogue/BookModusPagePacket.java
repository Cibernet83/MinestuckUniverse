package com.cibernet.minestuckuniverse.network.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.BookModus;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class BookModusPagePacket extends MSUPacket
{
	int page;
	
	public MSUPacket generatePacket(Object... args)
	{
		this.data.writeInt((Integer) args[0]);
		
		return this;
	}
	
	public MSUPacket consumePacket(ByteBuf data)
	{
		
		page = data.readInt();
		
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(MinestuckPlayerData.getData(player).modus instanceof BookModus)
			((BookModus) MinestuckPlayerData.getData(player).modus).page = page;
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
