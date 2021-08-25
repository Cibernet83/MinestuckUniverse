package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class RetrieveStrifeCardPacket extends MSUPacket
{
	int index;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt((int) args[0]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		index = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		StrifePortfolioHandler.retrieveCard(player, index);
		MSUChannelHandler.sendToPlayer(makePacket(Type.UPDATE_STRIFE, player), player);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
