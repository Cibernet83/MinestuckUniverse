package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class SwapOffhandStrifePacket extends MSUPacket
{
	int specibusIndex;
	int weaponIndex;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt((int) args[0]);
		data.writeInt((int) args[1]);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		specibusIndex = data.readInt();
		weaponIndex = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(!MSUConfig.combatOverhaul)
			return;

		StrifePortfolioHandler.swapOffhandWeapon(player, specibusIndex, weaponIndex);
		MSUChannelHandler.sendToPlayer(makePacket(Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.PORTFOLIO), player);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
