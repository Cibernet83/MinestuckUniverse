package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.strife.StrifePortfolioHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class RetrieveStrifeCardPacket extends MSUPacket
{
	int index;
	boolean isCard;
	EnumHand hand;

	@Override
	public MSUPacket generatePacket(Object... args)
	{
		data.writeInt((int) args[0]);
		data.writeBoolean(args.length <= 1 || (boolean)args[1]);
		data.writeBoolean(args.length <= 2 || args[2] == EnumHand.MAIN_HAND);
		return this;
	}

	@Override
	public MSUPacket consumePacket(ByteBuf data)
	{
		index = data.readInt();
		isCard = data.readBoolean();
		hand = data.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(!MSUConfig.combatOverhaul)
			return;

		if(isCard)
			StrifePortfolioHandler.retrieveCard(player, index);
		else StrifePortfolioHandler.retrieveWeapon(player, index, hand);
		MSUChannelHandler.sendToPlayer(makePacket(Type.UPDATE_STRIFE, player, UpdateStrifeDataPacket.UpdateType.PORTFOLIO), player);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
